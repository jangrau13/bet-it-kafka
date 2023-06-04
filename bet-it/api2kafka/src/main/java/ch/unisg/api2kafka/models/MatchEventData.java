package ch.unisg.api2kafka.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchEventData {
    private String id;
    private String match_id;
    private String player;
    private String time;
    private String event;
    private String sort;
    private String home_away;
    private String info;
}