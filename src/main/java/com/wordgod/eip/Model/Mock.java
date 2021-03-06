package com.wordgod.eip.Model;

public class Mock {
	private String mock_seq;
    private String category_id;
    private String category_name;
    private String mock_name;
    private String active;
    private String original_price;
    
	public Mock(String mock_seq,String category_id,String category_name,String mock_name,String active,String original_price) {
		this.mock_seq = mock_seq;
		this.category_id = category_id;
		this.category_name = category_name;
		this.mock_name = mock_name;
		this.active = active;
		this.original_price = original_price;
	}

	public String getMock_seq() {
		return mock_seq;
	}

	public void setMock_seq(String mock_seq) {
		this.mock_seq = mock_seq;
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

	public String getMock_name() {
		return mock_name;
	}

	public void setMock_name(String mock_name) {
		this.mock_name = mock_name;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(String original_price) {
		this.original_price = original_price;
	}
	
}
