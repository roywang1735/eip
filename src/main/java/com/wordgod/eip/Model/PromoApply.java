package com.wordgod.eip.Model;

public class PromoApply {
	private String promoApply_seq;
	private String classPromotion_id;
    private String school_code;
    private String school_name;
    
	public PromoApply(
			String promoApply_seq,
			String classPromotion_id,
			String school_code,
			String school_name
		){
		this.promoApply_seq = promoApply_seq;
		this.classPromotion_id = classPromotion_id; 
		this.school_code = school_code;
		this.school_name = school_name;
	}

	public PromoApply() {
		// TODO Auto-generated constructor stub
	}

	public String getPromoApply_seq() {
		return promoApply_seq;
	}

	public void setPromoApply_seq(String promoApply_seq) {
		this.promoApply_seq = promoApply_seq;
	}

	public String getClassPromotion_id() {
		return classPromotion_id;
	}

	public void setClassPromotion_id(String classPromotion_id) {
		this.classPromotion_id = classPromotion_id;
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
	
}
