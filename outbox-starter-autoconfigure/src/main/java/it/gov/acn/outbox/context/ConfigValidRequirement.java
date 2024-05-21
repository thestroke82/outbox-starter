package it.gov.acn.outbox.context;

import it.gov.acn.outbox.configuration.OutboxStarterProperties;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class is a ContextRequirement that checks if the configuration is valid.
 * It is used by the ContextManager as one of the requirements to be satisfied.
 */
public class ConfigValidRequirement implements ContextRequirement {

    private List<String> validationErrorMessages = new ArrayList<>();
    private Environment environment;

    public ConfigValidRequirement(Environment environment) {
        this.environment = environment;
    }

    @Override
    public boolean isSatisfied() {
        validateFixedDelay();
        return validationErrorMessages.isEmpty();
    }

    @Override
    public String getDescription() {
        return "Requires a valid configuration: "+
                this.validationErrorMessages.stream().collect(Collectors.joining(", "));
    }

    @Override
    public boolean isError() {
        return true;
    }

    private void validateFixedDelay() {
        Optional<Long> fixedDelay = getLongProperty(OutboxStarterProperties.EnvPropertyKeys.FIXED_DELAY.getKeyWithPrefix());
        if(fixedDelay.isPresent() && fixedDelay.get() < 100) {
            validationErrorMessages.add("Fixed delay must be at least 100 milliseconds");
        }
    }

    private Optional<Boolean> getBooleanProperty(String key) {
        String property = this.environment.getProperty(key);
        return Optional.ofNullable(property).map(Boolean::parseBoolean);
    }

    private Optional<Long> getLongProperty(String key) {
        String property = this.environment.getProperty(key);
        try {
            return Optional.ofNullable(property).map(Long::parseLong);
        } catch (NumberFormatException e) {
            validationErrorMessages.add("Invalid value for property " + key);
            return Optional.empty();
        }
    }
}
