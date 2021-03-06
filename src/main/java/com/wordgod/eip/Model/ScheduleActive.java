package com.wordgod.eip.Model;

public class ScheduleActive {

	private String schedule_id;
    private String active;
  
  
	public ScheduleActive(String schedule_id,String active) {
		this.schedule_id = schedule_id;
		this.active = active;
	}


	public String getSchedule_id() {
		return schedule_id;
	}


	public void setSchedule_id(String schedule_id) {
		this.schedule_id = schedule_id;
	}


	public String getActive() {
		return active;
	}


	public void setActive(String active) {
		this.active = active;
	}

}
