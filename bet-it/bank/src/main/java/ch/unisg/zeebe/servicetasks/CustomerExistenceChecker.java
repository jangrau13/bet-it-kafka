package ch.unisg.zeebe.servicetasks;

import ch.unisg.ics.edpo.shared.bidding.Bid;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@EnableZeebeClient
public class CustomerExistenceChecker {

  private final static Logger LOG = LoggerFactory.getLogger(CustomerExistenceChecker.class);

  @ZeebeWorker(type = "customer-existence")
  public void checkExistence(final JobClient client, final ActivatedJob job) {
    final Map content_map = job.getVariablesAsMap();

    LOG.info("Received the following content: {}", content_map);

    final Map bidMap = (Map) content_map.get("bid");
    LOG.info("bid = " + bidMap);
    final String bidderName = (String) bidMap.get("buyerName");
    LOG.info("bidderName = " + bidderName);



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