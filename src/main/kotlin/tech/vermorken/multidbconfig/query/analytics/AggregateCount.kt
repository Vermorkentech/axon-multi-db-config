package tech.vermorken.multidbconfig.query.analytics

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class AggregateCount(
    @Column(nullable = false)
    var count: Int = 1,

    @Id
    @GeneratedValue
    val id: Long? = null
)