package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.util.Date;

/**
 * Represents the player being traded from the team specified
 */
@RelationshipEntity(type = "TRADED_FROM")
public class TradedFromTxn extends FromTxn {

    public TradedFromTxn(int retrosheetId,
                         Player player,
                         Team from,
                         Date transactionDate) {
        super(retrosheetId, player, from, transactionDate);
    }
}
