package com.wordgod.eip.Model;

public class ComboSale_outPublisher {
	private String comboSale_outPublisher_seq;
    private String comboSale_id;
    private String book_id;
    private String book_name;
    private String sellPrice;
    
	public ComboSale_outPublisher(String comboSale_outPublisher_seq,String comboSale_id,String book_id,String book_name,String sellPrice) {
		this.comboSale_outPublisher_seq = comboSale_outPublisher_seq;
		this.comboSale_id = comboSale_id;
		this.book_id = book_id;
		this.book_name = book_name;
		this.sellPrice = sellPrice;
	}

	public String getComboSale_outPublisher_seq() {
		return comboSale_outPublisher_seq;
	}

	public void setComboSale_outPublisher_seq(String comboSale_outPublisher_seq) {
		this.comboSale_outPublisher_seq = comboSale_outPublisher_seq;
	}

	public String getComboSale_id() {
		return comboSale_id;
	}

	public void setComboSale_id(String comboSale_id) {
		this.comboSale_id = comboSale_id;
	}

	public String getBook_id() {
		return book_id;
	}

	public void setBook_id(String book_id) {
		this.book_id = book_id;
	}

	public String getBook_name() {
		return book_name;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

}
