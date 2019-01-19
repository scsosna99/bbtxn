package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Neo4J relationship representing a player being assigned to a team
 */
@RelationshipEntity(type = "VOIDED")
public class VoidedTxn extends FromToTxn {

    /**
     * Constructor
     */
    public VoidedTxn() {
        super();
    }

    public VoidedTxn(TransactionType transactionType,
                     int retrosheetId,
                     Player player,
                     Team fromTeam,
                     Team toTeam,
                     LocalDateTime transactionDate) {
        super (transactionType, retrosheetId, player, fromTeam, toTeam, transactionDate);
    }
}
