package com.oc.auth.config;

import com.oc.auth.handler.QueryDatabaseAuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

/**
 * @author wyj
 * @date 2018/11/22
 */
public class CustomAuthenticationConfiguration implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;
    
    @Bean
    public AuthenticationHandler queryDatabaseAuthenticationHandler() {
        return new QueryDatabaseAuthenticationHandler(QueryDatabaseAuthenticationHandler.class.getName(),
                servicesManager, new DefaultPrincipalFactory(), 1);
    }

    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(queryDatabaseAuthenticationHandler());
    }
}
