package ch.unisg.camunda.delegates;

import ch.unisg.domain.Bank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AddCustomerDelegates  {
//    @Override implements JavaDelegate
//    public void execute(DelegateExecution delegateExecution) throws Exception {
//        boolean isApproved = (boolean) delegateExecution.getVariable("isApproved");
//        int amount = (int) delegateExecution.getVariable("customerMoney");
//        log.info("$$$$$$$for the amount {}", amount);
//        String customerName = (String) delegateExecution.getVariable("customerName");
//        log.info("$$$$$$$customer with name{}", customerName);
//        Bank bank = Bank.getInstance();
//        if(isApproved){
//            bank.addCustomer(customerName, amount);
//            log.info("added customer");
//        }else{
//            String approvedCustomerName = (String) delegateExecution.getVariable("toBeApprovedCustomer");
//            log.info("$$$$$$$approving customer proposal {}", approvedCustomerName);
//            if(approvedCustomerName.equals(customerName)){
//                bank.addCustomer(customerName, amount);
//                log.info("added customer");
//            } else{
//                log.info("not adding customer");
//            }
//        }
//
//    }
}
