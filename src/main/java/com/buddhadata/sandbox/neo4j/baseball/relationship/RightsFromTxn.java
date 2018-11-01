package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDate;
import java.util.Date;

/**
 * Represents the player rights being released from the team specified
 */
@RelationshipEntity(type = "RIGHTS_FROM")
public class RightsFromTxn extends FromTxn {

    public RightsFromTxn(TransactionType transactionType,
                         int retrosheetId,
                         Player player,
                         Team from,
                         LocalDate transactionDate) {
        super(transactionType, retrosheetId, player, from, transactionDate);
    }
}
