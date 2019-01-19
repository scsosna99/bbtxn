package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.node.Unassigned;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Represents the player being released from the team specified
 */
@RelationshipEntity(type = "FREE_AGENT_VOIDED")
public class FreeAgentVoidedTxn extends FromTxn {

    public FreeAgentVoidedTxn(TransactionType transactionType,
                              int retrosheetId,
                              Player player,
                              Team from,
                              Unassigned unsigned,
                              LocalDateTime transactionDate) {
        super(transactionType, retrosheetId, player, from, unsigned, transactionDate);
    }
}
