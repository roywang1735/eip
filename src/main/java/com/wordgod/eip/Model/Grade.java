package com.wordgod.eip.Model;

import java.util.List;

public class Grade{
	private String grade_seq;
	private String school_code;
	private String school_name;
	private String category_id;
	private String category_name;
    private String subject_id;
    private String subject_name;
    private String subject_abbr;
    private String status_code="0";//起始值,為草稿暫存狀態
    private String status_name;
    private String gradeName;
    private String class_no;
    private String class_makeup;
    private String class_style;
    private String lower_limit;
    private String upper_limit;
    private String class_grouping;
    private String teacher_id;
    private String teacher_name;
    private String teacher_code;
    private String class_start_date_0;
    private String class_start_date;
    private String class_xth_date;
    private String slot_id;
    private String slot_name;
    private String timeFrom;
    private String timeTo;
    private String video_date;
    private String GradeConflict;
    private List<String> classGroupStr;
    private String creater;
    private String update_time;
    private String attendStatus;//即將開課 or 進行中
    private String attendStatusV;//尚未錄影 or 錄影中
    private String onSell;
    private String offSell;
    private String gradeNo; //實體訂班人數
    private String subject_price;
    private String pre_grade_remark;
    private String register_comboSale_grade_statusStr; //學生取消,保留此課程
    private String gradeNo2; //應選期別
    private String gradeNoATime; //同選期別
    private String class_style_img; //實體,Video,online圖案
    private String subjectContent_name; //subject 內容等級名稱
    private String grade_date; //期別
    private String class_date_final; //最後一堂課日期
    private String class_date_start;
    private String gradeFreeNo; //實體贈課人數
    private String gradeFee; //實體訂班費用
    private String gradeFreeFee; //實體贈課費用
    private String gradeNo_v; //Video訂班人數
    private String gradeFreeNo_v; //Video贈課人數
    private String gradeFee_v; //Video訂班費用
    private String gradeFreeFee_v; //Video贈課費用  
    private String gradeNoRatioStr; //多班別占比
    private String lastClassDate; //結業日
    private String haveRead; //簽核已讀
    private String disable;
    private String videoPreserve;
    
