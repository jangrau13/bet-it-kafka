package ch.unisg.api2kafka.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MatchFixturesResponse {
    private Boolean success;
    private MatchFixturesResponseData data;
}
