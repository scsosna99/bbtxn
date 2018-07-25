package com.buddhadata.sandbox.neo4j.baseball.functions;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.TxnBase;
import org.jsoup.nodes.Element;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.session.Session;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * Created by scsosna on 7/19/18.
 */
abstract public class BaseFunction implements BiFunction<Element, Session, List<TxnBase>> {

    /**
     * Pattern used to match a string with the function to process.
     */
    private final Pattern regexPattern;

    /**
     * Constructor
     * @param regex
     */
    protected BaseFunction (String regex) {
        regexPattern = Pattern.compile (regex);
    }

    /**
     * Find an existing node for the team or create a new one
     * @param elem the HTML element containing the link to the team
     * @param session Neo4J session
     * @return existing or newly-created node
     */
    protected Team findOrCreateTeam (final Element elem,
                                     final Session session) {
        Team toReturn;

        //  Create a filter based on the team's URI and attempt to load from the database
        String href = elem.attributes().get("href").split("/")[2].trim();
        Filter filter = new Filter ("url", ComparisonOperator.EQUALS, href);
        Collection<Team> nodes = session.loadAll (Team.class, filter);
        if (nodes != null && !nodes.isEmpty()) {
            //  Neo4J OGM usually returns an ArrayList, but just in case it ever changes
            if (nodes instanceof List) {
                toReturn = ((List<Team>) nodes).get(0);
            } else {
                toReturn = nodes.toArray(new Team[nodes.size()])[0];
            }
        } else {
            toReturn = new Team (elem.childNodes().get(0).toString(),  href);
            session.save (toReturn);
        }


        return toReturn;

    }


    /**
     * Find an existing node for the player or create a new one
     * @param elem the HTML element containing the link to the player
     * @param session Neo4J session
     * @return existing or newly-created node
     */
    protected Player findOrCreatePlayer (final Element elem,
                                         final Session session) {
        Player toReturn;

        //  Create a filter based on the player's URI and attempt to load from the database
        String href = elem.attributes().get("href").split("/")[3].trim();
        Filter filter = new Filter ("url", ComparisonOperator.EQUALS, href);
        Collection<Player> nodes = session.loadAll (Player.class, filter);
        if (nodes != null && !nodes.isEmpty()) {
            //  Neo4J OGM usually returns an ArrayList, but just in case it ever changes
            if (nodes instanceof List) {
                toReturn = ((List<Player>) nodes).get(0);
            } else {
                toReturn = nodes.toArray(new Player[nodes.size()])[0];
            }
        } else {
            toReturn = new Player (elem.childNodes().get(0).toString(),  href);
            session.save (toReturn);
        }


        return toReturn;
    }

    /**
     * getter
     * @return compiled regex expression.
     */
    public Pattern getRegexPattern () {
        return regexPattern;
    }
}
