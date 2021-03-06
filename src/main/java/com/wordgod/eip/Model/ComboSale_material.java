package com.wordgod.eip.Model;

public class ComboSale_material {
	private String material_id;
    private String material_name;
    private String origin_price;
    private String sale_price;
    
	public ComboSale_material(String material_id, String material_name, String origin_price, String sale_price) {
		this.material_id = material_id;
		this.material_name = material_name;
		this.origin_price = origin_price;
		this.sale_price = sale_price;
	}

	public ComboSale_material() {
		// TODO Auto-generated constructor stub
	}

	public String getMaterial_id() {
		return material_id;
	}

	public void setMaterial_id(String material_id) {
		this.material_id = material_id;
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

}
