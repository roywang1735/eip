package com.wordgod.eip.Model;

public class JLM_gradeRegister {
	private String gradeRegister_seq;
	private String student_no;
	private String gradeId;
    private String gradeName;
    private String saleId;
    private String salePerson;
    private String registerDate;
    private String sitNo;
    private String eip_grade_seq;

    
	public JLM_gradeRegister(String gradeRegister_seq,String student_no,String gradeId,String gradeName,String saleId,String salePerson,String registerDate,String sitNo,String eip_grade_seq) {
		this.gradeRegister_seq = gradeRegister_seq;
		this.student_no = student_no;
		this.gradeId = gradeId;
		this.gradeName = gradeName;
		this.saleId = saleId;
		this.salePerson = salePerson;
		this.registerDate = registerDate;
		this.sitNo = sitNo;
		this.eip_grade_seq = eip_grade_seq;
	}


	public String getGradeRegister_seq() {
		return gradeRegister_seq;
	}


	public void setGradeRegister_seq(String gradeRegister_seq) {
		this.gradeRegister_seq = gradeRegister_seq;
	}


	public String getStudent_no() {
		return student_no;
	}


	public void setStudent_no(String student_no) {
		this.student_no = student_no;
	}


	public String getGradeName() {
		return gradeName;
	}


	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}


	public String getSalePerson() {
		return salePerson;
	}


	public void setSalePerson(String salePerson) {
		this.salePerson = salePerson;
	}


	public String getRegisterDate() {
		return registerDate;
	}


	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}


	public String getSitNo() {
		return sitNo;
	}


	public void setSitNo(String sitNo) {
		this.sitNo = sitNo;
	}


	public String getGradeId() {
		return gradeId;
	}


	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}


	public String getSaleId() {
		return saleId;
	}


	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}


	public String getEip_grade_seq() {
		return eip_grade_seq;
	}


	public void setEip_grade_seq(String eip_grade_seq) {
		this.eip_grade_seq = eip_grade_seq;
	}

}
