package ch.unisg.camunda.service;

import ch.unisg.camunda.util.VariablesUtil;
import ch.unisg.camunda.util.WorkflowLogger;
import ch.unisg.ics.edpo.shared.bidding.ReserveBid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    //private final RuntimeService runtimeService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

  //  public boolean checkScore(ReserveBid reserveBid, String messageName) {

//        String traceId = UUID.randomUUID().toString();
//        String cName = reserveBid.getBid().getBuyerName();
//
//        WorkflowLogger.info(logger, "Score checking", " for customer: " + cName);
//
//        Map<String, Object> variables = null;
//        try {
//            variables = VariablesUtil.toVariableMap(reserveBid);
//        } catch (IntrospectionException e) {
//            throw new RuntimeException(e);
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//        variables.put("customer", cName);
//        variables.put("biddingId", reserveBid.getBid().getBidId());
//
//        runtimeService.startProcessInstanceByKey("test_bid",
//                traceId, variables);
//
//        return true;
//    }

}
