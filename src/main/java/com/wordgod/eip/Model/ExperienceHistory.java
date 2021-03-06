package com.wordgod.eip.Model;

public class ExperienceHistory {

	private String experienceHistory_seq;
    private String experience_id;
    private String experience_name;
  
	public ExperienceHistory(String experienceHistory_seq,String experience_id,String experience_name) {
		this.experienceHistory_seq = experienceHistory_seq;
		this.experience_id = experience_id;
		this.experience_name = experience_name;
	}

	public String getExperienceHistory_seq() {
		return experienceHistory_seq;
	}

	public void setExperienceHistory_seq(String experienceHistory_seq) {
		this.experienceHistory_seq = experienceHistory_seq;
	}

	public String getExperience_id() {
		return experience_id;
	}

	public void setExperience_id(String experience_id) {
		this.experience_id = experience_id;
	}

	public String getExperience_name() {
		return experience_name;
	}

	public void setExperience_name(String experience_name) {
		this.experience_name = experience_name;
	}

}
