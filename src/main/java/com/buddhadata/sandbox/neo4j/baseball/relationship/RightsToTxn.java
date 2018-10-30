package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Date;

/**
 * Neo4J relationship representing a player rights being obtained by a team
 */
@RelationshipEntity(type = "RIGHTS_TO")
public class RightsToTxn extends ToTxn {

    /**
     * Constructor
     */
    public RightsToTxn() {
        super();
    }

    public RightsToTxn(int retrosheetId,
                       Player player,
                       Team team,
                       Date transactionDate) {
        super (retrosheetId, player, team, transactionDate);
    }
}
