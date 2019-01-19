package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.node.Unassigned;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.StartNode;

import java.time.LocalDateTime;

/**
 * All "from" transactions represent a player leaving a team in some way.
 */
public class FromTxn extends TxnBase {

    /**
     * the team from which the player is leaving
     */
    @StartNode
    private Team from;

    /**
     * the default unsigned node when a player is not currently signed by a team
     */
    @EndNode
    private Unassigned unsigned;


    /**
     * Constructor
     */
    public FromTxn() {
        super();
    }

    protected FromTxn(TransactionType transactionType,
                      int txnRetrosheetId,
                      Player player,
                      Team from,
                      Unassigned unsigned,
                      LocalDateTime transactionDate) {
        super (transactionType, txnRetrosheetId, player, transactionDate);
        this.from = from;
        this.unsigned = unsigned;
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

    /**
     * getter
     * @return the unassigned node
     */
    public Unassigned getUnsigned() {
        return unsigned;
    }

    /**
     * setter
     * @param unsigned the unassigned node
     */
    public void setUnsigned(Unassigned unsigned) {
        this.unsigned = unsigned;
    }
}
