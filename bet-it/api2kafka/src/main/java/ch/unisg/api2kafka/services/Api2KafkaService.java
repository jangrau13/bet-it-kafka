package ch.unisg.api2kafka.services;

import ch.unisg.api2kafka.clients.LiveScoreAPI;
import ch.unisg.api2kafka.models.*;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Api2KafkaService implements Runnable {
    @Value("${spring.kafka.game-topic-published}")
    private String gamePublishedTopic;

    @Value("${spring.kafka.game-topic-started}")
    private String gameStartedTopic;

    @Value("${spring.kafka.game-topic-ended}")
    private String gameEndedTopic;
    private final KafkaMapProducer kafkaMapProducer;
    private final LiveScoreAPI api;

    public Api2KafkaService(LiveScoreAPI api, KafkaMapProducer kafkaMapProducer) {
        this.api = api;
        this.kafkaMapProducer = kafkaMapProducer;
    }

    public void run(){
        this.getDataFromApiAndPublishToKafka();
    }

    public void getDataFromApiAndPublishToKafka() {
        System.out.println("getting data from the api and publishing to kafka");
        if (!this.api.verifyCredentials()) {
            throw new RuntimeException("failed to access api - bad credentials");
        }
        fetchAndPublishMatchFixtures();
        fetchAndPublishLiveMatches();
    }

    private void fetchAndPublishLiveMatches() {
        LiveScoresResponse liveMatches = this.api.getLiveScores();
        if (liveMatches == null || !liveMatches.getSuccess()) {
            System.out.println("failed to load data from api - live matches");
        } else {
            System.out.println("processing live matches");
            liveMatches.getData().getMatches().forEach(liveMatch -> {
                System.out.println(liveMatch.getHome_name());
                GameStartedEvent event = GameStartedEvent.fromLiveMatch(liveMatch);
                this.kafkaMapProducer.sendMessage(event.toMap(), this.gameStartedTopic, event.getGameId());
            });
        }
    }

    private void fetchAndPublishMatchFixtures() {
        MatchFixturesResponse matchFixtures = this.api.getMatchFixtures(null);
        processMatchFixturesResponse(matchFixtures);
        while (matchFixtures.getData().getNextPageNumber() != null) {
            matchFixtures = this.api.getMatchFixtures(matchFixtures.getData().getNextPageNumber());
            processMatchFixturesResponse(matchFixtures);
        }
    }

    private void processMatchFixturesResponse(MatchFixturesResponse matchFixtures) {
        if (matchFixtures == null || !matchFixtures.getSuccess()) {
            System.out.println("failed to load data from api - match fixtures");
        } else {
            System.out.println("processing match fixtures");
            matchFixtures.getData().getFixtures().forEach(this::publishGamePublishedEvent);
        }
    }

    private void publishGamePublishedEvent(MatchFixture matchFixture) {
        GamePublishedEvent event = GamePublishedEvent.fromMatchFixture(matchFixture);
        this.kafkaMapProducer.sendMessage(event.toMap(), this.gamePublishedTopic, event.getGameId());
    }
}
