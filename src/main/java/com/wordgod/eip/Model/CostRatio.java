package com.wordgod.eip.Model;

public class CostRatio {
	private String costRatio_seq;
    private String category_id;
    private String category_name;
    private String hrPrice_R;
    private String counselingPrice_R;
    private String lagnappePrice_R;
    private String handoutPrice_R;
    private String homeworkPrice_R;
    private String mockPrice_R;
    private String update_time; 
    
	public CostRatio(String costRatio_seq, String category_id, String category_name, String hrPrice_R, String counselingPrice_R, String lagnappePrice_R, String handoutPrice_R, String homeworkPrice_R, String mockPrice_R, String update_time) {
		this.costRatio_seq = costRatio_seq;
		this.category_id = category_id;
		this.category_name = category_name;
		this.hrPrice_R = hrPrice_R;
		this.counselingPrice_R = counselingPrice_R;
		this.lagnappePrice_R = lagnappePrice_R;
		this.handoutPrice_R = handoutPrice_R;
		this.homeworkPrice_R = homeworkPrice_R;
		this.mockPrice_R = mockPrice_R;
		this.update_time = update_time;
	}

	public String getCostRatio_seq() {
		return costRatio_seq;
	}

	public void setCostRatio_seq(String costRatio_seq) {
		this.costRatio_seq = costRatio_seq;
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

	public String getHrPrice_R() {
		return hrPrice_R;
	}

	public void setHrPrice_R(String hrPrice_R) {
		this.hrPrice_R = hrPrice_R;
	}

	public String getCounselingPrice_R() {
		return counselingPrice_R;
	}

	public void setCounselingPrice_R(String counselingPrice_R) {
		this.counselingPrice_R = counselingPrice_R;
	}

	public String getLagnappePrice_R() {
		return lagnappePrice_R;
	}

	public void setLagnappePrice_R(String lagnappePrice_R) {
		this.lagnappePrice_R = lagnappePrice_R;
	}

	public String getHandoutPrice_R() {
		return handoutPrice_R;
	}

	public void setHandoutPrice_R(String handoutPrice_R) {
		this.handoutPrice_R = handoutPrice_R;
	}

	public String getHomeworkPrice_R() {
		return homeworkPrice_R;
	}

	public void setHomeworkPrice_R(String homeworkPrice_R) {
		this.homeworkPrice_R = homeworkPrice_R;
	}

	public String getMockPrice_R() {
		return mockPrice_R;
	}

	public void setMockPrice_R(String mockPrice_R) {
		this.mockPrice_R = mockPrice_R;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
}
