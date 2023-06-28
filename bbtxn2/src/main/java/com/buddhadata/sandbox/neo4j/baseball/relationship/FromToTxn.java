package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.StartNode;

import java.time.LocalDateTime;

/**
 * All "from" transactions represent a player leaving a team in some way.
 */
public class FromToTxn extends TxnBase {

    /**
     * the source team from which the player started the transaction
     */
    @StartNode
    private Team from;

    /**
     * the destination team for the player
     */
    @EndNode
    private Team to;


    /**
     * Constructor
     */
    public FromToTxn() {
        super();
    }

    protected FromToTxn(TransactionType transactionType,
                        int txnRetrosheetId,
                        Player player,
                        Team from,
                        Team to,
                        LocalDateTime transactionDate) {
        super (transactionType, txnRetrosheetId, player, transactionDate);
        this.from = from;
        this.to = to;
    }

    /**
     * getter
     * @return the team from which the player is leaving
     */
    public Team getFrom() {
        return from;
    }

    /**
     * setter
     * @param from the team from which the player is leaving
     */
    public void setFrom(Team from) {
        this.from = from;
    }
}