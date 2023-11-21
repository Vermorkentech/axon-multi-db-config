package tech.vermorken.multidbconfig.config

import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.Configurer
import org.axonframework.config.ConfigurerModule
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import javax.sql.DataSource

@Configuration
class EventProcessorConfig {

    /**
     * Event processor config; make each event processor use the correct DB
     */
    @Bean
    fun processorConfigurerModule(
        axonTransactionManager: TransactionManager,
        tokenStore: TokenStore,
        @Qualifier("secondaryAxonTransactionManager") secondaryAxonTransactionManager: TransactionManager,
        @Qualifier("secondaryTokenStore") secondaryTokenStore: TokenStore
    ): ConfigurerModule {
        return ConfigurerModule { configurer: Configurer ->
            configurer.eventProcessing()
                //Defaults
                .registerTokenStore { tokenStore }
                .registerDefaultTransactionManager { axonTransactionManager }
                //Alternatives
                .registerTokenStore("tech.vermorken.multidbconfig.query.analytics") { secondaryTokenStore }
                .registerTransactionManager("tech.vermorken.multidbconfig.query.analytics") { secondaryAxonTransactionManager }
        }
    }

    /**
     * Helper function
     */
    companion object {
        fun createEntityManagerFactoryBean(
            persistenceUnit: String,
            dataSource: DataSource,
            jpaVendorAdapter: JpaVendorAdapter
        ): LocalContainerEntityManagerFactoryBean {
            val em = LocalContainerEntityManagerFactoryBean()
            em.persistenceUnitName = persistenceUnit
            em.dataSource = dataSource
            em.jpaVendorAdapter = jpaVendorAdapter
            em.afterPropertiesSet()
            return em
        }
    }
}