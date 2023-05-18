package ch.unisg.ics.edpo.shared;

public class Keys {

    public static final String PAYMENT = "payment";
    public static final String PAYER = "payer";
    public static final String PAYEE = "payee";
    public static final String HOME_TEAM_WINS = "homeTeamWins";
    public static final String AMOUNT_CONTRACTOR = "amountContractor";

    public static final String HOME_TEAM_WINS_BET = "homeTeamWinsBet";
    public static final String GAME_ID = "gameId";
    public static final String AMOUNT_TO = "amountTo";
    public static final String AMOUNT_FROM = "amountFrom";
    public static final String CONTRACTOR_NAME = "contractorName";
    public static final String BIDDER_NAME = "bidderName";
    public static final String ERROR_USER = "errorUser";
    public static final String AMOUNT = "amount";
    public static final String CONTRACT_ID = "contractId";
    public static final String AMOUNT_BIDDER = "amountBidder";
    public static final String BET_ID = "betId";
    public static final String GAME_NAME = "gameName";
    public static final String STATE = "state";
    public static final String PUBLISHED = "PUBLISHED";
    public static final String STARTED = "STARTED";
    public static final String ENDED = "ENDED";
    public static final String HOME_GOALS = "homeGoals";
    public static final String AWAY_GOALS = "awayGoals";
    public static final String CONTRACT_REQUESTED = "CONTRACT_REQUESTED";
    public static final String MESSAGE_NAME = "messageName";
    public static final String FRAUD_DETECTED = "FRAUD_DETECTED";
    public static final String GAME_ENDED = "GAME_ENDED";

    public static final String CORRELATION_ID = "correlationId";

    public static class ContractDataKeys {

        public static final String CONTRACT_RATIO_FIELD = "ratio";

        public static final String GAME_ID_FIELD = "gameId";

        public static final String CONTRACTOR_NAME_FIELD = "contractorName";

        public static final String HOME_TEAM_WINS_FIELD = "homeTeamWins";

        public static final String CONTRACT_ID_FIELD = "contractId";


    }

    public static class FreezeEventKeys {

        public static final String USERS = "users";
        public static final String AMOUNTS = "amounts";
        public static final String STATUS_FIELD = "status";
        public static final String CORRELATION_ID = "correlationId";
    }

    public static class TransactionEventKeys {
        public static final String FROM_FIELD = "from";
        public static final String TO_FIELD = "to";
        public static final String AMOUNT_FIELD = "amount";
        public static final String STATUS_FIELD = "status";

        public static final String WITH_UNFREEZE_FIELD = "with-unfreeze";
    }

    public static class UserCheckKeys {
        public static final String USER_FIELD = "user";

        public static final String RESULT = "user-check-result";

        public static final String CORRELATION_ID = Keys.CORRELATION_ID;

    }



}
