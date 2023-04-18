# Process Agreements

## 1. Create Customer
1. Call Customer API (name, password, bankpassword?), where: bet-platform :: creating a customer should be done on the bank MS 
2. API Fire Camunda Workflow over zeebe
3. Zeebe calls bank to check if user with that bankpassword exists
4. Include Task List to check for passport? (We dont send passport image), to maybe check if its this person :: I don't want to implement this and this generates work load
5. If TaskList completed User is created :: user creation all on the bank side (already implemented)

## 2. Create Contract
1. Creating via bet-platform api, Game needs to already exist
2. Create Contract zeebe workflow from bet-platform api
3. Workflow:
   1. User valid (user-api?) :: rest API provided by bank
   2. Game exist and not started (game-master api) :: can it be started already? it should not be done. What about replay? we should work with time stamps
   3. Contract Created Event via bet_platform (kafka event) :: can be done via Camunda more easily
   4. We could listen for created event

## 3. Create Bet
1. Create bet (bet-platform api)
2. Bet-Workflow start via zeebe
   1. Get Contract (bet-platform) :: why get contract, contract is part of the bet
   2. Check user credentials (user-platform) :: would not include that, too much for work for no benefit
   3. Freeze both accounts money (Can fail) (bank)
   4. Check if game not started (can fail)
   5. Bet created event (bet-platform) Starts new contract :: new contract event

## 4. Running Bet
New Contract (started by bet created event)
   1. Wait for game to finish event (game_master fires game_finished event, open for discussion) :: maybe game is a camunda process with fails and result etc
   2. Pay winner from looser (bank service) :: another camunda process
   3. If bank payment fails -> tasklist? 

## Jan
1. we need to include more possible errors. The whole contract should be error prone even after the payment, so that we can reverse the payment and cancel the contract. This can be done with a fraud service on a contract.


## Open questions

How to we make the game_finished stuff into the contract? 
This potentially needs to be sent to multiple camunda workflows that are waiting for the game to end. :: no problem just sending several messages with different "messageNames" to the camunda topic. And we should inverse the dependency. Camunda processes should be listening on the game_finished message. I think we can just have a Game_Finished front controller pattern.
