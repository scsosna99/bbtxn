package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Date;

/**
 * Represents a player being returned to his original team, such as a loan or purchase review
 */
@RelationshipEntity(type = "RETURNED_TO")
public class ReturnedToTxn extends ToTxn {

    /**
     * Constructor
     */
    public ReturnedToTxn() {
        super();
    }

    public ReturnedToTxn(int retrosheetId,
                         Player player,
                         Team team,
                         Date transactionDate) {
        super (retrosheetId, player, team, transactionDate);
    }
}
