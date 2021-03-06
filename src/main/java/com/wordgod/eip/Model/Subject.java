package com.wordgod.eip.Model;

public class Subject {
	private String subject_seq;
    private String category_id;
    private String category_name;
    private String name;
    private String abbr;
    private String price;
    private String hrPrice_R;
    private String counselingPrice_R;
    private String lagnappePrice_R;
    private String handoutPrice_R;
    private String homeworkPrice_R;
    private String mockPrice_R;
    private String remark;
    private String isVideo;
    private String isVirtual;
    private String parent_seq;
    private String updater;
    private String update_time;
    private String active;
    private String subjectContent_code;
    private String subjectContent_name;
    private String subjectTeacherGrade;
    private String free_makeUpNo;
    private String disable;
    
	public Subject(
			String subject_seq,
			String category_id,
			String category_name,
			String name,
			String abbr,
			String price,
			String hrPrice_R,
			String counselingPrice_R,
			String lagnappePrice_R,
			String handoutPrice_R,
			String homeworkPrice_R,
			String mockPrice_R,
			String remark,
			String isVideo,
			String isVirtual,
			String parent_seq,
			String updater,
			String update_time,
			String active,
			String subjectContent_code,
			String subjectContent_name,
			String subjectTeacherGrade,
			String free_makeUpNo,
			String disable
		) {
		this.subject_seq = subject_seq;
		this.category_id = category_id;
		this.category_name = category_name;
		this.name = name;
		this.abbr = abbr;
		this.price = price;		
		this.hrPrice_R = hrPrice_R;
		this.counselingPrice_R = counselingPrice_R;
		this.lagnappePrice_R = lagnappePrice_R;
		this.handoutPrice_R = handoutPrice_R;
		this.homeworkPrice_R = homeworkPrice_R;
		this.mockPrice_R = mockPrice_R;
		this.remark = remark;
		this.isVideo = isVideo;
		this.isVirtual = isVirtual;
		this.parent_seq = parent_seq;
		this.updater = updater;
		this.update_time = update_time;
		this.active = active;
		this.subjectContent_code = subjectContent_code;
		this.subjectContent_name = subjectContent_name;
		this.subjectTeacherGrade = subjectTeacherGrade;
		this.free_makeUpNo = free_makeUpNo;
		this.disable = disable;
	}

	public Subject() {
		// TODO Auto-generated constructor stub
	}

	public String getSubject_seq() {
		return subject_seq;
	}

	public void setSubject_seq(String subject_seq) {
		this.subject_seq = subject_seq;
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
	
	public String getHrPrice_R() {
		return hrPrice_R;
	}

	public void setHrPrice_R(String hrPrice_R) {
		this.hrPrice_R = hrPrice_R;
	}

	public String getCounselingPrice_R() {
		return counselingPrice_R;
	}

	public void setCounselingPrice_R(String counselingPrice_R) {
		this.counselingPrice_R = counselingPrice_R;
	}

	public String getLagnappePrice_R() {
		return lagnappePrice_R;
	}

	public void setLagnappePrice_R(String lagnappePrice_R) {
		this.lagnappePrice_R = lagnappePrice_R;
	}

	public String getHandoutPrice_R() {
		return handoutPrice_R;
	}

	public void setHandoutPrice_R(String handoutPrice_R) {
		this.handoutPrice_R = handoutPrice_R;
	}

	public String getHomeworkPrice_R() {
		return homeworkPrice_R;
	}

	public void setHomeworkPrice_R(String homeworkPrice_R) {
		this.homeworkPrice_R = homeworkPrice_R;
	}

	public String getMockPrice_R() {
		return mockPrice_R;
	}

	public void setMockPrice_R(String mockPrice_R) {
		this.mockPrice_R = mockPrice_R;
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

	public String getSubjectContent_name() {
		return subjectContent_name;
	}

	public void setSubjectContent_name(String subjectContent_name) {
		this.subjectContent_name = subjectContent_name;
	}

	public String getSubjectTeacherGrade() {
		return subjectTeacherGrade;
	}

	public void setSubjectTeacherGrade(String subjectTeacherGrade) {
		this.subjectTeacherGrade = subjectTeacherGrade;
	}

	public String getFree_makeUpNo() {
		return free_makeUpNo;
	}

	public void setFree_makeUpNo(String free_makeUpNo) {
		this.free_makeUpNo = free_makeUpNo;
	}

	public String getDisable() {
		return disable;
	}

	public void setDisable(String disable) {
		this.disable = disable;
	}

}
