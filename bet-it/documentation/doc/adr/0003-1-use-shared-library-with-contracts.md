# 3. Use Hashmaps as data type for Communication with Camunda and Kafka

Date: 2023-04-23

## Status

Superseded by 003-2

## Context

We needed to decide on dataypes for the contracts between microservices that are being connected by kafka with the json format.
The microservices are all written in java.


## Decision

We have decided that we create a shared-library that holds the contracts for the kafka communication between the microservices.
The contracts are java objects.

## Consequences

### Positive
- less code duplication
- can version the contracts
- less errors

### Negative
- Interoperability as we are using java objects, only services that run on java really profit 


