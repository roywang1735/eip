package com.wordgod.eip.Model;

public class ClassRoomUnavail {

	private String classRoomUnavail_seq;
    private String school_code;
    private String classRoomName;
    private String seatNo;
    private String dateFrom;
    private String dateTo;
    private String updater;
    private String updateTime; 
    private String comment;
  
	public ClassRoomUnavail(String classRoomUnavail_seq,String school_code,String classRoomName,String seatNo,String dateFrom,String dateTo,String updater,String updateTime,String comment) {
		this.classRoomUnavail_seq = classRoomUnavail_seq;
		this.school_code = school_code;
		this.classRoomName = classRoomName;
		this.seatNo = seatNo;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.updater = updater;
		this.updateTime = updateTime;
		this.comment = comment;
	}

	public String getClassRoomUnavail_seq() {
		return classRoomUnavail_seq;
	}

	public void setClassRoomUnavail_seq(String classRoomUnavail_seq) {
		this.classRoomUnavail_seq = classRoomUnavail_seq;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getClassRoomName() {
		return classRoomName;
	}

	public void setClassRoomName(String classRoomName) {
		this.classRoomName = classRoomName;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}


}
