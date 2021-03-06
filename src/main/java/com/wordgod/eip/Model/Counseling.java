package com.wordgod.eip.Model;

public class Counseling {
	private String counseling_seq;
    private String category_id;
    private String category_name;
    private String counseling_name;
    private String origin_price;
    private String active;
    
	public Counseling(String counseling_seq,String category_id,String category_name,String counseling_name,String origin_price,String active) {
		this.counseling_seq = counseling_seq;
		this.category_id = category_id;
		this.category_name = category_name;
		this.counseling_name = counseling_name;
		this.origin_price = origin_price;
		this.active = active;
	}

	public Counseling() {
		// TODO Auto-generated constructor stub
	}

	public String getCounseling_seq() {
		return counseling_seq;
	}

	public void setCounseling_seq(String counseling_seq) {
		this.counseling_seq = counseling_seq;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getCounseling_name() {
		return counseling_name;
	}

	public void setCounseling_name(String counseling_name) {
		this.counseling_name = counseling_name;
	}

	public String getOrigin_price() {
		return origin_price;
	}

	public void setOrigin_price(String origin_price) {
		this.origin_price = origin_price;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
	
}
