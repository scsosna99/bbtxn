package com.buddhadata.sandbox.neo4j.baseball.functions;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.PurchaseTxn;
import com.buddhadata.sandbox.neo4j.baseball.relationship.SellTxn;
import com.buddhadata.sandbox.neo4j.baseball.relationship.SigningTxn;
import com.buddhadata.sandbox.neo4j.baseball.relationship.TxnBase;
import org.apache.tools.ant.taskdefs.Concat;
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
public class PurchasesFunction extends BaseFunction {

    /**
     * Expected location of the player link in the array of children nodes
     */
    private static int PLAYER_NODE_INDEX = 3;

    /**
     * Expected location of the purchasing team link in the array of children nodes.
     */
    private static int PURCHASE_TEAM_NODE_INDEX = 1;

    /**
     * Expected location of the selling team link in the array of children nodes
     */
    private static int SELL_TEAM_NODE_INDEX = 5;

    /**
     * Regex expression used to identify when this function should be used.
     */
    private static String regex = "^(The )?.+( purchased ).+( from the | from ).+[\\.]";

    /**
     * Singleton instance
     */
    public static BaseFunction INSTANCE = new PurchasesFunction(regex);


    /**
     * Private constructor to prevent instantiation.
     */
    private PurchasesFunction(String regex) {
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
        Player player;
        Team purchasingTeam;
        Team sellingTeam;

        switch (children.size()) {

            //  When there are seven nodes, the selling and buying team were both major league clubs
            case 7:
                player = findOrCreatePlayer((Element) children.get(PLAYER_NODE_INDEX), session);
                purchasingTeam = findOrCreateTeam((Element) children.get(PURCHASE_TEAM_NODE_INDEX), session);
                sellingTeam = findOrCreateTeam((Element) children.get(SELL_TEAM_NODE_INDEX), session);
                toReturn.add (new SellTxn(sellingTeam, player, null));
                toReturn.add (new PurchaseTxn(player, purchasingTeam, null));
                break;

            // When five nodes, one side of the transaction was not a major league club, for example a Mexican
            // or minor league team.
            case 5:

                //  The easiest way to analyze what the components look like.  If the third node (array element 2) is
                //  a text element containing "purchased" then the major league club did the purchase.
                if (children.get(2) instanceof TextNode && ((TextNode) children.get(2)).text().equals (" purchased ")) {
                    player = findOrCreatePlayer((Element) children.get(PLAYER_NODE_INDEX), session);
                    purchasingTeam = findOrCreateTeam((Element) children.get(PURCHASE_TEAM_NODE_INDEX), session);
                    toReturn.add (new PurchaseTxn(player, purchasingTeam, null));
                } else {
                    player = findOrCreatePlayer((Element) children.get(1), session);
                    sellingTeam = findOrCreateTeam((Element) children.get(3), session);
                    toReturn.add (new SellTxn(sellingTeam, player, null));
                }
                break;

            default:
                toReturn = Collections.emptyList();
                break;
        }

        return toReturn;
    }
}
