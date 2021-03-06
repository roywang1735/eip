package com.wordgod.eip.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.wordgod.eip.Model.*;

@Service
public class CourseService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	AccountService accountService;	
	@Autowired
	SalesService salesService;
	@Autowired
	SystemService systemService;	
	
	
	
	public List<Category> getCategory(String category_seq,String name) {
		String sql = "select * from eip.category where 1=1";
		if (category_seq != null && !category_seq.isEmpty()) {
			sql += " and category_seq = " + category_seq;
		}
		if (name != null && !name.isEmpty()) {
			sql += " and name = '"+name+"'";
		}		
		
		sql += " order by ranks";
		
		List<Category> items = jdbcTemplate.query(sql,
				(result, rowNum) -> new Category(
						result.getString("category_seq"), 
						result.getString("name"),
						result.getBoolean("enabled"),
						result.getString("bgColor"),
						result.getString("ranks")
		));
		return items;
	}
/**
	public List<TestSubject> getTestSubject(String category_seq,String testSubject_seq){
		String sql = "select a.*,b.name as categoryName from eip.testSubject a,eip.category b where a.category_seq=b.category_seq";
		if (category_seq != null && !category_seq.isEmpty()) {
			sql += " and a.category_seq = '"+category_seq+"'";
		}
		if (testSubject_seq != null && !testSubject_seq.isEmpty()) {
			sql += " and a.testSubject_seq = "+testSubject_seq;
		}		
		sql += " order by testSubject_seq";

		List<TestSubject> LTestSubject = jdbcTemplate.query(sql,
				(result, rowNum) -> new TestSubject(
						result.getString("testSubject_seq"), 
						result.getString("category_seq"),
						result.getString("name"),
						result.getString("categoryName"),
						"",
						"",
						""
		));
		
		for(int i=0;i<LTestSubject.size();i++) {
			
			List<TestSubjectSelection> LTestSubjectSelection = getTestSubjectSelection(LTestSubject.get(i).getTestSubject_seq(),"");
			
			List<TestSubjectSelection2> LTestSubjectSelection2 = getTestSubjectSelection2(LTestSubject.get(i).getTestSubject_seq());
					
			
			String testRound_Str = "";
			String testRoundItem_Str = "";
			String noLimit_Str = "";
			for(int j=0;j<LTestSubjectSelection.size();j++) {
				testRound_Str += "<div>"+LTestSubjectSelection.get(j).getTestRound()+"</div>";
			}
			for(int j=0;j<LTestSubjectSelection2.size();j++) {
				testRoundItem_Str += "<div>"+LTestSubjectSelection2.get(j).getTestRoundItem()+"</div>";
				noLimit_Str += "<div>"+LTestSubjectSelection2.get(j).getNoLimit()+"</div>";
			}			
			
			LTestSubject.get(i).setTestRound_Str(testRound_Str);
			LTestSubject.get(i).setTestRoundItem_Str(testRoundItem_Str);
			LTestSubject.get(i).setNoLimit_Str(noLimit_Str);
		}
		return LTestSubject;		
	}
**/
	public List<TestSubjectSelection> getTestSubjectSelection(String testSubject_id,String category_id){
		String sql2 = "select * from eip.testSubjectSelection where 1=1";
		if (testSubject_id != null && !testSubject_id.isEmpty()) {
			sql2 += " and testSubject_id = '"+testSubject_id+"'";
		}
		if (category_id != null && !category_id.isEmpty()) {
			sql2 = "select * from eip.testSubjectSelection a,eip.testSubject b where a.testSubject_id=b.testSubject_seq and b.category_seq='"+category_id+"'";
		}		
		
		List<TestSubjectSelection> LTestSubjectSelection = jdbcTemplate.query(sql2,
				(result, rowNum) -> new TestSubjectSelection(
						result.getString("testSubjectSelection_seq"), 
						result.getString("testSubject_id"),
						result.getString("testRound")
		));
		return LTestSubjectSelection;
	}

/**	
	public List<TestSubjectSelection2> getTestSubjectSelection2(String testSubject_id){
		String sql3 = "select * from eip.testSubjectSelection2 where testSubject_id='"+testSubject_id+"'";
		List<TestSubjectSelection2> LTestSubjectSelection2 = jdbcTemplate.query(sql3,
				(result, rowNum) -> new TestSubjectSelection2(
						result.getString("testSubjectSelection2_seq"), 
						result.getString("testSubject_id"),
						result.getString("testRoundItem"),
						result.getString("noLimit")
		));
		return LTestSubjectSelection2;
	}
**/
	
	public List<Subject> getSubject(String category_id,String subject_seq,String abbr,String active,String subjectName_like,String disable) {
		String sql = "select a.*,b.name as category_name,c.name as subjectContent_name"+
	                 " from eip.subject a,eip.category b,eip.subjectContent c"+
	                 " where a.subjectContent_code=c.code and a.category_id = b.category_seq";
		if (category_id != null && !category_id.isEmpty()) {
			sql += " and category_id = " + category_id;
		}
		if (subject_seq != null && !subject_seq.isEmpty()) {
			sql += " and subject_seq = " + subject_seq;
		}	
		if (abbr != null && !abbr.isEmpty()) {
			sql += " and abbr = '"+ abbr+"'";
		}
		if (active != null && !active.isEmpty()) {
			sql += " and active = " + active;
		}
		if (subjectName_like != null && !subjectName_like.isEmpty()) {
			sql += " and a.name like '"+subjectName_like+"'"  ;
		}
		if (disable == null || disable.equals("0")) {
			sql += " and (a.disable is null or a.disable='0')";
		}
		List<Subject> items = jdbcTemplate.query(sql,
				(result, rowNum) -> new Subject(
						result.getString("subject_seq"), 
						result.getString("category_id"), 
						result.getString("category_name"),
						result.getString("name"), 
						result.getString("abbr"), 
						result.getString("price"), 
						"",
						"",
						"",
						"",
						"",
						"",
						result.getString("remark"),
						result.getString("isVideo"),
						result.getString("isVirtual"),
						result.getString("parent_seq"),
						result.getString("updater"),
						result.getString("update_time"),
						result.getString("active"),
						result.getString("subjectContent_code"),
						result.getString("subjectContent_name"),
						"",
						result.getString("free_makeUpNo"),
						result.getString("disable")
				));
		return items;
	}
	
	public List<Subject_ver> getSubject_ver(String category_id,String subject_seq,String abbr,String active) {
		String sql = "select a.*,b.name as category_name,c.name as subjectContent_name"+
	                 " from eip.subject_ver a,eip.category b,eip.subjectContent c"+
	                 " where a.subjectContent_code=c.code and a.category_id = b.category_seq";
		if (category_id != null && !category_id.isEmpty()) {
			sql += " and category_id = " + category_id;
		}
		if (subject_seq != null && !subject_seq.isEmpty()) {
			sql += " and subject_seq = " + subject_seq;
		}	
		if (abbr != null && !abbr.isEmpty()) {
			sql += " and abbr = '"+ abbr+"'";
		}
		if (active != null && !active.isEmpty()) {
			sql += " and active = " + active;
		}
		sql += " order by update_time desc";
		
		List<Subject_ver> items = jdbcTemplate.query(sql,
				(result, rowNum) -> new Subject_ver(
						result.getString("subject_seq"), 
						result.getString("category_id"), 
						result.getString("category_name"),
						result.getString("name"), 
						result.getString("abbr"), 
						result.getString("price"), 
						"",
						"",
						"",
						"",
						"",
						"",
						result.getString("remark"),
						result.getString("isVideo"),
						result.getString("isVirtual"),
						result.getString("parent_seq"),
						result.getString("updater"),
						result.getString("update_time"),
						result.getString("active"),
						result.getString("subjectContent_code"),
						result.getString("subjectContent_name"),
						"",
						result.getString("free_makeUpNo")
				));
		return items;
	}	

	public List<Subject_R> getSubjectWithR_original(String subject_id,String class_style){
		String sql = "select * from eip.subject_R where 1=1";
		if (subject_id != null && !subject_id.isEmpty()) {
			sql += " and subject_id = " + subject_id;
		}
		if (class_style != null && !class_style.isEmpty()) {
			sql += " and class_style = " + class_style;
		}		
		List<Subject_R> items = jdbcTemplate.query(sql,
				(result, rowNum) -> new Subject_R(
						result.getString("subject_R_seq"), 
						result.getString("subject_id"), 
						result.getString("class_style"),
						result.getString("hrPrice_R"), 
						result.getString("counselingPrice_R"), 
						result.getString("lagnappePrice_R"),
						result.getString("handoutPrice_R"),
						result.getString("homeworkPrice_R"),
						result.getString("mockPrice_R")
		));	
		return items;
	}
	public List<Subject> getSubjectWithR(String category_id,String subject_seq,String abbr,String active,String class_style) {
		String sql = "select a.*,b.name as category_name,c.name as subjectContent_name,d.*"+
	                 " from eip.subject a,eip.category b,eip.subjectContent c,eip.subject_R d"+
	                 " where d.class_style='"+class_style+"' and a.subject_seq=d.subject_id and a.subjectContent_code=c.code and a.category_id = b.category_seq";
		if (category_id != null && !category_id.isEmpty()) {
			sql += " and category_id = " + category_id;
		}
		if (subject_seq != null && !subject_seq.isEmpty()) {
			sql += " and subject_seq = " + subject_seq;
		}	
		if (abbr != null && !abbr.isEmpty()) {
			sql += " and abbr = '"+ abbr+"'";
		}
		if (active != null && !active.isEmpty()) {
			sql += " and active = " + active;
		}
	
		List<Subject> items = jdbcTemplate.query(sql,
				(result, rowNum) -> new Subject(
						result.getString("subject_seq"), 
						result.getString("category_id"), 
						result.getString("category_name"),
						result.getString("name"), 
						result.getString("abbr"), 
						result.getString("price"), 
						result.getString("hrPrice_R"),
						result.getString("counselingPrice_R"),
						result.getString("lagnappePrice_R"),
						result.getString("handoutPrice_R"),
						result.getString("homeworkPrice_R"),
						result.getString("mockPrice_R"),
						result.getString("remark"),
						result.getString("isVideo"),
						result.getString("isVirtual"),
						result.getString("parent_seq"),
						result.getString("updater"),
						result.getString("update_time"),
						result.getString("active"),
						result.getString("subjectContent_code"),
						result.getString("subjectContent_name"),
						"",
						result.getString("free_makeUpNo"),
						result.getString("disable")
				));
		return items;
	}	
	
	public List<Material> getMaterial(String category_id,String material_seq) {
		String sql = "select * from eip.material where 1 = 1";
		if (category_id != null && !category_id.isEmpty()) {
			sql += " and category_id = " + category_id;
		}
		if (material_seq != null && !material_seq.isEmpty()) { 
			sql += " and material_seq = " + material_seq;
		}		
		List<Material> items = jdbcTemplate.query(sql,
				(result, rowNum) -> new Material(result.getString("material_seq"), result.getString("category_id"), "",
						result.getString("material_name"), result.getString("origin_price"), result.getString("sale_price"),
						result.getString("remark"), result.getString("update_time")));
		return items;
	}	
	
	public List<Mock> getMock(String category_id,String mock_seq,String active) {
		String sql = "select a.*,b.name as category_name from eip.mock a, category b where a.category_id = b.category_seq";
		if (category_id != null && !category_id.isEmpty()) {
			sql += " and a.category_id = " + category_id;
		}
		if (mock_seq != null && !mock_seq.isEmpty()) { 
			sql += " and a.mock_seq = " + mock_seq;
		}
		if (active != null && !active.isEmpty()) { 
			sql += " and a.active = " + active;
		}		

		List<Mock> items = jdbcTemplate.query(sql,
				(result, rowNum) -> new Mock(
						result.getString("mock_seq"), 
						result.getString("category_id"),
						result.getString("category_name"),
						result.getString("mock_name"),
						result.getString("active"),
						result.getString("original_price")
				));
		return items;
	}
	
	public List<MockDetail> getMockDetail(String mock_id,String mockDetail_seq) {
		String sql = "select a.*,c.category_id,b.name as categoryName from eip.mockDetail a,eip.category b,eip.mock c where c.mock_seq=a.mock_id and c.category_id=b.category_seq";
		if (mock_id != null && !mock_id.isEmpty()) {
			sql += " and mock_id = " + mock_id;
		}
		if (mockDetail_seq != null && !mockDetail_seq.isEmpty()) {
			sql += " and mockDetail_seq = " + mockDetail_seq;
		}

		List<MockDetail> LMockDetail = jdbcTemplate.query(sql,(result, rowNum) -> new MockDetail(
						result.getString("mockDetail_seq"), 
						result.getString("mock_id"),
						result.getString("totalNo"),
						result.getString("noName"),
						result.getString("testStyle"),
						"",
						result.getString("testMethod"),
						"",
						result.getString("category_id"),
						result.getString("categoryName")
		));
		for(int i=0;i<LMockDetail.size();i++) {
			if(LMockDetail.get(i).getTestStyle().equals("1")) {
				LMockDetail.get(i).setTestStyleName("個別");
			}else if(LMockDetail.get(i).getTestStyle().equals("2")) {
				LMockDetail.get(i).setTestStyleName("視訊");
			}else if(LMockDetail.get(i).getTestStyle().equals("3")) {
				LMockDetail.get(i).setTestStyleName("團體");
			}
			
			if(LMockDetail.get(i).getTestMethod().equals("1")) {
				LMockDetail.get(i).setTestMethodName("電腦");
			}else if(LMockDetail.get(i).getTestMethod().equals("2")) {
				LMockDetail.get(i).setTestMethodName("口試");
			}else if(LMockDetail.get(i).getTestMethod().equals("3")) {
				LMockDetail.get(i).setTestMethodName("紙筆");
			}
		}
		return LMockDetail;
	}
	
	public List<MockVideo> getMockVideo(String mock_id){
		String sql = "select a.*,b.name from eip.MockVideo a,eip.subject b where a.subject_id=b.subject_seq and mock_id="+mock_id;
		List<MockVideo> LMockVideo = jdbcTemplate.query(sql,
				(result, rowNum) -> new MockVideo(
						result.getString("mockVideo_seq"), 
						result.getString("mock_id"),
						result.getString("subject_id"),
						result.getString("name")
				));
		return LMockVideo;
	}
	
	public List<Counseling> getCounseling(String category_id,String counseling_seq,String active) {
		String sql = "select a.*,b.name as category_name from eip.counseling a,eip.category b where a.category_id = b.category_seq";
		if (category_id != null && !category_id.isEmpty()) {
			sql += " and category_id = " + category_id;
		}
		if (counseling_seq != null && !counseling_seq.isEmpty()) { 
			sql += " and counseling_seq = " + counseling_seq;
		}
		if (active != null && !active.isEmpty()) { 
			sql += " and active = " + active;
		}		
		List<Counseling> items = jdbcTemplate.query(sql,
				(result, rowNum) -> new Counseling(
						result.getString("counseling_seq"), 
						result.getString("category_id"), 
						result.getString("category_name"),
						result.getString("counseling_name"), 
						result.getString("origin_price"),
						result.getString("active")
				));
		return items;
	}

