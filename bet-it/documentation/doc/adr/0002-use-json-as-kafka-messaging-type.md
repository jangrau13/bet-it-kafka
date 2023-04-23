# 2. Use JSON as Kafka Messaging Type

Date: 2023-04-23

## Status

Accepted

## Context

Our system uses Kafka as a messaging platform for exchanging messages between different components. We need to decide on the messaging type to be used in Kafka, and we are considering JSON and AVRO as potential options. Additionally, our workflow engine, Camunda, uses JSON as its native message type.

## Decision

We have decided to use JSON as the messaging type in Kafka for the following reasons:

- Compatibility with Camunda: Camunda uses JSON as its messaging format, which simplifies message handling and integration between Camunda and our microservices.
- Use of JSON: JSON provides support for human-readable text and allows for flexibility in message structure.

## Consequences

### Positive
- Interoperability: Using JSON as the messaging type in Kafka aligns with Camunda's message format, simplifying message handling and integration between Camunda and our microservices.
- Familiarity and Flexibility

### Negative 
- Performance: JSON is less performant than AVRO