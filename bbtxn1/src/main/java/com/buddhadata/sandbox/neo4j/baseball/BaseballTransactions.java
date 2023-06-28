package com.buddhadata.sandbox.neo4j.baseball;

import com.buddhadata.sandbox.neo4j.baseball.node.BatThrowEnum;
import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.PlayerBio;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.processor.FromToTransactionProcessor;
import com.buddhadata.sandbox.neo4j.baseball.processor.FromTransactionProcessor;
import com.buddhadata.sandbox.neo4j.baseball.processor.ToTransactionProcessor;
import com.buddhadata.sandbox.neo4j.baseball.relationship.TransactionType;
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
     * Date format for debut of player, manager, coach, and umpire, ready for parsing
     */
    private static DateTimeFormatter DATE_FORMAT_PLAYER = DateTimeFormatter.ofPattern ("M/d/yyyy HH:mm");

    /**
     * What to look for to determine that player is in the Hall of Fame.
     */
    private static String HALL_OF_FAME = "HOF";

    /**
     * Field position for the players' raw data, separated by commas
     * https://www.retrosheet.org/retroID.htm
     */
    private static int PLAYER_DEBUT_COACH = 5;
    private static int PLAYER_DEBUT_MANAGER = 4;
    private static int PLAYER_DEBUT_PLAYER = 3;
    private static int PLAYER_DEBUT_UMPIRE = 6;
    private static int PLAYER_NAME_FIRST = 2;
    private static int PLAYER_NAME_LAST = 1;
    private static int PLAYER_RETROSHEET_ID = 0;

    /**
     * Field position for the players' bio data, separated by columns
     * https://www.retrosheet.org/BIOFILE.TXT
     */
    private static int BIO_BATS = 20;
    private static int BIO_BAT_CHANGE = 31;
    private static int BIO_BIRTH_CITY = 5;
    private static int BIO_BIRTH_COUNTRY = 7;
    private static int BIO_BIRTH_DATE = 4;
    private static int BIO_BIRTH_NAME = 29;
    private static int BIO_BIRTH_STATE = 6;
    private static int BIO_CEMETARY_CITY = 25;
    private static int BIO_CEMETARY_COUNTRY = 27;
    private static int BIO_CEMETARY_NAME = 24;
    private static int BIO_CEMETARY_NOTE = 28;
    private static int BIO_CEMETARY_STATE = 26;
    private static int BIO_COACH_DEBUT = 12;
    private static int BIO_COACH_RETIRE = 13;
    private static int BIO_DEATH_CITY = 17;
    private static int BIO_DEATH_COUNTRY = 19;
    private static int BIO_DEATH_DATE = 16;
    private static int BIO_DEATH_STATE = 18;
    private static int BIO_HALL_OF_FAME = 32;
    private static int BIO_HEIGHT = 22;
    private static int BIO_MANAGER_DEBUT = 10;
    private static int BIO_MANAGER_RETIRE = 11;
    private static int BIO_NAME_FIRST = 2;
    private static int BIO_NAME_CHANGE = 30;
    private static int BIO_NAME_LAST = 1;
    private static int BIO_NAME_NICK = 3;
    private static int BIO_PLAYER_DEBUT = 8;
    private static int BIO_PLAYER_RETIRE = 9;
    private static int BIO_RETROSHEET_ID = 0;
    private static int BIO_THROWS = 21;
    private static int BIO_UMPIRE_DEBUT = 14;
    private static int BIO_UMPIRE_RETIRE = 15;
    private static int BIO_WEIGHT = 23;

    /**
     * Field position for the teams' raw data, separated by commas
     */
    private static int TEAM_CITY = 2;
    private static int TEAM_END_DATE = 5;
    private static int TEAM_LEAGUE = 1;
    private static int TEAM_NAME = 3;
    private static int TEAM_RETROSHEET_ID = 0;
    private static int TEAM_START_DATE = 4;

    /**
     * Field position for the transactions' raw data, separated by commas
     */
    public static int TXN_APPROXIMATE = 2;
    public static int TXN_APPROXIMATE_SECONDARY = 4;
    public static int TXN_DATE_PRIMARY = 0;
    public static int TXN_DATE_SECONDARY = 3;
    public static int TXN_DRAFT_PICK = 14;
    public static int TXN_DRAFT_RND = 13;
    public static int TXN_DRAFT_TYPE = 12;
    public static int TXN_FROM_LEAGUE = 9;
    public static int TXN_FROM_TEAM = 8;
    public static int TXN_INFO = 15;
    public static int TXN_PLAYER = 6;
    public static int TXN_RETROSHEET_ID = 5;
    public static int TXN_TIME = 1;
    public static int TXN_TO_LEAGUE = 11;
    public static int TXN_TO_TEAM = 10;
    public static int TXN_TYPE = 7;


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
                String[] fields = br.readLine().split(",", -1);
                unquoteFields(fields);
                LocalDateTime debut = parsePlayerDate(fields[BIO_PLAYER_DEBUT]);
                if (debut != null) {
                    PlayerBio p = new PlayerBio();
                    p.setRetrosheetId(fields[BIO_RETROSHEET_ID]);
                    p.setLastName(fields[BIO_NAME_LAST]);
                    p.setFirstName(fields[BIO_NAME_FIRST]);
                    p.setPlayerDebut(debut);
                    p.setPlayerRetire(parsePlayerDate(fields[BIO_PLAYER_RETIRE]));
                    p.setHallOfFame(HALL_OF_FAME.equals(fields[BIO_HALL_OF_FAME]));
                    p.setPlayerBats(parseBatThrows(fields[BIO_BATS]));
                    p.setPlayerThrows(parseBatThrows(fields[BIO_THROWS]));
                    p.setHeightInches(parseHeight(fields[BIO_HEIGHT]));
                    p.setWeightLbs(parseWeight(fields[BIO_WEIGHT]));
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
                //  No need to check for already-existing player, assumes we've just cleared the database and are
                //  loading players before doing anything else.
                String[] fields = br.readLine().split(",", -1);
                unquoteFields(fields);
                LocalDateTime debut = parsePlayerDate(fields[PLAYER_DEBUT_PLAYER]);
                if (debut != null) {
                    Player p = new Player();
                    p.setRetrosheetId(fields[PLAYER_RETROSHEET_ID]);
                    p.setLastName(fields[PLAYER_NAME_LAST]);
                    p.setFirstName(fields[PLAYER_NAME_FIRST]);
                    p.setPlayerDebut(debut);
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
                String[] fields = br.readLine().split(",", -1);
                unquoteFields(fields);
                Team t = new Team (fields[TEAM_RETROSHEET_ID], fields[TEAM_NAME],
                    fields[TEAM_CITY], fields[TEAM_LEAGUE]);
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

                String[] fields = br.readLine().split(",", -1);
                unquoteFields (fields);

                //  Determine what transaction type we're dealing with.
                String txnType = fields[TXN_TYPE];
                if (txnType != null && !txnType.isEmpty()) {
                    TransactionType type = TransactionType.valueOf(fields[TXN_TYPE]);
                    switch (type) {
                        case A:  // assigned from one team to another without compensation
                        case C:  // conditional deal
                        case Cr: // returned to original team after conditional deal
                        case D:  // rule 5 draft pick
                        case Df: // first year draft pick
                        case Dm: // minor league draft pick
                        case Dr: // returned to original team after draft selection
                        case Ds: // special draft pick
                        case Fc: // free agent compensation pick
                        case J:  // jumped teams
                        case Jr: // returned to original team after jumping
                        case L:  // loaned to another team
                        case Lr: // returned to original team after loan
                        case M:  // obtained rights when entering into working agreement with minor league team
                        case Mr: // rights returned when working agreement with minor league team ended
                        case P:  // purchase
                        case Pr: // returned to original team after purchase
                        case Pv: // purchase voided
                        case T:  // traded from one team to another team
                        case Tn: // traded but refused to report
                        case Tp: // added to trade (usually because one of the original players refused to report or retired)
                        case Tr: // returned to original team after trade
                        case Tv: // trade voided
                        case U:  // unknown (could have been two separate transactions)
                        case Wr: // returned to original team after waiver pick
                        case W:  // waiver pick
                        case Wf: // first year waiver pick
                        case Wv: // waiver pick voided
                        case X:  // expansion draft
                        case Xe: // premium phase of expansion draft
                        case Xm: // either the 1960 AL minor league expansion draft or the premium phase of the 1961 NL draft
                        case Xp: // added as expansion pick at a later date
                        case Xr: // returned to original team after expansion draft
                            FromToTransactionProcessor.instance.process(session, type, fields);
                            break;

                        case Fg: // free agent granted
                        case Fv: // free agent signing voided
                        case R:  // released
                        case Z:  // voluntarily retired
                            FromTransactionProcessor.instance.process(session, type, fields);
                            break;

                        case Da: // amateur draft pick
                        case F:  // free agent signing
                        case Fa: // amateur free agent signing
                        case Fb: // amateur free agent "bonus baby" signing under the 1953-57 rule requiring player to stay on ML roster
                        case Fo: // free agent signing with first ML team
                        case Zr: // returned from voluntarily retired list
                            ToTransactionProcessor.instance.process(session, type, fields);
                            break;

                        //  Intentionally not processing
                        case Dn: // selected in amateur draft but did not sign
                        case Dv: // amateur draft pick voided
                            break;

                        //  Don't expect any transactions of these types
                        case Hb:  // went on the bereavement list
                        case Hbr: // came off the bereavement list
                        case Hd:  // declared ineligible
                        case Hdr: // reinistated from the ineligible list
                        case Hf:  // demoted to the minor league
                        case Hfr: // promoted from the minor league
                        case Hh:  // held out
                        case Hhr: // ended hold out
                        case Hi:  // went on the disabled list
                        case Hir: // came off the disabled list
                        case Hm:  // went into military service
                        case Hmr: // returned from military service
                        case Hs:  // suspended
                        case Hsr: // reinstated after a suspension
                        case Hu:  // unavailable but not on DL
                        case Hur: // returned from being unavailable
                        case Hv:  // voluntarity retired
                        case Hvr: // unretired
                        case Vg: // player assigned to league control
                        case V:  // player purchased or assigned to team from league
                            System.out.println("Unexpected transaction type: " + type);
                            break;

                        default:
                            System.out.println("Unknown transaction type: " + type);
                    }
                }
            }

            //  Do a final commit on the transactions in the last batch
            txn.commit();
        } catch (Exception e) {
            System.out.println("Exception on record " + count + ":" + e);
        }
    }

    /**
     * convert the bat/throws from a string representation in the bio data into an enum
     * @param batsThrows
     * @return
     */
    private BatThrowEnum parseBatThrows (String batsThrows) {
        if (batsThrows != null && batsThrows.trim().length() > 0) {
            switch (batsThrows.toUpperCase().charAt(0)) {
                case 'R':
                    return BatThrowEnum.RIGHT;
                case 'L':
                    return BatThrowEnum.LEFT;
                case 'B':
                    return BatThrowEnum.BOTH;
                default:
                    return BatThrowEnum.UNKNOWN;
            }
        } else {
            return BatThrowEnum.UNKNOWN;
        }
    }

    /**
     * A player's height is represented as "ft-in" and we'll convert it into the number of inches.
     * @param height
     * @return
     */
    private Integer parseHeight (String height) {

        Integer toReturn = null;
        if (height != null && height.trim().length() > 0) {
            String[] fields = height.split("-");
            try {
                toReturn = Integer.valueOf(fields[0]) * 12;
                if (fields.length > 1) {
                    toReturn += Integer.valueOf(fields[1]);
                }
            } catch (NumberFormatException e) {
                // the value provided must not be an integer as expected
                toReturn = null;
            }
        }


        return toReturn;
    }

    /**
     * Parse a string representing the debut date of the player
     * @param date the string representation in the raw data
     * @return Date object that can stored.
     */
    private LocalDateTime parsePlayerDate (String date) {

        if (date != null && !date.isEmpty()) {
            try {
                return LocalDateTime.parse(date + " 00:00", DATE_FORMAT_PLAYER);
            } catch (DateTimeParseException pe) {
                System.out.println ("Unable to parse player date: " + date);
            }
        }


        //  If no initial string was provided or the parsing failed, return null.
        return null;
    }

    private Integer parseWeight (String weight) {

        if (weight != null && weight.length() > 0) {
            try {
                return Integer.valueOf(weight);
            } catch (NumberFormatException e) {
                //  invalid weight string that can't be turned into a number.
                return null;
            }
        }

        return null;
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
        loadPlayers(session);

        //  Load all known teams from retrosheet data (though munged together to create franchises and not just teams).
        loadTeams(session);

        //  process all the transactions, loading them into the database based on type.
        loadTransactions(session);
    }

    /**
     * Remove all double-quotes from fields.
     * @param fields array of fields from parsed raw data.
     */
    private void unquoteFields (String[] fields) {

        for (int i = 0; i < fields.length; i++) {
            String tmp = fields[i];
            if (tmp != null && !tmp.isEmpty() && tmp.startsWith("\"")) {
                fields[i] = tmp.replace("\"", "").trim();
            } else {
                fields[i] = tmp.trim();
            }
        }
    }
}

