package it.gov.acn.outbox.configuration;

import it.gov.acn.outbox.context.ContextManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@EnableConfigurationProperties(OutboxStarterProperties.class)
public class ContextInitAutoconfiguration {


    @Bean
    public ContextManager outboxContextRequirementValidator(
            Environment environment,
            ApplicationContext applicationContext
    ){
        return new ContextManager(environment, applicationContext);
    }
}
