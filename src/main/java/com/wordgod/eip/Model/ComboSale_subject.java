package com.wordgod.eip.Model;

public class ComboSale_subject {
	private String comboSale_subject_seq;
	private String comboSale_id;
	private String subject_id;
    private String subject_name;
    private String gradeStr;
    private String subject_price;
    private String outPublisher_price;
    private String CounselingStr;//此單科所帶之充電站
    private String LagnappeStr;//此單科所帶之贈品
    private String MockStr;//此單科所帶之模考
    private String OutPublisherStr; //此單科所帶之外版書
    private String hrPrice_R;
    private String counselingPrice_R;
    private String lagnappePrice_R;
    private String handoutPrice_R;
    private String homeworkPrice_R;
    private String mockPrice_R;
    
	public ComboSale_subject(String comboSale_subject_seq,String comboSale_id,String subject_id,String subject_name,String gradeStr,String subject_price,String outPublisher_price,String CounselingStr,String LagnappeStr,String MockStr,String OutPublisherStr,String hrPrice_R,String counselingPrice_R,String lagnappePrice_R,String handoutPrice_R,String homeworkPrice_R,String mockPrice_R) {
		this.comboSale_subject_seq = comboSale_subject_seq;
		this.comboSale_id = comboSale_id;
		this.subject_id = subject_id;
		this.subject_name = subject_name;
		this.gradeStr = gradeStr;
		this.subject_price = subject_price;
		this.outPublisher_price = outPublisher_price;
		this.CounselingStr = CounselingStr;
		this.LagnappeStr = LagnappeStr;
		this.MockStr = MockStr;
		this.OutPublisherStr = OutPublisherStr;
		this.hrPrice_R = hrPrice_R;
		this.counselingPrice_R = counselingPrice_R;
		this.lagnappePrice_R = lagnappePrice_R;
		this.handoutPrice_R = handoutPrice_R;
		this.homeworkPrice_R = homeworkPrice_R;
		this.mockPrice_R = mockPrice_R;
	}

	public String getComboSale_id() {
		return comboSale_id;
	}

	public void setComboSale_id(String comboSale_id) {
		this.comboSale_id = comboSale_id;
	}

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public String getGradeStr() {
		return gradeStr;
	}

	public void setGradeStr(String gradeStr) {
		this.gradeStr = gradeStr;
	}

	public String getSubject_price() {
		return subject_price;
	}

	public void setSubject_price(String subject_price) {
		this.subject_price = subject_price;
	}

	public String getOutPublisher_price() {
		return outPublisher_price;
	}

	public void setOutPublisher_price(String outPublisher_price) {
		this.outPublisher_price = outPublisher_price;
	}

	public String getCounselingStr() {
		return CounselingStr;
	}

	public void setCounselingStr(String counselingStr) {
		CounselingStr = counselingStr;
	}

	public String getLagnappeStr() {
		return LagnappeStr;
	}

	public void setLagnappeStr(String lagnappeStr) {
		LagnappeStr = lagnappeStr;
	}

	public String getMockStr() {
		return MockStr;
	}

	public void setMockStr(String mockStr) {
		MockStr = mockStr;
	}

	public String getOutPublisherStr() {
		return OutPublisherStr;
	}

	public void setOutPublisherStr(String outPublisherStr) {
		OutPublisherStr = outPublisherStr;
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

	public String getComboSale_subject_seq() {
		return comboSale_subject_seq;
	}

	public void setComboSale_subject_seq(String comboSale_subject_seq) {
		this.comboSale_subject_seq = comboSale_subject_seq;
	}
}
