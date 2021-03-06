package com.wordgod.eip.Model;

public class Category {
	private String category_seq;
    private String name;
    private boolean enabled;
    private String bgColor;
    private String ranks;

    
	public Category(String category_seq,String name,boolean enabled,String bgColor,String ranks) {
		this.category_seq = category_seq;
		this.name = name;
		this.enabled = enabled;
		this.bgColor = bgColor;
		this.ranks = ranks;
	}

	public String getCategory_seq() {
		return category_seq;
	}
	public void setCategory_seq(String category_seq) {
		this.category_seq = category_seq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getRanks() {
		return ranks;
	}

	public void setRanks(String ranks) {
		this.ranks = ranks;
	}


}
