package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Date;

/**
 * Represents a signing - such as a free agency signing - voided
 */
@RelationshipEntity(type = "VOIDED_FROM")
public class VoidedFromTxn extends FromTxn {

    public VoidedFromTxn(int retrosheetId,
                         Player player,
                         Team from,
                         Date transactionDate) {
        super(retrosheetId, player, from, transactionDate);
    }
}
