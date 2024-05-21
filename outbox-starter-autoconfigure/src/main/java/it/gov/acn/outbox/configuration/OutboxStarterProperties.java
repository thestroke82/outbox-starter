package it.gov.acn.outbox.configuration;

import it.gov.acn.outbox.etc.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = Constants.APP_PROPERTIES_PREFIX)
public class OutboxStarterProperties{
    public enum EnvPropertyKeys {
        ENABLED("enabled"),
        FIXED_DELAY("fixed-delay");

        private final String key;

        EnvPropertyKeys(String key) {
            this.key = key;
        }
        public String getKey() {
            return this.key;
        }
        public String getKeyWithPrefix() {
            return Constants.APP_PROPERTIES_PREFIX + "." + this.key;
        }
    }
    /**
     * Enable or disable the outbox scheduler.
     */
    private boolean enabled = DefaultOutboxStarterConfiguration.ENABLED;

    /**
     * Fixed delay in milliseconds between the end of the last invocation and the start of the next.
     */
    private long fixedDelay = DefaultOutboxStarterConfiguration.FIXED_DELAY;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getFixedDelay() {
        return fixedDelay;
    }

    public void setFixedDelay(long fixedDelay) {
        this.fixedDelay = fixedDelay;
    }

    @Override
    public String toString() {
        return "OutboxStarterProperties{" +
                "enabled=" + enabled +
                ", fixedDelay=" + fixedDelay +
                '}';
    }
}
