package com.wordgod.eip.Model;

public class ClassesMaterial {

	private String classesMaterial_seq;
    private String class_id;
    private String material_id;
    private String book_id;
    private String bookName;
    private String inputTex;
  
	public ClassesMaterial(String classesMaterial_seq,String class_id,String material_id,String book_id,String bookName,String inputTex) {
		this.classesMaterial_seq = classesMaterial_seq;
		this.class_id = class_id;
		this.material_id = material_id;
		this.book_id = book_id;
		this.bookName = bookName;
		this.inputTex = inputTex;
	}

	public String getClassesMaterial_seq() {
		return classesMaterial_seq;
	}

	public void setClassesMaterial_seq(String classesMaterial_seq) {
		this.classesMaterial_seq = classesMaterial_seq;
	}

	public String getClass_id() {
		return class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public String getMaterial_id() {
		return material_id;
	}

	public void setMaterial_id(String material_id) {
		this.material_id = material_id;
	}

	public String getBook_id() {
		return book_id;
	}

	public void setBook_id(String book_id) {
		this.book_id = book_id;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getInputTex() {
		return inputTex;
	}

	public void setInputTex(String inputTex) {
		this.inputTex = inputTex;
	}

}
