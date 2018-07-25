package com.buddhadata.sandbox.neo4j.baseball.node;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Domain Object for baseball players
 */
@NodeEntity
public class Player {

    /**
     * Internal Neo4J id of the node
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of the player
     */
    private String name;

    /**
     * The baseball-reference.com URL for the player
     */
    @Index (unique = true)
    private String url;

    /**
     * Constructor
     */
    public Player() {
        return;
    }

    /**
     * Constructor
     * @param name name of the player
     * @param bbrefUrl baseball-reference.com URL for the player
     */
    public Player (String name,
                   String bbrefUrl) {
        this.name = name;
        this.url = bbrefUrl;
    }

    /**
     * getter
     * @return player's name
     */
    public String getName() {
        return name;
    }

    /**
     * setter
     * @param name player's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter
     * @return baseball-reference.com URL for player
     */
    public String getUrl() {
        return url;
    }

    /**
     * setter
     * @param url baseball-reference.com URL for player
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (url != null ? !url.equals(player.url) : player.url != null) return false;
        return name != null ? name.equals(player.name) : player.name == null;

    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
