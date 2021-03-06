package com.wordgod.eip.Model;

public class Suspension_allow {
	private String suspension_date;
	private String allow_slot;


	public Suspension_allow(String suspension_date,String allow_slot) {
		this.suspension_date = suspension_date;
		this.allow_slot = allow_slot;
	}


	public String getSuspension_date() {
		return suspension_date;
	}


	public void setSuspension_date(String suspension_date) {
		this.suspension_date = suspension_date;
	}


	public String getAllow_slot() {
		return allow_slot;
	}


	public void setAllow_slot(String allow_slot) {
		this.allow_slot = allow_slot;
	}


}
