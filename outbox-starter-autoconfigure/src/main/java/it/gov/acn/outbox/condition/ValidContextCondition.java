package it.gov.acn.outbox.condition;

import it.gov.acn.outbox.context.ContextManager;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ValidContextCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ContextManager contextValidator =
                context.getBeanFactory().getBean(ContextManager.class);
        return contextValidator.isContextValid();
    }
}
