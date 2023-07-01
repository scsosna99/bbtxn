package com.buddhadata.sandbox.neo4j.baseball.node;

import org.neo4j.ogm.annotation.NodeEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@NodeEntity
public class PlayerBio extends Player {

    /**
     * Field position for the players' bio data, separated by columns
     * https://www.retrosheet.org/BIOFILE.TXT
     */
    private static int BIO_BATS = 20;
    private static int BIO_BAT_CHANGE = 31;
    private static int BIO_BIRTH_CITY = 5;
    private static int BIO_BIRTH_COUNTRY = 7;
    private static int BIO_BIRTH_DATE = 4;
    private static int BIO_BIRTH_NAME = 29;
    private static int BIO_BIRTH_STATE = 6;
    private static int BIO_CEMETARY_CITY = 25;
    private static int BIO_CEMETARY_COUNTRY = 27;
    private static int BIO_CEMETARY_NAME = 24;
    private static int BIO_CEMETARY_NOTE = 28;
    private static int BIO_CEMETARY_STATE = 26;
    private static int BIO_COACH_DEBUT = 12;
    private static int BIO_COACH_RETIRE = 13;
    private static int BIO_DEATH_CITY = 17;
    private static int BIO_DEATH_COUNTRY = 19;
    private static int BIO_DEATH_DATE = 16;
    private static int BIO_DEATH_STATE = 18;
    private static int BIO_HALL_OF_FAME = 32;
    private static int BIO_HEIGHT = 22;
    private static int BIO_MANAGER_DEBUT = 10;
    private static int BIO_MANAGER_RETIRE = 11;
    private static int BIO_NAME_FIRST = 2;
    private static int BIO_NAME_CHANGE = 30;
    private static int BIO_NAME_LAST = 1;
    private static int BIO_NAME_NICK = 3;
    private static int BIO_PLAYER_DEBUT = 8;
    private static int BIO_PLAYER_RETIRE = 9;
    private static int BIO_RETROSHEET_ID = 0;
    private static int BIO_THROWS = 21;
    private static int BIO_UMPIRE_DEBUT = 14;
    private static int BIO_UMPIRE_RETIRE = 15;
    private static int BIO_WEIGHT = 23;

    /**
     * What to look for to determine that player is in the Hall of Fame.
     */
    private static String HALL_OF_FAME = "HOF";


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

    public static PlayerBio fromRaw (String line) {
        String[] fields = line.split(",", -1);
        unquoteFields(fields);
        LocalDateTime debut = parsePlayerDate(fields[BIO_PLAYER_DEBUT]);
        if (debut != null) {
            PlayerBio p = new PlayerBio();
            p.setRetrosheetId(fields[BIO_RETROSHEET_ID]);
            p.setLastName(fields[BIO_NAME_LAST]);
            p.setFirstName(fields[BIO_NAME_FIRST]);
            p.setPlayerDebut(debut);
            p.setPlayerRetire(parsePlayerDate(fields[BIO_PLAYER_RETIRE]));
            p.setHallOfFame(HALL_OF_FAME.equals(fields[BIO_HALL_OF_FAME]));
            p.setPlayerBats(parseBatThrows(fields[BIO_BATS]));
            p.setPlayerThrows(parseBatThrows(fields[BIO_THROWS]));
            p.setHeightInches(parseHeight(fields[BIO_HEIGHT]));
            p.setWeightLbs(parseWeight(fields[BIO_WEIGHT]));
            return p;
        } else {
            return null;
        }
    }

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

    public Integer getHeightInches() {
        return heightInches;
    }

    public void setHeightInches(Integer heightInches) {
        this.heightInches = heightInches;
    }

    public Integer getWeightLbs() {
        return weightLbs;
    }

    public void setWeightLbs(Integer weightLbs) {
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
    /**
     * convert the bat/throws from a string representation in the bio data into an enum
     * @param batsThrows
     * @return
     */
    private static BatThrowEnum parseBatThrows (String batsThrows) {
        if (batsThrows != null && batsThrows.trim().length() > 0) {
            switch (batsThrows.toUpperCase().charAt(0)) {
                case 'R':
                    return BatThrowEnum.RIGHT;
                case 'L':
                    return BatThrowEnum.LEFT;
                case 'B':
                    return BatThrowEnum.BOTH;
                default:
                    return BatThrowEnum.UNKNOWN;
            }
        } else {
            return BatThrowEnum.UNKNOWN;
        }
    }

    /**
     * A player's height is represented as "ft-in" and we'll convert it into the number of inches.
     * @param height
     * @return
     */
    private static Integer parseHeight (String height) {

        Integer toReturn = null;
        if (height != null && height.trim().length() > 0) {
            String[] fields = height.split("-");
            try {
                toReturn = Integer.valueOf(fields[0]) * 12;
                if (fields.length > 1) {
                    toReturn += Integer.valueOf(fields[1]);
                }
            } catch (NumberFormatException e) {
                // the value provided must not be an integer as expected
                toReturn = null;
            }
        }


        return toReturn;
    }

    private static Integer parseWeight (String weight) {

        if (weight != null && weight.length() > 0) {
            try {
                return Integer.valueOf(weight);
            } catch (NumberFormatException e) {
                //  invalid weight string that can't be turned into a number.
                return null;
            }
        }

        return null;
    }
}
