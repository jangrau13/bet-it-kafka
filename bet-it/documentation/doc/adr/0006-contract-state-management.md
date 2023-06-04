# 6. Contract State Management

Date: 2023-04-23

## Status

Accepted, In-Progress

## Context

In our event-driven bet-platform registered customers can create contracts on games, that can then be bought by customers.
Because of this contracts need to be stored in our event processing system.


## Decision

As we are using event-carried state transfer in our events we decided to use a ksql table to access the contracts as
all the contract data will be reconstructed through the events.
This implemented is not completely finished yet.


## Consequences

### Positive
- Handling the state eventual and asynchronous will make the application more scalable and will higher performance.

### Negative
- Increased complexity because of the characteristics of an event processing system 
