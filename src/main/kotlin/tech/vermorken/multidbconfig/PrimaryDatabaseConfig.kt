package tech.vermorken.multidbconfig

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.axonframework.common.jpa.SimpleEntityManagerProvider
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.Configurer
import org.axonframework.config.ConfigurerModule
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore
import org.axonframework.serialization.Serializer
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import tech.vermorken.multidbconfig.EventProcessorConfig.Companion.createEntityManagerFactoryBean
import java.util.*
import javax.sql.DataSource


@Configuration
@EnableJpaRepositories(basePackages =  ["tech.vermorken.multidbconfig.query.projection"],
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "platformTransactionManager"
)
class PrimaryDatabaseConfig {

    /**
     * Primary DB config - projection
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSourceProperties() = DataSourceProperties()

    @Bean
    @Primary
    fun dataSource(dataSourceProperties: DataSourceProperties): DataSource =
        dataSourceProperties.initializeDataSourceBuilder().build()

    @Bean
    @Primary
    fun entityManagerFactory(dataSource: DataSource, env: Environment): LocalContainerEntityManagerFactoryBean =
        createEntityManagerFactoryBean("projection", dataSource, env)

    @Bean
    @Primary
    fun platformTransactionManager(entityManagerFactory: EntityManagerFactory) =
        JpaTransactionManager(entityManagerFactory)

    @Bean
    @Primary
    fun entityManagerProvider(entityManager: EntityManager) = SimpleEntityManagerProvider(entityManager)

    @Bean
    @Primary
    fun axonTransactionManager(platformTransactionManager: PlatformTransactionManager) =
        SpringTransactionManager(platformTransactionManager)

    @Bean
    @Primary
    fun tokenStore(entityManagerProvider: SimpleEntityManagerProvider, serializer: Serializer): TokenStore {
        return JpaTokenStore.builder()
            .entityManagerProvider(entityManagerProvider)
            .serializer(serializer)
            .build()
    }





}