# 15. Split up Fraud detection

Date: 2023-06-04

## Status

Accepted

## Context

Previously, the fraud detection had to respond immediately in the bet workflow. In our opinion however the fraud detection is an area that observes bets over a long time from multiple bets. Also frauds should be detectable well after a bet has ended, and thus actions should be taken at that time.

## Decision

We decided to split the fraud detection into its own service and moved it out of the bet workflow. The goal is that the fraud detection should be able to listen to various topics and make decisions on fraud claims based on Streams etc. Compensating actions will be events thrown, consumed by the bank. For this schema we will use avro.

## Consequences

The bet workflow will get leaner which improves surveilability. Furthermore the communication will get asynchronous, allowing the system to scale better.
