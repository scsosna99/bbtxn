package com.buddhadata.sandbox.neo4j.baseball.functions;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.neo4j.ogm.session.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by scsosna on 7/19/18.
 */
public class UnknownFunction extends BaseFunction {

    /**
     * Expected location of the player link in the array of children nodes
     */
    private static int PLAYER_NODE_INDEX = 0;

    /**
     * Expected location of the purchasing team link in the array of children nodes.
     */
    private static int UNKNOWN_TO_TEAM_NODE_INDEX = 4;

    /**
     * Expected location of the selling team link in the array of children nodes
     */
    private static int UNKNOWN_FROM_TEAM_NODE_INDEX = 2;

    /**
     * Regex expression used to identify when this function should be used.
     */
    private static String regex = "^.+( sent from the ).+( to | to the ).+( in an unknown transaction)(.)( \\(Date given is approximate. Exact date is uncertain.\\))?";

    /**
     * Singleton instance
     */
    public static BaseFunction INSTANCE = new UnknownFunction(regex);


    /**
     * Private constructor to prevent instantiation.
     */
    private UnknownFunction(String regex) {
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
        List<Node> children = element.childNodes();

        Player player;
        Team unknownFrom;
        switch (children.size()) {
            case 6:
                player = findOrCreatePlayer((Element) children.get(PLAYER_NODE_INDEX), session);
                Team unknownTo = findOrCreateTeam((Element) children.get(UNKNOWN_TO_TEAM_NODE_INDEX), session);
                unknownFrom = findOrCreateTeam((Element) children.get(UNKNOWN_FROM_TEAM_NODE_INDEX), session);
                toReturn = new ArrayList<>(2);
                toReturn.add(new UnknownFromTxn(unknownFrom, player, null));
                toReturn.add(new UnknownToTxn(player, unknownTo, null));
                break;

            case 4:
                player = findOrCreatePlayer((Element) children.get(PLAYER_NODE_INDEX), session);
                unknownFrom = findOrCreateTeam((Element) children.get(UNKNOWN_FROM_TEAM_NODE_INDEX), session);
                toReturn = Collections.singletonList(new UnknownFromTxn(unknownFrom, player, null));
                break;

            default:
                toReturn = Collections.EMPTY_LIST;
        }

        return toReturn;
    }
}
