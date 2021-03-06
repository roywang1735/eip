package com.wordgod.eip.Model;

public class ScheduleTask {
	private String scheduleTask_seq;
    private String recordsID;
    private String scheduleName;
    private String scheduleTime;
    private String FlowStatus; 

    
	public ScheduleTask(String scheduleTask_seq,String recordsID,String scheduleName,String scheduleTime,String FlowStatus) {
		this.scheduleTask_seq = scheduleTask_seq;
		this.recordsID = recordsID;
		this.scheduleName = scheduleName;
		this.scheduleTime = scheduleTime;
		this.FlowStatus = FlowStatus;
	}


	public String getScheduleTask_seq() {
		return scheduleTask_seq;
	}


	public void setScheduleTask_seq(String scheduleTask_seq) {
		this.scheduleTask_seq = scheduleTask_seq;
	}


	public String getRecordsID() {
		return recordsID;
	}


	public void setRecordsID(String recordsID) {
		this.recordsID = recordsID;
	}


	public String getScheduleName() {
		return scheduleName;
	}


	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}


	public String getScheduleTime() {
		return scheduleTime;
	}


	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}


	public String getFlowStatus() {
		return FlowStatus;
	}


	public void setFlowStatus(String flowStatus) {
		FlowStatus = flowStatus;
	}

}
