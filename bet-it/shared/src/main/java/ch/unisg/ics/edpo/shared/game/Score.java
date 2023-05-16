package ch.unisg.ics.edpo.shared.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import java.io.Serializable;
@Data
@Builder
public class Score implements Serializable{

    public Score(){
    }

    @JsonProperty("homeTeamScore")
    @Getter private int homeTeamScore;

    @Getter
    @JsonProperty("awayTeamScore")
    private int awayTeamScore;

    public Score(int homeTeamScore, int awayTeamScore) {
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
    }

}

