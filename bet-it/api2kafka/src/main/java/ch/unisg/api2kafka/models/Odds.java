package ch.unisg.api2kafka.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Odds {
    @JsonSetter("pre")
    private MatchOdds PreMatchOdds;
    @JsonSetter("live")
    private MatchOdds LiveMatchOdds;
}

