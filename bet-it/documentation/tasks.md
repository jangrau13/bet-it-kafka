# Task for presentation and hand-in

## week 1
1. explain why we use JSON as mapping --> easier to read and debug. Camunda uses JSON as well as well as result (performance lost)
2. explain how we use kafka and why
   - activity tracking (Events)
   - messaging (publish-subscribe)
   - stream processing (as a database)
   - metrics and logging (not directly used)
   - commit log (not directly used)
   - event sourcing (not yet used)
3. explain trade-off on how we use kafka (fire-and-forget, asynchronous, synchronous)
4. difference of consumer/customer groups --> what do we do here?

## week 2
1. explain why we use events (decoupling) for new game, new bid and new contract
2. explain why we use event notification for new game, new bid and new contract (dependency inversion & make change a first-class citizen)
3. no commands (we use events instead) --> we just know that a new game, bid, contract happen, but the system should know how to react to it & events are facts of the past & easily extendible
4. Event-carried state transfer --> we use it with Camunda for adding User credibility
5. Event-Sourcing --> we use it with the payment system. Payment is a ledger by itself
6. CQRS --> not used :: explain why
7. orchestration vs choreography :: choreography for bets, banking needs orchestration due to "transactional nature" and difficulty if we need to handle back order (compensation) & the way to see how the process looks like
8. explain why we used one of those patterns: Front Controller, Stateless Choreography, Stamp Coupling --> what do we do for choreography for game, bet, contract?
9. choreography for game, bid and contract due to its error less nature (maybe handle time problem a little bit)
10. maybe change our events to [CNCF Cloud Events](https://github.com/cloudevents/spec) might have to adjust it in Kafka Camunda API as well, but would solve our time problem (see point above)

## week 3
1. explain the benefits of orchestration for adding_customer
2. add Subprocess to cancel something in the middle (payment probably)

## week 4
1. ADR to say that the banking should contain the logic for checking the transactions financially
2. discussion on how the microservices work together (separate processes)
3. discussion on the direction of dependency (event vs command) --> Is it OK with the component emitting an event if that event is ignored
4. discuss on Orchestration does not introduce temporal coupling, synchronous communication does
5. introduce some commands (check customer) --> add reasons for using the banking as a commands

## week 5
1. assign each process a saga with its benefits and downfalls (epic, phone tag, etc.)
2. explain the waiting process/hystrix we have implemented on the checking and the payment (to have the fallacy of distributed computing on)
3. ADRs on how we added error handling on those subprocesses (maybe add one with human intervention)
4. try to add the aggregator pattern (maybe add a recommendation for adding a customer)
5. have a 202_ACCEPTED for the contract implemented
6. implement at least a stateful resilience pattern --> name it explicitely

## ToDos
1. explicitely tell about state reslience patterns
2. say where we use event notification, which one are event carried state transfer
3. Event Subprocess
4. why we chose Camunda v8
5. add ADR to flexibility and complexity --> JSON
