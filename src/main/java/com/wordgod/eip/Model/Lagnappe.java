package com.wordgod.eip.Model;

public class Lagnappe {
	private String lagnappe_seq;
    private String lagnappe_name;
    private String origin_price;
    private String sale_price;
    private String payOffRelease;
    private String active;
    private String del_item;
    private String updater;
    private String update_time;
    
    
    
	public Lagnappe(String lagnappe_seq,String lagnappe_name,String origin_price,String sale_price,String payOffRelease,String active,String del_item,String updater,String update_time) {
		this.lagnappe_seq = lagnappe_seq;
		this.lagnappe_name = lagnappe_name;
		this.origin_price = origin_price;
		this.sale_price = sale_price;
		this.payOffRelease = payOffRelease;
		this.active = active;
		this.del_item = del_item;
		this.updater = updater;
		this.update_time = update_time;
	}

	public Lagnappe() {
		// TODO Auto-generated constructor stub
	}

	public String getLagnappe_seq() {
		return lagnappe_seq;
	}

	public void setLagnappe_seq(String lagnappe_seq) {
		this.lagnappe_seq = lagnappe_seq;
	}

	public String getLagnappe_name() {
		return lagnappe_name;
	}

	public void setLagnappe_name(String lagnappe_name) {
		this.lagnappe_name = lagnappe_name;
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

	public String getPayOffRelease() {
		return payOffRelease;
	}

	public void setPayOffRelease(String payOffRelease) {
		this.payOffRelease = payOffRelease;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getDel_item() {
		return del_item;
	}

	public void setDel_item(String del_item) {
		this.del_item = del_item;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
}
