package com.wordgod.eip.Model;

public class OnlineClass {

	private String onlineClasses_seq;
    private String class_id;
    private String link;

   
  
	public OnlineClass(String onlineClasses_seq,String class_id,String link) {
		this.onlineClasses_seq = onlineClasses_seq;
		this.class_id = class_id;
		this.link = link;
	}



	public String getOnlineClasses_seq() {
		return onlineClasses_seq;
	}



	public void setOnlineClasses_seq(String onlineClasses_seq) {
		this.onlineClasses_seq = onlineClasses_seq;
	}



	public String getClass_id() {
		return class_id;
	}



	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}



	public String getLink() {
		return link;
	}



	public void setLink(String link) {
		this.link = link;
	}


}
