package ch.unisg.api2kafka.models;

import com.fasterxml.jackson.annotation.JsonSetter;

public class MatchOdds {
    @JsonSetter("1")
    private float One;
    @JsonSetter("2")
    private float Two;
    @JsonSetter("X")
    private float Tie;
}
