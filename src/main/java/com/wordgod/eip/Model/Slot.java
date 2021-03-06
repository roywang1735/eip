package com.wordgod.eip.Model;

public class Slot {
	private String slot_id;
    private String slot_name;
    
	public Slot(String slot_id, String slot_name) {
		this.slot_id = slot_id;
		this.slot_name = slot_name;
	}

	public String getSlot_id() {
		return slot_id;
	}

	public void setSlot_id(String slot_id) {
		this.slot_id = slot_id;
	}

	public String getSlot_name() {
		return slot_name;
	}

	public void setSlot_name(String slot_name) {
		this.slot_name = slot_name;
	}

}
