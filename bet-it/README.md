# Bet Platform

## Setup
install the following software:
- [Open Source Desktop Modeler](https://camunda.com/de/download/modeler/)

select Camunda Platform Version 8.2 (alpha) for all the documents created and creating.

## Build
mvn clean install -DskipTests
docker-compose up --build -d

## Monitor Kafka
open [kafdrop](http://localhost:9000) and have a look at the topics

## Create Game

POST localhost:8083/game/createRandom body: "GameName"

## Create Contract

POST localhost:8082/platform/bet/write body: { "winHome": boolean, "winAway": boolean, "ratio": float, "writerName": String, "gameId": "String"

## Create Bid

POST localhost:8082/platform/bet/bid body: { "buyerName": String, "amount": String, "contractId": String}

This will set the bid, if however the bank potentially rejects the bid, it will be deleted. If the bank does not reject it, it will be set as active.

## add a new User
1. Open Desktop Modeler
2. Open file bet-it/bank/src/main/resources/add_customer.bpmn
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


## Payment Process
- [ ] make payment after Game ended
- [ ] add fraud mechanism, that we have to redo (compensate) the whole process including the bet itself

## Check Customer Process
- [ ] when going for a Contract, the system should check the user, whether it thinks that the contract user is trustworthy. This is done by the [risk management controller](bank/src/main/java/ch/unisg/controller/RiskManagementController.java)
- [ ] the above process should be modeled as a Camunda process
- [ ] when going for a bid, the system should check both the contract user and the bidder, whether in case of success both parties would be able to pay. This could be done a little bit risky, so for example that if users have more than one bet open, that they get accepted with the risk of them not being able to pay, which would trigger a deletion of the bet.
- [ ] when creating a customer add via event-carried state transfer a flag on how trust-worthy he/she is




## Kafka Camunda API
We were expecting Kafka and Camunda to operate more easily, so that you can tell camunda as a task (with an own implementation for every task) to post a topic on Kafka and for a Microservice to start a Camunda Process by posting something on Kafka.
In order to achieve this we implemented this API by ourselves in the bank microservice, where we are using the Camunda Tool. 
You can find the implemenation [here](bank/src/main/java/ch/unisg/kafka/service/BankConsumerService.java) at line 116 (might change, but it is a Map implementation listening on the camunda topic).
This API is transforming Kafka request from/to Camunda to/from Kafka depending on the map keys "messageName" and "topic". 
If the request to the camunda topic contains a "messageName" the API will create the camunda process with that name (if it exists). 
If the request does not contain a "messageName", but a topic it will post the variables of Camunda to the topic specified in the payload.
In order to use this feature from Camunda, a [ZeebeWorker](bank/src/main/java/ch/unisg/zeebe/servicetasks/SendToKafka.java) was created, which automatically send the Camunda task to the camunda topic.
An example of how to use this feature from the Camunda side, please refer to [add_customer.bpmn](bank/src/main/resources/add_customer.bpmn).
An example of how to use this feature from the Microservice side, please refer to [BankController.java](bank/src/main/java/ch/unisg/controller/BankController.java)

### Add Connector Templates
If you want to use templates for the Camunda Desktop Modeler download the templates and follow the [instructions](https://docs.camunda.io/docs/self-managed/connectors-deployment/install-and-start/)

