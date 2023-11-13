package tech.vermorken.multidbconfig

import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.Configurer
import org.axonframework.config.ConfigurerModule
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import java.util.*
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
                .registerDefaultTransactionManager{ axonTransactionManager }
                //Alternatives
                .registerTokenStore("tech.vermorken.multidbconfig.query.analytics") { secondaryTokenStore }
                .registerTransactionManager("tech.vermorken.multidbconfig.query.analytics") { secondaryAxonTransactionManager }
        }
    }

    /**
     * Helper functions
     */
    companion object {
        fun createEntityManagerFactoryBean(
            persistenceUnit: String,
            dataSource: DataSource,
            env: Environment
        ): LocalContainerEntityManagerFactoryBean {
            val em = LocalContainerEntityManagerFactoryBean()
            em.persistenceUnitName = persistenceUnit
            em.dataSource = dataSource
            em.jpaVendorAdapter = HibernateJpaVendorAdapter().also { it.setGenerateDdl(true) }
            em.setJpaProperties(Properties().also {
                it.setProperty("jpa.database-platform", env.getProperty(("spring.jpa.database-platform")))
                it.setProperty("hibernate.hbm2ddl.auto", env.getProperty(("spring.jpa.hibernate.ddl-auto")))
            })
            em.afterPropertiesSet()
            return em
        }
    }
}