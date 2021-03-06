package com.wordgod.eip.Model;

public class AdmRegularJob {
	private String admRegularJob_seq;
    private String school_code;
    private String jobContent;
    private String editerId;
    private String editerTime;
    
    public AdmRegularJob(String admRegularJob_seq,String school_code,String jobContent,String editerId,String editerTime) {
        this.admRegularJob_seq = admRegularJob_seq;
        this.school_code = school_code;
        this.jobContent = jobContent;
        this.editerId = editerId;
        this.editerTime = editerTime;
    }

	public String getAdmRegularJob_seq() {
		return admRegularJob_seq;
	}

	public void setAdmRegularJob_seq(String admRegularJob_seq) {
		this.admRegularJob_seq = admRegularJob_seq;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getJobContent() {
		return jobContent;
	}

	public void setJobContent(String jobContent) {
		this.jobContent = jobContent;
	}

	public String getEditerId() {
		return editerId;
	}

	public void setEditerId(String editerId) {
		this.editerId = editerId;
	}

	public String getEditerTime() {
		return editerTime;
	}

	public void setEditerTime(String editerTime) {
		this.editerTime = editerTime;
	}

}
