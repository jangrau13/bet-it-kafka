package ch.unisg.camunda.delegates;

import ch.unisg.domain.Bank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CheckBaileeDelegates  {
//    @Override implements JavaDelegate
//    public void execute(DelegateExecution delegateExecution) throws Exception {
//        String bailedCustomer = (String) delegateExecution.getVariable("bailedCustomer");
//        //bailName
//        String bailName = (String) delegateExecution.getVariable("bailName");
//        //bailee
//        String bailee = (String) delegateExecution.getVariable("bailee");
//        Bank bank = Bank.getInstance();
//        boolean bailSuccessful = false;
//
//        if(bailedCustomer.equals(bailName)){
//            if(bank.isCustomer(bailee)){
//                bailSuccessful = true;
//            }
//        }
//
//        delegateExecution.setVariable("test_bid", bailSuccessful, "bailSuccessful");
//    }
}
