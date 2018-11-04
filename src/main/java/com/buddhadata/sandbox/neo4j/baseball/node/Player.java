package com.buddhadata.sandbox.neo4j.baseball.node;

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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * The date of his coaching debut
     */
    private LocalDateTime coachDebut;

    /**
     * The date of his managerial debut.
     */
    private LocalDateTime managerDebut;

    /**
     * The date of his playing debut
     */
    private LocalDateTime playerDebut;

    /**
     * The datae of his umpiring debut
     */
    private LocalDateTime umpireDebut;

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
     * Constructor
     * @param retrosheetId
     * @param lastName
     * @param firstName
     * @param playerDebut
     * @param coachDebut
     * @param managerDebut
     * @param umpireDebut
     */
    public Player (String retrosheetId,
                   String lastName,
                   String firstName,
                   LocalDateTime playerDebut,
                   LocalDateTime coachDebut,
                   LocalDateTime managerDebut,
                   LocalDateTime umpireDebut) {
        this.retrosheetId = retrosheetId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.playerDebut = playerDebut;
        this.coachDebut = coachDebut;
        this.managerDebut = managerDebut;
        this.umpireDebut = umpireDebut;
        this.name = firstName + " " + lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCoachDebut() {
        return coachDebut;
    }

    public void setCoachDebut(LocalDateTime coachDebut) {
        this.coachDebut = coachDebut;
    }

    public LocalDateTime getManagerDebut() {
        return managerDebut;
    }

    public void setManagerDebut(LocalDateTime managerDebut) {
        this.managerDebut = managerDebut;
    }

    public LocalDateTime getPlayerDebut() {
        return playerDebut;
    }

    public void setPlayerDebut(LocalDateTime playerDebut) {
        this.playerDebut = playerDebut;
    }

    public LocalDateTime getUmpireDebut() {
        return umpireDebut;
    }

    public void setUmpireDebut(LocalDateTime umpireDebut) {
        this.umpireDebut = umpireDebut;
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
}
