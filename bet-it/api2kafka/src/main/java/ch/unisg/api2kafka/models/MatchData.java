package ch.unisg.api2kafka.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchData {
    private String id;
    private String date;
    private String home_name;
    private String away_name;
    private String score;
    private String ht_score;
    private String ft_score;
    private String et_score;
    private String ps_score;
    private String time;
    private String league_id;
    private String status;
    private String added;
    private String last_changed;
    private String home_id;
    private String away_id;
    private String competition_id;
    private String location;
    private String fixture_id;
    private String scheduled;
    private Competitor home;
    private Competitor away;
    private Competition competition;
}
