# 14. Split up Game Master

Date: 2023-06-04

## Status

Accepted

## Context

Our system should work with various sources, including our own game.

## Decision

Because our system should be able to be fed by various sources, we decided to split up the game-master, to make sure we can do this in a safe manner.


## Consequences

One more service, will cost more money. 
No issues when developing different sources for the platform as they just need to match the kafka schemas.
