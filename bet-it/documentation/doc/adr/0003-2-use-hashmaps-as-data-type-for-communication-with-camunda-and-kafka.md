# 3. Use Hashmaps as data type for Communication with Camunda and Kafka

Date: 2023-04-23

## Status

Accepted

## Context

We needed to decide on dataypes for the contracts between microservices that are being connected by kafka with the json format.
The microservices are written in java, we are however using a workflow engine that handles variables as json, which is in a way a hashmap.


## Decision

We have decided to use the hashmap datatype to communicate between the services and the workflow engine.

## Consequences

### Positive
- general development with easy datatypes is in the beginning very fast
- We don't need to refactor contracts, if the workflow engine should all of a sudden have access to a certain service.
- very easy to serialize and flexible

### Negative
- weak typing can lead to error
- only store data

