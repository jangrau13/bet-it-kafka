package ch.unisg.port.zeebe.servicetasks;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@EnableZeebeClient
public class UuidCreator {

  private final static Logger LOG = LoggerFactory.getLogger(UuidCreator.class);

  @ZeebeWorker(type = "uuid-creator")
  public void createUuid(final JobClient client, final ActivatedJob job) {
    UUID uuid = UUID.randomUUID();
    LOG.info("creating UUID: ", uuid);
    Map map = job.getVariablesAsMap();
    map.put("correlationId", uuid);

    client.newCompleteCommand(job.getKey()).variables(map).send()
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