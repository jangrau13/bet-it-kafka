package ch.unisg.domain;

import lombok.extern.slf4j.Slf4j;

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
    private Bank() {
        this.moneyBalances = new HashMap<>();
        this.frozenBalance = new HashMap<>();
    }

    public void freeze(FreezeEvent freezeEvent) {
        for (int i = 0; i <= freezeEvent.getUsers().length; i++) {
            String user = freezeEvent.getUsers()[i];
            double amount = freezeEvent.getAmounts()[i];
            try {
                freezeOneAccount(user, amount);
            } catch (RuntimeException e){
                rollback(i, freezeEvent);
                throw e;
            }
        }
    }
    private void freezeOneAccount(String user, double amount) throws RuntimeException {
        double frozen = 0;
        if (!moneyBalances.containsKey(user)) {
            throw new RuntimeException("This user does not exist in the bank " + user);
        }
        double money = moneyBalances.get(user);
        if (frozenBalance.containsKey(user)) {
            frozen = frozenBalance.get(user);
        }
        if (money - frozen >= amount) {
            this.frozenBalance.put(user, frozen + amount);
        } else {
            throw new RuntimeException("User with name: " + user + " did not have enough money");
        }
    }
    private void rollback(int untilIndex, FreezeEvent freezeEvent) {
        log.error("Freezing failed trying to rollback because could not freeze for user " + freezeEvent.getUsers()[untilIndex]);
        for (int i = 0; i < untilIndex; i++) {
            // we just freeze them with negative -
            try {
                freezeOneAccount(freezeEvent.getUsers()[i], -freezeEvent.getAmounts()[i]);
            } catch (Exception e){
                log.error("Damn the rollback failed, now we have a real problem", e);
            }
        }
    }
    public static Bank getInstance() {
        return instance;
    }

}
