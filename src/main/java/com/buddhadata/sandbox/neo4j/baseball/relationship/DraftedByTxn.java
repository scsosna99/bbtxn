package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.util.Date;

/**
 * Neo4J relationship representing a player is drafted, such as the amateur draft
 */
@RelationshipEntity(type = "DRAFTED_BY")
public class DraftedByTxn extends TxnBase {

    /**
     * Neo4j Primary Key
     */
    private Long id;

    /**
     * the player being drafted
     */
    @StartNode
    private Player player;

    /**
     * the team who drafted the player
     */
    @EndNode
    private Team team;

    /**
     * Constructor
     */
    public DraftedByTxn() {
        super();
    }

    public DraftedByTxn(Player player,
                        Team team,
                        Date transactionDate) {
        super (transactionDate);
        this.player = player;
        this.team = team;
    }

    /**
     * getter
     * @return Neo4J primary key
     */
    public Long getId() {
        return id;
    }

    /**
     * setter
     * @param id Neo4J primary key
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * getter
     * @return player getting drafted
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * setter
     * @param player the player getting drafted
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * getter
     * @return the team who drafted the player
     */
    public Team getTeam() {
        return team;
    }

    /**
     * setter
     * @param team the team who drafted the player
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DraftedByTxn released = (DraftedByTxn) o;

        if (id != null ? !id.equals(released.id) : released.id != null) return false;
        if (team != null ? !team.equals(released.team) : released.team != null) return false;
        return player != null ? player.equals(released.player) : released.player == null;

    }

    @Override
    public int hashCode() {
        int result = team != null ? team.hashCode() : 0;
        result = 31 * result + (player != null ? player.hashCode() : 0);
        return result;
    }
}
