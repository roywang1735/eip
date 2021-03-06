package com.wordgod.eip.Model;

public class ComboSale_counseling {
	private String counseling_id;
    private String counseling_name;
    
	public ComboSale_counseling(String counseling_id, String counseling_name) {
		this.counseling_id = counseling_id;
		this.counseling_name = counseling_name;
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
}
