package com.wordgod.eip.Model;

public class Register_mock {
	private String register_mock_seq;
	private String comboSale_id;
	private String Register_seq;
	private String mock_id;
	private String mock_name;
	private String active;
	
	public Register_mock(String register_mock_seq,String comboSale_id,String Register_seq,String mock_id,String mock_name,String active) {
		this.register_mock_seq = register_mock_seq;
		this.comboSale_id = comboSale_id;
		this.Register_seq = Register_seq;
		this.mock_id = mock_id;
		this.mock_name = mock_name;
		this.active = active;
	}

	public String getRegister_mock_seq() {
		return register_mock_seq;
	}

	public void setRegister_mock_seq(String register_mock_seq) {
		this.register_mock_seq = register_mock_seq;
	}

	public String getComboSale_id() {
		return comboSale_id;
	}

	public void setComboSale_id(String comboSale_id) {
		this.comboSale_id = comboSale_id;
	}

	public String getRegister_seq() {
		return Register_seq;
	}

	public void setRegister_seq(String register_seq) {
		Register_seq = register_seq;
	}

	public String getMock_id() {
		return mock_id;
	}

	public void setMock_id(String mock_id) {
		this.mock_id = mock_id;
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

}
