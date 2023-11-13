package tech.vermorken.multidbconfig.query.analytics

import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import tech.vermorken.multidbconfig.api.BoringAggregateCreated

@Component
class AggregateCountProjection(
    val repository: AggregateCountRepository
) {

    @EventHandler
    fun handle(event: BoringAggregateCreated) {
        val aggregateCount = repository.findAll()
            .firstOrNull()?.also {
                it.count += 1
            } ?: AggregateCount(1)
        repository.save(aggregateCount)
    }

}