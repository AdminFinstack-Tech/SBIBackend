package com.csme.csmeapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.csme.csmeapi.fin.config.FinUtil;
import com.csme.csmeapi.fin.services.NotificationBatchJob;

/**
 * Controller class for managing notification batch jobs.
 * Handles starting, stopping, updating, and retrieving default timing for the batch job.
 */
@RestController
public class NotificationJobController {
	
	FinUtil finUtil = new FinUtil();

    @Autowired
    private NotificationBatchJob batchJob;

    /**
     * Endpoint for starting the batch job with a given cron expression.
     * @param cronExpression The cron expression for scheduling the batch job.
     */
    @PostMapping("/startBatchJob")
    public void startBatchJob(@RequestBody String cronExpression) {
        batchJob.startBatchJob(cronExpression);
    }

    /**
     * Endpoint for stopping the batch job.
     */
    @PostMapping("/stopBatchJob")
    public void stopBatchJob() {
        batchJob.stopBatchJob();
    }

    /**
     * Endpoint for updating the cron expression of the batch job.
     * @param cronExpression The new cron expression for scheduling the batch job.
     */
    @PostMapping("/updateBatchJobTiming")
    public void updateBatchJobTiming(@RequestBody String cronExpression) {
        batchJob.updateBatchJobTiming(cronExpression);
    }

    /**
     * Endpoint for retrieving the default cron expression for the batch job.
     * @return The default cron expression.
     */
    @PostMapping("/getDefaultBatchJobTiming")
    public String getDefaultBatchJobTiming() {
        return batchJob.getCronExpression();
    }
}