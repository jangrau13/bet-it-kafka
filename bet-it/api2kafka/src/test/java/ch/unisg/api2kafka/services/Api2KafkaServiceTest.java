package ch.unisg.api2kafka.services;

import ch.unisg.api2kafka.clients.LiveScoreAPI;
import org.junit.jupiter.api.Test;

class Api2KafkaServiceTest {

    @Test
    void getDataFromApiAndPublishToKafka() {
        LiveScoreAPI api = new LiveScoreAPI();
        Api2KafkaService service = new Api2KafkaService(api, null);
        service.getDataFromApiAndPublishToKafka();
    }
}