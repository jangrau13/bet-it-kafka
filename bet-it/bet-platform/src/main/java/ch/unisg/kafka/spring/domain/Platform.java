package ch.unisg.kafka.spring.domain;

import ch.unisg.ics.edpo.shared.bidding.Bid;
import ch.unisg.ics.edpo.shared.bidding.Contract;
import ch.unisg.ics.edpo.shared.bidding.ReserveBid;
import ch.unisg.ics.edpo.shared.checking.BankResponse;
import ch.unisg.ics.edpo.shared.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.unisg.ics.edpo.shared.Keys.*;

public class Platform {

    private static final Platform instance = new Platform();



    private HashMap<String, List<String>> gamesToContracts = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> contractsToBets = new HashMap<String, List<String>>();

    private HashMap<String, Object> contracts = new HashMap<>();
    private HashMap<String, Object> bets = new HashMap<>();
    private HashMap<String, Object> games = new HashMap<>();
    //private final Map<String, Bid> bids = new HashMap<>();
    //private final Map<String, Contract> contracts = new HashMap<>();
    //private final Map<String, Game> games = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(Platform.class);

    // private constructor to avoid client applications using the constructor
    private Platform(){}

    public static Platform getInstance() {
        return instance;
    }

    /**
     * just a placeholder until KSQL
     * @param game
     */
    public void putGame(HashMap<String, Object> game){
        if(game.containsKey(GAME_ID)){
            games.put(game.get(GAME_ID).toString(), game);
        }
    }

    /**
     * just a placeholder until KSQL
     * @param game
     */
    public void putContract(HashMap<String, Object> contract){
        if(contract.containsKey(CONTRACT_ID)){
            contracts.put(contract.get(CONTRACT_ID).toString(), contract);
        }
    }

    /**
     * just a placeholder until KSQL
     * @param game
     */
    public void putBet(HashMap<String, Object> bets){
        if(bets.containsKey(BET_ID)){
            bets.put(bets.get(BET_ID).toString(), bets);
        }
    }

    /**
     * calculates the amounts to be freezed and adds it to the bet
     * @param betId
     * @return
     */
    public HashMap<String, Object> getToBeFreezedBet(String betId){
        HashMap<String, Object> errorMap = new HashMap<>();
        errorMap.put(AMOUNT_TO, 100);
        errorMap.put(AMOUNT_FROM, 100);
        errorMap.put(CONTRACTOR_NAME, ERROR_USER);
        errorMap.put(BIDDER_NAME, ERROR_USER);
        if(bets.containsKey(betId)){
            HashMap<String, Object> bet = (HashMap<String, Object>) bets.get(betId);
            int amountContractor = 0;
            int amountBidder = 0;
            if(bet.containsKey(AMOUNT) && bet.containsKey(BIDDER_NAME) && bet.containsKey(CONTRACT_ID)){
                if(bet.containsKey(CONTRACT_ID)){
                    HashMap<String, Object> contract = (HashMap<String, Object>) contracts.get(CONTRACT_ID);
                    if(contract.containsKey(RATIO)){
                        float ratio = Float.parseFloat(contract.get(RATIO).toString());
                        float amount = Float.parseFloat(bet.get(AMOUNT).toString());
                        amountContractor = (int) (ratio * amount);
                        amountBidder = (int) ((1/ratio) * amount);
                        bet.put(AMOUNT_BIDDER, amountBidder);
                        bet.put(AMOUNT_CONTRACTOR, amountContractor);
                        return bet;
                    }
                }
            }
        }
        return errorMap;
    }

    /**
     * calculates the amounts to be paid and adds it to the bet
     * @param betId
     * @return
     */
    public HashMap<String, Object> getToBePaidBet(String betId){
        HashMap<String, Object> errorMap = new HashMap<>();
        errorMap.put(AMOUNT_TO, 100);
        errorMap.put(AMOUNT_FROM, 100);
        errorMap.put(CONTRACTOR_NAME, ERROR_USER);
        errorMap.put(BIDDER_NAME, ERROR_USER);
        if(bets.containsKey(betId)){
            HashMap<String, Object> bet = (HashMap<String, Object>) bets.get(betId);
            int amountContractor = 0;
            int amountBidder = 0;
            if(bet.containsKey(AMOUNT) && bet.containsKey(BIDDER_NAME) && bet.containsKey(CONTRACT_ID) ){
                if(bet.containsKey(CONTRACT_ID)){
                    String contractId = bet.get(CONTRACT_ID).toString();
                    HashMap<String, Object> contract = (HashMap<String, Object>) contracts.get(contractId);
                    if(contract.containsKey(RATIO) && contract.containsKey(HOME_TEAM_WINS_BET) && contract.containsKey(GAME_ID)){
                        String gameId = contract.get(GAME_ID).toString();
                        if(games.containsKey(gameId)){
                            HashMap<String, Object> game = (HashMap<String, Object>) games.get(gameId);
                            float ratio = Float.parseFloat(contract.get(RATIO).toString());
                            float amount = Float.parseFloat(bet.get(AMOUNT).toString());
                            amountContractor = (int) (ratio * amount);
                            amountBidder = (int) ((1/ratio) * amount);
                            bet.put(AMOUNT_BIDDER, amountBidder);
                            bet.put(AMOUNT_CONTRACTOR, amountContractor);
                            boolean homeTeamWon = (boolean) game.get(HOME_TEAM_WINS);
                            boolean homeTeamWonBet = (boolean) contract.get(HOME_TEAM_WINS_BET);
                            if(homeTeamWonBet == homeTeamWon){
                                bet.put(PAYMENT, amountContractor);
                                bet.put(PAYER, amountContractor);
                                bet.put(PAYEE, amountBidder);
                            }
                        }
                    }
                }
            }
        }
        return errorMap;
    }

