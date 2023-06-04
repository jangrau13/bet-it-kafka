package ch.unisg.api2kafka.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Competitor {
    private String id;
    private String name;
    private String stadium;
    private String location;
}
