package com.buddhadata.sandbox.neo4j.baseball.processor;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.TransactionType;
import org.neo4j.ogm.cypher.BooleanOperator;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.session.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Created by scsosna on 10/27/18.
 */
abstract public class TransactionProcessor {

    /**
     * Date format for debut of player, manager, coach, and umpire, ready for parsing
     */
    private static final DateTimeFormatter DATE_FORMAT_TXN = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Field position for the transactions' raw data, after separated by commas
     */
    protected static int TXN_FIELD_APPROXIMATE = 2;
    protected static int TXN_FIELD_APPROXIMATE_SECONDARY = 4;
    protected static int TXN_FIELD_DATE_PRIMARY = 0;
    protected static int TXN_FIELD_DATE_SECONDARY = 3;
    protected static int TXN_FIELD_DRAFT_PICK = 14;
    protected static int TXN_FIELD_DRAFT_RND = 13;
    protected static int TXN_FIELD_DRAFT_TYPE = 12;
    protected static int TXN_FIELD_FROM_LEAGUE = 9;
    protected static int TXN_FIELD_FROM_TEAM = 8;
    protected static int TXN_FIELD_ID = 5;
    protected static int TXN_FIELD_INFO = 15;
    protected static int TXN_FIELD_PLAYER = 6;
    protected static int TXN_FIELD_TIME = 1;
    protected static int TXN_FIELD_TO_LEAGUE = 11;
    protected static int TXN_FIELD_TO_TEAM = 10;
    protected static int TXN_FIELD_TYPE = 7;

    /**
     * Cache to map legacy team to current team within a franchise.
     */
    private static final Map<String,String> teamMap = Collections.unmodifiableMap(loadTeamMappings());

    /**
     * Classpath resource containing mapping from legacy team to current team within franchise.
     */
    private static final String RESOURCE_NAME_TEAM_MAPPINGS = "teammap.txt";


    abstract public void process (Session session, TransactionType transactionType, String[] fields);

    /**
     * Find the player identified by the transaction
     * @param session the Neo4J database session
     * @param retrosheetId unique ID from retrosheet
     * @return either a <code>Player</code> node or a <code>null</code> if the player doesn't exist in retrosheet
     */
    protected Player getPlayer (Session session,
                                String retrosheetId) {

        //  The value to return.
        Player toReturn = null;

        //  If the ID has any whitespace, it's likely because the player never played in the major league and therefore
        //  didn't get a retrosheet ID, so just skip.
        if (retrosheetId.indexOf(" ") == -1) {

            //  Create an OGM filter for the provided retrosheetId
            Filter filter = new Filter("retrosheetId", ComparisonOperator.EQUALS, retrosheetId);

            //  Execute the query and see what returns.
            Collection<Player> players = session.loadAll(Player.class, filter);

            //  Since the query is by unique ID, should return one and only one node.
            if (players != null && !players.isEmpty()) {
                toReturn = players.iterator().next();
            }
        }


        return toReturn;
    }

    /**
     * Get the team used in the transaction.  The majority of transactions will involve only the major league teams;
     * however, for others - minor or foreign - a new team node might be required
     * @param session Neo4J database session
     * @param name team's name
     * @param league team's league
     * @return a valid team node
     */
    protected Team getTeam (Session session,
                            String name,
                            String league) {

        //  The team to return
        Team toReturn = null;

        //  Two different scenarios.  If the league name isn't parenthesized, then it's a straight lookup based on
        //  retrosheet ID; otherwise, it's a minor league team that we'll either lookup or add to the database.
        if (!league.startsWith("(")) {
            toReturn = queryTeamByKey(session, name);
        } else {
            //  Normalize (de-parenthesize) the league name
            String temp = league.substring(1, league.length() - 1);

            //  Does the non-major league team already exist?
            toReturn = queryTeamByNameLeague(session, name, temp);
            if (toReturn == null) {
                //  No, so create a new team.
                toReturn = new Team(null, name, null, temp);
                session.save(toReturn);
            }
        }


        return toReturn;
    }

