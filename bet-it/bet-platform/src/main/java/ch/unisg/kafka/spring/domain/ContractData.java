package ch.unisg.kafka.spring.domain;

import ch.unisg.ics.edpo.shared.Keys;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ContractData {

    @Getter
    private final String gameId;
    @Getter
    private final double ratio;
    @Getter
    private final String contractorName;
    @Getter
    private final boolean homeTeamWins;
    @Getter
    private final String contractId;


    // Constructor to initialize all parameters
    public ContractData(String gameId, double ratio, String contractorName, boolean homeTeamWins, String contractId) {
        this.gameId = gameId;
        this.ratio = ratio;
        this.contractorName = contractorName;
        this.homeTeamWins = homeTeamWins;
        this.contractId = (contractId != null && !contractId.isEmpty()) ? contractId : UUID.randomUUID().toString();
    }

    public ContractData(Map<String, Object> map) {
        validateMap(map);
        this.gameId = (String) map.get(Keys.ContractDataKeys.GAME_ID_FIELD);
        this.ratio = (Double) map.get(Keys.ContractDataKeys.CONTRACT_RATIO_FIELD);
        this.contractorName = (String) map.get(Keys.ContractDataKeys.CONTRACTOR_NAME_FIELD);
        this.homeTeamWins = (Boolean) map.get(Keys.ContractDataKeys.HOME_TEAM_WINS_FIELD);
        this.contractId = getContractId(map);
    }

    private String getContractId(Map<String,Object> map){
        Object value = map.get(Keys.ContractDataKeys.CONTRACT_ID_FIELD);
        if (value != null) {
            return (String) value;
        } else {
            return UUID.randomUUID().toString();
        }
    }

    // Generate a Map<String, Object> representation of the object
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("gameId", gameId);
        map.put("ratio", ratio);
        map.put("contractorName", contractorName);
        map.put("homeTeamWins", homeTeamWins);
        map.put("contractId", contractId);
        return map;
    }

    // Populate the object from a Map<String, Object>

    private void validateMap(Map<String, Object> map) {
        Object gameIdObj = map.get("gameId");
        Object ratioObj = map.get("ratio");
        Object contractorNameObj = map.get("contractorName");
        Object homeTeamWinsObj = map.get("homeTeamWins");

        if (gameIdObj == null) {
            throw new IllegalArgumentException("gameId cannot be null");
        }
        if (!(gameIdObj instanceof String)) {
            throw new IllegalArgumentException("gameId must be of type String");
        }
        if (ratioObj == null) {
            throw new IllegalArgumentException("ratio cannot be null");
        }
        if (!(ratioObj instanceof Double)) {
            throw new IllegalArgumentException("ratio must be a numeric value");
        }

        if (contractorNameObj == null) {
            throw new IllegalArgumentException("contractorName cannot be null");
        }
        if (!(contractorNameObj instanceof String)) {
            throw new IllegalArgumentException("contractorName must be of type String");
        }
        if (homeTeamWinsObj == null) {
            throw new IllegalArgumentException("homeTeamWins cannot be null");
        }
        if (!(homeTeamWinsObj instanceof Boolean)) {
            throw new IllegalArgumentException("homeTeamWins must be a boolean value");
        }

    }

}
