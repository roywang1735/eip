package com.wordgod.eip.Model;

public class Material {
	private String material_seq;
    private String category_id;
    private String category_name;
    private String material_name;
    private String origin_price;
    private String sale_price;
    private String remark;
    private String update_time;
    
	public Material(String material_seq, String category_id, String category_name, String material_name, String origin_price, String sale_price, String remark, String update_time) {
		this.material_seq = material_seq;
		this.category_id = category_id;
		this.category_name = category_name;
		this.material_name = material_name;
		this.origin_price = origin_price;
		this.sale_price = sale_price;
		this.remark = remark;
		this.update_time = update_time;
	}

	public String getMaterial_seq(){
		return material_seq;
	}

	public void setMaterial_seq(String material_seq) {
		this.material_seq = material_seq;
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

	public String getMaterial_name() {
		return material_name;
	}

	public void setMaterial_name(String material_name) {
		this.material_name = material_name;
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

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}	
}
