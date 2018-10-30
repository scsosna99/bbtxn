package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Date;

/**
 * Represents the player being purchased from the team specified
 */
@RelationshipEntity(type = "PURCHASED_FROM")
public class PurchasedFromTxn extends FromTxn {

    public PurchasedFromTxn(int retrosheetId,
                            Player player,
                            Team from,
                            Date transactionDate) {
        super(retrosheetId, player, from, transactionDate);
    }
}
