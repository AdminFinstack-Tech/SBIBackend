package com.csme.csmeapi.fin.services;

import java.io.IOException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.csme.csmeapi.fin.config.FinConfig;
import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.util.CSMEMobLogUtil;

/**
 * Handles scheduled batch job for notifications.
 */
@Component
public class NotificationBatchJob {
	
	FinUtil finUtil = new FinUtil();

	/** Logger for logging batch job activities. */
    private final CSMEMobLogUtil logger = new CSMEMobLogUtil();

    /** Flag to track the running status of the batch job. */
    private volatile boolean isBatchJobRunning = true;

    /** The cron expression for scheduling the batch job. */
    private String cronExpression = null;

    /**
     * Constructs a new NotificationBatchJob instance.
     * @throws IOException if an I/O error occurs
     */
    public NotificationBatchJob() throws IOException {
        cronExpression = FinConfig.getCustomProperty("batch.job.cron.expression");
    }

    /**
     * Runs the batch job based on a scheduled cron expression.
     */
    @Scheduled(cron = "0 */2 * * * *") // Fixed cron expression initially
    public void runBatchJob() {
        if (isBatchJobRunning) {
            try {
                NotificationBatchServices service = new NotificationBatchServices();
                logger.info("Calling processing notification jobs");
                service.processingNotification();
                logger.info("End the process");
            } catch (Exception e) {
                logger.error("Error running batch job: " + e.getMessage());
            }
        } else {
            logger.info("Batch job is not running.");
        }
    }

    /**
     * Starts the batch job with a custom cron expression.
     * @param cronExpression the custom cron expression
     */
    public void startBatchJob(String cronExpression) {
        logger.info("Starting batch job with custom timing: " + cronExpression);
        this.cronExpression = cronExpression;
        isBatchJobRunning = true;
    }

    /**
     * Stops the batch job.
     */
    public void stopBatchJob() {
        logger.info("Stopping batch job...");
        isBatchJobRunning = false;
    }

    /**
     * Updates the batch job timing to a new cron expression.
     * @param cronExpression the new cron expression
     */
    public void updateBatchJobTiming(String cronExpression) {
        logger.info("Updating batch job timing to: " + cronExpression);
        this.cronExpression = cronExpression;
    }

    /**
     * Gets the current cron expression used by the batch job.
     * @return the cron expression
     */
    public String getCronExpression() {
        return cronExpression;
    }
}
