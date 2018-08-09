package com.buddhadata.sandbox.neo4j.baseball;

import com.buddhadata.sandbox.neo4j.baseball.functions.*;
import com.buddhadata.sandbox.neo4j.baseball.relationship.TxnBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by scsosna on 5/19/18.
 */
public class BaseballTransactions {

    /**
     * Session factory for connecting to Neo4j database
     */
    private final SessionFactory sessionFactory;

    /**
     * The base URL used for fetching pages.
     */
    private URL BASE_URL = null;

    /**
     * Collection of all known functions which may process/consume a transaction
     */
    private static final List<Map.Entry<Pattern, BaseFunction>> consumers;
    static {
        consumers = new ArrayList<>();
        register (DraftedByFunction.INSTANCE);
        register (DraftedFromFunction.INSTANCE);
        register (FreeAgentFunction.INSTANCE);
        register (PurchasesFunction.INSTANCE);
        register (ReleasesFunction.INSTANCE);
        register (RetiredFunction.INSTANCE);
        register (SignsFunction.INSTANCE);
        register (TradedFunction.INSTANCE);
        register (UnknownFunction.INSTANCE);
        register (WaiversFunction.INSTANCE);
    }

    /**
     * Registers a function as a potential transaction consumer
     * @param function function instance to register
     */
    private static void register (BaseFunction function) {
        consumers.add (new AbstractMap.SimpleEntry<> (function.getRegexPattern(), function));
    }

    /**
     * Use for converting the text date into a usable date.
     */
    private static final SimpleDateFormat[] dateFormats = {
        new SimpleDateFormat("MMM d, yyyy"),
        new SimpleDateFormat ("MMM, yyyy")
    };

    /**
     * the HTML tag for list items, which is one per transaction date
     */
    private static final String HTML_TAG_BY_DAY_BREAKOUT = "li";

    /**
     * the HTML tag in which the transaction date can be found.
     */
    private static final String HTML_TAG_TXN_DATE = "span";

    /**
     * Within a single li item, each transaction is a separate <p>
     */
    private static final String HTML_TAG_TXNS_PER_DAY = "p";

    /**
     * Base URL from which all yearly transactional pages are derived
     */
    private static final String BBREF_BASE_URL = "https://www.baseball-reference.com/leagues/MLB";

    //  Configuration info for connecting to the Neo4J database
    static private final String SERVER_URI = "bolt://localhost";
    static private final String SERVER_USERNAME = "neo4j";
    static private final String SERVER_PASSWORD = "password";

    /**
     * the relative URL for all Wikipedia topics.
     */
    static private final String BBREF_TXN_URL = "%d-transactions.shtml";

    private int countUnprocessed = 0;
    private int countTransactions = 0;


    /**
     * Main method for running the program
     * @param args command line arguments
     */
    public static void main (String[] args) {

        new BaseballTransactions().process();

    }

    /**
     * Constructor
     */
    public BaseballTransactions() {

        try {
            BASE_URL = new URL(BBREF_BASE_URL);
        } catch (MalformedURLException e) {
            System.out.println ("Exception creating URL: " + e);
        }

        //  Define session factory for connecting to Neo4j database
        Configuration configuration = new Configuration.Builder().uri(SERVER_URI).credentials(SERVER_USERNAME, SERVER_PASSWORD).build();
        sessionFactory = new SessionFactory(configuration, "com.buddhadata.sandbox.neo4j.baseball.node", "com.buddhadata.sandbox.neo4j.baseball.relationship");
    }


    /**
     * Use JSoup to get the requested transaction page into a usable Document.
     * @param season the season for which transactions are being retrieved
     * @return the JSoup document
     */
    private Document getTxnPageForSeason(final int season) {

        Document toReturn = null;
        try {
            toReturn = Jsoup.parse (
                    Thread.currentThread().getContextClassLoader().getResourceAsStream(String.format(BBREF_TXN_URL, season)),
                    "UTF-8",
                    BBREF_BASE_URL);
        } catch (IOException e) {
            System.out.println ("Exception retrieving page: " + e);
        }


        return toReturn;
    }

    private Date parseTxnDate(String date) {

        for (SimpleDateFormat one : dateFormats) {
            try {
                return one.parse (date);
            } catch (ParseException pe) {
                //  Catch but ignore exception, go on to next available format
            }
        }

        //  Nothing parsed, so return null.
        return null;
    }

    private void process() {

        //  When creating a session, always clean up the database by purging the database.
        Session session = sessionFactory.openSession();
        session.purgeDatabase();
        session.query("CREATE CONSTRAINT ON (player:Player) ASSERT player.url IS UNIQUE", Collections.EMPTY_MAP);

        //  Fetch/processSeason all the transaction from previous full season to 1901
        for (int season = 1901; season < 2018; season++) {
            processSeason(season, session);
        }

        System.out.println (countUnprocessed + "/" + countTransactions + " unprocessed.");
    }

    /**
     * Method for doing the work
     * @param season for which transactions are being retrieved.
     */
    private void processSeason(int season,
                               Session session) {

        System.out.println ("Processing season " + season);
        Transaction txn = null;
        try {
            //  New transaction for each season
            txn = session.beginTransaction();

            //  For the given season, retrieve the transactions from baseball-reference.com
            Document doc = getTxnPageForSeason(season);

            //  Get the transactional section of the document
            Element element = doc.getElementById("div_trxs");

            //  Each list tag is a specific day to processSeason.
            element.getElementsByTag(HTML_TAG_BY_DAY_BREAKOUT).forEach (item -> processDate(item, session));

            txn.commit();
            txn = null;
        } catch (Throwable t) {
            System.out.println("Something bad happened: " + t);
            t.printStackTrace();
        } finally {
            if (txn != null) {
                txn.rollback();
            }
        }
    }


    private void processDate(Element oneDate,
                             Session session) {

        //  The date is in the <span> tag, should only be one of them, turn into date field.
        Date txnDate = parseTxnDate(oneDate.getElementsByTag(HTML_TAG_TXN_DATE).first().text());

        //  Process the transactions for the date.
        processTransactions (oneDate, txnDate, session);
    }


    private void processTransactions (Element oneDate,
                                      Date txnDate,
                                      Session session) {

        //  The individual transaction are individual <p> within each <li> element (what's passed in)
        List<TxnBase> created = new ArrayList<>(20);
        oneDate.getElementsByTag(HTML_TAG_TXNS_PER_DAY).forEach (one -> {

            //  Iterate through known patterns looking for a match.  Could be multiple matches, such as
            //  for a purchase where there's a selling and buying team each with their own transaction part
            int before = created.size();
            for (Map.Entry<Pattern,BaseFunction> function : consumers) {
                if (function.getKey().matcher(one.text()).matches()) {
                    created.addAll (function.getValue().apply(one, session));
                }
            }

            //  For debugging, if the countUnprocessed hasn't changed, nothing generated and we need to look at the transaction
            countTransactions++;
            if (created.size() == before) {
                System.out.println (one.text());
                countUnprocessed++;
            }
        });

        created.forEach (one -> {
            one.setTransactionDate (txnDate);
            session.save(one);
        });
    }
}

