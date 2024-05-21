package it.gov.acn.outbox.context;

import it.gov.acn.outbox.etc.Constants;
import it.gov.acn.outbox.etc.Utils;
import org.springframework.core.env.Environment;

/**
 * This class is a ContextRequirement that checks if the configuration is present.
 * It is used by the ContextManager as one of the requirements to be satisfied.
 */
public class ConfigPresentRequirement implements ContextRequirement {

    private final Environment environment;

    public ConfigPresentRequirement(Environment environment) {
        this.environment = environment;
    }

    @Override
    public boolean isSatisfied() {
        return Utils.isPrefixPresentInProperties(Constants.APP_PROPERTIES_PREFIX, environment);
    }

    @Override
    public String getDescription() {
        return "Requires "+Constants.APP_PROPERTIES_PREFIX+" configuration.";
    }

    @Override
    public boolean isError() {
        return false;
    }
}