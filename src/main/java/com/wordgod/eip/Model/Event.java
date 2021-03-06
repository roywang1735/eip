package com.wordgod.eip.Model;

public class Event {

	private String title;
    private String start;
    private String end;
    private String description;
    private String description0;
    private String backgroundColor="white" ;
    private String borderColor="ffeeff";
  

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }
    public String getEnd() {
        return end;
    }
    public void setEnd(String end) {
        this.end = end;
    }
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
	public String getDescription0() {
		return description0;
	}
	public void setDescription0(String description0) {
		this.description0 = description0;
	}


}
