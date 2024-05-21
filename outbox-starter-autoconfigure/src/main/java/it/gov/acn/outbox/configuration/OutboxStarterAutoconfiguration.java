package it.gov.acn.outbox.configuration;

import it.gov.acn.outbox.OutboxStarterScheduler;
import it.gov.acn.outbox.condition.ValidContextCondition;
import it.gov.acn.outbox.context.ContextManager;
import it.gov.acn.outbox.condition.OutboxEnabledCondition;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@AutoConfiguration(after = ContextInitAutoconfiguration.class)
public class OutboxStarterAutoconfiguration {
    private Logger logger = LoggerFactory.getLogger(OutboxStarterAutoconfiguration.class);

    private OutboxStarterProperties outboxStarterProperties;
    private ContextManager contextValidator;

    public OutboxStarterAutoconfiguration(
            OutboxStarterProperties outboxStarterProperties,
            ContextManager contextValidator
    ) {
        this.outboxStarterProperties = outboxStarterProperties;
        this.contextValidator = contextValidator;
    }

    @Bean
    @ConditionalOnMissingBean(TaskScheduler.class) // Create a TaskScheduler bean only if there is none in the context
    @Conditional(value = {ValidContextCondition.class, OutboxEnabledCondition.class})
    public TaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix(
                "ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    @Conditional(value = {ValidContextCondition.class, OutboxEnabledCondition.class})
    public OutboxStarterScheduler outboxStarterScheduler(
            OutboxStarterProperties outboxStarterProperties,
            TaskScheduler taskScheduler
    ){
        return new OutboxStarterScheduler(outboxStarterProperties, taskScheduler);
    }

    @PostConstruct
    public void logContextStatus(){
        boolean isContextValid = contextValidator.isContextValid();
        if (!isContextValid) {
            logger.error("There's a problem with the application context. Please set the log level to 'debug' for more details.");
            contextValidator.logFaultyConditions();
        } else {
            logger.debug("Configuration Details: {}", outboxStarterProperties);
        }
        logger.info("Outbox Scheduler Status: {}", shouldEnableOutboxScheduler() ? "Enabled" : "Disabled");
    }

    private boolean shouldEnableOutboxScheduler(){
        return contextValidator.isContextValid() && outboxStarterProperties.isEnabled();
    }

}