	public Grade() {
		// TODO Auto-generated constructor stub
	}
    public Grade(
    		String grade_seq,
    		String school_code,
    		String school_name,
    		String category_id,
    		String category_name,
    		String subject_id,
    		String subject_name,
    		String subject_abbr,
    		String status_code,
    		String status_name,
    		String gradeName,
    		String class_no,
    		String class_makeup,
    		String class_style,
    		String lower_limit,
    		String upper_limit,
    		String teacher_id,
    		String teacher_name,
    		String teacher_code,
    		String class_start_date_0,
    		String class_start_date,
    		String class_xth_date,
    		String slot_id,
    		String slot_name,
    		String timeFrom,
    		String timeTo,
    		String video_date,
    		String GradeConflict,
    		List<String> classGroupStr,
    		String creater,
    		String update_time,
    		String attendStatus,
    		String attendStatusV,
    		String onSell,
    		String offSell,
    		String gradeNo,
    		String subject_price,
    		String pre_grade_remark,
    		String register_comboSale_grade_statusStr,
    		String gradeNo2,
    		String gradeNoATime,
    		String class_style_img,
    		String subjectContent_name,
    		String grade_date,
    		String class_date_final,
    		String class_date_start,
    		String gradeFreeNo,
    		String gradeFee,
    		String gradeFreeFee,
    		String gradeNo_v,
    		String gradeFreeNo_v,
    		String gradeFee_v,
    		String gradeFreeFee_v,
    		String gradeNoRatioStr,
    		String lastClassDate,
    		String haveRead,
    		String disable,
    		String videoPreserve
    	) {
		this.grade_seq = grade_seq;
		this.school_code = school_code;
		this.school_name = school_name;
		this.category_id = category_id;
		this.category_name = category_name;
		this.subject_id = subject_id;
		this.subject_name = subject_name;
		this.subject_abbr = subject_abbr;
		this.status_code = status_code;
		this.status_name = status_name;
		this.gradeName = gradeName;
		this.class_no = class_no;
		this.class_makeup = class_makeup;
		this.class_style = class_style;
		this.lower_limit = lower_limit;
		this.upper_limit = upper_limit;
		this.teacher_id = teacher_id;
		this.teacher_name = teacher_name;
		this.teacher_code = teacher_code;
		this.class_start_date_0 = class_start_date_0;
		this.class_start_date = class_start_date;
		this.class_xth_date = class_xth_date;
		this.slot_id = slot_id;
		this.slot_name = slot_name;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.video_date = video_date;
		this.GradeConflict = GradeConflict;
		this.classGroupStr = classGroupStr;
		this.creater = creater;
		this.update_time = update_time;
		this.attendStatus = attendStatus;
		this.attendStatusV = attendStatusV;
		this.onSell = onSell;
		this.offSell = offSell;
		this.gradeNo = gradeNo;
		this.subject_price = subject_price;
		this.pre_grade_remark = pre_grade_remark;
		this.register_comboSale_grade_statusStr = register_comboSale_grade_statusStr;
		this.gradeNo2 = gradeNo2;
		this.gradeNoATime = gradeNoATime;
		this.class_style_img = class_style_img;
		this.subjectContent_name = subjectContent_name;
		this.grade_date = grade_date;
		this.class_date_final = class_date_final;
		this.class_date_start = class_date_start;
		this.gradeFreeNo = gradeFreeNo;
		this.gradeFee = gradeFee;
		this.gradeFreeFee = gradeFreeFee;
		this.gradeNo_v = gradeNo_v;
		this.gradeFreeNo_v = gradeFreeNo_v;
		this.gradeFee_v = gradeFee_v;
		this.gradeFreeFee_v = gradeFreeFee_v;
		this.gradeNoRatioStr = gradeNoRatioStr;
		this.lastClassDate = lastClassDate;
		this.haveRead = haveRead;
		this.disable = disable;
		this.videoPreserve = videoPreserve;
	}
	public String getGrade_seq() {
		return grade_seq;
	}
	public void setGrade_seq(String grade_seq) {
		this.grade_seq = grade_seq;
	}
	public String getSchool_code() {
		return school_code;
	}
	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public String getClass_no() {
		return class_no;
	}
	public void setClass_no(String class_no) {
		this.class_no = class_no;
	}
	public String getClass_makeup() {
		return class_makeup;
	}
	public void setClass_makeup(String class_makeup) {
		this.class_makeup = class_makeup;
	}
	public String getClass_style() {
		return class_style;
	}
	public void setClass_style(String class_style) {
		this.class_style = class_style;
	}
	public String getLower_limit() {
		return lower_limit;
	}
	public void setLower_limit(String lower_limit) {
		this.lower_limit = lower_limit;
	}
	public String getUpper_limit() {
		return upper_limit;
	}
	public void setUpper_limit(String upper_limit) {
		this.upper_limit = upper_limit;
	}
	public String getClass_grouping() {
		return class_grouping;
	}
	public void setClass_grouping(String class_grouping) {
		this.class_grouping = class_grouping;
	}
	public String getTeacher_id() {
		return teacher_id;
	}
	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getSubject_name() {
		return subject_name;
	}
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
	public String getTeacher_name() {
		return teacher_name;
	}
	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
	public String getStatus_name() {
		return status_name;
	}
	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}
	public String getSubject_abbr() {
		return subject_abbr;
	}
	public void setSubject_abbr(String subject_abbr) {
		this.subject_abbr = subject_abbr;
	}
	public String getTeacher_code() {
		return teacher_code;
	}
	public void setTeacher_code(String teacher_code) {
		this.teacher_code = teacher_code;
	}	
	public String getClass_start_date_0() {
		return class_start_date_0;
	}
	public void setClass_start_date_0(String class_start_date_0) {
		this.class_start_date_0 = class_start_date_0;
	}
	public String getClass_start_date() {
		return class_start_date;
	}
	public void setClass_start_date(String class_start_date) {
		this.class_start_date = class_start_date;
	}
	
	public String getClass_xth_date() {
		return class_xth_date;
	}
	public void setClass_xth_date(String class_xth_date) {
		this.class_xth_date = class_xth_date;
	}
	public String getSlot_id() {
		return slot_id;
	}
	public void setSlot_id(String slot_id) {
		this.slot_id = slot_id;
	}
	public String getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}
	public String getTimeTo() {
		return timeTo;
	}
	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getSlot_name() {
		return slot_name;
	}
	public void setSlot_name(String slot_name) {
		this.slot_name = slot_name;
	}
	public String getVideo_date() {
		return video_date;
	}
	public void setVideo_date(String video_date) {
		this.video_date = video_date;
	}
	public String getGradeConflict() {
		return GradeConflict;
	}
	public void setGradeConflict(String gradeConflict) {
		GradeConflict = gradeConflict;
	}
	public List<String> getClassGroupStr() {
		return classGroupStr;
	}
	public void setClassGroupStr(List<String> classGroupStr) {
		this.classGroupStr = classGroupStr;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getAttendStatus() {
		return attendStatus;
	}
	public void setAttendStatus(String attendStatus) {
		this.attendStatus = attendStatus;
	}
	public String getAttendStatusV() {
		return attendStatusV;
	}
	public void setAttendStatusV(String attendStatusV) {
		this.attendStatusV = attendStatusV;
	}
	public String getOnSell() {
		return onSell;
	}
	public void setOnSell(String onSell) {
		this.onSell = onSell;
	}
	public String getOffSell() {
		return offSell;
	}
	public void setOffSell(String offSell) {
		this.offSell = offSell;
	}
	public String getGradeNo() {
		return gradeNo;
	}
	public void setGradeNo(String gradeNo) {
		this.gradeNo = gradeNo;
	}
	public String getSubject_price() {
		return subject_price;
	}
	public void setSubject_price(String subject_price) {
		this.subject_price = subject_price;
	}
	public String getPre_grade_remark() {
		return pre_grade_remark;
	}
	public void setPre_grade_remark(String pre_grade_remark) {
		this.pre_grade_remark = pre_grade_remark;
	}
	public String getRegister_comboSale_grade_statusStr() {
		return register_comboSale_grade_statusStr;
	}
	public void setRegister_comboSale_grade_statusStr(String register_comboSale_grade_statusStr) {
		this.register_comboSale_grade_statusStr = register_comboSale_grade_statusStr;
	}
	public String getGradeNo2() {
		return gradeNo2;
	}
	public void setGradeNo2(String gradeNo2) {
		this.gradeNo2 = gradeNo2;
	}
	public String getGradeNoATime() {
		return gradeNoATime;
	}
	public void setGradeNoATime(String gradeNoATime) {
		this.gradeNoATime = gradeNoATime;
	}
	public String getClass_style_img() {
		return class_style_img;
	}
	public void setClass_style_img(String class_style_img) {
		this.class_style_img = class_style_img;
	}
	public String getSubjectContent_name() {
		return subjectContent_name;
	}
	public void setSubjectContent_name(String subjectContent_name) {
		this.subjectContent_name = subjectContent_name;
	}
	public String getGrade_date() {
		return grade_date;
	}
	public void setGrade_date(String grade_date) {
		this.grade_date = grade_date;
	}
	public String getClass_date_final() {
		return class_date_final;
	}
	public void setClass_date_final(String class_date_final) {
		this.class_date_final = class_date_final;
	}
	public String getClass_date_start() {
		return class_date_start;
	}
	public void setClass_date_start(String class_date_start) {
		this.class_date_start = class_date_start;
	}
	public String getGradeFreeNo() {
		return gradeFreeNo;
	}
	public void setGradeFreeNo(String gradeFreeNo) {
		this.gradeFreeNo = gradeFreeNo;
	}
	public String getGradeFee() {
		return gradeFee;
	}
	public void setGradeFee(String gradeFee) {
		this.gradeFee = gradeFee;
	}
	public String getGradeFreeFee() {
		return gradeFreeFee;
	}
	public void setGradeFreeFee(String gradeFreeFee) {
		this.gradeFreeFee = gradeFreeFee;
	}
	
	public String getGradeNo_v() {
		return gradeNo_v;
	}
	public void setGradeNo_v(String gradeNo_v) {
		this.gradeNo_v = gradeNo_v;
	}
	public String getGradeFreeNo_v() {
		return gradeFreeNo_v;
	}
	public void setGradeFreeNo_v(String gradeFreeNo_v) {
		this.gradeFreeNo_v = gradeFreeNo_v;
	}
	public String getGradeFee_v() {
		return gradeFee_v;
	}
	public void setGradeFee_v(String gradeFee_v) {
		this.gradeFee_v = gradeFee_v;
	}
	public String getGradeFreeFee_v() {
		return gradeFreeFee_v;
	}
	public void setGradeFreeFee_v(String gradeFreeFee_v) {
		this.gradeFreeFee_v = gradeFreeFee_v;
	}
	public String getGradeNoRatioStr() {
		return gradeNoRatioStr;
	}
	public void setGradeNoRatioStr(String gradeNoRatioStr) {
		this.gradeNoRatioStr = gradeNoRatioStr;
	}
	public String getLastClassDate() {
		return lastClassDate;
	}
	public void setLastClassDate(String lastClassDate) {
		this.lastClassDate = lastClassDate;
	}
	public String getHaveRead() {
		return haveRead;
	}
	public void setHaveRead(String haveRead) {
		this.haveRead = haveRead;
	}
	public String getDisable() {
		return disable;
	}
	public void setDisable(String disable) {
		this.disable = disable;
	}
	public String getVideoPreserve() {
		return videoPreserve;
	}
	public void setVideoPreserve(String videoPreserve) {
		this.videoPreserve = videoPreserve;
	}	
	
}
