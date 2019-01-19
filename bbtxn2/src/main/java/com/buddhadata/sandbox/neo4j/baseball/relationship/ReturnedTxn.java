package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Represents a player being returned to his original team, such as a loan or purchase review
 */
@RelationshipEntity(type = "RETURNED")
public class ReturnedTxn extends FromToTxn {

    /**
     * Constructor
     */
    public ReturnedTxn() {
        super();
    }

    public ReturnedTxn(TransactionType transactionType,
                       int retrosheetId,
                       Player player,
                       Team fromTeam,
                       Team toTeam,
                       LocalDateTime transactionDate) {
        super (transactionType, retrosheetId, player, fromTeam, toTeam, transactionDate);
    }
}
