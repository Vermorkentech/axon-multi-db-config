package tech.vermorken.multidbconfig.config

import jakarta.persistence.EntityManagerFactory
import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore
import org.axonframework.serialization.Serializer
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import tech.vermorken.multidbconfig.config.EventProcessorConfig.Companion.createEntityManagerFactoryBean
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    basePackages = ["tech.vermorken.multidbconfig.query.analytics"],
    entityManagerFactoryRef = "secondaryEntityManagerFactory",
    transactionManagerRef = "secondaryPlatformTransactionManager"
)
class SecondaryDatabaseConfig {

    /**
     * Secondary DB config - secondary
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    fun secondaryDataSourceProperties() = DataSourceProperties()

    @Bean
    fun secondaryDataSource(@Qualifier("secondaryDataSourceProperties") secondaryDataSourceProperties: DataSourceProperties): DataSource =
        secondaryDataSourceProperties.initializeDataSourceBuilder().build()

    @Bean
    fun secondaryEntityManagerFactory(
        @Qualifier("secondaryDataSource") secondaryDataSource: DataSource,
        env: Environment,
        jpaVendorAdapter: JpaVendorAdapter
    ): LocalContainerEntityManagerFactoryBean =
        createEntityManagerFactoryBean("secondary", secondaryDataSource, jpaVendorAdapter)

    @Bean
    fun secondaryPlatformTransactionManager(
        @Qualifier("secondaryEntityManagerFactory") secondaryEntityManagerFactory: EntityManagerFactory
    ) =
        JpaTransactionManager(secondaryEntityManagerFactory)

    @Bean
    fun secondaryEntityManagerProvider() = SecondaryEntityManagerProvider()

    @Bean
    fun secondaryAxonTransactionManager(
        @Qualifier("secondaryPlatformTransactionManager") secondaryPlatformTransactionManager: PlatformTransactionManager
    ) =
        SpringTransactionManager(secondaryPlatformTransactionManager)

    @Bean
    fun secondaryTokenStore(
        @Qualifier("secondaryEntityManagerProvider") secondaryEntityManagerProvider: EntityManagerProvider,
        serializer: Serializer
    ): TokenStore {
        return JpaTokenStore.builder()
            .entityManagerProvider(secondaryEntityManagerProvider)
            .serializer(serializer)
            .build()
    }
}