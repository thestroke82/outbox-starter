package it.gov.acn.outbox.condition;

import it.gov.acn.outbox.configuration.OutboxStarterProperties;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OutboxEnabledCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Boolean enabled = context.getEnvironment()
                .getProperty(OutboxStarterProperties.EnvPropertyKeys.ENABLED.getKeyWithPrefix(),Boolean.class);
        return enabled != null && enabled;
    }
}
