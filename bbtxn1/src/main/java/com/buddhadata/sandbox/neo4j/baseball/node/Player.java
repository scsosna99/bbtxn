package com.buddhadata.sandbox.neo4j.baseball.node;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.time.LocalDateTime;

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
     * The date of his playing debut
     */
    private LocalDateTime playerDebut;

    /**
     * First name of player
     */
    private String firstName;

    /**
     * Last name of player
     */
    private String lastName;

    /**
     * Complete name of player
     */
    private String name;

    /**
     * Player ID from retrosheet download
     */
    @Index (unique = true)
    private String retrosheetId;

    /**
     * Default constructor
     */
    public Player() {
        return;
    }

    /**
     * Constructor
     * @param retrosheetId
     * @param lastName
     * @param firstName
     * @param playerDebut
     */
    public Player (String retrosheetId,
                   String lastName,
                   String firstName,
                   LocalDateTime playerDebut) {
        this.retrosheetId = retrosheetId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.playerDebut = playerDebut;
        this.name = firstName + " " + lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPlayerDebut() {
        return playerDebut;
    }

    public void setPlayerDebut(LocalDateTime playerDebut) {
        this.playerDebut = playerDebut;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRetrosheetId() {
        return retrosheetId;
    }

    public void setRetrosheetId(String retrosheetId) {
        this.retrosheetId = retrosheetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return new EqualsBuilder().append(id, player.id).append(playerDebut, player.playerDebut).append(firstName, player.firstName).append(lastName, player.lastName).append(name, player.name).append(retrosheetId, player.retrosheetId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(playerDebut).append(firstName).append(lastName).append(name).append(retrosheetId).toHashCode();
    }
}
