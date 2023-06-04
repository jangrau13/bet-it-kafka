# 4. Usage of Camunda

Date: 2023-04-23

## Status

Accepted

## Context

Our event-driven bet-platform contains some workflows that are hard to track in an event-driven architecture.

## Decision

We decided on using camunda as a workflow engine to make the more complex workflows more controllable and observable.
Furthermore, we decided to use camunda as it was requested by the lecture.

## Consequences

### Positive
- camunda bpm is very scalable which is important for our platform
- the workflow engine creates increased visibility of our processes
### Negative
- it takes a lot of time to implement a workflow engine
- by using a workflow engine we are getting pushed towards an orchestration architecture inside workflows, as we are using an orchestrator