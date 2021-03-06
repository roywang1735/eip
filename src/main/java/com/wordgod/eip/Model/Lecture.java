package com.wordgod.eip.Model;

public class Lecture {
	private String lecture_seq;
	private String school_id;
    private String school_name;
	private String teacher_id;
    private String teacher_name;    
    private String location;
    private String lectureDate;
    private String lectureTimeFrom;
    private String lectureTimeTo;
    private String category_id;
    private String category_name;
    private String lectureName;
    private String charText;
    private String remark;
    private String creater;
    private String createTime;
    private String active;
    
	public Lecture(
			String lecture_seq,
			String school_id,
			String school_name,
			String teacher_id,
			String teacher_name,			
			String location,
			String lectureDate,
			String lectureTimeFrom,
			String lectureTimeTo,
			String category_id,
			String category_name,
			String lectureName,
			String charText,
			String remark,
			String creater,
			String createTime,
			String active
	) {
		this.lecture_seq = lecture_seq;
		this.school_id = school_id;
		this.school_name = school_name;
		this.teacher_id = teacher_id;
		this.teacher_name = teacher_name;		
		this.location = location;
		this.lectureDate = lectureDate;
		this.lectureTimeFrom = lectureTimeFrom;
		this.lectureTimeTo = lectureTimeTo;
		this.category_id = category_id;
		this.category_name = category_name;
		this.lectureName = lectureName;
		this.charText = charText;
		this.remark = remark;
		this.creater = creater;
		this.createTime = createTime;
		this.active = active;
	}

	public Lecture() {
		// TODO Auto-generated constructor stub
	}

	public String getLecture_seq() {
		return lecture_seq;
	}

	public void setLecture_seq(String lecture_seq) {
		this.lecture_seq = lecture_seq;
	}

	public String getSchool_id() {
		return school_id;
	}

	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLectureDate() {
		return lectureDate;
	}

	public void setLectureDate(String lectureDate) {
		this.lectureDate = lectureDate;
	}

	public String getLectureTimeFrom() {
		return lectureTimeFrom;
	}

	public void setLectureTimeFrom(String lectureTimeFrom) {
		this.lectureTimeFrom = lectureTimeFrom;
	}

	public String getLectureTimeTo() {
		return lectureTimeTo;
	}

	public void setLectureTimeTo(String lectureTimeTo) {
		this.lectureTimeTo = lectureTimeTo;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getLectureName() {
		return lectureName;
	}

	public void setLectureName(String lectureName) {
		this.lectureName = lectureName;
	}

	public String getCharText() {
		return charText;
	}

	public void setCharText(String charText) {
		this.charText = charText;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
}
