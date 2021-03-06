package com.wordgod.eip.Model;

public class CounselingBaseBook {

    private String counselingBaseBook_seq;
    private String counselingBase_id;
    private String student_id;
    private String updater;
    private String update_time;
    private String active;
    private String attend;
    private String setDate;
    private String slot;
    private String comment;
    private String classroom;
    private String seat;
    private String school_code;
    private String counselingLimit_seq;
    private String register_counseling_seq;
    private String ch_name;
    private String student_no;  
    private String limitName;
    private String counseling_name;
    
    public CounselingBaseBook(
    		String counselingBaseBook_seq,
    		String counselingBase_id,
    		String student_id,
    		String updater,
    		String update_time,
    		String active,
    		String attend,
    		String setDate,
    		String slot,
    		String comment,
    		String classroom,
    		String seat,
    		String school_code,
    		String counselingLimit_seq,
    		String register_counseling_seq,
    		String ch_name,
    		String student_no,
    		String limitName,
    		String counseling_name
	    	) {
		        this.counselingBaseBook_seq = counselingBaseBook_seq;
		        this.counselingBase_id = counselingBase_id;
		        this.student_id = student_id;
		        this.updater = updater;
		        this.update_time = update_time;
		        this.active = active;
		        this.attend = attend;
		        this.setDate = setDate;
		        this.slot = slot;
		        this.comment = comment;
		        this.classroom = classroom;
		        this.seat = seat;
		        this.school_code = school_code;
		        this.counselingLimit_seq = counselingLimit_seq;
		        this.register_counseling_seq = register_counseling_seq;
		        this.ch_name = ch_name;
		        this.student_no = student_no;
		        this.limitName = limitName;
		        this.counseling_name = counseling_name;
           }

	public String getCounselingBaseBook_seq() {
		return counselingBaseBook_seq;
	}

	public void setCounselingBaseBook_seq(String counselingBaseBook_seq) {
		this.counselingBaseBook_seq = counselingBaseBook_seq;
	}

	public String getCounselingBase_id() {
		return counselingBase_id;
	}

	public void setCounselingBase_id(String counselingBase_id) {
		this.counselingBase_id = counselingBase_id;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getAttend() {
		return attend;
	}

	public void setAttend(String attend) {
		this.attend = attend;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getCounselingLimit_seq() {
		return counselingLimit_seq;
	}

	public void setCounselingLimit_seq(String counselingLimit_seq) {
		this.counselingLimit_seq = counselingLimit_seq;
	}

	public String getRegister_counseling_seq() {
		return register_counseling_seq;
	}

	public void setRegister_counseling_seq(String register_counseling_seq) {
		this.register_counseling_seq = register_counseling_seq;
	}

	public String getCh_name() {
		return ch_name;
	}

	public void setCh_name(String ch_name) {
		this.ch_name = ch_name;
	}

	public String getStudent_no() {
		return student_no;
	}

	public void setStudent_no(String student_no) {
		this.student_no = student_no;
	}

	public String getLimitName() {
		return limitName;
	}

	public void setLimitName(String limitName) {
		this.limitName = limitName;
	}

	public String getCounseling_name() {
		return counseling_name;
	}

	public void setCounseling_name(String counseling_name) {
		this.counseling_name = counseling_name;
	}


}
