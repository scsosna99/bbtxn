package com.buddhadata.sandbox.neo4j.baseball.relationship;

import com.buddhadata.sandbox.neo4j.baseball.node.Player;
import com.buddhadata.sandbox.neo4j.baseball.node.Team;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.util.Date;

/**
 * Created by scsosna on 7/17/18.
 */
@RelationshipEntity(type = "SELECTED")
public class SelectedTxn extends TxnBase {

    /**
     * Neo4j Primary Key
     */
    private Long id;

    /**
     * the team to which one or more players are traded
     */
    @StartNode
    private Player player;

    /**
     * the team from which one or more players are traded
     */
    @EndNode
    private Team from;

    /**
     * Constructor
     */
    public SelectedTxn() {
        super();
    }

    public SelectedTxn(Player player,
                       Team from,
                       Date transactionDate) {
        super (transactionDate);
        this.player = player;
        this.from = from;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getFrom() {
        return from;
    }

    public void setFrom(Team from) {
        this.from = from;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectedTxn released = (SelectedTxn) o;

        if (id != null ? !id.equals(released.id) : released.id != null) return false;
        if (from != null ? !from.equals(released.from) : released.from != null) return false;
        return player != null ? player.equals(released.player) : released.player == null;

    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (player != null ? player.hashCode() : 0);
        return result;
    }
}
