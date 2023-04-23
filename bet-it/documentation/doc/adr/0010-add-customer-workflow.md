# 10. Add Customer Workflow

Date: 2023-04-23

## Status

Accepted

## Context

In our event-driven bet-platform we needed to decide on the architecture of our create-customer workflow.

## Decision

We have decided to again use the fairy tale saga pattern for the following reasons:
- orchestration: we want to model the workflow in camunda for increased visibility
- eventual: we are still in an event-driven architecture with eventual consistency
- synchronous: we have synchronous checks (password check for example)

## Consequences

### Positive

### Positive
- Orchestration is more visible and easier to debug
- As it follows a linear sequence it is easier to follow and understand
- The user data is more consistent (potential undo events are not needed)

### Negative
- Because of the nature of synchronous communication the workflow will be less scalable

