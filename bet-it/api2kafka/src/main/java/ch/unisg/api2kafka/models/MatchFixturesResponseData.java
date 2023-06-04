package ch.unisg.api2kafka.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Getter
@Setter
public class MatchFixturesResponseData {
    private List<MatchFixture> fixtures;
    private String next_page;
    private String prev_page;

    public Integer getNextPageNumber() {
        return extractPageNumberFromPaginationLink(this.getNext_page());
    }
    private Integer extractPageNumberFromPaginationLink(String addr) {
        MultiValueMap<String, String> params =  UriComponentsBuilder.fromHttpUrl(addr).build().getQueryParams();
        List<String> pages = params.get("page");
        if (pages.size() > 0) {
            return Integer.parseInt(pages.get(0));
        }
        return null;
    }
}
