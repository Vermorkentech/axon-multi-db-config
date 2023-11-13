package tech.vermorken.multidbconfig.api

data class CreateBoringAggregate(val aggregateId: String)
data class BoringAggregateCreated(val aggregateId: String)