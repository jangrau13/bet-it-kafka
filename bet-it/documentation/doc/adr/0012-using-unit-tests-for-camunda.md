# 12. Using Unit Tests for Camunda

Date: 2023-06-04

## Status

Accepted

## Context

Developing camunda workflows can be difficult, as the logic gets very big very quickly. Furthermore it's hard to debug in case of errors.

## Decision

We decided to develop the camunda workflows test-driven, to speed up development speed and rule out potential errors.

## Consequences

Over time the development speed will be faster, as there will be less errors to debug.
There is quit an overhead at the beginning. Also the Unit-Tests Frameworks are not perfect as of today, which some bugs in them.
