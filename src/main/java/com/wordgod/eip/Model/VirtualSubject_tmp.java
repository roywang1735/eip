package com.wordgod.eip.Model;

public class VirtualSubject_tmp {
	private String schedule_time;
    private String subject_id;
    private String child_subject_id;
 
	public VirtualSubject_tmp(
			String schedule_time,
			String subject_id,
			String child_subject_id
		) {
		this.schedule_time = schedule_time;
		this.subject_id = subject_id;
		this.child_subject_id = child_subject_id;
	}

	public String getSchedule_time() {
		return schedule_time;
	}

	public void setSchedule_time(String schedule_time) {
		this.schedule_time = schedule_time;
	}

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public String getChild_subject_id() {
		return child_subject_id;
	}

	public void setChild_subject_id(String child_subject_id) {
		this.child_subject_id = child_subject_id;
	}

	
}
