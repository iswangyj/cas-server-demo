package com.oc.auth.config;

import com.oc.auth.handler.QueryDatabaseAuthenticationHandler;
import com.oc.entity.DatabaseProperties;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * @author wyj
 * @date 2018/11/22
 */
@PropertySource("classpath:db.properties")
public class CustomAuthenticationConfiguration implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Value("${db.driver}")
    private String dbDriver;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;
    
    @Bean
    public AuthenticationHandler queryDatabaseAuthenticationHandler() {
        DatabaseProperties databaseProperties = new DatabaseProperties();
        databaseProperties.setDriver(dbDriver);
        databaseProperties.setUrl(dbUrl);
        databaseProperties.setUsername(dbUsername);
        databaseProperties.setPassword(dbPassword);

        return new QueryDatabaseAuthenticationHandler(QueryDatabaseAuthenticationHandler.class.getName(),
                servicesManager, new DefaultPrincipalFactory(), 1, databaseProperties);
    }

    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(queryDatabaseAuthenticationHandler());
    }
}
