package ch.unisg.api2kafka.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Competition {
    private String id;
    private String name;
    private String is_league;
    private String is_cup;
    private String tier;
    private String has_groups;
    private String active;
    private String national_teams_only;
}