    /**
     * Parse a string representing the transaction date into a Date object
     * @param date the string representation in the raw data
     * @return Date object that can stored.
     */
    protected LocalDate parseTxnDate(final String date) {

        if (date != null && !date.isEmpty()) {

            //  Special cases are zeros for month/day or day when exact transaction date isn't known
            String temp;
            if (date.endsWith("0000")) temp = date.substring(0,4) + "0101";
            else if (date.endsWith("00")) temp = date.substring(0,6) + "01";
            else temp = date;

            try {
                return LocalDate.parse(temp, DATE_FORMAT_TXN);
            } catch (DateTimeParseException pe) {
                System.out.println ("Unable to parse transaction date: " + date);
            }
        }


        //  If no initial string was provided or the parsing failed, return null.
        return null;
    }


    /**
     * Find the team by its PK (retrosheet) ID
     * @param session Neo4J database session
     * @param retrosheetId unique team ID from retrosheet for the major leagues
     * @return should always be a team, though possible to somehow fail.
     */
    private Team queryTeamByKey(final Session session,
                                final String retrosheetId) {

        //  Team to return
        Team toReturn = null;

        //  To maintain a complete franchise list of transactions, it's possible that the retrofit ID for a team
        //  needs to be translated into the ID of the current team representing the franchise.
        String franchiseId = translateTeamToFranchise(retrosheetId);
        if (franchiseId == null) {
            franchiseId = retrosheetId;
        }

        //  Create an OGM filter for the provided retrosheetId
        Filter filter = new Filter("retrosheetId", ComparisonOperator.EQUALS, franchiseId);

        //  Execute the query and see what is returned.
        Collection<Team> teams = session.loadAll(Team.class, filter);

        //  Since the query is by unique ID, should return one and only one node.
        if (teams != null && !teams.isEmpty()) {
            toReturn = teams.iterator().next();
        }


        return toReturn;
    }

    /**
     * Find the team by its team name and league.  This is for the non-major league teams which were
     * involved in a transaction, usually a minor or foreign league
     * @param session Neo4J database session
     * @param teamName name of the team
     * @param leagueName name of the league
     * @return
     */
    private Team queryTeamByNameLeague(final Session session,
                                       final String teamName,
                                       final String leagueName) {

        //  The team to return
        Team toReturn = null;

        //  Create an OGM filter for the team name and league.
        Filters composite = new Filters();
        Filter filter = new Filter ("name", ComparisonOperator.EQUALS, teamName);
        composite.add(filter);
        filter = new Filter("league", ComparisonOperator.EQUALS, leagueName);
        filter.setBooleanOperator(BooleanOperator.AND);
        composite.add(filter);

        //  Execute the query and see what is returned
        Collection<Team> teams = session.loadAll(Team.class, filter);

        //  Since the team/league should be unique, should return one and only one node.
        if (teams != null && !teams.isEmpty()) {
            toReturn = teams.iterator().next();
        }


        return toReturn;
    }

    /**
     * Get the constructor for the given transaction class
     * @param clazz reference to class
     * @return constructor retrieved through reflection.
     */
    public static Constructor getConstructor (Class clazz) {
        try {
            return clazz.getConstructor(TransactionType.class, int.class, Player.class, Team.class, LocalDate.class);
        } catch (NoSuchMethodException nsme) {
            System.out.println ("Exception getting constructor: " + clazz.getName());
            return null;
        }
    }

    /**
     * Loads classpath resource containing mapping from a previous team ID - such as before a team moved - to
     * the current team, maintaining franchise integrity.
     */
    private static Map loadTeamMappings () {

        //  Create the hash map.
        Map<String,String> toReturn = new HashMap<>();

        //  Each line contains a single team-to-franchise mapping
        try (BufferedReader br = new BufferedReader (new InputStreamReader(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_NAME_TEAM_MAPPINGS)))) {

            //  Process line-by-line
            while (br.ready()) {
                String[] fields = br.readLine().split (",");
                toReturn.put(fields[1],fields[0]);
            }
        } catch (IOException ioe) {
            System.out.println ("Exception reading mapping data: " + ioe);
        }


        return toReturn;
    }

    private static String translateTeamToFranchise (String originalId) {
        return teamMap.get(originalId);
    }
}
