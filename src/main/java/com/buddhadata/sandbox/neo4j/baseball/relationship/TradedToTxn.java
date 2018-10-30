package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.util.Date;

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

    public TradedToTxn(int retrosheetId,
                       Player player,
                       Team team,
                       Date transactionDate) {
        super (retrosheetId, player, team, transactionDate);
    }
}
