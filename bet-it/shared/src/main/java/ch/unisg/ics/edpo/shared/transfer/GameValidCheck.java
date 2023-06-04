package ch.unisg.ics.edpo.shared.transfer;

import ch.unisg.ics.edpo.shared.Keys;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class GameValidCheck {
    @Getter
    private final String gameId;

    @Getter
    private final String correlationKey;

    @Getter @Setter
    private GameValidStatus result;
    public GameValidCheck(String gameId, GameValidStatus gameValidStatus) {
        this.gameId = gameId;
        this.correlationKey = gameId;
        this.result = gameValidStatus;
    }
    public static GameValidCheck fromMap(Map<String, Object> map) {
        String gameid = (String) map.get(Keys.GameValidCheckKeys.GAME_ID_FIELD);
        String statusString = (String) map.get(Keys.GameValidCheckKeys.RESULT);
        GameValidStatus result = GameValidStatus.valueOf(statusString);

        return new GameValidCheck(gameid, result);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(Keys.GameValidCheckKeys.GAME_ID_FIELD, gameId);
        map.put(Keys.GameValidCheckKeys.CORRELATION_ID, gameId);
        map.put(Keys.GameValidCheckKeys.RESULT, result.toString());
        return map;
    }
    public enum GameValidStatus {
        APPROVED, REJECTED
    }
}


