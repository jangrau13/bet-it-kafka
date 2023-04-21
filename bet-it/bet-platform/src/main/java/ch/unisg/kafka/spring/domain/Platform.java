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
            this.bets.put(bets.get(BET_ID).toString(), bets);
        }
    }

    /**
     * calculates the amounts to be freezed and adds it to the bet
     * @param betId
     * @return
     */
    public HashMap<String, Object> getToBeFreezedBet(String betId){
        logger.info("to be freezed with id: {}", betId);
        logger.info("to be freezed with bets: {}", bets);
        logger.info("to be freezed with games: {}", games);
        logger.info("to be freezed with contracts: {}", contracts);
        HashMap<String, Object> errorMap = new HashMap<>();
        errorMap.put(AMOUNT_TO, 100);
        errorMap.put(AMOUNT_FROM, 100);
        errorMap.put(CONTRACTOR_NAME, ERROR_USER);
        errorMap.put(BIDDER_NAME, ERROR_USER);
        if(this.bets.containsKey(betId)){
            HashMap<String, Object> bet = (HashMap<String, Object>) bets.get(betId);
            logger.info("from bets by id: {}", bet);
            int amountContractor = 0;
            int amountBidder = 0;
            if(bet != null && bet.containsKey(AMOUNT) && bet.containsKey(BIDDER_NAME) && bet.containsKey(CONTRACT_ID)){
                String contractId = bet.get(CONTRACT_ID).toString();
                HashMap<String, Object> contract = (HashMap<String, Object>) contracts.get(contractId);
                logger.info("from contract by id: {}", contract);
                    if(contract != null && contract.containsKey(RATIO) && contract.containsKey(CONTRACTOR_NAME)){
                        float ratio = Float.parseFloat(contract.get(RATIO).toString());
                        float amount = Float.parseFloat(bet.get(AMOUNT).toString());
                        String contractorName = contract.get(CONTRACTOR_NAME).toString();
                        amountContractor = (int) (ratio * amount);
                        amountBidder = (int) amount;
                        bet.put(AMOUNT_BIDDER, String.valueOf(amountBidder));
                        bet.put(AMOUNT_CONTRACTOR, String.valueOf(amountContractor));
                        bet.put(CONTRACTOR_NAME, contractorName);
                        return bet;
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
            if(bet != null && bet.containsKey(AMOUNT) && bet.containsKey(BIDDER_NAME) && bet.containsKey(CONTRACT_ID) ){
                if(bet.containsKey(CONTRACT_ID)){
                    String contractId = bet.get(CONTRACT_ID).toString();
                    HashMap<String, Object> contract = (HashMap<String, Object>) contracts.get(contractId);
                    if(contract != null && contract.containsKey(RATIO) && contract.containsKey(HOME_TEAM_WINS_BET) && contract.containsKey(GAME_ID)){
                        String gameId = contract.get(GAME_ID).toString();
                        logger.info("from contract by id: {}", contract);
                        if(games != null && games.containsKey(gameId)){
                            HashMap<String, Object> game = (HashMap<String, Object>) games.get(gameId);
                            logger.info("from game by id: {}", game);
                            float ratio = Float.parseFloat(contract.get(RATIO).toString());
                            float amount = Float.parseFloat(bet.get(AMOUNT).toString());
                            amountContractor = (int) (ratio * amount);
                            amountBidder = (int) amount;
                            bet.put(AMOUNT_BIDDER, amountBidder);
                            bet.put(AMOUNT_CONTRACTOR, amountContractor);
                            boolean homeTeamWon = Boolean.getBoolean(game.get(HOME_TEAM_WINS).toString());
                            boolean homeTeamWonBet = Boolean.getBoolean(contract.get(HOME_TEAM_WINS_BET).toString());
                            if(homeTeamWonBet == homeTeamWon){
                                bet.put(PAYMENT, amountContractor);
                                bet.put(PAYER, amountContractor);
                                bet.put(PAYEE, amountBidder);
                            }
                            return bet;
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
        logger.info("adding contract {} to game {}", contractId, gamesToContracts);
        addToMap(gameId, contractId, gamesToContracts);
        logger.info("added contract {} to game {}", contractId, gamesToContracts);
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
        logger.info("adding bet {} to contract {}", betId, contractsToBets);
        addToMap(contractId, betId, contractsToBets);
        logger.info("added bet {} to contract {}", betId, contractsToBets);
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
        logger.info("in following list: {}", gamesToContracts );
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


    private void addToMap(String gameId, String contractId, HashMap<String, List<String>> gamesToContracts) {
        if(gamesToContracts.containsKey(gameId)){
            List<String> listOfContractsForGame = gamesToContracts.get(gameId);
            listOfContractsForGame.add(gameId);
            gamesToContracts.put(gameId,listOfContractsForGame);
        }else{
            List<String> listOfContractsForGame = new ArrayList<>();
            listOfContractsForGame.add(contractId);
            gamesToContracts.put(gameId,listOfContractsForGame);
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
