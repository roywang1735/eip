package com.wordgod.eip.Model;

public class StudentOrderStatus {
	private String orderStatus;
    private String allow_attend;

    
	public StudentOrderStatus(String orderStatus,String allow_attend) {
		this.orderStatus = orderStatus;
		this.allow_attend = allow_attend;
	}


	public String getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public String getAllow_attend() {
		return allow_attend;
	}


	public void setAllow_attend(String allow_attend) {
		this.allow_attend = allow_attend;
	}

}
