package ch.unisg.api2kafka.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @Builder
public class GameEndedEvent {
    private String gameId;
    private String team1;
    private String team2;
    private String description;
    private String gameState;
    private String team1Wins;
    private Integer projectedHits;
    private Integer actualHits;
    private String correlationId;
    public static GameEndedEvent fromMatchFixture(MatchFixture match) {
        return GameEndedEvent
                .builder()
                .gameId(Integer.toString(match.getId()))
                .team1(Integer.toString(match.getHome_id()))
                .team2(Integer.toString(match.getAway_id()))
                .description(match.getHome_name() + match.getAway_name() + match.getDate())
                .gameState("FOOTBALL")
                .build();
    }

    public Map<String,Object> toMap() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this, Map.class);
    }
}
