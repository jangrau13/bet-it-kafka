# Process Agreements

zeebe_addon to pipe all stuff to zeebee, listener camunda.*

## 1. Create Customer Done
Start via Kafka

## 1.2 Bank
Bank has in-memory userlist and money and freezed money

## 1.5 Game
game.published, game.started, game.ended (events) via rest api

## 2. Create Contract
1. Creating via bet-platform api
2. Create Contract zeebe workflow via zeebe_addon topic: camunda.contract_request, (messageName=CONTRACT_REQUEST, ...contract)
3. Workflow:
   1. User valid rest API provided by bank -> camunda.contract_rejected, (...reason)
   2. Game exist and not started game-master-rest-api -> camunda.contract_rejected, (...reason)
   3. If all good -> camunda.contract_accepted
   4. Check if event got sent

## 3. Create Bet
1. Create bet (bet-platform rest api)
2. Create Bet zeebe workflow via zeebe_addon topic: camunda.bet_request, (messageName=BET_REQUEST, ...bet)
3. Bet-Workflow
4. 
   3. freeze account kafka event: topic: bank.freeze_request, wait for: camunda.bank_freeze_rejected, camunda.bank_freeze_accepted
   4. Check if game not started (rest api game_master)
   5. Bet created event (bet-platform) bet_accepted, bet_rejected topic 
   6. Bet_Platform has list of running of accepted bets, game.ended -> camunda.bet_ended (messageName: BET_ENDED, correlation_id, ...betResult)
   7. pay bet kafka event: topic: bank.pay_bet, wait for: camunda.bank_paid
   8. bet.resolved
   9. Fraud Detector 
   10. If fraud -> bank.payment_reverse
   11. bet.fraud_detected





