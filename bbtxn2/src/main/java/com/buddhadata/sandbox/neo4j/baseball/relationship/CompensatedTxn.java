package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Neo4J relationship representing a player sent to a team as a free agent compensation pick
 */
@RelationshipEntity(type = "COMPENSATION")
public class CompensatedTxn extends FromToTxn {

    /**
     * Constructor
     */
    public CompensatedTxn() {
        super();
    }

    public CompensatedTxn(TransactionType transactionType,
                          int retrosheetId,
                          Player player,
                          Team fromTeam,
                          Team toTeam,
                          LocalDateTime transactionDate) {
        super (transactionType, retrosheetId, player, fromTeam, toTeam, transactionDate);
    }
}
