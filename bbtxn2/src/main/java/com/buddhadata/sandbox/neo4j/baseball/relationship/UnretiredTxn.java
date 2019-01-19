package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.node.Unassigned;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Represents the player retiring from the team specified
 */
@RelationshipEntity(type = "RETIRED")
public class UnretiredTxn extends ToTxn {

    public UnretiredTxn(TransactionType transactionType,
                        int retrosheetId,
                        Player player,
                        Unassigned unassigned,
                        Team team,
                        LocalDateTime transactionDate) {
        super(transactionType, retrosheetId, player, unassigned, team, transactionDate);
    }
}
