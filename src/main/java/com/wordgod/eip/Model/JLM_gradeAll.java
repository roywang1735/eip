package com.wordgod.eip.Model;

public class JLM_gradeAll {
	private String gradeAll_seq;
    private String category_id;
    private String category_name;
    private String gradeId;
    private String gradeName;
    private String dateFrom;
    private String dateTo;
    private String eip_grade_seq;

    
	public JLM_gradeAll(String gradeAll_seq,String category_id,String category_name,String gradeId,String gradeName,String dateFrom,String dateTo,String eip_grade_seq) {
		this.gradeAll_seq = gradeAll_seq;
		this.category_id = category_id;
		this.category_name = category_name;
		this.gradeId = gradeId;
		this.gradeName = gradeName;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.eip_grade_seq = eip_grade_seq;
	}


	public String getGradeAll_seq() {
		return gradeAll_seq;
	}


	public void setGradeAll_seq(String gradeAll_seq) {
		this.gradeAll_seq = gradeAll_seq;
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


	public String getGradeId() {
		return gradeId;
	}


	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}


	public String getGradeName() {
		return gradeName;
	}


	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
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


	public String getEip_grade_seq() {
		return eip_grade_seq;
	}


	public void setEip_grade_seq(String eip_grade_seq) {
		this.eip_grade_seq = eip_grade_seq;
	}

	
}
