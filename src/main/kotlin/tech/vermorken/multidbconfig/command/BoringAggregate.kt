package tech.vermorken.multidbconfig.command

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate
import tech.vermorken.multidbconfig.api.BoringAggregateCreated
import tech.vermorken.multidbconfig.api.CreateBoringAggregate

@Aggregate
class BoringAggregate() {

    @AggregateIdentifier
    lateinit var aggregateIdentifier: String

    @CommandHandler
    constructor(cmd: CreateBoringAggregate) : this() {
        apply(BoringAggregateCreated(cmd.aggregateId))
    }

    @EventSourcingHandler
    fun on(event: BoringAggregateCreated) {
        aggregateIdentifier = event.aggregateId
    }

}