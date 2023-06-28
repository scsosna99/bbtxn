package com.buddhadata.sandbox.neo4j.baseball.node;

import org.neo4j.ogm.annotation.NodeEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@NodeEntity
public class PlayerBio extends Player {

    /**
     * Which way does the player bat?
     */
    private BatThrowEnum playerBats;

    /**
     * Which way does the player throw?
     */
    private BatThrowEnum playerThrows;

    /**
     * Is the player in Baseball's Hall of Fame
     */
    private boolean isHallOfFame;

    /**
     * How tall is the player in inches.
     */
    private Integer heightInches;

    /**
     * How much does the player weigh in pounds
     */
    private Integer weightLbs;

    /**
     * The date of his playing debut
     */
    private LocalDateTime playerRetire;

    /**
     * In what country was the player born?
     */
    private String birthCountry;


    public PlayerBio () {
        super ();
    }

    public BatThrowEnum getPlayerBats() {
        return playerBats;
    }

    public void setPlayerBats(BatThrowEnum playerBats) {
        this.playerBats = playerBats;
    }

    public BatThrowEnum getPlayerThrows() {
        return playerThrows;
    }

    public void setPlayerThrows(BatThrowEnum playerThrows) {
        this.playerThrows = playerThrows;
    }

    public boolean isHallOfFame() {
        return isHallOfFame;
    }

    public void setHallOfFame(boolean hallOfFame) {
        isHallOfFame = hallOfFame;
    }

    public int getHeightInches() {
        return heightInches;
    }

    public void setHeightInches(int heightInches) {
        this.heightInches = heightInches;
    }

    public int getWeightLbs() {
        return weightLbs;
    }

    public void setWeightLbs(int weightLbs) {
        this.weightLbs = weightLbs;
    }

    public LocalDateTime getPlayerRetire() {
        return playerRetire;
    }

    public void setPlayerRetire(LocalDateTime playerRetire) {
        this.playerRetire = playerRetire;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }
}
