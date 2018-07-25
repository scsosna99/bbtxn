package com.buddhadata.sandbox.neo4j.baseball.functions;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.relationship.SigningTxn;
import com.buddhadata.sandbox.neo4j.baseball.relationship.TxnBase;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.neo4j.ogm.session.Session;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by scsosna on 7/19/18.
 */
public class SignsFunction extends BaseFunction {

    /**
     * Expected location of the player link in the array of children nodes
     */
    private static int PLAYER_NODE_INDEX = 3;

    /**
     * Expected location of the team link in the array of children nodes
     */
    private static int TEAM_NODE_INDEX = 1;

    /**
     * Regex expression used to identify when this function should be used.
     */
    private static String regex = "^(The ).+( signed ).+( as a(?:n amateur?)? free agent.)";

    /**
     * Singleton instance
     */
    public static BaseFunction INSTANCE = new SignsFunction(regex);

    /**
     * Private constructor to prevent instantiation.
     */
    private SignsFunction(String regex) {
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

        TxnBase toReturn = null;
        List<Node> children = element.childNodes();
        if (children.size() == 5) {
            Player player = findOrCreatePlayer((Element) children.get(PLAYER_NODE_INDEX), session);
            Team team = findOrCreateTeam((Element) children.get(TEAM_NODE_INDEX), session);
            toReturn = new SigningTxn(player, team, null);
        } else {
            //  TODO: fish through the nodes and try and figure out what you have.
        }

        return toReturn != null ? Collections.singletonList(toReturn) : Collections.emptyList();
    }
}
