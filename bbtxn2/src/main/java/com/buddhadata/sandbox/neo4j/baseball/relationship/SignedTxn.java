package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.node.Unassigned;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Neo4J relationship for a player signing with a team as a free agent (in any fashion)
 */
@RelationshipEntity(type = "SIGNED_WITH")
public class SignedTxn extends ToTxn {

    /**
     * Constructor
     */
    public SignedTxn() {
        super();
    }

    public SignedTxn(TransactionType transactionType,
                     int retrosheetId,
                     Player player,
                     Unassigned unassigned,
                     Team team,
                     LocalDateTime transactionDate) {
        super (transactionType, retrosheetId, player, unassigned, team, transactionDate);
    }
}
