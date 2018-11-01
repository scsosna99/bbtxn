package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.time.LocalDate;
import java.util.Date;

/**
 * All "from" transactions represent a player leaving a team in some way.
 */
public class FromTxn extends TxnBase {

    /**
     * the player for the transaction
     */
    @EndNode
    private Player player;

    /**
     * the team from which the player is leaving
     */
    @StartNode
    private Team from;

    /**
     * Constructor
     */
    public FromTxn() {
        super();
    }

    protected FromTxn(TransactionType transactionType,
                      int retrosheetId,
                      Player player,
                      Team from,
                      LocalDate transactionDate) {
        super (transactionType, retrosheetId, transactionDate);
        this.from = from;
        this.player = player;
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
     * @return the player involved in the transaction
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * setter
     * @param player the player involved in the transaction
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
