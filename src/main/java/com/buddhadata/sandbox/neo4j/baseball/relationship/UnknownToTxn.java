package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDate;
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

    public UnknownToTxn(TransactionType transactionType,
                        int retrosheetId,
                        Player player,
                        Team team,
                        LocalDate transactionDate) {
        super (transactionType, retrosheetId, player, team, transactionDate);
    }
}
