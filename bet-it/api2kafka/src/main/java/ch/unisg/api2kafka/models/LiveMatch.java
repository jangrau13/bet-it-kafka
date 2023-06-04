package ch.unisg.api2kafka.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveMatch {
    private String score;
    private Odds odds;
    private int away_id;
    private String ht_score;
    private String home_name;
    private String competition_name;
    private String last_changed;
    private String league_name;
    private Country country;
    private int competition_id;
    private int fixture_id;
    private String h2h;
    private String status;
//    private String federation;
    private int home_id;
    private String added;
    private String ft_score;
    private String ps_score;
    private String location;
    private int id;
    private boolean has_lineups;
    private String et_score;
    private String time;
    private int league_id;
    private String away_name;
    private String scheduled;
    private String events;
    private Outcomes outcomes;
}
