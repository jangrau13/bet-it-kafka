package ch.unisg.service;

import ch.unisg.ics.edpo.shared.bank.FreezeEvent;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class FreezeEventTest {

    @Test
    public void testMe(){
        double[] amounts = {123, 12.5};
        String[] users = {"user1", "user2"};
        Map<String, Object> map = new HashMap<>();
        map.put("correlationId", "123");
        map.put("amounts", amounts);
        map.put("users", users);
        map.put("status", "REQUESTED");
        FreezeEvent request = new FreezeEvent(map);
        FreezeEvent request2 = new FreezeEvent(request.toMap());
    }
}