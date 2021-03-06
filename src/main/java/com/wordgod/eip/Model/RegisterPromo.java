package com.wordgod.eip.Model;

public class RegisterPromo {
	private String registerPromo_seq;
    private String register_id;
    private String classPromotion_id;
    private String classPromotion_name;
    
	public RegisterPromo(String registerPromo_seq,String register_id,String classPromotion_id,String classPromotion_name) {
		this.registerPromo_seq = registerPromo_seq;
		this.register_id = register_id;
		this.classPromotion_id = classPromotion_id;
		this.classPromotion_name = classPromotion_name;
	}

	public String getRegisterPromo_seq() {
		return registerPromo_seq;
	}

	public void setRegisterPromo_seq(String registerPromo_seq) {
		this.registerPromo_seq = registerPromo_seq;
	}

	public String getRegister_id() {
		return register_id;
	}

	public void setRegister_id(String register_id) {
		this.register_id = register_id;
	}

	public String getClassPromotion_id() {
		return classPromotion_id;
	}

	public void setClassPromotion_id(String classPromotion_id) {
		this.classPromotion_id = classPromotion_id;
	}

	public String getClassPromotion_name() {
		return classPromotion_name;
	}

	public void setClassPromotion_name(String classPromotion_name) {
		this.classPromotion_name = classPromotion_name;
	}

}
