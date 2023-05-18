package ch.unisg.ics.edpo.shared;

import ch.unisg.ics.edpo.shared.game.Game;

public class Topics {


    public static class Bank {
        public static class Freeze {
            public static String FREEZE_REQUEST = "bank.freeze-request";
            public static String FREEZE_RESULT = "camunda.freeze-result";
        }
        public static class Transaction {
            public static String TRANSACTION_REQUEST = "bank.transaction-request";

            public static String TRANSACTION_RESULT = "camunda.transaction-result";

        }
        public static class User {
            public static String CHECK_REQUEST = "user.check-request";
            public static String CHECK_RESULT = "camunda.user.check-result";
        }
    }

    public static class Contract {
        public static String CONTRACT_REQUESTED = "camunda.contract.requested";
        public static String CONTRACT_REJECTED = "contract.rejected";
        public static String CONTRACT_ACCEPTED = "contract.accepted";
        public static String CONTRACT_EXPIRED = "contract.expired";
    }

    public static class Game {

        public static String GAME_VALID_FOR_CONTRACT_REQUEST = "game.valid-for-contract-request";

        public static String GAME_VALID_FOR_CONTRACT_RESULT = "camunda.game.valid-for-contract-result";
    }
}

