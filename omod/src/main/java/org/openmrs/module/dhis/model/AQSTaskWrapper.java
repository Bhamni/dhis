package org.openmrs.module.dhis.model;

import aggregatequeryservice.aqstask.AQSTask;

import java.sql.Timestamp;

public class AQSTaskWrapper {
    private Integer taskId;
    private String aqsConfigPath;
    private String taskStatus;
    private Timestamp dateCreated;
    private String results;
    private String inputParameters;
    private String queryConfig;
    private String uuid;

    public AQSTaskWrapper() {}

    public AQSTaskWrapper(AQSTask aqsTask) {
        taskId = ((Long) aqsTask.taskId).intValue();
        aqsConfigPath = (String) aqsTask.aqsConfigPath;
        taskStatus = (String) aqsTask.taskStatus;
        dateCreated = (Timestamp) aqsTask.dateCreated;
        results = (String) aqsTask.results;
        inputParameters = (String) aqsTask.inputParameters;
        queryConfig = (String) aqsTask.queryConfig;
        uuid = (String) aqsTask.uuid;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getAqsConfigPath() {
        return aqsConfigPath;
    }

    public void setAqsConfigPath(String aqsConfigPath) {
        this.aqsConfigPath = aqsConfigPath;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(String inputParameters) {
        this.inputParameters = inputParameters;
    }

    public String getQueryConfig() {
        return queryConfig;
    }

    public void setQueryConfig(String queryConfig) {
        this.queryConfig = queryConfig;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
