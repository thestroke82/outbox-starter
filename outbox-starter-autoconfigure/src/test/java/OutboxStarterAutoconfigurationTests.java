import it.gov.acn.outbox.OutboxStarterScheduler;
import it.gov.acn.outbox.configuration.ContextInitAutoconfiguration;
import it.gov.acn.outbox.configuration.OutboxStarterAutoconfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static org.assertj.core.api.Assertions.assertThat;

public class OutboxStarterAutoconfigurationTests {

    private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(OutboxStarterAutoconfiguration.class, ContextInitAutoconfiguration.class));


    @Test
    void should_provide_ThreadPoolTaskScheduler_when_no_other_TaskScheduler_present() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(true)
                .withFixedDelay(3000)
                .withPostgres()
                .claim()
                .run(context -> {
                    assertThat(context).hasSingleBean(TaskScheduler.class);
                    assertThat(context).hasBean("threadPoolTaskScheduler");
                    assertThat(context.getBean("threadPoolTaskScheduler")).isInstanceOf(ThreadPoolTaskScheduler.class);
                });
    }

    @Test
    void should_not_provide_ThreadPoolTaskScheduler_when_other_TaskScheduler_present() {
        ContextRunnerDecorator.create(contextRunner)
                .withUserConfiguration(TestConfiguration.class)
                 .withEnabled(true)
                .withPostgres()
                .claim()
                .run(context -> {
                    assertThat(context).hasSingleBean(TaskScheduler.class);
                    assertThat(context).doesNotHaveBean("threadPoolTaskScheduler");
                });
    }

    @Test
    void should_provide_OutboxStarterScheduler() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(true)
                .withPostgres()
                .claim()
                .run(context -> {
                    assertThat(context).hasSingleBean(OutboxStarterScheduler.class);
                    assertThat(context).hasBean("outboxStarterScheduler");
                });
    }

    @Test
    void should_not_provide_OutboxStarterScheduler_when_properties_not_configured() {
        ContextRunnerDecorator.create(contextRunner)
                .claim()
                .run(context -> {
                    assertThat(context).doesNotHaveBean(OutboxStarterScheduler.class);
                    assertThat(context).doesNotHaveBean("outboxStarterScheduler");
                });
    }

    @Test
    void should_not_provide_OutboxStarterScheduler_when_context_fixedDelay_invalid() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(true)
                .withFixedDelay(-1)
                .withPostgres()
                .claim()
                .run(context -> {
                    assertThat(context).doesNotHaveBean(OutboxStarterScheduler.class);
                    assertThat(context).doesNotHaveBean("outboxStarterScheduler");
                });
    }

    @Test
    void should_not_provide_OutboxStarterScheduler_when_postgres_datasource_not_present() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(true)
                .withFixedDelay(3000)
                .claim()
                .run(context -> {
                    assertThat(context).doesNotHaveBean(OutboxStarterScheduler.class);
                    assertThat(context).doesNotHaveBean("outboxStarterScheduler");
                });
    }

    @Test
    void should_not_provide_OutboxStarterScheduler_when_scheduler_not_enabled() {
        ContextRunnerDecorator.create(contextRunner)
                .withEnabled(false)
                .withPostgres()
                .claim()
                .run(context -> {
                    assertThat(context).doesNotHaveBean(OutboxStarterScheduler.class);
                    assertThat(context).doesNotHaveBean("outboxStarterScheduler");
                });
    }
}