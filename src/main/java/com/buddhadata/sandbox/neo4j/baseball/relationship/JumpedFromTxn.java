package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Date;

/**
 * Represents the player jumping from the team specified
 */
@RelationshipEntity(type = "JUMPED_FROM")
public class JumpedFromTxn extends FromTxn {

    public JumpedFromTxn(int retrosheetId,
                         Player player,
                         Team from,
                         Date transactionDate) {
        super(retrosheetId, player, from, transactionDate);
    }
}
