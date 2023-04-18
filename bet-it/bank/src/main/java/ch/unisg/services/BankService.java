package ch.unisg.services;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankService {

    private final String STRIPE = "stripe";
    private final Logger log = LoggerFactory.getLogger(getClass());

    public boolean testService(String from, String to, int amount){
        //if from is stripe, we can consider it as a credit card payment, which always works
        if(from.equals(STRIPE)){
            //check only to
            log.info("checking to");

            log.info("check over");
        }else{
            // check from and to
        }
        // test whether user from and to exists

        // test whether from has this amount of money

        return true;
    }
}
