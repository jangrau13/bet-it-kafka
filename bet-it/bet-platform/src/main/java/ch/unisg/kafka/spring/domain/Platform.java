package ch.unisg.kafka.spring.domain;

import ch.unisg.ics.edpo.shared.bidding.Bid;
import ch.unisg.ics.edpo.shared.bidding.Contract;
import ch.unisg.ics.edpo.shared.bidding.ReserveBid;
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



    public void handleBankResponse(BankResponse bankResponse){
        logger.info("All bidding ids are : " + bids.keySet());
        logger.info("The id we are looking for is : " + bankResponse.getBiddingId());
        Bid bid = bids.get(bankResponse.getBiddingId());
        bid.setBidState(bankResponse.getBidState());
    }


    public ReserveBid addBid(Bid bid){
        if(!contracts.containsKey(bid.getContractId())){
            return null;
        }

        Contract contract = contracts.get(bid.getContractId());
        bids.put(bid.getBidId(), bid);
        return new ReserveBid(bid, contract);
    }

    public boolean addContract(Contract contract) {
        if(games.containsKey(contract.getGameId())){
            contracts.put(contract.getContractId(), contract);
            return true;
        }
        return false;
    }

    public void updateGame(Game game){
        if (game.isHasEnded()){
            logger.info("A game has finished");
            contracts.values().stream()
                    .filter(contract -> contract.getGameId().equals(game.getId()))
                    .forEach((this::handleContractsOfFinishedGame));

            games.remove(game.getId());
        } else {
            games.put(game.getId(), game);
        }
    }


    private void handleContractsOfFinishedGame(Contract contract){
        logger.info("We're handling a contract from " + contract.getWriterName() + " with id: " + contract.getContractId());
        bids.values().stream()
                .filter(bid -> bid.getContractId().equals(contract.getContractId()))
                .forEach(bid -> makePayments(bid, contract));
        contracts.remove(contract.getContractId());
    }

    /**
     * Check if bid actually got accepted before making any payments
     */
    private void makePayments(Bid bid, Contract contract) {
        logger.info("Some payments need to happen asap!");
    }

}
