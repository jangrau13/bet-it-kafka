package io.flowing.retail.monitor.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flowing.retail.monitor.domain.PastEvent;

public class LogRepository {
  
  public static LogRepository instance = new LogRepository();
  
  private Map<String, List<PastEvent>> events = new HashMap<String, List<PastEvent>>();
  private Map<String, List<String>> payloads = new HashMap<String, List<String>>();

  public Map<String, List<PastEvent>> getAllPastEvents() {
    return events;    
  }

  public Map<String, List<String>> getAllPastPayloads(){return payloads;}

  public List<PastEvent> getAllPastEvents(String transactionId) {
    return events.get(transactionId);
  }

  public void addEvent(PastEvent pastEvent) {
    if (!events.containsKey(pastEvent.getTransactionId())) {
      events.put(pastEvent.getTransactionId(), new ArrayList<PastEvent>());
    }
    events.get(pastEvent.getTransactionId()).add(pastEvent);
  }

  public void addPayload(String lastPayload) {
    if (!payloads.containsKey(lastPayload)) {
      payloads.put(lastPayload, new ArrayList<String>());
    }
    payloads.get(lastPayload).add(lastPayload);
  }

}
