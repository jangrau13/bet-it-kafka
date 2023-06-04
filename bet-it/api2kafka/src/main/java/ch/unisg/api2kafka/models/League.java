package ch.unisg.api2kafka.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class League {
    private String id;
    @JsonSetter("country_id")
    private String countryID;
    private String name;
}
