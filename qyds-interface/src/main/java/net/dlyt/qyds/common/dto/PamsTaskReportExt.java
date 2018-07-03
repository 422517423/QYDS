package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class PamsTaskReportExt extends PamsTaskReport {

    private String taskStatus;

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}