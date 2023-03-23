package ch.unisg.camunda.process;


import ch.unisg.domain.Bank;
import ch.unisg.ics.edpo.shared.bidding.Bid;
import ch.unisg.ics.edpo.shared.bidding.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("PaymentOfCustomerAdapter")
public class PaymentOfCustomerAdapter  {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass()); implements JavaDelegate
//
//    @Override
//    public void execute(DelegateExecution execution) {
//
//        Bid bid = (Bid) execution.getVariable("bid");
//        Contract contract = (Contract) execution.getVariable("contract");
//        Boolean isValid = false;
//        String bailName = "";
//
//        String buyerName = bid.getBuyerName();
//
//        String writerName = contract.getWriterName();
//
//        logger.info("--------------------------buyer name:" + buyerName);
//        logger.info("--------------------------writer name:" + writerName);
//
//        Bank bank = Bank.getInstance();
//
//        if (bank.isCustomer(writerName) && bank.isCustomer(buyerName)) {
//            logger.info("++++++++++++++++++++++++++++valid transaction");
//            isValid = true;
//        } else if (bank.isCustomer(writerName)) {
//            bailName = buyerName;
//        } else if (bank.isCustomer(writerName)) {
//            bailName = writerName;
//        } else {
//            logger.info("++++++++++++++++++++++++++++not a valid transaction");
//            bailName = "both";
//        }
//
//        execution.setVariable("isValid", isValid);
//        execution.setVariable("bailName", bailName);
//
//    }

}