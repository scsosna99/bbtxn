package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Date;

/**
 * Represents a unknown transaction for a player leaving a team
 */
@RelationshipEntity(type = "UNKNOWN_FROM")
public class UnknownFromTxn extends FromTxn {

    public UnknownFromTxn(int retrosheetId,
                          Player player,
                          Team from,
                          Date transactionDate) {
        super(retrosheetId, player, from, transactionDate);
    }
}
