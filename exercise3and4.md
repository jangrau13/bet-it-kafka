# Event Driven Processing
## Exercise 3
### Task
For your project portfolio we suggest that you implement your own process solution using the
Spring Boot Framework and Camunda. Here you can think about your own BPMN-based proces￾ses with simple HTML user interfaces to start processes, and extended sets of process elements,
e.g., different types of gateways, external tasks, user tasks, as well as timers, message events and exceptions as further introduced in the Camunda Lafayette3 tutorials. You are free to choose if you want to use Kafka as a message broker (as demonstrated in Lab6) or not.

### Solution
see process AddCustomer Process.

## Exercise 4
### Task
For your project portfolio you could think about either extending the order flow of the given
Flowing Retail project with additional commands, events and other process elements. Alternati￾vely, you could think about developing your own prototype of a small event-driven system where
you try to find a balance between events and commands.

### Solution
See process ScoreCustomer@bid. Still an open question though: how can we access the payload of the Kafka-message?


Whole project:
https://github.com/jangrau13/bet-it-kafka/releases/tag/Ex3and4