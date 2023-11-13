package tech.vermorken.multidbconfig.query.projection

import jakarta.transaction.Transactional
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import tech.vermorken.multidbconfig.api.BoringAggregateCreated

@Component
class BoringProjection(
    val repository: BoringEntityRepository
) {

    @EventHandler
    @Transactional
    fun handle(event: BoringAggregateCreated) {
        repository.save(BoringEntity(event.aggregateId))
    }

}