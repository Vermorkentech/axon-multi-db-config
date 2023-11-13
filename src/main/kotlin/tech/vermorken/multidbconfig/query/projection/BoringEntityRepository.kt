package tech.vermorken.multidbconfig.query.projection

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BoringEntityRepository : JpaRepository<BoringEntity, String> {
}