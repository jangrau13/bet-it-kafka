# Process Agreements

## 1. Create Customer
1. Call Customer API (name, password, bankpassword?), where: bet-platform
2. API Fire Camunda Workflow over zeebe
3. Zeebe calls bank to check if user with that bankpassword exists
4. Include Task List to check for passport? (We dont send passport image), to maybe check if its this person
5. If TaskList completed User is created

## 2. Create Contract
1. Creating via bet-platform api, Game needs to already exist
2. Create Contract zeebe workflow from bet-platform api
3. Workflow:
   1. User valid (user-api?)
   2. Game exist and not started (game-master api)
   3. Contract Created Event via bet_platform (kafka event)
   4. We could listen for created event

## 3. Create Bet
1. Create bet (bet-platform api)
2. Bet-Workflow start via zeebe
   1. Get Contract (bet-platform)
   2. Check user credentials (user-platform)
   3. Freeze both accounts money (Can fail) (bank)
   4. Check if game not started (can fail)
   5. Bet created event (bet-platform) Starts new contract

## 4. Running Bet
New Contract (started by bet created event)
   1. Wait for game to finish event (game_master fires game_finished event, open for discussion)
   2. Pay winner from looser (bank service)
   3. If bank payment fails -> tasklist?


## Open questions

How to we make the game_finished stuff into the contract? 
This potentially needs to be sent to multiple camunda workflows that are waiting for the game to end.