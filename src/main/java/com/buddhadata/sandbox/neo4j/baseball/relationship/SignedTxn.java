package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDate;
import java.util.Date;

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
                     Team team,
                     LocalDate transactionDate) {
        super (transactionType, retrosheetId, player, team, transactionDate);
    }
}
