package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDateTime;

/**
 * Neo4J relationship representing a player being traded to a team
 */
@RelationshipEntity(type = "TRADED_TO")
public class TradedToTxn extends ToTxn {

    /**
     * Constructor
     */
    public TradedToTxn() {
        super();
    }

    public TradedToTxn(TransactionType transactionType,
                       int retrosheetId,
                       Player player,
                       Team team,
                       LocalDateTime transactionDate) {
        super (transactionType, retrosheetId, player, team, transactionDate);
    }
}
