package com.wordgod.eip.Model;

public class InterestSubject {
	private String interestSubject_seq;
    private String Register_comboSale_id;
    private String child_subject_id;

    
	public InterestSubject(String interestSubject_seq, String Register_comboSale_id, String child_subject_id) {
		this.interestSubject_seq = interestSubject_seq;
		this.Register_comboSale_id = Register_comboSale_id;
		this.child_subject_id = child_subject_id;
	}


	public String getInterestSubject_seq() {
		return interestSubject_seq;
	}


	public void setInterestSubject_seq(String interestSubject_seq) {
		this.interestSubject_seq = interestSubject_seq;
	}


	public String getRegister_comboSale_id() {
		return Register_comboSale_id;
	}


	public void setRegister_comboSale_id(String register_comboSale_id) {
		Register_comboSale_id = register_comboSale_id;
	}


	public String getChild_subject_id() {
		return child_subject_id;
	}


	public void setChild_subject_id(String child_subject_id) {
		this.child_subject_id = child_subject_id;
	}

}
