package com.wordgod.eip.Model;

public class LectureFlow {

	private String lectureFlow_seq;
    private String lecture_id;
    private String beclass;
    private String beclass_edit;
    private String beclass_time;
    private String art;
    private String art_edit;
    private String art_time;        
    private String website;
    private String website_edit;
    private String website_time;    
    private String comment;
    private String beclass_comment;
    private String art_comment;
    private String website_comment;    
  

	public LectureFlow(
			String lectureFlow_seq,
			String lecture_id,
			String beclass,
			String beclass_edit,
			String beclass_time,
			String art,
			String art_edit,
			String art_time,
			String website,
			String website_edit,
			String website_time,
			String comment,
		    String beclass_comment,
		    String art_comment,
		    String website_comment  			
		){
			this.lectureFlow_seq = lectureFlow_seq;
			this.lecture_id = lecture_id;
			this.beclass = beclass;
			this.beclass_edit = beclass_edit;
			this.beclass_time = beclass_time;
			this.art = art;
			this.art_edit = art_edit;
			this.art_time = art_time;
			this.website = website;
			this.website_edit = website_edit;
			this.website_time = website_time;
			this.comment = comment;
			this.beclass_comment = beclass_comment;
			this.art_comment = art_comment;
			this.website_comment = website_comment;
	}


	public LectureFlow() {
		// TODO Auto-generated constructor stub
	}


	public String getLectureFlow_seq() {
		return lectureFlow_seq;
	}


	public void setLectureFlow_seq(String lectureFlow_seq) {
		this.lectureFlow_seq = lectureFlow_seq;
	}


	public String getLecture_id() {
		return lecture_id;
	}


	public void setLecture_id(String lecture_id) {
		this.lecture_id = lecture_id;
	}


	public String getBeclass() {
		return beclass;
	}


	public void setBeclass(String beclass) {
		this.beclass = beclass;
	}


	public String getBeclass_edit() {
		return beclass_edit;
	}


	public void setBeclass_edit(String beclass_edit) {
		this.beclass_edit = beclass_edit;
	}


	public String getBeclass_time() {
		return beclass_time;
	}


	public void setBeclass_time(String beclass_time) {
		this.beclass_time = beclass_time;
	}


	public String getArt() {
		return art;
	}


	public void setArt(String art) {
		this.art = art;
	}


	public String getArt_edit() {
		return art_edit;
	}


	public void setArt_edit(String art_edit) {
		this.art_edit = art_edit;
	}


	public String getArt_time() {
		return art_time;
	}


	public void setArt_time(String art_time) {
		this.art_time = art_time;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}


	public String getWebsite_edit() {
		return website_edit;
	}


	public void setWebsite_edit(String website_edit) {
		this.website_edit = website_edit;
	}


	public String getWebsite_time() {
		return website_time;
	}


	public void setWebsite_time(String website_time) {
		this.website_time = website_time;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getBeclass_comment() {
		return beclass_comment;
	}


	public void setBeclass_comment(String beclass_comment) {
		this.beclass_comment = beclass_comment;
	}


	public String getArt_comment() {
		return art_comment;
	}


	public void setArt_comment(String art_comment) {
		this.art_comment = art_comment;
	}


	public String getWebsite_comment() {
		return website_comment;
	}


	public void setWebsite_comment(String website_comment) {
		this.website_comment = website_comment;
	}


}
