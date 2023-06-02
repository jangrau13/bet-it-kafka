package ch.unisg.ics.edpo.shared.game;

import ch.unisg.ics.edpo.shared.Keys;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class GameObject {
    @Getter
    private final String gameId;

    @Getter
    private final String team1;

    @Getter
    private final String team2;

    @Getter
    private final String description;

    @Getter @Setter
    private GameState gameState;

    /**
     * For the single player, this means the player wins
     */
    @Getter
    private final Boolean team1Wins;

    public GameObject(String gameId, String team1, String team2, GameState gameState, Boolean team1Wins, String description) {
        this.gameId = gameId;
        this.team1 = team1;
        this.team2 = team2;
        this.gameState = gameState;
        this.team1Wins = team1Wins;
        this.description = description;
    }

    public static GameObject fromMap(Map<String, Object> map) {
        String gameId = (String) map.get(Keys.GameObjectFields.GAME_ID);
        String team1 = (String) map.get(Keys.GameObjectFields.TEAM_1);
        String team2 = (String) map.get(Keys.GameObjectFields.TEAM_2);
        String description = (String) map.get(Keys.GameObjectFields.DESCRIPTION);

        Object gameStateObj = map.get(Keys.GameObjectFields.GAME_STATE);
        if (!(gameStateObj instanceof String)) {
            throw new IllegalArgumentException("Invalid type for 'gameState' field");
        }
        GameState gameState = GameState.valueOf((String) gameStateObj);

        Object team1WinsObj = map.get(Keys.GameObjectFields.TEAM_1_WINS);
        if (!(team1WinsObj instanceof Boolean)) {
            throw new IllegalArgumentException("Invalid type for 'team1Wins' field");
        }
        Boolean team1Wins = (Boolean) team1WinsObj;

        return new GameObject(gameId, team1, team2, gameState, team1Wins, description);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(Keys.GameObjectFields.GAME_ID, gameId);
        map.put(Keys.GameObjectFields.TEAM_1, team1);
        map.put(Keys.GameObjectFields.TEAM_2, team2);
        map.put(Keys.GameObjectFields.GAME_STATE, gameState.toString());
        map.put(Keys.GameObjectFields.TEAM_1_WINS, team1Wins);
        map.put(Keys.GameObjectFields.DESCRIPTION, description);
        return map;
    }

    public enum GameState {
        PUBLISHED, STARTED, ENDED, ERROR
    }
}