/**	
	public List<Lagnappe> getLagnappe(String category_id,String lagnappe_seq) {
		String sql = "select * from eip.lagnappe where 1 = 1";
		if (category_id != null && !category_id.isEmpty()) {
			sql += " and category_id = " + category_id;
		}
		if (lagnappe_seq != null && !lagnappe_seq.isEmpty()) { 
			sql += " and lagnappe_seq = " + lagnappe_seq;
		}
		List<Lagnappe> items = jdbcTemplate.query(sql,
				(result, rowNum) -> new Lagnappe(
						result.getString("lagnappe_seq"), 
						result.getString("lagnappe_name"), 
						result.getString("origin_price"),
						result.getString("exchange")
				));
		return items;
	}
**/		
	/*
	 * public String getSubjectTrWithCost(String category_id) { String sql =
	 * "select a.*, b.name as category_name from eip.subject a, eip.category b where a.category_id = b.category_seq"
	 * ; if (category_id != null && !category_id.isEmpty()) { sql +=
	 * " and category_id = " + category_id; } sql += " order by a.category_id";
	 * 
	 * List<Subject> LSubject = jdbcTemplate.query(sql, (result, rowNum) -> new
	 * Subject(result.getString("subject_seq"), result.getString("category_id"),
	 * result.getString("category_name"), result.getString("name"),
	 * result.getString("abbr"), result.getString("price"),
	 * result.getString("class_no"), result.getString("class_makeup"),
	 * result.getString("remark"),result.getString("sql_str")));
	 * 
	 * String returnStr = ""; String Category_name = ""; for (int i = 0; i <
	 * LSubject.size(); i++) { if (i == 0) { Category_name =
	 * LSubject.get(i).getCategory_name(); } else { if
	 * (Category_name.equals(LSubject.get(i).getCategory_name())) { Category_name =
	 * ""; } else { Category_name = LSubject.get(i).getCategory_name(); returnStr +=
	 * "<tr><td colspan='10'><hr style='border: 1px dashed lightblue;'></td></tr>";
	 * } } returnStr += "<tr style=''>" +
	 * "<th style='padding:5px'><font color='brown'>" + Category_name +
	 * "</font></th>" + "<td>" + LSubject.get(i).getName() + "</td>" + "<td>" +
	 * LSubject.get(i).getAbbr() + "</td>" + "<td>" + LSubject.get(i).getPrice() +
	 * "</td>" + "<td>" + "750" + "</td>" + "<td>" + LSubject.get(i).getClass_no() +
	 * "</td>" + "<td>" + LSubject.get(i).getClass_makeup() + "</td>" + "<td>" +
	 * LSubject.get(i).getRemark() + "</td>" + "<td><A href='" +
	 * LSubject.get(i).getSubject_seq() + "'>...</A></td>" + "</tr>"; Category_name
	 * = LSubject.get(i).getCategory_name(); } return returnStr; }
	 */
	
	/*
	 * public String getSubjectStr(String category_id) { String sql =
	 * "select a.*, b.name as category_name from eip.subject a, eip.category b where a.category_id = b.category_seq"
	 * ; if (category_id != null && !category_id.isEmpty()) { sql +=
	 * " and category_id = " + category_id; } sql += " order by a.category_id";
	 * 
	 * List<Subject> LSubject = jdbcTemplate.query(sql, (result, rowNum) -> new
	 * Subject(result.getString("subject_seq"), result.getString("category_id"),
	 * result.getString("category_name"), result.getString("name"),
	 * result.getString("abbr"), result.getString("price"),
	 * result.getString("class_no"), result.getString("class_makeup"),
	 * result.getString("remark"),result.getString("sql_str")));
	 * 
	 * String returnStr = ""; String Category_name = ""; for (int i = 0; i <
	 * LSubject.size(); i++) { if (i == 0) { Category_name =
	 * LSubject.get(i).getCategory_name(); } else { if
	 * (Category_name.equals(LSubject.get(i).getCategory_name())) { Category_name =
	 * ""; } else { Category_name = LSubject.get(i).getCategory_name(); returnStr +=
	 * "<tr><td colspan='10'><hr style='border: 1px dashed lightblue;'></td></tr>";
	 * } } returnStr += "<tr style=''>" +
	 * "<th style='padding:5px'><font color='brown'>" + Category_name +
	 * "</font></th>" + "<td>" + LSubject.get(i).getName() + "</td>" + "<td>" +
	 * LSubject.get(i).getAbbr() + "</td>" + "<td>" + LSubject.get(i).getPrice() +
	 * "</td>" + "<td>" //+ "100" + "</td>" + "<td>" //+
	 * LSubject.get(i).getClass_no() + "</td>" + "<td>" //+
	 * LSubject.get(i).getClass_makeup() + "</td>" + "<td>" +
	 * LSubject.get(i).getRemark() + "</td>" + "<td><A href='" +
	 * LSubject.get(i).getSubject_seq() + "'>...</A></td>" + //+
	 * "<td>"+LSubject.get(i).getSql_str()+"</td>"+ "</tr>"; Category_name =
	 * LSubject.get(i).getCategory_name(); } return returnStr; }
	 */	

	public List<Teacher> getTeacher(String teacher_seq,String name,String realName,String code,String enabled,String virtual_teacher,String artOrReal) {
		String sql = "select a.* from eip.teacher a where 1=1";
		if (teacher_seq != null && !teacher_seq.isEmpty()) {sql += " and a.teacher_seq="+teacher_seq;}
		if (name != null && !name.isEmpty()) {sql += " and a.name='"+name.trim()+"'";}
		if (realName != null && !realName.isEmpty()) {sql += " and a.realName='"+realName.trim()+"'";}
		if (code != null && !code.isEmpty()) {sql += " and a.code='"+code+"'";}
		if (enabled != null && !enabled.isEmpty()) {sql += " and a.enabled="+enabled;}
		if (virtual_teacher != null && !virtual_teacher.isEmpty()) {sql += " and a.virtual_teacher="+virtual_teacher;}
		if (artOrReal != null && !artOrReal.isEmpty()) {sql += " and ( a.name='"+artOrReal.trim()+"' or a.realName='"+artOrReal.trim()+"')";}

		
		List<Teacher> items = jdbcTemplate.query(sql, (result, rowNum) -> new Teacher(
				result.getString("teacher_seq"),
				result.getString("code"), 
				result.getString("name"),
				result.getString("realName"), 
				result.getString("IDN"), 
				result.getString("Tel"), 
				result.getString("ADR"), 
				result.getString("enabled"),
				result.getString("creater"),
				result.getString("update_time"),
				result.getString("virtual_teacher"),
				result.getString("school_id"),
				result.getString("email"),
				result.getString("line"),
				result.getString("fb"),
				result.getString("email_contact"),
				result.getString("line_contact"),
				result.getString("fb_contact"),
				result.getString("tel_contact"),
				result.getString("face_contact"),
				"",
				result.getString("county"),
				result.getString("district")
		));
		return items;
	}
	
	public List<Teacher> getTeacher2(String enabled,String account0) {
		String sql = "select a.* from eip.teacher a,eip.employee b where 1=1";
		if (enabled != null && !enabled.isEmpty()) {sql += " and a.enabled="+enabled;}
		if (account0 != null && !account0.isEmpty()) {sql += " and b.ch_name=a.realName and b.account0='"+account0+"'";}
	
		List<Teacher> items = jdbcTemplate.query(sql, (result, rowNum) -> new Teacher(
				result.getString("teacher_seq"),
				result.getString("code"), 
				result.getString("name"),
				result.getString("realName"), 
				result.getString("IDN"), 
				result.getString("Tel"), 
				result.getString("ADR"), 
				result.getString("enabled"),
				result.getString("creater"),
				result.getString("update_time"),
				result.getString("virtual_teacher"),
				result.getString("school_id"),
				result.getString("email"),
				result.getString("line"),
				result.getString("fb"),
				result.getString("email_contact"),
				result.getString("line_contact"),
				result.getString("fb_contact"),
				result.getString("tel_contact"),
				result.getString("face_contact"),
				"",
				result.getString("county"),
				result.getString("district")
		));
		return items;
	}	
	
	public List<Virtual_teacher> getVirtual_teacher(String teacher_id_parent) {
		String sql = "select a.*,b.name as teacher_id_childName from eip.virtual_teacher a,eip.teacher b where a.teacher_id_child = b.teacher_seq and a.teacher_id_parent='"+teacher_id_parent+"'";
		List<Virtual_teacher> items = jdbcTemplate.query(sql, (result, rowNum) -> new Virtual_teacher(
				result.getString("virtual_teacher_seq"),
				result.getString("teacher_id_parent"), 
				result.getString("teacher_id_child"),
				result.getString("teacher_id_childName")
		));
		return items;
	}
	
	public List<FlowStatus> getFlowStatus() {
		String sql = "select * from eip.FlowStatus where code !=7";
		List<FlowStatus> items = jdbcTemplate.query(sql,
				(result, rowNum) -> new FlowStatus(result.getString("code"), result.getString("name")));
		return items;
	}

	public List<Slot> getSlot() {
		String sql = "select * from eip.Slot";
		List<Slot> items = jdbcTemplate.query(sql,
				(result, rowNum) -> new Slot(result.getString("slot_id"), result.getString("slot_name")));
		return items;
	}	
	
	
	public String getCourseStr(String teacher,String class_date,String timeFrom,String timeTo,String class_no,String class_style,String slot_id,String school_code) {
		String returnStr = "";

		int classNo = Integer.valueOf(class_no);
		List<Teacher> LTeacher = getTeacher("","","","","","","");
		
		String teacherStr = "";
	    for(int i=0;i<LTeacher.size();i++) {
	    	if(LTeacher.get(i).getTeacher_seq().equals(teacher)) {
	    		teacherStr += "<option value='"+LTeacher.get(i).getTeacher_seq()+"' selected>"+LTeacher.get(i).getName()+"</option>";
	    	}else {
	    		teacherStr += "<option value='"+LTeacher.get(i).getTeacher_seq()+"'>"+LTeacher.get(i).getName()+"</option>";
	    	}
	    }
	    
	    List<classRoom> LclassRoom = systemService.getclassRoom(school_code,"","school_code,name","","");
	    String classRoomStr="",selected1="",selected2="",selected3="";
	    for(int i=0;i<LclassRoom.size();i++) {
	    	classRoomStr +=
	    	"<option value='"+LclassRoom.get(i).getName()+"'>"+LclassRoom.get(i).getName()+"</option>";
	    }

	    String styleStr = "";
	    if(class_style.equals("1")) {
	    	selected1="selected";
	    }else if(class_style.equals("2")) {
	    	selected2="selected";
	    }else if(class_style.equals("3")) {
	    	selected3="selected";
	    }
	    
    	styleStr +="<option value=1 "+selected1+">正班</option>";
    	styleStr +="<option value=2 "+selected2+">Video</option>";
    	styleStr +="<option value=3 "+selected3+">線上</option>";
	    
	    String slotStr = "",sel_1="",sel_2="",sel_3="";
	    if(slot_id.equals("1")) {
	    	sel_1 = "selected";
	    }else if(slot_id.equals("2")) {
	    	sel_2 = "selected";
	    }else if(slot_id.equals("3")) {
	    	sel_3 = "selected";
	    }
    	slotStr +=
		  "<option value='1' "+sel_1+">早</option>"+
		  "<option value='2' "+sel_2+">午</option>"+
		  "<option value='3' "+sel_3+">晚</option>";
        
        String trialStr0="",trialStr1="";

		String autoDateFill = 
				"<script>"+
						"$(function () {"+ 
							"$(\"[data-toggle='tooltip']\").tooltip({"+
								"content: function() {"+
									"return $(this).attr('title');"+
		                        "}"+
		                    "});"+ 
		                "});"+
	            "</script>";
		
            
    		autoDateFill +=
    				"<div style='padding:5px;text-align:right'><img src='/images/edit2.png' height='15px' title='自動填入日期及時間'>&nbsp;<A href='javascript:autoDateFill();' style='text-decoration:underline;letter-spacing:1px;color:blue'>自動填入日期及時間<br></A></div>"+	
    				"<span style='padding:5px;color:darkblue;letter-spacing:1px'>(訂期別)課程呈現起日<input type='text' id='onSell' name='onSell' readonly style='width:120px;height:35px;border:1px #dddddd solid;border-radius:3px' onclick='$(this).datepicker({dateFormat: \"mm/dd/yy D\",});$(this).datepicker(\"show\")'></span>&nbsp;&nbsp;"+
    				"<span style='padding:5px;color:darkblue;letter-spacing:1px'>課程呈現迄日<input type='text' id='offSell' name='offSell' readonly style='width:120px;height:35px;border:1px #dddddd solid;border-radius:3px' onclick='$(this).datepicker({dateFormat: \"mm/dd/yy D\",});$(this).datepicker(\"show\")'></span>"+
    				"<A href='#' class='tooltip-test' data-toggle='tooltip' title=\"<span style='padding:2px;letter-spacing:2px;background-color:#ff66b2;color:white'>&#10149; 當定期別時, 課程呈現起日(預設為該期別前60日), 此時學員便能選取該期別課程,直至課程迄日後(預設為此期別第三堂課), 則無法再自行選取!\"><img src='/images/message.png' height='9px'/></A>";
    				
    		
	    	returnStr += 	
		    		"<div style=''>"+
		    		  "<div style='font-size:small;text-align:center;padding:2px'>"+
							autoDateFill+						
					  "</div>"+
					"</div>";		
					
    		String classDateValue = "";
		    for(int i=0;i<classNo;i++) {
		    	if(i==0) {
		    		classDateValue = class_date;
		    		trialStr0= "";
		    		trialStr1 = "selected";
		    	}else {
		    		classDateValue = "";
		    		trialStr0= "selected";
		    		trialStr1= "";
		    	}
	    		  returnStr +=
			      "<div style='margin:10px;padding-top:10px;padding-left:10px;border-radius:10px;border:1px #dddddd solid'>"+
						"<div style='display:inline-block;color:black' id='selectArea"+(i+1)+"' class='selArea'>"+
	    		            "<select id='class_th' name='class_th' class='class_th_class' style='padding:5px;font-weight:bold' onchange='reArrange(this)'>";
	    		  					String selectX = "";
	    		  					returnStr +="<option value='-1'>停 課</option>";
	    		  					for(int x=0;x<classNo+5;x++) {
	    		  						selectX = "";
	    		  						if(x==i) {selectX = "selected";}
	    		  						returnStr +="<option value='"+(x+1)+"' "+selectX+">第 "+(x+1)+" 堂</option>";
	    		  					}	
			    		    returnStr +=    
			    		    "</select>"+
						"</div>&nbsp;"+
						"<div style='display:inline-block'><input type='text' style='display:inline-block;padding:0px;width:190px' class='form-control' name='c_class_name' placeholder='課堂名稱'/></div>"+
						"<div style='display:inline-block;padding:3px'><input type='text' style='width:140px' readonly='readonly' name = 'c_class_date' value='"+classDateValue+"' class='form-control class_date videoEmpty'  onclick='$(this).datepicker({dateFormat: \"mm/dd/yy D\",});$(this).datepicker(\"show\")' onchange='checkDay(this.value)'/></div>"+
						"<div style='display:inline-block;padding:3px'>"+
							"<select name='c_slot_id' class='form-control videoEmpty2'>"+
								"<option value='-1'></option>"+
								slotStr+
							"</select>"+						
				        "</div>"+
						"<div style='display:inline-block'>"+
							"<input type='text' style='display:inline-block;width:65px' class='form-control timeFrom videoEmpty' name='c_timeFrom' value='"+timeFrom+"' />"+ 
							"~"+
							"<input type='text' style='display:inline-block;width:65px' class='form-control timeTo videoEmpty' name='c_timeTo' value='"+timeTo+"' />"+						
						"</div>"+					
						"<div style='display:inline-block;padding:3px'>"+
							"<select name='c_teacher_id' class='form-control videoEmpty2'>"+
								"<option value='-1'></option>"+
								teacherStr+
							"</select>"+						
						"</div>"+				    
	                    //--------------------------------------------------------------------------//
					  	"<div style='display:inline-block;vertical-align:top;padding:3px'>"+
							"<span style='font-size:x-small;font-weight:bold;padding:1px'><A href=javascript:void(0) style='color:blue;text-decoration:underline' onclick=classReceive("+i+")>領 取</A></span><br>"+	
							"<input type='hidden' name='classReceiveHidden' id='classReceiveHidden'>"+
							"<textarea cols='38' rows='4' name='classReceiveTextarea' style='font-size:x-small;background-color:#ffeeff;border-radius:3px'></textarea>"+
					    "</div>"+
					    /**		
					    "<div style='display:inline-block;padding:5px'>"+
					    	"<br><textarea cols='38' rows='4' name='c_class_remarkAdm' style='font-size:x-small;border-radius:3px' placeholder='行政作業備註'></textarea>"+
					    "</div>"+
					    **/
					    "<div style='display:inline-block;padding:5px''>"+
				    		"<br><textarea cols='38' rows='4' name='c_class_remark' style='font-size:x-small;border-radius:3px' placeholder='學員通知備註'></textarea>"+
				    	"</div>"+
				    	"<div style='display:inline-block;padding:2px;vertical-align:top;margin-top:20px'>"+	
				    			"上課方式"+
				    			"<select name='c_class_style' class='form-control' style='width:90px;font-size:small'  onChange=\" $(this).parent().parent().find('.videoEmpty').val('');$(this).parent().parent().find('.videoEmpty2').val('-1')\">"+
									styleStr+
							    "</select>"+							
						 "</div>"+ 							    
				    	 "<div style='display:inline-block;padding:2px;vertical-align:top;margin-top:20px'>"+	
								"教室"+
				    	 		"<select name='c_classRoom' class='form-control' style='width:70px;margin-left:2px'>"+
									"<option value=''></option>"+
								    classRoomStr+
								"</select>"+							
						  "</div>"+ 								
						  "<div style='display:inline-block;padding:2px;vertical-align:top;margin-top:20px'>"+
						  		"試聽"+	
						  		"<select name='c_class_trial' class='form-control' style='width:90px;font-size:small'>"+
									  "<option value='0' "+trialStr0+">不試聽</option>"+
									  "<option value='1' "+trialStr1+">可試聽</option>"+
							    "</select>"+						
						  "</div>"+								    
						"</div>"+	    
				  "</div>"+		    	
		    "</div>"; 
		}
		return returnStr;
	}
	
	public String getCourseStr_2(String teacher,String class_date,String timeFrom,String timeTo,String class_style,String slot_id,String class_th,String class_no,String school_code) {
		String returnStr = "";
		List<Teacher> LTeacher = getTeacher("","","","","","","");
		int classNo = Integer.valueOf(class_no);
		
		String teacherStr = "";
	    for(int i=0;i<LTeacher.size();i++) {
	    	if(LTeacher.get(i).getTeacher_seq().equals(teacher)) {
	    		teacherStr += "<option value='"+LTeacher.get(i).getTeacher_seq()+"' selected>"+LTeacher.get(i).getName()+"</option>";
	    	}else {
	    		teacherStr += "<option value='"+LTeacher.get(i).getTeacher_seq()+"'>"+LTeacher.get(i).getName()+"</option>";
	    	}
	    }
	    
	    List<classRoom> LclassRoom = systemService.getclassRoom(school_code,"","school_code,name","","");
	    String classRoomStr = "";
	    for(int i=0;i<LclassRoom.size();i++) {
	    	classRoomStr +=
	    	"<option value='"+LclassRoom.get(i).getName()+"'>"+LclassRoom.get(i).getName()+"</option>";
	    }	    
	    
	    String styleStr = "";
	    if(class_style.equals("1")) {
	    	styleStr +="<option value=1 selected>正班</option>";
	    }else {
	    	styleStr +="<option value=1>正班</option>";
	    }
	    if(class_style.equals("2")) {
	    	styleStr +="<option value=2 selected>Video</option>";
	    }else {
	    	styleStr +="<option value=2>Video</option>";
	    }
	    
	    String slotStr = "",sel_1="",sel_2="",sel_3="";
	    if(slot_id.equals("1")) {
	    	sel_1 = "selected";
	    }else if(slot_id.equals("2")) {
	    	sel_2 = "selected";
	    }else if(slot_id.equals("3")) {
	    	sel_3 = "selected";
	    }
    	slotStr +=
		  "<option value='1' "+sel_1+">早</option>"+
		  "<option value='2' "+sel_2+">午</option>"+
		  "<option value='3' "+sel_3+">晚</option>";
        
        String trialStr0="selected",trialStr1="",classDateValue="";
	        
	    	returnStr += 	
    		"<div style='margin:10px;background-color:#eeeeee;padding-top:10px;padding-left:10px;border-radius:10px;border:1px #dddddd solid;'>"+  	
		      "<div>"+			    
				"<div style='display:inline-block;color:black'  class='selArea'>"+
				    "<A href='javascript:void(0)' title='刪除此筆'  onclick='$(this).parent().parent().parent().remove();'><img src='/images/delete.png' height='15px'/></A>&nbsp;"+
					"<select id='class_th' name='class_th' class='class_th_class' style='padding:5px;font-weight:bold' onchange='reArrange(this)'>";
  					String selectX = "";
  					returnStr +="<option value=''></option><option value='-1'>停 課</option>";
  					for(int x=0;x<classNo+5;x++) {
  						selectX = "";
  						returnStr +="<option value='"+(x+1)+"' "+selectX+">第 "+(x+1)+" 堂</option>";
  					}
	    		    returnStr +=    
	    		    "</select>"+
				"</div>&nbsp;"+				
				
				"<div style='display:inline-block'><input type='text' style='display:inline-block;width:190px;padding:0px' class='form-control' name='c_class_name' placeholder='課堂名稱'/></div>"+
				"<div style='display:inline-block;padding:3px'><input type='text' placeholder='&#128197;' style='width:130px' readonly='readonly' name='c_class_date' class='addDate' value='"+classDateValue+"' class='form-control class_date'  onclick=\"$(this).datepicker({dateFormat:'mm/dd/yy D',});$(this).datepicker('show');\" onchange=\"checkDay(this.value)\" /></div>"+
				"<div style='display:inline-block;padding:3px'>"+
					"<select name='c_slot_id' class='form-control videoEmpty2'>"+
						"<option value='-1'></option>"+
						slotStr+
					"</select>"+						
		        "</div>"+
				"<div style='display:inline-block'>"+
					"<input type='text' style='display:inline-block;width:65px' class='form-control timeFrom' name='c_timeFrom' value='"+timeFrom+"' />"+ 
					"~"+
					"<input type='text' style='display:inline-block;width:65px' class='form-control timeTo' name='c_timeTo' value='"+timeTo+"' />"+						
				"</div>"+					
				"<div style='display:inline-block;padding:3px'>"+
					"<select name='c_teacher_id' class='form-control videoEmpty2'>"+
						"<option value='-1'></option>"+
						teacherStr+
					"</select>"+						
				"</div>"+
			"</div>"+
			"<div>"+	
				  	"<div style='display:inline-block;vertical-align:top;padding:3px'>"+
						"<span style='font-size:x-small;font-weight:bold'><A href=javascript:void(0) style='color:blue;text-decoration:underline;padding:1px' onclick=classReceive("+class_th+")>領取</A></span><br>"+					        
						"<input type='hidden' name='classReceiveHidden' id='classReceiveHidden'>"+
						"<textarea cols='38' rows='4' name='classReceiveTextarea' style='font-size:x-small;background-color:#ffeeff;border-radius:3px'></textarea>"+
				    "</div>"+
				    "<div style='display:inline-block;padding:5px'>"+
				        "<span>&nbsp;</span>"+
			    		"<textarea cols='38' rows='4' name='c_class_remark' style='font-size:x-small;border-radius:3px' placeholder='學員提示備註'></textarea>"+
			    	"</div>"+
			    	"<div style='display:inline-block;padding:2px;vertical-align:top;margin-top:20px'>"+	
		    			"上課方式"+
		    			"<select name='c_class_style' class='form-control' style='width:90px;font-size:small'  onChange=\" $(this).parent().parent().parent().find('.videoEmpty').val('');$(this).parent().parent().parent().find('.videoEmpty2').val('-1')\">"+
							styleStr+
					    "</select>"+							
			        "</div>"+
			    	"<div style='display:inline-block;padding:2px;vertical-align:top;margin-top:20px'>"+	
							"教室"+
			    	 		"<select name='c_classRoom' class='form-control' style='width:70px;margin-left:2px'>"+
								"<option value=''></option>"+
							    classRoomStr+
							"</select>"+							
					"</div>"+ 				    
					"<div style='display:inline-block;padding:2px;vertical-align:top;margin-top:20px'>"+
				  		"試聽"+	
				  		"<select name='c_class_trial' class='form-control' style='width:90px;font-size:small'>"+
							  "<option value='0' "+trialStr0+">不試聽</option>"+
							  "<option value='1' "+trialStr1+">可試聽</option>"+
					    "</select>"+						
				    "</div>"+    
			  "</div>"+	
		    "</div>"; 

		return returnStr;
	}	
	
	
	public List<Course> getCourse(String course_seq){ 
		String sql = "SELECT a.*,b.name as subject_name FROM eip.course a, eip.subject b where a.subject_id = b.subject_seq";
		
		if (course_seq != null && !course_seq.isEmpty()) {
			sql += " and course_seq="+course_seq;
		}
		
		List<Course> LCourse = jdbcTemplate.query(sql,(result,rowNum)->new Course(
	    		result.getString("course_seq"),
	    		result.getString("status_code"),
	    		result.getString("school_code"),
                result.getString("category_id"),
                result.getString("subject_id"),
                result.getString("subject_name"),
                "",
                result.getString("issue_date"),
                result.getString("from_date"),
                result.getString("to_date"),
                "",
                ""
                ));
		return LCourse;
	}

	
	public String saveGrade(Grade grade) {
        //課程簽核改成直接待上架
		if(grade.getStatus_code().equals("2")) {
			grade.setStatus_code("3");
			grade.setHaveRead("0");//審核未讀
		}
		
		jdbcTemplate.update("INSERT INTO eip.grade VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURDATE(),?,?,?,?,?,?);",
				    grade.getSchool_code(),
				    grade.getCategory_id(),
				    grade.getSubject_id(),
				    grade.getStatus_code(),
				    grade.getGradeName(),
				    grade.getClass_no(),
				    "",
				    grade.getClass_style(),
				    grade.getLower_limit(),
				    grade.getUpper_limit(),
				    grade.getTeacher_id(),
				    grade.getClass_start_date_0(),
				    grade.getVideo_date(),
				    grade.getSlot_id(),
				    grade.getTimeFrom(),
				    grade.getTimeTo(),
				    grade.getCreater(),
				    grade.getOnSell().substring(0,10),
				    grade.getOffSell().substring(0,10),
				    grade.getGrade_date(),
				    grade.getHaveRead(),
				    "0",
				    grade.getVideoPreserve()
		);
		int x  = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);	
		return Integer.toString(x);
	}
	
	public Boolean updateGrade(Grade grade){
		jdbcTemplate.update("Update eip.grade set school_code=?,category_id=?,subject_id=?,status_code=?,gradeName=?,class_no=?,class_makeup=?,class_style=?,lower_limit=?,upper_limit=?,teacher_id=?,class_start_date_0=?,video_date=?,slot_id=?,timeFrom=?,timeTo=?,update_time=CURDATE(),onSell=?,offSell=?,grade_date=?,videoPreserve=? where grade_seq=?;",
			    grade.getSchool_code(),
			    grade.getCategory_id(),
			    grade.getSubject_id(),
			    grade.getStatus_code(),
			    grade.getGradeName(),
			    grade.getClass_no(),
			    "",
			    grade.getClass_style(),
			    grade.getLower_limit(),
			    grade.getUpper_limit(),
			    grade.getTeacher_id(),
			    grade.getClass_start_date_0(),
			    grade.getVideo_date(),
			    grade.getSlot_id(),
			    grade.getTimeFrom(),
			    grade.getTimeTo(),
			    grade.getOnSell().substring(0,10),
			    grade.getOffSell().substring(0,10),
			    grade.getGrade_date(),
			    grade.getVideoPreserve(),
			    grade.getGrade_seq()
	    );				
		return true;
	}
	
	public Boolean updateGradeHaveRead(String grade_seq){
		jdbcTemplate.update("Update eip.grade set haveRead=1  where grade_seq=?;",
				grade_seq
	    );				
		return true;
	}
	
	public Boolean updateComboSaleHaveRead(String comboSale_seq){
		jdbcTemplate.update("Update eip.comboSale set haveRead=1  where comboSale_seq=?;",
				comboSale_seq
	    );				
		return true;
	}	
	
	public Boolean updateSpecialCaseHaveRead(String studentExperience_seq){
		jdbcTemplate.update("Update eip.studentExperience set haveRead=1  where studentExperience_seq=?;",
				studentExperience_seq
	    );				
		return true;
	}	
	
	public Boolean deleteClasses(String grade_seq){
		if (grade_seq != null && !grade_seq.isEmpty()){
			jdbcTemplate.update("delete from eip.classes where grade_id="+grade_seq);
		}	
		return true;
	}
	
	public Boolean deleteVideoOpen(String grade_seq){
		if (grade_seq != null && !grade_seq.isEmpty()){
			jdbcTemplate.update("delete from eip.videoOpen where grade_id="+grade_seq);
		}	
		return true;
	}
	
	public List<Grade> getGradeList(String teacher_id,String subject_id,String grade_seq,String school_code,String category_id,String subject_abbr,String class_start_date_0,String teacher_name,String status_code,String xth,String video_date,String artOrReal,String limit,String haveRead,String orderBy,String gradeName,String include_disable,String include_lock,String videoPreserve){ 
		String sql = "SELECT i.name as subjectContent_name,a.*,b.name as teacher_name, b.code as teacher_code, d.price as subject_price,c.name as category_name, d.name as subject_name, d.abbr as subject_abbr, e.name as status_name, f.name as school_name, g.slot_name, h.class_date as class_xth_date"+
				     " FROM eip.subjectContent i,eip.Grade a, eip.teacher b, eip.category c, eip.subject d, eip.FlowStatus e, eip.school f, eip.slot g, eip.classes h"+
				     " where i.code=d.subjectContent_code and a.slot_id=g.slot_id and a.teacher_id = b.teacher_seq and a.category_id = c.category_seq and a.subject_id = d.subject_seq and a.status_code=e.code and a.school_code = f.code and a.grade_seq=h.grade_id and a.status_code!=7";

		if (teacher_id != null && !teacher_id.isEmpty()) {
			sql += " and a.teacher_id='"+teacher_id+"'";
		}		
		if (subject_id != null && !subject_id.isEmpty()) {
			sql += " and a.subject_id="+subject_id;
		}
		if (grade_seq != null && !grade_seq.isEmpty()) {
			sql += " and a.grade_seq="+grade_seq;
		}
		if (category_id != null && !category_id.isEmpty()) {
			sql += " and a.category_id="+category_id;
		}		
		if (school_code != null && !school_code.isEmpty()) {
			sql += " and a.school_code='"+school_code+"'";
		}
		if (subject_abbr != null && !subject_abbr.isEmpty()) {
			sql += " and d.abbr='"+subject_abbr+"'";
		}
		if (class_start_date_0 != null && !class_start_date_0.isEmpty()) {
			sql += " and a.class_start_date_0='"+class_start_date_0+"'";
		}	
		if (teacher_name != null && !teacher_name.isEmpty()) {
			sql += " and b.name='"+teacher_name+"'";
		}
		if(status_code != null && !status_code.isEmpty()) {
			sql += " and a.status_code in ("+status_code+")";
		}
		if(xth != null && !xth.isEmpty()) {
			sql += " and h.class_th="+xth;
		}
		if (video_date != null && !video_date.isEmpty()) {
			sql += " and a.video_date='"+video_date+"'";
		}
		
		if (artOrReal != null && !artOrReal.isEmpty()) {
			sql += " and ( b.name='"+artOrReal+"' or b.realName='"+artOrReal+"')";
		}
		
		if (haveRead != null && !haveRead.isEmpty()) {
			sql += " and a.haveRead="+haveRead;
		}
		
		if(gradeName!=null && !gradeName.isEmpty()) {
			sql += " and a.gradeName='"+gradeName+"'";
		}
		
		//disable: 正常:0,null,課程刪除或不開課:1,勿訂:2
		if(include_disable!=null && include_disable.equals("1")) {
			 //全部顯示
		}else {
			 sql +=" and (a.disable !='1' or a.disable is null)";
		}
		
		//包含勿訂
		if(include_lock!=null && include_lock.equals("1")) {
		}else {
			 sql +=" and (a.disable !='2' or a.disable is null)";
		}		
				
		if (videoPreserve != null && !videoPreserve.isEmpty()) {
			sql += " and a.videoPreserve='"+videoPreserve+"'";
		}		
	    
		if(orderBy !=null && !orderBy.isEmpty()) {
			sql += " "+orderBy;
		}else {
			sql +=" order by a.grade_seq desc";
		}	
		
		if (limit != null && !limit.isEmpty()) {
			sql += " LIMIT "+limit;
		}
		  List<Grade> LGrade = jdbcTemplate.query(sql,(result,rowNum)->new Grade(
	    		result.getString("grade_seq"),
	    		result.getString("school_code"),
	    		result.getString("school_name"),
	    		result.getString("category_id"),
	    		result.getString("category_name"),
	    		result.getString("subject_id"),
	    		result.getString("subject_name"),
	    		result.getString("subject_abbr"),
	    		result.getString("status_code"),
	    		result.getString("status_name"),
	    		result.getString("gradeName"),
	    		result.getString("class_no"),
	    		result.getString("class_makeup"),
                result.getString("class_style"),
                result.getString("lower_limit"),
                result.getString("upper_limit"),
                result.getString("teacher_id"),
                result.getString("teacher_name"),
                result.getString("teacher_code"),
                result.getString("class_start_date_0"),
                "", //class_start_date(新期別格式)
                result.getString("class_xth_date"),
                result.getString("slot_id"),
                result.getString("slot_name"),
                result.getString("timeFrom"),
                result.getString("timeTo"),
                result.getString("video_date"),
                "",
                null,
                result.getString("creater"),
                result.getString("update_time"),
                "", // attendStatus
                "", // attendStatusV
                result.getString("onSell"),
                result.getString("offSell"),
                "",
                result.getString("subject_price"),
                "",
                "",
                "",
                "",
                "",
                result.getString("subjectContent_name"),
                result.getString("grade_date"),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                result.getString("haveRead"),
                result.getString("disable"),
                result.getString("videoPreserve")
          ));
		  
		  List<SubjectTeacher> LSubjectTeacher = new ArrayList<SubjectTeacher>() ;
		  if(subject_id != null && !subject_id.isEmpty()) {
			  LSubjectTeacher =  getSubjectTeacher(subject_id);
		  } 	  
		
		  for(int i=0;i<LGrade.size();i++) {
				if(LGrade.get(i).getClass_start_date_0().length()==10) { //ex.07/24/2019				
					LGrade.get(i).setClass_start_date(LGrade.get(i).getClass_start_date_0().substring(8,10)+LGrade.get(i).getClass_start_date_0().substring(0,2)+LGrade.get(i).getClass_start_date_0().substring(3,5));
				}
				
				for(int j=0;j<LSubjectTeacher.size();j++) {
					if(LGrade.get(i).getTeacher_id().equals(LSubjectTeacher.get(j).getTeacher_id())) {
						LGrade.get(i).setGradeNo2(LSubjectTeacher.get(j).getGradeNo());
						LGrade.get(i).setGradeNoATime(LSubjectTeacher.get(j).getGradeNoATime());
					}
				}
						
		  }
		return LGrade;
	}

	public List<Grade> getGrade(String grade_seq,String status_code,String subject_id,String school_code,String class_start_date_0,String category_id,String teacher_id,String limit,String orderBy,String showLastClassDate,String notIn,String disable,String onlyOnline){
		String sql = "select a.*,b.name as teacher_name,f.name as school_name,e.name as status_name,c.name as category_name, d.name as subject_name"+
					 " FROM eip.Grade a,eip.teacher b,eip.school f,eip.FlowStatus e,eip.category c, eip.subject d"+
	                 " where a.teacher_id = b.teacher_seq and a.school_code = f.code and a.status_code=e.code and a.category_id = c.category_seq and a.subject_id = d.subject_seq";
		if (grade_seq != null && !grade_seq.isEmpty()) {
			sql += " and grade_seq="+grade_seq;
		}
		if (status_code != null && !status_code.isEmpty()) {
			sql += " and status_code in ("+status_code+")";
		}
		if (subject_id != null && !subject_id.isEmpty()) {
			sql += " and a.subject_id="+subject_id;
		}
		if (school_code != null && !school_code.isEmpty()) {
			sql += " and a.school_code='"+school_code+"'";
		}
		if (class_start_date_0 != null && !class_start_date_0.isEmpty()) {
			sql += " and a.class_start_date_0='"+class_start_date_0+"'";
		}	
		if (category_id != null && !category_id.isEmpty()) {
			sql += " and a.category_id="+category_id;
		}	
		if (teacher_id != null && !teacher_id.isEmpty()) {
			sql += " and a.teacher_id="+teacher_id;
		}
		
		if (disable != null && !disable.isEmpty()) {
			sql += " and a.disable="+disable;
		}		

		if (notIn != null && !notIn.isEmpty()) {
			sql += " and a.grade_seq not in ("+notIn+")";
		}
		
		if (onlyOnline != null && onlyOnline.equals("1")) {
			sql += " and a.grade_seq in (select distinct grade_id from eip.classes where class_style=3)";
		}		
		
		if(orderBy!=null && !orderBy.isEmpty()) {
			sql +=" order by "+orderBy;
		}else {
			sql +=" order by a.grade_seq desc";
		}	
		
		if (limit != null && !limit.isEmpty()) {
			sql += " LIMIT "+limit;
		}
	
		  List<Grade> LGrade = jdbcTemplate.query(sql,(result,rowNum)->new Grade(
		    		result.getString("grade_seq"),
		    		result.getString("school_code"),
		    		result.getString("school_name"),
		    		result.getString("category_id"),
		    		result.getString("category_name"),
		    		result.getString("subject_id"),
		    		result.getString("subject_name"),
		    		"",
		    		result.getString("status_code"),
		    		result.getString("status_name"),
		    		result.getString("gradeName"),
		    		result.getString("class_no"),
		    		result.getString("class_makeup"),
	                result.getString("class_style"),
	                result.getString("lower_limit"),
	                result.getString("upper_limit"),
	                result.getString("teacher_id"),
	                result.getString("teacher_name"),
	                "",
	                result.getString("class_start_date_0"),
	                "", //class_start_date(新期別格式)
	                "",
	                result.getString("slot_id"),
	                "",
	                result.getString("timeFrom"),
	                result.getString("timeTo"),
	                result.getString("video_date"),
	                "",
	                null,
	                result.getString("creater"),
	                result.getString("update_time"),
	                "", // attendStatus
	                "", // attendStatusV
	                result.getString("onSell"),
	                result.getString("offSell"),
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                result.getString("grade_date"),
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                result.getString("haveRead"),
	                result.getString("disable"),
	                result.getString("videoPreserve")
	          ));
		  String lastClassDate = "";
			for(int i=0;i<LGrade.size();i++) {
				if(LGrade.get(i).getClass_start_date_0().length()==10) { //ex.07/24/2019				
					LGrade.get(i).setClass_start_date(LGrade.get(i).getClass_start_date_0().substring(8,10)+LGrade.get(i).getClass_start_date_0().substring(0,2)+LGrade.get(i).getClass_start_date_0().substring(3,5));
				}
				
				if(showLastClassDate.equals("1")) {
					try {
						lastClassDate  = jdbcTemplate.queryForObject("select class_date from eip.classes where grade_id="+LGrade.get(i).getGrade_seq()+" and class_date is not null and class_date !='' order by class_th desc limit 1", String.class);
					}catch (Exception e) {
						lastClassDate = "";
		            }	
					LGrade.get(i).setLastClassDate(lastClassDate);
				}				
			}
			

		  return LGrade;
	}
	
	public List<Grade> getGrade2(String grade_seq,String status_code,String subject_id,String school_code,String class_start_date_0,String category_id,String teacher_id,String orderBy,String like_class_date_final){
		String sql = "select h.class_date as class_date_start,g.class_date as class_date_final,a.*,b.name as teacher_name,f.name as school_name,e.name as status_name,c.name as category_name, d.name as subject_name"+
					 " FROM eip.Grade a,eip.teacher b,eip.school f,eip.FlowStatus e,eip.category c,eip.subject d,eip.classes g,eip.classes h"+
	                 " where h.class_th=1 and h.grade_id=a.grade_seq and g.grade_id=a.grade_seq and g.class_th=a.class_no and a.teacher_id = b.teacher_seq and a.school_code = f.code and a.status_code=e.code and a.category_id = c.category_seq and a.subject_id = d.subject_seq";
		if (grade_seq != null && !grade_seq.isEmpty()) {
			sql += " and grade_seq="+grade_seq;
		}
		if (status_code != null && !status_code.isEmpty()) {
			sql += " and status_code in ("+status_code+")";
		}
		if (subject_id != null && !subject_id.isEmpty()) {
			sql += " and a.subject_id="+subject_id;
		}
		if (school_code != null && !school_code.isEmpty()) {
			sql += " and a.school_code='"+school_code+"'";
		}
		if (class_start_date_0 != null && !class_start_date_0.isEmpty()) {
			sql += " and a.class_start_date_0='"+class_start_date_0+"'";
		}	
		if (category_id != null && !category_id.isEmpty()) {
			sql += " and a.category_id="+category_id;
		}	
		if (teacher_id != null && !teacher_id.isEmpty()) {
			sql += " and a.teacher_id="+teacher_id;
		}
		if (like_class_date_final != null && !like_class_date_final.isEmpty()) {
			sql += " and g.class_date  like '"+like_class_date_final+"'";
		}		
		
		if(orderBy != null && !orderBy.isEmpty()) {
			sql += " order by "+orderBy;
		}

		  List<Grade> LGrade = jdbcTemplate.query(sql,(result,rowNum)->new Grade(
		    		result.getString("grade_seq"),
		    		result.getString("school_code"),
		    		result.getString("school_name"),
		    		result.getString("category_id"),
		    		result.getString("category_name"),
		    		result.getString("subject_id"),
		    		result.getString("subject_name"),
		    		"",
		    		result.getString("status_code"),
		    		result.getString("status_name"),
		    		result.getString("gradeName"),
		    		result.getString("class_no"),
		    		result.getString("class_makeup"),
	                result.getString("class_style"),
	                result.getString("lower_limit"),
	                result.getString("upper_limit"),
	                result.getString("teacher_id"),
	                result.getString("teacher_name"),
	                "",
	                result.getString("class_start_date_0"),
	                "", //class_start_date(新期別格式)
	                "",
	                result.getString("slot_id"),
	                "",
	                result.getString("timeFrom"),
	                result.getString("timeTo"),
	                result.getString("video_date"),
	                "",
	                null,
	                result.getString("creater"),
	                result.getString("update_time"),
	                "", // attendStatus
	                "", // attendStatusV
	                result.getString("onSell"),
	                result.getString("offSell"),
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                result.getString("grade_date"),
	                result.getString("class_date_final"),
	                result.getString("class_date_start"),
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                "",
	                result.getString("haveRead"),
	                result.getString("disable"),
	                result.getString("videoPreserve")
	          ));
		  
		  String sql_x="";
		  
		  for(int i=0;i<LGrade.size();i++) { 
			  int gradeNo = 0; //正班人數
			  int gradeFreeNo = 0; //贈課人數
			  int gradeFee = 0; //正班收入
			  int gradeFreeFee = 0; //贈課費用
			  int gradeNo_v = 0; 
			  int gradeFreeNo_v = 0;
			  int gradeFee_v = 0; 
			  int gradeFreeFee_v = 0; 
			  String gradeNoRatioStr = "()";
			  
				sql_x = "select a.costShare,b.freeChoice,d.price as subject_price,c.class_style"+
						" from eip.Register_comboSale a left join eip.FreeClass b on a.Register_comboSale_seq=b.Register_comboSale_id,eip.Register_comboSale_grade c,eip.subject d"+
						" where d.subject_seq=a.subject_id and c.Register_comboSale_id=a.Register_comboSale_seq and c.register_status=1 and c.active=1 and c.grade_id="+LGrade.get(i).getGrade_seq(); 			
			
				List<GradeCost> LGradeCost = jdbcTemplate.query(sql_x,(result,rowNum)->new GradeCost(
			    		result.getString("costShare"),
			    		result.getString("freeChoice"),
			    		result.getString("subject_price"),
			    		result.getString("class_style")
		        ));
				
				
				
				for(int j=0;j<LGradeCost.size();j++) {
					
					//******處理多班別數********//
					float gradeNoRatio = 1;
					List<SubjectTeacher> LSubjectTeacher =  getSubjectTeacher2("",LGrade.get(i).getTeacher_id(),LGrade.get(i).getSubject_id(),"","1");
					if(LSubjectTeacher.size()==0) { //此科目此老師未設定
						gradeNoRatioStr = "(?)";
					}else if(LSubjectTeacher.size()>0 && LSubjectTeacher.get(0).getGradeNo().equals("1")) { //此科目此老師只有一門課
						gradeNoRatio = 1;
						gradeNoRatioStr = "1";			
					}else{
							List<GradeClassNo> LGradeClassNo = getGradeClassNo("",LGrade.get(i).getSubject_id(),LGrade.get(i).getTeacher_id(),LGrade.get(i).getGradeName());
							if(LGradeClassNo.size()>0) {
								String totalClassNo = LGradeClassNo.get(0).getTotalClassNo();
								String gradeClassNo = LGradeClassNo.get(0).getGradeClassNo();			
								if(totalClassNo!=null && !totalClassNo.isEmpty() && gradeClassNo!=null && !totalClassNo.isEmpty()) {
									gradeNoRatio = Float.valueOf(gradeClassNo)/Float.valueOf(totalClassNo);
									gradeNoRatioStr = "("+gradeClassNo+"/"+totalClassNo+")";
								}
							}else {
								gradeNoRatioStr = "(?)";
							}
					}					
					
					
					
					//正班
					if(LGradeCost.get(j).getClass_style().equals("1")) {
						gradeNo +=1;
						gradeFee += Integer.valueOf(LGradeCost.get(j).getCostShare())*gradeNoRatio;				
						if(LGradeCost.get(j).getFreeChoice()!=null && LGradeCost.get(j).getFreeChoice().equals("2")) {
							gradeFreeNo += 1;
							gradeFreeFee += Integer.valueOf(LGradeCost.get(j).getSubject_price());
						}
					//Video班
					}else if(LGradeCost.get(j).getClass_style().equals("2")) {
						gradeNo_v +=1;
						gradeFee_v += Integer.valueOf(LGradeCost.get(j).getCostShare())*gradeNoRatio; //此次報名各subject平攤後				
						if(LGradeCost.get(j).getFreeChoice()!=null && LGradeCost.get(j).getFreeChoice().equals("2")) { //補償贈課
							gradeFreeNo_v += 1;
							gradeFreeFee_v += Integer.valueOf(LGradeCost.get(j).getSubject_price()); //補償贈課,此次報名各subject原價
						}
					}	
				}
				
				//多班別占比
				LGrade.get(i).setGradeNoRatioStr(gradeNoRatioStr);
				//正班總人數
				LGrade.get(i).setGradeNo(String.valueOf(gradeNo));				
				//正班贈課人數
				LGrade.get(i).setGradeFreeNo(String.valueOf(gradeFreeNo));
				//正班總收入
				LGrade.get(i).setGradeFee(String.valueOf(gradeFee));
				//正班贈課費用
				LGrade.get(i).setGradeFreeFee(String.valueOf(gradeFreeFee));
				
				//Video總人數
				LGrade.get(i).setGradeNo_v(String.valueOf(gradeNo_v));				
				//Video贈課人數
				LGrade.get(i).setGradeFreeNo_v(String.valueOf(gradeFreeNo_v));
				//Video總收入
				LGrade.get(i).setGradeFee_v(String.valueOf(gradeFee_v));
				//Video贈課費用
				LGrade.get(i).setGradeFreeFee_v(String.valueOf(gradeFreeFee_v));				
			}
			
		  return LGrade;
	}

	public int getGrade3(String school_code,String category_id,String subject_id,String teacher_id,String slot_id,String gradeName,String class_start_date_0,String class_style){
		String sql ="select count(*) from eip.grade where status_code!=7 and (disable is null or disable=0)";
		if (school_code != null && !school_code.isEmpty()) { sql += " and school_code='"+school_code+"'";}
		if (category_id != null && !category_id.isEmpty()) { sql += " and category_id="+category_id;}
		if (subject_id != null && !subject_id.isEmpty()) { sql += " and subject_id="+subject_id;}
		if (teacher_id != null && !teacher_id.isEmpty()) { sql += " and teacher_id="+teacher_id;}
		if (slot_id != null && !slot_id.isEmpty()) { sql += " and slot_id="+slot_id;}
		if (gradeName != null && !gradeName.isEmpty()) { sql += " and gradeName='"+gradeName+"'";}
		if (class_start_date_0 != null && !class_start_date_0.isEmpty()) { sql += " and class_start_date_0='"+class_start_date_0+"'";}
		if (class_style != null && !class_style.isEmpty()) { sql += " and class_style="+class_style;}

		int x  = jdbcTemplate.queryForObject(sql, Integer.class);

		return x;
	}
	
	public List<Classes> getClasses(String grade_id,String teacher_id,String class_date,String school_code,String category_id,String status_code_In,String class_th,String subject_id,String class_date_like,String class_date_like2){ 
		String sql = //"select a.*,Concat(b.name,c.gradeName) as subject_name,c.class_no,c.class_start_date_0,c.school_code,d.name as teacher_name,e.bgColor,f.name as statusName"+
				"select a.*,b.name as subject_name,c.gradeName,c.class_no,c.class_start_date_0,c.school_code,d.name as teacher_name,e.bgColor,f.name as statusName"+
	                 " from eip.classes a left join teacher d on a.teacher_id=d.teacher_seq,subject b,grade c,category e,FlowStatus f"+
	                 " where c.grade_seq=a.grade_id and c.subject_id=b.subject_seq and c.category_id=e.category_seq and c.status_code=f.code";
		
		if (grade_id != null && !grade_id.isEmpty()) { sql += " and c.grade_seq="+grade_id;}
		if (teacher_id != null && !teacher_id.isEmpty()) { sql += " and a.teacher_id="+teacher_id;}
		if (class_date != null && !class_date.isEmpty()) { sql += " and a.class_date='"+class_date+"'";}
		if (school_code != null && !school_code.isEmpty()) { sql += " and c.school_code='"+school_code+"'";}
		if (category_id != null && !category_id.isEmpty()) { sql += " and c.category_id="+category_id;}
		if (subject_id != null && !subject_id.isEmpty()) { sql += " and c.subject_id="+subject_id;}
		if (status_code_In != null && !status_code_In.isEmpty()) { sql += " and c.status_code in "+status_code_In;}
		if (class_th != null && !class_th.isEmpty()) { sql += " and a.class_th="+class_th;}
		if (class_date_like != null && !class_date_like.isEmpty() && class_date_like2.isEmpty()) { sql += " and a.class_date like '"+class_date_like+"'";}
		if (class_date_like2 != null && !class_date_like2.isEmpty()) { sql += " and (a.class_date like '"+class_date_like+"' or a.class_date like '"+class_date_like2+"')";}
		sql +=" and c.status_code!=7 order by c.class_start_date_0, a.class_th_seq";
	
	
		List<Classes> LClasses = jdbcTemplate.query(sql,(result,rowNum)->new Classes(
	    		result.getString("class_seq"),
	    		result.getString("school_code"),
	    		"",
	    		result.getString("grade_id"),
	    		result.getString("class_th"),
	    		result.getString("class_date"),
                result.getString("time_from"),
                result.getString("time_to"),
                result.getString("teacher_id"),
                result.getString("teacher_name"),
                result.getString("class_style"),
                result.getString("class_name"),
                result.getString("class_remark"),
                result.getString("class_remarkAdm"),
                result.getString("subject_name"),
                result.getString("class_no"),
                result.getString("class_start_date_0"),
                result.getString("slot_id"),
                result.getString("bgColor"),
                result.getString("statusName"),
                result.getString("class_trial"),
                "",
                "",
                result.getString("classRoom"),
                result.getString("class_th_seq"),
                result.getString("gradeName")
             ));
	
		String classReceiveHidden = null;
		String classReceiveTextarea = null;
		for(int i=0;i<LClasses.size();i++) {
			if(LClasses.get(i).getClass_start_date().length()==10) { //ex.07/24/2019				
				LClasses.get(i).setClass_start_date(LClasses.get(i).getClass_start_date().substring(8,10)+LClasses.get(i).getClass_start_date().substring(0,2)+LClasses.get(i).getClass_start_date().substring(3,5));
			}
			LClasses.get(i).setSchool_name(accountService.getSchool(LClasses.get(i).getSchool_code(),"").get(0).getName());
			if(getTeacher(LClasses.get(i).getTeacher_id(),"","","","","","").size()>0) {
				LClasses.get(i).setTeacher_name(getTeacher(LClasses.get(i).getTeacher_id(),"","","","","","").get(0).getName());
			}
			classReceiveHidden = "";
			classReceiveTextarea = "";
			List<ClassesMaterial> LClassesMaterial = getClassesMaterial(LClasses.get(i).getClass_seq());

			for(int a=0;a<LClassesMaterial.size();a++) {
				if(LClassesMaterial.get(a).getMaterial_id().equals("1")) {
					classReceiveHidden += "@1#"+LClassesMaterial.get(a).getBook_id();
					classReceiveTextarea += "[書籍] "+LClassesMaterial.get(a).getBookName()+"\n";					
				}else if(LClassesMaterial.get(a).getMaterial_id().equals("2")) {
					classReceiveHidden += "@2#"+LClassesMaterial.get(a).getInputTex();
					classReceiveTextarea += "[講義] "+LClassesMaterial.get(a).getInputTex()+"\n";					
				}
			}
			LClasses.get(i).setClassReceiveHidden(classReceiveHidden);
			LClasses.get(i).setClassReceiveTextarea(classReceiveTextarea);
		}		
		return LClasses;
	}	
	public int saveClasses(Classes classes) {
		jdbcTemplate.update("INSERT INTO eip.classes VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
						classes.getGrade_id(),
						classes.getClass_th(),
						classes.getClass_date(),
						classes.getTime_from(),
						classes.getTime_to(),
						classes.getTeacher_id(),
						classes.getClass_style(),
						classes.getClass_name(),
						classes.getClass_remark(),
						classes.getClass_remarkAdm(),
						classes.getSlot_id(),
						classes.getClass_trial(),
						classes.getClassRoom(),
						classes.getClass_th_seq()
		);
		
		int x  = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);			
		return x;
	}
	
	public Boolean updateSignRecordHistory(String grade_seq,Classes classes_new,String class_style,String updater,Boolean exist,String class_th_seq) {
		//找出所有訂此期別的學生紀錄, 所有堂數, 所有學生
		String sql = "select * from eip.signRecordHistory where grade_id="+grade_seq+" and class_th_seq=1";			
		List<SignRecordHistory> LSignRecordHistory_thisGrade = jdbcTemplate.query(sql,(result,rowNum)->new SignRecordHistory(
	    		"",
	    		result.getString("Register_comboSale_grade_id"),
	    		result.getString("register_status"),
	    		result.getString("student_id"),
	    		"",
	    		"",
	    		"",
	    		result.getString("school_code"),
	    		"",
	    		result.getString("grade_id"),
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		result.getString("allowAttend"),
	    		"",
	    		result.getString("class_th_seq")
        ));	
		
		//所有學生->更新
		if(exist){
			jdbcTemplate.update("Update eip.signRecordHistory set class_th=?,classroom=?,comment=?,updater=?,update_time=CURDATE() where grade_id=? and class_th_seq=?;",
					classes_new.getClass_th(),
					classes_new.getClassRoom(),
					classes_new.getClass_remark(),
					updater,
					grade_seq,
					class_th_seq	
			);
			
		//所有訂期別學生->新增 
		}else{
			for(int x=0;x<LSignRecordHistory_thisGrade.size();x++) {
				Register_comboSale_grade register_comboSale_grade = getRegister_comboSale_grade("",LSignRecordHistory_thisGrade.get(x).getRegister_comboSale_grade_id(),"","").get(0);
				String class_style0 = register_comboSale_grade.getClass_style();
				if(class_style0.equals("0")) {class_style0 = "-99";}
				
				String allowAttend = "0"; //不可進班
				List<Register> LRegister = salesService.getRegister3(LSignRecordHistory_thisGrade.get(x).getRegister_comboSale_grade_id());
				if(LRegister.get(0).getOrderStatus().equals("2")) { //結清
					allowAttend = "1"; //可進班
				}
				jdbcTemplate.update("INSERT INTO eip.signRecordHistory VALUES (default,?,?,?,?,?,?,?,?,?,?,?,CURDATE(),?,?,?,?,?,?,?,?);",
						LSignRecordHistory_thisGrade.get(x).getRegister_comboSale_grade_id(),
						LSignRecordHistory_thisGrade.get(x).getRegister_status(),
						LSignRecordHistory_thisGrade.get(x).getStudent_id(),
						class_style0,
						LSignRecordHistory_thisGrade.get(x).getSchool_code(),
						LSignRecordHistory_thisGrade.get(x).getGrade_id(),
						classes_new.getClass_th(),
						0,
						1,
						"",
						-99, //slot
						updater,
						0,
						"",
						"",
						"",
						"",
						allowAttend,
						classes_new.getClass_th_seq()
				);
			}	
		}	
		return true;
		
	}
	
	public Boolean saveVideoOpen(VideoOpen videoOpen) {
		jdbcTemplate.update("INSERT INTO eip.videoOpen VALUES (default,?,?);",
				        videoOpen.getGrade_id(),
				        videoOpen.getSchoolCode()
				    );
		return true;
	}	
	public String getClassStartDate(String grade_id) {
		String sql = "select class_date from eip.classes where class_th=1 and grade_id=?";		
		String min_class_date = (String) jdbcTemplate.queryForObject(sql, new Object[] { grade_id }, String.class);
		return min_class_date;
	}
	
	public Boolean UpdateGradeStatus(String grade_seq,String Status_code) {
		jdbcTemplate.update("Update eip.grade set status_code =?,update_time=CURDATE() where grade_seq=?;",
				Status_code,
				grade_seq		
		);
		return true;
	}
	
	public Boolean UpdateGradeStatus2(String teacher_id,String school_code,String category_id,String grade_seq,String Status_code) {
		if(category_id!=null && !category_id.isEmpty() && grade_seq.equals("-1")) {
			List<Grade> LGrade = getGradeList(teacher_id,"","",school_code,category_id,"","","","","","","","","","","","","1","");			
			//status_code: 1,'暫存' 2,'送審' 6,'退件' 7,'刪除' 8,'Video下架'
			for(int i=0;i<LGrade.size();i++) {
				jdbcTemplate.update("Update eip.grade set status_code=?,update_time=CURDATE() where teacher_id=? and status_code not in (1,2,6,7,8) and status_code!=? and grade_seq=?;",
						Status_code,
						teacher_id,
						Status_code,
						LGrade.get(i).getGrade_seq()		
				);				
			}
		}else if(grade_seq!=null && !grade_seq.isEmpty()){
			jdbcTemplate.update("Update eip.grade set status_code=?,update_time=CURDATE() where teacher_id=? and status_code!=? and grade_seq=?;",
					Status_code,
					teacher_id,
					Status_code,
					grade_seq		
			);
		}	
		return true;
	}
	
	public Boolean updateClassesComment(String grade_seq,int class_th,String class_remark,String class_remarkAdm,String classRoom) {
		jdbcTemplate.update("Update eip.classes set class_remark =?,class_remarkAdm =?,classRoom=? where grade_id=? and class_th=?;",
				class_remark,
				class_remarkAdm,
				classRoom,
				grade_seq,	
				class_th
		);
		return true;		
	}
	
	public List<VideoOpen> getVideoOPen(String grade_id){
		
		String sql = "SELECT * FROM eip.videoOpen where grade_id ="+grade_id;			
		List<VideoOpen> LVideoOpen = jdbcTemplate.query(sql,(result,rowNum)->new VideoOpen(
	    		result.getString("videoOpen_seq"),
	    		result.getString("grade_id"),
	    		result.getString("schoolCode")
                ));
		return LVideoOpen;		
	}

	public String SubjectEditSave(Subject subject,String subject_seq,String[] A_selVirtualSubject_id,Subject_R subject_R1,Subject_R subject_R3) {
 //新增或進版 
		//old version subject
		if(subject_seq!=null && !subject_seq.equals("")) {			
			jdbcTemplate.update("UPDATE eip.subject SET active=0 WHERE subject_seq="+subject_seq);
			subject.setParent_seq(subject_seq);
		}else {
	    //new subject		
			subject.setParent_seq("");
		}
		
		jdbcTemplate.update("INSERT INTO eip.subject VALUES (default,?,?,?,?,?,?,?,?,?,CURDATE(),?,?,?,?)",
					subject.getCategory_id(),
					subject.getName(),
					subject.getAbbr(),
					subject.getPrice(),
					subject.getRemark(),
					subject.getIsVideo(),
					subject.getIsVirtual(),
					subject.getParent_seq(),
					subject.getUpdater(),
					subject.getActive(),
					subject.getSubjectContent_code(),
					subject.getFree_makeUpNo(),
					"0"
		);
		subject_seq  = String.valueOf(jdbcTemplate.queryForObject("select last_insert_id()", Integer.class));

		
		if(A_selVirtualSubject_id!=null && subject.getIsVirtual().equals("1")) {
			for(int i=0;i<A_selVirtualSubject_id.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.virtualSubject VALUES (default,?,?)",
						subject_seq,
						A_selVirtualSubject_id[i]
				);				
			}				
		}
		//分攤比率預設值
			//班內
			List<Subject_R> LSubject_R1_or = getSubjectWithR_original("-1","1");
			Subject_R subject_R1_or = new Subject_R();
			if(LSubject_R1_or.size()>0) {
				subject_R1_or = LSubject_R1_or.get(0);
			}		
			insertSubject_R("1",subject_seq,subject_R1_or);
	
			//線上
			List<Subject_R> LSubject_R3_or = getSubjectWithR_original("-1","3");
			Subject_R subject_R3_or = new Subject_R();
			if(LSubject_R3_or.size()>0) {
				subject_R3_or = LSubject_R3_or.get(0);
			}		
			insertSubject_R("3",subject_seq,subject_R3_or);
		
		//分攤比率修改
		if(subject_R1.getHrPrice_R()!=null && !subject_R1.getHrPrice_R().isEmpty()) {
			jdbcTemplate.update("DELETE from eip.subject_R where subject_id=?",
					subject_seq
			);
			if(!subject_R1.getHrPrice_R().isEmpty()) {
				insertSubject_R("1",subject_seq,subject_R1);
			}
			if(!subject_R3.getHrPrice_R().isEmpty()) {
				insertSubject_R("3",subject_seq,subject_R3);
			}		
		}
		
		
		return subject_seq;
	}
	
	public Boolean SubjectUpdateSave(Subject subject,String subject_seq,String[] A_selVirtualSubject_id,Subject_R subject_R1,Subject_R subject_R3) {
		//舊版本先存
		 Subject subject_or = getSubject("",subject_seq,"","","","0").get(0);
			jdbcTemplate.update("INSERT INTO eip.subject_ver VALUES (?,?,?,?,?,?,?,?,?,?,CURDATE(),?,?,?)",
					subject_or.getSubject_seq(),
					subject_or.getCategory_id(),
					subject_or.getName(),
					subject_or.getAbbr(),
					subject_or.getPrice(),
					subject_or.getRemark(),
					subject_or.getIsVideo(),
					subject_or.getIsVirtual(),
					subject_or.getParent_seq(),
					subject_or.getUpdater(),
					subject_or.getActive(),
					subject_or.getSubjectContent_code(),
					subject_or.getFree_makeUpNo()
		);		 
		//修改	
		jdbcTemplate.update("UPDATE eip.subject set category_id=?,name=?,abbr=?,price=?,remark=?,isVideo=?,isVirtual=?,parent_seq=?,updater=?,update_time=NOW(),active=?,subjectContent_code=?,free_makeUpNo=? where subject_seq=?;",
					subject.getCategory_id(),
					subject.getName(),
					subject.getAbbr(),
					subject.getPrice(),
					subject.getRemark(),
					subject.getIsVideo(),
					subject.getIsVirtual(),
					subject.getParent_seq(),
					subject.getUpdater(),
					subject.getActive(),
					subject.getSubjectContent_code(),
					subject.getFree_makeUpNo(),
					subject_seq
		);
				
		if(!subject_seq.isEmpty()) {
			jdbcTemplate.update("DELETE from eip.virtualSubject where subject_id=?",
					subject_seq
			);
			if(A_selVirtualSubject_id!=null && subject.getIsVirtual().equals("1")) {
				for(int i=0;i<A_selVirtualSubject_id.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.virtualSubject VALUES (default,?,?)",
							subject_seq,
							A_selVirtualSubject_id[i]
					);				
				}
			}
			//分攤比率
			if(subject_R1.getHrPrice_R()!=null && !subject_R1.getHrPrice_R().isEmpty()) {
				jdbcTemplate.update("DELETE from eip.subject_R where subject_id=?",
						subject_seq
				);			
				if(!subject_R1.getHrPrice_R().isEmpty()) {
					insertSubject_R("1",subject_seq,subject_R1);
				}
				if(!subject_R3.getHrPrice_R().isEmpty()) {
					insertSubject_R("3",subject_seq,subject_R3);
				}
			}	
		}			
		return true;
	}
	
	public Boolean insertSubject_R(String class_style,String subject_id,Subject_R subject_R) {
		jdbcTemplate.update("INSERT INTO eip.subject_R VALUES (default,?,?,?,?,?,?,?,?)",
				subject_id,
				class_style,
				subject_R.getHrPrice_R(),
				subject_R.getCounselingPrice_R(),
				subject_R.getLagnappePrice_R(),
				subject_R.getHandoutPrice_R(),	
				subject_R.getHomeworkPrice_R(),
				subject_R.getMockPrice_R()
		);
		return true;		
	}
	
		
	public Boolean SubjectUpdateTmpSave(Subject subject,String[] A_selVirtualSubject_id,Subject_R subject_R1,Subject_R subject_R3,String schedule_time) {
		
		//排定修改
		jdbcTemplate.update("DELETE from eip.subject_tmp where subject_id=?",
				subject.getSubject_seq()
		);
		jdbcTemplate.update("INSERT INTO eip.subject_tmp VALUES (?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?)",
				schedule_time,
				subject.getSubject_seq(),
				subject.getCategory_id(),
				subject.getName(),
				subject.getAbbr(),
				subject.getPrice(),
				subject.getRemark(),
				subject.getIsVideo(),
				subject.getIsVirtual(),
				subject.getParent_seq(),
				subject.getUpdater(),
				subject.getActive(),
				subject.getSubjectContent_code()
		);		
				
		//自由選
		if(A_selVirtualSubject_id!=null && subject.getIsVirtual().equals("1")) {
			jdbcTemplate.update("DELETE from eip.virtualSubject_tmp where subject_id=?",
					subject.getSubject_seq()
			);			
			for(int i=0;i<A_selVirtualSubject_id.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.virtualSubject_tmp VALUES (?,?,?)",
						schedule_time,
						subject.getSubject_seq(),
						A_selVirtualSubject_id[i]
				);				
			}
		}
			//分攤比率
			/**
			if(subject_R1.getHrPrice_R()!=null && !subject_R1.getHrPrice_R().isEmpty()) {
				jdbcTemplate.update("DELETE from eip.subject_R where subject_id=?",
						subject_seq
				);			
				if(!subject_R1.getHrPrice_R().isEmpty()) {
					insertSubject_R("1",subject_seq,subject_R1);
				}
				if(!subject_R3.getHrPrice_R().isEmpty()) {
					insertSubject_R("3",subject_seq,subject_R3);
				}
			}
			**/	
		
		return true;
	}	
	
	public Boolean teacherAndGradeEditUpdate(SubjectTeacher subjectTeacher,String[] A_pre_grade_seq,String[] A_pre_grade_remark,String[] A_gradeName,String[] A_gradeClassNo) {
		String isPreGrade = "0";
		if(subjectTeacher.getIsPreGrade().equals("1") && A_pre_grade_seq!=null && A_pre_grade_seq.length>0) {isPreGrade="1";}

		jdbcTemplate.update("UPDATE eip.subjectTeacher set subject_id=?,teacher_id=?,gradeNo=?,gradeNoATime=?,isPreGrade=?,updater=?,update_time=NOW(),totalClassNo=? where subjectTeacher_seq=?;",
				subjectTeacher.getSubject_id(),
				subjectTeacher.getTeacher_id(),
				subjectTeacher.getGradeNo(),
				subjectTeacher.getGradeNoATime(),
				isPreGrade,
				subjectTeacher.getUpdater(),
				subjectTeacher.getTotalClassNo(),
				subjectTeacher.getSubjectTeacher_seq()				
		);
		
		jdbcTemplate.update("DELETE from eip.pre_grade where subjectTeacher_id=?",
				subjectTeacher.getSubjectTeacher_seq()
		);
		jdbcTemplate.update("DELETE from eip.gradeClassNo where subjectTeacher_id=?",
				subjectTeacher.getSubjectTeacher_seq()
		);		
		
		if(A_pre_grade_seq!=null && A_pre_grade_remark!=null && isPreGrade.equals("1")) {
			for(int i=0;i<A_pre_grade_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.pre_grade VALUES (default,?,?,?)",
						subjectTeacher.getSubjectTeacher_seq(),
						A_pre_grade_seq[i],
						A_pre_grade_remark[i]
				);				
			}
		}	
			
		if(A_gradeName!=null && A_gradeClassNo!=null) {
			for(int i=0;i<A_gradeName.length;i++) {
				if(!A_gradeName[i].isEmpty() && !A_gradeClassNo[i].isEmpty()) {
					jdbcTemplate.update("INSERT INTO eip.gradeClassNo VALUES (default,?,?,?)",
							subjectTeacher.getSubjectTeacher_seq(),
							A_gradeName[i],
							A_gradeClassNo[i]
					);
				}	
			}
		}			
		

		return true;
	}
	
	public Boolean teacherAndGradeEditInsert(SubjectTeacher subjectTeacher,String[] A_pre_grade_seq,String[] A_pre_grade_remark,String[] A_gradeName,String[] A_gradeClassNo) {
		String isPreGrade = "0";
		if(A_pre_grade_seq!=null && A_pre_grade_seq.length>0) {isPreGrade="1";}

		jdbcTemplate.update("INSERT INTO eip.subjectTeacher VALUES (default,?,?,?,?,?,?,?,NOW(),?)",
				subjectTeacher.getSubject_id(),
				subjectTeacher.getTeacher_id(),
				subjectTeacher.getGradeNo(),
				subjectTeacher.getGradeNoATime(),
				isPreGrade,
				subjectTeacher.getTotalClassNo(),
				subjectTeacher.getUpdater(),
				1 //active
		);
		int x  = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);	
		
			if(A_pre_grade_seq!=null && A_pre_grade_remark!=null) {
				for(int i=0;i<A_pre_grade_seq.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.pre_grade VALUES (default,?,?,?)",
							x,
							A_pre_grade_seq[i],
							A_pre_grade_remark[i]
					);				
				}				
			}
			
			if(A_gradeName!=null && A_gradeClassNo!=null) {
				for(int i=0;i<A_gradeName.length;i++) {
					if(!A_gradeName[i].isEmpty() && !A_gradeClassNo[i].isEmpty()) {
						jdbcTemplate.update("INSERT INTO eip.gradeClassNo VALUES (default,?,?,?)",
								x,
								A_gradeName[i],
								A_gradeClassNo[i]
						);
					}
				}
			}	
		return true;
	}	

	public List<Register_comboSale_grade> getRegister_comboSale_grade(String Register_comboSale_id,String Register_comboSale_grade_seq,String in_register_status,String active){
		
		String sql = "SELECT a.*,b.name as school_name FROM eip.Register_comboSale_grade a, school b where a.school_code=b.code";	//#訂班狀態 1:訂班
		if (in_register_status != null && !in_register_status.isEmpty()) {sql += " and register_status in "+in_register_status;}
		if (Register_comboSale_id != null && !Register_comboSale_id.isEmpty()) {sql += " and Register_comboSale_id="+Register_comboSale_id;}
		if (Register_comboSale_grade_seq != null && !Register_comboSale_grade_seq.isEmpty()) {sql += " and Register_comboSale_grade_seq="+Register_comboSale_grade_seq;}
		if (active!= null && !active.isEmpty()) {sql += " and a.active ="+active;}
		//sql +=" order by a.Register_comboSale_id desc";

		List<Register_comboSale_grade> LRegister_comboSale_grade = jdbcTemplate.query(sql,(result,rowNum)->new Register_comboSale_grade(
	    		result.getString("Register_comboSale_grade_seq"),
	    		result.getString("Register_comboSale_id"),
	    		result.getString("grade_id"),
	    		result.getString("register_status"),
	    		result.getString("class_style"),
	    		result.getString("school_code"),
	    		result.getString("school_name"),
	    		result.getString("sitNo"),
	    		result.getString("active"),
	    		result.getString("updater"),
	    		result.getString("update_time")
        ));
		return LRegister_comboSale_grade;		
	}
	
	public boolean MockEditSave(String mock_seq,String category_id,String mock_name,String[] A_totalNo,String[] A_noName,String[] A_testStyle,String[] A_testMethod,String[] A_subject_id,String original_price,String active) {
        //修改
		if(mock_seq != null && !mock_seq.isEmpty()) {
			jdbcTemplate.update("update eip.mock set category_id=?,mock_name=?,original_price=?,active=? where mock_seq=?",
					category_id,
					mock_name,
					original_price,
					active,
					mock_seq					
		    );
			//刪除現有模考
			jdbcTemplate.update("delete from eip.mockDetail where mock_id=?",
					mock_seq
		    );
			//刪除現有模考講解
			jdbcTemplate.update("delete from eip.mockVideo where mock_id=?",
					mock_seq
		    );
			
		//新增	
		}else {		 
			jdbcTemplate.update("INSERT INTO eip.mock VALUES (default,?,?,?,?);",
					category_id,
					mock_name,
					active,
					original_price
			);
			mock_seq  = String.valueOf(jdbcTemplate.queryForObject("select last_insert_id()", Integer.class));	
		}
		
		
		    //新增模考
			if(A_totalNo!=null) {
				for(int i=0;i<A_totalNo.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.mockDetail VALUES (default,?,?,?,?,?);",
							mock_seq,
							A_totalNo[i],
							A_noName[i],
							A_testStyle[i],
							A_testMethod[i]
				    );			
				}
			}
			//新增模考講解
			if(A_subject_id!=null) {
				for(int i=0;i<A_subject_id.length;i++) {
					jdbcTemplate.update("INSERT INTO eip.mockVideo VALUES (default,?,?);",
							mock_seq,
							A_subject_id[i]				
				    );			
				}
			}
			
		return true;
	}
	

	public List<Register_mockBook> getRegister_mockBook(String Register_seq,String mock_id,String mockDetail_id){ 
		String sql = "SELECT * from eip.Register_mockBook where 1=1";
		
		if (Register_seq != null && !Register_seq.isEmpty()) {sql += " and Register_seq="+Register_seq;}
		if (mock_id != null && !mock_id.isEmpty()) {sql += " and mock_id="+mock_id;}
		if (mockDetail_id != null && !mockDetail_id.isEmpty()) {sql += " and mockDetail_id="+mockDetail_id;}
		
		List<Register_mockBook> LRegister_mockBook = jdbcTemplate.query(sql,(result,rowNum)->new Register_mockBook(
	    		result.getString("register_mockDetail_seq"),
	    		result.getString("Register_seq"),
	    		result.getString("mock_id"),
                result.getString("mockDetail_id"),
                result.getString("mockSet_id"),
                result.getString("date_mockVideo"),
                result.getString("slot_mockVideo"),
                result.getString("school_mockVideo"),
                result.getString("attend")
                ));
		return LRegister_mockBook;
	}	

	public Boolean noClassSave(NoClass noClass) {
		if(
			noClass.getTeacher_id()!=null && !noClass.getTeacher_id().isEmpty() && 
			noClass.getDateSel()!=null && !noClass.getDateSel().isEmpty() && 
			noClass.getSlot_id()!=null && !noClass.getSlot_id().isEmpty()
		) {
			jdbcTemplate.update("DELETE from eip.noClass where teacher_id=? and dateSel=? and slot_id=?;",
					noClass.getTeacher_id(),
					noClass.getDateSel(),
					noClass.getSlot_id()
			);
			
			jdbcTemplate.update("INSERT INTO eip.noClass VALUES (default,?,?,?,?,?,?);",
					noClass.getTeacher_id(),
					noClass.getDateSel(),
					noClass.getTimeFrom(),
					noClass.getTimeTo(),
					noClass.getNoClassType(),
					noClass.getSlot_id()
			);
		}		
		return true;
	}
		
	
	public Boolean mockSetSave(String dateSel,String school_id,String category_id,String testMethod,String[] A_slot) {
		if(A_slot!=null) {
		  for(int i=0;i<A_slot.length;i++) {
			jdbcTemplate.update("INSERT INTO eip.mockSet VALUES (default,?,?,?,?,?,?);",
					category_id,
					school_id,
					testMethod,
					"", //noName
					A_slot[i],
					dateSel
			);
		  }
		}
		return true;
	}
	
	public Boolean subjectExchangeSave(String[] A_subject_id_1,String[] A_subject_id_2,String creater) {
		if(A_subject_id_1!=null && A_subject_id_2!=null) {
			jdbcTemplate.update("INSERT INTO eip.subjectExchange VALUES (default,?,NOW());",
					creater
			);
			int x  = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
			
		    for(int i=0;i<A_subject_id_1.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.subjectExchange1 VALUES (default,?,?);",
						x,
						A_subject_id_1[i]
				);
		    }
		    
		    for(int i=0;i<A_subject_id_2.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.subjectExchange2 VALUES (default,?,?);",
						x,
						A_subject_id_2[i]
				);
		    }		    
		}	
		return true;
	}
	
	
	public List<SubjectExchange> getSubjectExchange(){
		
		String sql = "SELECT * FROM eip.subjectExchange";			
		List<SubjectExchange> LSubjectExchange = jdbcTemplate.query(sql,(result,rowNum)->new SubjectExchange(
	    		result.getString("subjectExchange_seq"),
	    		result.getString("creater"),
	    		result.getString("update_time"),
	    		"",
	    		""
        ));
		for(int i=0;i<LSubjectExchange.size();i++) {
			List<SubjectExchange1> LSubjectExchange1= getSubjectExchange1(LSubjectExchange.get(i).getSubjectExchange_seq());
			String subject_1_str = "";
			for(int x=0;x<LSubjectExchange1.size();x++) {
				subject_1_str += "<div>"+LSubjectExchange1.get(x).getSubject_name()+"</div>";
			}
			LSubjectExchange.get(i).setSubject_1_str(subject_1_str);
			
			List<SubjectExchange2> LSubjectExchange2= getSubjectExchange2(LSubjectExchange.get(i).getSubjectExchange_seq());
			String subject_2_str = "";
			for(int x=0;x<LSubjectExchange2.size();x++) {
				subject_2_str += "<div>"+LSubjectExchange2.get(x).getSubject_name()+"</div>";
			}
			LSubjectExchange.get(i).setSubject_2_str(subject_2_str);			
		}
		return LSubjectExchange;		
	}
	
	public List<SubjectExchange1> getSubjectExchange1(String subjectExchange_id){
		
		String sql = "SELECT a.*,b.name as subject_name FROM eip.subjectExchange1 a,subject b where a.subject_id=b.subject_seq ";
		if (subjectExchange_id != null && !subjectExchange_id.isEmpty()) {sql += " and subjectExchange_id="+subjectExchange_id;}
		List<SubjectExchange1> LSubjectExchange1 = jdbcTemplate.query(sql,(result,rowNum)->new SubjectExchange1(
	    		result.getString("subjectExchange1_seq"),
	    		result.getString("subjectExchange_id"),
	    		result.getString("subject_id"),
	    		result.getString("subject_name")
        ));
		return LSubjectExchange1;		
	}	
	
	public List<SubjectExchange2> getSubjectExchange2(String subjectExchange_id){
		
		String sql = "SELECT a.*,b.name as subject_name FROM eip.subjectExchange2 a,subject b where a.subject_id=b.subject_seq ";
		if (subjectExchange_id != null && !subjectExchange_id.isEmpty()) {sql += " and subjectExchange_id="+subjectExchange_id;}
		List<SubjectExchange2> LSubjectExchange2 = jdbcTemplate.query(sql,(result,rowNum)->new SubjectExchange2(
	    		result.getString("subjectExchange2_seq"),
	    		result.getString("subjectExchange_id"),
	    		result.getString("subject_id"),
	    		result.getString("subject_name")
        ));
		return LSubjectExchange2;		
	}	
	
		
	public Boolean TeacherProfileSave(Teacher teacher,String creater,String[] A_teacher_seqSel) {	
		if(teacher.getTeacher_seq()!=null && !teacher.getTeacher_seq().isEmpty()) {
			  jdbcTemplate.update("UPDATE eip.teacher set code=?,name=?,realName=?,IDN=?,Tel=?,ADR=?,enabled=?,creater=?,update_time=NOW(),virtual_teacher=?,school_id=?,email=?,line=?,fb=?,email_contact=?,line_contact=?,fb_contact=?,tel_contact=?,face_contact=?,county=?,district=? where teacher_seq=?;",
					  teacher.getCode(),
					  teacher.getName(),
					  teacher.getRealName(),
					  teacher.getiDN(),
					  teacher.getTel(),
					  teacher.getAddress(),
					  teacher.getEnabled(),
					  creater,
					  teacher.getVirtual_teacher(),
					  teacher.getSchool_id(),
					  teacher.getEmail(),
					  teacher.getLine(),
					  teacher.getFb(),
					  teacher.getEmail_contact(),
					  teacher.getLine_contact(),
					  teacher.getFb_contact(),
					  teacher.getTel_contact(),
					  teacher.getFace_contact(),
					  teacher.getCounty(),
					  teacher.getDistrict(),						  
					  teacher.getTeacher_seq()			  
			      );			
		}else {
		  jdbcTemplate.update("INSERT INTO eip.teacher VALUES (default,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?,?,?);",
			  teacher.getCode(),
			  teacher.getName(),
			  teacher.getRealName(),
			  teacher.getiDN(),
			  teacher.getTel(),
			  teacher.getAddress(),
			  1,
			  creater,
			  teacher.getVirtual_teacher(),
			  teacher.getSchool_id(),
			  teacher.getEmail(),
			  teacher.getLine(),
			  teacher.getFb(),
			  teacher.getEmail_contact(),
			  teacher.getLine_contact(),
			  teacher.getFb_contact(),
			  teacher.getTel_contact(),
			  teacher.getFace_contact(),
			  teacher.getCounty(),
			  teacher.getDistrict()
	      );
		}
		
		if(teacher.getTeacher_seq()!=null && !teacher.getTeacher_seq().isEmpty()){
			
			jdbcTemplate.update("delete from eip.virtual_teacher where teacher_id_parent="+teacher.getTeacher_seq());
			if(A_teacher_seqSel!=null && A_teacher_seqSel.length>0) {			
				for(int i=0;i<A_teacher_seqSel.length;i++) {
					  jdbcTemplate.update("INSERT INTO eip.virtual_teacher VALUES (default,?,?);",
							  teacher.getTeacher_seq(),
							  A_teacher_seqSel[i]
					  );		  
				}
			}
		}	
		return true;
	}
	
	public Boolean scheduleTask(String recordsID,String scheduleName,String FlowStatus) {	
		 
		jdbcTemplate.update("INSERT INTO eip.scheduleTask VALUES (default,?,?,NOW(),?);",
			recordsID,
			scheduleName,
			FlowStatus
	    );
		return true;
	}
	
	public Boolean saveClassesMaterial(String class_id,String material_id,String book_id,String inputTex) {
		if(class_id!=null && !class_id.isEmpty()) {
			jdbcTemplate.update("delete from eip.classesMaterial where class_id='"+class_id+"'"); 
			jdbcTemplate.update("INSERT INTO eip.classesMaterial VALUES (default,?,?,?,?);",
					class_id,
					material_id,
					book_id,
					inputTex.equals("")?"講義":inputTex
		    );
		}	
		return true;
	}
	
	public List<ClassesMaterial> getClassesMaterial(String class_id){
		
		String sql = "SELECT a.*,b.bookName FROM eip.classesMaterial a left join eip.books b on a.book_id=b.books_seq where class_id ="+class_id;			
		List<ClassesMaterial> LClassesMaterial= jdbcTemplate.query(sql,(result,rowNum)->new ClassesMaterial(
	    		result.getString("classesMaterial_seq"),
	    		result.getString("class_id"),
	    		result.getString("material_id"),
	    		result.getString("book_id"),
	    		result.getString("bookName"),
	    		result.getString("inputTex")
        ));
		
		return LClassesMaterial;		
	}
	
	public List<SignRecordMaterialHistory> getSignRecordMaterialHistory_0(String signRecordHistory_id){
		
		String sql = "SELECT a.* FROM eip.signRecordMaterialHistory a where 1=1";
		if (signRecordHistory_id != null && !signRecordHistory_id.isEmpty()) {sql += " and a.signRecordHistory_id="+signRecordHistory_id;}

		List<SignRecordMaterialHistory> LSignRecordMaterialHistory= jdbcTemplate.query(sql,(result,rowNum)->new SignRecordMaterialHistory(
	    		result.getString("signRecordMaterialHistory_seq"),
	    		result.getString("signRecordHistory_id"),
	    		result.getString("classesMateria_id"),
	    		"",
	    		"",
	    		"",
	    		"",
	    		result.getString("update_time"),
	    		result.getString("updater")
        ));
		return LSignRecordMaterialHistory;		
	}
	
	public List<SignRecordMaterialHistory> getSignRecordMaterialHistory(String signRecordHistory_id){
		
		String sql = "SELECT a.*,b.material_id,b.book_id,b.inputTex,c.bookName FROM eip.signRecordMaterialHistory a, eip.classesMaterial b left join eip.books c on b.book_id = c.books_seq"+
				     " where a.classesMateria_id=b.classesMaterial_seq";
		if (signRecordHistory_id != null && !signRecordHistory_id.isEmpty()) {sql += " and a.signRecordHistory_id="+signRecordHistory_id;}

		List<SignRecordMaterialHistory> LSignRecordMaterialHistory= jdbcTemplate.query(sql,(result,rowNum)->new SignRecordMaterialHistory(
	    		result.getString("signRecordMaterialHistory_seq"),
	    		result.getString("signRecordHistory_id"),
	    		result.getString("classesMateria_id"),
	    		result.getString("material_id"),
	    		result.getString("book_id"),
	    		result.getString("bookName"),
	    		result.getString("inputTex"),
	    		result.getString("update_time"),
	    		result.getString("updater")
        ));
		return LSignRecordMaterialHistory;		
	}	

