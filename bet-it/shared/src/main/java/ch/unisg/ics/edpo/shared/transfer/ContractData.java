package ch.unisg.ics.edpo.shared.transfer;

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
    /**
     * If team1 Winns = True the contractor winns in that case
     */
    @Getter
    private final boolean team1Winns;
    @Getter
    private final String contractId;


    // Constructor to initialize all parameters
    public ContractData(String gameId, double ratio, String contractorName, boolean team1Winns, String contractId) {
        this.gameId = gameId;
        this.ratio = ratio;
        this.contractorName = contractorName;
        this.team1Winns = team1Winns;
        this.contractId = (contractId != null && !contractId.isEmpty()) ? contractId : UUID.randomUUID().toString();
    }

    public ContractData(Map<String, Object> map) {
        validateMap(map);
        this.gameId = (String) map.get(Keys.ContractDataKeys.GAME_ID_FIELD);
        this.ratio = (Double) map.get(Keys.ContractDataKeys.CONTRACT_RATIO_FIELD);
        this.contractorName = (String) map.get(Keys.ContractDataKeys.CONTRACTOR_NAME_FIELD);
        this.team1Winns = (Boolean) map.get(Keys.ContractDataKeys.TEAM_ONE_WINNS);
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
        map.put(Keys.ContractDataKeys.GAME_ID_FIELD, gameId);
        map.put(Keys.ContractDataKeys.CONTRACT_RATIO_FIELD, ratio);
        map.put(Keys.ContractDataKeys.CONTRACTOR_NAME_FIELD, contractorName);
        map.put(Keys.ContractDataKeys.TEAM_ONE_WINNS, team1Winns);
        map.put(Keys.ContractDataKeys.CONTRACT_ID_FIELD, contractId);
        return map;
    }

    // Populate the object from a Map<String, Object>

    private void validateMap(Map<String, Object> map) {
        Object gameIdObj = map.get(Keys.ContractDataKeys.GAME_ID_FIELD);
        Object ratioObj = map.get(Keys.ContractDataKeys.CONTRACT_RATIO_FIELD);
        Object contractorNameObj = map.get(Keys.ContractDataKeys.CONTRACTOR_NAME_FIELD);
        Object homeTeamWinsObj = map.get(Keys.ContractDataKeys.TEAM_ONE_WINNS);

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
