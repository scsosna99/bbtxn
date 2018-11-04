package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Represents a player being returned to his original team, such as a loan or purchase review
 */
@RelationshipEntity(type = "RETURNED_TO")
public class ReturnedToTxn extends ToTxn {

    /**
     * Constructor
     */
    public ReturnedToTxn() {
        super();
    }

    public ReturnedToTxn(TransactionType transactionType,
                         int retrosheetId,
                         Player player,
                         Team team,
                         LocalDateTime transactionDate) {
        super (transactionType, retrosheetId, player, team, transactionDate);
    }
}
