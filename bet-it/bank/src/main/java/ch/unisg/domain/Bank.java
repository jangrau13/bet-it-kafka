package ch.unisg.domain;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Create Bank Instance etc
 */
@Slf4j
public class Bank {

    private Map<String, Double> moneyBalances;

    private Map<String, Double> frozenBalance;
    private static final Bank instance = new Bank();


    // private constructor to avoid client applications using the constructor
    private Bank(){
        this.moneyBalances = new HashMap<>();
        this.frozenBalance = new HashMap<>();
    }
    public void freeze(FreezeEvent freezeEvent) {
        for (int i = 0; i <= freezeEvent.getUsers().length; i++ ) {
            String user = freezeEvent.getUsers()[i];
            double amount = freezeEvent.getAmounts()[i];
            boolean success = freezeOneAccount(user, amount);
            if(!success){
                rollbackAndThrow(i, freezeEvent);
                break;
            }
        }
    }
    private void rollbackAndThrow(int untilIndex, FreezeEvent freezeEvent){
        log.error("Freezing failed trying to rollback because could not freeze for user " + freezeEvent.getUsers()[untilIndex]);
        for (int i=0; i<untilIndex; i++){
            // we just freeze them with negative -
            freezeOneAccount(freezeEvent.getUsers()[i], -freezeEvent.getAmounts()[i]);
        }
        throw new RuntimeException("Could not freeze for user: " + freezeEvent.getUsers()[untilIndex]);
    }

    private boolean freezeOneAccount(String user, double amount){
        throw new NotImplementedException();
    }


    public static Bank getInstance() {
        return instance;
    }

}
