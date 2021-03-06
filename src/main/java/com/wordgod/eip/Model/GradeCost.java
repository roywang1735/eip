package com.wordgod.eip.Model;

public class GradeCost {
	private String costShare; 
    private String freeChoice; 
    private String subject_price;
    private String class_style;
    
	public GradeCost(String costShare,String freeChoice,String subject_price,String class_style) {
		this.costShare = costShare;
		this.freeChoice = freeChoice;
		this.subject_price = subject_price;
		this.class_style = class_style;
	}

	public String getCostShare() {
		return costShare;
	}

	public void setCostShare(String costShare) {
		this.costShare = costShare;
	}

	public String getFreeChoice() {
		return freeChoice;
	}

	public void setFreeChoice(String freeChoice) {
		this.freeChoice = freeChoice;
	}

	public String getSubject_price() {
		return subject_price;
	}

	public void setSubject_price(String subject_price) {
		this.subject_price = subject_price;
	}

	public String getClass_style() {
		return class_style;
	}

	public void setClass_style(String class_style) {
		this.class_style = class_style;
	}

}
