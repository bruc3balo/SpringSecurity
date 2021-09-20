/*
package com.security.spring.config.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "securityDbManagerFactory", transactionManagerRef = "securityDbTransactionManager", basePackages = {"com.security.spring" })

public class PostgresDbConfig {
    @Bean(name = "securityDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource securityDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        return dataSource;
    }

    @Bean(name = "securityDbManagerFactory")
    public LocalContainerEntityManagerFactoryBean securityDbManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("securityDataSource") DataSource securityDataSource) {
        return builder.dataSource(securityDataSource).packages("com.security.spring").persistenceUnit("securityDatabase").build();
    }

    @Bean(name = "springDbTransactionManager")
    public PlatformTransactionManager securityDbTransactionManager(@Qualifier("securityDbManagerFactory") EntityManagerFactory securityDbManagerFactory) {
        return new JpaTransactionManager(securityDbManagerFactory);
    }
}
*/
