package com.wordgod.eip.Model;

public class Teacher {
	private String teacher_seq;
	private String code;
	private String name;
	private String realName;
	private String iDN;
	private String tel;
	private String address;
	private String enabled;
	private String creater;
	private String update_time;
	private String virtual_teacher;
	private String school_id;
	private String email;
	private String line;
	private String fb;
	private String email_contact;
	private String line_contact;
	private String fb_contact;
	private String tel_contact;
	private String face_contact;
	private String checkStr;
	private String county;
	private String district;

	public Teacher(
			String teacher_seq,
			String code,
			String name,
			String realName,
			String iDN,
			String tel,
			String address,
			String enabled,
			String creater,
			String update_time,
			String virtual_teacher,
			String school_id,
			String email,
			String line,
			String fb,
			String email_contact,
			String line_contact,
			String fb_contact,
			String tel_contact,
			String face_contact,
			String checkStr,
			String county,
			String district
		) {
		this.teacher_seq = teacher_seq;
		this.code = code;
		this.name = name;
		this.realName = realName;
		this.iDN = iDN;
		this.tel = tel;
		this.address = address;
		this.enabled = enabled;
		this.creater = creater;
		this.update_time = update_time;
		this.virtual_teacher = virtual_teacher;
		this.school_id = school_id;
		this.email = email;
		this.line = line;
		this.fb = fb;
		this.email_contact = email_contact;
		this.line_contact = line_contact;
		this.fb_contact = fb_contact;
		this.tel_contact = tel_contact;
		this.face_contact = face_contact;
		this.checkStr = checkStr;
		this.county = county;
		this.district = district;
	}

	public Teacher() {
		// TODO Auto-generated constructor stub
	}

	public String getTeacher_seq() {
		return teacher_seq;
	}

	public void setTeacher_seq(String teacher_seq) {
		this.teacher_seq = teacher_seq;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getiDN() {
		return iDN;
	}

	public void setiDN(String iDN) {
		this.iDN = iDN;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
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

	public String getVirtual_teacher() {
		return virtual_teacher;
	}

	public void setVirtual_teacher(String virtual_teacher) {
		this.virtual_teacher = virtual_teacher;
	}

	public String getSchool_id() {
		return school_id;
	}

	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getFb() {
		return fb;
	}

	public void setFb(String fb) {
		this.fb = fb;
	}

	public String getEmail_contact() {
		return email_contact;
	}

	public void setEmail_contact(String email_contact) {
		this.email_contact = email_contact;
	}

	public String getLine_contact() {
		return line_contact;
	}

	public void setLine_contact(String line_contact) {
		this.line_contact = line_contact;
	}

	public String getFb_contact() {
		return fb_contact;
	}

	public void setFb_contact(String fb_contact) {
		this.fb_contact = fb_contact;
	}

	public String getTel_contact() {
		return tel_contact;
	}

	public void setTel_contact(String tel_contact) {
		this.tel_contact = tel_contact;
	}

	public String getFace_contact() {
		return face_contact;
	}

	public void setFace_contact(String face_contact) {
		this.face_contact = face_contact;
	}

	public String getCheckStr() {
		return checkStr;
	}

	public void setCheckStr(String checkStr) {
		this.checkStr = checkStr;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}


}
