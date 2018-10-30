package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Date;

/**
 * Neo4J relationship representing an unknown transaction resulting in a player going to a team
 */
@RelationshipEntity(type = "UNKNOWN_TO")
public class UnknownToTxn extends ToTxn {

    /**
     * Constructor
     */
    public UnknownToTxn() {
        super();
    }

    public UnknownToTxn(int retrosheetId,
                        Player player,
                        Team team,
                        Date transactionDate) {
        super (retrosheetId, player, team, transactionDate);
    }
}
