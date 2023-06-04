package ch.unisg.ics.edpo.bank.domain;

import ch.unisg.ics.edpo.shared.bank.BankException;
import ch.unisg.ics.edpo.shared.bank.FreezeEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Create Bank Instance etc
 */
@Slf4j
public class Bank {

    @Getter
    private final Map<String, Double> moneyBalances;

    @Getter
    private final Map<String, Double> frozenBalance;
    private static final Bank instance = new Bank();


    // private constructor to avoid client applications using the constructor
    private Bank() {
        this.moneyBalances = new HashMap<>();
        this.frozenBalance = new HashMap<>();
        initBankMoney();
    }

    public Double getMoneyOfUser(String user) {
        return moneyBalances.getOrDefault(user, null);
    }

    /**
     * Careful this wipes state
     */
    public void wipe(){
        instance.moneyBalances.clear();
        instance.frozenBalance.clear();
        initBankMoney();
    }

    private void initBankMoney(){
        this.moneyBalances.put("bank", 1000000.0);
    }

    public void pay(String from, String to, double amount) throws BankException {
        if (amount <= 0) {
            throw new BankException("Nice try, trying to pay a negative amount" + amount);
        }
        if (!moneyBalances.containsKey(from)) {
            throw new BankException("Man this user is not in the balance sheets, how should we get money from him? " + from);
        }
        double fromAmount = moneyBalances.get(from);
        if (fromAmount < amount) {
            throw new BankException("We can not take more from this user as he has!" + from + " he only had: " + fromAmount + " but needed to pay " + amount);
        }
        double toAmount = 0;
        if (moneyBalances.containsKey(to)) {
            toAmount = moneyBalances.get(to);
        }
        moneyBalances.put(from, fromAmount - amount);
        moneyBalances.put(to, toAmount + amount);
    }

    /**
     * amount should still be positive
     */
    public void unfreeze(FreezeEvent event) throws BankException {
        unfreezeOneAccount(event.getUser(), event.getAmount());
    }

    public void freeze(FreezeEvent freezeEvent) throws BankException {
        freezeOneAccount(freezeEvent.getUser(), freezeEvent.getAmount());
    }

    private void unfreezeOneAccount(String user, double amount) throws BankException {
        if (!frozenBalance.containsKey(user)) {
            throw new BankException("Well no frozen money for the user " + user + " what should we unfreeze here?");
        }
        double frozen = frozenBalance.get(user);
        double newFrozen = frozen - amount;
        if (newFrozen < 0) {
            log.error("We tried to freeze under 0 this is weird, but this should never happen, new amount was: " + newFrozen);
            newFrozen = 0;
        }
        frozenBalance.put(user, newFrozen);
    }

    private void freezeOneAccount(String user, double amount) throws BankException {
        double frozen = 0;
        if (!moneyBalances.containsKey(user)) {
            throw new BankException("This user does not exist in the bank " + user);
        }
        double money = moneyBalances.get(user);
        if (frozenBalance.containsKey(user)) {
            frozen = frozenBalance.get(user);
        }
        if (money - frozen >= amount) {
            this.frozenBalance.put(user, frozen + amount);
        } else {
            throw new BankException("User with name: " + user + " did not have enough money");
        }
    }

    public static Bank getInstance() {
        return instance;
    }

}
