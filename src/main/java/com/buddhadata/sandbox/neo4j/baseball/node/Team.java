package com.buddhadata.sandbox.neo4j.baseball.node;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Domain Object for the baseball team
 */
@NodeEntity
public class Team {

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
    public Team() {
        return;
    }

    /**
     * Constructor
     * @param name name of the team
     * @param bbrefUrl baseball-reference.com URL for the player
     */
    public Team(String name,
                String bbrefUrl) {
        this.name = name;
        this.url = bbrefUrl;
    }

    /**
     * getter
     * @return team's name
     */
    public String getName() {
        return name;
    }

    /**
     * setter
     * @param name team's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter
     * @return baseball-reference.com URL for the team
     */
    public String getUrl() {
        return url;
    }

    /**
     * setter
     * @param url baseball-reference.com URL for the team
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team player = (Team) o;

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
