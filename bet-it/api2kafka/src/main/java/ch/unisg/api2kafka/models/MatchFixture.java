package ch.unisg.api2kafka.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
public class MatchFixture {
    private Map<String,String> homeTranslations = new ConcurrentHashMap<>();
    private int competition_id;
    private Competition competition;
    private int home_id;
    private String time;
    private String home_name;
    private String away_name;
    private String date;
    private String location;
    private int id;
    private int group_id;
    private Odds odds;
    private int away_id;
    private int league_id;
    private League league;
    private Map<String,String> awayTranslations = new ConcurrentHashMap<>();
    private String round;
    private String h2h;
}
