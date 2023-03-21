package ch.unisg.camunda.process;


import ch.unisg.camunda.util.WorkflowLogger;
import ch.unisg.domain.Bank;
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

        String buyerName = (String) execution.getVariable("buyerName");

        String writerName = (String) execution.getVariable("writerName");

        logger.info("--------------------------buyer name:" + buyerName);
        logger.info("--------------------------writer name:" + writerName);

        Bank bank  = Bank.getInstance();

        if(bank.isCustomer(writerName) && bank.isCustomer(buyerName)){
            logger.info("++++++++++++++++++++++++++++valid transaction");
        }else{
            logger.info("++++++++++++++++++++++++++++not a valid transaction");
        }

    }

}