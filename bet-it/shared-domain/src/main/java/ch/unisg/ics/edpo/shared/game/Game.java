package ch.unisg.ics.edpo.shared.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * This is representing a game for now. Up for change in the future.
 */
public class Game implements Serializable{

    public Game(){
    }

    public Game(String id, String name, Score score, boolean hasEnded){
        this.id = id;
        this.name = name;
        this.score = score;
        this.hasEnded = hasEnded;
    }

    public Game update(Score score, boolean hasEnded){
        return new Game(this.id, this.name, score, hasEnded);
    }

    @Getter
    @JsonProperty("id")
    private String id;

    @Getter
    @JsonProperty("name")
    private String name;

    @Getter
    @JsonProperty("score")
    private Score score;

    @Getter
    @JsonProperty("hasEnded")
    private boolean hasEnded;

}