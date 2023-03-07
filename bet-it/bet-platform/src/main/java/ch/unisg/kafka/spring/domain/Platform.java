package ch.unisg.kafka.spring.domain;

import ch.unisg.ics.edpo.shared.bidding.Bid;
import ch.unisg.ics.edpo.shared.bidding.Contract;
import ch.unisg.ics.edpo.shared.checking.BankResponse;
import ch.unisg.ics.edpo.shared.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Platform {

    private static final Platform instance = new Platform();


    private final Map<String, Bid> bids = new HashMap<>();
    private final Map<String, Contract> contracts = new HashMap<>();
    private final Map<String, Game> games = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(Platform.class);

    // private constructor to avoid client applications using the constructor
    private Platform(){}

    public static Platform getInstance() {
        return instance;
    }


    private void checkIfBothValid(Bid bid, Contract contract){
    }

    public void handleBankResponse(BankResponse bankResponse){
        Bid bid = bids.get(bankResponse.getBiddingId());
        bid.setBidState(bankResponse.getBidState());
    }


    public void addBid(Bid bid){
        Contract contract = contracts.get(bid.getContractId());
        bids.put(bid.getBidId(), bid);
        checkIfBothValid(bid, contract);
    }

    public void addContract(Contract contract) {
        contracts.put(contract.getContractId(), contract);
    }

    public void updateGame(Game game){
        if (game.isHasEnded()){
            logger.info("A game has finished");
            contracts.values().stream()
                    .filter(contract -> contract.getGameId().equals(game.getId()))
                    .forEach((this::handleContractsOfFinishedGame));
        }
        games.remove(game.getId());
    }


    private void handleContractsOfFinishedGame(Contract contract){
        bids.values().stream()
                .filter(bid -> bid.getContractId().equals(contract.getContractId()))
                .forEach(bid -> makePayments(bid, contract));
        contracts.remove(contract.getContractId());
    }

    /**
     * Check if bid actually got accepted before making any payments
     */
    private void makePayments(Bid bid, Contract contract) {
    }

}
