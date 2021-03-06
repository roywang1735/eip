package com.wordgod.eip.Model;

public class StudentGradeShare {
	private String student_seq;
	private String student_no;
	private String costShare;
	private String ch_name;
	private String hr_share;
	private String counseling_share;
	private String lagnappe_share;
	private String handout_share;
	private String homework_share;
	private String mock_share;
	private String grade_share;
	private String class_style;
	private String school_code;
	private String school_name;
	private String register_status;
	
    
    public StudentGradeShare(
    		String student_seq,
    		String student_no,
    		String costShare,
    		String ch_name,
    		String hr_share,
    		String counseling_share,
    		String lagnappe_share,
    		String handout_share,
    		String homework_share,
    		String mock_share,
    		String grade_share, //課程收入
    		String class_style,
    		String school_code,
    		String school_name,
    		String register_status
    	) {
        this.student_seq = student_seq;
        this.student_no = student_no;
        this.costShare = costShare;
        this.ch_name = ch_name;
        this.hr_share = hr_share;
        this.counseling_share = counseling_share;
        this.lagnappe_share = lagnappe_share;
        this.handout_share = handout_share;
        this.homework_share = homework_share;
        this.mock_share = mock_share;
        this.grade_share = grade_share;
        this.class_style = class_style;
        this.school_code = school_code;
        this.school_name = school_name;
        this.register_status = register_status;
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


	public String getCostShare() {
		return costShare;
	}

	public void setCostShare(String costShare) {
		this.costShare = costShare;
	}


	public String getCh_name() {
		return ch_name;
	}

	public void setCh_name(String ch_name) {
		this.ch_name = ch_name;
	}

	public String getHr_share() {
		return hr_share;
	}

	public void setHr_share(String hr_share) {
		this.hr_share = hr_share;
	}

	public String getCounseling_share() {
		return counseling_share;
	}

	public void setCounseling_share(String counseling_share) {
		this.counseling_share = counseling_share;
	}

	public String getLagnappe_share() {
		return lagnappe_share;
	}

	public void setLagnappe_share(String lagnappe_share) {
		this.lagnappe_share = lagnappe_share;
	}

	public String getHandout_share() {
		return handout_share;
	}

	public void setHandout_share(String handout_share) {
		this.handout_share = handout_share;
	}

	public String getHomework_share() {
		return homework_share;
	}

	public void setHomework_share(String homework_share) {
		this.homework_share = homework_share;
	}

	public String getMock_share() {
		return mock_share;
	}

	public void setMock_share(String mock_share) {
		this.mock_share = mock_share;
	}

	public String getGrade_share() {
		return grade_share;
	}

	public void setGrade_share(String grade_share) {
		this.grade_share = grade_share;
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

	public String getRegister_status() {
		return register_status;
	}

	public void setRegister_status(String register_status) {
		this.register_status = register_status;
	}

}
