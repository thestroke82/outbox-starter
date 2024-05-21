package it.gov.acn.outbox.etc;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static boolean isPrefixPresentInProperties(String prefix, Environment environment){
        if (environment instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
            for (PropertySource<?> propertySource : configurableEnvironment.getPropertySources()) {
                if (propertySource.getSource() instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) propertySource.getSource();
                    for (String key : map.keySet()) {
                        if (key.startsWith(prefix)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static <T> T getBeanIfExists(Class<T> type, ApplicationContext applicationContext){
        ConfigurableListableBeanFactory beanFactory =
            (ConfigurableListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        // Get all beans of the specified type
        Map<String, T> beansOfType = applicationContext.getBeansOfType(type);

        // Iterate over the beans and check if any is instantiated
        for (Map.Entry<String, T> entry : beansOfType.entrySet()) {
            String beanName = entry.getKey();
            if (beanFactory.containsSingleton(beanName)) {
                return entry.getValue();
            }
        }

        return null; // No instantiated bean of the specified type found
    }
}
