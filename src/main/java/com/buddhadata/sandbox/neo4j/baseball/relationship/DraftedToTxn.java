package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Neo4J relationship representing a player being assigned to a team
 */
@RelationshipEntity(type = "DRAFTED_TO")
public class DraftedToTxn extends ToTxn {

    /**
     * Constructor
     */
    public DraftedToTxn() {
        super();
    }

    public DraftedToTxn(TransactionType transactionType,
                        int retrosheetId,
                        Player player,
                        Team team,
                        LocalDateTime transactionDate) {
        super (transactionType, retrosheetId, player, team, transactionDate);
    }
}
