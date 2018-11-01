package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDate;
import java.util.Date;

/**
 * Represents the player being dealt conditionally from the team specified
 */
@RelationshipEntity(type = "CONDITIONAL_FROM")
public class ConditionalFromTxn extends FromTxn {

    public ConditionalFromTxn(TransactionType transactionType,
                              int retrosheetId,
                              Player player,
                              Team from,
                              LocalDate transactionDate) {
        super(transactionType, retrosheetId, player, from, transactionDate);
    }
}
