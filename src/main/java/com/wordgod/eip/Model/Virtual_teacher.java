package com.wordgod.eip.Model;

public class Virtual_teacher {
	private String virtual_teacher_seq;
	private String teacher_id_parent;
	private String teacher_id_child;
	private String teacher_id_childName;

	public Virtual_teacher(String virtual_teacher_seq,String teacher_id_parent,String teacher_id_child,String teacher_id_childName) {
		this.virtual_teacher_seq = virtual_teacher_seq;
		this.teacher_id_parent = teacher_id_parent;
		this.teacher_id_child = teacher_id_child;
		this.teacher_id_childName = teacher_id_childName;
	}

	public String getVirtual_teacher_seq() {
		return virtual_teacher_seq;
	}

	public void setVirtual_teacher_seq(String virtual_teacher_seq) {
		this.virtual_teacher_seq = virtual_teacher_seq;
	}

	public String getTeacher_id_parent() {
		return teacher_id_parent;
	}

	public void setTeacher_id_parent(String teacher_id_parent) {
		this.teacher_id_parent = teacher_id_parent;
	}

	public String getTeacher_id_child() {
		return teacher_id_child;
	}

	public void setTeacher_id_child(String teacher_id_child) {
		this.teacher_id_child = teacher_id_child;
	}

	public String getTeacher_id_childName() {
		return teacher_id_childName;
	}

	public void setTeacher_id_childName(String teacher_id_childName) {
		this.teacher_id_childName = teacher_id_childName;
	}

}
