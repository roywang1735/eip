package com.wordgod.eip.Model;

public class Subject_tmp {
	private String schedule_time;
    private String subject_id;
    private String category_id;
    private String name;
    private String abbr;
    private String price;
    private String remark;
    private String isVideo;
    private String isVirtual;
    private String parent_seq;
    private String updater;
    private String update_time;
    private String active;
    private String subjectContent_code ;
    
	public Subject_tmp(
			String schedule_time,
			String subject_id,
			String category_id,
			String name,
			String abbr,
			String price,
			String remark,
			String isVideo,
			String isVirtual,
			String parent_seq,
			String updater,
			String update_time,
			String active,
			String subjectContent_code 
		) {
		this.schedule_time = schedule_time;
		this.subject_id = subject_id;
		this.category_id = category_id;
		this.name = name;
		this.abbr = abbr;
		this.price = price;		
		this.remark = remark;
		this.isVideo = isVideo;
		this.isVirtual = isVirtual;
		this.parent_seq = parent_seq;
		this.updater = updater;
		this.update_time = update_time;
		this.active = active;
		this.subjectContent_code = subjectContent_code ;
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

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsVideo() {
		return isVideo;
	}

	public void setIsVideo(String isVideo) {
		this.isVideo = isVideo;
	}

	public String getIsVirtual() {
		return isVirtual;
	}

	public void setIsVirtual(String isVirtual) {
		this.isVirtual = isVirtual;
	}

	public String getParent_seq() {
		return parent_seq;
	}

	public void setParent_seq(String parent_seq) {
		this.parent_seq = parent_seq;
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

	public String getSubjectContent_code() {
		return subjectContent_code;
	}

	public void setSubjectContent_code(String subjectContent_code) {
		this.subjectContent_code = subjectContent_code;
	}	
}
