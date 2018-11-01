package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDate;
import java.util.Date;

/**
 * Represents a unknown transaction for a player leaving a team
 */
@RelationshipEntity(type = "UNKNOWN_FROM")
public class UnknownFromTxn extends FromTxn {

    public UnknownFromTxn(TransactionType transactionType,
                          int retrosheetId,
                          Player player,
                          Team from,
                          LocalDate transactionDate) {
        super(transactionType, retrosheetId, player, from, transactionDate);
    }
}
