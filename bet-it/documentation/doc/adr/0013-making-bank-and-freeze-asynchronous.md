# 13. Making Bank and Freeze asynchronous

Date: 2023-06-04

## Status

Accepted

## Context
A bank should have some time to make transactions, meaning that the communication shall not be asynchronous. For example, if unusual transactions happen, human intervention should be necessary to make the transaction. Furthermore a bank should be able to recover if its in-memory state gets lost.

## Decision

We decided to make the bank communicate over kafka instead of http. Because of this we can use event-sourcing to recover the in-memory state by replaying the messages in case of failure. Furthermore the communication became asynchronous by that.

## Consequences

By having the communication asynchronous, the whole system gets more fail-safe, as a short disruption of the bank service won't make the workflows fail, but rather wait until the bank is availible again. The system will also be able to scale better. Eventual consistency is hard to manage as a bank, that needs to go trough audits. 
