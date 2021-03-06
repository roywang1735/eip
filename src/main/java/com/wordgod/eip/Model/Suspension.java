package com.wordgod.eip.Model;

public class Suspension {
	private String suspension_seq;
	private String suspension_date;
	private String reason;
	private String update_time;
	private String updater;

	public Suspension(String suspension_seq,String suspension_date,String reason,String update_time,String updater) {
		this.suspension_seq = suspension_seq;
		this.suspension_date = suspension_date;
		this.reason = reason;
		this.update_time = update_time;
		this.updater = updater;
	}

	public String getSuspension_seq() {
		return suspension_seq;
	}

	public void setSuspension_seq(String suspension_seq) {
		this.suspension_seq = suspension_seq;
	}

	public String getSuspension_date() {
		return suspension_date;
	}

	public void setSuspension_date(String suspension_date) {
		this.suspension_date = suspension_date;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

}
