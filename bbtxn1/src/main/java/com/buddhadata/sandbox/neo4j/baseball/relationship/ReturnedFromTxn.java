package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Represents the player being return by the team specified
 */
@RelationshipEntity(type = "RETURNED_FROM")
public class ReturnedFromTxn extends FromTxn {

    public ReturnedFromTxn(TransactionType transactionType,
                           int retrosheetId,
                           Player player,
                           Team from,
                           LocalDateTime transactionDate) {
        super(transactionType, retrosheetId, player, from, transactionDate);
    }
}
