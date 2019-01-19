package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Neo4J relationship representing a player jumping to a team
 */
@RelationshipEntity(type = "JUMPED")
public class JumpedTxn extends FromToTxn {

    /**
     * Constructor
     */
    public JumpedTxn() {
        super();
    }

    public JumpedTxn(TransactionType transactionType,
                     int retrosheetId,
                     Player player,
                     Team fromTeam,
                     Team toTeam,
                     LocalDateTime transactionDate) {
        super (transactionType, retrosheetId, player, fromTeam, toTeam, transactionDate);
    }
}
