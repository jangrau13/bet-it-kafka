package ch.unisg.zeebe.servicetasks;

import ch.unisg.ics.edpo.shared.bank.TwoFactor;
import ch.unisg.kafka.service.BankProducerService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@EnableZeebeClient
@RequiredArgsConstructor
@Slf4j
public class SendToKafka {

  private final static Logger LOG = LoggerFactory.getLogger(SendToKafka.class);
  private final BankProducerService<Map> mapService;

  @ZeebeWorker(type = "send-to-kafka")
  public void sendToKafka(final JobClient client, final ActivatedJob job) {

    Map map = job.getVariablesAsMap();
    LOG.info("ZeebeWorker send-to-kafka received map", map);
    if(map.containsKey("topic")){
        //check that we do not start a Camunda Process again
        if(map.containsKey("messageName")){
            map.remove("messageName");
        }
        mapService.sendCamundaMessage(map,"camunda");
        LOG.info("sending Message to Camunda Kafka Interface");
    }else{
        LOG.warn("Job {} did not contain a topic, I cannot send it, I am sorry", job.getElementInstanceKey());
    }
    client.newCompleteCommand(job.getKey()).send()
      // join(); <-- This would block for the result. While this is easier-to-read code, it has limitations for parallel work.
      // Hence, the following code leverages reactive programming. This is discssed in https://blog.bernd-ruecker.com/writing-good-workers-for-camunda-cloud-61d322cad862.
      .whenComplete((result, exception) -> {
        if (exception == null) {
          LOG.info("Completed job successful with result:" + result);
        } else {
          LOG.error("Failed to complete job", exception);
        }
      });    
  }

}