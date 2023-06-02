package ch.unisg.ics.edpo.shared.game.dot;

import ch.unisg.ics.edpo.shared.Keys;
import ch.unisg.ics.edpo.shared.game.GameObject;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class DotGameObject extends GameObject {

    @Getter
    private final int projectedHits;
    public DotGameObject(String gameId, String team1, String team2, GameState gameState, Boolean team1Wins, String description, int projectedHits) {
        super(gameId, team1, team2, gameState, team1Wins, description);
        this.projectedHits = projectedHits;
    }
    public DotGameObject(GameObject gameObject, int projectedHits) {
        super(gameObject.getGameId(), gameObject.getTeam1(), gameObject.getTeam2(), gameObject.getGameState(), gameObject.getTeam1Wins(), gameObject.getDescription());
        this.projectedHits = projectedHits;
    }

    public static DotGameObject fromMap(Map<String, Object> map){
        int projectedHits = (int) map.get(Keys.DotGameObjectFields.HITS);
        GameObject gameObject = GameObject.fromMap(map);
        return new DotGameObject(gameObject, projectedHits);
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = super.toMap();
        map.put(Keys.DotGameObjectFields.HITS, this.projectedHits);
        return map;
    }
}
