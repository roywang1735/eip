package com.wordgod.eip.Service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wordgod.eip.Model.*;

@Service
public class SalesService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	CourseSaleService courseSaleService;
	@Autowired
	CourseService courseService;
	@Autowired
	AdmService admService;
	@Autowired
	AccountService accountService;

	public List<Student> getStudent(String student_seq,String ch_name,String en_name,String student_no,String idn,String mobile_1,String email,String create_time,String degree,String cowork) {

		String sql = "select a.*,b.ch_name as createrName from eip.student a left join eip.employee b"
				+ " on a.creater=b.jl_code where 1=1";
		if (student_seq != null && !student_seq.isEmpty()) {
			sql += " and student_seq = " + student_seq.trim();
		}
		if (ch_name != null && !ch_name.isEmpty()) {
			sql += " and a.ch_name like '%"+ch_name.trim()+"%'";
		}
		if (en_name != null && !en_name.isEmpty()) {
			sql += " and a.en_name like '%"+en_name.trim()+"%'";
		}
		if (student_no != null && !student_no.isEmpty()) {
			sql += " and student_no = '" + student_no.trim() + "'";
		}
		if (idn != null && !idn.isEmpty()) {
			sql += " and a.idn = '" + idn.trim() + "'";
		}
		if (mobile_1 != null && !mobile_1.isEmpty()) {
			sql += " and mobile_1 = '" + mobile_1.trim() + "'";
		}
		if (email != null && !email.isEmpty()) {
			sql += " and email = '" + email.trim() + "'";
		}		
		if (create_time != null && !create_time.isEmpty()) {
			sql += " and create_time <= '" + create_time.trim() + "'";
		}
		if (degree != null && !degree.isEmpty()) {
			sql += " and degree = " + degree.trim();
		}
		if (cowork != null && !cowork.isEmpty()) {
			sql += " and cowork like '%"+cowork.trim()+"%'";
		}		

		sql+=" order by create_time desc LIMIT 200";

		List<Student> LStudent = jdbcTemplate.query(sql,
				(result, rowNum) -> new Student(
						result.getString("student_seq"),
						result.getString("student_no"),
						result.getString("ch_name"), 
						result.getString("en_name"),
						result.getString("sex") == null ? "" : 
						result.getString("sex"),
						result.getString("category"),
						result.getString("derived"),
						result.getString("idn"),
						result.getString("birthday"),
						result.getString("identity"),
						result.getString("tel"), 
						result.getString("mobile_1"),
						result.getString("mobile_2"),
						result.getString("email_1"),
						result.getString("email_2"),
						result.getString("address_1"),
						result.getString("address_2"),
						result.getString("fb"),
						result.getString("line"), 
						result.getString("parent_1_name"),
						result.getString("parent_1_mobile"),
						result.getString("parent_1_email"),
						result.getString("parent_1_line"),
						result.getString("parent_2_name"),
						result.getString("parent_2_mobile"),
						result.getString("parent_2_email"),
						result.getString("parent_2_line"), 
						result.getString("createrName")==null? result.getString("creater"):result.getString("createrName"),
						result.getString("create_time"),
						result.getString("editor"),
						result.getString("remark") == null ? "": result.getString("remark").replaceAll("(\r\n|\r|\n|\n\r)", "<br>"),
						result.getString("remark2") == null ? "": result.getString("remark2").replaceAll("(\r\n|\r|\n|\n\r)", "<br>"),
						result.getString("special_idn"), 
						result.getString("col_3"), //exam
						result.getString("col_4"), //abroad_date
						result.getString("balanceTotal") == null ? "0" : result.getString("balanceTotal"),
						result.getString("makeUpTotal") == null ? "0" : result.getString("makeUpTotal"),"<img src=\'/images/studentPhoto/"+result.getString("photo")+"\' height=\'80px\'/>",
					    result.getString("school_code"),
					    result.getString("school_code2"),
					    result.getString("updater"),
					    result.getString("update_time"),
					    result.getString("col_5"),
					    "",
					    result.getString("remarkTotal"),
					    result.getString("handover"),
					    result.getString("tel2"),
					    result.getString("company_tel"),
					    result.getString("agent_studentNo"),
					    result.getString("grade_highSchool"),
					    result.getString("postCode"),
					    result.getString("agent_studentName"),
					    result.getString("passwd"),
					    result.getString("col_6"),
					    result.getString("col_7"),
					    result.getString("col_8"),
					    result.getString("col_9"),
					    result.getString("degree"),
					    result.getString("cowork"),
					    "",
					    ""
				));
		
		return LStudent;
	}
	
	public List<Student> getStudentRecord(String student_seq) {

		String sql = "select a.*,b.ch_name as createrName from eip.studentRecord a left join eip.employee b"
				+ " on a.creater=b.jl_code where 1=1";
		if (student_seq != null && !student_seq.isEmpty()) {
			sql += " and student_seq = '" + student_seq+"'";
		}

		sql+=" order by create_time desc";
		

		List<Student> LStudent = jdbcTemplate.query(sql,
				(result, rowNum) -> new Student(
						result.getString("student_seq"),
						result.getString("student_no"),
						result.getString("ch_name"), 
						result.getString("en_name"),
						result.getString("sex") == null ? "" : 
						result.getString("sex"),
						result.getString("category"),
						result.getString("derived"),
						result.getString("idn"),
						result.getString("birthday"),
						result.getString("identity"),
						result.getString("tel"), 
						result.getString("mobile_1"),
						result.getString("mobile_2"),
						result.getString("email_1"),
						result.getString("email_2"),
						result.getString("address_1"),
						result.getString("address_2"),
						result.getString("fb"),
						result.getString("line"), 
						result.getString("parent_1_name"),
						result.getString("parent_1_mobile"),
						result.getString("parent_1_email"),
						result.getString("parent_1_line"),
						result.getString("parent_2_name"),
						result.getString("parent_2_mobile"),
						result.getString("parent_2_email"),
						result.getString("parent_2_line"), 
						result.getString("createrName")==null? result.getString("creater"):result.getString("createrName"),
						result.getString("create_time"),
						result.getString("editor"),
						result.getString("remark") == null ? "": result.getString("remark").replaceAll("(\r\n|\r|\n|\n\r)", "<br>"),
						result.getString("remark2") == null ? "": result.getString("remark2").replaceAll("(\r\n|\r|\n|\n\r)", "<br>"),
						result.getString("special_idn"), 
						result.getString("col_3"), //exam
						result.getString("col_4"), //abroad_date
						result.getString("balanceTotal") == null ? "0" : result.getString("balanceTotal"),
						result.getString("makeUpTotal") == null ? "0" : result.getString("makeUpTotal"),"<img src=\'/images/studentPhoto/"+result.getString("photo")+"\' height=\'80px\'/>",
					    result.getString("school_code"),
					    result.getString("school_code2"),
					    result.getString("updater"),
					    result.getString("update_time"),
					    result.getString("col_5"),
					    "",
					    result.getString("remarkTotal"),
					    result.getString("handover"),
					    result.getString("tel2"),
					    result.getString("company_tel"),
					    result.getString("agent_studentNo"),
					    result.getString("grade_highSchool"),
					    result.getString("postCode"),
					    result.getString("agent_studentName"),
					    result.getString("passwd"),
					    result.getString("col_6"),
					    result.getString("col_7"),
					    result.getString("col_8"),
					    result.getString("col_9"),
					    result.getString("degree"),
					    result.getString("cowork"),
					    "",
					    ""
				));
		
		return LStudent;
	}	

 public int RegisterSave(
		 	String pop,
			Register register, 
			String[] A_comboSale_seq,
			String[] A_Register_comboSale_seq,
			String[] A_subject_seq,
			String[] A_classPromotion_id, 
			String[] A_lagnappe_seq, 
			String[] A_lagnappe_no, 
			String[] A_mock_seq,
			String[] A_counseling_seq, 
			String[] A_replaceFrom,
			String[] A_books_seq,
			String[] A_addMoney,
			String[] A_salePrice,
			String[] A_freeChoice,
			String[] A_Register_comboSale_seq_off 
 ){
//*******(A)報名or改報新增*************//
   int newSeq = -1;	
   int newSeq_2 = -1;
   if (register.getRegister_seq() == null || register.getRegister_seq().isEmpty() || (pop!=null && pop.equals("re_register"))) {

	   //eip.Register
			jdbcTemplate.update("INSERT INTO eip.Register VALUES (default,?,?,?,?,?,?,?,NOW(),0);",
					register.getStudent_seq(), 
					register.getOriginPrice(), 
					register.getActualPrice(), 
					register.getPaid(),
					register.getComment(), 
					register.getCreater(), 
					register.getOrderStatus()
			);
			newSeq = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);

			// eip.Register_comboSale 學生報名_單科或套裝
			if (A_comboSale_seq != null) {
				String subject_id_virtual = null;
				for (int i = 0; i < A_comboSale_seq.length; i++) {
					subject_id_virtual = "";
					String isVirtual = courseService.getSubject("",A_subject_seq[i].split("@")[0],"","","","0").get(0).getIsVirtual();
					if (isVirtual.equals("1")) {
						subject_id_virtual = A_subject_seq[i].split("@")[0];
					}
					
					Subject subject = courseService.getSubject("",A_subject_seq[i].split("@")[0],"","","","0").get(0); 
					jdbcTemplate.update("INSERT INTO eip.Register_comboSale VALUES (default,?,?,?,?,?,?,?,?,?,?);", 
							newSeq,
							A_comboSale_seq[i], //單科或套裝
							A_subject_seq[i].split("@")[0], // @Subject_seq@HrPrice_R@CounselingPrice_R@LagnappePrice_R@HandoutPrice_R@HomeworkPrice_R@MockPrice_R
							"-99", //預設gradeNo未定班時起始值, 
							subject_id_virtual, 
							A_replaceFrom[i],
							"",		
							"1",
							A_salePrice[i],
							subject.getPrice()
					);
					newSeq_2 = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
					
					//贈課
					if (A_freeChoice[i].equals("2")) { //失誤贈課					
						jdbcTemplate.update("INSERT INTO eip.FreeClass VALUES (default,?,?);", 
								2,
								newSeq_2
						);
					}				
				}
			}		
            
			//優惠
			if (A_classPromotion_id != null) {
				for (int i = 0; i < A_classPromotion_id.length; i++) {
					jdbcTemplate.update("INSERT INTO eip.registerPromo VALUES (default,?,?);", 
							newSeq,
							A_classPromotion_id[i]);
				}
			}
			
			//新增報名其他物品
			newRegisterOther(newSeq,A_comboSale_seq,A_lagnappe_seq,A_lagnappe_no,A_mock_seq,A_counseling_seq,A_books_seq);
			
	        //****改報****
	        if(pop!=null && pop.equals("re_register")) {        	
	        	//將原來的訂單改掉
	    	    jdbcTemplate.update("Update eip.Register set cancelRegister=? where Register_seq=?;",
	    	    		2, //0預設值,1退費訂單,2改報訂單   
	    				register.getRegister_seq()
	    		);
	    	        	    
	    	    //原來的已訂期別取消處理(Register-->多個Register_comboSale-->(一個科目多個班別)多個Register_comboSale_grade)
	    	    List<Register_comboSale> LRegister_comboSale = getComboSaleByRegister("",register.getRegister_seq(),"","","","1",false,false,"");	    	    
				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
				String curDate = sdFormat.format(new Date());	
	    	    for(int x=0;x<LRegister_comboSale.size();x++) {	    	    	
	    	    	//1.課程處理
	    	    	Boolean processed = false; //表示此舊的subject是否已經過判斷
	    	    	List<Register_comboSale_grade> LRegister_comboSale_grade = courseService.getRegister_comboSale_grade(LRegister_comboSale.get(x).getRegister_comboSale_seq(),"","(1)","1");//1:register_status訂班//1:active	    	    		    	    	
	    	    	//判斷此科目下的所有班是否可取消或續存
	    	    	Boolean cancelSubject = true;
		    	    	for(int i=0;i<LRegister_comboSale_grade.size();i++) {
		    	    		//實體班(尚未開課)
		    	    		if(LRegister_comboSale_grade.get(i).getClass_style().equals("1")) {
		    	    			String startDate = courseService.getClassStartDate(LRegister_comboSale_grade.get(i).getGrade_id());
		    	    			
		    	    			if(startDate!=null && startDate.length()==10) {
		    						String classDate = startDate.substring(6,10)+startDate.substring(0,2)+startDate.substring(3,5);
		    						if(Integer.valueOf(curDate)>=Integer.valueOf(classDate)) {
		    							cancelSubject = false; //續存
		    						}
		    					}
		    	    		//Video班(尚未上過Video)	
		    	    		}else{
		    	    			List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("","","","1","(1)","",LRegister_comboSale_grade.get(i).getRegister_comboSale_grade_seq(),"","","","");
		    	    			for(int a=0;a<LSignRecordHistory.size();a++) {
		    	    				String attendDate = LSignRecordHistory.get(a).getAttend_date();
			    	    			if(attendDate!=null && attendDate.length()==10) {
			    						String classDate = attendDate.substring(6,10)+attendDate.substring(0,2)+attendDate.substring(3,5);
			    						if(Integer.valueOf(curDate)>=Integer.valueOf(classDate)) {
			    							cancelSubject = false; //續存
			    						}
			    					}	    	    				
		    	    			}	    	    			
		    	    		}
		    	    	} 
	    	    		//A.取消此subject下面所有班
		    	    	if(cancelSubject) {
		    	    		processed = true;
	    	    			for(int i=0;i<LRegister_comboSale_grade.size();i++) {
	    	    				admService.UpdateRegisterStatus(LRegister_comboSale_grade.get(i).getRegister_comboSale_grade_seq(),LRegister_comboSale.get(x).getRegister_comboSale_seq(),"2");
	    	    			}		
	    	    		//B.若有相同科目則班別續存
	    	    		}else{
	    	    			List<Register_comboSale> LRegister_comboSale_new = getComboSaleByRegister("",String.valueOf(newSeq),"","","","1",false,false,"");
	    	    			for(int i=0;i<LRegister_comboSale_new.size();i++) {
	    	    				 if(LRegister_comboSale.get(x).getSubject_id().equals(LRegister_comboSale_new.get(i).getSubject_id()) && LRegister_comboSale_new.get(i).getGradeNoLeft()!="0") {
			    	    			 processed = true;
	    	    					 for(int j=0;j<LRegister_comboSale_grade.size();j++) { //移轉所有班別
	    	    						if (LRegister_comboSale_new.get(i).getGradeNoLeft()!="0") {
		    	    						jdbcTemplate.update("Update eip.Register_comboSale_grade set Register_comboSale_id=? where Register_comboSale_grade_seq=?;",
				    	    	    	    		LRegister_comboSale_new.get(i).getRegister_comboSale_seq(), 
				    	    	    	    		LRegister_comboSale_grade.get(j).getRegister_comboSale_grade_seq()
				    	    	    		);
				    	    	    	    //減少已移轉gradeNoLeft
		    	    						List<Grade> LGrade = courseService.getGrade(LRegister_comboSale_grade.get(j).getGrade_id(),"","","","","","","200","","","","","");
		    	    						if(LGrade.size()>0){
			    	    						List<SubjectTeacher> LSubjectTeacher = courseService.getSubjectTeacher2("",LGrade.get(0).getTeacher_id(),LGrade.get(0).getSubject_id(),"","1");
					    	    	    	    int totalGradeNo = 0;
					    	    	    	    if(LSubjectTeacher.get(0).getGradeNo()!=null && LSubjectTeacher.get(0).getGradeNo().isEmpty()) {
					    	    	    	    	totalGradeNo = Integer.valueOf(LSubjectTeacher.get(0).getGradeNo())-1;
					    	    	    	    };
			    	    						jdbcTemplate.update("Update eip.Register_comboSale set gradeNoLeft=? where Register_comboSale_seq=?;",
			    	    								totalGradeNo,
			    	    								LRegister_comboSale_new.get(i).getRegister_comboSale_seq()
					    	    	    		);
		    	    						}    
	    	    					    }    
			    	    			 }
	    	    				 	 
	    	    				 }
	    	    	        }	    	    	
		    	        }
	    	    	
		    	    	//若此subject尚未處理,直接取消	
		    	    	if(!processed) {
	    	    			for(int i=0;i<LRegister_comboSale_grade.size();i++) {
	    	    				admService.UpdateRegisterStatus(LRegister_comboSale_grade.get(i).getRegister_comboSale_grade_seq(),LRegister_comboSale.get(x).getRegister_comboSale_seq(),"2");
	    	    			}	    	    	
		    	    	}
		    	    	
		    	   //2.模考取消處理
		    	    	List<Register_mock> LRegister_mock = getMockByRegisterSeq(LRegister_comboSale.get(x).getRegister_id(),"","1");	
		    	    	for(int a=0;a<LRegister_mock.size();a++) {
		    			    jdbcTemplate.update("Update eip.Register_mock set active=? where Register_seq=? and comboSale_id=?;",
		    						0,
		    						LRegister_comboSale.get(x).getRegister_id(),
		    						LRegister_comboSale.get(x).getComboSale_id()
		    						
		    		    	);		    	    		
		    	    	}
		    	    	
		    	   //3.充電站取消處理 
		    	    	
		    	    	
		    	    	
	          }	//for(int x=0;x<LRegister_comboSale.size();x++) {	  		
	      } //if(pop!=null && pop.equals("re_register")) {
	        
	        
//******(B)換課更新eip.Register*******//
  }else{
	    //結清未結清若應繳變動則改為未結清orderStatus=1  
	    Register register2 = getRegisterList("","",register.getRegister_seq(),"").get(0);
	    if(!register2.getActualPrice().equals(register.getActualPrice())) {
		    jdbcTemplate.update("Update eip.Register set orderStatus=? where Register_seq=?;",
					1,
					register.getRegister_seq()
	    	);		
	    }
	    
	    jdbcTemplate.update("Update eip.Register set originPrice=?,actualPrice=?,comment=?,creater=?,update_time=CURDATE() where Register_seq=?;",
				register.getOriginPrice(), 
				register.getActualPrice(),
				register.getComment(), 
				register.getCreater(), 
				register.getRegister_seq()
		);
	    
		//將刪除的eip.Register_comboSale改為 not Active
	    if(A_Register_comboSale_seq_off!=null) {
		    for(int i=0;i<A_Register_comboSale_seq_off.length;i++) {
				jdbcTemplate.update("update eip.Register_comboSale set active = ? where Register_comboSale_seq=?", 
	                   0, // not Active
	                   A_Register_comboSale_seq_off[i].split("@")[0] 
				);
		    }
	    }    
		//eip.Register_comboSale 學生報名_單科或套裝
		if (A_comboSale_seq != null) {
			String subject_id_virtual = null;
			for (int i = 0; i < A_comboSale_seq.length; i++) {
				Subject subject = courseService.getSubject("",A_subject_seq[i].split("@")[0],"","","","0").get(0); 
				subject_id_virtual = "";
				String isVirtual = courseService.getSubject("", A_subject_seq[i].split("@")[0],"","","","0").get(0).getIsVirtual();
				//String gradeNo = courseService.getSubject("", A_subject_seq[i].split("@")[0], "", "").get(0).getGradeNo();
				String gradeNo = "-99";
				if (isVirtual.equals("1")) {
					subject_id_virtual = A_subject_seq[i].split("@")[0];
				}
				
				//若A_replaceFrom==1, 則為更新的科目
				if(A_replaceFrom[i]!=null && A_replaceFrom[i].equals("1")) {					
					String comboSale_id = A_comboSale_seq[i];
					//若為單科,更換單科名稱
					String isCombo = courseSaleService.getComboSale(A_comboSale_seq[i],"","","","","","","0").get(0).getIsCombo();	
					if(isCombo.equals("0")) {
						//找出此科目的單科comboSale_id
						List<ComboSale_subject> LComboSale_subject = courseSaleService.GetSubjectComboSeq("",A_subject_seq[i].split("@")[0],"1");
						comboSale_id = LComboSale_subject.get(0).getComboSale_id();
					}	

					jdbcTemplate.update("INSERT INTO eip.Register_comboSale VALUES (default,?,?,?,?,?,?,?,?,?,?);", 
							register.getRegister_seq(),
							comboSale_id, 
							A_subject_seq[i].split("@")[0], // @Subject_seq@HrPrice_R@CounselingPrice_R@LagnappePrice_R@HandoutPrice_R@HomeworkPrice_R@MockPrice_R
							gradeNo, 
							subject_id_virtual, 
							"1",//A_replaceFrom[i],不再使用		
							"", //A_Register_comboSale_seq[i] 不再使用		
							"1",
							A_salePrice[i], //costShare
							subject.getPrice() //報名當下此科目原價
					);					
					jdbcTemplate.update("INSERT INTO eip.orderChange VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?);",
							register.getStudent_seq(),
							register.getRegister_seq(),
							"1", // 1換課,3.已繳更改,4.應繳更改,5.繳費方式更改
							"", 
							A_subject_seq[i].split("@")[0], 
							"", 
							"",
							"",
							"",
							"",
							register.getActualPrice(), 
							register.getCreater(),
							""
					);					
													
				}
				


			}
		}		
		//優惠
		if (A_classPromotion_id != null) {
			if(register.getRegister_seq()!=null && !register.getRegister_seq().isEmpty()) {
				jdbcTemplate.update("DELETE from eip.registerPromo where register_id=?", register.getRegister_seq());
			}	
			for (int i = 0; i < A_classPromotion_id.length; i++) {
				jdbcTemplate.update("INSERT INTO eip.registerPromo VALUES (default,?,?);", 
						register.getRegister_seq(),
						A_classPromotion_id[i]);
			}
		}

	}		
		return newSeq;
 }
 
    public void newRegisterOther(int newSeq,String[] A_comboSale_seq,String[] A_lagnappe_seq,String[] A_lagnappe_no,String[] A_mock_seq,String[] A_counseling_seq,String[] A_books_seq) {
    	
        /*** 1. 贈品 *****/
		// 本次報名新增
		if (A_lagnappe_seq != null) {
			for (int i = 0; i < A_lagnappe_seq.length; i++) {
				jdbcTemplate.update("INSERT INTO eip.Register_lagnappe VALUES (default,?,?,?,?,?,?,?,?);", 
						"-1", // comboSale_id無值,// 預設// -1
						newSeq, 
						A_lagnappe_seq[i], 
						A_lagnappe_no[i], 
						0, 
						"", 
						"", 
						""
				);
			}
		}
		// 本次報名套裝標準
		String unique = "-1";
		for (int i = 0; i < A_comboSale_seq.length; i++) {
			if (!A_comboSale_seq[i].equals(unique)) {
				List<ComboSale_lagnappe> LComboSale_lagnappe = courseSaleService.getComboSale_lagnappe(A_comboSale_seq[i]);
				for (int j = 0; j < LComboSale_lagnappe.size(); j++) {
					jdbcTemplate.update("INSERT INTO eip.Register_lagnappe VALUES (default,?,?,?,?,?,?,?,?,?);",
							A_comboSale_seq[i], 
							newSeq, // Register_seq,
							LComboSale_lagnappe.get(j).getLagnappe_id(), 
							LComboSale_lagnappe.get(j).getLagnappe_no(), 
							0,
							"", 
							"", 
							"",
							""
					);
					//若為Video點數,需更改student.[makeUpTotal]
			/**	需發放	
					Lagnappe lagnappe = courseSaleService.getLagnappe(LComboSale_lagnappe.get(j).getLagnappe_id()).get(0);
					if(lagnappe.getLagnappe_name().equals("Video點數")) {
			    		Student student = getStudent(register.getStudent_seq(),"","","","","","","").get(0);
			    		updateMakeUpTotal(
			    				student.getStudent_no(),
			    				Integer.valueOf(student.getMakeUpTotal())+Integer.valueOf(LComboSale_lagnappe.get(j).getLagnappe_no()),
			    				LComboSale_lagnappe.get(j).getLagnappe_no(),
			    				register.getStudent_seq(),
			    				creater,
			    				"1" //1:課程新增
			    		);
					}
			**/			
				}
			}
			unique = A_comboSale_seq[i];
		}

        /***2. 模考 *****/
		// 本次報名新增
		if (A_mock_seq != null) {
			for (int i = 0; i < A_mock_seq.length; i++) {
				jdbcTemplate.update("INSERT INTO eip.Register_mock VALUES (default,?,?,?,?);", 
						"-1", // comboSale_id無值, 預設// -1
						newSeq, 
						A_mock_seq[i],
						1
				);
			}
		}

		// 本次報名套裝標準
		String unique1 = "-1";
		for (int i = 0; i < A_comboSale_seq.length; i++) {
			if (!A_comboSale_seq[i].equals(unique1)) {
				List<ComboSale_mock> LComboSale_mock = courseSaleService.getComboSale_mock(A_comboSale_seq[i]);
				for (int j = 0; j < LComboSale_mock.size(); j++) {
					
						jdbcTemplate.update("INSERT INTO eip.Register_mock VALUES (default,?,?,?,?);", 
								A_comboSale_seq[i],
								newSeq, // Register_seq,
								LComboSale_mock.get(j).getMock_id(),
								1
						);
				}
				unique1 = A_comboSale_seq[i];
			}
		}
		
		/***3. 充電站 *****/
		// 本次報名新增
		if (A_counseling_seq != null) {
			for (int i = 0; i < A_counseling_seq.length; i++) {
				jdbcTemplate.update("INSERT INTO eip.Register_counseling VALUES (default,?,?,?);", 
						"-1", // comboSale_id無值,																											// 預設 -1
						newSeq, 
						A_counseling_seq[i]
				);
			}
		}

		// 本次報名套裝標準
		String unique2 = "-1";
		for (int i = 0; i < A_comboSale_seq.length; i++) {
			List<ComboSale_counseling> LComboSale_counseling = courseSaleService.getComboSale_counseling(A_comboSale_seq[i]);
			for (int j = 0; j < LComboSale_counseling.size(); j++) {
				if (!A_comboSale_seq[i].equals(unique2)) {
					jdbcTemplate.update("INSERT INTO eip.Register_counseling VALUES (default,?,?,?);",
							A_comboSale_seq[i], 
							newSeq, // Register_seq,
							LComboSale_counseling.get(j).getCounseling_id());
				}				
			}
			unique2 = A_comboSale_seq[i];
		}		

		/***4. 外版書 *****/
		// 本次報名新增
		if (A_books_seq != null) {
			for (int i = 0; i < A_books_seq.length; i++) {
				jdbcTemplate.update("INSERT INTO eip.Register_outPublisher VALUES (default,?,?,?,?,?,?,?);", 
						"-1", // comboSale_id無值,// 預設 -1
						newSeq, 
						A_books_seq[i], 
						0,
						"", 
						"", 
						""
				);
			}
		}

		// 本次報名套裝標準
		String unique3 = "-1";
		for (int i = 0; i < A_comboSale_seq.length; i++) {
			List<ComboSale_outPublisher> LComboSale_outPublisher = courseSaleService.getComboSale_outPublisher(A_comboSale_seq[i]);
			for (int j = 0; j < LComboSale_outPublisher.size(); j++) {
				if (!A_comboSale_seq[i].equals(unique3)) {
					jdbcTemplate.update("INSERT INTO eip.Register_outPublisher VALUES (default,?,?,?,?,?,?,?,?);",
							A_comboSale_seq[i], 
							newSeq, // Register_seq,
							LComboSale_outPublisher.get(j).getBook_id(),
							0,
							"", 
							"", 
							"",
							""
					);			
				}			
			}
			unique3 = A_comboSale_seq[i];
		}		
    }

	public List<Register_comboSale> getComboSaleByRegister(
			String Register_comboSale_seq, 
			String Register_seq,
			String student_seq, 
			String subject_id, 
			String comboSale_id, 
			String active,
			Boolean subject_id_virtual_off,
			Boolean subject_id_child_off,
			String from_Register_comboSale_id
		) {
		String sql = "select a.*,b.class_style,b.name as comboSale_name, b.isCombo,c.name as subject_name, f.name as subject_virtual_name,d.student_seq,e.student_no,e.ch_name,b.originSubjectName"
				+ " from eip.Register_comboSale a" + " left join subject f on a.subject_id_virtual=f.subject_seq,"
				+ " eip.comboSale b,subject c,Register d,student e" +
				" where a.comboSale_id=b.comboSale_seq and a.subject_id=c.subject_seq and d.Register_seq=a.Register_id and d.student_seq=e.student_seq";

		if (Register_comboSale_seq != null && !Register_comboSale_seq.isEmpty()) {
			sql += " and a.Register_comboSale_seq = " + Register_comboSale_seq;
		}
		if (Register_seq != null && !Register_seq.isEmpty()) {
			sql += " and d.Register_seq = " + Register_seq;
		}
		if (student_seq != null && !student_seq.isEmpty()) {
			sql += " and d.student_seq = " + student_seq;
		}
		if (comboSale_id != null && !comboSale_id.isEmpty()) {
			sql += " and a.comboSale_id = " + comboSale_id;
		}
		if (subject_id != null && !subject_id.isEmpty()) {
			sql += " and a.subject_id = " + subject_id;
		}
		if (active != null && !active.isEmpty()) {
			sql += " and a.active = " + active;
		}
		if(subject_id_virtual_off) {
			sql += " and NOT (a.subject_id = a.subject_id_virtual and a.gradeNoLeft=0)";
		}
		if(subject_id_child_off) {
			sql += " and a.from_Register_comboSale_id=''";
		}
		if(from_Register_comboSale_id != null && !from_Register_comboSale_id.isEmpty()) {
			sql += " and a.from_Register_comboSale_id="+from_Register_comboSale_id;
		}	
		sql += " order by a.Register_comboSale_seq";

		List<Register_comboSale> LRegister_comboSale = jdbcTemplate.query(sql,
				(result, rowNum) -> new Register_comboSale(
						result.getString("Register_comboSale_seq"),
						result.getString("Register_id"), 
						result.getString("comboSale_id"),
						result.getString("comboSale_name"),
						result.getString("isCombo"),
						result.getString("subject_id"),
						result.getString("subject_id_virtual"), 
						result.getString("subject_name"),
						result.getString("subject_virtual_name"), 
						"", 
						result.getString("student_seq"),
						result.getString("student_no"), 
						result.getString("ch_name"), 
						"",
						result.getString("gradeNoLeft"), 
						result.getString("replaceFrom"),
						result.getString("from_Register_comboSale_id"),
						result.getString("active"),
						result.getString("costShare"),
						result.getString("class_style"),
						"", //category_name,
						result.getString("originSubjectName"),
						result.getString("subject_price")
		));
        
		String category_name = "";
		for(int i=0;i<LRegister_comboSale.size();i++) {
			category_name = courseService.getSubject("",LRegister_comboSale.get(i).getSubject_id(),"","","","").get(0).getCategory_name();
			LRegister_comboSale.get(i).setCategory_name(category_name);
		}
		 
		return LRegister_comboSale;
	}

	public List<Register> getRegisterList(String student_seq,String Action,String Register_seq,String in_cancelRegister) {
		String sql = "select a.*,b.name as orderStatusName from eip.Register a, eip.OrderStatus b"
				+ " where a.orderStatus=b.code";
		if (student_seq != null && !student_seq.isEmpty()) {
			sql += " and a.student_seq = " + student_seq;
		}
		if (Register_seq != null && !Register_seq.isEmpty()) {
			sql += " and a.Register_seq = " + Register_seq;
		}
		
		if (in_cancelRegister != null && !in_cancelRegister.isEmpty()) {
			sql += " and a.cancelRegister in "+in_cancelRegister;
		}
		
		sql += " order by Register_seq desc";
		
		List<Register> LRegister = jdbcTemplate.query(sql,(result, rowNum) -> new Register(
						result.getString("Register_seq"), 
						result.getString("student_seq"),
						result.getString("originPrice"), 
						result.getString("actualPrice"),
						Integer.toString(Integer.valueOf(result.getString("originPrice"))- Integer.valueOf(result.getString("actualPrice"))),
						result.getString("paid"), 
						Integer.toString(Integer.valueOf(result.getString("actualPrice"))- Integer.valueOf(result.getString("paid"))),
						result.getString("comment"), 
						result.getString("creater"),
						result.getString("orderStatusName"), 
						result.getString("update_time"), 
						"", 
						"",
						"<A href='javascript:void(0)' onclick='openRemarkDetail(" + result.getString("Register_seq")+ ")'><img src='/images/more.png' height='13px'/></A>",
						"<A href='javascript:void(0)' onclick='openReceipDetail(" + result.getString("Register_seq")+ ")' style='padding:2px'><img src='/images/sheet.jpg' height='15px' title='明細' /></A>",
						result.getString("cancelRegister")
				));
		  

		
/***********************[繳費/變更]*****************************/
		if (Action.equals("Fee")) {
			// 設定該Register之ComboSaleString
			for (int i = 0; i < LRegister.size(); i++) {
				List<Register_comboSale> LRegister_comboSale = getComboSaleByRegister("",LRegister.get(i).getRegister_seq(),"","","","1",false,false,"");//不看自由選,看已選科目
				String comboSaleString = "";
				String comboName="";
				for (int j = 0; j < LRegister_comboSale.size(); j++) {
					if (LRegister_comboSale.get(j).getComboSale_name()!=null && !LRegister_comboSale.get(j).getComboSale_name().equals("")) {
						//單科套裝名稱重複,透明顏色
						if(LRegister_comboSale.get(j).getComboSale_name().equals(comboName)) {
							if(LRegister_comboSale.get(j).getIsCombo().equals("1")) {
								comboSaleString += "<span style='color:#ffffff;font-size:x-small'>["+LRegister_comboSale.get(j).getComboSale_name()+"]</span>";
							}
						//單科套裝名稱不重複
						}else {
							comboName = LRegister_comboSale.get(j).getComboSale_name();
							if(LRegister_comboSale.get(j).getIsCombo().equals("1")) {
								comboSaleString += "<span style='color:#555555;font-size:x-small'>["+ LRegister_comboSale.get(j).getComboSale_name()+"]</span>";
							}else {
								if(LRegister_comboSale.get(j).getComboSale_name().equals(LRegister_comboSale.get(j).getOriginSubjectName())) {
									comboSaleString += "<span style='color:#555555;font-size:x-small'>"+ LRegister_comboSale.get(j).getCategory_name()+"</span>";
								}else {
									comboSaleString += "<span style='color:#555555;font-size:x-small'>"+ LRegister_comboSale.get(j).getComboSale_name()+"</span>";
								}								
							}
						}
					}
					
					comboSaleString += "<span style='font-size:small;color:black'>-" + LRegister_comboSale.get(j).getSubject_name()+"</span><br>";
				}
				LRegister.get(i).setComboSaleString(comboSaleString);
			}
			

			
/***********************[期別選課]*****************************/
		} else if (Action.equals("Book")) {
			String comboSaleString = "";
			String bgcolor="white";
			String cancelRegister = "";
			for (int i = 0; i < LRegister.size(); i++) {
				if(i%2==1) {
					bgcolor="#ffefff";
				}else {
					bgcolor="white";
				}
				//退費or改報
				if(LRegister.get(i).getCancelRegister().equals("1")) {
					cancelRegister = "<span style='color:red'><img src='/images/cancel.jpg' height='10px'>已退費</span>";					
				}else if(LRegister.get(i).getCancelRegister().equals("2")) {
					cancelRegister = "<span style='color:red'><img src='/images/cancel.jpg' height='10px'>已改報</span>";					
				}else {
					cancelRegister = "";
				}
				//未結清   			
				List<Register_comboSale> LRegister_comboSale = getComboSaleByRegister("",LRegister.get(i).getRegister_seq(),"","","","1",true,false,"");
				String saleStr = LRegister.get(i).getUpdate_time()==null?"":LRegister.get(i).getUpdate_time()+"<br>"+(LRegister.get(i).getCreater()==null?"":LRegister.get(i).getCreater());
				comboSaleString = 
				"<div class='tr' style='background-color:"+bgcolor+"'>"+
				"<div class='th3' style='text-align:center;width:70px;font-size:x-small;vertical-align:middle'>"+saleStr+"</div>"+ //業務
				"<div class='th3' style='text-align:center;width:70px;font-size:small;vertical-align:middle'>"+
					"<span style='color:darkblue;letter-spacing:2px'>"+LRegister.get(i).getOrderStatus()+"</span><br>"+cancelRegister+ //費用(結清/未結清)
				"</div>"+					
				"<div class='th3'>"+
				  "<div class='css-table' style='border-spacing:1px'>";				

				String comboSaleString2="";
				for (int j=0; j<LRegister_comboSale.size();j++) {
	    			String class_styleName="";
	    			if(LRegister_comboSale.get(j).getClass_style()==null){
	    				class_styleName = "";
	    			}else if(LRegister_comboSale.get(j).getClass_style().equals("1")) {
	    				class_styleName = "";
	    			}else if(LRegister_comboSale.get(j).getClass_style().equals("3")) {
	    				class_styleName = "(線上)";
	    			}
					//虛擬科目
					String iSvirtual = "color:#000000"; 
					if(LRegister_comboSale.get(j).getSubject_id().equals(LRegister_comboSale.get(j).getSubject_id_virtual())) {
						iSvirtual = "color:#555555";
					}
					

/****************/
// 報名項目列表
/****************/
                    //報名項目 
					comboSaleString2 += 
								 "<div class='tr'>"+
									"<div class='th3' style='width:300px;padding:5px;font-size:small;letter-spacing:0px;vertical-align:middle'>";
								    if(LRegister_comboSale.get(j).getSubject_virtual_name()!=null && !LRegister_comboSale.get(j).getSubject_virtual_name().equals(LRegister_comboSale.get(j).getSubject_name())) {	
					                    comboSaleString2 += "<span style='color:#aaaaaa;font-size:x-small'>"+LRegister_comboSale.get(j).getSubject_virtual_name()+"</span>";
								    }
					                    comboSaleString2 += "<span style='"+iSvirtual+"'>&bull;"+ LRegister_comboSale.get(j).getSubject_name()+"</span>&nbsp;<span style='font-size:x-small'>"+class_styleName+"</span>&nbsp;" + 
					                "</div>"+
							        "<div class='th3' style='width:100px;text-align:center;font-size:small;vertical-align:middle'>";
					//退費or改報
					if(LRegister.get(i).getCancelRegister().equals("1") || LRegister.get(i).getCancelRegister().equals("2")) {
						comboSaleString2 += "-";
					//政龍未定不適用
				    }else if (LRegister_comboSale.get(j).getGradeNoLeft().equals("-1")) {
						comboSaleString2 += "-";
					// 已訂班
				    }else if (LRegister_comboSale.get(j).getGradeNoLeft().equals("0") && !LRegister_comboSale.get(j).getSubject_id().equals(LRegister_comboSale.get(j).getSubject_id_virtual())) {
						//comboSaleString2 += "<img src='/images/order.png' height='12px'/>";
				    	comboSaleString2 += "-";
					// 未訂班
					} else {
						// 尚未訂完此科目的班級
						if (!LRegister_comboSale.get(j).getSubject_id_virtual().equals(LRegister_comboSale.get(j).getSubject_id())) {
							        String waitGrade = "";
						        	String teacher_id = "-1";
							        String gradeNoLeft = LRegister_comboSale.get(j).getGradeNoLeft();
							        if(gradeNoLeft.equals("-99")) {
							        	waitGrade = "未訂";
							        }else {
							        	waitGrade = "待訂期別*"+gradeNoLeft;
							        	
							        	//將老師帶到前端比對是否在此科目選擇同一個期別
							        	List<Register_comboSale_grade> LRegister_comboSale_grade = getRegister_comboSale_grade("","",LRegister_comboSale.get(j).getRegister_comboSale_seq(),"");
							        	Grade grade = new Grade();
							        	if(LRegister_comboSale_grade.size()>0) {
							        		grade = courseService.getGrade(LRegister_comboSale_grade.get(0).getGrade_id(),"","","","","","","200","","","","","").get(0);
							        		teacher_id = grade.getTeacher_id();
							        	}							        	
							        }
									comboSaleString2 += "<A href='javascript:void(0)' style='color:blue;text-decoration:underline;letter-spacing:1px' onclick='openGradeToSelect("
											+ LRegister_comboSale.get(j).getRegister_comboSale_seq() + ","
											+ LRegister_comboSale.get(j).getRegister_id() + ","
											+ LRegister_comboSale.get(j).getComboSale_id() + ","
											+ LRegister_comboSale.get(j).getSubject_id() + ","
											+ gradeNoLeft + ","
											+ teacher_id + ","
											+ LRegister_comboSale.get(j).getClass_style() + ")'>"+waitGrade+"</A>";
						} else { 					
							// 虛擬科目		
							if (!LRegister_comboSale.get(j).getGradeNoLeft().equals("0")) {
									comboSaleString2 += "<span><A href='javascript:void(0)' style='text-decoration:underline;letter-spacing:1px;color:blue' onclick='openSubjectToSelect(\"right\","
												+ LRegister_comboSale.get(j).getRegister_comboSale_seq() + ","
												+ LRegister_comboSale.get(j).getRegister_id() + ","
												+ LRegister_comboSale.get(j).getComboSale_id() + ","
												+ LRegister_comboSale.get(j).getGradeNoLeft() + ","
												+ LRegister_comboSale.get(j).getSubject_id() + ","
												+ LRegister_comboSale.get(j).getCostShare() + ")'>選科目</A></b></span>";
							}    
						}
					}
					comboSaleString2 += 
							       "</div>";

/***************/
// 排定課程列表
/***************/
										//本次報名可進班先上課單科總原價
										if(LRegister.get(i).getOrderStatus().equals("未結清")) {									
											/**
											List<String> salePrice = registerGradeOriginPrice(LRegister.get(i).getRegister_seq());
											for(int x=0;x<salePrice.size();x++) {
												if(salePrice.get(x)!=null && !salePrice.get(x).isEmpty()) {
													salePriceTotal += Integer.valueOf(salePrice.get(x));
												}	
											}
											**/
										}
										
										String order_school="",order_grade="",order_classStyle="",order_teacher="",order_attendStatus="",pay_status="";
										List<Register_comboSale_grade> LRegister_comboSale_grade = courseService.getRegister_comboSale_grade(LRegister_comboSale.get(j).getRegister_comboSale_seq(),"","","1");
										for (int k = 0; k < LRegister_comboSale_grade.size(); k++) {
											String classStyle = "";
											String FirstClass = "";
											String cancelFlag = "";
											
											List<Grade> LGrade = courseService.getGradeList("","",LRegister_comboSale_grade.get(k).getGrade_id(),"","","","","","","1","","","","","","","","1","");
											if (LGrade.size() > 0) {
												Grade grade = LGrade.get(0);
												if (LRegister_comboSale_grade.get(k).getClass_style().equals("1")) {
													classStyle = "正班";
													FirstClass = grade.getClass_xth_date();	
												} else if (LRegister_comboSale_grade.get(k).getClass_style().equals("2")) {
													classStyle = "Video";
													FirstClass = "";
												}
												// 上課狀態
												String attendStatus = "";
												if (LRegister_comboSale_grade.get(k).getRegister_status().equals("2")) {
													attendStatus = "取消";
													cancelFlag = " color:#aaaaaa; ";
												}else {
													attendStatus = 
															"<A href='javascript:void(0)' style='text-decoration:underline;font-size:small;color:blue' onclick=gradeChange("
															+ LRegister.get(i).getRegister_seq() + "," 
															+ grade.getGrade_seq() + "," 
															+ student_seq + ","
															+ LRegister_comboSale_grade.get(k).getRegister_comboSale_grade_seq()
															+ ",\'"+grade.getClass_start_date()+grade.getSubject_name()+ "\',"
															+ ") title='課程異動'>"+getAttendStatus(grade.getGrade_seq(),LRegister_comboSale_grade.get(k).getClass_style())+"</A>";
												}
												
												order_school += "<span style='"+cancelFlag+"'>"+LRegister_comboSale_grade.get(k).getSchool_name()+"</span><br>";
												String video_date = grade.getVideo_date();
												if(video_date!=null && !video_date.isEmpty()) {
													video_date = "/"+video_date;
												}
												order_grade  += "<span style='"+cancelFlag+"'>&bull;"+grade.getClass_start_date()+video_date+" "+grade.getSubject_name()+" "+(grade.getGradeName()==null?"":grade.getGradeName())+"</span><br>";
												order_classStyle += "<span style='"+cancelFlag+"'>"+classStyle+"</span><br>";
												order_teacher += "<span style='"+cancelFlag+"'>"+grade.getTeacher_name()+"</span><br>";
												order_attendStatus += "<span style='"+cancelFlag+"'>"+attendStatus+"</span><br>";
												if(LRegister_comboSale_grade.get(k).getRegister_status().equals("1")) {	//[register_status]#訂班狀態 1:訂班,2:取消,3:保留  
													if(LRegister.get(i).getOrderStatus().equals("未結清")) {
																/**
																	if(LRegister_comboSale_grade.get(k).getAllow_attend().equals("1")) {
																		pay_status += "<span style='font-size:small'><A href='javascript:void(0)' style='text-decoration:underline' onclick='pay_status("+LRegister_comboSale_grade.get(k).getRegister_comboSale_id()+","+LRegister_comboSale_grade.get(k).getRegister_comboSale_grade_seq()+")'>先上</A></span><br>";
																	}else {
																			int subjectSalePrice = subjectSalePrice(LRegister_comboSale.get(j).getSubject_id());
																			if(LRegister.get(i).getOrderStatus().equals("2") || (paidTotal-salePriceTotal)>=subjectSalePrice) { 														
																		        //可上課
																				pay_status += "<span style='font-size:small'><A href='javascript:void(0)' style='font-size:large;text-decoration:underline' onclick='pay_status("+LRegister_comboSale_grade.get(k).getRegister_comboSale_id()+","+LRegister_comboSale_grade.get(k).getRegister_comboSale_grade_seq()+")'>&hellip;</A></span><br>";
																			}else {
																				pay_status +="&#10008;<br>";//X	
																			}
																	}
																**/
														pay_status += "<span style='font-size:small'><A href='javascript:void(0)' style='font-weight:bold;text-decoration:underline' color:blue onclick='allowAttend("+grade.getGrade_seq()+")'>&hellip;</A></span><br>";
													}else if(LRegister.get(i).getOrderStatus().equals("結清")) {
														pay_status += "<span style='font-size:small'><A href='javascript:void(0)' style='text-decoration:underline' color:blue onclick='allowAttend("+grade.getGrade_seq()+")'>&#10004;</A></span><br>"; // V	
													}
												}else {
													    pay_status += "-<br>";
												}
											}											
										}
										
										comboSaleString2 += 												
												"<div class='th3' style='width:200px;font-size:small'>"+order_grade+"</div>"+ //已訂
												"<div class='th3' style='width:70px;text-align:center'>"+pay_status+"</div>"+ //進班
												"<div class='th3' style='width:50px;text-align:center;font-size:small'>"+order_classStyle+"</div>"+ //性質
												"<div class='th3' style='width:70px;text-align:center;font-size:small;padding:5px'>"+order_school+"</div>"+ //上課分校												
												"<div class='th3' style='width:60px;text-align:center;font-size:small'>"+order_teacher+"</div>"+ //老師
												"<div class='th3' style='width:100px;text-align:center;font-size:small;text-align:center'>"+order_attendStatus+"</div>"; //狀態

							   				
				          comboSaleString2 += "</div>";
				}
				comboSaleString += comboSaleString2;
				comboSaleString += "</div></div></div>";				
				comboSaleString += "<div class='tr' style='background-color:#eeeeee;height:1px'><div class='td'></div><div class='td'></div><div class='td'></div><div class='td'></div></div>";
				LRegister.get(i).setComboSaleString(comboSaleString);
			}
		}
		return LRegister;
	}

	public String getAttendStatus(String grade_id, String class_style) {
		String attendStatus = "";
		String Class_end_date = "";
		String Class_no = courseService.getGradeList("","",grade_id,"","","","","","","","","","","","","","","1","").get(0).getClass_no();
		String Class_1th_date = courseService.getGradeList("","",grade_id,"","","","","","","1","","","","","","","","1","").get(0).getClass_xth_date();
		List<Grade> LGrade = courseService.getGradeList("","",grade_id,"","","","","","",Class_no,"","","","","","","","1","");
		if(LGrade.size()>0) {
			Class_end_date = courseService.getGradeList("","",grade_id,"","","","","","",Class_no,"","","","","","","","1","").get(0).getClass_xth_date();
		}	

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date current = new Date();
		Date nowDate;
		try {
		    if(!(Class_1th_date.isEmpty() || Class_end_date.isEmpty())){
				nowDate = sdf.parse(sdf.format(current));
				Date FirstClass = sdf.parse(Class_1th_date);
				Date EndClass = sdf.parse(Class_end_date);
				if (class_style.equals("1") || class_style.equals("2")) { // 面授班+錄影班
					if (nowDate.before(FirstClass)) {
						attendStatus = "即將開課";
					} else if (nowDate.after(EndClass)) {
						attendStatus = "正班結束";
					} else {
						attendStatus = "進行中";
					}
				}
		    }else {
		    	attendStatus = "&hellip;";
		    }
		} catch (ParseException e) {
			e.printStackTrace();
		}

		/*
		 * 進行中 保留 即將開課 已結業 取消
		 */

		return attendStatus;
	}

	public Boolean BookGradeSave(
			String Register_seq,
			String Register_comboSale_seq, 
			String grade_seq, 
			String subject_id, 
			String class_no,
			String student_seq, 
			String updater, 
			String classStyle, 
			String parent_subject_id,
			String school_code,
			String gradeNo2
		){
		if(parent_subject_id != null && !parent_subject_id.isEmpty()) { // 虛擬科目選好了科目
			jdbcTemplate.update("UPDATE eip.Register_comboSale set subject_id=? where Register_comboSale_seq=?",
					subject_id, 
					Register_comboSale_seq
			);
		}
		// -99尚未設定應選期別
		String gradeNoLeft = getComboSaleByRegister(Register_comboSale_seq,"","","","","1",false,false,"").get(0).getGradeNoLeft();
		if(gradeNoLeft.equals("-99")) {
			gradeNoLeft = gradeNo2;
		}
		jdbcTemplate.update("UPDATE eip.Register_comboSale set gradeNoLeft=? where Register_comboSale_seq=?",
				Integer.valueOf(gradeNoLeft) - 1, 
				Register_comboSale_seq
		);
       		
		jdbcTemplate.update("INSERT into eip.Register_comboSale_grade values (default,?,?,?,?,?,?,?,?,NOW())", 
				Register_comboSale_seq,
				grade_seq, 
				1, // #訂班狀態 1:訂班,2:取消,3:保留
				classStyle,
				school_code, //上課分校
				"", //sitNo
				1,
				updater
		);
		int Register_comboSale_grade_seq = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);

