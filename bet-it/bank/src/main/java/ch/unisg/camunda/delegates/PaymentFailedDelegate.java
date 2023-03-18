package ch.unisg.camunda.delegates;

import ch.unisg.ics.edpo.shared.bidding.BidState;
import ch.unisg.ics.edpo.shared.checking.BankResponse;
import ch.unisg.ics.edpo.shared.checking.BankResponseType;
import ch.unisg.kafka.service.BankProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentFailedDelegate implements JavaDelegate {

    private final BankProducerService<BankResponse> bankProducerService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("@@@@@@@@@@@@@Payment not successful {}", delegateExecution.getCurrentActivityId());
        String biddingId = (String) delegateExecution.getVariable("biddingId");
        log.info("@@@@@@@@@@@@@Payment not successful {}", biddingId);
        BankResponse bankResponse = new BankResponse(BankResponseType.PAYMENT_ATTEMPT, biddingId, BidState.PAYMENT_FAILED);
        bankProducerService.sendBankResponse(bankResponse);
    }
}