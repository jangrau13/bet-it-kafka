# 9. Contract Workflow

Date: 2023-04-23

## Status

Accepted

## Context

The creation of a contract by a customer involves multiple microservices which makes the process hard to debug as our system
is operating in an event-driven environment. We need to decide on the architecture of the workflow.

## Decision

We have decided to implement the contract workflow as a fairy tale saga as we want to have synchronous checks before validation a contract.
We decided make create a camunda model for this workflow to increase visibility.
We have chosen the fairy tale over the parallel saga as we expect less load on this workflow than on the actual betting on contracts.

## Consequences

### Positive
- As it follows a linear sequence it is easier to follow and understand
- The contract data is more consistent (potential undo events are not needed)
- Orchestration is more visible and easier to debug

### Negative
- Because of the nature of synchronous communication the workflow will be less scalable
- It will be less fault-tolerant as for example the risk-management service will block the creation of contracts it its offline

