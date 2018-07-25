package com.buddhadata.sandbox.neo4j.baseball.functions;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.PurchaseTxn;
import com.buddhadata.sandbox.neo4j.baseball.relationship.SelectedTxn;
import com.buddhadata.sandbox.neo4j.baseball.relationship.TxnBase;
import com.buddhadata.sandbox.neo4j.baseball.relationship.WaivedTxn;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.neo4j.ogm.session.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by scsosna on 7/19/18.
 */
public class WaiversFunction extends BaseFunction {

    /**
     * Expected location of the player link in the array of children nodes
     */
    private static int PLAYER_NODE_INDEX = 3;

    /**
     * Expected location of the purchasing team link in the array of children nodes.
     */
    private static int WAIVING_TEAM_NODE_INDEX = 1;

    /**
     * Expected location of the selling team link in the array of children nodes
     */
    private static int SELECTING_TEAM_NODE_INDEX = 5;

    /**
     * Regex expression used to identify when this function should be used.
     */
    private static String regex = "^(The ).+( selected ).+( off waivers from the ).+[\\.]";

    /**
     * Singleton instance
     */
    public static BaseFunction INSTANCE = new WaiversFunction(regex);


    /**
     * Private constructor to prevent instantiation.
     */
    private WaiversFunction(String regex) {
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

        List<TxnBase> toReturn = new ArrayList<>(2);
        List<Node> children = element.childNodes();
        if (children.size() == 7) {
            Player player = findOrCreatePlayer((Element) children.get(PLAYER_NODE_INDEX), session);
            Team waivingTeam = findOrCreateTeam((Element) children.get(WAIVING_TEAM_NODE_INDEX), session);
            toReturn.add (new WaivedTxn(waivingTeam, player, null));
            Team selectingTeam = findOrCreateTeam ((Element) children.get(SELECTING_TEAM_NODE_INDEX), session);
            toReturn.add (new SelectedTxn(player, selectingTeam, null));
        } else {
            //  TODO: fish through the nodes and try and figure out what you have.
        }

        return toReturn;
    }
}
