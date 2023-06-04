package ch.unisg.api2kafka.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LiveScoresResponse {
    private Boolean success;
    private LiveScoresResponseData data;
}
