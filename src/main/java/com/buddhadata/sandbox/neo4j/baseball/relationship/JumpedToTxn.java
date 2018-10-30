package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Date;

/**
 * Neo4J relationship representing a player jumping to a team
 */
@RelationshipEntity(type = "JUMPED_TO")
public class JumpedToTxn extends ToTxn {

    /**
     * Constructor
     */
    public JumpedToTxn() {
        super();
    }

    public JumpedToTxn(int retrosheetId,
                       Player player,
                       Team team,
                       Date transactionDate) {
        super (retrosheetId, player, team, transactionDate);
    }
}
