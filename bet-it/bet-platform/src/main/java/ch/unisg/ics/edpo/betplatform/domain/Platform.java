package ch.unisg.ics.edpo.betplatform.domain;

import ch.unisg.ics.edpo.shared.transfer.ContractData;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

import static ch.unisg.ics.edpo.shared.Keys.GAME_ID;

@Slf4j
public class Platform {
    private static final Platform instance = new Platform();
    private final HashMap<String, ContractData> contracts = new HashMap<>();
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

    public ContractData getContract(String contractId) {
        return contracts.get(contractId);
    }

}






