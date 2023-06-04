package ch.unisg.api2kafka.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class LiveScoresResponseData {
    @JsonSetter("match")
    private List<LiveMatch> matches;
}
