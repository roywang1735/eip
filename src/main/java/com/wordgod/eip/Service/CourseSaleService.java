package com.wordgod.eip.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.wordgod.eip.Model.*;

@Service
public class CourseSaleService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*
	 * public Subject getSubjectStr(String subject_seq) { String sql =
	 * "select * from eip.subject where 1 = 1";
	 * 
	 * if (subject_seq != null && !subject_seq.isEmpty()) { sql +=
	 * " and subject_seq = " + subject_seq; }
	 * 
	 * List<Subject> items = jdbcTemplate.query(sql, (result, rowNum) -> new
	 * Subject(result.getString("subject_seq"), result.getString("category_id"), "",
	 * result.getString("name"), result.getString("abbr"),
	 * result.getString("price"), result.getString("class_no"),
	 * result.getString("class_makeup"),
	 * result.getString("remark"),result.getString("sql_str"))); return
	 * items.get(0); }
	 */
/**	
	public Subject getSubjectReplace(String old_subject_id, String new_subject_id) {
		String sql = "select * from eip.subject where subject_seq =" + new_subject_id;
		
		List<Subject> LSubject = jdbcTemplate.query(sql,(result, rowNum) -> new Subject(
				"","","",result.getString("name"),"","","","","",""));
		//換課價差
		String sql_old = "select a.* from eip.comboSale a, eip.comboSale_subject b"+
				         " where a.isCombo=0 and a.comboSale_seq = b.comboSale_id and b.subject_id="+old_subject_id;
		List<ComboSale> LComboSale_old = jdbcTemplate.query(sql_old,(result,rowNum)->new ComboSale(
				"","","","","","",
                result.getString("origin_price"),
                result.getString("sale_price"),
                "","","","",""));	
		String sql_new = "select a.* from eip.comboSale a, eip.comboSale_subject b"+
		                 " where a.isCombo=0 and a.comboSale_seq = b.comboSale_id and b.subject_id="+new_subject_id;
		List<ComboSale> LComboSale_new = jdbcTemplate.query(sql_new,(result,rowNum)->new ComboSale(
				"","","","","","",
				result.getString("origin_price"),
				result.getString("sale_price"),
				"","","","","" ));	

		int priceDifference = Integer.valueOf(LComboSale_new.get(0).getSale_price())-Integer.valueOf(LComboSale_old.get(0).getSale_price());
		//借用Remark欄位填價差
		if(priceDifference>0) {
			 LSubject.get(0).setRemark(Integer.toString(priceDifference));
		}else {LSubject.get(0).setRemark("0");}
		
		return LSubject.get(0);
	}	
**/		
	public List<Mock> getMock(){ 
		String sql = "SELECT a.*,b.name as category_name FROM eip.mock a, eip.category b where a.category_id = b.category_seq";
				
		List<Mock> LMock = jdbcTemplate.query(sql,(result,rowNum)->new Mock(
	    		result.getString("mock_seq"),
	    		result.getString("category_id"),
	    		result.getString("category_name"),
                result.getString("mock_name"),
                result.getString("active"),
                result.getString("original_price")
                ));
		return LMock;
	}
	
	public List<Counseling> getCounseling(String counseling_seq){ 
		String sql = "SELECT a.*,b.name as category_name FROM eip.counseling a, eip.category b where a.category_id = b.category_seq";
		if (counseling_seq != null && !counseling_seq.isEmpty()) {
			sql += " and counseling_seq = " + counseling_seq;
		}
		List<Counseling> LCounseling = jdbcTemplate.query(sql,(result,rowNum)->new Counseling(
	    		result.getString("counseling_seq"),
	    		result.getString("category_id"),
	    		result.getString("category_name"),
                result.getString("counseling_name"),
                result.getString("origin_price"),
                result.getString("active")
                ));
		return LCounseling;
	}
	
	public List<Lagnappe> getLagnappe(String lagnappe_seq,String active){ 
		String sql = "SELECT * FROM eip.lagnappe where 1=1";
		if (lagnappe_seq != null && !lagnappe_seq.isEmpty()) {
			sql += " and lagnappe_seq = " + lagnappe_seq;
		}
		if (active != null && !active.isEmpty()) {
			sql += " and active = " + active;
		}
		sql +=" and del_item !=1";
		
		List<Lagnappe> LLagnappe = jdbcTemplate.query(sql,(result,rowNum)->new Lagnappe(
	    		result.getString("lagnappe_seq"),
                result.getString("lagnappe_name"),
                result.getString("origin_price"),
                result.getString("sale_price"),
                result.getString("payOffRelease"),
                result.getString("active"),
                result.getString("del_item"),
                result.getString("updater"), 
                result.getString("update_time")
        ));
		return LLagnappe;
	}

	public Boolean otherLagnappeEditSave(Lagnappe lagnappe,String updater) {
		if(lagnappe.getLagnappe_seq()!=null && !lagnappe.getLagnappe_seq().isEmpty()) {
			jdbcTemplate.update("update eip.lagnappe set lagnappe_name=?,origin_price=?,sale_price=?,payOffRelease=?,active=?,updater=?,update_time=NOW() where lagnappe_seq=?;",
					lagnappe.getLagnappe_name(),
					lagnappe.getOrigin_price(),
					lagnappe.getSale_price(),
					lagnappe.getPayOffRelease(),
					lagnappe.getActive(),
					updater,
					lagnappe.getLagnappe_seq()
			);			
		}else{
			jdbcTemplate.update("INSERT INTO eip.lagnappe VALUES (default,?,?,?,?,?,?,?,NOW());",
					lagnappe.getLagnappe_name(),
					lagnappe.getOrigin_price(),
					lagnappe.getSale_price(),
					lagnappe.getPayOffRelease(),
					lagnappe.getActive(),
					0, //del_item
					updater
			);
		}	
		return true;
	}	

	public List<Material> getMaterial(String material_seq){ 
		String sql = "SELECT a.*,b.name as category_name FROM eip.material a, eip.category b where a.category_id = b.category_seq";
		if (material_seq != null && !material_seq.isEmpty()) {
			sql += " and material_seq = " + material_seq;
		}				
		List<Material> LMaterial = jdbcTemplate.query(sql,(result,rowNum)->new Material(
	    		result.getString("material_seq"),
	    		result.getString("category_id"),
	    		result.getString("category_name"),
                result.getString("material_name"),
                result.getString("origin_price"),
                result.getString("sale_price"),
                result.getString("remark"),
                result.getString("update_time")
                ));
		return LMaterial;
	}
	
	public String ComboSaleSave(ComboSale comboSale,String[] A_subject_seq,String[] A_mock_seq,String[] A_counseling_seq,String[] A_lagnappe_seq,String[] A_lagnappe_no,String[] A_books_seq,String schedule_time,String haveRead) {
		String comboSale_name = comboSale.getName();
		if(comboSale_name==null || comboSale_name.isEmpty()) {
			comboSale_name = comboSale.getOriginSubjectName();
		}
		jdbcTemplate.update("INSERT INTO eip.comboSale VALUES (default,?,?,?,?,?,?,?,?,?,CURDATE(),?,?,?,?,?,?);",
				    comboSale.getIsCombo(),
				    comboSale.getCategory_id(),
				    comboSale.getFlowStatus_code(),
				    comboSale_name,
				    comboSale.getOrigin_price(),
				    comboSale.getSale_price(),
				    comboSale.getClass_makeup(),
				    comboSale.getRemark(),
				    comboSale.getCreater(),
				    "1", //class_style預設值
				    schedule_time,
				    comboSale.getOriginSubjectName(),
				    comboSale.getFavor_id(),
				    haveRead,
				    "0" //disable
		);
		
		int newSeq  = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);	
		if(A_subject_seq!=null){
			for(int i=0;i<A_subject_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.comboSale_subject VALUES (default,?,?);",
					newSeq,
				    A_subject_seq[i].split("@")[0]
				);			
			}
		}
		
		if(A_mock_seq!=null){
			for(int i=0;i<A_mock_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.comboSale_mock VALUES (default,?,?);",
					newSeq,
				    A_mock_seq[i]
			    );
			}	
		}
		
		if(A_counseling_seq!=null){
			for(int i=0;i<A_counseling_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.comboSale_counseling VALUES (default,?,?);",
					newSeq,
				    A_counseling_seq[i]
			    );
			}	
		}
		
		if(A_lagnappe_seq!=null){
			for(int i=0;i<A_lagnappe_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.comboSale_lagnappe VALUES (default,?,?,?);",
					newSeq,
				    A_lagnappe_seq[i],
				    A_lagnappe_no[i]
			    );
			}	
		}
		
		if(A_books_seq!=null){
			for(int i=0;i<A_books_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.comboSale_outPublisher VALUES (default,?,?);",
					newSeq,
					A_books_seq[i]
			    );
			}	
		}		
		
		return Integer.toString(newSeq);
	}
	
	public Boolean ComboSaleUpdate(ComboSale comboSale,String[] A_subject_seq,String[] A_mock_seq,String[] A_counseling_seq,String[] A_lagnappe_seq,String[] A_lagnappe_no,String[] A_books_seq) {
//update comboSale
		jdbcTemplate.update("update eip.comboSale set isCombo=?,FlowStatus_code=?,name=?,origin_price=?,sale_price=?,class_makeup=?,remark=?,creater=?,originSubjectName=?,update_time=CURDATE(),favor_id=? where comboSale_seq=?;",
				    comboSale.getIsCombo(),
				    comboSale.getFlowStatus_code(),
				    comboSale.getName(),
				    comboSale.getOrigin_price(),
				    comboSale.getSale_price(),
				    comboSale.getClass_makeup(),
				    comboSale.getRemark(),
				    comboSale.getCreater(),
				    comboSale.getOriginSubjectName(),
				    comboSale.getFavor_id(),
				    comboSale.getComboSale_seq()			    
		);
//delete		
		if (comboSale.getComboSale_seq() != null && !comboSale.getComboSale_seq().isEmpty()) {
			jdbcTemplate.update("delete from eip.comboSale_subject  where comboSale_id=?;",
					comboSale.getComboSale_seq()
			);	
			jdbcTemplate.update("delete from eip.comboSale_mock  where comboSale_id=?;",
					comboSale.getComboSale_seq()
			);	
			jdbcTemplate.update("delete from eip.comboSale_counseling  where comboSale_id=?;",
					comboSale.getComboSale_seq()
			);
			jdbcTemplate.update("delete from eip.comboSale_lagnappe  where comboSale_id=?;",
					comboSale.getComboSale_seq()
			);
			jdbcTemplate.update("delete from eip.comboSale_outPublisher  where comboSale_id=?;",
					comboSale.getComboSale_seq()
			);			
		}
//insert	
		if(A_subject_seq!=null){
			for(int i=0;i<A_subject_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.comboSale_subject VALUES (default,?,?);",
					comboSale.getComboSale_seq(),
				    A_subject_seq[i].split("@")[0]
				);			
			}
		}
		
		if(A_mock_seq!=null){
			for(int i=0;i<A_mock_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.comboSale_mock VALUES (default,?,?);",
					comboSale.getComboSale_seq(),
				    A_mock_seq[i]
			    );
			}	
		}
		
		if(A_counseling_seq!=null){
			for(int i=0;i<A_counseling_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.comboSale_counseling VALUES (default,?,?);",
				    comboSale.getComboSale_seq(),
				    A_counseling_seq[i]
			    );
			}	
		}
		
		if(A_lagnappe_seq!=null){
			for(int i=0;i<A_lagnappe_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.comboSale_lagnappe VALUES (default,?,?,?);",
					comboSale.getComboSale_seq(),
				    A_lagnappe_seq[i],
				    A_lagnappe_no[i]
			    );
			}	
		}
		
		if(A_books_seq!=null){
			for(int i=0;i<A_books_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.comboSale_outPublisher VALUES (default,?,?);",
					comboSale.getComboSale_seq(),
					A_books_seq[i]
			    );
			}	
		}		
		
		return true;
	}	
	
	public Boolean SingleCourseSaleSave(SinCourseSale sinCourseSale,String[] A_counseling_seq) {
		jdbcTemplate.update("INSERT INTO eip.singleCourseSale VALUES (default,?,?,?,?,?,?,?,?,?,?,?,CURDATE(),?);",
				sinCourseSale.getCategory_id(),
				sinCourseSale.getSubject_id(),
				sinCourseSale.getOrigin_price(),
				sinCourseSale.getSale_price(),
				sinCourseSale.getHrPrice(),
				sinCourseSale.getCounselingPrice(),
				sinCourseSale.getLagnappePrice(),
				sinCourseSale.getHandoutPrice(),
				sinCourseSale.getHomeworkPrice(),
				sinCourseSale.getMockPrice(),
				sinCourseSale.getCoursePrice(),
				sinCourseSale.getFlowStatus_code()
		);	
		
		int newSeq  = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
		
		if(A_counseling_seq!=null){
			for(int i=0;i<A_counseling_seq.length;i++) {
				jdbcTemplate.update("INSERT INTO eip.courseSale_counseling VALUES (default,?,?,?);",
					"-1",
					newSeq, //適用於單科時
				    A_counseling_seq[i]
			    );
			}	
		}
			
		return true;
	}
	public List<ComboSale> getComboSaleBySubject(String isCombo,String subject_id){
		String sql = "select a.* from comboSale a, comboSale_subject b"+
					  " where a.comboSale_seq = b.comboSale_id and b.subject_id="+subject_id;
		if (isCombo != null && !isCombo.isEmpty()) {
			sql += " and a.isCombo = " + isCombo;
		}
		List<ComboSale> LComboSale = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale(
	    		result.getString("comboSale_seq"),
	    		result.getString("isCombo"),
	    		result.getString("category_id"),
	    		"",
	    		result.getString("flowStatus_code"),
	    		"",
	    		result.getString("name"),
	    		result.getString("origin_price"),
                result.getString("sale_price"),
                result.getString("remark"),
                result.getString("creater"),
                result.getString("update_time"),
                "",
                result.getString("class_makeup"),
                result.getString("schedule_time"),
                result.getString("originSubjectName"),
                result.getString("favor_id"),
                result.getString("disable")
        ));
		return LComboSale;
	}
	
	public List<ComboSale> getComboSaleByCombo(String isCombo,String comboSale_seq){
		String sql = "select a.* from comboSale a"+
					  " where 1=1";
		if (isCombo != null && !isCombo.isEmpty()) {
			sql += " and a.isCombo = " + isCombo;
		}
		if (comboSale_seq != null && !comboSale_seq.isEmpty()) {
			sql += " and a.comboSale_seq = " + comboSale_seq;
		}
		
		sql += " and (a.disable is null or a.disable='0')";
		
		List<ComboSale> LComboSale = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale(
	    		result.getString("comboSale_seq"),
	    		result.getString("isCombo"),
	    		result.getString("category_id"),
	    		"",
	    		result.getString("flowStatus_code"),
	    		"",
	    		result.getString("name"),
	    		result.getString("origin_price"),
                result.getString("sale_price"),
                result.getString("remark"),
                result.getString("creater"),
                result.getString("update_time"),
                "",
                result.getString("class_makeup"),
                result.getString("schedule_time"),
                result.getString("originSubjectName"),
                result.getString("favor_id"),
                result.getString("disable")
        ));
		return LComboSale;
	}	
	public List<ComboSale> getComboSale(String comboSale_seq,String category_id,String isCombo,String FlowStatus_code,String scheduleOnly,String subjectOrderBy,String haveRead,String disable){ 
		String sql1_a = "select a.*, b.name as category_name, c.name as flowStatus_name from eip.comboSale a,eip.category b,eip.FlowStatus c";
		String sql1_b = " where a.category_id = b.category_seq and a.FlowStatus_code = c.code and a.FlowStatus_code !=7";
		
		if (comboSale_seq != null && !comboSale_seq.isEmpty()) {
			sql1_b += " and a.comboSale_seq = " + comboSale_seq;
		}
		if (category_id != null && !category_id.isEmpty()) {
			sql1_b += " and a.category_id = " + category_id;
		}
		if (isCombo != null && !isCombo.isEmpty()) {
			sql1_b += " and a.isCombo = " + isCombo;
		}
		if (FlowStatus_code != null && !FlowStatus_code.isEmpty()) {
			sql1_b += " and a.FlowStatus_code = " + FlowStatus_code;
		}
		if (scheduleOnly != null && scheduleOnly.equals("1")) {
			sql1_b += " and a.schedule_time!=''";
		}
		if (haveRead != null && !haveRead.isEmpty()) {
			sql1_b += " and a.haveRead=" + haveRead;
		}
		if (disable == null || disable.equals("0")) {
			sql1_b += " and (a.disable is null or a.disable='0')";
		}else {
			//全選
		}
		if(subjectOrderBy != null && !subjectOrderBy.isEmpty()) {
			if(isCombo.equals("0")) {
				sql1_a += ",eip.comboSale_subject d";
				sql1_b +=" and a.comboSale_seq=d.comboSale_id order by d."+subjectOrderBy;
			}	
		}else {
			sql1_b +=" order by a.comboSale_seq desc";
		}	
	
	
		List<ComboSale> LComboSale = jdbcTemplate.query(sql1_a+sql1_b,(result,rowNum)->new ComboSale(
	    		result.getString("comboSale_seq"),
	    		result.getString("isCombo"),
	    		result.getString("category_id"),
	    		result.getString("category_name"),
	    		result.getString("flowStatus_code"),
	    		result.getString("flowStatus_name"),
	    		result.getString("name"),
	    		result.getString("origin_price"),
                result.getString("sale_price"),
                result.getString("remark"),
                result.getString("creater"),
                result.getString("update_time"),
                "",
                result.getString("class_makeup"),
                result.getString("schedule_time"),
                result.getString("originSubjectName"),
                result.getString("favor_id"),
                result.getString("disable")
        ));
		
		
		for(int i=0;i<LComboSale.size();i++) {			
			String sql2 = "select b.free_makeUpNo,b.name,c.name as subjectContent_name,b.disable from eip.comboSale_subject a,eip.subject b,eip.subjectContent c where c.code=b.subjectContent_code and b.parent_seq='' and a.subject_id = b.subject_seq and a.comboSale_id ="+ LComboSale.get(i).getComboSale_seq();				
			List<Subject> Lsubject = jdbcTemplate.query(sql2,(result,rowNum)->new Subject(
		    		"",
		    		"",
		    		"",
		    		result.getString("name"),
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
	                result.getString("subjectContent_name"),
	                "",
	                result.getString("free_makeUpNo"),
	                result.getString("disable")
	        ));
			
			String subjectStr = "";
			for(int j=0;j<Lsubject.size();j++) {
				subjectStr +=Lsubject.get(j).getName()+" "+(Lsubject.get(j).getSubjectContent_name().equals("")?"":"<span style='font-size:xx-small;color:#888888'>("+Lsubject.get(j).getSubjectContent_name()+")</span>")+"<br>";
			}
			LComboSale.get(i).setSubjectStr(subjectStr);
		}
		return LComboSale;
	}

	
	public List<ComboSale_subject> getComboSale_subject(String comboSale_id,String subject_id){ 
		
		String sql = "SELECT a.comboSale_subject_seq,a.comboSale_id,b.*,c.class_style,d.*,c.isCombo"+
					 " FROM eip.ComboSale_subject a,eip.subject b,eip.ComboSale c,eip.subject_R d"+
				     " where d.subject_id=a.subject_id and c.class_style=d.class_style and c.comboSale_seq ="+comboSale_id+" and a.comboSale_id="+comboSale_id+" and a.subject_id = b.subject_seq";

		if (subject_id != null && !subject_id.isEmpty()) {
			sql += " and a.subject_id ="+subject_id+" ";
		}
		
		List<ComboSale_subject> LComboSale_subject = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale_subject(
				result.getString("comboSale_subject_seq"),
				result.getString("comboSale_id"),
				result.getString("subject_seq"),
	    		result.getString("name"),
	    		"",
	    		result.getString("price"),
	    		"",
	    		"",
	    		"",
	    		"",
	    		"",
	    		result.getString("hrPrice_R"),
	    		result.getString("counselingPrice_R"),
	    		result.getString("lagnappePrice_R"),
	    		result.getString("handoutPrice_R"),
	    		result.getString("homeworkPrice_R"),
	    		result.getString("mockPrice_R")
        ));
		
