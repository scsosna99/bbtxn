package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDate;
import java.util.Date;

/**
 * Neo4J relationship representing a player being selected from waivers by the team
 */
@RelationshipEntity(type = "WAIVER_TO")
public class WaiverToTxn extends ToTxn {

    /**
     * Constructor
     */
    public WaiverToTxn() {
        super();
    }

    public WaiverToTxn(TransactionType transactionType,
                       int retrosheetId,
                       Player player,
                       Team team,
                       LocalDate transactionDate) {
        super (transactionType, retrosheetId, player, team, transactionDate);
    }
}
