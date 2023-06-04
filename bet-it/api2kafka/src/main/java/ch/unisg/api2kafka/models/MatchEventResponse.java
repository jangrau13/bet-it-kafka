package ch.unisg.api2kafka.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchEventResponse {
    private Boolean success;
    private MatchEventResponseData data;
}
