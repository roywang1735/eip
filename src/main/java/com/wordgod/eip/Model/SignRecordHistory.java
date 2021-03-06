package com.wordgod.eip.Model;

/**
 * @author roy
 *
 */
public class SignRecordHistory {
	private String signRecordHistory_seq;
	private String register_comboSale_grade_id;
	private String register_status;
    private String student_id;
    private String student_no;
    private String ch_name;
    private String class_style;
    private String school_code;
    private String school_name;
    private String grade_id;
    private String class_th;
    private String attend;
    private String active;
    private String attend_date;
    private String slot;
    private String update_time;
    private String updater;
    private String subject_name;
    private String class_start_date;
    private String class_date;
    private String time_from;
    private String time_to;
    private String makeUpNo;
    private String class_th_ex;
    private String teacher_name;
    private String class_id;
    private String classroom;
    private String seat;
    private String comment;
    private String allowAttend;
    private String video_date;
    private String class_th_seq;
    
	public SignRecordHistory (
			String signRecordHistory_seq,
			String register_comboSale_grade_id,
			String register_status,
			String student_id,
			String student_no,
			String ch_name,
			String class_style,
			String school_code,
			String school_name,
			String grade_id,
			String class_th,
			String attend,
			String active,
			String attend_date, //預約日期			
			String slot,
			String update_time,
			String updater,
			String subject_name,
			String class_start_date, //期別		
			String class_date, //該課原先日期		
			String time_from, //該課原先時間
			String time_to,
			String makeUpNo,
			String class_th_ex,
			String teacher_name,
			String class_id,
			String classroom,
			String seat,
			String comment,
			String allowAttend,
			String video_date,
			String class_th_seq
		) {
		this.signRecordHistory_seq = signRecordHistory_seq;
		this.register_comboSale_grade_id = register_comboSale_grade_id;
		this.register_status = register_status;
		this.student_id = student_id;
		this.student_no = student_no;
		this.ch_name = ch_name;
		this.class_style = class_style;
		this.school_code = school_code;
		this.school_name = school_name;
		this.grade_id = grade_id;
		this.class_th = class_th;
		this.attend = attend;
		this.active = active;
		this.slot = slot;
		this.update_time = update_time;
		this.updater = updater;	
		this.subject_name = subject_name;
		this.class_start_date = class_start_date;
		this.class_date = class_date;
		this.attend_date = attend_date;
		this.time_from = time_from;
		this.time_to = time_to;
		this.makeUpNo = makeUpNo;
		this.class_th_ex = class_th_ex;
		this.teacher_name = teacher_name;
		this.class_id = class_id;
		this.classroom = classroom;
		this.seat = seat;
		this.comment = comment;
		this.allowAttend = allowAttend;
		this.video_date = video_date;
		this.class_th_seq = class_th_seq;
	}

	public String getSignRecordHistory_seq() {
		return signRecordHistory_seq;
	}

	public void setSignRecordHistory_seq(String signRecordHistory_seq) {
		this.signRecordHistory_seq = signRecordHistory_seq;
	}

	public String getRegister_comboSale_grade_id() {
		return register_comboSale_grade_id;
	}

	public void setRegister_comboSale_grade_id(String register_comboSale_grade_id) {
		this.register_comboSale_grade_id = register_comboSale_grade_id;
	}

	public String getRegister_status() {
		return register_status;
	}

	public void setRegister_status(String register_status) {
		this.register_status = register_status;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getStudent_no() {
		return student_no;
	}

	public void setStudent_no(String student_no) {
		this.student_no = student_no;
	}

	public String getCh_name() {
		return ch_name;
	}

	public void setCh_name(String ch_name) {
		this.ch_name = ch_name;
	}

	public String getClass_style() {
		return class_style;
	}

	public void setClass_style(String class_style) {
		this.class_style = class_style;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public String getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(String grade_id) {
		this.grade_id = grade_id;
	}

	public String getClass_th() {
		return class_th;
	}

	public void setClass_th(String class_th) {
		this.class_th = class_th;
	}

	public String getAttend() {
		return attend;
	}

	public void setAttend(String attend) {
		this.attend = attend;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getAttend_date() {
		return attend_date;
	}

	public void setAttend_date(String attend_date) {
		this.attend_date = attend_date;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public String getClass_start_date() {
		return class_start_date;
	}

	public void setClass_start_date(String class_start_date) {
		this.class_start_date = class_start_date;
	}

	public String getClass_date() {
		return class_date;
	}

	public void setClass_date(String class_date) {
		this.class_date = class_date;
	}

	public String getTime_from() {
		return time_from;
	}

	public void setTime_from(String time_from) {
		this.time_from = time_from;
	}

	public String getTime_to() {
		return time_to;
	}

	public void setTime_to(String time_to) {
		this.time_to = time_to;
	}

	public String getMakeUpNo() {
		return makeUpNo;
	}

	public void setMakeUpNo(String makeUpNo) {
		this.makeUpNo = makeUpNo;
	}

	public String getClass_th_ex() {
		return class_th_ex;
	}

	public void setClass_th_ex(String class_th_ex) {
		this.class_th_ex = class_th_ex;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public String getClass_id() {
		return class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAllowAttend() {
		return allowAttend;
	}

	public void setAllowAttend(String allowAttend) {
		this.allowAttend = allowAttend;
	}

	public String getVideo_date() {
		return video_date;
	}

	public void setVideo_date(String video_date) {
		this.video_date = video_date;
	}

	public String getClass_th_seq() {
		return class_th_seq;
	}

	public void setClass_th_seq(String class_th_seq) {
		this.class_th_seq = class_th_seq;
	}

}
