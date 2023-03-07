package ch.unisg.ics.edpo;

import lombok.Getter;

import java.util.Date;


/**
 * This is representing a game for now. Up for change in the future.
 */
public class Game {

    public Game(String id, String name, Date startTime){
        this.id = id;
        this.name = name;
        this.startTime = startTime;
    }

    public Game update(Score score, boolean hasEnded){
        this.score = score;
        this.hasEnded = hasEnded;
        this.currentTime = new Date();
        if(hasEnded){
            this.endedAt = new Date();
        }
        return this;
    }

    @Getter
    private final String id;

    @Getter
    private final String name;

    @Getter
    private final Date startTime;

    @Getter
    private Score score;

    @Getter
    private Date currentTime;

    /**
     * Time the game actually ended
     */
    @Getter
    private Date endedAt;

    @Getter
    private boolean hasEnded;

}