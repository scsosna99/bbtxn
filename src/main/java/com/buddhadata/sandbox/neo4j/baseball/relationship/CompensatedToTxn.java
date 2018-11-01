package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.time.LocalDate;
import java.util.Date;

/**
 * Neo4J relationship representing a player sent to a team as a free agent compensation pick
 */
@RelationshipEntity(type = "COMPENSATED_TO")
public class CompensatedToTxn extends ToTxn {

    /**
     * Constructor
     */
    public CompensatedToTxn() {
        super();
    }

    public CompensatedToTxn(TransactionType transactionType,
                            int retrosheetId,
                            Player player,
                            Team team,
                            LocalDate transactionDate) {
        super (transactionType, retrosheetId, player, team, transactionDate);
    }
}
