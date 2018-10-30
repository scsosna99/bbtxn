package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.util.Date;

/**
 * All "To" transactions representing a player joining a team somehow
 */
public class ToTxn extends TxnBase {

    /**
     * the player participating in the transaction
     */
    @StartNode
    private Player player;

    /**
     * the team whom the player is joining
     */
    @EndNode
    private Team team;

    /**
     * Constructor
     */
    public ToTxn() {
        super();
    }

    public ToTxn(int retrosheetId,
                 Player player,
                 Team team,
                 Date transactionDate) {
        super (retrosheetId, transactionDate);
        this.player = player;
        this.team = team;
    }

    /**
     * getter
     * @return player participating in the transaction
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * setter
     * @param player the player participating in the transaction
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * getter
     * @return the team whom the player is joining
     */
    public Team getTeam() {
        return team;
    }

    /**
     * setter
     * @param team the team whom the player is joining
     */
    public void setTeam(Team team) {
        this.team = team;
    }
}
