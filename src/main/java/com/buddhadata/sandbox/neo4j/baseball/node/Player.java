package com.buddhadata.sandbox.neo4j.baseball.node;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Date;

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
     * The date of his coaching debut
     */
    private Date coachDebut;

    /**
     * The date of his managerial debut.
     */
    private Date managerDebut;

    /**
     * The date of his playing debut
     */
    private Date playerDebut;

    /**
     * The datae of his umpiring debut
     */
    private Date umpireDebut;


    /**
     * First name of player
     */
    private String firstName;

    /**
     * Last name of player
     */
    private String lastName;

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
                   Date playerDebut,
                   Date coachDebut,
                   Date managerDebut,
                   Date umpireDebut) {
        this.retrosheetId = retrosheetId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.playerDebut = playerDebut;
        this.coachDebut = coachDebut;
        this.managerDebut = managerDebut;
        this.umpireDebut = umpireDebut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCoachDebut() {
        return coachDebut;
    }

    public void setCoachDebut(Date coachDebut) {
        this.coachDebut = coachDebut;
    }

    public Date getManagerDebut() {
        return managerDebut;
    }

    public void setManagerDebut(Date managerDebut) {
        this.managerDebut = managerDebut;
    }

    public Date getPlayerDebut() {
        return playerDebut;
    }

    public void setPlayerDebut(Date playerDebut) {
        this.playerDebut = playerDebut;
    }

    public Date getUmpireDebut() {
        return umpireDebut;
    }

    public void setUmpireDebut(Date umpireDebut) {
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

    public String getRetrosheetId() {
        return retrosheetId;
    }

    public void setRetrosheetId(String retrosheetId) {
        this.retrosheetId = retrosheetId;
    }
}
