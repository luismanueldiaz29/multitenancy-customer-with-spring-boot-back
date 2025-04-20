package com.example.art.multitenancy.config;

import com.example.art.multitenancy.multitenant.MultiTenantConnectionProviderImpl;
import com.example.art.multitenancy.multitenant.TenantDataSourceConfig;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

import static org.hibernate.cfg.MultiTenancySettings.MULTI_TENANT_CONNECTION_PROVIDER;
import static org.hibernate.cfg.MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER;

@Configuration
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@EnableJpaRepositories(basePackages = JpaConfig.COM_EXAMPLE_ART_MULTITENANCY_REPOSITORIES)
public class JpaConfig {
    public static final String COM_EXAMPLE_ART_MULTITENANCY_ENTITIES = "com.example.art.multitenancy.entities"; //packages donde se crean las entidades
    public static final String COM_EXAMPLE_ART_MULTITENANCY_REPOSITORIES = "com.example.art.multitenancy.repositories"; //packages donde se crean los repositorios de jpa

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            MultiTenantConnectionProvider<String> multiTenantConnectionProvider,
            CurrentTenantIdentifierResolver<String> tenantIdentifierResolver,
            JpaProperties jpaProperties,
            ObjectProvider<PersistenceUnitManager> persistenceUnitManager
    ) {
        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        properties.put(MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        properties.put(MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(properties);
        em.setPersistenceUnitManager(persistenceUnitManager.getIfAvailable());
        em.setPackagesToScan(COM_EXAMPLE_ART_MULTITENANCY_ENTITIES);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public MultiTenantConnectionProvider<String> multiTenantConnectionProvider(TenantDataSourceConfig config) {
        return new MultiTenantConnectionProviderImpl(config);
    }

    @Bean
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }
}


