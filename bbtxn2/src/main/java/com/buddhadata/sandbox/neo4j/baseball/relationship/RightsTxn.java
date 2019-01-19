package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Neo4J relationship representing a player rights being obtained by a team
 */
@RelationshipEntity(type = "ACQUIRE_RIGHTS")
public class RightsTxn extends FromToTxn {

    /**
     * Constructor
     */
    public RightsTxn() {
        super();
    }

    public RightsTxn(TransactionType transactionType,
                     int retrosheetId,
                     Player player,
                     Team fromTeam,
                     Team toTeam,
                     LocalDateTime transactionDate) {
        super (transactionType, retrosheetId, player, fromTeam, toTeam, transactionDate);
    }
}
