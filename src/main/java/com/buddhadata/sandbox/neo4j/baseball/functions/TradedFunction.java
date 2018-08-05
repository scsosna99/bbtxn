package com.buddhadata.sandbox.neo4j.baseball.functions;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.neo4j.ogm.session.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by scsosna on 7/19/18.
 */
public class TradedFunction extends BaseFunction {

    /**
     * Where the team is found when separating trade into two constituent parts.
     */
    private static int TEAM_POSITION_INDEX = 1;

    /**
     * Regex expression used to identify when this function should be used.
     */
    private static String regex = "^(The ).+( traded ).+( to the ).+( for ).+[\\.]";

    /**
     * The phrase " to the " separates the first team and the player the team is trading from the other team/players
     */
    private static String separator = " to the ";

    /**
     * Singleton instance
     */
    public static BaseFunction INSTANCE = new TradedFunction(regex);


    /**
     * Private constructor to prevent instantiation.
     */
    private TradedFunction(String regex) {
        super (regex);
    };

    /**
     * Create transaction indicating that a player has been released from the team
     * @param element HTTP element with the player/team information
     * @param session Neo4J session
     * @return Released transaction
     */
    public List<TxnBase> apply (Element element,
                                Session session) {

        List<TxnBase> toReturn;
        if (!element.text().contains("to be named")) {
            List<Node> children = element.childNodes();

            //  Separate the nodes into source/destination bucket
            int separatorPos = findSeparator(children);

            // breakout players from each chunk of the trade.
            List<Player> sourcePlayers = new ArrayList<>(6);
            Team sourceTeam = breakoutTradePieces(children.subList(0, separatorPos), sourcePlayers, session);
            List<Player> destPlayers = new ArrayList<>(6);
            Team destTeam = breakoutTradePieces(children.subList(separatorPos, children.size() - 1), destPlayers, session);

            //  Create the trade for each player found, first with source/originating
            toReturn = new ArrayList<>((sourcePlayers.size() + destPlayers.size()));
            for (Player one : sourcePlayers) {
                toReturn.add(new TradedFromTxn(sourceTeam, one, null));
                toReturn.add(new TradedToTxn(one, destTeam, null));
            }

            //  Now for the destination team, for each player found
            for (Player one : destPlayers) {
                toReturn.add(new TradedFromTxn(destTeam, one, null));
                toReturn.add(new TradedToTxn(one, sourceTeam, null));
            }
        } else {
            toReturn = Collections.emptyList();
        }


        return toReturn;
    }

    /**
     * Helper method to find the two pieces of the trade, source team/players from destination team/players.
     * @param children Html nodes from the transaction being processed
     * @return the array index where the separator can be found or -1 if not found.
     */
    private int findSeparator (final List<Node> children) {

        int toReturn = -1;
        for (Node one : children) {
            toReturn++;
            if (one instanceof TextNode && ((TextNode) one).text().contains(separator)) {
                return toReturn;
            }
        }


        return -1;
    }

    private Team breakoutTradePieces (final List<Node> nodes,
                                      final List<Player> players,
                                      final Session session) {

        //  When the nodes are split into parts of the trade, the team is always position 1.
        Team toReturn = findOrCreateTeam(((Element) nodes.get(TEAM_POSITION_INDEX)), session);

        //  Loop through the remainder of the nodes looking for players.
        for (int index = TEAM_POSITION_INDEX + 2; index < nodes.size(); index++) {
            Node one = nodes.get(index);
            if (one instanceof Element) {
                players.add(findOrCreatePlayer((Element) one, session));
            }
        }


        return toReturn;
    }
}
