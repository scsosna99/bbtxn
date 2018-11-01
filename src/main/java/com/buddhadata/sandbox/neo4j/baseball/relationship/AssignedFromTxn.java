package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDate;

/**
 * Represents the player being assigned from the team specified
 */
@RelationshipEntity(type = "ASSIGNED_FROM")
public class AssignedFromTxn extends FromTxn {

    public AssignedFromTxn(TransactionType transactionType,
                           int retrosheetId,
                           Player player,
                           Team from,
                           LocalDate transactionDate) {
        super(transactionType, retrosheetId, player, from, transactionDate);
    }
}
