package ch.unisg.ics.edpo.shared.transfer;

import ch.unisg.ics.edpo.shared.Keys;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class UserCheck {
    @Getter
    private final String user;

    @Getter @Setter
    private UserCheckResult result;

    @Getter
    private final String correlationKey;
    public UserCheck(String user, UserCheckResult userCheckResult) {
        this.user = user;
        this.result = userCheckResult;
        this.correlationKey = user;
    }
    public static UserCheck fromMap(Map<String, Object> map) {
        String user = (String) map.get(Keys.UserCheckKeys.USER_FIELD);
        String statusString = (String) map.get(Keys.UserCheckKeys.RESULT);
        UserCheckResult result = UserCheckResult.valueOf(statusString);

        return new UserCheck(user, result);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(Keys.UserCheckKeys.USER_FIELD, user);
        map.put(Keys.UserCheckKeys.RESULT, result.toString());
        map.put(Keys.UserCheckKeys.CORRELATION_ID, correlationKey);
        return map;
    }


    public enum UserCheckResult {
        APPROVED, REJECTED, REQUESTED
    }
}

