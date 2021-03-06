package com.wordgod.eip.Model;

public class SinCourseSale {
	private String singleCourseSale_seq;
    private String category_id;
    private String subject_id;
    private String origin_price;
    private String sale_price;
    private String hrPrice;
    private String counselingPrice;
    private String lagnappePrice;
    private String handoutPrice;
    private String homeworkPrice; 
    private String mockPrice; //該套裝內含課程
    private String coursePrice;
    private String update_time;
    private String FlowStatus_code;
    
	public SinCourseSale(String singleCourseSale_seq, String category_id, String subject_id, String origin_price, String sale_price,String hrPrice, String counselingPrice, String lagnappePrice, String handoutPrice, String homeworkPrice, String mockPrice, String coursePrice, String update_time, String FlowStatus_code) {
		this.singleCourseSale_seq = singleCourseSale_seq;
		this.category_id = category_id;
		this.subject_id = subject_id;
		this.origin_price = origin_price;
		this.sale_price = sale_price;
		this.hrPrice = hrPrice;
		this.counselingPrice = counselingPrice;
		this.lagnappePrice = lagnappePrice;
		this.handoutPrice = handoutPrice;
		this.homeworkPrice = homeworkPrice;
		this.mockPrice = mockPrice;
		this.coursePrice = coursePrice;
		this.update_time = update_time;
		this.FlowStatus_code = FlowStatus_code;
	}

	public String getSingleCourseSale_seq() {
		return singleCourseSale_seq;
	}

	public void setSingleCourseSale_seq(String singleCourseSale_seq) {
		this.singleCourseSale_seq = singleCourseSale_seq;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public String getOrigin_price() {
		return origin_price;
	}

	public void setOrigin_price(String origin_price) {
		this.origin_price = origin_price;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	public String getHrPrice() {
		return hrPrice;
	}

	public void setHrPrice(String hrPrice) {
		this.hrPrice = hrPrice;
	}

	public String getCounselingPrice() {
		return counselingPrice;
	}

	public void setCounselingPrice(String counselingPrice) {
		this.counselingPrice = counselingPrice;
	}

	public String getLagnappePrice() {
		return lagnappePrice;
	}

	public void setLagnappePrice(String lagnappePrice) {
		this.lagnappePrice = lagnappePrice;
	}

	public String getHandoutPrice() {
		return handoutPrice;
	}

	public void setHandoutPrice(String handoutPrice) {
		this.handoutPrice = handoutPrice;
	}

	public String getHomeworkPrice() {
		return homeworkPrice;
	}

	public void setHomeworkPrice(String homeworkPrice) {
		this.homeworkPrice = homeworkPrice;
	}

	public String getMockPrice() {
		return mockPrice;
	}

	public void setMockPrice(String mockPrice) {
		this.mockPrice = mockPrice;
	}

	public String getCoursePrice() {
		return coursePrice;
	}

	public void setCoursePrice(String coursePrice) {
		this.coursePrice = coursePrice;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getFlowStatus_code() {
		return FlowStatus_code;
	}

	public void setFlowStatus_code(String flowStatus_code) {
		FlowStatus_code = flowStatus_code;
	}


}
