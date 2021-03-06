package com.wordgod.eip.Model;

public class Register_counseling {
	private String register_counseling_seq;
	private String comboSale_id;
	private String Register_seq;
	private String counseling_id;
	private String counseling_name;
	private String category_id;

	
	public Register_counseling(
			String register_counseling_seq,
			String comboSale_id,
			String Register_seq,
			String counseling_id,
			String counseling_name,
			String category_id
	) {
		this.register_counseling_seq = register_counseling_seq;
		this.comboSale_id = comboSale_id;
		this.Register_seq = Register_seq;
		this.counseling_id = counseling_id;
		this.counseling_name = counseling_name;
		this.category_id = category_id;
	}


	public String getRegister_counseling_seq() {
		return register_counseling_seq;
	}


	public void setRegister_counseling_seq(String register_counseling_seq) {
		this.register_counseling_seq = register_counseling_seq;
	}


	public String getComboSale_id() {
		return comboSale_id;
	}


	public void setComboSale_id(String comboSale_id) {
		this.comboSale_id = comboSale_id;
	}


	public String getRegister_seq() {
		return Register_seq;
	}


	public void setRegister_seq(String register_seq) {
		Register_seq = register_seq;
	}


	public String getCounseling_id() {
		return counseling_id;
	}


	public void setCounseling_id(String counseling_id) {
		this.counseling_id = counseling_id;
	}


	public String getCounseling_name() {
		return counseling_name;
	}


	public void setCounseling_name(String counseling_name) {
		this.counseling_name = counseling_name;
	}


	public String getCategory_id() {
		return category_id;
	}


	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

}
