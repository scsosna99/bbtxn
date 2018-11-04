package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Neo4J relationship representing a player being loaned to a team
 */
@RelationshipEntity(type = "LOANED_TO")
public class LoanedToTxn extends ToTxn {

    /**
     * Constructor
     */
    public LoanedToTxn() {
        super();
    }

    public LoanedToTxn(TransactionType transactionType,
                       int retrosheetId,
                       Player player,
                       Team team,
                       LocalDateTime transactionDate) {
        super (transactionType, retrosheetId, player, team, transactionDate);
    }
}
