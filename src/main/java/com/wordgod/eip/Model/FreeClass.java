package com.wordgod.eip.Model;

public class FreeClass {

	private String freeClass_seq;
    private String freeChoice;
    private String Register_comboSale_id;

  
	public FreeClass(String freeClass_seq,String freeChoice,String Register_comboSale_id) {
		this.freeClass_seq = freeClass_seq;
		this.freeChoice = freeChoice;
		this.Register_comboSale_id = Register_comboSale_id;
	}


	public String getFreeClass_seq() {
		return freeClass_seq;
	}


	public void setFreeClass_seq(String freeClass_seq) {
		this.freeClass_seq = freeClass_seq;
	}


	public String getFreeChoice() {
		return freeChoice;
	}


	public void setFreeChoice(String freeChoice) {
		this.freeChoice = freeChoice;
	}


	public String getRegister_comboSale_id() {
		return Register_comboSale_id;
	}


	public void setRegister_comboSale_id(String register_comboSale_id) {
		Register_comboSale_id = register_comboSale_id;
	}


}
