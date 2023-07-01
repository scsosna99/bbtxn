package com.buddhadata.sandbox.neo4j.baseball;

import com.buddhadata.sandbox.neo4j.baseball.node.BatThrowEnum;
import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.PlayerBio;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.processor.FromToTransactionProcessor;
import com.buddhadata.sandbox.neo4j.baseball.processor.FromTransactionProcessor;
import com.buddhadata.sandbox.neo4j.baseball.processor.ToTransactionProcessor;
import com.buddhadata.sandbox.neo4j.baseball.relationship.TransactionType;
import com.buddhadata.sandbox.neo4j.baseball.relationship.TxnBase;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;


/**
 * Loads baseball transactions of interest into a Neo4J database
 */
public class BaseballTransactions {

    /**
     * Session factory for connecting to Neo4j database
     */
    private final SessionFactory sessionFactory;

    /**
     * Classpath resource containing bio information for all known players for whom transactions exist.
     */
    private static final String RESOURCE_NAME_BIO = "biofile.txt";

    /**
     * Classpath resource containing all known players for whom transactions exist.
     */
    private static final String RESOURCE_NAME_PLAYERS = "players.txt";

    /**
     * Classpath resource containing all top-level teams/franchises of major leagues (i.e., not containing moves)
     */
    private static final String RESOURCE_NAME_TEAMS = "teams.txt";

    /**
     * Classpath resource containing all transactional data to process.
     */
    private static final String RESOURCE_NAME_TRANSACTIONS = "txnsmod.txt";

    /**
     * Neo4J Connection Information
     */
    static private final String SERVER_URI = "bolt://localhost";
    static private final String SERVER_USERNAME = "neo4j";
    static private final String SERVER_PASSWORD = "password";


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

        //  Define session factory for connecting to Neo4j database
        Configuration configuration = new Configuration.Builder().uri(SERVER_URI).credentials(SERVER_USERNAME, SERVER_PASSWORD).build();
        sessionFactory = new SessionFactory(configuration, "com.buddhadata.sandbox.neo4j.baseball.node", "com.buddhadata.sandbox.neo4j.baseball.relationship");
    }


    private void loadPlayerBios (Session session) {

        //  Load all players in its own transaction
        try (Transaction txn = session.beginTransaction();

             // Each line contains one player
             BufferedReader br = new BufferedReader (new InputStreamReader(
                     Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_NAME_BIO)))) {

            //  Process line by line
            while (br.ready()) {
                //  No need to check for already-existing player, assumes we've just cleared the database and are
                //  loading players before doing anything else.
                PlayerBio pb = PlayerBio.fromRaw(br.readLine());
                if (pb != null) {
                    session.save(pb);
                }
            }

            //  Commit players to the database.
            txn.commit();
        } catch (IOException ioe) {
            System.out.println ("Exception reading player data: " + ioe);
        }

    }


    /**
     * Pre-load all the players inro the database.
     * @param session connection/session to the Neo4J database
     */
    private void loadPlayers (Session session) {

        //  Load all players in its own transaction
        try (Transaction txn = session.beginTransaction();

             // Each line contains one player
             BufferedReader br = new BufferedReader (new InputStreamReader(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_NAME_PLAYERS)))) {

            //  Process line by line
            while (br.ready()) {
                Player p = Player.fromRaw(br.readLine());
                if (p != null) {
                    session.save(p);
                }
            }

            //  Commit players to the database.
            txn.commit();
        } catch (IOException ioe) {
            System.out.println ("Exception reading player data: " + ioe);
        }
    }

    /**
     * Loads classpath resource comtaining all known franchises for the three major leagues since the modern age of
     * baseball.  The nodes are considered teams because other, non-major league teams will be included in some transaction
     * which we'll want to track.
     * @param session active connection to Neo4J database
     */
    private void loadTeams (Session session) {

        //  Load all teams in its own transaction
        try (Transaction txn = session.beginTransaction();

            //  Each line contains one team
            BufferedReader br = new BufferedReader (new InputStreamReader(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_NAME_TEAMS)))) {

            //  Process line by line
            while (br.ready()) {
                //  No need to check for already-existing team, assumes we've just cleared the database and are
                //  loading teams before doing any transaction processing.
                Team t = Team.fromRaw(br.readLine());
                session.save(t);
            }

            //  Commit all teams to the database
            txn.commit();
        } catch (IOException ioe) {
            System.out.println ("Exception reading team data: " + ioe);
        }
    }


    /**
     * Process all the transactions into the database.
     * @param session active connection to Neo4J database
     */
    private void loadTransactions (Session session) {

        int count = 0;
        Transaction txn = session.beginTransaction();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                     Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_NAME_TRANSACTIONS)))) {

            // Each line contains one transaction, process line by line.
            while (br.ready()) {

                //  Every n transactions, commit the data to the database.
                if (++count % 1000 == 0) {
                    txn.commit();
                    txn = session.beginTransaction();
                }

                TxnBase.fromRaw(session, br.readLine());
            }

            //  Do a final commit on the transactions in the last batch
            txn.commit();
        } catch (RuntimeException rte) {
            if (!rte.getMessage().endsWith("of realtionship may not be null")) {
                System.out.println("Exception on record " + count + ":" + rte);
            }
        } catch (Exception e) {
            System.out.println("Exception on record " + count + ":" + e);
        }
    }

    /**
     * Process all the data and create a graph out of it.
     */
    private void process() {

        //  When creating a session, always clean up the database by purging the database.
        Session session = sessionFactory.openSession();
        session.purgeDatabase();
        try {
            session.query("CREATE CONSTRAINT FOR (player:Player) REQUIRE player.retrosheetId IS UNIQUE", Collections.EMPTY_MAP);
        } catch (Exception e) {
            System.out.println ("Constraint on Player already exists.");
        }

        //  Load all known players from retrosheet: https://www.retrosheet.org/retroID.htm
        loadPlayerBios(session);

        //  Load all known teams from retrosheet data (though munged together to create franchises and not just teams).
        loadTeams(session);

        //  process all the transactions, loading them into the database based on type.
        loadTransactions(session);
    }
}

