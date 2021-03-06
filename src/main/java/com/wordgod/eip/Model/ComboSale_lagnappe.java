package com.wordgod.eip.Model;

public class ComboSale_lagnappe {
	private String lagnappe_id;
    private String lagnappe_name;
    private String lagnappe_no;
    
	public ComboSale_lagnappe(String lagnappe_id,String lagnappe_name,String lagnappe_no) {
		this.lagnappe_id = lagnappe_id;
		this.lagnappe_name = lagnappe_name;
		this.lagnappe_no = lagnappe_no;
	}

	public String getLagnappe_id() {
		return lagnappe_id;
	}

	public void setLagnappe_id(String lagnappe_id) {
		this.lagnappe_id = lagnappe_id;
	}

	public String getLagnappe_name() {
		return lagnappe_name;
	}

	public void setLagnappe_name(String lagnappe_name) {
		this.lagnappe_name = lagnappe_name;
	}

	public String getLagnappe_no() {
		return lagnappe_no;
	}

	public void setLagnappe_no(String lagnappe_no) {
		this.lagnappe_no = lagnappe_no;
	}
	
}
