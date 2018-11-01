package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDate;
import java.util.Date;

/**
 * Neo4J relationship representing a player being assigned to a team
 */
@RelationshipEntity(type = "ASSIGNED_TO")
public class AssignedToTxn extends ToTxn {

    /**
     * Constructor
     */
    public AssignedToTxn() {
        super();
    }

    public AssignedToTxn(TransactionType transactionType,
                         int retrosheetId,
                         Player player,
                         Team team,
                         LocalDate transactionDate) {
        super (transactionType, retrosheetId, player, team, transactionDate);
    }
}
