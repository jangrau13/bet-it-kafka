package ch.unisg.ics.edpo.betplatform.domain;

import ch.unisg.ics.edpo.shared.transfer.ContractData;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

import static ch.unisg.ics.edpo.shared.Keys.GAME_ID;

@Slf4j
public class Platform {
    private static final Platform instance = new Platform();
    private final HashMap<String, ContractData> contracts = new HashMap<>();

    private final HashMap<String, ContractData> rejectedContracts = new HashMap<>();

    private final HashMap<String, ContractData> expiredContracts = new HashMap<>();
    private final HashMap<String, Object> games = new HashMap<>();

    private Platform(){}

    public static Platform getInstance() {
        return instance;
    }

    /**
     * just a placeholder until KSQL
     */
    public void putGame(HashMap<String, Object> game){
        if(game.containsKey(GAME_ID)){
            games.put(game.get(GAME_ID).toString(), game);
        }
    }

    /**
     * put it after listening to the contract event from the workflow
     */
    public void putContract(ContractData contract){
        this.contracts.put(contract.getContractId(), contract);
    }

    public void removeContract(ContractData contractData) {
        this.contracts.remove(contractData.getContractId());
    }

    public void addRejectedContract(ContractData contractData) {
        this.rejectedContracts.put(contractData.getContractId(), contractData);
    }

    public void addExpiredContract(ContractData contractData) {
        this.expiredContracts.put(contractData.getContractId(), contractData);
    }
    public String getContractState(String contractId){
       if(this.contracts.get(contractId) != null){
           return "Accepted";
       } else if (this.rejectedContracts.get(contractId) != null) {
           return "Rejected";
       } else if(this.expiredContracts.get(contractId) != null){
           return "Expired";
       }
        return "Not found";
    }
    public ContractData getContract(String contractId) {
        return contracts.get(contractId);
    }

}






