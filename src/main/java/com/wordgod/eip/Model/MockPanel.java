package com.wordgod.eip.Model;

public class MockPanel {
	private String mockPanel_seq;
	private String category_id;
    private String mockBaseTitle_id;
    private String panelName;
    
	public MockPanel(String mockPanel_seq,String category_id,String mockBaseTitle_id,String panelName) {
		this.mockPanel_seq = mockPanel_seq;
		this.category_id = category_id;
		this.mockBaseTitle_id = mockBaseTitle_id;
		this.panelName = panelName;
	}

	public String getMockPanel_seq() {
		return mockPanel_seq;
	}

	public void setMockPanel_seq(String mockPanel_seq) {
		this.mockPanel_seq = mockPanel_seq;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getMockBaseTitle_id() {
		return mockBaseTitle_id;
	}

	public void setMockBaseTitle_id(String mockBaseTitle_id) {
		this.mockBaseTitle_id = mockBaseTitle_id;
	}

	public String getPanelName() {
		return panelName;
	}

	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}
	
}
