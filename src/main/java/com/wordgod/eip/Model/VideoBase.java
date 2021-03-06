package com.wordgod.eip.Model;

public class VideoBase {

	private String mockBase_seq;
    private String school_code;
    private String setDate;
    private String slot;
    private String slotName;
    private String updater;
    private String update_time;
    private String weekDay;
    
  
	public VideoBase(String mockBase_seq,String school_code,String setDate,String slot,String slotName,String updater,String update_time,String weekDay) {
		this.mockBase_seq = mockBase_seq;
		this.school_code = school_code;
		this.setDate = setDate;
		this.slot = slot;
		this.slotName = slotName;
		this.updater = updater;
		this.update_time = update_time;
		this.weekDay = weekDay;
	}

	public VideoBase() {
		// TODO Auto-generated constructor stub
	}

	public String getMockBase_seq() {
		return mockBase_seq;
	}

	public void setMockBase_seq(String mockBase_seq) {
		this.mockBase_seq = mockBase_seq;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getSetDate() {
		return setDate;
	}

	public void setSetDate(String setDate) {
		this.setDate = setDate;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getSlotName() {
		return slotName;
	}

	public void setSlotName(String slotName) {
		this.slotName = slotName;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}


}
