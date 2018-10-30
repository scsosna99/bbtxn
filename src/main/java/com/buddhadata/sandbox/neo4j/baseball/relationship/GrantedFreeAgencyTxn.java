package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Date;

/**
 * Represents the player being granted free agency from the team specified
 */
@RelationshipEntity(type = "GRANTED_FREE_AGENCY")
public class GrantedFreeAgencyTxn extends FromTxn {

    public GrantedFreeAgencyTxn(int retrosheetId,
                                Player player,
                                Team from,
                                Date transactionDate) {
        super(retrosheetId, player, from, transactionDate);
    }
}
