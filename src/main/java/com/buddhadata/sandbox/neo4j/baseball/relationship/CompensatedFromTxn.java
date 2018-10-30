package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Date;

/**
 * Represents the player is a free-agency compensation from the team
 */
@RelationshipEntity(type = "COMPENSATED_FROM")
public class CompensatedFromTxn extends FromTxn {

    public CompensatedFromTxn(int retrosheetId,
                              Player player,
                              Team from,
                              Date transactionDate) {
        super(retrosheetId, player, from, transactionDate);
    }
}
