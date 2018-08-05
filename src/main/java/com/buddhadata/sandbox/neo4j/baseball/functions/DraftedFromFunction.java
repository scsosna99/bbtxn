package com.buddhadata.sandbox.neo4j.baseball.functions;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.DraftedByTxn;
import com.buddhadata.sandbox.neo4j.baseball.relationship.DraftedFromTxn;
import com.buddhadata.sandbox.neo4j.baseball.relationship.TxnBase;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.neo4j.ogm.session.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by scsosna on 7/19/18.
 */
public class DraftedFromFunction extends BaseFunction {

    /**
     * Expected location of the player link in the array of children nodes
     */
    private static int PLAYER_NODE_INDEX = 3;

    /**
     * Expected location of the purchasing team link in the array of children nodes.
     */
    private static int DRAFTED_BY_TEAM_NODE_INDEX = 1;

    /**
     * Expected location of the selling team link in the array of children nodes
     */
    private static int DRAFTED_FROM_TEAM_NODE_INDEX = 5;

    /**
     * Regex expression used to identify when this function should be used.
     */
    private static String regex = "^(The ).+( drafted ).+( from | from the ).+( minor league| rule 5| expansion)( draft)[\\.]";

    /**
     * Singleton instance
     */
    public static BaseFunction INSTANCE = new DraftedFromFunction(regex);


    /**
     * Private constructor to prevent instantiation.
     */
    private DraftedFromFunction(String regex) {
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

        List<TxnBase> toReturn = null;
        List<Node> children = element.childNodes();

        Player player;
        Team draftedBy;
        switch (children.size()) {
            //  With 7 nodes, there's a from and to team
            case 7:
                player = findOrCreatePlayer((Element) children.get(PLAYER_NODE_INDEX), session);
                draftedBy = findOrCreateTeam((Element) children.get(DRAFTED_BY_TEAM_NODE_INDEX), session);
                Team draftedFrom = findOrCreateTeam((Element) children.get(DRAFTED_FROM_TEAM_NODE_INDEX), session);
                toReturn = new ArrayList<>(2);
                toReturn.add(new DraftedFromTxn(draftedFrom, player, null));
                toReturn.add(new DraftedByTxn(player, draftedBy, null));
                break;
            //  When 5 nodes, you only have the drafing team, the drafted-from team is likely foreign or minor leagues
            case 5:
                player = findOrCreatePlayer((Element) children.get(PLAYER_NODE_INDEX), session);
                draftedBy = findOrCreateTeam((Element) children.get(DRAFTED_BY_TEAM_NODE_INDEX), session);
                toReturn = Collections.singletonList(new DraftedByTxn(player, draftedBy, null));
                break;
            default:
                //  TODO: fish through the nodes and try and figure out what you have.
                System.out.println ("invalid number of nodes.");
                toReturn = Collections.EMPTY_LIST;
        }

        return toReturn;
    }
}
