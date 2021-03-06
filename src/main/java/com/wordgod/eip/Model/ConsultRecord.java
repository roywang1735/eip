package com.wordgod.eip.Model;

public class ConsultRecord {
	private String student_id;
	private String consultRecord_seq;
    private String consultCategory_id;
    private String consultCategory_name;
    private String oneDayValid;
    private String content;
    private String validDate;
    private String remark;
    private String employee_id;
    private String employee_name;
    private String employee_school;
    private String createDate;
    private String classCategoryNameSel;
    private String crossDate;
    private String infoFrom_text_1;
    private String nameFrom_text_1;
    private String nameFrom_text_2;
    private String category_id_text_1;
    private String lectureDate;
    private String avaiable;
    
    public ConsultRecord() {}
    public ConsultRecord(
    		String student_id,
    		String consultRecord_seq, 
    		String consultCategory_id,
    		String consultCategory_name,
    		String oneDayValid,
    		String content,
    		String validDate,
    		String remark,
    		String employee_id,
    		String employee_name,
    		String employee_school,
    		String createDate,
    		String classCategoryNameSel,
    		String crossDate,
    		String infoFrom_text_1,
    		String nameFrom_text_1,
    		String nameFrom_text_2,
    		String category_id_text_1,
    		String lectureDate,
    		String avaiable
    ){
        this.student_id = student_id;
    	this.consultRecord_seq = consultRecord_seq;
        this.consultCategory_id = consultCategory_id;
        this.consultCategory_name = consultCategory_name;
        this.oneDayValid = oneDayValid;
        this.content = content;
        this.validDate = validDate;
        this.remark = remark;
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.employee_school = employee_school;
        this.createDate = createDate;
        this.classCategoryNameSel = classCategoryNameSel;
        this.crossDate = crossDate;
        this.infoFrom_text_1 = infoFrom_text_1;
        this.nameFrom_text_1 = nameFrom_text_1;
        this.nameFrom_text_2 = nameFrom_text_2;
        this.category_id_text_1 = category_id_text_1;
        this.lectureDate = lectureDate;
        this.avaiable = avaiable;
    }

	public String getStudent_id() {
		return student_id;
	}
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}
	public String getConsultRecord_seq() {
		return consultRecord_seq;
	}

	public void setConsultRecord_seq(String consultRecord_seq) {
		this.consultRecord_seq = consultRecord_seq;
	}

	public String getConsultCategory_id() {
		return consultCategory_id;
	}

	public void setConsultCategory_id(String consultCategory_id) {
		this.consultCategory_id = consultCategory_id;
	}

	public String getConsultCategory_name() {
		return consultCategory_name;
	}
	public void setConsultCategory_name(String consultCategory_name) {
		this.consultCategory_name = consultCategory_name;
	}


	public String getOneDayValid() {
		return oneDayValid;
	}
	public void setOneDayValid(String oneDayValid) {
		this.oneDayValid = oneDayValid;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getValidDate() {
		return validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getEmployee_school() {
		return employee_school;
	}
	public void setEmployee_school(String employee_school) {
		this.employee_school = employee_school;
	}
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getClassCategoryNameSel() {
		return classCategoryNameSel;
	}
	public void setClassCategoryNameSel(String classCategoryNameSel) {
		this.classCategoryNameSel = classCategoryNameSel;
	}
	public String getCrossDate() {
		return crossDate;
	}
	public void setCrossDate(String crossDate) {
		this.crossDate = crossDate;
	}
	public String getInfoFrom_text_1() {
		return infoFrom_text_1;
	}
	public void setInfoFrom_text_1(String infoFrom_text_1) {
		this.infoFrom_text_1 = infoFrom_text_1;
	}
	public String getNameFrom_text_1() {
		return nameFrom_text_1;
	}
	public void setNameFrom_text_1(String nameFrom_text_1) {
		this.nameFrom_text_1 = nameFrom_text_1;
	}
	public String getNameFrom_text_2() {
		return nameFrom_text_2;
	}
	public void setNameFrom_text_2(String nameFrom_text_2) {
		this.nameFrom_text_2 = nameFrom_text_2;
	}
	public String getCategory_id_text_1() {
		return category_id_text_1;
	}
	public void setCategory_id_text_1(String category_id_text_1) {
		this.category_id_text_1 = category_id_text_1;
	}
	public String getLectureDate() {
		return lectureDate;
	}
	public void setLectureDate(String lectureDate) {
		this.lectureDate = lectureDate;
	}
	public String getAvaiable() {
		return avaiable;
	}
	public void setAvaiable(String avaiable) {
		this.avaiable = avaiable;
	}

}
