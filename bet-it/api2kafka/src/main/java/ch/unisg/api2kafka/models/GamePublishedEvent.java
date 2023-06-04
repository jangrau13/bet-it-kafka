package ch.unisg.api2kafka.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @Builder
public class GamePublishedEvent {
    private String gameId;
    private String team1;
    private String team2;
    private String description;
    private String gameName;
    private String gameType;
    private String state;
    public static GamePublishedEvent fromMatchFixture(MatchFixture match) {
        return GamePublishedEvent
                .builder()
                .gameId(Integer.toString(match.getId()))
                .team1(Integer.toString(match.getHome_id()))
                .team2(Integer.toString(match.getAway_id()))
                .description(match.getHome_name() + match.getAway_name() + match.getDate())
                .gameName(match.getCompetition().getName())
                .gameType("FOOTBALL")
                .state("SCHEDULED")
                .build();
    }

    public Map<String,Object> toMap() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this, Map.class);
    }
}
