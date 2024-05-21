package it.gov.acn.outbox.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

public class ContextManager {
    private final Logger logger = LoggerFactory.getLogger(ContextManager.class);
    private List<ContextRequirement> requirements;
    private List<ContextRequirement> unsatisfiedRequirements = new ArrayList<>();

    public ContextManager(Environment environment, ApplicationContext applicationContext) {
        this.requirements = new ArrayList<>();
        this.requirements.add(new ConfigPresentRequirement(environment));
        this.requirements.add(new ConfigValidRequirement(environment));
        this.requirements.add(new PostgresDatasourceRequirement(applicationContext));
        this.evaluateRequirements();
    }

    public boolean isContextValid() {
        return this.unsatisfiedRequirements.stream()
                .filter(ContextRequirement::isError)
                .count() == 0;
    }

    public void logFaultyConditions() {
        this.unsatisfiedRequirements.stream()
                .filter(ContextRequirement::isError)
                .forEach(r -> logger.debug(r.getDescription()));
    }

    private void evaluateRequirements() {
        for (ContextRequirement condition : requirements) {
            if (!condition.isSatisfied()) {
                unsatisfiedRequirements.add(condition);
            }
        }
    }


}