/**
		String sql = "SELECT a.comboSale_subject_seq,a.comboSale_id,b.*,c.class_style,c.isCombo"+
				 " FROM eip.ComboSale_subject a,eip.subject b,eip.ComboSale c"+
			     " where c.comboSale_seq ="+comboSale_id+" and a.comboSale_id="+comboSale_id+" and a.subject_id = b.subject_seq";

	if (subject_id != null && !subject_id.isEmpty()) {
		sql += " and a.subject_id ="+subject_id+" ";
	}
	
	List<ComboSale_subject> LComboSale_subject = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale_subject(
			result.getString("comboSale_subject_seq"),
			result.getString("comboSale_id"),
			result.getString("subject_seq"),
	   		result.getString("name"),
	   		"",
	   		result.getString("price"),
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
	   		""
   ));
**/   		
		for(ComboSale_subject comboSale_subject : LComboSale_subject){
			//Counseling
			String CounselingStr = "";
			comboSale_subject.setCounselingStr(CounselingStr);
			//Lagnappe
			String LagnappeStr = "";
			comboSale_subject.setLagnappeStr(LagnappeStr);
			//Mock
			String MockStr = "";
			comboSale_subject.setMockStr(MockStr);
			//outPublisher
			String OutPublisherStr = "";
			int outPublisher_price = 0;

			comboSale_subject.setOutPublisherStr(OutPublisherStr);
			comboSale_subject.setOutPublisher_price(String.valueOf(outPublisher_price));
		}	
			
		return LComboSale_subject;		
	}
	
	public List<ComboSale_material> getComboSale_material(String comboSale_id){ 
		String sql = "SELECT b.material_seq as material_id,b.material_name,a.origin_price,a.sale_price FROM eip.ComboSale_material a, eip.material b"+
				     " where a.comboSale_id="+comboSale_id+" and a.material_id = b.material_seq";
		List<ComboSale_material> LComboSale_material = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale_material(
	    		result.getString("material_id"),
	    		result.getString("material_name"),
	    		result.getString("origin_price"),
	    		result.getString("sale_price")
        ));
		return LComboSale_material;		
	}
	
	public List<ComboSale_mock> getComboSale_mock(String comboSale_id){ 
		String sql = "SELECT b.mock_seq as mock_id,b.mock_name"+
	                 " FROM eip.ComboSale_mock a, eip.mock b"+
				     " where a.comboSale_id="+comboSale_id+" and a.mock_id = b.mock_seq";
		List<ComboSale_mock> LComboSale_mock = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale_mock(
	    		result.getString("mock_id"),
	    		result.getString("mock_name")
        ));
		return LComboSale_mock;		
	}
	
	public List<ComboSale_outPublisher> getComboSale_outPublisher(String comboSale_id){ 
		String sql = "SELECT a.*,b.bookName,b.sellPrice"+
	                 " FROM eip.comboSale_outPublisher a, eip.books b"+
				     " where a.comboSale_id="+comboSale_id+" and a.book_id = b.books_seq";

		List<ComboSale_outPublisher> LComboSale_outPublisher = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale_outPublisher(
	    		result.getString("comboSale_outPublisher_seq"),
	    		result.getString("comboSale_id"),
	    		result.getString("book_id"),
	    		result.getString("bookName")+" "+result.getString("sellPrice")+"元",
	    		result.getString("sellPrice")
        ));
		return LComboSale_outPublisher;		
	}	
	
	public List<ComboSale_counseling> getComboSale_counseling(String comboSale_id){ 
		String sql = "SELECT b.counseling_seq as counseling_id,b.counseling_name"+
	                 " FROM eip.ComboSale_counseling a, eip.counseling b"+
				     " where a.comboSale_id="+comboSale_id+" and a.counseling_id = b.counseling_seq";
		List<ComboSale_counseling> LComboSale_counseling = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale_counseling(
	    		result.getString("counseling_id"),
	    		result.getString("counseling_name")
        ));
		return LComboSale_counseling;		
	}
	
	public List<ComboSale_lagnappe> getComboSale_lagnappe(String comboSale_id){ 
		String sql = "SELECT b.lagnappe_seq as lagnappe_id,b.lagnappe_name,a.lagnappe_no"+
				     " FROM eip.ComboSale_lagnappe a, eip.lagnappe b"+
				     " where a.comboSale_id="+comboSale_id+" and a.lagnappe_id = b.lagnappe_seq";
		
		List<ComboSale_lagnappe> LComboSale_lagnappe = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale_lagnappe(
	    		result.getString("lagnappe_id"),
	    		result.getString("lagnappe_name"),
	    		result.getString("lagnappe_no")
        ));
		return LComboSale_lagnappe;		
	}	
	
	
	public List<ComboSale_counseling> getCounselingBySubject_id(String subject_id){
		
		String sql = "SELECT b.counseling_seq as counseling_id,b.counseling_name"+
		             " FROM eip.ComboSale_counseling a, eip.counseling b, eip.comboSale_subject c, eip.comboSale d"+
			         " where a.comboSale_id=d.comboSale_seq and a.counseling_id = b.counseling_seq and d.comboSale_seq=c.comboSale_id and d.isCombo=0 and c.subject_id="+subject_id;
		List<ComboSale_counseling> LComboSale_counseling = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale_counseling(
				result.getString("counseling_id"),
				result.getString("counseling_name")
		));		
		return LComboSale_counseling;
	}	
	
	public List<ComboSale_lagnappe> getLagnappeBySubject_id(String subject_id){
		
		String sql = "SELECT b.lagnappe_seq as lagnappe_id,b.lagnappe_name,a.lagnappe_no"+
					 " FROM eip.ComboSale_lagnappe a, eip.lagnappe b, eip.comboSale_subject c, eip.comboSale d"+
			         " where a.comboSale_id=c.comboSale_id and a.lagnappe_id = b.lagnappe_seq and d.comboSale_seq=c.comboSale_id and d.isCombo=0 and c.subject_id="+subject_id;
		List<ComboSale_lagnappe> LComboSale_lagnappe = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale_lagnappe(
				result.getString("lagnappe_id"),
				result.getString("lagnappe_name"),
				result.getString("lagnappe_no")
		));		
		return LComboSale_lagnappe;
	}
	
	public List<ComboSale_mock> getMockBySubject_id(String subject_id){
		
		String sql = "SELECT b.mock_seq as mock_id,b.mock_name"+
		             " FROM eip.ComboSale_mock a, eip.mock b, eip.comboSale_subject c, eip.comboSale d"+
			         " where a.comboSale_id=c.comboSale_id and a.mock_id = b.mock_seq and d.comboSale_seq=c.comboSale_id and d.isCombo=0 and c.subject_id="+subject_id;
		List<ComboSale_mock> LComboSale_mock = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale_mock(
				result.getString("mock_id"),
				result.getString("mock_name")
		));		
		return LComboSale_mock;
	}
	
	public List<ComboSale_outPublisher> getOutPublisherBySubject_id(String subject_id){
		
		String sql = "SELECT b.books_seq as book_id,b.bookName as book_name,b.sellPrice"+
		             " FROM eip.ComboSale_outPublisher a, eip.books b, eip.comboSale_subject c, eip.comboSale d"+
			         " where a.comboSale_id=c.comboSale_id and a.book_id = b.books_seq and d.comboSale_seq=c.comboSale_id and d.isCombo=0 and c.subject_id="+subject_id;
		List<ComboSale_outPublisher> LComboSale_outPublisher = jdbcTemplate.query(sql,(result,rowNum)->new ComboSale_outPublisher(
				"",
				"",
				result.getString("book_id"),
				result.getString("book_name"),
				result.getString("sellPrice")
		));		
		return LComboSale_outPublisher;
	}	
	
	//for 單科使用
	public List<ComboSale_subject> GetSubjectComboSeq(String category_id,String subject_id,String same_originSubjectName_comboName) {		
		String sql2 = "select a.comboSale_subject_seq,a.comboSale_id,b.name,a.subject_id"+
				      " from eip.comboSale_subject a, subject b, comboSale c"+
				      " where a.subject_id = b.subject_seq and a.comboSale_id = c.comboSale_seq and c.isCombo=0";
		
		if (category_id != null && !category_id.isEmpty()) {
			sql2 += " and b.category_id = "+category_id;
		}
		if (subject_id != null && !subject_id.isEmpty()) {
			sql2 += " and a.subject_id = "+subject_id;
		}
        //找出單科-->相同單科名稱與科目名稱
		if (same_originSubjectName_comboName != null && same_originSubjectName_comboName.equals("1")) {
			sql2 += " and c.name = c.originSubjectName";
		}		
	
		List<ComboSale_subject> LComboSale_subject = jdbcTemplate.query(sql2,(result,rowNum)->new ComboSale_subject(
				result.getString("comboSale_subject_seq"),
				result.getString("comboSale_id"),
				result.getString("subject_id"),
	    		result.getString("name"),
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
                ""
        ));
		return LComboSale_subject;
	}
	public Boolean UpdatecomboSaleStatus(String comboSale_seq,String FlowStatus_code) {
		jdbcTemplate.update("Update eip.comboSale set FlowStatus_code =? where comboSale_seq=?;",
				FlowStatus_code,
				comboSale_seq		
		);
		return true;
	}

	public List<CounselingLimit> getCounselingLimit(String comboSale_id){ 
		String sql = "SELECT * FROM eip.counselingLimit where comboSale_id="+comboSale_id;
		List<CounselingLimit> LCounselingLimit = jdbcTemplate.query(sql,(result,rowNum)->new CounselingLimit(
	    		result.getString("counselingLimit_seq"),
                result.getString("comboSale_id"),
                result.getString("counselingLimitClass"),
                "",
                result.getString("creater"),
                result.getString("update_time")
        ));
		return LCounselingLimit;
	}	

	
	public List<CounselingLimitSubject> getCounselingLimitSubject(String counselingLimit_id){ 
		String sql = "SELECT a.*,b.name as subject_name FROM eip.counselingLimitSubject a, subject b"+
				     " where a.subject_id=b.subject_seq and a.counselingLimit_id="+counselingLimit_id;
	
		List<CounselingLimitSubject> LCounselingLimitSubject = jdbcTemplate.query(sql,(result,rowNum)->new CounselingLimitSubject(
	    		result.getString("counselingLimitSubject_seq"),
                result.getString("counselingLimit_id"),
                result.getString("subject_id"),
                result.getString("subject_name")
        ));
		
		return LCounselingLimitSubject;
	}
	
	public Boolean CounselingSettingSave(String comboSale_id,String counselingLimitClass,String[] A_subject_id,String creater) {
		jdbcTemplate.update("INSERT INTO eip.counselingLimit VALUES (default,?,?,?,CURDATE());",
				comboSale_id,
				counselingLimitClass,
				creater
		);	
		
		int newSeq  = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
		for(int i=0;i<A_subject_id.length;i++) {
			jdbcTemplate.update("INSERT INTO eip.counselingLimitSubject VALUES (default,?,?);",
					newSeq,
					A_subject_id[i]
			);			
		}
		return true;
	}
	
	
	public Boolean counselingCostSave(Counseling counseling,String counseling_seq) {
		if(counseling_seq==null || counseling_seq.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.counseling VALUES (default,?,?,?,?);",
				counseling.getCategory_id(),
				counseling.getCounseling_name(),
				counseling.getOrigin_price(),
				counseling.getActive()
			);
	    }else{
			jdbcTemplate.update("update eip.counseling set category_id=?,counseling_name=?,origin_price=?,active=? where counseling_seq=? ;",
					counseling.getCategory_id(),
					counseling.getCounseling_name(),
					counseling.getOrigin_price(),
					counseling.getActive(),
					counseling_seq
			);	    	
	    }
		return true;
	}	
}
