package com.wordgod.eip.Model;

public class ComboSale {
	private String comboSale_seq;
    private String isCombo;
    private String category_id;
    private String category_name;
    private String flowStatus_code;
    private String flowStatus_name;
    private String name;
    private String origin_price;
    private String sale_price;
    private String remark;
    private String creater;
    private String update_time; 
    private String subjectStr; //該套裝內含課程
    private String class_makeup;
    //private String class_style;
    private String schedule_time;
    private String originSubjectName;
    private String favor_id;
    private String disable;
    
	public ComboSale(String comboSale_seq,String isCombo,String category_id,String category_name,String flowStatus_code,String flowStatus_name,String name,String origin_price,String sale_price,String remark,String creater,String update_time,String subjectStr,String class_makeup,String schedule_time,String originSubjectName,String favor_id,String disable) {
		this.comboSale_seq = comboSale_seq;
		this.isCombo = isCombo;
		this.category_id = category_id;
		this.category_name = category_name;
		this.flowStatus_code = flowStatus_code;
		this.flowStatus_name = flowStatus_name;
		this.name = name;
		this.origin_price = origin_price;
		this.sale_price = sale_price;
		this.remark = remark;
		this.creater = creater;
		this.update_time = update_time;
		this.subjectStr = subjectStr;
		this.class_makeup = class_makeup;
		this.schedule_time = schedule_time;
		this.originSubjectName = originSubjectName;
		this.favor_id = favor_id;
		this.disable = disable;
	}

	public ComboSale() {
		// TODO Auto-generated constructor stub
	}

	public String getComboSale_seq() {
		return comboSale_seq;
	}

	public void setComboSale_seq(String comboSale_seq) {
		this.comboSale_seq = comboSale_seq;
	}

	public String getIsCombo() {
		return isCombo;
	}

	public void setIsCombo(String isCombo) {
		this.isCombo = isCombo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getSubjectStr() {
		return subjectStr;
	}

	public void setSubjectStr(String subjectStr) {
		this.subjectStr = subjectStr;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getFlowStatus_code() {
		return flowStatus_code;
	}

	public void setFlowStatus_code(String flowStatus_code) {
		this.flowStatus_code = flowStatus_code;
	}

	public String getFlowStatus_name() {
		return flowStatus_name;
	}

	public void setFlowStatus_name(String flowStatus_name) {
		this.flowStatus_name = flowStatus_name;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getClass_makeup() {
		return class_makeup;
	}

	public void setClass_makeup(String class_makeup) {
		this.class_makeup = class_makeup;
	}

	public String getSchedule_time() {
		return schedule_time;
	}

	public void setSchedule_time(String schedule_time) {
		this.schedule_time = schedule_time;
	}

	public String getOriginSubjectName() {
		return originSubjectName;
	}

	public void setOriginSubjectName(String originSubjectName) {
		this.originSubjectName = originSubjectName;
	}

	public String getFavor_id() {
		return favor_id;
	}

	public void setFavor_id(String favor_id) {
		this.favor_id = favor_id;
	}

	public String getDisable() {
		return disable;
	}

	public void setDisable(String disable) {
		this.disable = disable;
	}
	
}
