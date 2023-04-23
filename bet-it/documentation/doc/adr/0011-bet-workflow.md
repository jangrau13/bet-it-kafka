# 11. Bet Workflow

Date: 2023-04-23

## Status

Accepted

## Context

We need to decide on the architectural properties of the bet-workflow that needs to be highly scalable.
For our workflows we are using the camunda workflow engine which results in a rather orchestrated environment.

## Decision

We decided to use the parallel saga as we need a scalability is the most crucial in this workflow.
Also, the nature of waiting for a game to end is very much asynchronous anyway.


## Consequences

### Positive
- Scalability
- More fault-tolerant if a service is unavailable for a short period of time

### Negative
- Consistency: the state is only saved within the camunda workflow engine process
- Hard to debug: the processes are harder to debug as in synchronous communication 