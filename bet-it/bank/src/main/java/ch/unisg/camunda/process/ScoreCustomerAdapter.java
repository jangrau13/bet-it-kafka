package ch.unisg.camunda.process;


import ch.unisg.camunda.util.WorkflowLogger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("ScoreCustomerAdapter")
public class ScoreCustomerAdapter implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(DelegateExecution execution) {

        String name = (String) execution.getVariable("customer");

        if (name.equals("Lukas")) {
            execution.setVariable("customerScore", (boolean)true);
            WorkflowLogger.info(logger, "scoreCustomer","Customer scores a 6");
        }
        else {
            execution.setVariable("customerScore", (boolean)false);
            WorkflowLogger.info(logger, "scoreCustomer","Customer scores a 5");
        }



    }

}