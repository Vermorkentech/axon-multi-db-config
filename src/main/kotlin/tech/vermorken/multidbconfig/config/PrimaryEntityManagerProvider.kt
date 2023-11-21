package tech.vermorken.multidbconfig.config

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.axonframework.common.jpa.EntityManagerProvider

class PrimaryEntityManagerProvider : EntityManagerProvider {

    private lateinit var entityManager: EntityManager

    override fun getEntityManager(): EntityManager {
        return entityManager
    }

    @PersistenceContext(unitName = "primary")
    fun setEntityManager(entityManager: EntityManager) {
        this.entityManager = entityManager
    }
}
