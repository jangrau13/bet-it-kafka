# 8. Payment State Management

Date: 2023-04-23

## Status

Accepted

## Context

The bank needs to do payments between different customers. The balances of each customer needs to be saved.
The balance of a customer is very critical data. Furthermore, balance data needs to be very durable.

## Decision

We decided to save the balances in a database as the data is highly important and needs to be atomic.

## Consequences

### Positive
- Atomic data and more consistency
- Easier to implement and very durable data

###
- Less scalable as an event-driven approach
