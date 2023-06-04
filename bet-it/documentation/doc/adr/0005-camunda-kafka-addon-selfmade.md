# 5. Camunda Kafka Addon Selfmade

Date: 2023-04-23

## Status

Accepted

## Context

Our event-driven bet platform is using kafka to communicate between the different services. Together with kafka
we decided earlier that we want to use camunda as our workflow engine. Camunda however has not yet a working kafka connector.
We need to decide between using an addon, or letting every microsservice that is involved with the worklfow having connect to zeebe.

## Decision
We decided to create an addon that is connected to the workflow engine and that will pipe all kafka events from and to the engine.

## Consequences

### Positive
- Interoperability: We can easily connect other microservices that do not know anything about the workflow engine to the process via kafka.
- Lower Coupling: Only the addon is directly connected to the workflow engine which means lower coupling between the service and the orchestrator.
### Negative
- Traffic: The addon will generate more traffic as it has to forward events to camunda
