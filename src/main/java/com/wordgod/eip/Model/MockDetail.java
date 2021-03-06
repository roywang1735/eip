package com.wordgod.eip.Model;

public class MockDetail {
	private String mockDetail_seq;
    private String mock_id;
    private String totalNo; 
    private String noName;
    private String testStyle;
    private String testStyleName;
    private String testMethod;
    private String testMethodName;
    private String category_id;
    private String category_name;
    
	public MockDetail(
		String mockDetail_seq,
		String mock_id,
		String totalNo,
		String noName,
		String testStyle,
		String testStyleName,
		String testMethod,
		String testMethodName,
		String category_id,
		String category_name
	){
		this.mockDetail_seq = mockDetail_seq;
		this.mock_id = mock_id;
		this.totalNo = totalNo;
		this.noName = noName;
		this.testStyle = testStyle;
		this.testStyleName = testStyleName;
		this.testMethod = testMethod;
		this.testMethodName = testMethodName;
		this.category_id = category_id;
		this.category_name = category_name;
	 }

	public String getMockDetail_seq() {
		return mockDetail_seq;
	}

	public void setMockDetail_seq(String mockDetail_seq) {
		this.mockDetail_seq = mockDetail_seq;
	}

	public String getMock_id() {
		return mock_id;
	}

	public void setMock_id(String mock_id) {
		this.mock_id = mock_id;
	}

	public String getTotalNo() {
		return totalNo;
	}

	public void setTotalNo(String totalNo) {
		this.totalNo = totalNo;
	}

	public String getNoName() {
		return noName;
	}

	public void setNoName(String noName) {
		this.noName = noName;
	}

	public String getTestStyle() {
		return testStyle;
	}

	public void setTestStyle(String testStyle) {
		this.testStyle = testStyle;
	}

	public String getTestMethod() {
		return testMethod;
	}

	public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}


	public String getTestStyleName() {
		return testStyleName;
	}

	public void setTestStyleName(String testStyleName) {
		this.testStyleName = testStyleName;
	}

	public String getTestMethodName() {
		return testMethodName;
	}

	public void setTestMethodName(String testMethodName) {
		this.testMethodName = testMethodName;
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

}
