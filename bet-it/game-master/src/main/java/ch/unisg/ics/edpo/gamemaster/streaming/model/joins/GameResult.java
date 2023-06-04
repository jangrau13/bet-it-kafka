package ch.unisg.ics.edpo.gamemaster.streaming.model.joins;

import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotSpawnEvent;
import ch.unisg.ics.edpo.shared.game.GameObject;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class GameResult {

    @SerializedName("gameId")
    @Getter @Setter
    private final String gameId;

    @SerializedName("team1")
    @Getter @Setter
    private final String team1;

    @SerializedName("team2")
    @Getter @Setter
    private final String team2;

    @SerializedName("description")
    @Getter @Setter
    private final String description;

    @SerializedName("gameState")
    @Getter @Setter
    private GameObject.GameState gameState;

    /**
     * For the single player, this means the player wins
     */
    @SerializedName("team1Wins")
    @Getter @Setter
    private Boolean team1Wins;

    @SerializedName("projectedHits")
    @Getter @Setter
    private int projectedHits;

    @SerializedName("actualHits")
    @Getter @Setter
    private int actualHits;

    @SerializedName("correlationId")
    @Getter @Setter
    private String correlationId;

    public GameResult(String gameId, String team1, String team2, GameObject.GameState gameState, Boolean team1Wins, String description, int projectedHits, int actualHits) {
        this.gameId = gameId;
        this.team1 = team1;
        this.team2 = team2;
        this.gameState = gameState;
        this.team1Wins = team1Wins;
        this.description = description;
        this.projectedHits = projectedHits;
        this.actualHits = actualHits;
        this.correlationId = gameId;
    }

    public GameResult(DotSpawnEvent dotSpawnEvent, Long count){
        this.gameId = dotSpawnEvent.getGameId().toString();
        this.gameState = GameObject.GameState.ENDED;
        this.team1 = dotSpawnEvent.getUsername();
        this.team2 = "Rest of the World";
        this.description = dotSpawnEvent.toString();
        this.projectedHits = dotSpawnEvent.getProjectedHits();
        this.correlationId = dotSpawnEvent.getGameId().toString();
        if(count != null){
            this.actualHits = count.intValue();
            if(count.intValue() >= dotSpawnEvent.getProjectedHits()){
                this.team1Wins = true;
            }else{
                this.team1Wins = false;
            }
        }else{
            this.team1Wins = false;
        }
    }

}
