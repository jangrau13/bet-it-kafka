package ch.unisg.ics.edpo.addon.service.zeebe;

import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This is the zeebe worker, that allows zeebe to produce kafka messages
 */
@Component
@Slf4j
public class ZeebeListener {


    private static final String TOPIC_VARIABLE = "topic";

    private static final String ERROR_NO_TOPIC = "There was no topic specified in the variables received";

    private final KafkaMapProducer kafkaMapProducer;

    public ZeebeListener(KafkaMapProducer kafkaMapProducer) {
        this.kafkaMapProducer = kafkaMapProducer;
    }

    /**
     * This will send the map from zeebe to kafka with the topic variable as topic
     */
    @ZeebeWorker(type = "send-to-kafka")
    public void sendToKafka(final JobClient client, final ActivatedJob job) {

        Map<String, Object> map = job.getVariablesAsMap();
        log.info("ZeebeWorker send-to-kafka received map {}", map);
        if(!map.containsKey(TOPIC_VARIABLE)){
            log.warn("Job {} did not contain a topic, I cannot send it, I am sorry", job.getElementInstanceKey());
            tellZeebeWeFailed(client, job);
            return;
        }
        String topic = (String) map.get(TOPIC_VARIABLE);
        map.remove(topic);
        kafkaMapProducer.sendMessage(map, topic, "send-to-kafka-key");
        tellZeebeWeDidIt(client, job);
    }

    private void tellZeebeWeDidIt(final JobClient client, final ActivatedJob job){
        client.newCompleteCommand(job.getKey())
                .send()
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("Completed job successful with result:" + result);
                    } else {
                        log.error("Failed to complete job", exception);
                    }
                });
    }

    private void tellZeebeWeFailed(final JobClient client, final ActivatedJob job){
        client.newFailCommand(job.getKey())
                .retries(job.getRetries() - 1)
                .errorMessage(ZeebeListener.ERROR_NO_TOPIC)
                .send()
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("Successfully told zeebe we failed:" + result);
                    } else {
                        log.error("Failed to tell her we failed", exception);
                    }
                });
    }

}