    /**
     * just a placeholder until KSQL
     * @param game
     */
    public void addContractToGame(String gameId, String contractId){

        if(gamesToContracts.containsKey(gameId)){
            List<String> listOfContractsForGame = gamesToContracts.get(gameId);
            listOfContractsForGame.add(gameId);
        }else{
            List<String> listOfContractsForGame = new ArrayList<>();
            listOfContractsForGame.add(contractId);
        }
    }

    /**
     * just a placeholder until KSQL
     * @param game
     */
    public void removeContractFromGame(String gameId, String contractId){
        if(gamesToContracts.containsKey(gameId)){
            List<String> listOfContractsForGame = gamesToContracts.get(gameId);
            if(listOfContractsForGame.contains(contractId)){
                listOfContractsForGame.remove(contractId);
            }
        }
    }

    /**
     * just a placeholder until KSQL
     * @param game
     */
    public List<String> contractsForGame(String gameId){
        if(gamesToContracts.containsKey(gameId)){
            return gamesToContracts.get(gameId);
        }else{
            return new ArrayList<>();
        }
    }

    /**
     * just a placeholder until KSQL
     * @param game
     */
    public void addBetToContract(String contractId, String betId){

        if(contractsToBets.containsKey(contractId)){
            List<String> listOfBetsForContract = contractsToBets.get(contractId);
            listOfBetsForContract.add(contractId);
        }else{
            List<String> listOfBetsForContract = new ArrayList<>();
            listOfBetsForContract.add(betId);
        }
    }

    /**
     * just a placeholder until KSQL
     * @param game
     */
    public void removeBetFromContract(String gameId, String contractId){
        if(contractsToBets.containsKey(gameId)){
            List<String> listOfBetsForContract = contractsToBets.get(gameId);
            if(listOfBetsForContract.contains(contractId)){
                listOfBetsForContract.remove(contractId);
            }
        }
    }

    /**
     * just a placeholder until KSQL
     * @param game
     */
    public List<String> betsForContracts(String contractId){
        if(contractsToBets.containsKey(contractId)){
            return contractsToBets.get(contractId);
        }else{
            return new ArrayList<>();
        }
    }
    /**
     * just a placeholder until KSQL
     * @param game
     */
    public List<String> betsForGame(String gameId){
        logger.info("getting bets for game {}", gameId);
        if(gamesToContracts.containsKey(gameId)){
            List<String> bets = gamesToContracts.get(gameId).stream()
                    .flatMap(contractId -> contractsToBets.containsKey(contractId)
                            ? contractsToBets.get(contractId).stream()
                            : Stream.empty())
                    .collect(Collectors.toList());
            logger.info("found: {}", bets);
            return bets;
        }else{
            logger.info("found nothing");
            return new ArrayList<>();
        }
    }

//
//    public void handleBankResponse(BankResponse bankResponse){
//        logger.info("All bidding ids are : " + bids.keySet());
//        logger.info("The id we are looking for is : " + bankResponse.getBiddingId());
//        Bid bid = bids.get(bankResponse.getBiddingId());
//        bid.setBidState(bankResponse.getBidState());
//    }
//
//
//    public ReserveBid addBid(Bid bid){
//        if(!contracts.containsKey(bid.getContractId())){
//            return null;
//        }
//
//        Contract contract = contracts.get(bid.getContractId());
//        bids.put(bid.getBidId(), bid);
//        return new ReserveBid(bid, contract);
//    }
//
//    public boolean addContract(Contract contract) {
//        if(games.containsKey(contract.getGameId())){
//            contracts.put(contract.getContractId(), contract);
//            return true;
//        }
//        return false;
//    }
//
//    public void updateGame(Game game){
//        if (game.isHasEnded()){
//            logger.info("A game has finished");
//            contracts.values().stream()
//                    .filter(contract -> contract.getGameId().equals(game.getId()))
//                    .forEach((this::handleContractsOfFinishedGame));
//
//            games.remove(game.getId());
//        } else {
//            games.put(game.getId(), game);
//        }
//    }
//
//
//    private void handleContractsOfFinishedGame(Contract contract){
//        logger.info("We're handling a contract from " + contract.getWriterName() + " with id: " + contract.getContractId());
//        bids.values().stream()
//                .filter(bid -> bid.getContractId().equals(contract.getContractId()))
//                .forEach(bid -> makePayments(bid, contract));
//        contracts.remove(contract.getContractId());
//    }
//
//    /**
//     * Check if bid actually got accepted before making any payments
//     */
//    private void makePayments(Bid bid, Contract contract) {
//        logger.info("Some payments need to happen asap!");
//    }

}
