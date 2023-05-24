package ch.unisg.ics.edpo.shared;

public class Topics {


    public static class Bank {
        public static class Freeze {
            public static final String FREEZE_REQUEST = "bank.freeze-request";
            public static final String FREEZE_RESULT = "camunda.freeze-result";

        }
        public static class Transaction {
            public static final String TRANSACTION_REQUEST = "bank.transaction-request";

            public static final String TRANSACTION_RESULT = "camunda.transaction-result";

        }
        public static class User {
            public static final String CHECK_REQUEST = "user.check-request";
            public static final String CHECK_RESULT = "camunda.user.check-result";
        }
    }

    public static class Contract {
        public static final String CONTRACT_REQUESTED = "camunda.contract.requested";
        public static final String CONTRACT_REJECTED = "contract.rejected";
        public static final String CONTRACT_ACCEPTED = "contract.accepted";
        public static final String CONTRACT_EXPIRED = "contract.expired";
    }

    public static class Game {

        public static final String GAME_VALID_FOR_CONTRACT_REQUEST = "game.valid-for-contract-request";

        public static final String GAME_VALID_FOR_CONTRACT_RESULT = "camunda.game.valid-for-contract-result";

        public static final String GAME_PUBLISHED = "game.published";

        public static final String GAME_ENDED = "camunda.game.ended";

        public static final String GAME_STARTED = "camunda.game.started";

        public static class GameDot {
            public static final String DOT_HITS = "game.dot.hit";

            public static final String DOT_MISSES = "game.dot.miss";

            public static final String DOT_SPAWN = "game.dot.spawn";
        }
    }

    public static class Bet {
        public static final String BET_REQUESTED = "camunda.bet.requested";

        public static final String BET_REJECTED = "bet.rejected";

        public static final String BET_ACCEPTED = "bet.accepted";

        public static final String BET_DONE = "bet.done";

        public static final String BET_ERROR = "bet.error";
    }
}