/**
		int class_style = -99; // -99預設值(沿用舊課程型態設定值)
		if(classStyle.equals("1")) {class_style = 1;} // 實體
		if(classStyle.equals("2")) {class_style = 2;} // Video
		if(classStyle.equals("3")) {class_style = 3;} // Video
**/		
		String attend = null;
		List<Classes> LClasses = courseService.getClasses(grade_seq,"","","","","","","","","");
		for (int i=0;i<LClasses.size();i++) {
			//若中途插班實體課, 前面沒上課attend=2
			attend = "0";
			if(classStyle.equals("1")) {				
				if(LClasses.size()>0 && LClasses.get(0).getClass_style().equals("1")) {
					SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
					String curDate = sdFormat.format(new Date());	
					String classDate = LClasses.get(0).getClass_date().substring(6, 10)+LClasses.get(0).getClass_date().substring(0,2)+LClasses.get(0).getClass_date().substring(3, 5);
					if(Integer.valueOf(curDate)>Integer.valueOf(classDate)) {
						attend = "2";
					}
				}
			}
			String allowAttend = "0";
			Register register = getRegister("",Register_seq).get(0);
			if(register.getOrderStatus().equals("2")) {allowAttend = "1";} //1未結清,2已結清,-1不適用(正龍已選期別但毋須付款)
			
			jdbcTemplate.update("INSERT INTO eip.signRecordHistory VALUES (default,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?);",
					Register_comboSale_grade_seq, 
					1, // #訂班狀態 1:訂班,2:取消,3:保留
					student_seq, 
					classStyle, 
					school_code, //上課分校
					grade_seq, 
					LClasses.get(i).getClass_th(),     
					attend, //attend  #0預定,1出席,-1缺席,2 中途插班代選
					1, 
					"", 
					-99, 
					updater,
					0,
					"",
					"",
					"",
					"",
					allowAttend,
					i+1
		    );
		}

		admService.SaveRegisterLog(Register_comboSale_seq, String.valueOf(Register_comboSale_grade_seq), "1","","-1",updater,"","","","","","","","","");
		return true;
	}

	public String compare2GradeTime(String Grade_id1, String Grade_id2) {
		List<Classes> LClasses1 = courseService.getClasses(Grade_id1,"","","","","","","","","");
		List<Classes> LClasses2 = courseService.getClasses(Grade_id2,"","","","","","","","","");
		String time1_From = ""; // ex.201906240900
		String time1_To = "";
		long time1_From_i;
		long time1_To_i;
		String time2_From = "";
		String time2_To = "";
		long time2_From_i;
		long time2_To_i;
		String yearTmp1 = "";
		String yearTmp2 = "";
		String returnTmp = "";
		for (int i = 0; i < LClasses1.size(); i++) {
			if (!LClasses1.get(i).getClass_date().equals("")) { // 實體課程
				yearTmp1 = LClasses1.get(i).getClass_date().substring(6, 10)
						+ LClasses1.get(i).getClass_date().substring(0, 2)
						+ LClasses1.get(i).getClass_date().substring(3, 5);
				time1_From = yearTmp1 + LClasses1.get(i).getTime_from();
				time1_To = yearTmp1 + LClasses1.get(i).getTime_to();
				time1_From_i = Long.parseLong(time1_From);
				time1_To_i = Long.parseLong(time1_To);
				for (int j = 0; j < LClasses2.size(); j++) {
					if (!LClasses2.get(j).getClass_date().equals("")) { // 實體課程
						yearTmp2 = LClasses2.get(j).getClass_date().substring(6, 10)
								+ LClasses2.get(j).getClass_date().substring(0, 2)
								+ LClasses2.get(j).getClass_date().substring(3, 5);
						time2_From = yearTmp2 + LClasses2.get(j).getTime_from();
						time2_To = yearTmp2 + LClasses2.get(j).getTime_to();
						time2_From_i = Long.parseLong(time2_From);
						time2_To_i = Long.parseLong(time2_To);

						if ((time2_From_i > time1_From_i && time2_From_i < time1_To_i)
								|| (time2_To_i > time1_From_i && time2_To_i < time1_To_i)
								|| (time2_From_i == time1_From_i && time2_To_i == time1_To_i)) {
							returnTmp += "[" + yearTmp1 + ":" + LClasses1.get(i).getTime_from() + "~"
									+ LClasses1.get(i).getTime_to() + "] ";
						}
					}
				}
			}
		}
		return returnTmp;
	}

	public Boolean ClassBookSave(String Register_comboSale_grade_id,String student_id,String class_style,String school_code,String grade_id,String class_th,String slot,String updater,String attend_date,String signRecordHistory_seq,String makeUpNo,String class_th_ex,String remark,String comment) {
        
		//修改舊的eip.signRecordHistory
		jdbcTemplate.update("update eip.signRecordHistory set active = 0 where signRecordHistory_seq=?",signRecordHistory_seq);
		if(class_style.equals("1")) { //補實體課程
			//attend_date = "";
			slot = "-99";
			makeUpNo = "0";
		}
		//線上課程
/**		
		if(class_style.equals("3")) { 
			slot = "-99";
		}
**/				
		SignRecordHistory signRecordHistory= admService.getSignRecordHistory(signRecordHistory_seq,"","","","","","","","","","").get(0);
		jdbcTemplate.update("INSERT INTO eip.signRecordHistory VALUES (default,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?);",
				Register_comboSale_grade_id,
				1, 
				student_id, 
				class_style, 
				school_code, 
				grade_id, 
				class_th, 
				0, 
				1,
				attend_date, 
				slot, 
				updater,
				makeUpNo, //目前補課數
				class_th_ex,
				"",
				"",
				comment,
				signRecordHistory.getAllowAttend(),
				signRecordHistory.getClass_th_seq()
		);
		
		
		Grade grade = courseService.getGrade(grade_id,"","","","","","","200","","","","","").get(0);
		
		//修改eip.student.[makeUpTotal]	
		String toClassStr = grade.getClass_start_date()+grade.getSubject_name()+" 第"+class_th+"堂";
		Student student = getStudent(student_id,"","","","","","","","","").get(0);
		String reduceMakeUpNo = "0"; //增加或減少video點數數量
		int makeUpTotal = 0;
		
		//取得免費補課堂數: ex.GRE/GMAT可免費補2堂
		int free_makeUpNo = 1;
		Grade grade2 = courseService.getGrade2(grade_id,"","","","","","","","").get(0);
		List<Subject> LSubject = courseService.getSubject("",grade2.getSubject_id(),"","","","0");
		if(LSubject.size()>0) {
			free_makeUpNo = Integer.valueOf(LSubject.get(0).getFree_makeUpNo());
		}
		
		//判斷此課程是否第一堂為正班且出席
		Boolean firstRealAttend = firstRealAttend(student_id,Register_comboSale_grade_id,class_th);
		if(firstRealAttend) {
			makeUpNo = String.valueOf(Integer.valueOf(makeUpNo)+1);
		}
		if(Integer.valueOf(makeUpNo)>free_makeUpNo) {
			reduceMakeUpNo = "1";
			makeUpTotal = Integer.valueOf(student.getMakeUpTotal())-1;
		}
		
		Register register = getRegister3(Register_comboSale_grade_id).get(0);	
		updateMakeUpTotal(
			student.getStudent_no(),
			makeUpTotal,
			reduceMakeUpNo, //amount
			student_id,
			updater,
			"-1", //-1:使用扣除
			remark, //remark
			toClassStr, //content
			register.getRegister_seq() //register_id
		);   		
			
		return true;
	}

	public String StudentSave(String degree,Student student,String employeeName,String creater,String commentThis) {
		// 新增學生
		String student_no = "";
		if (student.getStudent_seq() == null || student.getStudent_seq().isEmpty()) {
			if (student.getStudent_no() == null || student.getStudent_no().isEmpty()) {
				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
				Date current = new Date();
				String curDate = sdFormat.format(current).substring(2);
				int maxNo = jdbcTemplate.queryForObject(
						"select count(*) from eip.student where student_no like '" + curDate + "%'", Integer.class);
				String maxNoStr = String.valueOf(maxNo + 1);
				if (maxNo < 9) {
					maxNoStr = "0" + maxNoStr;
				}

				Random random = new Random();
				int ran = random.ints(11, (98 + 1)).findFirst().getAsInt();
				student_no = curDate + maxNoStr + String.valueOf(ran);
			} else { // 政龍
				student_no = student.getStudent_no();
			}

//Generate QRcode		 			
			GenQRcode(student_no);
			 

//Insert to eip.student	
			//時間不從政龍而來, 自己產生
			if(student.getCreate_time()==null || student.getCreate_time().isEmpty()) {
				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                student.setCreate_time(sdFormat.format(new Date()));				
			}
			jdbcTemplate.update(
					"INSERT INTO eip.student VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
					student_no, 
					student.getCh_name(), 
					student.getEn_name(), 
					student.getSex(), 
					student.getCategory(),
					student.getDerived(), 
					student.getIdn(), 
					student.getBirthday(), 
					student.getIdentity(),
					student.getTel(), 
					student.getMobile_1(), 
					student.getMobile_2(), 
					student.getEmail_1(),
					student.getEmail_2(), 
					student.getAddress_1(), 
					student.getAddress_2(), 
					student.getFb(),
					student.getLine(), 
					student.getParent_1_name(), 
					student.getParent_1_mobile(),
					student.getParent_1_email(), 
					student.getParent_1_line(), 
					student.getParent_2_name(),
					student.getParent_2_mobile(), 
					student.getParent_2_email(), 
					student.getParent_2_line(), 
					employeeName, //creater
					student.getCreate_time(),
					"", //editor
					student.getRemark(), 
					student.getRemark2(), 
					student.getSpecial_idn(),
					student.getExam(), 
					student.getAbroad_date(), 
					"0", //balanceTotal
					"0", //makeUpTotal
					student.getPhoto(),
					student.getSchool_code(),
					student.getSchool_code2(),
		    		student.getUpdater(),
		    		student.getUpdate_time(),  		 		
		    		student.getCol_5(),
		    		"",
		    		commentThis, //Handover
		    		student.getTel2(),
		    		student.getCompany_tel(),
		    		student.getAgent_studentNo(),
		    		student.getGrade_highSchool(),
		    		student.getPostCode(),
		    		student.getAgent_studentName(),
		    		"888888", //password
		    		student.getCol_6(),
		    		student.getCol_7(),
		    		student.getCol_8(),
		    		student.getCol_9(),
		    		degree,
		    		student.getCowork()
			);
			// 更新學生
		} else {
			student_no = student.getStudent_no();
			String remarkTotal = null;
			String remarkTotalOri = getStudent(student.getStudent_seq(),"","","","","","","","","").get(0).getRemarkTotal();
			if(remarkTotalOri==null) {remarkTotalOri="";}
			
			String handover = getStudent(student.getStudent_seq(),"","","","","","","","","").get(0).getHandover();
			if(handover==null) {handover="";}

			//更新前先塞一筆至歷史紀錄 
	    	Student student_or = getStudent(student.getStudent_seq(),"","","","","","","","","").get(0);

			jdbcTemplate.update(
					"INSERT INTO eip.studentRecord VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
					student_or.getStudent_seq(),
					student_or.getStudent_no(), 
					student_or.getCh_name(), 
					student_or.getEn_name(), 
					student_or.getSex(), 
					student_or.getCategory(),
					student_or.getDerived(), 
					student_or.getIdn(), 
					student_or.getBirthday(), 
					student_or.getIdentity(),
					student_or.getTel(), 
					student_or.getMobile_1(), 
					student_or.getMobile_2(), 
					student_or.getEmail_1(),
					student_or.getEmail_2(), 
					student_or.getAddress_1(), 
					student_or.getAddress_2(), 
					student_or.getFb(),
					student_or.getLine(), 
					student_or.getParent_1_name(), 
					student_or.getParent_1_mobile(),
					student_or.getParent_1_email(), 
					student_or.getParent_1_line(), 
					student_or.getParent_2_name(),
					student_or.getParent_2_mobile(), 
					student_or.getParent_2_email(), 
					student_or.getParent_2_line(), 
					student_or.getCreater(), //creater
					student_or.getCreate_time(),
					student_or.getEditor(), //editor
					student_or.getRemark(), 
					student_or.getRemark2(), 
					student_or.getSpecial_idn(),
					student_or.getExam(), 
					student_or.getAbroad_date(), 
					student_or.getBalanceTotal(), //balanceTotal
					student_or.getMakeUpTotal(), //makeUpTotal
					"",//photo
					student_or.getSchool_code(),
					student_or.getSchool_code2(),
					student_or.getUpdater(),
					student_or.getUpdate_time(),  		 		
					student_or.getCol_5(),
					student_or.getRemarkTotal(),
					student_or.getHandover(), //Handover
					student_or.getTel2(),
					student_or.getCompany_tel(),
					student_or.getAgent_studentNo(),
					student_or.getGrade_highSchool(),
					student_or.getPostCode(),
					student_or.getAgent_studentName(),
					student_or.getPasswd(),
					student_or.getCol_6(),
					student_or.getCol_7(),
					student_or.getCol_8(),
					student_or.getCol_9(),
					student_or.getDegree(),
					student_or.getCowork()
			);			
			
			jdbcTemplate.update(
					"UPDATE eip.student set cowork=?,degree=?,student_no=?,ch_name=?,en_name=?,sex=?,category=?,derived=?,idn=?,birthday=?,identity=?,tel=?,tel2=?,mobile_1=?,mobile_2=?,email_1=?,email_2=?,address_1=?,address_2=?,fb=?,line=?,parent_1_name=?,parent_1_mobile=?,parent_1_email=?,parent_1_line=?,parent_2_name=?,parent_2_mobile=?,parent_2_email=?,parent_2_line=?,editor=?,remark=?,remark2=?,special_idn=?,col_3=?,col_4=?,balanceTotal=?,makeUpTotal=?,school_code=?,school_code2=?,updater=?,update_time=NOW(),col_5=?,col_6=?,col_7=?,col_8=?,col_9=?,agent_studentName=?,agent_studentNo=?,handover=? where student_seq=?",
					student.getCowork(),
					degree,
					student_no, 
					student.getCh_name(), 
					student.getEn_name(), 
					student.getSex(),
					student.getCategory(), 
					student.getDerived(), 
					student.getIdn(), 
					student.getBirthday(),
					student.getIdentity(), 
					student.getTel(), 
					student.getTel2(),
					student.getMobile_1(), 
					student.getMobile_2(),
					student.getEmail_1(), 
					student.getEmail_2(), 
					student.getAddress_1(), 
					student.getAddress_2(),
					student.getFb(), 
					student.getLine(), 
					student.getParent_1_name(), 
					student.getParent_1_mobile(),
					student.getParent_1_email(), 
					student.getParent_1_line(), 
					student.getParent_2_name(),
					student.getParent_2_mobile(), 
					student.getParent_2_email(), 
					student.getParent_2_line(), 
					"",
					student.getRemark(), 
					student.getRemark2(), 
					student.getSpecial_idn(), 
					student.getExam(),
					student.getAbroad_date(), 
					student.getBalanceTotal(), 
					student.getMakeUpTotal(),					
					student.getSchool_code(),
					student.getSchool_code2(),
					employeeName,		 		
		    		student.getCol_5(),
		    		student.getCol_6(),
		    		student.getCol_7(),
		    		student.getCol_8(),
		    		student.getCol_9(),
		    		student.getAgent_studentName(),
		    		student.getAgent_studentNo(),
		    		handover+commentThis,//Handover
					student.getStudent_seq()					
			);
		}
		return student_no;
	}
	
	//Generate QRcode
	@Value("${UploadPath}") String UploadPath;
	public Boolean GenQRcode(String student_no) {
		  String myCodeText = student_no; 
		  String filePath = UploadPath+"QRcode/"+student_no+".png"; 
		  int size = 250;
		  String fileType = "png"; 
		  File myFile = new File(filePath); 
		  try {		  
			  Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType,Object>(EncodeHintType.class); 
			  hintMap.put(EncodeHintType.CHARACTER_SET,"UTF-8");
			  
			  hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			  
			  QRCodeWriter qrCodeWriter = new QRCodeWriter(); 
			  BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
			  int CrunchifyWidth = byteMatrix.getWidth(); BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
			  image.createGraphics();
			  
			  Graphics2D graphics = (Graphics2D) image.getGraphics();
			  graphics.setColor(Color.WHITE); graphics.fillRect(0, 0, CrunchifyWidth,CrunchifyWidth); graphics.setColor(Color.BLACK);
			  
			  for (int i = 0; i < CrunchifyWidth; i++) { 
				  for (int j = 0; j <CrunchifyWidth; j++) { 
					  if (byteMatrix.get(i, j)) { 
						  graphics.fillRect(i, j, 1,1); 
					  } 
				  } 
			  } 
			  ImageIO.write(image, fileType, myFile); 
		  } catch (Exception e) {
			  e.printStackTrace(); 
		  }	
		  return true;
	}
	
	public Boolean StudentRegisterSave(String student_no) {

		String sql = "SELECT a.班別代碼 as gradeId,b.班別 as gradeName,c.employee_no as saleId,c.employee_name as salePerson,a.報名日期 as registerDate,a.座號 as sitNo"+
					" FROM eip.JLM_StudentGradeInfo a left join eip.JLM_employee c on c.employee_no=a.業務 , eip.JLM_StudentGrade b"+
					" where a.班別代碼=b.代碼 and a.學號='" + student_no + "'";

		List<JLM_gradeRegister> LJLM_gradeRegister = jdbcTemplate.query(sql,
				(result, rowNum) -> new JLM_gradeRegister(
						"", 
						"", 
						result.getString("gradeId").trim(),
						result.getString("gradeName"), 
						result.getString("saleId"), 
						result.getString("salePerson"),
						result.getString("registerDate"), 
						result.getString("sitNo"),
						""
		));

		for (int i = 0; i < LJLM_gradeRegister.size(); i++) {
			jdbcTemplate.update("INSERT INTO eip.JLM_gradeRegister VALUES (default,?,?,?,?,?,?,?);", 
					student_no,
					LJLM_gradeRegister.get(i).getGradeId(), 
					LJLM_gradeRegister.get(i).getGradeName(),
					LJLM_gradeRegister.get(i).getSaleId(), 
					LJLM_gradeRegister.get(i).getSalePerson(),
					LJLM_gradeRegister.get(i).getRegisterDate(), 
					LJLM_gradeRegister.get(i).getSitNo()
			);
		}
		return true;
	}

	public Boolean StudentPaySave(String student_no) {

		String sql = "SELECT a.班別代碼 as gradeId,b.班別 as gradeName,employee_no as saleId,c.employee_name as salePerson,e.應繳學費 as originPrice,d.優待 as discountPrice,e.修業期限起 as gradeFrom,e.修業期限訖 as gradeTo,a.座號 as sitNo"
				+ " FROM eip.JLM_StudentGradeInfo a left join eip.JLM_employee c on c.employee_no=a.業務,eip.JLM_StudentGrade b,eip.JLM_StudentDiscount d,eip.JLM_StudentClass e"
				+ " where a.班別代碼=b.代碼 and a.學號=d.學號 and a.班別代碼=d.班別代碼 and b.班級代碼=e.班級代碼 and a.學號='"
				+ student_no + "'";

		List<JLM_studentPay> LJLM_studentPay = jdbcTemplate.query(sql,(result, rowNum) -> new JLM_studentPay(
						"", 
						"", 
						result.getString("gradeId").trim(),
						result.getString("gradeName"), 
						result.getString("saleId"), 
						result.getString("salePerson"),
						result.getString("originPrice"), 
						result.getString("discountPrice"), 
						"", 
						"",
						result.getString("gradeFrom"), 
						result.getString("gradeTo"), 
						"",
						result.getString("sitNo")
		));

		for (int i = 0; i < LJLM_studentPay.size(); i++) {
			jdbcTemplate.update("INSERT INTO eip.JLM_studentPay VALUES (default,?,?,?,?,?,?,?,?,?,?,?);", 
					student_no,
					LJLM_studentPay.get(i).getGradeId(), 
					LJLM_studentPay.get(i).getGradeName(),
					LJLM_studentPay.get(i).getSaleId(), 
					LJLM_studentPay.get(i).getSalePerson(),
					LJLM_studentPay.get(i).getOriginPrice(), 
					LJLM_studentPay.get(i).getDiscountPrice(), 
					"",
					LJLM_studentPay.get(i).getGradeFrom(), 
					LJLM_studentPay.get(i).getGradeTo(),
					LJLM_studentPay.get(i).getSitNo()
			);
		}
		return true;
	}

	public Boolean StudentPayRecordSave(String student_no) {
		String sql = "select a.班別代碼 as gradeId,a.繳費金額 as payMoney,a.承辦人 as takeId,c.employee_name as takePerson,a.收據編號 as receiptNo,a.繳費日期 as payDate,a.繳費時間 as payTime,a.繳費方式 as payStyle"
				+ " FROM eip.JLM_StudentFee a left join eip.JLM_employee c on c.employee_no=a.承辦人 where  a.學號='" + student_no
				+ "'";

		List<JLM_studentPayRecord> LJLM_studentPayRecord = jdbcTemplate.query(sql,
				(result, rowNum) -> new JLM_studentPayRecord("", "", result.getString("gradeId").trim(),
						result.getString("payMoney"), result.getString("takeId"), result.getString("takePerson"),
						result.getString("receiptNo"), result.getString("payDate"), result.getString("payTime"),
						result.getString("payStyle")));

		for (int i = 0; i < LJLM_studentPayRecord.size(); i++) {
			jdbcTemplate.update("INSERT INTO eip.JLM_studentPayRecord VALUES (default,?,?,?,?,?,?,?,?,?);", student_no,
					LJLM_studentPayRecord.get(i).getGradeId(), LJLM_studentPayRecord.get(i).getPayMoney(),
					LJLM_studentPayRecord.get(i).getTakeId(), LJLM_studentPayRecord.get(i).getTakePerson(),
					LJLM_studentPayRecord.get(i).getReceiptNo(), LJLM_studentPayRecord.get(i).getPayDate(),
					LJLM_studentPayRecord.get(i).getPayTime(), LJLM_studentPayRecord.get(i).getPayStyle());
		}
		return true;
	}

	public List<JLM_studentPayRecord> getJLM_studentPayRecord(String student_no,String gradeId,String limit) {

		String sql = "SELECT * from eip.JLM_studentPayRecord where 1=1";
		if (student_no != null && !student_no.isEmpty()) {
			sql += " and student_no = '" + student_no+"'";
		}
		if (gradeId != null && !gradeId.isEmpty()) {
			sql += " and gradeId = '" + gradeId+"'";
		}		
		sql+=" order by payDate desc"; 
		
		if (limit != null && !limit.isEmpty()) {
			sql += " LIMIT "+ limit;
		}	

		List<JLM_studentPayRecord> LJLM_studentPayRecord = jdbcTemplate.query(sql,
				(result, rowNum) -> new JLM_studentPayRecord(
						result.getString("studentPayRecord_seq"),
						result.getString("student_no"), 
						result.getString("gradeId"), 
						result.getString("payMoney"),
						result.getString("takeId"), 
						result.getString("takePerson"), 
						result.getString("receiptNo"),
						result.getString("payDate"), 
						result.getString("payTime"), 
						result.getString("payStyle")
				));
		return LJLM_studentPayRecord;
	}

	public List<RegisterPromo> getRegisterPromo(String register_id) {

		String sql = "SELECT a.*,b.promoName as classPromotion_name from eip.registerPromo a, eip.classPromotion b"
				+ " where a.classPromotion_id=b.classPromotion_seq";
		if (register_id != null && !register_id.isEmpty()) {
			sql += " and a.register_id = " + register_id;
		}

		List<RegisterPromo> LRegisterPromo = jdbcTemplate.query(sql,
				(result, rowNum) -> new RegisterPromo(result.getString("registerPromo_seq"),
						result.getString("register_id"), result.getString("classPromotion_id"),
						result.getString("classPromotion_name")));
		return LRegisterPromo;
	}
	
	
	

	public String StudentPaySaveRecord(
			StudentPayRecord studentPayRecord, 
			String payStyle_1_money, 
			String payStyle_2_money,
			String[] payStyle_3_date, 
			String[] payStyle_3_money,
			String payStyle_4_code,
			String payStyle_4_money,
			String payStyle_5_periods,
			String payStyle_5_code,
			String payStyle_5_money, 
			String payStyle_6_code, 
			String payStyle_6_money, 
			String payStyle_7_code,
			String payStyle_7_money,
			String student_seq,
			String creater,
			String new_paid,
			String payWay,
			String rebate,
			String cashRefund1,
			String cashRefund2,
			String InstalNoSel
		){

//studentPayRecord 儲存		
		jdbcTemplate.update("INSERT INTO eip.studentPayRecord VALUES (default,?,?,?,?,?,?,?,?);",
				studentPayRecord.getRegister_id(), 
				"0", // payMoney
				studentPayRecord.getActualPrice(), // 應繳
				studentPayRecord.getTakePerson(),
				0, // receiptNo
				studentPayRecord.getPayDate(), 
				studentPayRecord.getSchool_code(),
				payWay
		);

		int currentSeq = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
		int maxReceiptNo = jdbcTemplate.queryForObject("select max(receiptNo) from eip.studentPayRecord where school_code='"
						+ studentPayRecord.getSchool_code() + "'", Integer.class);
		if (maxReceiptNo == 0) {
			maxReceiptNo = 10000000;
		}

//studentPayRecordDetail 儲存
		int payMoney = 0;
		// payStyle_1
		if (payStyle_1_money != null && !payStyle_1_money.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.studentPayRecordDetail VALUES (default,?,?,?,?,?,?);",currentSeq,"1","",payStyle_1_money,"","");
			payMoney += Integer.valueOf(payStyle_1_money);
			//學生餘額
			Student student = getStudent(student_seq,"","","","","","","","","").get(0);
			BalanceSave(student_seq,"-2",payStyle_1_money,"",creater,student.getBalanceTotal());
		}
		// payStyle_2
		if (payStyle_2_money != null && !payStyle_2_money.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.studentPayRecordDetail VALUES (default,?,?,?,?,?,?);", currentSeq,"2","",payStyle_2_money,"","");
			payMoney += Integer.valueOf(payStyle_2_money);						
		}		
		// payStyle_3 現金分期
		if (payStyle_3_money != null && payStyle_3_date != null) {
			for (int i = 0; i < payStyle_3_date.length; i++) {				
				if (payStyle_3_date[i]!=null && !payStyle_3_date[i].isEmpty() && payStyle_3_money[i] != null && !payStyle_3_money[i].isEmpty()) {
					jdbcTemplate.update("INSERT INTO eip.studentPayRecordDetail VALUES (default,?,?,?,?,?,?);",currentSeq,"3","",payStyle_3_money[i],payStyle_3_date[i],InstalNoSel);
					payMoney += Integer.valueOf(payStyle_3_money[i]);
				}
			}
		}
		// payStyle_4
		if (payStyle_4_money != null && !payStyle_4_money.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.studentPayRecordDetail VALUES (default,?,?,?,?,?,?);",currentSeq,"4",payStyle_4_code, payStyle_4_money,"","");
			payMoney += Integer.valueOf(payStyle_4_money); //禮券
		}
		// payStyle_5
		if (payStyle_5_money != null && !payStyle_5_money.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.studentPayRecordDetail VALUES (default,?,?,?,?,?,?);",currentSeq,"5",payStyle_5_code, payStyle_5_money,"",payStyle_5_periods);
			payMoney += Integer.valueOf(payStyle_5_money);
		}
		// payStyle_6
		if (payStyle_6_money != null && !payStyle_6_money.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.studentPayRecordDetail VALUES (default,?,?,?,?,?,?);",currentSeq,"6",payStyle_6_code, payStyle_6_money,"","");
			payMoney += Integer.valueOf(payStyle_6_money);
		}
		// payStyle_7
		if (payStyle_7_money != null && !payStyle_7_money.isEmpty()) {
			jdbcTemplate.update("INSERT INTO eip.studentPayRecordDetail VALUES (default,?,?,?,?,?,?);",currentSeq,"7",payStyle_7_code, payStyle_7_money,"","");
			payMoney += Integer.valueOf(payStyle_7_money);
		}
		String payMoney_or = String.valueOf(payMoney);
		//訂單改報轉入
		if (new_paid != null && !new_paid.isEmpty()) {
			

			if(payWay.equals("1")) { //繳費
	
				if(rebate.equals("1")) { //若折讓則以原訂單淨移轉為所付金額
					payMoney = Integer.valueOf(new_paid);
				}else {
					payMoney += Integer.valueOf(new_paid);
				}
				jdbcTemplate.update("INSERT INTO eip.studentPayRecordDetail VALUES (default,?,?,?,?,?,?);", currentSeq, "8","", payMoney,"","");
			}else if(payWay.equals("-1")){	//退費
				if(rebate.equals("1")) { //若折讓則以原訂單淨移-應退 = 所付金額
					payMoney = Integer.valueOf(new_paid)-Integer.valueOf(cashRefund2);
					jdbcTemplate.update("INSERT INTO eip.studentPayRecordDetail VALUES (default,?,?,?,?,?,?);", currentSeq, "8","", payMoney,"","");
				}else {				
					payMoney = Integer.valueOf(new_paid)-payMoney; //(移轉減去退費)
					jdbcTemplate.update("INSERT INTO eip.studentPayRecordDetail VALUES (default,?,?,?,?,?,?);", currentSeq, "8","", payMoney,"","");
				}	
			}
		}	
		
		// 儲存本次所繳費用加總
		// 修改 receiptNo
		if (payMoney > 0) { 
			if(rebate!=null && rebate.equals("1")) {
				//若折讓(rebate=1實際計算)則繳費方式無須紀錄
				jdbcTemplate.update("update eip.studentPayRecord set payMoney=? where studentPayRecord_seq=?", payMoney,currentSeq);
			}else {
				jdbcTemplate.update("update eip.studentPayRecord set payMoney=? where studentPayRecord_seq=?", payMoney,currentSeq);
				jdbcTemplate.update("update eip.studentPayRecord set receiptNo=? where studentPayRecord_seq=?",maxReceiptNo + 1, currentSeq);
			}	
		}

//Register 儲存
		int paid = 0;
		List<StudentPayRecord> LStudentPayRecord = getStudentPayRecord(studentPayRecord.getRegister_id());
		for (int j = 0; j < LStudentPayRecord.size(); j++) {
			if (LStudentPayRecord.get(j).getPayMoney() == null || LStudentPayRecord.get(j).getPayMoney().isEmpty()) {
				LStudentPayRecord.get(j).setPayMoney("0");
			}
			paid += Integer.valueOf(LStudentPayRecord.get(j).getPayMoney());
		}
		if(rebate!=null && rebate.equals("1") && !cashRefund1.equals("")) { //(rebate=1折讓,cashRefund1=應繳金額),才需計算加上應繳金額
			paid += Integer.valueOf(cashRefund1);
		}
		
		jdbcTemplate.update("update eip.Register set paid=? where Register_seq=?", 
				paid,
				studentPayRecord.getRegister_id()
		);
//結清	  
		Register register = getRegisterList("","",studentPayRecord.getRegister_id(),"").get(0);
		int actualPrice = Integer.valueOf(register.getActualPrice());
		int paidMoney = Integer.valueOf(register.getPaid());
		if (paidMoney >= actualPrice) {
			jdbcTemplate.update("update eip.Register set orderStatus=2 where Register_seq=?",
					studentPayRecord.getRegister_id()
			);
			
			//結清時,eip.signRecordHistory更改為可進班
			List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory3(studentPayRecord.getRegister_id());
			for(int i=0;i<LSignRecordHistory.size();i++) {
				jdbcTemplate.update("update eip.signRecordHistory set allowAttend=1 where signRecordHistory_seq=?",
						LSignRecordHistory.get(i).getSignRecordHistory_seq()
				);				
			}
			//結清時,清除輪值項目
			jdbcTemplate.update("update eip.consultRecord set avaiable=0 where student_id=? and consultCategory_id!=?",
					student_seq,
					"4"
			);			
			//帳戶餘額
			if (paidMoney>actualPrice) {
				int amount = paidMoney-actualPrice;
				String OverPaytype = "1";//1,溢繳(增加)
				String balanceTotal = getStudent(student_seq,"","","","","","","","","").get(0).getBalanceTotal();
				BalanceSave(student_seq,OverPaytype,String.valueOf(amount),"",creater,balanceTotal);
			}	
		}

		return payMoney_or;
	}

	public List<StudentPayRecordDetail> getStudentPayRecordDetail(String studentPayRecord_id,String register_id) {

		String sql = "SELECT a.*,b.name as payStyleCodeName,c.register_id from eip.StudentPayRecordDetail a,eip.payStyle b,eip.studentPayRecord c "
				+ " where a.payStyleID=b.id and a.studentPayRecord_id = c.studentPayRecord_seq";
		
		if (studentPayRecord_id != null && !studentPayRecord_id.isEmpty()) {
			sql += " and studentPayRecord_id = " + studentPayRecord_id;
		}	
		if (register_id != null && !register_id.isEmpty()) {
			sql += " and c.register_id = '" +register_id+"'";
		}		

		List<StudentPayRecordDetail> LStudentPayRecordDetail = jdbcTemplate.query(sql,
				(result, rowNum) -> new StudentPayRecordDetail(
						result.getString("studentPayRecordDetail_seq"),
						result.getString("studentPayRecord_id"), 
						result.getString("payStyleID"),
						result.getString("payStyleCode"), 
						result.getString("payStyleCodeName"),
						result.getString("payStyleMoney"), 
						result.getString("payStyleDate"),
						result.getString("periods"),
						result.getString("register_id")
				));
		return LStudentPayRecordDetail;
	}

	public List<StudentPayRecord> getStudentPayRecord(String register_id) {
//繳費主檔eip.studentPayRecord
		String sql = "SELECT a.*,b.name as school_name,c.ch_name as studentName,d.actualPrice"
				+ " from eip.StudentPayRecord a left join eip.school b on a.school_code=b.code,eip.student c,eip.Register d"
				+ " where a.register_id=d.Register_seq and d.student_seq=c.student_seq";
		if (register_id != null && !register_id.isEmpty()) {
			sql += " and register_id = " + register_id;
		}
		List<StudentPayRecord> LStudentPayRecord = jdbcTemplate.query(sql,
				(result, rowNum) -> new StudentPayRecord(
						result.getString("studentPayRecord_seq"), 
						"",
						result.getString("register_id"), 
						result.getString("payMoney"), 
						result.getString("actualPrice"), // 應繳
						result.getString("takePerson"),
						result.getString("receiptNo").equals("0") ? "N/A" : result.getString("receiptNo"),
						result.getString("payDate"), "", 
						result.getString("school_code"),
						result.getString("school_name"), 
						result.getString("studentName"),
						result.getString("payWay")
				));

		for (int i = 0; i < LStudentPayRecord.size(); i++) {
			String PayStyleNameStr = "";
//繳費細項eip.studentPayRecordDetail			
			List<StudentPayRecordDetail> LStudentPayRecordDetail = getStudentPayRecordDetail(LStudentPayRecord.get(i).getStudentPayRecord_seq(),"");
			for (int j = 0; j < LStudentPayRecordDetail.size(); j++) {
				if (LStudentPayRecordDetail.get(j).getPayStyleMoney() != null && !LStudentPayRecordDetail.get(j).getPayStyleMoney().isEmpty()) {
					PayStyleNameStr += "["+LStudentPayRecordDetail.get(j).getPayStyleCodeName()+LStudentPayRecordDetail.get(j).getPayStyleCode()+"]" +" - "+LStudentPayRecordDetail.get(j).getPayStyleMoney()+"元"+" - "+LStudentPayRecordDetail.get(j).getPeriods()+"期<br>";
				}
			}
			LStudentPayRecord.get(i).setPayStyle(PayStyleNameStr);
		}
		return LStudentPayRecord;
	}

	public List<ConsultCategory> getConsultCategory() {

		String sql = "SELECT * from eip.consultCategory " + " where 1=1";

		List<ConsultCategory> LConsultCategory = jdbcTemplate.query(sql,(result, rowNum) -> new ConsultCategory(
						result.getString("consultCategory_seq"), 
						result.getString("id"),
						result.getString("name")
						));
		return LConsultCategory;
	}

