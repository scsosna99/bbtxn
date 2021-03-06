package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Neo4J relationship representing a player being dealt conditionally to a team
 */
@RelationshipEntity(type = "CONDTIONAL")
public class ConditionalTxn extends FromToTxn {

    /**
     * Constructor
     */
    public ConditionalTxn() {
        super();
    }

    public ConditionalTxn(TransactionType transactionType,
                          int retrosheetId,
                          Player player,
                          Team fromTeam,
                          Team toTeam,
                          LocalDateTime transactionDate) {
        super (transactionType, retrosheetId, player, fromTeam, toTeam, transactionDate);
    }
}
