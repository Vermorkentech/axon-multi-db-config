package tech.vermorken.multidbconfig.query.analytics

import org.springframework.data.jpa.repository.JpaRepository

interface AggregateCountRepository : JpaRepository<AggregateCount, Long> {
}