# Bet Platform

## Setup
install the following software:
- [Open Source Desktop Modeler](https://camunda.com/de/download/modeler/)

select Camunda Platform Version 8.2 (alpha) for all the documents created and creating.

## Build
mvn clean install -DskipTests
docker-compose up --build -d

## Development
Run frontend in docker
```shell
docker-compose -f bet-it/docker-compose.yml up fraud-frontend --build 
docker-compose -f bet-it/docker-compose.yml up fraud-backend --build
docker-compose -f bet-it/docker-compose.yml up bank --build
```

## Monitor Kafka
open [kafdrop](http://localhost:9000) and have a look at the topics

## add a new User
1. Open Desktop Modeler
2. Open file bet-it/zeebe-addon/src/main/resources/add_customer.bpmn
3. Deploy and Run with the commands in the Desktop Modeler. For instructions see here: ![](documentation/images/Deploy_And_Run_Camunda.png)
4. open [Zeebe Tasklist](http://localhost:8181) and login with user demo and password demo
5. Click on new Task ![](documentation/images/Tasklist_Start_Task.png)
6. Assign new Task to yourself ![](documentation/images/Tasklist_Assign_Task.png) 
7. Enter name and password and click "Complete Task"
8. open [Zeebe Operate](http://localhost:8180) and login with user demo and password demo
9. open Process Instance Dashboard ![](documentation/images/Operate_Look_at_Task.png)
10. select your created task ![](documentation/images/Operate_Select_Task.png)
11. copy "correlationId" to your clipboard ![](documentation/images/Operate_Get_CorrelationID.png)
12. POST localhost:8081/bank/twoFactor body: { "name": String, "correlationId": float, "password": String} with the correct user, password and correlationId you just copied
13. if you want to you can try it with a wrong password or user. After three tries the process will fail. Also if you take longer than one minute, the process will fail as well.
14. if done right, you can inspect [kafdrop](http://localhost:9000) for the topic bet.added-new-customer to see. Also intersting is the camunda topic. There you will see everything via the [Kafka Camunda API](#kafka-camunda-api)


## How to Create and Win a Bet
1. publish a game via the game-master 
```shell
curl - --location 'localhost:8083/game/publish' \
--data 'STRING :: your name of the game'
```
2. get the gameId from the response header
3. use it to create a contract via the bet platform
```shell
curl -v --location 'localhost:8082/platform/publishContract' \
--header 'Content-Type: application/json' \
--data '{
    "ratio": <FLOAT :: the ratio you want to set your contract (i.e.2.0 for 1 to 2 bets)>,
    "homeTeamWinsBet": <"False/True" :: to tell whether the Home Team will win>,
    "contractorName": <STRING :: not checked yet(since we are integrating Kafka Streams for this feature as well), however if you want to be save, take a user name that starts with "good" for a successfull result, "bad" for a declined result and "error" for an error-result>, 
    "gameId": <STRING :: gameId from above>
}'
```
4. use the [operate-service](http://localhost:8180) with demo/demo to see the state of the processes
5. get the contractId from the response header
6. go to the [tasklist](http://localhost:8181) with demo/demo and select the task "check risk manually". Check the box if you trust this user and continue
7. use the gameId and the contractId to create a bet via the bet platform
```shell
curl -v --location 'localhost:8082/platform/publishBet' \
--header 'Content-Type: application/json' \
--data '{
    "amount": <INTEGER :: the amount you want to bet on>,
    "bidderName": <STRING :: not checked yet(since we are integrating Kafka Streams for this feature as well), however if you want to be save, take a user name that starts with "good" for a successfull result, "bad" for a declined result and "error" for an error-result>, 
    "gameId": <STRING :: gameId from above>,
    "contractId": <STRING :: contractId from above>
}'
```
8. get the betId from the response
10. use the [operate-service](http://localhost:8180) with demo/demo to see the state of the processes
11. start the game via the game-platform
```shell
curl --location --request PUT 'localhost:8083/game/start' \
--data '<STRING :: gameId from above>'
```
11. end the game via the game-platform
```shell
curl --location --request PUT 'localhost:8083/game/end' \
--data '<STRING :: gameId from above>'
```
12. use the [operate-service](http://localhost:8180) with demo/demo to see the state of the processes
13. if detected, tell the riskmanagement that the bet was frauded:
```shell
curl --location --request PUT 'localhost:8083/game/end' \
--data '<STRING :: betId from above>'
```
14. use the [operate-service](http://localhost:8180) with demo/demo to see the state of the processes
15. if you don't detect any fraud, the bet will finalized after 5 minutes


## ToDos
- [ ] use Kafka Streaming to have a ledger
- [ ] use KSQL for the bet platform to keep the state of game, contracts and bets



## Kafka Camunda API
We were expecting Kafka and Camunda to operate more easily, so that you can tell camunda as a task (with an own implementation for every task) to post a topic on Kafka and for a Microservice to start a Camunda Process by posting something on Kafka.
In order to achieve this we implemented this API by ourselves in the bank microservice, where we are using the Camunda Tool. 
You can find the implemenation [here](zeebe-addon/src/main/java/ch/unisg) .
This API is transforming Kafka request from/to Camunda to/from Kafka depending on the map keys "messageName" and "topic". 
If the request to the camunda topic contains a "messageName" the API will create the camunda process with that name (if it exists). 
If the request does not contain a "messageName", but a topic it will post the variables of Camunda to the topic specified in the payload.
In order to use this feature from Camunda, a [ZeebeWorker](zeebe-addon/src/main/java/ch/unisg/ics/edpo/zeebe/ZeebeListener.java) was created, which automatically send the Camunda task to the camunda topic.
An example of how to use this feature from the Camunda side, please refer to [add_customer.bpmn](bank/src/main/resources/add_customer.bpmn).
An example of how to use this feature from the Microservice side, please refer to [BankController.java](bank/src/main/java/ch/unisg/controller/BankController.java)

### Add Connector Templates
If you want to use templates for the Camunda Desktop Modeler download the templates and follow the [instructions](https://docs.camunda.io/docs/self-managed/connectors-deployment/install-and-start/)

