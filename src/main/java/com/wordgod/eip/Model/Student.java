package com.wordgod.eip.Model;

public class Student {
	private String student_seq;
    private String student_no;
    private String ch_name;
    private String en_name;
    private String sex;
    private String category;
    private String derived;
    private String idn;
    private String birthday;
    private String identity;
    private String tel;
    private String mobile_1;
    private String mobile_2;
    private String email_1;
    private String email_2;
    private String fb;
    private String line;    
    private String address_1;
    private String address_2;
    private String parent_1_name;
    private String parent_1_mobile;
    private String parent_1_email;
    private String parent_1_line;
    private String parent_2_name;
    private String parent_2_mobile;
    private String parent_2_email;
    private String parent_2_line;
    private String creater;
    private String create_time;
    private String editor;
    private String remark;
    private String remark2;
    private String special_idn;
    private String exam;
    private String abroad_date;
    private String balanceTotal;
    private String makeUpTotal;
    private String photo;
    private String school_code;
    private String school_code2;
    private String updater;
    private String update_time;
    private String col_5;
    private String sitNo;
    private String remarkTotal;
    private String handover;
    private String tel2;
    private String company_tel;
    private String agent_studentNo;
    private String grade_highSchool;
    private String postCode;
    private String agent_studentName;
    private String passwd;
    private String col_6;
    private String col_7;
    private String col_8;
    private String col_9;
    private String degree;
    private String cowork;
    private String updater2; //選課業務
    private String update_time2;
    
    public Student(
    		String student_seq,
    		String student_no,
    		String ch_name,
    		String en_name,
    		String sex,
    		String category,
    		String derived,
    		String idn,
    		String birthday,
    		String identity,
    		String tel,
    		String mobile_1,
    		String mobile_2,
    		String email_1,
    		String email_2,
    		String address_1,
    		String address_2,
    		String fb,
    		String line,
    		String parent_1_name,
    		String parent_1_mobile,
    		String parent_1_email,
    		String parent_1_line,
    		String parent_2_name,
    		String parent_2_mobile,
    		String parent_2_email,
    		String parent_2_line,
    		String creater,
    		String create_time,
    		String editor,
    		String remark,
    		String remark2,
    		String special_idn,
    		String exam,
    		String abroad_date,
    		String balanceTotal,
    		String makeUpTotal,
    		String photo,
    		String school_code,
    		String school_code2,
    	    String updater,
    	    String update_time,
    	    String col_5,
    	    String sitNo,
    	    String remarkTotal,
    	    String handover,
    	    String tel2,
    	    String company_tel,
    	    String agent_studentNo,
    	    String grade_highSchool,
    	    String postCode,
    	    String agent_studentName,
    	    String passwd,
    	    String col_6,
    	    String col_7,
    	    String col_8,
    	    String col_9,
    	    String degree,
    	    String cowork,
    	    String updater2,
    	    String update_time2
        ) {
        this.student_seq = student_seq;
        this.student_no = student_no;
        this.ch_name = ch_name;
        this.en_name = en_name;
        this.sex = sex;
        this.category = category;
        this.derived = derived;
        this.idn = idn;
        this.birthday = birthday;
        this.identity = identity;
        this.tel = tel;
        this.mobile_1 = mobile_1;
        this.mobile_2 = mobile_2;
        this.email_1 = email_1;
        this.email_2 = email_2;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.fb = fb;
        this.line = line;
        this.parent_1_name = parent_1_name;
        this.parent_1_mobile = parent_1_mobile;
        this.parent_1_email = parent_1_email;
        this.parent_1_line = parent_1_line;
        this.parent_2_name = parent_2_name;
        this.parent_2_mobile = parent_2_mobile;
        this.parent_2_email = parent_2_email;
        this.parent_2_line = parent_2_line;
        this.creater = creater;
        this.create_time = create_time;
        this.editor = editor;
        this.remark = remark;
        this.remark2 = remark2;
        this.special_idn  = special_idn;
        this.exam = exam;
        this.abroad_date = abroad_date;
        this.balanceTotal = balanceTotal;
        this.makeUpTotal = makeUpTotal;
        this.photo = photo;
        this.school_code = school_code;
        this.school_code2 = school_code2;
        this.updater = updater;
        this.update_time = update_time;
        this.col_5 = col_5;
        this.sitNo = sitNo;
        this.remarkTotal = remarkTotal;
        this.handover = handover;
        this.tel2 = tel2;
        this.company_tel = company_tel;
        this.agent_studentNo = agent_studentNo;
        this.grade_highSchool = grade_highSchool;
        this.postCode = postCode;
        this.agent_studentName = agent_studentName;
        this.passwd = passwd;        
        this.col_6 = col_6;
        this.col_7 = col_7;
        this.col_8 = col_8;
        this.col_9 = col_9;
        this.degree = degree;
        this.cowork = cowork;
        this.updater2 = updater2;
        this.update_time2 = update_time2;
    }

	public Student() {
		// TODO Auto-generated constructor stub
	}

	public String getStudent_seq() {
		return student_seq;
	}

