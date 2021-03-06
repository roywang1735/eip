package com.wordgod.eip.Model;

public class Books {

	private String books_seq;
    private String category_id;
    private String category_name;
    private String bookName;
    private String originPrice;
    private String sellPrice;
    private String isbn;
    private String publisher;
    private String active;
    private String updater;
    private String update_time;  
    private String payOffRelease;
  
	public Books(String books_seq,String category_id,String category_name,String bookName,String originPrice,String sellPrice,String isbn,String publisher,String active,String updater,String update_time,String payOffRelease) {
		this.books_seq = books_seq;
		this.category_id = category_id;
		this.category_name = category_name;
		this.bookName = bookName;
		this.originPrice = originPrice;
		this.sellPrice = sellPrice;
		this.isbn = isbn;
		this.publisher = publisher;
		this.active = active;
		this.updater = updater;
		this.update_time = update_time;
		this.payOffRelease = payOffRelease;
	}

	public Books() {
		// TODO Auto-generated constructor stub
	}

	public String getBooks_seq() {
		return books_seq;
	}

	public void setBooks_seq(String books_seq) {
		this.books_seq = books_seq;
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

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(String originPrice) {
		this.originPrice = originPrice;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
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

	public String getPayOffRelease() {
		return payOffRelease;
	}

	public void setPayOffRelease(String payOffRelease) {
		this.payOffRelease = payOffRelease;
	}
 
	

}