//約訪紀錄_諮詢課程_選取	
	public List<ClassCategorySel> getClassCategorySel(String consultRecord_id) {
		String sql = "SELECT * from eip.classCategorySel " + " where consultRecord_id='" + consultRecord_id + "'";

		List<ClassCategorySel> LClassCategorySel = jdbcTemplate.query(sql,
				(result, rowNum) -> new ClassCategorySel(result.getString("classCategorySel_seq"),
						result.getString("consultRecord_id"), result.getString("category_id")));
		return LClassCategorySel;
	}

	public List<ConsultReason> getConsultReason() {

		String sql = "SELECT * from eip.consultReason " + " where 1=1";

		List<ConsultReason> LConsultReason = jdbcTemplate.query(sql,
				(result, rowNum) -> new ConsultReason(result.getString("consultReason_seq"), result.getString("id"),
						result.getString("name")));
		return LConsultReason;
	}

	// 約訪紀錄_諮詢原因_選取
	public List<ConsultReasonSel> getConsultReasonSel(String consultRecord_id) {
		String sql = "SELECT * from eip.consultReasonSel " + " where consultRecord_id='" + consultRecord_id + "'";

		List<ConsultReasonSel> LConsultReasonSel = jdbcTemplate.query(sql,
				(result, rowNum) -> new ConsultReasonSel(result.getString("consultReasonSel_seq"),
						result.getString("consultRecord_id"), result.getString("consultReason_id")));
		return LConsultReasonSel;
	}
	// 約訪紀錄_從何得知
	public List<InfoFrom> getInfoFrom() {

		String sql = "SELECT * from eip.infoFrom " + " where 1=1";

		List<InfoFrom> LInfoFrom = jdbcTemplate.query(sql,(result, rowNum) -> new InfoFrom(
						result.getString("infoFrom_seq"), 
						result.getString("id"),
						result.getString("name")
						));
		return LInfoFrom;
	}

	// 約訪紀錄_從何得知_選取
	public List<InfoFromSel> getInfoFromSel(String consultRecord_id) {
		String sql = "SELECT * from eip.infoFromSel " + " where consultRecord_id='" + consultRecord_id + "'";

		List<InfoFromSel> LInfoFromSel = jdbcTemplate.query(sql,
				(result, rowNum) -> new InfoFromSel(result.getString("infoFromSel_seq"),
						result.getString("consultRecord_id"), result.getString("infoFrom_id")));
		return LInfoFromSel;
	}
	
	// 約訪紀錄_名單來源
	public List<NameFrom> getNameFrom() {

		String sql = "SELECT * from eip.nameFrom " + " where 1=1";

		List<NameFrom> LNameFrom = jdbcTemplate.query(sql,(result, rowNum) -> new NameFrom(
						result.getString("nameFrom_seq"), 
						result.getString("id"),
						result.getString("name")
						));
		return LNameFrom;
	}

	// 約訪紀錄_從何得知_選取
	public List<NameFromSel> getNameFromSel(String consultRecord_id) {
		String sql = "SELECT * from eip.nameFromSel " + " where consultRecord_id='" + consultRecord_id + "'";

		List<NameFromSel> LNameFromSel = jdbcTemplate.query(sql,(result, rowNum) -> new NameFromSel(
						result.getString("nameFromSel_seq"),
						result.getString("consultRecord_id"), 
						result.getString("nameFrom_id")
						));
		return LNameFromSel;
	}	

	public List<ConsultRecord> getConsultRecord(String consultRecord_seq, String student_id,String avaiable) {

		String sql = "SELECT a.*,b.name as consultCategory_name,c.ch_name as employee_name,d.name as employee_school"+
		             " from eip.consultRecord a,eip.consultCategory b,eip.employee c,eip.school d"+
				     " where   a.consultCategory_id=b.consultCategory_seq and a.employee_id=c.employee_seq and c.school = d.code";
		if (consultRecord_seq != null && !consultRecord_seq.isEmpty()) {
			sql += " and consultRecord_seq = " + consultRecord_seq;
		}
		if (student_id != null && !student_id.isEmpty()) {
			sql += " and  student_id = " + student_id;
		}
		if (avaiable != null && !avaiable.isEmpty()) {
			sql += " and  avaiable = " + avaiable;
		}		
		
		sql += " order by a.createDate desc";

		List<ConsultRecord> LConsultRecord = jdbcTemplate.query(sql,
				(result, rowNum) -> new ConsultRecord(result.getString("student_id"),
						result.getString("consultRecord_seq"), 
						result.getString("consultCategory_id"),
						result.getString("consultCategory_name"), 
						result.getString("oneDayValid"),
						result.getString("content"), 
						result.getString("validDate"), 
						result.getString("remark"),
						result.getString("employee_id"), 
						result.getString("employee_name"),
						result.getString("employee_school"), 
						result.getString("createDate"),
						"",
						result.getString("crossDate"),
						result.getString("infoFrom_text_1"),
						result.getString("nameFrom_text_1"),
						result.getString("nameFrom_text_2"),
						result.getString("category_id_text_1"),
						result.getString("lectureDate"),
						result.getString("avaiable")
				));
	
		for(int i=0;i<LConsultRecord.size();i++) {
			List<ClassCategorySel> LClassCategorySel = getClassCategorySel(LConsultRecord.get(i).getConsultRecord_seq());
			List<Category> LCategory = new ArrayList<Category>();
			String classCategoryNameSel = "";
			for(int j=0;j<LClassCategorySel.size();j++) {
				LCategory = courseService.getCategory(LClassCategorySel.get(j).getCategory_id(),"");
				classCategoryNameSel += LCategory.get(0).getName()+"<br>";
			}

			LConsultRecord.get(i).setClassCategoryNameSel(classCategoryNameSel);	
		}
			
		return LConsultRecord;
	}

	public int ConsultRecordSave(ConsultRecord consultRecord, String[] category_id, String[] consultReason,
			String[] infoFrom, String[] nameFrom) {
		int newSeq;
		if (consultRecord.getConsultRecord_seq() != null && !consultRecord.getConsultRecord_seq().isEmpty()) {
			newSeq = Integer.valueOf(consultRecord.getConsultRecord_seq());
			jdbcTemplate.update(
					"UPDATE eip.consultRecord set consultCategory_id=?,oneDayValid=?,content=?,validDate=?,remark=?,employee_id=?,createDate=?,crossDate=?,infoFrom_text_1=?,nameFrom_text_1=?,nameFrom_text_2=?,category_id_text_1=?,lectureDate=? where consultRecord_seq="+ consultRecord.getConsultRecord_seq(),
					consultRecord.getConsultCategory_id(), 
					consultRecord.getOneDayValid(), 
					consultRecord.getContent(),
					consultRecord.getValidDate(), 
					consultRecord.getRemark(), 
					consultRecord.getEmployee_id(),
					consultRecord.getCreateDate(),
					consultRecord.getCrossDate(),
					consultRecord.getInfoFrom_text_1(),
					consultRecord.getNameFrom_text_1(),
					consultRecord.getNameFrom_text_2(),
					consultRecord.getCategory_id_text_1(),
					consultRecord.getLectureDate()
				);

			jdbcTemplate.update("delete from eip.classCategorySel where consultRecord_id='"+ consultRecord.getConsultRecord_seq() + "'");
			//jdbcTemplate.update("delete from eip.consultReasonSel where consultRecord_id='"+ consultRecord.getConsultRecord_seq() + "'");
			jdbcTemplate.update("delete from eip.infoFromSel where consultRecord_id='"+ consultRecord.getConsultRecord_seq() + "'");
			jdbcTemplate.update("delete from eip.nameFromSel where consultRecord_id='"+ consultRecord.getConsultRecord_seq() + "'");

		} else {
			jdbcTemplate.update("INSERT INTO eip.consultRecord VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
					consultRecord.getStudent_id(), 
					consultRecord.getConsultCategory_id(),
					consultRecord.getOneDayValid(), 
					consultRecord.getContent(), 
					consultRecord.getValidDate(),
					consultRecord.getRemark(), 
					consultRecord.getEmployee_id(), 
					consultRecord.getCreateDate(),
					consultRecord.getCrossDate(),
					consultRecord.getInfoFrom_text_1(),
					consultRecord.getNameFrom_text_1(),
					consultRecord.getNameFrom_text_2(),
					consultRecord.getCategory_id_text_1(),
					consultRecord.getLectureDate(),
					1 //avaiable 1:未結清, 0已結清
					);

			newSeq = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
		}
		if (category_id != null) {
			for (int i = 0; i < category_id.length; i++) {
				jdbcTemplate.update("INSERT INTO eip.classCategorySel VALUES (default,?,?);", newSeq, category_id[i]);
			}
		}
/**
		if (consultReason != null) {
			for (int i = 0; i < consultReason.length; i++) {
				jdbcTemplate.update("INSERT INTO eip.consultReasonSel VALUES (default,?,?);", newSeq, consultReason[i]);
			}
		}
**/
		if (infoFrom != null) {
			for (int i = 0; i < infoFrom.length; i++) {
				jdbcTemplate.update("INSERT INTO eip.infoFromSel VALUES (default,?,?);", newSeq, infoFrom[i]);
			}
		}
		
		if (nameFrom != null) {
			for (int i = 0; i < nameFrom.length; i++) {
				jdbcTemplate.update("INSERT INTO eip.nameFromSel VALUES (default,?,?);", newSeq, nameFrom[i]);
			}
		}		

		return newSeq;
	}

	public List<VirtualSubject> getVirtualSubjectForStudent(String subject_id) {
		String sql = "SELECT c.virtualSubject_seq,c.subject_id,c.child_subject_id,d.name as child_subject_name,e.name as child_category_name,a.origin_price,a.sale_price"
				+ " from eip.comboSale a,eip.comboSale_subject b,eip.virtualSubject c,eip.subject d,eip.category e"
				+ " where d.category_id=e.category_seq and d.subject_seq=b.subject_id and a.comboSale_seq=b.comboSale_id and a.isCombo=0 and b.subject_id =c.child_subject_id  and c.subject_id ='"
				+ subject_id + "'";

		List<VirtualSubject> LVirtualSubject = jdbcTemplate.query(sql,
				(result, rowNum) -> new VirtualSubject(
						result.getString("virtualSubject_seq"),
						result.getString("subject_id"), 
						result.getString("child_subject_id"),
						result.getString("child_subject_name"), 
						result.getString("child_category_name"),
						result.getString("origin_price"), 
						result.getString("sale_price")
				));
		return LVirtualSubject;
	}

	public List<VirtualSubject> getVirtualSubject(String subject_id) {
		String sql = "SELECT c.virtualSubject_seq,c.subject_id,c.child_subject_id,d.name as child_subject_name"
				+ " from eip.virtualSubject c,eip.subject d"
				+ " where d.subject_seq=c.child_subject_id  and c.subject_id ='" + subject_id + "'";

		List<VirtualSubject> LVirtualSubject = jdbcTemplate.query(sql,
				(result, rowNum) -> new VirtualSubject(
						result.getString("virtualSubject_seq"),
						result.getString("subject_id"), 
						result.getString("child_subject_id"),
						result.getString("child_subject_name"), 
						"", 
						"", 
						""
				));

		return LVirtualSubject;
	}

	public Boolean belongSameSchoolArea(String account, String grade_school_code) {
		String employee_school = accountService.getEmployee("","","","","","",account,"").get(0).getSchool();
		if (employee_school.equals(grade_school_code)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean openVideoToSchoolArea(String schoolCode, String grade_seq) {
		List<VideoOpen> LVideoOpen = courseService.getVideoOPen(grade_seq);
		Boolean isOpen = false;
		for (int i = 0; i < LVideoOpen.size(); i++) {
			if (LVideoOpen.get(i).getSchoolCode().equals(schoolCode)) {
				isOpen = true;
			}
		}
		return isOpen;
	}

	public List<Register_lagnappe> getRegister_lagnappeByRegister_seq(String Register_seq, String comboSale_id) {
		String sql = "SELECT a.*,c.sale_price,c.lagnappe_name from eip.Register_lagnappe a,eip.lagnappe c"
				+ " where  c.lagnappe_seq = a.lagnappe_id and a.Register_seq =" + Register_seq
				+ " and a.comboSale_id ='" + comboSale_id + "'";

		List<Register_lagnappe> LRegister_lagnappe = jdbcTemplate.query(sql,
				(result, rowNum) -> new Register_lagnappe(
						result.getString("register_lagnappe_seq"),
						result.getString("lagnappe_id"), 
						result.getString("lagnappe_no"), 
						result.getString("sale_price"),
						result.getString("comboSale_id"), 
						"", 
						result.getString("lagnappe_name"), 
						"", 
						"", 
						"",
						result.getString("isIssue"), 
						result.getString("issueUpdater"), 
						result.getString("issueDate"),
						result.getString("remark"),
						result.getString("Register_seq"),
						"",
						result.getString("reason")
				));
		return LRegister_lagnappe;
	}

	public List<Register_lagnappe> getRegister_lagnappe(String student_seq,String lagnappe_id,String register_lagnappe_seq,String Register_seq,String isIssue,String payOffRelease) {
		String sql = "SELECT c.payOffRelease,c.sale_price,b.Register_seq,b.comboSale_id,b.register_lagnappe_seq,b.lagnappe_no,b.lagnappe_id,c.lagnappe_name,c.origin_price,a.creater,a.update_time,b.isIssue,b.issueUpdater,b.issueDate,b.remark,b.reason"+
				" from eip.Register a,eip.Register_lagnappe b,eip.lagnappe c"+
				" where  c.lagnappe_seq=b.lagnappe_id and a.Register_seq=b.Register_seq";
		
		if (student_seq != null && !student_seq.isEmpty()) {
			sql += " and a.student_seq = " + student_seq;
		}
		if (lagnappe_id != null && !lagnappe_id.isEmpty()) {
			sql += " and b.lagnappe_id = " + lagnappe_id;
		}
		if (register_lagnappe_seq != null && !register_lagnappe_seq.isEmpty()) {
			sql += " and b.register_lagnappe_seq = " + register_lagnappe_seq;
		}		
		if (Register_seq != null && !Register_seq.isEmpty()) {
			sql += " and b.Register_seq = '"+Register_seq+"'";
		}
		if (isIssue != null && !isIssue.isEmpty()) {
			sql += " and b.isIssue = "+isIssue;
		}		
		if (payOffRelease != null && !payOffRelease.isEmpty()) {
			sql += " and c.payOffRelease = "+payOffRelease;
		}			

		List<Register_lagnappe> LRegister_lagnappe = jdbcTemplate.query(sql,
				(result, rowNum) -> new Register_lagnappe(
						result.getString("register_lagnappe_seq"),
						result.getString("lagnappe_id"), 
						result.getString("lagnappe_no"), 
						result.getString("sale_price"),
						result.getString("comboSale_id"), 
						"", 
						result.getString("lagnappe_name"),
						result.getString("origin_price"), 
						result.getString("creater"), 
						result.getString("update_time"),
						result.getString("isIssue"), 
						result.getString("issueUpdater"), 
						result.getString("issueDate"),
						result.getString("remark"),
						result.getString("Register_seq"),
						result.getString("payOffRelease"),
						result.getString("reason")
				));
		for (int i = 0; i < LRegister_lagnappe.size(); i++) {
			List<ComboSale> LcomboSale = courseSaleService.getComboSale(LRegister_lagnappe.get(i).getComboSale_id(),"","","","","","","0");
			if (LcomboSale.size() > 0) {
				LRegister_lagnappe.get(i).setComboSale_name(LcomboSale.get(0).getName());
			} else {
				LRegister_lagnappe.get(i).setComboSale_name(" -- ");
			}
		}
		return LRegister_lagnappe;
	}
	
	public List<Register_outPublisher> getRegister_outPublisher(String student_seq,String outPublisher_id,String register_outPublisher_seq,String isIssue) {
		String sql = "SELECT b.comboSale_id,a.Register_seq,b.register_outPublisher_seq,b.book_id,c.bookName,c.sellPrice,a.creater,a.update_time,b.isIssue,b.issueUpdater,b.issueDate,b.remark,b.reason"
				+ " from eip.Register a,eip.Register_outPublisher b,eip.books c"
				+ " where  c.books_seq=b.book_id and a.Register_seq=b.Register_seq and a.student_seq ="
				+ student_seq;
		if (outPublisher_id != null && !outPublisher_id.isEmpty()) {
			sql += " and b.outPublisher_id = "+outPublisher_id;
		}
		if (register_outPublisher_seq != null && !register_outPublisher_seq.isEmpty()) {
			sql += " and b.register_outPublisher_seq = "+register_outPublisher_seq;
		}
		if (isIssue != null && !isIssue.isEmpty()) {
			sql += " and b.isIssue = "+isIssue;
		}		
		List<Register_outPublisher> LRegister_outPublisher = jdbcTemplate.query(sql,
				(result, rowNum) -> new Register_outPublisher(
						result.getString("register_outPublisher_seq"),
						result.getString("comboSale_id"), 
						result.getString("Register_seq"),
						result.getString("book_id"), 
						result.getString("bookName"), 
						result.getString("sellPrice"),
						result.getString("isIssue"),  
						result.getString("issueUpdater"), 
						result.getString("issueDate"),
						result.getString("remark"),
						result.getString("update_time"),
						"", //comboSale_name
						result.getString("creater"),
						"", //payOffRelease,
						result.getString("reason")
				));

		for (int i = 0; i < LRegister_outPublisher.size(); i++) {
			List<ComboSale> LcomboSale = courseSaleService.getComboSale(LRegister_outPublisher.get(i).getComboSale_id(),"","","","","","","0");
			if (LcomboSale.size() > 0) {
				LRegister_outPublisher.get(i).setComboSale_name(LcomboSale.get(0).getName());
			} else {
				LRegister_outPublisher.get(i).setComboSale_name(" -- ");
			}
		}
		return LRegister_outPublisher;
	}	

	public Boolean BalanceSave(String student_id,String OverPaytype,String amount,String remark,String creater,String balanceTotal) {
		//費用紀錄
		jdbcTemplate.update("INSERT INTO eip.balanceRecord VALUES (default,?,?,?,?,?,NOW());",
				student_id,
				OverPaytype,
				amount,
				remark,
				creater
		);
		int balanceTotalNew = Integer.valueOf(balanceTotal);
		if (Integer.valueOf(OverPaytype) > 0) {
			balanceTotalNew += Integer.valueOf(amount);
		} else {
			balanceTotalNew -= Integer.valueOf(amount);
		}
        //帳戶餘額
		jdbcTemplate.update("UPDATE eip.student set balanceTotal=? where student_seq =?", balanceTotalNew, student_id);
		
        //儲存eip.student.[remarkTotal]所有備註
 		String remarkTotal = null;
 		String remarkTotalOri = getStudent(student_id,"","","","","","","","","").get(0).getRemarkTotal();
 		if(remarkTotalOri==null) {remarkTotalOri="";}
         Date date = new Date();
         DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
         String nowTime = dateFormat.format(date);
 		if(remark!=null && !remark.isEmpty()) {
 			remarkTotal = "------------  "+creater+" "+nowTime+"  ------------\n"+remark+"\n"+remarkTotalOri;
 		}
 		jdbcTemplate.update("Update eip.student set remarkTotal=?,update_time=NOW() where student_seq=?;",
 				remarkTotal, 
 				student_id
 		);      	
    	
        //orderChange訂單變更 主管->繳費(換科及改繳費及課程取消),無訂單編號,register_id=""
    	orderChangeSave("",student_id,OverPaytype,"","","","","",amount,"","",creater,""); 
    	
		return true;
	}

	public List<BalanceRecord> getBalanceRecord(String student_id) {
		String sql = "SELECT a.*,b.name as typeName" + " from eip.balanceRecord a,eip.balanceType b "
				+ " where a.type=b.id and student_id='" + student_id + "'";

		List<BalanceRecord> LBalanceRecord = jdbcTemplate.query(sql,
				(result, rowNum) -> new BalanceRecord(result.getString("balanceRecord_seq"),
						result.getString("student_id"), result.getString("type"), result.getString("typeName"),
						result.getString("amount"), result.getString("remark"), result.getString("creater"),
						result.getString("createTime")));
		return LBalanceRecord;
	}
/**
	public Boolean MakeUpSave(String student_id, String type, String amount, String remark, String creater,
			String createTime, String makeUpTotal) {
		jdbcTemplate.update("INSERT INTO eip.makeUpRecord VALUES (default,?,?,?,?,?,?);", student_id, type, amount,
				remark, creater, createTime);
		int makeUpTotalNew = Integer.valueOf(makeUpTotal);
		if (Integer.valueOf(type) > 0) {
			makeUpTotalNew += Integer.valueOf(amount);
		} else {
			makeUpTotalNew -= Integer.valueOf(amount);
		}

		jdbcTemplate.update("UPDATE eip.student set makeUpTotal=? where student_seq =?", makeUpTotalNew, student_id);
		return true;
	}
**/
	public List<MakeUpRecord> getMakeUpRecord(String student_id,String cutFromRegister_id) {
		String sql = "SELECT a.*,b.name as typeName from eip.makeUpRecord a,eip.makeUpType b"+
				     " where a.makeUpType=b.id";
		if (student_id != null && !student_id.isEmpty()) {
			sql += " and a.student_id = " + student_id;
		}
		if (cutFromRegister_id != null && !cutFromRegister_id.isEmpty()) {
			sql += " and a.cutFromRegister_id = " + cutFromRegister_id;
		}
		sql += " order by a.createTime desc";
	
		List<MakeUpRecord> LMakeUpRecord = jdbcTemplate.query(sql,
				(result, rowNum) -> new MakeUpRecord(
						result.getString("makeUpRecord_seq"),
						result.getString("student_id"),
						result.getString("makeUpType"), 
						result.getString("typeName"),
						result.getString("amount"), 
						result.getString("content"),
						result.getString("remark"), 
						result.getString("creater"),
						result.getString("createTime"),
						result.getString("cutFromRegister_id")
				));
		return LMakeUpRecord;
	}
	public List<SignRecordChange> getSignRecordChange(String student_id) {
		String sql = "SELECT * from eip.signRecordChange "
				+ " where student_id='"+student_id+"' order by update_time desc";

		List<SignRecordChange> LSignRecordChange = jdbcTemplate.query(sql,
				(result, rowNum) -> new SignRecordChange(
						result.getString("signRecordChange_seq"),
						result.getString("student_id"),
						result.getString("class_style"), 
						result.getString("fromClassStr"),
						result.getString("toClassStr"), 
						result.getString("makeUpNoUsed"), 
						result.getString("update_time"),
						result.getString("updater")
				));
		return LSignRecordChange;
	}	

	public Boolean deleteInterestSubject(String Register_comboSale_id) {

		jdbcTemplate.update("DELETE from eip.interestSubject where Register_comboSale_id=?", Register_comboSale_id);
		return true;
	}

	public Boolean saveInterestSubject(String Register_comboSale_id, String[] child_subject_id) {

		for (int i = 0; i < child_subject_id.length; i++) {
			jdbcTemplate.update("Insert into eip.interestSubject VALUES (default,?,?);", Register_comboSale_id,
					child_subject_id[i]);
		}
		return true;
	}

	public List<InterestSubject> getInterestSubject(String Register_comboSale_id) {
		String sql = "SELECT *" + " from eip.interestSubject " + " where Register_comboSale_id='"
				+ Register_comboSale_id + "'";

		List<InterestSubject> LInterestSubject = jdbcTemplate.query(sql,(result, rowNum) -> new InterestSubject(
				result.getString("interestSubject_seq"),
				result.getString("Register_comboSale_id"), 
				result.getString("child_subject_id")
		));
		return LInterestSubject;
	}

	public List<Register> getRegister(String student_seq, String Register_seq) {
		String sql = "select * from eip.Register where 1=1 ";
		if (student_seq != null && !student_seq.isEmpty()) {
			sql += " and student_seq = " + student_seq;
		}
		if (Register_seq != null && !Register_seq.isEmpty()) {
			sql += " and Register_seq = " + Register_seq;
		}

		List<Register> LRegister = jdbcTemplate.query(sql,
				(result, rowNum) -> new Register(
						result.getString("Register_seq"), 
						result.getString("student_seq"),
						result.getString("originPrice"), 
						result.getString("actualPrice"),
						"",
						result.getString("paid"),
						"",
						result.getString("comment"), 
						result.getString("creater"), 
						result.getString("orderStatus"),
						result.getString("update_time"),
						"", 
						"", 
						"", 
						"",
						result.getString("cancelRegister")
		));
		return LRegister;
	}
	
	public List<Register> getRegister2(String student_seq, String grade_id) {
		String sql = "select * from eip.Register a,eip.Register_comboSale_grade b,eip.Register_comboSale c"+
					 " where a.Register_seq=c.Register_id and c.Register_comboSale_seq=b.Register_comboSale_id and b.grade_id="+grade_id+" and a.student_seq="+student_seq;

		List<Register> LRegister = jdbcTemplate.query(sql,
				(result, rowNum) -> new Register(
						result.getString("Register_seq"), 
						result.getString("student_seq"),
						result.getString("originPrice"), 
						result.getString("actualPrice"),
						"",
						result.getString("paid"),
						"",
						result.getString("comment"), 
						result.getString("creater"), 
						result.getString("orderStatus"),
						result.getString("update_time"),
						"", 
						"", 
						"", 
						"",
						result.getString("cancelRegister")
		));
		
		return LRegister;
	}
	
	public List<Register> getRegister3(String Register_comboSale_grade_seq) {
		String sql = "select * from eip.Register a,eip.Register_comboSale c,eip.Register_comboSale_grade b"+
					 " where a.Register_seq=c.Register_id and c.Register_comboSale_seq=b.Register_comboSale_id and b.Register_comboSale_grade_seq="+Register_comboSale_grade_seq;

		List<Register> LRegister = jdbcTemplate.query(sql,
				(result, rowNum) -> new Register(
						result.getString("Register_seq"), 
						result.getString("student_seq"),
						result.getString("originPrice"), 
						result.getString("actualPrice"),
						"",
						result.getString("paid"),
						"",
						result.getString("comment"), 
						result.getString("creater"), 
						result.getString("orderStatus"),
						result.getString("update_time"),
						"", 
						"", 
						"", 
						"",
						result.getString("cancelRegister")
		));
		return LRegister;
	}		

	public List<Register_mock> getMockByRegisterSeq(String Register_seq,String comboSale_id,String active) {
		String sql = "select a.*,b.mock_name from eip.Register_mock a, eip.mock b" + " where a.mock_id = b.mock_seq ";
		if (Register_seq != null && !Register_seq.isEmpty()) {
			sql += " and Register_seq = " + Register_seq;
		}
		if (comboSale_id != null && !comboSale_id.isEmpty()) {
			sql += " and a.comboSale_id = " + comboSale_id;
		}
		if (active != null && !active.isEmpty()) {
			sql += " and a.active = " + active;
		}		
		

		List<Register_mock> LRegister_mock = jdbcTemplate.query(sql,
				(result, rowNum) -> new Register_mock(
						result.getString("register_mock_seq"),
						result.getString("comboSale_id"), 
						result.getString("Register_seq"), 
						result.getString("mock_id"),
						result.getString("mock_name"),
						result.getString("active")
				));
		return LRegister_mock;
	}
	
	public List<Register_outPublisher> getOutPublisherByRegisterSeq(String Register_seq, String comboSale_id) {
		String sql = "select a.*,b.bookName,b.sellPrice from eip.Register_outPublisher a, eip.books b" + " where a.book_id = b.books_seq ";
		if (Register_seq != null && !Register_seq.isEmpty()) {
			sql += " and Register_seq = " + Register_seq;
		}
		if (comboSale_id != null && !comboSale_id.isEmpty()) {
			sql += " and a.comboSale_id = " + comboSale_id;
		}

		List<Register_outPublisher> LRegister_outPublisher = jdbcTemplate.query(sql,(result, rowNum) -> new Register_outPublisher(
				result.getString("register_outPublisher_seq"),
				result.getString("comboSale_id"), 
				result.getString("Register_seq"), 
				result.getString("book_id"),
				result.getString("bookName"),
				result.getString("sellPrice"),
				result.getString("isIssue"),
				result.getString("issueUpdater"),
				result.getString("issueDate"),
				result.getString("remark"),
				"",
				"",
				"",
				"1", //payOffRelease
				result.getString("reason")
		));
		return LRegister_outPublisher;
	}	

	public List<Register_counseling> getCounselingByRegisterSeq(String Register_seq, String comboSale_id) {
		String sql = "select a.*,b.counseling_name,b.category_id from eip.Register_counseling a, eip.counseling b"
				+ " where a.counseling_id = b.counseling_seq";
		if (Register_seq != null && !Register_seq.isEmpty()) {
			sql += " and a.Register_seq = " + Register_seq;
		}
		if (comboSale_id != null && !comboSale_id.isEmpty()) {
			sql += " and a.comboSale_id = " + comboSale_id;
		}

		List<Register_counseling> LRegister_counseling = jdbcTemplate.query(sql,
				(result, rowNum) -> new Register_counseling(
						result.getString("register_counseling_seq"),
						result.getString("comboSale_id"), 
						result.getString("Register_seq"),
						result.getString("counseling_id"), 
						result.getString("counseling_name"),
						result.getString("category_id")
				));
		return LRegister_counseling;
	}

	public List<NoClass> getNoClass(String teacher_id,String dateSel,String slot_id) {
		String sql = "select * from eip.noClass where 1=1";
		if (teacher_id != null && !teacher_id.isEmpty()) {
			sql += " and teacher_id = " + teacher_id;
		}
		if (dateSel != null && !dateSel.isEmpty()) {
			sql += " and dateSel = '"+dateSel+"'";
		}
		if (slot_id != null && !slot_id.isEmpty()) {
			sql += " and slot_id = " + slot_id;
		}

		List<NoClass> LNoClass = jdbcTemplate.query(sql,
				(result, rowNum) -> new NoClass(
						result.getString("noClass_seq"), 
						result.getString("teacher_id"),
						result.getString("dateSel"), 
						result.getString("timeFrom"), 
						result.getString("timeTo"),
						result.getString("noClassType"),
						result.getString("slot_id")
		));
		return LNoClass;
	}

	public Boolean saveRegister_mockBook(Register_mockBook register_mockBook) {

		jdbcTemplate.update("Insert into eip.Register_mockBook VALUES (default,?,?,?,?,?,?,?,?);",
				register_mockBook.getRegister_seq(), register_mockBook.getMock_id(),
				register_mockBook.getMockDetail_id(), register_mockBook.getMockSet_id(),
				register_mockBook.getDate_mockVideo(), register_mockBook.getSlot_mockVideo(),
				register_mockBook.getSchool_mockVideo(), register_mockBook.getAttend());
		return true;
	}

	public List<orderChange> getOrderChange(String school_code,String ch_name) {
		// left join 因為register_id可能不存在(退款並不須指名訂單)
		String sql = "select a.*,c.ch_name as studentName,e.name as school,b.cancelRegister"+
				" from eip.orderChange a left join eip.Register b on a.register_id=b.Register_seq"+
				",eip.student c,eip.employee d,eip.school e"+
				" where a.student_id=c.student_seq and a.creater=d.account0 and d.school=e.code";
		if (ch_name != null && !ch_name.isEmpty()) {
			sql += " and c.ch_name like '%"+ch_name.trim()+"%'";
		}
		if (school_code != null && !school_code.isEmpty()) {
			sql += " and d.school = '" + school_code + "'";
		} // 建立者所屬校區

		List<orderChange> LOrderChange = jdbcTemplate.query(sql,
				(result, rowNum) -> new orderChange(
						result.getString("orderChange_seq"),
						result.getString("student_id"),
						result.getString("register_id"), 
						result.getString("changeType"),
						result.getString("subject_from"), 
						result.getString("subject_to"),
						result.getString("gradeName_from"), 
						result.getString("gradeName_to"),						
						result.getString("payMoney_from"), 
						result.getString("payMoney_to"),
						result.getString("actualPrice_from"), 
						result.getString("actualPrice_to"),
						result.getString("creater"), 
						result.getString("updateTime"), 
						result.getString("studentName"),
						result.getString("school"),
						result.getString("cancelRegister"),
						result.getString("isUpdate")
				));
		return LOrderChange;
	}

	public List<orderChange> getCostChange(String grade_id,String student_id) {
		//String sql0 = "select a.Register_seq,a.cancelRegister from eip.Register a,eip.Register_comboSale b,eip.Register_comboSale_grade c"+
					  //" where b.Register_id=a.Register_seq and c.Register_comboSale_id=b.Register_comboSale_seq and c.grade_id="+grade_id+" and a.student_seq="+student_id;	
		//int Register_seq = jdbcTemplate.queryForObject(sql0, Integer.class);
		String Register_seq="130920"; //??
				
		
		String sql = "select a.* from eip.orderChange a where student_id='"+student_id+"' and register_id='"+Register_seq+"' order by updateTime desc";

		List<orderChange> LOrderChange = jdbcTemplate.query(sql,
				(result, rowNum) -> new orderChange(
						result.getString("orderChange_seq"),
						result.getString("student_id"),
						result.getString("register_id"), 
						result.getString("changeType"),
						result.getString("subject_from"), 
						result.getString("subject_to"),
						result.getString("gradeName_from"), 
						result.getString("gradeName_to"),						
						result.getString("payMoney_from"), 
						result.getString("payMoney_to"),
						result.getString("actualPrice_from"), 
						result.getString("actualPrice_to"),
						result.getString("creater"), 
						result.getString("updateTime"), 
						"",
						"",
						"",
						result.getString("isUpdate")
					));
		return LOrderChange;	
	}
	
	public Boolean orderChangeSave(
			String register_id, 
			String student_id,
			String changeType, 
			String subject_from, 
			String subject_to,
			String gradeName_from, 
			String gradeName_to,			
			String payMoney_from, 
			String payMoney_to, 
			String actualPrice_from, 
			String actualPrice_to, 
			String crater,
			String isUpdate
		) {
		jdbcTemplate.update("Insert into eip.orderChange VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,Now(),?);", 
				student_id,
				register_id,
				changeType, 
				subject_from, 
				subject_to,
				gradeName_from, 
				gradeName_to, 				
				payMoney_from, 
				payMoney_to, 
				actualPrice_from, 
				actualPrice_to,
				crater,
				isUpdate
		);

		return true;
	}
	
	public Boolean orderChangeUpdate(
			String register_id,
			String student_seq,
			String changeType,
			String gradeName_to,
			String isUpdate
	) {
		//舊的改為line-Through
		jdbcTemplate.update("UPDATE eip.orderChange set isUpdate=? where register_id=? and student_id=? and changeType=? and gradeName_to=?;", 
				isUpdate,
				register_id,
				student_seq,
				changeType,
				gradeName_to
		);		
		return true;
	}
	
	public Boolean SaveRegister_comboSale(String Register_comboSale_seq,String Register_id,String comboSale_id,String subject_id,String subject_id_virtual,String costShare) {
		//新增選科目
		Subject subject = courseService.getSubject("",subject_id,"","","","0").get(0); 
		jdbcTemplate.update("Insert into eip.Register_comboSale VALUES (default,?,?,?,?,?,?,?,?,?,?);", 
				Register_id,
				comboSale_id,
				subject_id,
				"-99",
				subject_id_virtual,
				"",
				Register_comboSale_seq,
				1,
				costShare,
				subject.getPrice()
		);
		//虛擬科目扣除
		jdbcTemplate.update("UPDATE eip.Register_comboSale set gradeNoLeft=?,active=? where Register_comboSale_seq=?;", 
				0,
				0,
				Register_comboSale_seq
		);		
		return true;
	}
	
	public Boolean bookSubjectCancel(String Register_comboSale_seq) {
		Register_comboSale register_comboSale1 = getComboSaleByRegister(Register_comboSale_seq,"","","","","",false,false,"").get(0);
		String from_Register_comboSale_id = register_comboSale1.getFrom_Register_comboSale_id();
		String gradeNoLeft1 = register_comboSale1.getGradeNoLeft();
		
		Register_comboSale register_comboSale2 = getComboSaleByRegister(from_Register_comboSale_id,"","","","","",false,false,"").get(0);
		String gradeNoLeft2 = register_comboSale2.getGradeNoLeft();
		
		String gradeNoLeft = String.valueOf(Integer.valueOf(gradeNoLeft1)+Integer.valueOf(gradeNoLeft2));
		
		jdbcTemplate.update("UPDATE eip.Register_comboSale set gradeNoLeft=?,active=? where Register_comboSale_seq=?;", 
				gradeNoLeft,
				1,
				from_Register_comboSale_id
		);	

		if(Register_comboSale_seq!=null && !Register_comboSale_seq.isEmpty()) {
			jdbcTemplate.update("DELETE from eip.Register_comboSale  where Register_comboSale_seq=?;", 
					Register_comboSale_seq
			);
		}	
		return true;
	}
	
	public Boolean JLM_gradeAllSave(JLM_gradeAll jLM_gradeAll) {
		//新增選科目
		jdbcTemplate.update("Insert into eip.JLM_gradeAll VALUES (default,?,?,?,?,?,?);", 
				jLM_gradeAll.getCategory_id(),
				jLM_gradeAll.getGradeId(),
				jLM_gradeAll.getGradeName(),
				jLM_gradeAll.getDateFrom(),
				jLM_gradeAll.getDateTo(),
				""
		);
		return true;
	}
	
	public List<JLM_studentPay> getJLM_studentPay(String student_no,String exclude_sitNo,String limit,String gradeId) {
		String sql = "SELECT * from eip.JLM_studentPay where 1=1";
		if (student_no != null && !student_no.isEmpty()) {
			sql += " and student_no='"+student_no+"'";
		}
		if (exclude_sitNo != null && !exclude_sitNo.isEmpty()) {
			sql += " and (sitNo is null or sitNo not like '%"+exclude_sitNo+"%')";
		}
		if (gradeId != null && !gradeId.isEmpty()) {
			sql += " and gradeId='"+gradeId+"'";
		}		
		sql+=" order by studentPay_seq desc";
		
		if (limit != null && !limit.isEmpty()) {
			sql += " LIMIT "+limit;
		}		
		  List<JLM_studentPay> LJLM_studentPay =
		  jdbcTemplate.query(sql,(result,rowNum)->new JLM_studentPay(
				  result.getString("studentPay_seq"), 
				  result.getString("student_no"), 
				  result.getString("gradeId"),
				  result.getString("gradeName"),
				  result.getString("saleId"), 
				  result.getString("salePerson"),
				  result.getString("originPrice"), 
				  result.getString("discountPrice"),
				  "",
				  result.getString("paidMoney"),
				  result.getString("gradeFrom")==null?"":result.getString("gradeFrom").substring(0,10),
				  result.getString("gradeTo")==null?"":result.getString("gradeFrom").substring(0,10),
				  "<A href='javascript:void(0)' onClick=\"javascript:getJLM_studentPayRecord('"+result.getString("student_no")+"','"+result.getString("gradeId")+"')\"> ... </A>",
				  result.getString("sitNo")
		  ));
		  
		  for(int i=0;i<LJLM_studentPay.size();i++) {
			//應繳  
			  if(LJLM_studentPay.get(i).getDiscountPrice()!=null && !LJLM_studentPay.get(i).getDiscountPrice().isEmpty()) {
				  LJLM_studentPay.get(i).setNeedPay(Integer.toString(Integer.valueOf(LJLM_studentPay.get(i).getOriginPrice())-Integer.valueOf(LJLM_studentPay.get(i).getDiscountPrice())));
			  }else {
				  LJLM_studentPay.get(i).setNeedPay(LJLM_studentPay.get(i).getOriginPrice());
			  }
			//已繳
			  List<JLM_studentPayRecord> LJLM_studentPayRecord = getJLM_studentPayRecord(LJLM_studentPay.get(i).getStudent_no(),LJLM_studentPay.get(i).getGradeId(),"");
			  int paidMoney = 0;
			  for(int j=0;j<LJLM_studentPayRecord.size();j++) {
				  if(LJLM_studentPayRecord.get(j).getPayMoney()!=null){
					  String tmp = LJLM_studentPayRecord.get(j).getPayMoney();
					  if(!tmp.equals("")) {
						  paidMoney += Integer.valueOf(tmp.substring(0,tmp.indexOf(".")));
					  }
				  }  	
			  }
			  LJLM_studentPay.get(i).setPaidMoney(String.valueOf(paidMoney));
		  }			  
		return LJLM_studentPay;
	}
	
	public List<String> registerGradeOriginPrice(String Register_seq) {
		String sql = "SELECT b.price from eip.Register_comboSale a, subject b"+                 
	                 " where a.Register_comboSale_seq in (select Register_comboSale_id from eip.Register_comboSale_grade where allow_attend=1)"+
					 " and a.subject_id = b.subject_seq and a.Register_id="+Register_seq;

		List<String> subjectPrice = jdbcTemplate.query(sql,(result, rowNum) -> new String(result.getString("price")));
		return subjectPrice;
	}
	
	public int subjectSalePrice(String subject_id) {
		String sql = "SELECT price from subject where subject_seq="+subject_id;
		String sale_price = jdbcTemplate.queryForObject(sql,java.lang.String.class);		
		return Integer.valueOf(sale_price);
	}	
	

	public Boolean updateMakeUpTotal(String student_no,int makeUpTotal,String amount,String student_id,String creater,String makeUpType,String remark,String content,String register_id) {
	    //無扣點數時
		if(!amount.equals("0")) {
			jdbcTemplate.update("UPDATE eip.student set makeUpTotal=? where student_no=?;", 
					makeUpTotal,
					student_no
			);
	    }	

		//預約紀錄紀錄及點數修改
		jdbcTemplate.update("INSERT INTO eip.makeUpRecord VALUES (default,?,?,?,?,?,?,NOW(),?);", 
				student_id, 
				makeUpType, //2:購買新增
				amount,
				content, 
				remark, 
				creater,
				register_id
		);		
		return true;
	}
	
	public Boolean ClassBookCancel(String Register_comboSale_grade_id,String class_th,String signRecordHistory_seq,String makeUpNo,String student_seq,String creater,String register_id) {	
		jdbcTemplate.update("DELETE from eip.signrecordhistory where signRecordHistory_seq = ?", 
				signRecordHistory_seq
		);
		
		//回復原來的
		String sql = "select max(signRecordHistory_seq) from eip.signrecordhistory where active=0 and student_id="+student_seq+" and Register_comboSale_grade_id='"+Register_comboSale_grade_id+"' and class_th ="+class_th;
		String signRecordHistory_id = jdbcTemplate.queryForObject(sql,java.lang.String.class);
		jdbcTemplate.update("UPDATE eip.signrecordhistory set active = 1 where signRecordHistory_seq = ?", 
				signRecordHistory_id
		);
		
		
		//修改eip.student.[makeUpTotal]
		if(Integer.valueOf(makeUpNo)>1) {
    		Student student = getStudent(student_seq,"","","","","","","","","").get(0);
    		updateMakeUpTotal(
    				student.getStudent_no(),
    				Integer.valueOf(student.getMakeUpTotal())+1,
    				"1",
    				student_seq,
    				creater,
    				"1",//1:課程新增
    				"" ,//remark
    				"", //content
    				register_id
		   );		
		}		
		return true;
	}
	
	public Boolean updateRegister_lagnappe(String register_lagnappe_seq,String isIssue,String ch_name,String reason) {
		jdbcTemplate.update("UPDATE eip.Register_lagnappe set isIssue=?,issueUpdater=?,issueDate=NOW(),reason=? where register_lagnappe_seq =?",
				isIssue, 
				ch_name,
				reason,
				register_lagnappe_seq
				
		);
		return true;
	}
	
	public Boolean updateRegister_outPublisher(String register_outPublisher_seq,String isIssue,String ch_name,String createTime,String reason) {
		jdbcTemplate.update("UPDATE eip.Register_outPublisher set isIssue=?,issueUpdater=?,issueDate=?,reason=? where register_outPublisher_seq =?",
				isIssue, 
				ch_name,
				createTime,
				reason,
				register_outPublisher_seq			
		);
		return true;
	}	

/**	
	public Boolean pay_statusSave(String Register_comboSale_id,String Register_comboSale_grade_seq,String allow_attend,String creater) {
		jdbcTemplate.update("UPDATE eip.Register_comboSale_grade set allow_attend=? where Register_comboSale_grade_seq =?",
				allow_attend, 
				Register_comboSale_grade_seq			
		);
		String reason = "未結清";
		if(allow_attend.equals("1")) {
			reason="先上課";
			admService.SaveRegisterLog(Register_comboSale_id, Register_comboSale_grade_seq, "1",reason,"-1",creater);
		}	
		return true;
	}	
**/	
	public List<Student> getGradeStudent(String ch_name,String en_name,String student_no,String idn,String mobile,String email,String student_seq,String grade_id,String class_style,String school_code,String in_register_status){

		String sql = "select distinct(a.student_no) as fakeCol,a.* from eip.student a,eip.signRecordHistory b"
				   + " where a.student_seq = b.student_id";
		
		if (ch_name != null && !ch_name.isEmpty()) {
			sql += " and a.ch_name ='"+ch_name+"'";
		}
		if (en_name != null && !en_name.isEmpty()) {
			sql += " and a.en_name ='"+en_name+"'";
		}
		if (student_no != null && !student_no.isEmpty()) {
			sql += " and a.student_no ='"+student_no+"'";
		}
		if (idn != null && !idn.isEmpty()) {
			sql += " and a.idn ='"+idn+"'";
		}
		if (mobile != null && !mobile.isEmpty()) {
			sql += " and a.mobile ='"+mobile+"'";
		}
		if (email != null && !email.isEmpty()) {
			sql += " and a.email ='"+email+"'";
		}		
		if (student_seq != null && !student_seq.isEmpty()) {
			sql += " and a.student_seq ="+student_seq;
		}
		if (grade_id != null && !grade_id.isEmpty()) {
			sql += " and b.grade_id ="+grade_id;
		}
		if (class_style != null && !class_style.isEmpty()) {
			sql += " and b.class_style ="+class_style;
		}
		if (school_code != null && !school_code.isEmpty()) {
			sql += " and b.school_code ='"+school_code+"'";
		}
		if(in_register_status!=null && !in_register_status.isEmpty()) { 
			sql +=" and b.register_status in "+in_register_status;
	    }	


		List<Student> LStudent = jdbcTemplate.query(sql,(result, rowNum) -> new Student(
						result.getString("student_seq"),
						result.getString("student_no"),
						result.getString("ch_name"), 
						result.getString("en_name"),
						result.getString("sex") == null ? "" : 
						result.getString("sex"),
						result.getString("category"),
						result.getString("derived"),
						result.getString("idn"),
						result.getString("birthday"),
						result.getString("identity"),
						result.getString("tel"), 
						result.getString("mobile_1"),
						result.getString("mobile_2"),
						result.getString("email_1"),
						result.getString("email_2"),
						result.getString("address_1"),
						result.getString("address_2"),
						result.getString("fb"),
						result.getString("line"), 
						result.getString("parent_1_name"),
						result.getString("parent_1_mobile"),
						result.getString("parent_1_email"),
						result.getString("parent_1_line"),
						result.getString("parent_2_name"),
						result.getString("parent_2_mobile"),
						result.getString("parent_2_email"),
						result.getString("parent_2_line"), 
						//result.getString("createrName"),
						"",
						result.getString("create_time"),
						result.getString("editor"),
						result.getString("remark") == null ? "": result.getString("remark").replaceAll("(\r\n|\r|\n|\n\r)", "<br>"),
						result.getString("remark2") == null ? "": result.getString("remark2").replaceAll("(\r\n|\r|\n|\n\r)", "<br>"),
						result.getString("special_idn"), 
						result.getString("col_3"),
						result.getString("col_4"),
						result.getString("balanceTotal") == null ? "0" : result.getString("balanceTotal"),
						result.getString("makeUpTotal") == null ? "0" : result.getString("makeUpTotal"),"<img src=\'/images/studentPhoto/"+result.getString("photo")+"\' height=\'80px\'/>",
					    result.getString("school_code"),
					    result.getString("school_code2"),
					    result.getString("updater"),
					    result.getString("update_time"),
					    result.getString("col_5"),
					    "",
					    result.getString("remarkTotal"),
					    result.getString("handover"),
					    result.getString("tel2"),
					    result.getString("company_tel"),
					    result.getString("agent_studentNo"),
					    result.getString("grade_highSchool"),
					    result.getString("postCode"),
					    result.getString("agent_studentName"),
					    result.getString("passwd"),
					    result.getString("col_6"),
					    result.getString("col_7"),
					    result.getString("col_8"),
					    result.getString("col_9"),
					    result.getString("degree"),
					    result.getString("cowork"),
					    "",
					    ""
				));

		return LStudent;		
	}
	
	public List<Student> getGradeStudent2(String grade_id,String class_style,String school_code,String register_status){

		String sql = "select a.*,d.sitNo,d.updater as updater2,d.update_time as update_time2,d.class_style from"+ 
					 " eip.student a, eip.Register b,eip.Register_comboSale c, eip.Register_comboSale_grade d" + 
				     " where a.student_seq = b.student_seq and b.Register_seq=c.Register_id and c.Register_comboSale_seq=d.Register_comboSale_id";
					 		
		if (grade_id != null && !grade_id.isEmpty()) {
			sql += " and d.grade_id ='"+grade_id+"'";
		}
		if (class_style != null && !class_style.isEmpty()) {
			sql += " and d.class_style ='"+class_style+"'";
		}
		if (school_code != null && !school_code.isEmpty()) {
			sql += " and d.school_code ='"+school_code+"'";
		}
		if (grade_id != null && !grade_id.isEmpty()) {
			sql += " and d.register_status ='"+register_status+"'";
		}
		
		List<Student> LStudent = jdbcTemplate.query(sql,(result, rowNum) -> new Student(
				result.getString("student_seq"),
				result.getString("student_no"),
				result.getString("ch_name"), 
				result.getString("en_name"),
				result.getString("sex") == null ? "" : 
				result.getString("sex"),
				result.getString("category"),
				result.getString("derived"),
				result.getString("idn"),
				result.getString("birthday"),
				result.getString("identity"),
				result.getString("tel"), 
				result.getString("mobile_1"),
				result.getString("mobile_2"),
				result.getString("email_1"),
				result.getString("email_2"),
				result.getString("address_1"),
				result.getString("address_2"),
				result.getString("fb"),
				result.getString("line"), 
				result.getString("parent_1_name"),
				result.getString("parent_1_mobile"),
				result.getString("parent_1_email"),
				result.getString("parent_1_line"),
				result.getString("parent_2_name"),
				result.getString("parent_2_mobile"),
				result.getString("parent_2_email"),
				result.getString("parent_2_line"), 
				//result.getString("createrName"),
				"",
				result.getString("create_time"),
				result.getString("editor"),
				result.getString("remark") == null ? "": result.getString("remark").replaceAll("(\r\n|\r|\n|\n\r)", "<br>"),
				result.getString("remark2") == null ? "": result.getString("remark2").replaceAll("(\r\n|\r|\n|\n\r)", "<br>"),
				result.getString("special_idn"), 
				result.getString("col_3"),
				result.getString("col_4"),
				result.getString("balanceTotal") == null ? "0" : result.getString("balanceTotal"),
				result.getString("makeUpTotal") == null ? "0" : result.getString("makeUpTotal"),"<img src=\'/images/studentPhoto/"+result.getString("photo")+"\' height=\'80px\'/>",
			    result.getString("school_code"),
			    result.getString("school_code2"),
			    result.getString("updater"),
			    result.getString("update_time"),
			    result.getString("col_5"),
			    result.getString("sitNo"),
			    result.getString("remarkTotal"),
			    result.getString("handover"),
			    result.getString("tel2"),
			    result.getString("company_tel"),
			    result.getString("agent_studentNo"),
			    result.getString("grade_highSchool"),
			    result.getString("postCode"),
			    result.getString("agent_studentName"),
			    result.getString("passwd"),
			    result.getString("col_6"),
			    result.getString("col_7"),
			    result.getString("col_8"),
			    result.getString("class_style"), //借用col_9 為class_style
			    result.getString("degree"),
			    result.getString("cowork"),
			    result.getString("updater2"),
			    result.getString("update_time2")
		));

		return LStudent;			
	}
	
	public List<StudentGradeShare> getGradeStudentShare(String grade_id,String class_style,String school_code,String register_status){

		String sql = "select a.student_seq,a.student_no,a.ch_name,c.costShare,d.class_style,d.school_code,e.name as school_name, d.register_status from"+ 
					 " eip.student a, eip.Register b,eip.Register_comboSale c, eip.Register_comboSale_grade d,eip.school e" + 
				     " where e.code = d.school_code and a.student_seq = b.student_seq and b.Register_seq=c.Register_id and c.Register_comboSale_seq=d.Register_comboSale_id";
					 		
		if (grade_id != null && !grade_id.isEmpty()) {
			sql += " and d.grade_id ='"+grade_id+"'";
		}
		if (class_style != null && !class_style.isEmpty()) {
			sql += " and d.class_style ='"+class_style+"'";
		}
		if (school_code != null && !school_code.isEmpty()) {
			sql += " and d.school_code ='"+school_code+"'";
		}
		if (register_status != null && !register_status.isEmpty()) {
			sql += " and d.register_status ='"+register_status+"'";
		}
	
		List<StudentGradeShare> LStudentGradeShare = jdbcTemplate.query(sql,(result, rowNum) -> new StudentGradeShare(
				result.getString("student_seq"),
				result.getString("student_no"),
				result.getString("costShare"),
				result.getString("ch_name"),
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				result.getString("class_style"),
				result.getString("school_code"),
				result.getString("school_name"),
				result.getString("register_status")
		));
		return LStudentGradeShare;			
	}		
    
	//正班訂班總人數
	public int getRegisterGradeNo(String grade_id) {
		int gradeNo = jdbcTemplate.queryForObject(
				         "select count(*) from eip.Register_comboSale_grade"+
		                 " where grade_id="+grade_id+" and register_status=1 and class_style=1", 
		                 Integer.class
		              );		
		return gradeNo;
		
		
	}
	
	//Video訂班總人數
	public int getVideoRegisterGradeNo(String grade_id) {
		int gradeNo = jdbcTemplate.queryForObject(
				         "select count(*) from eip.Register_comboSale_grade"+
		                 " where grade_id="+grade_id+" and register_status=1 and class_style=2", 
		                 Integer.class
		              );		
		return gradeNo;
	}	

	//取消訂班	
	public Boolean cancelRegister(String Register_seq,String updater,String commentTotal ,String cashRefund,String saveAccount,String student_seq) {
		jdbcTemplate.update("Update eip.Register set cancelRegister=?,update_time=NOW(),comment=? where Register_seq=?;",
				1, 
				commentTotal,
				Register_seq
		);

		//主管繳費紀錄
		if(cashRefund!=null && !cashRefund.isEmpty()) {
			orderChangeSave(
					Register_seq,
					student_seq,
					"8", //訂單取消退款 
					"", 
					"",
					"", 
					"",			
					"", 
					cashRefund, 
					"", 
					"", 
					updater,
					""
			);		
		}
		//溢繳紀錄
		/*
		 * if(saveAccount!=null && !saveAccount.isEmpty()) { String balanceTotal =
		 * getStudent(student_seq,"","","","","","").get(0).getBalanceTotal();
		 * if(balanceTotal==null || balanceTotal.isEmpty()) {balanceTotal="0";} String
		 * OverPaytype = "2"; //訂單取消-新增
		 * BalanceSave(student_seq,OverPaytype,saveAccount,commentCancel,updater,
		 * balanceTotal); }
		 */
		
		//點名紀錄eip.signRecordHistory 
		List<Register_comboSale> LRegister_comboSale = getComboSaleByRegister("",Register_seq,"","","","",false,false,"");		
		for(int i=0;i<LRegister_comboSale.size();i++) {
			 List<Register_comboSale_grade> LRegister_comboSale_grade = courseService.getRegister_comboSale_grade(LRegister_comboSale.get(i).getRegister_comboSale_seq(),"","","");
			 for(int j=0;j<LRegister_comboSale_grade.size();j++) {
				jdbcTemplate.update("UPDATE eip.signRecordHistory set register_status=? where Register_comboSale_grade_id=?",
					  2,
					  LRegister_comboSale_grade.get(j).getRegister_comboSale_grade_seq()
				);
			 }	
		}
		return true;
	}
	
	public List<ExperienceHistory> getExperienceHistory() {
		String sql = "SELECT * from eip.experienceHistory";

		List<ExperienceHistory> LExperienceHistory = jdbcTemplate.query(sql,(result, rowNum) -> new ExperienceHistory(
				result.getString("experienceHistory_seq"),
				result.getString("experience_id"), 
				result.getString("experience_name")
		));
		return LExperienceHistory;
	}
	
	public List<StudentExperience> getStudentExperience(String student_id,String experience_id,String studentExperience_seq,String haveRead) {
		String sql = "SELECT a.*,b.experience_name,d.ch_name,d.student_no from eip.studentExperience a,eip.experienceHistory b,eip.student d"+
				 " where a.experience_id=b.experience_id  and d.student_seq=a.student_id";	
		if (student_id != null && !student_id.isEmpty()) {
			sql+= " and a.student_id ='"+student_id+"'";
		}
		if (experience_id != null && !experience_id.isEmpty()) {
			sql+= " and a.experience_id ='"+experience_id+"'";
		}
		if (haveRead != null && !haveRead.isEmpty()) {
			sql+= " and a.haveRead ="+haveRead;
		}		
		if (studentExperience_seq != null && !studentExperience_seq.isEmpty()) {
			sql+= " and a.studentExperience_seq ='"+studentExperience_seq+"'";
		}	
		sql+= " order by studentExperience_seq desc";
		
		List<StudentExperience> LStudentExperience = jdbcTemplate.query(sql,(result, rowNum) -> new StudentExperience(
				result.getString("studentExperience_seq"),
				result.getString("student_id"),
				"",//school_code
				"",//school_name
				result.getString("experience_id"),
				result.getString("experience_name"),
				result.getString("experience_state"),
				result.getString("experience_content"),
				result.getString("validDate"),
				result.getString("creater"),
				result.getString("createTime"),
				result.getString("Register_id"),
				result.getString("haveRead"),
				result.getString("ch_name"),
				result.getString("student_no")
		));
		return LStudentExperience;
	}
	
	public Boolean insertStudentExperience(String student_id,String school_code,String experience_id,String experience_state,String experience_content,String validDate,String creater,String Register_id) {
		jdbcTemplate.update("Insert into eip.studentExperience VALUES (default,?,?,?,?,?,?,?,NOW(),?,?);", 
				student_id,
				school_code,
				experience_id,
				experience_state,
				experience_content,
				validDate,
				creater,
				Register_id,
				0 //haveRead
		);
		return true;
	}	
	
	public Boolean afterThenNow(String offSell0,String nowDate) {
		Boolean returnValue = false;
		if(offSell0.length()==10) {
		   String offSell = offSell0.substring(6,10)+offSell0.substring(0,2)+offSell0.substring(3,5);
				if(Integer.valueOf(offSell)>=Integer.valueOf(nowDate)) {
					returnValue = true;
				}
			}		   
		return returnValue;		
	}
	
	public List<Pre_grade> getPre_grade(String subjectTeacher_id,String grade_id) {
		String sql = "SELECT a.*,b.video_date from eip.pre_grade a, grade b where a.grade_id = b.grade_seq";
		
		if (subjectTeacher_id != null && !subjectTeacher_id.isEmpty()) {
			sql += " and a.subjectTeacher_id ='"+subjectTeacher_id+"'";
		}
		
		if (grade_id != null && !grade_id.isEmpty()) {
			sql += " and a.grade_id ='"+grade_id+"'";
		}		
		
		List<Pre_grade> LPre_grade = jdbcTemplate.query(sql,(result, rowNum) -> new Pre_grade(
				result.getString("pre_grade_seq"),
				result.getString("subjectTeacher_id"), 
				result.getString("grade_id"),
				"",
				"",
				result.getString("video_date"),
				result.getString("grade_remark")
		));
		return LPre_grade;
	}
	
	public List<Pre_grade> getPre_grade2(String subject_id,String teacher_id) {
		String sql = "SELECT a.*,b.video_date"+
	                 " from eip.pre_grade a, grade b, eip.subjectTeacher c"+
	                 " where a.grade_id=b.grade_seq and a.subjectTeacher_id=c.subjectTeacher_seq and c.subject_id='"+subject_id+"' and c.teacher_id='"+teacher_id+"'";
		
		
		List<Pre_grade> LPre_grade = jdbcTemplate.query(sql,(result, rowNum) -> new Pre_grade(
				result.getString("pre_grade_seq"),
				result.getString("subjectTeacher_id"), 
				result.getString("grade_id"),
				"",
				"",
				result.getString("video_date"),
				result.getString("grade_remark")
		));
		return LPre_grade;
	}	

	public List<Register_comboSale_grade> getRegister_comboSale_grade(String Register_seq,String active,String Register_comboSale_id,String in_register_status) {
		String sql = "SELECT * from eip.Register_comboSale_grade a,eip.Register_comboSale b"+
	                 " where a.Register_comboSale_id=b.Register_comboSale_seq" ;
		if (Register_seq != null && !Register_seq.isEmpty()) {
			sql += " and b.Register_id ="+Register_seq;
		}
		if (active != null && !active.isEmpty()) {
			sql += " and a.active ="+active;
		}
		if (Register_comboSale_id != null && !Register_comboSale_id.isEmpty()) {
			sql += " and a.Register_comboSale_id ='"+Register_comboSale_id+"'";
		}
		if (in_register_status != null && !in_register_status.isEmpty()) {
			sql += " and a.register_status in "+in_register_status;
		}		
		
		List<Register_comboSale_grade> LRegister_comboSale_grade = jdbcTemplate.query(sql,(result, rowNum) -> new Register_comboSale_grade(
				result.getString("Register_comboSale_grade_seq"),
				result.getString("Register_comboSale_id"), 
				result.getString("grade_id"),
				result.getString("register_status"),
				result.getString("class_style"),
				result.getString("school_code"),
				"",
				result.getString("sitNo"),
				result.getString("active"),
				result.getString("updater"),
				result.getString("update_time")
		));
		return LRegister_comboSale_grade;
	}	
	
	public List<Student_degree> getStudent_degree(String student_no) {
		String sql = "SELECT * from eip.student_degree where student_no ='"+student_no+"'";

		List<Student_degree> LStudent_degree = jdbcTemplate.query(sql,(result, rowNum) -> new Student_degree(
				result.getString("student_degree_seq"),
				result.getString("student_no"), 
				result.getString("urg_name"),
				result.getString("urg_relation"), 
				result.getString("urg_line"), 
				result.getString("urg_email"), 
				result.getString("urg_mobile"), 
				result.getString("urg_tel"),
				result.getString("a1"),
				result.getString("a2"),
				result.getString("a3"),
				result.getString("a4"),
				result.getString("a5"),
				result.getString("a6"),
				result.getString("b1"),
				result.getString("b2"),
				result.getString("b3"),
				result.getString("b4"),
				result.getString("b5"),
				result.getString("c1"),
				result.getString("c2"),
				result.getString("c3"),
				result.getString("c4"),
				result.getString("c5"),
				result.getString("c6"),
				result.getString("c7"),
				result.getString("apply"),
				result.getString("target1"),
				result.getString("target2"),
				result.getString("schoolArea"),
				result.getString("student_degree"),
				result.getString("internation")
		));
		return LStudent_degree;
	}
	
	public Boolean Student_degreeSave(String student_no,Student_degree student_degree) {
		if(student_no!=null && !student_no.isEmpty()) {
			jdbcTemplate.update("DELETE from eip.student_degree where student_no=?", student_no);
		}	
		jdbcTemplate.update("Insert into eip.student_degree VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);", 
				student_no,
				student_degree.getUrg_name(),
				student_degree.getUrg_relation(),
				student_degree.getUrg_line(),
				student_degree.getUrg_email(),
				student_degree.getUrg_mobile(),
				student_degree.getUrg_tel(),
				student_degree.getA1(),student_degree.getA2(),student_degree.getA3(),student_degree.getA4(),student_degree.getA5(),student_degree.getA6(), 
				student_degree.getB1(),student_degree.getB2(),student_degree.getB3(),student_degree.getB4(),student_degree.getB5(),
				student_degree.getC1(),student_degree.getC2(),student_degree.getC3(),student_degree.getC4(),student_degree.getC5(),student_degree.getC6(),student_degree.getC7(),
				student_degree.getApply(),
				student_degree.getTarget1(),
				student_degree.getTarget2(),
				student_degree.getSchoolArea(),
				student_degree.getStudent_degree(),
				student_degree.getInternation()
		);
		return true;
	}
	
	public List<FreeClass> getFreeClass(String Register_comboSale_id) {
		String sql = "SELECT * from eip.FreeClass where Register_comboSale_id='"+Register_comboSale_id+"'";
				
		List<FreeClass> LFreeClass = jdbcTemplate.query(sql,(result, rowNum) -> new FreeClass(
				result.getString("freeClass_seq"),
				result.getString("freeChoice"), 
				result.getString("Register_comboSale_id")
		));
		return LFreeClass;
	}
	
	public Boolean EverAttend(String student_id,String Register_comboSale_grade_id,String class_th) {
		List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("",student_id,"","","","",Register_comboSale_grade_id,"(1)","","",class_th);
		if(LSignRecordHistory.size()>0) {
			return true;
		}else {
			return false;
		}		
	}
	
	public Boolean firstRealAttend(String student_id,String Register_comboSale_grade_id,String class_th) {

		List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("",student_id,"","","","",Register_comboSale_grade_id,"(1)","1","",class_th);
		if(LSignRecordHistory.size()>0) {
			return true;
		}else {
			return false;
		}		
	}
	
	public Boolean MockVideoSave(String student_id,String school_code,String grade_id,String register_mock_id,String updater,String mockVideo_id) {
		jdbcTemplate.update("Insert into eip.mockVideoHistory VALUES (default,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?);", 
				register_mock_id,
				mockVideo_id,
				1, //1:訂班
				student_id,
				school_code,
				grade_id,
				0, //0:預訂
				1, //1為最新紀錄
				"", //attend_date
				-99, //slot
				updater,
				"", //classroom
				"", //seat
				"" //comment
		);
		return true;
	}
	
	public Boolean MockVideoUpdate(String mockVideoHistory_seq,String school_code,String comment,String attend_date,String slot,String updater){
		List<MockVideoHistory> LMockVideoHistory = courseService.getMockVideoHistory("","","","","","","","","",mockVideoHistory_seq);
		//目前為取消狀態,須新增一筆		
		if(LMockVideoHistory.get(0).getRegister_status().equals("2")) {
			jdbcTemplate.update("UPDATE eip.mockVideoHistory set active=? where mockVideoHistory_seq=?",
					"0", //#1最新,0舊的
					mockVideoHistory_seq
			);
			
			jdbcTemplate.update("Insert into eip.mockVideoHistory VALUES (default,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?);", 
					LMockVideoHistory.get(0).getRegister_mock_id(),
					LMockVideoHistory.get(0).getMockVideo_id(),
					"1",  //register_status TINYINT, #訂班狀態 1:訂班,2:取消,3:保留
					LMockVideoHistory.get(0).getStudent_id(),
					school_code,
					LMockVideoHistory.get(0).getGrade_id(),
					"0",  //attend #0預定,1出席,-1缺席
					"1",  //active
					attend_date,   //attend_date
					slot,   //slot
					updater,
					"", //classroom
					"", //seat
					comment //comment
			);
		
		//目前為訂班,需更新上課時間	
		}else if(LMockVideoHistory.get(0).getRegister_status().equals("1")) {
			jdbcTemplate.update("UPDATE eip.mockVideoHistory set school_code=?,comment=?,attend_date=?,slot=?,updater=?,update_time=NOW(),attend=? where mockVideoHistory_seq=?",
					school_code, 
					comment,
					attend_date,
					slot,
					updater,
					"0", //attend 
					mockVideoHistory_seq
			);
		}	
		return true;
	}		
		

	public Boolean updateMockBaseBook(String student_seq,String mockBase_id,String updater,String mockDetail_id,String setDate,String slot,String school_code,String panelName,String limitName,String register_mock_seq) {
		jdbcTemplate.update("Update eip.mockBaseBook set active='0' where register_mock_seq=?", 
				register_mock_seq
		);		
		jdbcTemplate.update("Insert into eip.mockBaseBook VALUES (default,?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?,?);", 
				mockDetail_id,
				mockBase_id,
				student_seq,
				updater,
				"1", //active
				"1",  //attend
				setDate,
				slot,
				"", //comment
				"", //classroom
				"", //seat
				school_code,
				panelName,
				limitName,
				register_mock_seq
		);
		return true;
	}
	
	public Boolean updateCounselingBaseBook(String student_seq,String counselingBase_id,String updater,String setDate,String slot,String school_code,String limitName,String register_counseling_seq) {
		jdbcTemplate.update("Update eip.counselingBaseBook set active='0' where register_counseling_seq=?", 
				register_counseling_seq
		);		
		jdbcTemplate.update("Insert into eip.counselingBaseBook VALUES (default,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?);", 
				counselingBase_id,
				student_seq,
				updater,
				"1", //active
				"1",  //attend
				setDate,
				slot,
				"", //comment
				"", //classroom
				"", //seat
				school_code,
				limitName,
				register_counseling_seq
		);
		return true;
	}	
	
	
	public List<MockBaseBook2> getMockBaseBook(String student_id,String active,String mockDetail_id,String setDate,String school_code,String slot,String mockLimit_seq,String register_mock_seq,String mockBaseBook_seq,String mockBase_id) {
		String sql = "SELECT * from eip.mockBaseBook where 1=1";
		if (student_id != null && !student_id.isEmpty()) {sql += " and student_id ='"+student_id+"'";}	
		if (active != null && !active.isEmpty()) {sql += " and active ='"+active+"'";}
		if (mockDetail_id != null && !mockDetail_id.isEmpty()) {sql += " and mockDetail_id ='"+mockDetail_id+"'";}
		if (setDate != null && !setDate.isEmpty()) {sql += " and setDate ='"+setDate+"'";}
		if (school_code != null && !school_code.isEmpty()) {sql += " and school_code ='"+school_code+"'";}
		if (slot != null && !slot.isEmpty()) {sql += " and slot ='"+slot+"'";}
		if (mockLimit_seq != null && !mockLimit_seq.isEmpty()) {sql += " and mockLimit_seq ='"+mockLimit_seq+"'";}
		if (register_mock_seq != null && !register_mock_seq.isEmpty()) {sql += " and register_mock_seq ='"+register_mock_seq+"'";}
		if (mockBaseBook_seq != null && !mockBaseBook_seq.isEmpty()) {sql += " and mockBaseBook_seq ='"+mockBaseBook_seq+"'";}
		if (mockBase_id != null && !mockBase_id.isEmpty()) {sql += " and mockBase_id ='"+mockBase_id+"'";}
		
		sql +="order by mockBaseBook_seq desc";
		
		List<MockBaseBook2> LMockBaseBook2 = jdbcTemplate.query(sql,(result, rowNum) -> new MockBaseBook2(
				result.getString("mockBaseBook_seq"),
				result.getString("mockDetail_id"),
				result.getString("mockBase_id"), 
				result.getString("student_id"),
				result.getString("updater"),
				result.getString("update_time"),
				result.getString("active"),
				"",
				"",
				"",
				result.getString("attend"),
				"",
				"",
				result.getString("slot"),
				result.getString("comment"),
				result.getString("classroom"),
				result.getString("seat"),
				result.getString("school_code"),
				result.getString("panelName"),
				result.getString("mockLimit_seq"),
				"",
				result.getString("register_mock_seq")
		));

		for(int i=0;i<LMockBaseBook2.size();i++) {
			String slotName = "";
			if(LMockBaseBook2.get(i).getSlot()!=null && LMockBaseBook2.get(i).getSlot().equals("1")) {
				slotName = "(早)";
			}else if(LMockBaseBook2.get(i).getSlot()!=null && LMockBaseBook2.get(i).getSlot().equals("2")) {
				slotName = "(午)";
			}else if(LMockBaseBook2.get(i).getSlot()!=null && LMockBaseBook2.get(i).getSlot().equals("3")) {
				slotName = "(晚)";
			}
			
			//人數限制
			if(LMockBaseBook2.get(i).getMockLimit_seq()!=null && !LMockBaseBook2.get(i).getMockLimit_seq().isEmpty()) {
				slotName = "";
    			List<MockLimit> LMockLimit = admService.getMockLimit("",LMockBaseBook2.get(i).getMockLimit_seq());
				if(LMockLimit.size()>0) {
					LMockBaseBook2.get(i).setLimitName(LMockLimit.get(0).getFromx()+"~"+LMockLimit.get(0).getTox());
				}	
			}	
			
			
			List<MockBase> LMockBase = admService.getMockBase("","",LMockBaseBook2.get(i).getMockBase_id(),"");			
			if(LMockBase.size()>0) {
				LMockBaseBook2.get(i).setSetDate(LMockBase.get(0).getSetDate());
				LMockBaseBook2.get(i).setMockSubjectName(LMockBase.get(0).getCategoryName());
				LMockBaseBook2.get(i).setMockParaName((LMockBase.get(0).getRound().isEmpty()?"":"第"+LMockBase.get(0).getRound()+"回")+slotName+(LMockBaseBook2.get(i).getMockLimit_seq().isEmpty()?"":LMockBaseBook2.get(i).getLimitName()+"<br>真人評測"));
			}
			
			List<Student> LStudent = getStudent(LMockBaseBook2.get(i).getStudent_id(),"","","","","","","","","");
			if(LStudent.size()>0) {
				LMockBaseBook2.get(i).setCh_name(LStudent.get(0).getCh_name());
				LMockBaseBook2.get(i).setStudent_no(LStudent.get(0).getStudent_no());
			}

		}		
		return LMockBaseBook2;
	}
	
	public List<CounselingBaseBook> getCounselingBaseBook(String student_id,String active,String setDate,String school_code,String slot,String counselingLimit_seq,String register_counseling_seq,String counselingBaseBook_seq,String counselingBase_id,String attend) {

		String sql = "SELECT a.*,c.counseling_name from eip.CounselingBaseBook a, eip.register_counseling b, eip.counseling c where a.register_counseling_seq=b.register_counseling_seq and b.counseling_id=c.counseling_seq";
		if (student_id != null && !student_id.isEmpty()) {sql += " and a.student_id ='"+student_id+"'";}	
		if (active != null && !active.isEmpty()) {sql += " and a.active ='"+active+"'";}
		if (setDate != null && !setDate.isEmpty()) {sql += " and a.setDate ='"+setDate+"'";}
		if (school_code != null && !school_code.isEmpty()) {sql += " and a.school_code ='"+school_code+"'";}
		if (slot != null && !slot.isEmpty()) {sql += " and a.slot ='"+slot+"'";}
		if (counselingLimit_seq != null && !counselingLimit_seq.isEmpty()) {sql += " and a.counselingLimit_seq ='"+counselingLimit_seq+"'";}
		if (register_counseling_seq != null && !register_counseling_seq.isEmpty()) {sql += " and a.register_counseling_seq ='"+register_counseling_seq+"'";}
		if (counselingBaseBook_seq != null && !counselingBaseBook_seq.isEmpty()) {sql += " and a.counselingBaseBook_seq ='"+counselingBaseBook_seq+"'";}
		if (counselingBase_id != null && !counselingBase_id.isEmpty()) {sql += " and a.counselingBase_id ='"+counselingBase_id+"'";}
		if (attend != null && !attend.isEmpty()) {sql += " and a.attend ='"+attend+"'";}
		sql +=" order by a.counselingBaseBook_seq desc";

		List<CounselingBaseBook> LCounselingBaseBook = jdbcTemplate.query(sql,(result, rowNum) -> new CounselingBaseBook(
				result.getString("counselingBaseBook_seq"),
				result.getString("counselingBase_id"),
				result.getString("student_id"), 
				result.getString("updater"),
				result.getString("update_time"),
				result.getString("active"),
				result.getString("attend"),
				result.getString("setDate"),
				result.getString("slot"),
				result.getString("comment"),
				result.getString("classroom"),
				result.getString("seat"),
				result.getString("school_code"),
				result.getString("counselingLimit_seq"),
				result.getString("register_counseling_seq"),
				"",
				"",
				"",
				result.getString("counseling_name")
		));

		for(int i=0;i<LCounselingBaseBook.size();i++) {
			String slotName = "";
			if(LCounselingBaseBook.get(i).getSlot()!=null && LCounselingBaseBook.get(i).getSlot().equals("1")) {
				slotName = "(早)";
			}else if(LCounselingBaseBook.get(i).getSlot()!=null && LCounselingBaseBook.get(i).getSlot().equals("2")) {
				slotName = "(午)";
			}else if(LCounselingBaseBook.get(i).getSlot()!=null && LCounselingBaseBook.get(i).getSlot().equals("3")) {
				slotName = "(晚)";
			}
			
			//人數限制
			if(LCounselingBaseBook.get(i).getCounselingLimit_seq()!=null && !LCounselingBaseBook.get(i).getCounselingLimit_seq().isEmpty()) {
				List<CounselingLimit2> LCounselingLimit2 = admService.getCounselingLimit2("",LCounselingBaseBook.get(i).getCounselingLimit_seq());
				if(LCounselingLimit2.size()>0) {
					LCounselingBaseBook.get(i).setLimitName(LCounselingLimit2.get(0).getFrom1()+"~"+LCounselingLimit2.get(0).getTo1());
				}	
			}	
			
			List<Student> LStudent = getStudent(LCounselingBaseBook.get(i).getStudent_id(),"","","","","","","","","");
			if(LStudent.size()>0) {
				LCounselingBaseBook.get(i).setCh_name(LStudent.get(0).getCh_name());
				LCounselingBaseBook.get(i).setStudent_no(LStudent.get(0).getStudent_no());
			}

		}		
		return LCounselingBaseBook;
	}	
		
	
	public Boolean cancelMockBook(String student_id,String mockBaseBook_seq,String updater){
		
		jdbcTemplate.update("UPDATE eip.mockBaseBook set active=? where student_id=? and mockBaseBook_seq=?",
				"0", //#1最新,0舊的
				student_id,
				mockBaseBook_seq
		);
		
		List<MockBaseBook2> LMockBaseBook2 = getMockBaseBook("","","","","","","","",mockBaseBook_seq,"");
		if(LMockBaseBook2.size()>0) {
			jdbcTemplate.update("Insert into eip.mockBaseBook VALUES (default,?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?,?);", 
					LMockBaseBook2.get(0).getMockDetail_id(),
					LMockBaseBook2.get(0).getMockBase_id(),
					LMockBaseBook2.get(0).getStudent_id(),
					updater,
					"1", //active
					"-1",  //#1預定,2出席,3缺席,-1取消
					"",
					"",
					"", //comment
					"", //classroom
					"", //seat
					"", //school_code,
					"", //panelName,
					"", //limitName,
					LMockBaseBook2.get(0).getRegister_mock_seq()
			);
		}	
		return true;
	}
	
	public Boolean cancelCounselingBook(String student_id,String counselingBaseBook_seq,String updater){
		
		jdbcTemplate.update("UPDATE eip.counselingBaseBook set active=? where student_id=? and counselingBaseBook_seq=?",
				"0", //#1最新,0舊的
				student_id,
				counselingBaseBook_seq
		);
		
		List<CounselingBaseBook> LCounselingBaseBook = getCounselingBaseBook("","","","","","","",counselingBaseBook_seq,"","");
		if(LCounselingBaseBook.size()>0) {
			jdbcTemplate.update("Insert into eip.counselingBaseBook VALUES (default,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?);", 
					LCounselingBaseBook.get(0).getCounselingBase_id(),
					LCounselingBaseBook.get(0).getStudent_id(),
					updater,
					"1", //active
					"-1",  //attend #1預定,2出席,3缺席,-1取消
					"",
					"",
					"", //comment
					"", //classroom
					"", //seat
					"", //school_code,
					"", //limitName,
					LCounselingBaseBook.get(0).getRegister_counseling_seq()
			);
		}	
		return true;
	}
	
	public Boolean cancelMockVideo(String student_id,String mockVideoHistory_seq,String creater) {
		jdbcTemplate.update("UPDATE eip.mockVideoHistory set active=? where student_id=? and mockVideoHistory_seq=?",
				"0", //#1最新,0舊的
				student_id,
				mockVideoHistory_seq
		);
		
		List<MockVideoHistory> LMockVideoHistory = courseService.getMockVideoHistory("","","","",student_id,"","","","",mockVideoHistory_seq);
		if(LMockVideoHistory.size()>0) {
			jdbcTemplate.update("Insert into eip.mockVideoHistory VALUES (default,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?);", 
					LMockVideoHistory.get(0).getRegister_mock_id(),
					LMockVideoHistory.get(0).getMockVideo_id(),
					"2",  //register_status TINYINT, #訂班狀態 1:訂班,2:取消,3:保留
					LMockVideoHistory.get(0).getStudent_id(),
					LMockVideoHistory.get(0).getSchool_code(),
					LMockVideoHistory.get(0).getGrade_id(),
					"0",  //attend #0預定,1出席,-1缺席
					"1",  //active
					"",   //attend_date
					-99,   //slot
					creater,
					"", //classroom
					"", //seat
					"" //comment
			);
		}	
		return true;		
	}
}
