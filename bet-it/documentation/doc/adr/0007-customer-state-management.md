# 7. Customer State Management

Date: 2023-04-23

## Status

Accepted

## Context

In our bet-platform user data of customers that want to use the platform need to be saved.


## Decision

As we are in an event-driven architecture we decided to save the customer state also as an event processing system (EPS).
Mainly because we are using event-carried state transfer and thus we have already all the data in the events.


## Consequences

### Positive
- It will be more complex to implement and handle user data changes
- It will scale very well

### Negative
- Increased complexity because of the characteristics of an event processing system 
