package it.gov.acn.outbox;

import it.gov.acn.OutboxProcessor;
import it.gov.acn.OutboxProcessorConfiguration;
import it.gov.acn.outbox.configuration.OutboxStarterProperties;
import org.springframework.scheduling.TaskScheduler;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class OutboxStarterScheduler {
    private OutboxStarterProperties outboxStarterProperties;
    private TaskScheduler taskScheduler;

    public OutboxStarterScheduler(OutboxStarterProperties outboxStarterProperties, TaskScheduler taskScheduler) {
        this.outboxStarterProperties = outboxStarterProperties;
        this.taskScheduler = taskScheduler;
        this.schedule();
    }

    public void schedule(){
        OutboxProcessorConfiguration outboxProcessorConfiguration = new OutboxProcessorConfiguration();
        outboxProcessorConfiguration.setTestPhrase("Here for the glory of the Lord");
        OutboxProcessor outboxProcessor = new OutboxProcessor(outboxProcessorConfiguration);

        taskScheduler.scheduleWithFixedDelay(outboxProcessor::process,
                Duration.of(outboxStarterProperties.getFixedDelay(), ChronoUnit.MILLIS));
    }
}
