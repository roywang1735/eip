package com.wordgod.eip.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.wordgod.eip.Model.*;
import com.wordgod.eip.Service.AccountService;
import com.wordgod.eip.Service.CourseService;
import com.wordgod.eip.Service.ManagerService;
import com.wordgod.eip.Service.SalesService;

import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class ManagerController {
    @Autowired
    ManagerService managerService;
    @Autowired
    AccountService accountService;
    @Autowired
    CourseService courseService;
    @Autowired
    SalesService salesService;
	@Autowired
	private JdbcTemplate jdbcTemplate;    
    
    @RequestMapping(value="/Manager/AccountLog")
    public String AccountLog(HttpSession session,HttpServletRequest request) {

		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
        return "/Manager/AccountLog";
    }  
    
    @RequestMapping(value="/Manager/GradeSetLog")
    public String GradeSetLog() {
        return "/Manager/GradeSetLog";
    }
    
    @RequestMapping(value="/Manager/comboSaleLog")
    public String comboSaleLog() {
        return "/Manager/comboSaleLog";
    } 
    
    @RequestMapping(value="/Manager/SubjectLog")
    public String SubjectLog() {
        return "/Manager/SubjectLog";
    }
    
    @RequestMapping(value="/Manager/MockLog")
    public String MockLog() {
        return "/Manager/MockLog";
    }     
  
    
    @RequestMapping("/Manager/getApplicationLog")
    @ResponseBody
    public List<ApplicationLog> getApplicationLog(HttpServletRequest request){
        return managerService.getApplicationLog(request.getParameter("fun1"),"","");
    }    

    
    @RequestMapping(value="/Manager/CourseApproveFlow")
    public String CourseApproveFlow(Model model,HttpSession session,HttpServletRequest request) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
		getApproveNumber(model); //取得待簽核數量
		
        return "/Manager/CourseApproveFlow";
    }
    
    public Boolean getApproveNumber(Model model) {
    	int CourseApproveFlow_No = jdbcTemplate.queryForObject("select count(*) from eip.grade where haveRead = 0", Integer.class); 	
    	model.addAttribute("CourseApproveFlow_No",CourseApproveFlow_No);
    	int ComboSaleFlow_No = jdbcTemplate.queryForObject("select count(*) from eip.comboSale where haveRead = 0", Integer.class); 	
    	model.addAttribute("ComboSaleFlow_No",ComboSaleFlow_No);
    	int PromoFlow_No = jdbcTemplate.queryForObject("select count(*) from eip.classPromotion where approve = 0", Integer.class); 	
    	model.addAttribute("PromoFlow_No",PromoFlow_No);
    	int SpecialCase_No = jdbcTemplate.queryForObject("select count(*) from eip.studentExperience where experience_id = 4 and haveRead = 0", Integer.class); 	
    	model.addAttribute("SpecialCase_No",SpecialCase_No);    	
    	return true;
    	
    }
 
    @RequestMapping(value="/Manager/ComboSaleFlow")
    public String ComboSaleFlow(Model model) {
		getApproveNumber(model); //取得待簽核數量		
        return "/Manager/ComboSaleFlow";
    }
    
    @RequestMapping(value="/Manager/PromoFlow")
    public String PromoFlow(Model model,HttpSession session,HttpServletRequest request) {	
		getApproveNumber(model); //取得待簽核數量		
        return "/Manager/PromoFlow";
    }
    
    @RequestMapping(value="/Manager/SpecialCase")
    public String SpecialCase(Model model,HttpSession session,HttpServletRequest request) {		
		getApproveNumber(model); //取得待簽核數量		
        return "/Manager/SpecialCase";
    }     
    
    @RequestMapping("/Manager/orderChange")
    public String orderChange(Model model,HttpServletRequest request,HttpSession session) {

		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);    	
        return "/Manager/orderChange";
    } 
 
    @RequestMapping("/Manager/openReceipt")
    public String openReceipt(Model model,HttpServletRequest request) {
    	
    	List<StudentPayRecord> LStudentPayRecord = salesService.getStudentPayRecord(request.getParameter("register_id"));
 		String returnStr = "<div class='css-table'>";
 		for(int x=0;x<LStudentPayRecord.size();x++) {
 		  if(!LStudentPayRecord.get(x).getPayMoney().equals("0")) {	
 			returnStr += "<div class='tr' style='font-size:small'>";
 			returnStr +=    "<div class='td2' style='padding:5px;width:180px'>"+LStudentPayRecord.get(x).getSchool_code()+LStudentPayRecord.get(x).getReceiptNo()+"</div>";
 			returnStr +=    "<div class='td2' style='padding:5px;width:100px'>"+LStudentPayRecord.get(x).getPayMoney()+"</div>"; 
 			returnStr +=    "<div class='td2' style='padding:5px;width:190px'>"+LStudentPayRecord.get(x).getPayDate()+"</div>";  
 			returnStr +=    "<div class='td2' style='padding:5px;width:100px'>"+LStudentPayRecord.get(x).getTakePerson()+"</div>"; 
 			returnStr +=    "<div class='td2' style='padding:5px;width:100px;text-align:center'>"+LStudentPayRecord.get(x).getPayStyle()+"</div>";
 			returnStr +=    "<div class='td2' style='padding:5px;width:70px;text-align:center'><img src='/images/print.png' height='12px'/></div>";
 			returnStr += "</div>";
 		  }
 		}
 		returnStr += "</div>";
 		model.addAttribute("returnStr", returnStr);	    
        return "/Manager/openReceipt";
    }     
 
    @RequestMapping(value="/Manager/costShare")
    public String costShare(Model model,Principal principal) {
    	
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
			
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		String school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("school_code",school_code);	    	
        return "/Manager/costShare";
    } 
    
    @RequestMapping("/Manager/subjectCostShare")
    public String subjectCostShare(Model model,HttpServletRequest request) {
		//班內
		List<Subject_R> LSubject_R1 = courseService.getSubjectWithR_original("-1","1");
		Subject_R subject_R1 = new Subject_R();
		if(LSubject_R1.size()>0) {
			subject_R1 = LSubject_R1.get(0);
		}	
		model.addAttribute("hrPrice_R1",subject_R1.getHrPrice_R());
		model.addAttribute("counselingPrice_R1",subject_R1.getCounselingPrice_R());
		model.addAttribute("lagnappePrice_R1",subject_R1.getLagnappePrice_R());
		model.addAttribute("handoutPrice_R1",subject_R1.getHandoutPrice_R());
		model.addAttribute("homeworkPrice_R1",subject_R1.getHomeworkPrice_R());
		model.addAttribute("mockPrice_R1",subject_R1.getMockPrice_R());
	
		//線上
		List<Subject_R> LSubject_R3 = courseService.getSubjectWithR_original("-1","3");	
		Subject_R subject_R3 = new Subject_R();
		if(LSubject_R3.size()>0) {
			subject_R3 = LSubject_R3.get(0);
		}		
		model.addAttribute("hrPrice_R3",subject_R3.getHrPrice_R());
		model.addAttribute("counselingPrice_R3",subject_R3.getCounselingPrice_R());
		model.addAttribute("lagnappePrice_R3",subject_R3.getLagnappePrice_R());
		model.addAttribute("handoutPrice_R3",subject_R3.getHandoutPrice_R());
		model.addAttribute("homeworkPrice_R3",subject_R3.getHomeworkPrice_R());
		model.addAttribute("mockPrice_R3",subject_R3.getMockPrice_R());  
		model.addAttribute("message",request.getParameter("message"));
        return "/Manager/subjectCostShare";
    }
    
        
    @RequestMapping(value="/Manager/subjectCostShareUpdate",method=RequestMethod.POST)
    public String subjectCostShareUpdate(HttpServletRequest request,Model model) {
    	String hrPrice_R1 = request.getParameter("hrPrice_R1");
    	String counselingPrice_R1 = request.getParameter("counselingPrice_R1");
    	String lagnappePrice_R1 = request.getParameter("lagnappePrice_R1");
    	String handoutPrice_R1 = request.getParameter("handoutPrice_R1");
    	String homeworkPrice_R1 = request.getParameter("homeworkPrice_R1");
    	String mockPrice_R1 = request.getParameter("mockPrice_R1");
 
    	String hrPrice_R3 = request.getParameter("hrPrice_R3");
    	String counselingPrice_R3 = request.getParameter("counselingPrice_R3");
    	String lagnappePrice_R3 = request.getParameter("lagnappePrice_R3");
    	String handoutPrice_R3 = request.getParameter("handoutPrice_R3");
    	String homeworkPrice_R3 = request.getParameter("homeworkPrice_R3");
    	String mockPrice_R3 = request.getParameter("mockPrice_R3");  
    	
    	//1. 全刪除
    	jdbcTemplate.update("delete from eip.subject_R");
    	//2.塞入資料
	    	//a.預設值
			jdbcTemplate.update("INSERT INTO eip.subject_R VALUES (default,?,?,?,?,?,?,?,?);",
					-1,
					"1",//#1實體,2Video,3線上	
					hrPrice_R1,
					counselingPrice_R1,
					lagnappePrice_R1,
					handoutPrice_R1,
					homeworkPrice_R1,
					mockPrice_R1
			);
    		jdbcTemplate.update("INSERT INTO eip.subject_R VALUES (default,?,?,?,?,?,?,?,?);",
    				-1,
					"3",//#1實體,2Video,3線上	
					hrPrice_R3,
					counselingPrice_R3,
					lagnappePrice_R3,
					handoutPrice_R3,
					homeworkPrice_R3,
					mockPrice_R3
    		);			
	    	
		    //b.各科目實際值	
	    	List<Subject> LSubject = courseService.getSubject("","","","","","0");
	    	for(int i=0;i<LSubject.size();i++) {   		
	    		jdbcTemplate.update("INSERT INTO eip.subject_R VALUES (default,?,?,?,?,?,?,?,?);",
	    				LSubject.get(i).getSubject_seq(),
						"1",//#1實體,2Video,3線上	
						hrPrice_R1,
						counselingPrice_R1,
						lagnappePrice_R1,
						handoutPrice_R1,
						homeworkPrice_R1,
						mockPrice_R1
	    		); 
	    		
	    		jdbcTemplate.update("INSERT INTO eip.subject_R VALUES (default,?,?,?,?,?,?,?,?);",
	    				LSubject.get(i).getSubject_seq(),
						"3",//#1實體,2Video,3線上	
						hrPrice_R3,
						counselingPrice_R3,
						lagnappePrice_R3,
						handoutPrice_R3,
						homeworkPrice_R3,
						mockPrice_R3
	    		);     		
	    	}

    	return "redirect:/Manager/subjectCostShare?message=Save Successfully";
    }

	@RequestMapping("/Manager/openGradeStudentShare")
	public String openGradeStudentShare(Model model,HttpServletRequest request){	
		model.addAttribute("grade_seq",request.getParameter("grade_seq"));
		model.addAttribute("school_code",request.getParameter("school_code"));
		model.addAttribute("gradeName",request.getParameter("gradeName"));		
		return "/Manager/openGradeStudentShare";
	}
	
    @RequestMapping(value="/Manager/costShareReport")
    public String costShareReport(Model model,Principal principal) {
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
			
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		String school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("school_code",school_code);	
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy 年 MM 月");
		model.addAttribute("YearMonth",dateFormat.format(new Date()));
		
        return "/Manager/costShareReport";
    } 
    
    @RequestMapping(value="/Manager/costShareReportMonth")
    public String costShareReportMonth(Model model,@Value("${ExcelReportPath}") String ExcelReportPath) {

    	String fileName = "";
    	File[] files = new File(ExcelReportPath).listFiles();
    	if(files!=null) {
	    	for (File file : files) {
	    	    if (file.isFile()) {
	    	    	fileName+= "<a href='/images/excel/report/"+file.getName()+"' target='_self' style='text-decoration:underline;font-weight:bold;color:blue'>"+file.getName()+"</a><br>";
	    	    }
	    	}
    	}  	
    	model.addAttribute("fileName",fileName);
        return "/Manager/costShareReportMonth";
    } 
    
    @RequestMapping("/Manager/getSpecialCase")
    @ResponseBody
    public List<StudentExperience> getSpecialCase(Model model,HttpServletRequest request) {
		String student_seq = request.getParameter("student_seq");
		String haveRead = request.getParameter("haveRead");
		List<StudentExperience> LStudentExperience = salesService.getStudentExperience(student_seq,"4","",haveRead);

        return LStudentExperience;
    }
    
    @RequestMapping("/Manager/openSpecialCase")
    public String openSpecialCase(Model model,HttpServletRequest request) {
		String studentExperience_seq = request.getParameter("studentExperience_seq");
		if(request.getParameter("haveRead")!=null && request.getParameter("haveRead").equals("1") && studentExperience_seq!=null) {
			courseService.updateSpecialCaseHaveRead(studentExperience_seq);
		}		
		List<StudentExperience> LStudentExperience = salesService.getStudentExperience("","4",studentExperience_seq,"");
		String experience_contentStr = "";
		for(int i=0;i<LStudentExperience.size();i++) {
			String tmp = LStudentExperience.get(i).getExperience_content().replaceAll("\n", "<br />");
			experience_contentStr += 
			"<div class='tr' style='font-size:small'>"+		
				"<div class='td2' style='width:500px;height:70px;border:1px #dddddd solid'><div style='letter-spacing:0px'>"+tmp+"</div></div>"+
		    "</div>";
		}

		model.addAttribute("experience_contentStr",experience_contentStr);		

        return "/Manager/openSpecialCase";
    }    
    
    
}