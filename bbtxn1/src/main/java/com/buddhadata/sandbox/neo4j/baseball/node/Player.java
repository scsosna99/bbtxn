package com.buddhadata.sandbox.neo4j.baseball.node;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Domain Object for baseball players
 */
@NodeEntity
public class Player extends BaseNode {

    /**
     * Field position for the players' raw data, separated by commas
     * https://www.retrosheet.org/retroID.htm
     */
    protected static int PLAYER_DEBUT_COACH = 5;
    protected static int PLAYER_DEBUT_MANAGER = 4;
    protected static int PLAYER_DEBUT_PLAYER = 3;
    protected static int PLAYER_DEBUT_UMPIRE = 6;
    protected static int PLAYER_NAME_FIRST = 2;
    protected static int PLAYER_NAME_LAST = 1;
    protected static int PLAYER_RETROSHEET_ID = 0;

    /**
     * Date format for debut of player, manager, coach, and umpire, ready for parsing
     */
    private static DateTimeFormatter DATE_FORMAT_PLAYER = DateTimeFormatter.ofPattern ("M/d/yyyy HH:mm");

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

    public static Player fromRaw (String line) {
        String[] fields = line.split(",", -1);
        unquoteFields(fields);
        LocalDateTime debut = parsePlayerDate(fields[PLAYER_DEBUT_PLAYER]);
        if (debut != null) {
            Player p = new Player();
            p.setRetrosheetId(fields[PLAYER_RETROSHEET_ID]);
            p.setLastName(fields[PLAYER_NAME_LAST]);
            p.setFirstName(fields[PLAYER_NAME_FIRST]);
            p.setPlayerDebut(debut);
            return p;
        } else {
            return null;
        }
    }

    /**
     * Default constructor
     */
    protected Player() {
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


    /**
     * Parse a string representing the debut date of the player
     * @param date the string representation in the raw data
     * @return Date object that can stored.
     */
    protected static LocalDateTime parsePlayerDate (String date) {

        if (date != null && !date.isEmpty()) {
            try {
                return LocalDateTime.parse(date + " 00:00", DATE_FORMAT_PLAYER);
            } catch (DateTimeParseException pe) {
                System.out.println ("Unable to parse player date: " + date);
            }
        }


        //  If no initial string was provided or the parsing failed, return null.
        return null;
    }

}
