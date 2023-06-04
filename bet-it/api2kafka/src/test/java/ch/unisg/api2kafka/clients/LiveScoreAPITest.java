package ch.unisg.api2kafka.clients;

import ch.unisg.api2kafka.models.LiveScoresResponse;
import ch.unisg.api2kafka.models.MatchEventResponse;
import ch.unisg.api2kafka.models.MatchFixturesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LiveScoreAPITest {
    @Test
    void verifyCredentials() {
        LiveScoreAPI client = new LiveScoreAPI();
        var status = client.verifyCredentials();
        Assertions.assertTrue(status);
    }
    @Test
    void getScores() {
        LiveScoreAPI client = new LiveScoreAPI();
        LiveScoresResponse resp = client.getLiveScores();
        System.out.println(resp);
    }
    @Test
    void getMatchFixtures(){
        LiveScoreAPI client = new LiveScoreAPI();
        MatchFixturesResponse resp = client.getMatchFixtures(null);
        System.out.println(resp.getData().getNextPageNumber());
    }

    @Test
    void getMatchEvents(){
        LiveScoreAPI client = new LiveScoreAPI();
        MatchEventResponse resp = client.getMatchEvents("431649");
        System.out.println(resp);
    }
}