	public void setStudent_seq(String student_seq) {
		this.student_seq = student_seq;
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

	public String getEn_name() {
		return en_name;
	}

	public void setEn_name(String en_name) {
		this.en_name = en_name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDerived() {
		return derived;
	}

	public void setDerived(String derived) {
		this.derived = derived;
	}

	public String getIdn() {
		return idn;
	}

	public void setIdn(String idn) {
		this.idn = idn;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile_1() {
		return mobile_1;
	}

	public void setMobile_1(String mobile_1) {
		this.mobile_1 = mobile_1;
	}

	public String getMobile_2() {
		return mobile_2;
	}

	public void setMobile_2(String mobile_2) {
		this.mobile_2 = mobile_2;
	}

	public String getEmail_1() {
		return email_1;
	}

	public void setEmail_1(String email_1) {
		this.email_1 = email_1;
	}

	public String getEmail_2() {
		return email_2;
	}

	public void setEmail_2(String email_2) {
		this.email_2 = email_2;
	}

	public String getAddress_1() {
		return address_1;
	}

	public void setAddress_1(String address_1) {
		this.address_1 = address_1;
	}

	public String getAddress_2() {
		return address_2;
	}

	public void setAddress_2(String address_2) {
		this.address_2 = address_2;
	}

	public String getFb() {
		return fb;
	}

	public void setFb(String fb) {
		this.fb = fb;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getParent_1_name() {
		return parent_1_name;
	}

	public void setParent_1_name(String parent_1_name) {
		this.parent_1_name = parent_1_name;
	}

	public String getParent_1_mobile() {
		return parent_1_mobile;
	}

	public void setParent_1_mobile(String parent_1_mobile) {
		this.parent_1_mobile = parent_1_mobile;
	}

	public String getParent_1_email() {
		return parent_1_email;
	}

	public void setParent_1_email(String parent_1_email) {
		this.parent_1_email = parent_1_email;
	}

	public String getParent_1_line() {
		return parent_1_line;
	}

	public void setParent_1_line(String parent_1_line) {
		this.parent_1_line = parent_1_line;
	}

	public String getParent_2_name() {
		return parent_2_name;
	}

	public void setParent_2_name(String parent_2_name) {
		this.parent_2_name = parent_2_name;
	}

	public String getParent_2_mobile() {
		return parent_2_mobile;
	}

	public void setParent_2_mobile(String parent_2_mobile) {
		this.parent_2_mobile = parent_2_mobile;
	}

	public String getParent_2_email() {
		return parent_2_email;
	}

	public void setParent_2_email(String parent_2_email) {
		this.parent_2_email = parent_2_email;
	}

	public String getParent_2_line() {
		return parent_2_line;
	}

	public void setParent_2_line(String parent_2_line) {
		this.parent_2_line = parent_2_line;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getSpecial_idn() {
		return special_idn;
	}

	public void setSpecial_idn(String special_idn) {
		this.special_idn = special_idn;
	}

	public String getExam() {
		return exam;
	}

	public void setExam(String exam) {
		this.exam = exam;
	}

	public String getAbroad_date() {
		return abroad_date;
	}

	public void setAbroad_date(String abroad_date) {
		this.abroad_date = abroad_date;
	}

	public String getBalanceTotal() {
		return balanceTotal;
	}

	public void setBalanceTotal(String balanceTotal) {
		this.balanceTotal = balanceTotal;
	}

	public String getMakeUpTotal() {
		return makeUpTotal;
	}

	public void setMakeUpTotal(String makeUpTotal) {
		this.makeUpTotal = makeUpTotal;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getSchool_code2() {
		return school_code2;
	}

	public void setSchool_code2(String school_code2) {
		this.school_code2 = school_code2;
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

	public String getCol_5() {
		return col_5;
	}

	public void setCol_5(String col_5) {
		this.col_5 = col_5;
	}

	public String getSitNo() {
		return sitNo;
	}

	public void setSitNo(String sitNo) {
		this.sitNo = sitNo;
	}

	public String getRemarkTotal() {
		return remarkTotal;
	}

	public void setRemarkTotal(String remarkTotal) {
		this.remarkTotal = remarkTotal;
	}

	public String getHandover() {
		return handover;
	}

	public void setHandover(String handover) {
		this.handover = handover;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getCompany_tel() {
		return company_tel;
	}

	public void setCompany_tel(String company_tel) {
		this.company_tel = company_tel;
	}

	public String getAgent_studentNo() {
		return agent_studentNo;
	}

	public void setAgent_studentNo(String agent_studentNo) {
		this.agent_studentNo = agent_studentNo;
	}

	public String getGrade_highSchool() {
		return grade_highSchool;
	}

	public void setGrade_highSchool(String grade_highSchool) {
		this.grade_highSchool = grade_highSchool;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getAgent_studentName() {
		return agent_studentName;
	}

	public void setAgent_studentName(String agent_studentName) {
		this.agent_studentName = agent_studentName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getCol_6() {
		return col_6;
	}

	public void setCol_6(String col_6) {
		this.col_6 = col_6;
	}

	public String getCol_7() {
		return col_7;
	}

	public void setCol_7(String col_7) {
		this.col_7 = col_7;
	}

	public String getCol_8() {
		return col_8;
	}

	public void setCol_8(String col_8) {
		this.col_8 = col_8;
	}

	public String getCol_9() {
		return col_9;
	}

	public void setCol_9(String col_9) {
		this.col_9 = col_9;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getCowork() {
		return cowork;
	}

	public void setCowork(String cowork) {
		this.cowork = cowork;
	}

	public String getUpdater2() {
		return updater2;
	}

	public void setUpdater2(String updater2) {
		this.updater2 = updater2;
	}

	public String getUpdate_time2() {
		return update_time2;
	}

	public void setUpdate_time2(String update_time2) {
		this.update_time2 = update_time2;
	}


}
