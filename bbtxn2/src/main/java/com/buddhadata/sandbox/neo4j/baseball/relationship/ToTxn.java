package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import com.buddhadata.sandbox.neo4j.baseball.node.Unassigned;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.StartNode;

import java.time.LocalDateTime;

/**
 * All "to" transactions represent a player leaving a team in some way.
 */
public class ToTxn extends TxnBase {

    /**
     * the destination team that the player ends up on
     */
    @EndNode
    private Team to;

    /**
     * the default unassigned node when a player is not currently signed by a team
     */
    @StartNode
    private Unassigned unassigned;


    /**
     * Constructor
     */
    public ToTxn() {
        super();
    }

    protected ToTxn(TransactionType transactionType,
                    int txnRetrosheetId,
                    Player player,
                    Unassigned unsigned,
                    Team to,
                    LocalDateTime transactionDate) {
        super (transactionType, txnRetrosheetId, player, transactionDate);
        this.to = to;
        this.unassigned = unsigned;
    }

    /**
     * getter
     * @return the team to which the player is leaving
     */
    public Team getTo() {
        return to;
    }

    /**
     * setter
     * @param to the team to which the player is leaving
     */
    public void setTo(Team to) {
        this.to = to;
    }

    /**
     * getter
     * @return the unassigned node
     */
    public Unassigned getUnassigned() {
        return unassigned;
    }

    /**
     * setter
     * @param unassigned the unassigned node
     */
    public void setUnassigned(Unassigned unassigned) {
        this.unassigned = unassigned;
    }
}