/**	
	public String NewFakeSubject(String subjectName,String category_id) {
		jdbcTemplate.update("INSERT INTO eip.subject VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURDATE(),?)",
				    category_id, //category_id
					subjectName,
					"-1",//abbr
					-1,//price
					-1,//hrPrice_R
					-1,//counselingPrice_R
					-1,//lagnappePrice_R
					-1,//handoutPrice_R
					-1,//homeworkPrice_R
					-1,//mockPrice_R
					"",//remark
					0,//isVideo
					0,//isVirtual
					1,//gradeNo
					1,//gradeNoATime
					"",
					"System",
					0 //active
		);
		String subject_seq  = String.valueOf(jdbcTemplate.queryForObject("select last_insert_id()", Integer.class));

		return subject_seq;
	}
**/	
	public List<Subject> getSubjectByGrade(String grade_id){
		
		String sql = "SELECT a.*,c.name as subjectContent_name from eip.subject a,eip.grade b,eip.subjectContent c "+
		             "where b.grade_seq="+grade_id+" and b.subject_id=a.subject_seq and a.subjectContent_code=c.code";
		List<Subject> LSubject = jdbcTemplate.query(sql,(result, rowNum) -> new Subject(
						result.getString("subject_seq"), 
						result.getString("category_id"), 
						"",
						result.getString("name"), 
						result.getString("abbr"), 
						result.getString("price"), 
						"",
						"",
						"",
						"",
						"",
						"",
						result.getString("remark"),
						result.getString("isVideo"),
						result.getString("isVirtual"),
						result.getString("parent_seq"),
						result.getString("updater"),
						result.getString("update_time"),
						result.getString("active"),
						result.getString("subjectContent_code"),
						result.getString("subjectContent_name"),
						"",
						result.getString("free_makeUpNo"),
						result.getString("disable")
		));	
		
		for(int i=0;i<LSubject.size();i++) {
			List<Subject> LSubject1 = getSubjectWithR("",LSubject.get(i).getSubject_seq(),"","1","1");
			if(LSubject1.size()>0) {
				LSubject.get(i).setHrPrice_R(LSubject1.get(0).getHrPrice_R());
				LSubject.get(i).setCounselingPrice_R(LSubject1.get(0).getCounselingPrice_R());
				LSubject.get(i).setLagnappePrice_R(LSubject1.get(0).getLagnappePrice_R());
				LSubject.get(i).setHandoutPrice_R(LSubject1.get(0).getHandoutPrice_R());
				LSubject.get(i).setHomeworkPrice_R(LSubject1.get(0).getHomeworkPrice_R());
				LSubject.get(i).setMockPrice_R(LSubject1.get(0).getMockPrice_R());
			}
		}	
			return LSubject;
	}
	
	public Boolean UpdateClasses(String class_th,String className,String teacher_id,String grade_id,String class_style) {
        //System.out.println("UPDATE eip.classes set class_name='"+className+"',teacher_id='"+teacher_id+"',class_style="+class_style+" where grade_id='"+grade_id+"' and class_th="+class_th);
		jdbcTemplate.update("UPDATE eip.classes set class_name=?,teacher_id=?,class_style=? where grade_id=? and class_th=?;",
			  className,
			  teacher_id,
			  class_style,
			  grade_id,
			  class_th
	  );
  
	  return true;
	} 
	

	public List<SubjectTeacher> getSubjectTeacher(String subject_seq) {
		String sql_0 = "select distinct(a.teacher_id),b.* from eip.grade a, teacher b where a.teacher_id=b.teacher_seq and a.subject_id="+subject_seq+" order by b.name";

		List<Teacher> LTeacher = jdbcTemplate.query(sql_0,
				(result, rowNum) -> new Teacher(
						result.getString("teacher_id"), 
						result.getString("code"),
						result.getString("name"),
						result.getString("realName"),
						"",
						"", 
						"",
						result.getString("enabled"),
						"",
						"",
						"",
						result.getString("school_id"),
						result.getString("email"),
						result.getString("line"),
						result.getString("fb"),
						result.getString("email_contact"),
						result.getString("line_contact"),
						result.getString("fb_contact"),
						result.getString("tel_contact"),
						result.getString("face_contact"),
						"",
						result.getString("county"),
						result.getString("district")						
				));
					
		List<SubjectTeacher> LSubjectTeacher = new ArrayList<SubjectTeacher>();
		List<SubjectTeacher> LSubjectTeacher3 = new ArrayList<SubjectTeacher>();
		//現有期別老師的subjectTeacher設定值
		for(int i=0;i<LTeacher.size();i++) {
			String teacher_id = LTeacher.get(i).getTeacher_seq();
			String teacher_code = LTeacher.get(i).getCode();
			String teacher_name = LTeacher.get(i).getName();
			String teacher_realName = LTeacher.get(i).getRealName();
			String teacher_enabled = LTeacher.get(i).getEnabled();
			String sql = "select a.*,b.name as subject_name from eip.subjectTeacher a, subject b"+
				         " where b.subject_seq = a.subject_id and a.active=1 and a.subject_id='"+subject_seq+"' and a.teacher_id ="+LTeacher.get(i).getTeacher_seq();
			List<SubjectTeacher> LSubjectTeacher_0 = jdbcTemplate.query(sql,
					        (result, rowNum) -> new SubjectTeacher(
							result.getString("subjectTeacher_seq"), 
							result.getString("subject_id"),
							result.getString("subject_name"), 
							result.getString("teacher_id"), 
							result.getString("gradeNo"),
							result.getString("gradeNoATime"),
							result.getString("isPreGrade"),
							teacher_code,
							teacher_name,
							teacher_realName,
							result.getString("updater"),
							result.getString("update_time"),
							teacher_enabled,
							result.getString("totalClassNo"),
							result.getString("active")
				    ));
			//無設定值時        
			if(LSubjectTeacher_0.size()==0) {
				LSubjectTeacher.add(new SubjectTeacher("",subject_seq,"",teacher_id,"-","-","-",teacher_code,teacher_name,teacher_realName,"","",teacher_enabled,"",""));
				LSubjectTeacher3.add(new SubjectTeacher("",subject_seq,"",teacher_id,"-","-","-",teacher_code,teacher_name,teacher_realName,"","",teacher_enabled,"",""));
			//有設定值時
			}else {
				SubjectTeacher su = LSubjectTeacher_0.get(0);
				LSubjectTeacher.add(new SubjectTeacher(su.getSubjectTeacher_seq(),subject_seq,"",teacher_id,su.getGradeNo(),su.getGradeNoATime(),su.getIsPreGrade(),teacher_code,teacher_name,teacher_realName,su.getUpdater(),su.getUpdate_time(),teacher_enabled,su.getTotalClassNo(),""));
				LSubjectTeacher3.add(new SubjectTeacher(su.getSubjectTeacher_seq(),subject_seq,"",teacher_id,su.getGradeNo(),su.getGradeNoATime(),su.getIsPreGrade(),teacher_code,teacher_name,teacher_realName,su.getUpdater(),su.getUpdate_time(),teacher_enabled,su.getTotalClassNo(),""));
				//LSubjectTeacher.add(LSubjectTeacher_0.get(0));
			}
		}	
			
			//新增老師卻尚未存在於已開期別當中
			List<SubjectTeacher> LSubjectTeacher2 = getSubjectTeacher2("","",subject_seq,"1","1");			
			int exist = -1;
			for(int a=0;a<LSubjectTeacher2.size();a++) {
				exist = 0;
				for(int b=0;b<LSubjectTeacher.size();b++) {
					if(LSubjectTeacher.get(b).getSubjectTeacher_seq().equals(LSubjectTeacher2.get(a).getSubjectTeacher_seq())) {
						exist = 1;
					}
				}
				if(exist==0) {
					LSubjectTeacher3.add(LSubjectTeacher2.get(a));
				}
			}
			
		return LSubjectTeacher3;
	}
	
	public List<SubjectTeacher> getSubjectTeacher2(String subjectTeacher_seq,String teacher_id,String subject_id,String enabled,String active) {
		String sql = "select a.*,b.code as teacher_code,b.name as teacher_name,b.realName teacher_realName,b.enabled as teacher_enabled,c.name as subject_name"+
	                 " from eip.subjectTeacher a,eip.teacher b,eip.subject c where c.subject_seq = a.subject_id and a.active=1 and a.teacher_id=b.teacher_seq";
		if (subjectTeacher_seq != null && !subjectTeacher_seq.isEmpty()) {sql += " and subjectTeacher_seq="+subjectTeacher_seq;}
		if (teacher_id != null && !teacher_id.isEmpty()) {sql += " and a.teacher_id='"+teacher_id+"'";}
		if (subject_id != null && !subject_id.isEmpty()) {sql += " and a.subject_id='"+subject_id+"'";}
		if (enabled != null && !enabled.isEmpty()) {sql += " and b.enabled='"+enabled+"'";}
		if (active != null && !active.isEmpty()) {sql += " and a.active="+active;}
		
		List<SubjectTeacher> LSubjectTeacher = jdbcTemplate.query(sql,
		        (result, rowNum) -> new SubjectTeacher(
				result.getString("subjectTeacher_seq"), 
				result.getString("subject_id"), 
				result.getString("subject_name"), 
				result.getString("teacher_id"), 
				result.getString("gradeNo"),
				result.getString("gradeNoATime"),
				result.getString("isPreGrade"),
				result.getString("teacher_code"),
				result.getString("teacher_name"),
				result.getString("teacher_realName"),
				result.getString("updater"),
				result.getString("update_time"),
				result.getString("teacher_enabled"),
				result.getString("totalClassNo"),
				result.getString("active")
		));
        return LSubjectTeacher;
	}
	
	public List<GradeClassNo> getGradeClassNo(String subjectTeacher_id,String subject_id,String teacher_id,String gradeName){
		String sql = "SELECT a.*,b.totalClassNo FROM eip.gradeClassNo a,eip.subjectTeacher b where b.subjectTeacher_seq=a.subjectTeacher_id and b.active=1";
		if (subjectTeacher_id != null && !subjectTeacher_id.isEmpty()) {sql += " and a.subjectTeacher_id='"+subjectTeacher_id+"'";}
		if (subject_id != null && !subject_id.isEmpty()) {sql += " and b.subject_id='"+subject_id+"'";}
		if (teacher_id != null && !teacher_id.isEmpty()) {sql += " and b.teacher_id='"+teacher_id+"'";}
		if (gradeName != null && !gradeName.isEmpty()) {sql += " and a.gradeName='"+gradeName+"'";}
		
		List<GradeClassNo> LGradeClassNo = jdbcTemplate.query(sql,(result,rowNum)->new GradeClassNo(
	    		result.getString("gradeClassNo_seq"),
	    		result.getString("subjectTeacher_id"),
	    		result.getString("gradeName"),
	    		result.getString("gradeClassNo"),
	    		result.getString("totalClassNo")
        ));
		return LGradeClassNo;			
	}
	

	public List<SubjectContent> getSubjectContent(){
		String sql = "SELECT * FROM eip.subjectContent order by code";			
		List<SubjectContent> LSubjectContent = jdbcTemplate.query(sql,(result,rowNum)->new SubjectContent(
	    		result.getString("subjectContent_seq"),
	    		result.getString("code"),
	    		result.getString("name")
        ));
		return LSubjectContent;			
	}
	
	public List<Subject_tmp> getSubject_tmp(String subject_id,String schedule_time_After,String schedule_time) {
		String sql = "select * from eip.subject_tmp where 1=1";
		if (subject_id != null && !subject_id.isEmpty()) {sql += " and subject_id='"+subject_id+"'";}
		if (schedule_time_After != null && !schedule_time_After.isEmpty()) {sql += " and schedule_time>'"+schedule_time_After+"'";}
		if (schedule_time != null && !schedule_time.isEmpty()) {sql += " and schedule_time='"+schedule_time+"'";}
		List<Subject_tmp> LSubject_tmp = jdbcTemplate.query(sql,(result, rowNum) -> new Subject_tmp(
						result.getString("schedule_time"),
						result.getString("subject_id"),
						result.getString("category_id"),
						result.getString("name"),
						result.getString("abbr"),
						result.getString("price"),
						result.getString("remark"),
						result.getString("isVideo"),
						result.getString("isVirtual"),
						result.getString("parent_seq"),
						result.getString("updater"),
						result.getString("update_time"),
						result.getString("active"),
						result.getString("subjectContent_code")
				));
		return LSubject_tmp;
	}
	
	public Boolean migrateSubject_tmp(Subject_tmp subject_tmp) {
		jdbcTemplate.update("UPDATE eip.subject set category_id=?,name=?,abbr=?,price=?,remark=?,isVideo=?,isVirtual=?,parent_seq=?,updater=?,update_time=NOW(),active=?,subjectContent_code=? where subject_seq=?;",
				subject_tmp.getCategory_id(),
				subject_tmp.getName(),
				subject_tmp.getAbbr(),
				subject_tmp.getPrice(),
				subject_tmp.getRemark(),
				subject_tmp.getIsVideo(),
				subject_tmp.getIsVirtual(),
				subject_tmp.getParent_seq(),
				subject_tmp.getUpdater(),
				subject_tmp.getActive(),
				subject_tmp.getSubjectContent_code(),
				subject_tmp.getSubject_id()
	);		
		return true;
	}
	
	public List<VirtualSubject_tmp> getVirtualSubject_tmp(String subject_id,String schedule_time) {
		String sql = "select * from eip.virtualSubject_tmp where 1=1";
		if (subject_id != null && !subject_id.isEmpty()) {sql += " and subject_id='"+subject_id+"'";}
		if (schedule_time != null && !schedule_time.isEmpty()) {sql += " and schedule_time='"+schedule_time+"'";}
		List<VirtualSubject_tmp> LVirtualSubject_tmp = jdbcTemplate.query(sql,(result, rowNum) -> new VirtualSubject_tmp(
						result.getString("schedule_time"),
						result.getString("subject_id"),
						result.getString("child_subject_id")
				));
		return LVirtualSubject_tmp;
	}
	
	public Boolean DeleteSubjectTeacher(String subjectTeacher_seq,String updater) {
		if(subjectTeacher_seq!=null && !subjectTeacher_seq.isEmpty()) {
			jdbcTemplate.update("UPDATE eip.subjectTeacher set active=?,updater=?,update_time=NOW() where subjectTeacher_seq=?;",
					0,
					updater,
					subjectTeacher_seq			
			);
		}	
		return true;
	}
	
	public Boolean DeleteLecture(String lecture_seq,String updater) {
		if(lecture_seq!=null && !lecture_seq.isEmpty()) {
			jdbcTemplate.update("UPDATE eip.lecture set active=?,creater=?,createTime=NOW() where lecture_seq=?;",
					0,
					updater,
					lecture_seq			
			);
		}	
		return true;
	}

	public Boolean DeletePromo(String classPromotion_id,String updater) {
		if(classPromotion_id!=null && !classPromotion_id.isEmpty()) {
			jdbcTemplate.update("UPDATE eip.classPromotion set active=?,updater=?,update_time=NOW() where classPromotion_seq=?;",
					0,
					updater,
					classPromotion_id			
			);
		}	
		return true;
	}
	
	public List<MockVideoHistory> getMockVideoHistory(String register_mock_id,String active,String mockVideo_id,String register_status_in,String student_id,String attend_date,String school_code,String slot,String attend,String mockVideoHistory_seq){
		String sql = "SELECT a.*,b.ch_name,b.student_no FROM eip.mockVideoHistory a,eip.student b where a.student_id=b.student_seq";
		if(register_mock_id != null && !register_mock_id.isEmpty()) {sql += " and register_mock_id='"+register_mock_id+"'";}
		if(active != null && !active.isEmpty()) {sql += " and active="+active;}
		if(mockVideo_id != null && !mockVideo_id.isEmpty()) {sql += " and mockVideo_id='"+mockVideo_id+"'";}
		if(register_status_in != null && !register_status_in.isEmpty()) {sql += " and register_status in "+register_status_in;}
		if(student_id != null && !student_id.isEmpty()) {sql += " and a.student_id="+student_id;}
		if(attend_date != null && !attend_date.isEmpty()) {sql += " and a.attend_date='"+attend_date+"'";}
		if(school_code != null && !school_code.isEmpty()) {sql += " and a.school_code='"+school_code+"'";}
		if(slot != null && !slot.isEmpty()) {sql += " and slot="+slot;}
		if(attend != null && !attend.isEmpty()) {sql += " and attend="+attend;}
		if(mockVideoHistory_seq != null && !mockVideoHistory_seq.isEmpty()) {sql += " and mockVideoHistory_seq="+mockVideoHistory_seq;}
		
		sql += " order by mockVideoHistory_seq desc";

		List<MockVideoHistory> LMockVideoHistory = jdbcTemplate.query(sql,(result,rowNum)->new MockVideoHistory(
	    		result.getString("mockVideoHistory_seq"),
	    		result.getString("register_mock_id"),
	    		result.getString("register_status"),
	    		result.getString("student_id"),
	    		result.getString("school_code"),
	    		result.getString("grade_id"),
	    		result.getString("attend"),
	    		result.getString("active"),
	    		result.getString("attend_date"),
	    		result.getString("slot"),
	    		result.getString("update_time"),
	    		result.getString("updater"),
	    		result.getString("classroom"),
	    		result.getString("seat"),
	    		result.getString("comment"),
	    		result.getString("mockVideo_id"),
	    		"",
	    		"",
	    		result.getString("ch_name"),
	    		result.getString("student_no")
        ));
		
		for(int i=0;i<LMockVideoHistory.size();i++) {
				List<Grade> LGrade = getGrade(LMockVideoHistory.get(i).getGrade_id(),"","","","","","","","","","","","");
				LMockVideoHistory.get(i).setMockVideo_name(LGrade.get(0).getClass_start_date()+" "+LGrade.get(0).getSubject_name());
				
				if(LMockVideoHistory.get(i).getSlot()!=null) {
					if(LMockVideoHistory.get(i).getSlot().equals("1")) {
						LMockVideoHistory.get(i).setSlotName("早");
					}else if(LMockVideoHistory.get(i).getSlot().equals("2")) {
						LMockVideoHistory.get(i).setSlotName("午");
					}else if(LMockVideoHistory.get(i).getSlot().equals("3")) {
						LMockVideoHistory.get(i).setSlotName("晚");
					}
				}	
		}
		return LMockVideoHistory;
	}
	
	public Boolean gradeDisable(String disable,String grade_seq) {
		if(grade_seq!=null && !grade_seq.isEmpty()) {
			jdbcTemplate.update("UPDATE eip.grade set disable=? where grade_seq=?;",
					disable,
					grade_seq		
			);
		}	
		return true;
	}
	
	public Boolean comboDisable(String comboSale_seq) {
		if(comboSale_seq!=null && !comboSale_seq.isEmpty()) {
			jdbcTemplate.update("UPDATE eip.comboSale set disable=? where comboSale_seq=?;",
					1,
					comboSale_seq		
			);
		}	
		return true;
	}
	
	public Boolean subjectDisable(String subject_seq) {
		if(subject_seq!=null && !subject_seq.isEmpty()) {
			jdbcTemplate.update("UPDATE eip.subject set disable=? where subject_seq=?;",
					1,
					subject_seq		
			);
		}	
		return true;
	}
	
	public List<TestRound> getTestRound(String category_id){
		String sql = "SELECT a.*,b.name as category_name FROM eip.testRound a,eip.category b where a.category_id=b.category_seq";			
		if(category_id != null && !category_id.isEmpty()) {sql += " and a.category_id='"+category_id+"'";}
		sql +=" order by a.category_id,a.name";
		List<TestRound> LTestRound = jdbcTemplate.query(sql,(result,rowNum)->new TestRound(
	    		result.getString("testRound_seq"),
	    		result.getString("category_id"),
	    		result.getString("name"),
	    		result.getString("category_name")
        ));
		return LTestRound;			
	}
	
	public List<TestRound> getTestRoundGroupBy(){
		String sql = "SELECT a.*,b.name as category_name FROM eip.testRound a,eip.category b where a.category_id=b.category_seq group by category_id";		
		List<TestRound> LTestRound = jdbcTemplate.query(sql,(result,rowNum)->new TestRound(
	    		result.getString("testRound_seq"),
	    		result.getString("category_id"),
	    		result.getString("name"),
	    		result.getString("category_name")
        ));
		return LTestRound;				
	}
}
