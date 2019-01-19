package com.buddhadata.sandbox.neo4j.baseball.node;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
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
     * The city in which the team/franchise is located.
     */
    private String city;

    /**
     * The league name
     */
    private String league;

    /**
     * The name of the team
     */
    private String name;

    /**
     * The unique identifier from the Retrosheet data
     */
    private String retrosheetId;

    /**
     * Constructor
     */
    public Team() {
        return;
    }

    /**
     * Constructor
     * @param retrosheetId unique ID from the Retrosheet data
     * @param name team's name
     * @param city team's city
     * @param league team's league
     */
    public Team(String retrosheetId,
                String name,
                String city,
                String league) {
        this.retrosheetId = retrosheetId;
        this.name = name;
        this.city = city;
        this.league = league;
    }

    /**
     * getter
     * @return internal Neo4J ID
     */
    public Long getId() {
        return id;
    }

    /**
     * setter
     * @param id internal Neo4J ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * getter
     * @return the city in which the team/franchise is located
     */
    public String getCity() {
        return city;
    }

    /**
     * setter
     * @param city the city in which the team/franchise is located
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * getter
     * @return the league in which the team plays
     */
    public String getLeague() {
        return league;
    }

    /**
     * setter
     * @param league the league in which the team plays
     */
    public void setLeague(String league) {
        this.league = league;
    }

    /**
     * getter
     * @return the name/nickname of the team
     */
    public String getName() {
        return name;
    }

    /**
     * setter
     * @param name the name/nickname of the team
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter
     * @return the unique ID from the Restrosheet data
     */
    public String getRetrosheetId() {
        return retrosheetId;
    }

    /**
     * setter
     * @param retrosheetId the unique ID from the retrosheet data
     */
    public void setRetrosheetId(String retrosheetId) {
        this.retrosheetId = retrosheetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (!league.equals(team.league)) return false;
        if (!name.equals(team.name)) return false;
        return retrosheetId != null ? retrosheetId.equals(team.retrosheetId) : team.retrosheetId == null;

    }

    @Override
    public int hashCode() {
        int result = league.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (retrosheetId != null ? retrosheetId.hashCode() : 0);
        return result;
    }
}
