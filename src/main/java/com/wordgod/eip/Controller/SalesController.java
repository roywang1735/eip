package com.wordgod.eip.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordgod.eip.Model.*;
import com.wordgod.eip.Service.AccountService;
import com.wordgod.eip.Service.AdmService;
import com.wordgod.eip.Service.CourseSaleService;
import com.wordgod.eip.Service.CourseService;
import com.wordgod.eip.Service.MarketingService;
import com.wordgod.eip.Service.SalesService;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class SalesController {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	CourseService courseService;
	@Autowired
	AccountService accountService;	
	@Autowired
	SalesService salesService;
	@Autowired
	CourseSaleService courseSaleService;		
	@Autowired
	AdmService admService;
	@Autowired
	MarketingService marketingService;	
	
	
    @RequestMapping("/Sales/CurrentCourse")
    public String CurrentCourse(Model model,HttpServletRequest request,Principal principal,HttpSession session) {

		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);		
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	

		String school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("school_code",school_code);
		model.addAttribute("category_id","2"); //????????????????????????
		
		String pop = request.getParameter("pop");
		if(pop!=null && pop.equals("1")) {
			model.addAttribute("pop","1");
		}else {
			model.addAttribute("pop","0");
		}		
        return "/Sales/CurrentCourse";
    }
    
	@RequestMapping("/Sales/Course")
    public String Course(Model model,Principal principal) {
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
			
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);	
		
		String school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("school_code",school_code);			
	    	
        return "/Sales/Course";
    }
	
	@RequestMapping("/Course/gradePeople")
    public String gradePeople(Model model,Principal principal) {
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
			
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);	
		
		String school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("school_code",school_code);			
	    	
        return "/Course/gradePeople";
    }	
    
    @RequestMapping("/Sales/SingleSale")
    public String SingleSale(Model model) {	    	
        return "/Sales/SingleSale";
    }  
    
    @RequestMapping("/Sales/Exchange")
    public String Exchange(Model model,HttpServletRequest request) {
    	String isBook = "false";
    	String Register_comboSale_seq =  request.getParameter("Register_comboSale_seq");
    	if(Register_comboSale_seq!=null && !Register_comboSale_seq.isEmpty()) {		  	
    		List<Register_comboSale_grade> LRegister_comboSale_grade = courseService.getRegister_comboSale_grade(Register_comboSale_seq,"","","");
		  	for(int a=0;a<LRegister_comboSale_grade.size();a++) {
		  		if(LRegister_comboSale_grade.get(a).getRegister_status()!=null && !LRegister_comboSale_grade.get(a).getRegister_status().equals("2")) {
		  			isBook = "true";
		  		}
		  	}
		  	List<Register_comboSale> LRegister_comboSale_child = salesService.getComboSaleByRegister("","","","","","",false,false,Register_comboSale_seq);
    		if(LRegister_comboSale_child.size()>0) {
			  	List<Register_comboSale_grade> LRegister_comboSale_grade_child = courseService.getRegister_comboSale_grade(LRegister_comboSale_child.get(0).getRegister_comboSale_seq(),"","","");
			  	for(int a=0;a<LRegister_comboSale_grade_child.size();a++) {
			  		if(LRegister_comboSale_grade_child.get(a).getRegister_status()!=null && !LRegister_comboSale_grade_child.get(a).getRegister_status().equals("2")) {
			  			isBook = "true";
			  		}
			  	}
    		}  	
    	} 	
    	model.addAttribute("isBook",isBook);
    	model.addAttribute("subject_id", request.getParameter("subject_id"));
    	model.addAttribute("old_Id", request.getParameter("old_Id"));
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	
		//???????????????
		if(request.getParameter("Register_seq")!=null && !request.getParameter("Register_seq").isEmpty()) {
			model.addAttribute("addMoney","Y");
		}
        return "/Sales/Exchange";
    } 
    
    
    @RequestMapping("/Sales/Exchange2")
    public String Exchange2(Model model,HttpServletRequest request) {
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	
		
		model.addAttribute("comboSale_id",request.getParameter("comboSale_id"));
		    String subjectStr="";
			if(request.getParameter("Register_seq")!=null && !request.getParameter("Register_seq").isEmpty()) {
			    List<Register_comboSale> LRegister_comboSale = salesService.getComboSaleByRegister("",request.getParameter("Register_seq"),"","",request.getParameter("comboSale_id"),"1",false,false,"");
			    // subject-Name@subject-Price^subject-Seq@HrPrice_R@CounselingPrice_R@LagnappePrice_R@HandoutPrice_R@HomeworkPrice_R@MockPrice_R
			    for(int i=0;i<LRegister_comboSale.size();i++) {
			    	Subject subject = courseService.getSubjectWithR("",LRegister_comboSale.get(i).getSubject_id(),"","","1").get(0);//?????????????????? 
			    	String tmp = subject.getName()+"@"+subject.getPrice()+"@"+LRegister_comboSale.get(i).getRegister_comboSale_seq()+"^"+subject.getSubject_seq()+"@"+subject.getHrPrice_R()+"@"+subject.getCounselingPrice_R()+"@"+subject.getLagnappePrice_R()+"@"+subject.getHandoutPrice_R()+"@"+subject.getHomeworkPrice_R()+"@"+subject.getMockPrice_R(); 
			    	//???????????????
			    	List<Register_comboSale_grade> LRegister_comboSale_grade = salesService.getRegister_comboSale_grade("","1",LRegister_comboSale.get(i).getRegister_comboSale_seq(),"(1,3)");
			    	String disabled="";
			    	if(LRegister_comboSale_grade.size()>0) {
			    		disabled="disabled";
			    	}
			    	subjectStr +="<div style='font-weight:bold;vertical-align:bottom'><input type='checkbox' class='ori_subjectId' name='ori_subjectId' value='"+tmp+"' style='zoom:1.2' "+disabled+">"+LRegister_comboSale.get(i).getSubject_name()+"</div>";
			    }
			    
			    
			//???????????????Register_seq    
			}else {				
				List<ComboSale_subject> LComboSale_subject = courseSaleService.getComboSale_subject(request.getParameter("comboSale_id"),"");
				for(int i=0;i<LComboSale_subject.size();i++) {
					String tmp = LComboSale_subject.get(i).getSubject_name()+"@"+LComboSale_subject.get(i).getSubject_price()+"@"+LComboSale_subject.get(i).getComboSale_id()+"^"+LComboSale_subject.get(i).getSubject_id()+"@"+LComboSale_subject.get(i).getHrPrice_R()+"@"+LComboSale_subject.get(i).getCounselingPrice_R()+"@"+LComboSale_subject.get(i).getLagnappePrice_R()+"@"+LComboSale_subject.get(i).getHandoutPrice_R()+"@"+LComboSale_subject.get(i).getHomeworkPrice_R()+"@"+LComboSale_subject.get(i).getMockPrice_R();
					subjectStr +="<div style='font-weight:bold;vertical-align:bottom'><input type='checkbox' class='ori_subjectId' name='ori_subjectId' value='"+tmp+"' style='zoom:1.2'>"+LComboSale_subject.get(i).getSubject_name()+"</div>";
				}
				model.addAttribute("newReg","1");
			}
			
			model.addAttribute("subjectStr",subjectStr);
	    return "/Sales/Exchange2";
    }			
    
    @RequestMapping("/Sales/Student")
    public String Student(Model model,HttpSession session,HttpServletRequest request) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
			
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
	    	
        return "/Sales/Student";
    }  
    
    @RequestMapping("/Sales/getStudent")
    @ResponseBody
    public List<Student> getStudent(HttpServletRequest request){
    	List<Student> LStudent = salesService.getStudent(
    			"",
        		request.getParameter("ch_name"),
        		request.getParameter("en_name"),
        		request.getParameter("student_no"),
        		request.getParameter("idn"),
        		request.getParameter("mobile"),
        		request.getParameter("email"),
        		request.getParameter("create_time"),
        		request.getParameter("degree"),
        		request.getParameter("cowork")
        );
	
    	for(int i=0;i<LStudent.size();i++) {
    		if(LStudent.get(i).getRemark().length()>200) {
    			LStudent.get(i).setRemark(LStudent.get(i).getRemark().substring(0,Integer.valueOf(request.getParameter("remark_limit")))+"<i>. . . . . . . . . . . . . . . . more</i>");
    		}
    	}
        return  LStudent;
    } 
    
    @RequestMapping("/Sales/getStudentRecord")
    @ResponseBody
    public List<Student> getStudentRecord(HttpServletRequest request){
    	List<Student> LStudent = salesService.getStudentRecord(
    			request.getParameter("student_seq")
        );
        return  LStudent;
    }     
    
    @RequestMapping("/Sales/StudentProfile")
    public String StudentProfile(Model model,HttpServletRequest request,Principal principal,HttpSession session,@Value("${UploadPath}") String UploadPath) {
    	if(request.getParameter("message")!=null && request.getParameter("message").equals("1")) {
    		model.addAttribute("message","????????????!");
    	}
    	if(request.getParameter("pop")!=null && request.getParameter("pop").equals("1")) {
    		model.addAttribute("pop","1");
    	}
    	List<Student> LStudent = new ArrayList<Student>();
    	List<Student_degree> LStudent_degree = new ArrayList<Student_degree>();
		List<School> LSchool = accountService.getSchool("","");
		model.addAttribute("LSchool", LSchool);
		//??????????????????
		String SchoolCode = accountService.getAccountByID("",principal.getName()).getSchool_code();
		model.addAttribute("principalSchool",SchoolCode);
		String student_seq = request.getParameter("student_seq");
		String ch_name = "";
		String student_no = "";
		String degree = "1";
		String internation = "0";
  		String degree_str = 
	  			"<input type='radio' name='degree' value='1' onclick='showVisibility()' checked>??? ??? ???&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
	  			"<input type='radio' name='degree' value='2' onclick='showVisibility()'>??? ??? ???";       		  
 	  		
    	String internation_str = 
    			"&nbsp;&nbsp;&nbsp;&nbsp;"+		 
	  			"<input type='radio' name='internation' value='1'>???&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
	  			"<input type='radio' name='internation' value='0' checked>???";       		  
    	
  		if(student_seq!=null && !student_seq.isEmpty()) {
    		//????????????
    		if(student_seq.equals("-1")) {     		
            	Student student = new Student();
            	Student_degree student_degree = new Student_degree();
            	student.setCreater(principal.getName());
            	student.setPasswd("888888");
            	String schoolCode = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
            	student.setSchool_code(schoolCode);
            	student.setSchool_code(accountService.getAccountByID("",principal.getName()).getSchool());
        		model.addAttribute("student", student);
        		model.addAttribute("student_degree", student_degree);
        		model.addAttribute("newId","Y");
        		model.addAttribute("degree_str", degree_str);
        		model.addAttribute("internation_str", internation_str);
        		return "/Sales/StudentProfile";
        	//???????????????
    		}else {
	    		LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
	    		if(LStudent.size()>0) {
	    			degree = LStudent.get(0).getDegree();
		    		model.addAttribute("student_seq",student_seq);
		    		ch_name = LStudent.get(0).getCh_name();
		    		model.addAttribute("ch_name",ch_name);
	    			student_no = LStudent.get(0).getStudent_no();
		    		model.addAttribute("student_no",student_no);	    		
	    		}       		
        	}
    	}else{	
			return "/Sales/Student"; 
		}
    	

    	LStudent_degree = salesService.getStudent_degree(student_no);
    	if(LStudent_degree.size()>0) {
		    model.addAttribute("student_degree",LStudent_degree.get(0));
		    internation = LStudent_degree.get(0).getInternation();
    	}else {
    		model.addAttribute("student_degree",new Student_degree());
    	}
 	
    	if(LStudent.size()>0) {
		    model.addAttribute("student",LStudent.get(0));

    		String student_no_final = student_no;
    		File dir = new File(UploadPath+"studentPhoto");
    		File[] files = dir.listFiles((d, name) -> name.startsWith(student_no_final));
    		if(files.length==0) {
    			model.addAttribute("fileName","nobody.png");
    		}else {
    			model.addAttribute("fileName","studentPhoto/"+files[0].getName());
    		}    		
    		

    	
    	 //?????????,????????? 
    	  if(degree.equals("2")) {
    		    degree_str = 
    			"<input type='radio' name='degree' value='1' onclick='showVisibility()'>??? ??? ???&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
    			"<input type='radio' name='degree' value='2' onclick='showVisibility()' checked>??? ??? ???";    		      		  
    	  }
    	  model.addAttribute("degree_str",degree_str); 
    	  
     	 //????????? 
     	  if(internation.equals("1")) {
     		 internation_str =
     		    "&nbsp;&nbsp;&nbsp;&nbsp;"+		 
     			"<input type='radio' name='internation' value='1' checked>???&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
     			"<input type='radio' name='internation' value='0'>???";    		      		  
     	  }
     	  model.addAttribute("internation_str",internation_str);     	  
       }  
		  return "/Sales/StudentProfile";       
    }  
  
    @RequestMapping("/Sales/Rotation")
    public String Rotation(Model model,HttpServletRequest request,Principal principal,HttpSession session,@Value("${UploadPath}") String UploadPath) {
		String student_seq = request.getParameter("student_seq"); 		  
  		if(student_seq!=null && !student_seq.isEmpty()) {
  			List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
		    model.addAttribute("student_seq",student_seq);
		    model.addAttribute("ch_name",LStudent.get(0).getCh_name());	    
    	}else{	
			return "/Sales/Student"; 
		}
 
    	String rotationStr = "";
        String rotationStr_0 = "";
        String rotationStr_1 = "";   		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String currentDay = sdf.format(new Date());    		
        	List<ConsultRecord> LConsultRecord = salesService.getConsultRecord("",student_seq,"");       	
    		for(int i=0;i<LConsultRecord.size();i++) {
    			    String statusStr = "";
    			    String validDay = "";
    			    String tmpStr = "";
	    				if(LConsultRecord.get(i).getConsultCategory_id().equals("1")) {
	    					validDay = LConsultRecord.get(i).getOneDayValid().substring(6,10)+LConsultRecord.get(i).getOneDayValid().substring(0,2)+LConsultRecord.get(i).getOneDayValid().substring(3,5);
	    				    if(LConsultRecord.get(i).getAvaiable()!=null && LConsultRecord.get(i).getAvaiable().equals("0")) {
	    				        statusStr = "??????";
	    				    }else if(Integer.valueOf(validDay)>=Integer.valueOf(currentDay)) {
	    						statusStr = "?????????...";
	    					}else {
	    						statusStr = "????????????";
	    					}
	    					tmpStr = "<div class='td2' style='width:120px;border-bottom:1px #cccccc solid;text-align:center'>"+LConsultRecord.get(i).getOneDayValid()+"</div>";
	    				}else if(LConsultRecord.get(i).getConsultCategory_id().equals("2")) { 
	    					validDay = LConsultRecord.get(i).getValidDate().substring(6,10)+LConsultRecord.get(i).getValidDate().substring(0,2)+LConsultRecord.get(i).getValidDate().substring(3,5);
	    				    if(LConsultRecord.get(i).getAvaiable()!=null && LConsultRecord.get(i).getAvaiable().equals("0")) {
	    				        statusStr = "??????";
	    				    }else if(Integer.valueOf(validDay)>=Integer.valueOf(currentDay)) {
	    						statusStr = "?????????...";
	    					}else {
	    						statusStr = "????????????";
	    					}	    					
	    					tmpStr = "<div class='td2' style='width:120px;border-bottom:1px #cccccc solid;text-align:center'>"+LConsultRecord.get(i).getValidDate()+"</div>";
	    				}else if(LConsultRecord.get(i).getConsultCategory_id().equals("3")) {
	    					if(LConsultRecord.get(i).getCrossDate()!=null && !LConsultRecord.get(i).getCrossDate().isEmpty()) {
	    						validDay = LConsultRecord.get(i).getCrossDate().substring(6,10)+LConsultRecord.get(i).getCrossDate().substring(0,2)+LConsultRecord.get(i).getCrossDate().substring(3,5);
	    					}else {
	    						validDay ="";
	    					}
	    					if(LConsultRecord.get(i).getAvaiable()!=null && LConsultRecord.get(i).getAvaiable().equals("0")) {
	    				        statusStr = "??????";
	    				    }else if(!validDay.equals("") && Integer.valueOf(validDay)>=Integer.valueOf(currentDay)) {
	    						statusStr = "?????????...";
	    					}else {
	    						statusStr = "????????????";
	    					}	    					
	    					tmpStr = "<div class='td2' style='width:120px;border-bottom:1px #cccccc solid;text-align:center'>"+LConsultRecord.get(i).getCrossDate()+"</div>";
	    				}else if(LConsultRecord.get(i).getConsultCategory_id().equals("4")) {
	    					validDay = LConsultRecord.get(i).getLectureDate().substring(6,10)+LConsultRecord.get(i).getLectureDate().substring(0,2)+LConsultRecord.get(i).getLectureDate().substring(3,5);
	    					if(Integer.valueOf(validDay)>=Integer.valueOf(currentDay)) {
	    						statusStr = "?????????...";
	    					}else {
	    						statusStr = "????????????";
	    					}	    					
	    					tmpStr = "<div class='td2' style='width:120px;border-bottom:1px #cccccc solid;text-align:center'>"+LConsultRecord.get(i).getLectureDate()+"</div>";
	    				}else {
	    					tmpStr = "<div class='td2' style='width:120px;border-bottom:1px #cccccc solid;text-align:center'>-</div>";    					
	    				}
	    				
	    				if(statusStr.equals("?????????...")) {
		    				        rotationStr_0 +=
				    	    		"<div class='tr' style='background-color:#ffffff;font-size:small;vertical-align:middle;color:blue'>"+
				    	    			"<div class='td2' style='width:100px;border-bottom:1px #cccccc solid;text-align:center;padding:6px'>"+LConsultRecord.get(i).getEmployee_name()+"</div>"+
				    	        		"<div class='td2' style='width:130px;border-bottom:1px #cccccc solid;text-align:center;font-size:x-small'>"+LConsultRecord.get(i).getCreateDate()+"</div>";  				
				    				
				    				rotationStr_0 += tmpStr;			    				
					    				
				    				rotationStr_0 +=
										"<div class='td2' style='width:140px;border-bottom:1px #cccccc solid'>"+LConsultRecord.get(i).getConsultCategory_name()+"</div>"+
										"<div class='td2' style='width:100px;border-bottom:1px #cccccc solid;text-align:center'>"+statusStr+"</div>"+
										"<div class='td2' style='width:100px;border-bottom:1px #cccccc solid;text-align:center'>"+LConsultRecord.get(i).getEmployee_school()+"</div>"+
										"<div class='td2' style='width:120px;border-bottom:1px #cccccc solid'>"+LConsultRecord.get(i).getClassCategoryNameSel()+LConsultRecord.get(i).getCategory_id_text_1()+"</div>"+
										"<div class='td2' style='width:270px;border-bottom:1px #cccccc solid'>"+LConsultRecord.get(i).getContent()+"</div>"+    				    				
					    				"<div class='td2' style='color:#ffffff;text-align:center;'></div>"+	
				    				"</div>";
                        }else {
		    				        rotationStr_1 +=
				    	    		"<div class='tr' style='background-color:#ffffff;font-size:small;vertical-align:middle;color:#999999'>"+
				    	    			"<div class='td2' style='width:100px;border-bottom:1px #cccccc solid;text-align:center;padding:6px'>"+LConsultRecord.get(i).getEmployee_name()+"</div>"+
				    	        		"<div class='td2' style='width:130px;border-bottom:1px #cccccc solid;text-align:center;font-size:x-small'>"+LConsultRecord.get(i).getCreateDate()+"</div>";  				
				    				
				    				rotationStr_1 += tmpStr;			    				
					    				
				    				rotationStr_1 +=
										"<div class='td2' style='width:140px;border-bottom:1px #cccccc solid'>"+LConsultRecord.get(i).getConsultCategory_name()+"</div>"+
										"<div class='td2' style='width:100px;border-bottom:1px #cccccc solid;text-align:center'>"+statusStr+"</div>"+
										"<div class='td2' style='width:100px;border-bottom:1px #cccccc solid;text-align:center'>"+LConsultRecord.get(i).getEmployee_school()+"</div>"+
										"<div class='td2' style='width:120px;border-bottom:1px #cccccc solid'>"+LConsultRecord.get(i).getClassCategoryNameSel()+LConsultRecord.get(i).getCategory_id_text_1()+"</div>"+
										"<div class='td2' style='width:270px;border-bottom:1px #cccccc solid'>"+LConsultRecord.get(i).getContent()+"</div>"+    				    				
					    				"<div class='td2' style='color:#ffffff;text-align:center;'></div>"+	
				    				"</div>";
                        }		    					    				
    		}
    	rotationStr = rotationStr_0 + rotationStr_1;
    	model.addAttribute("rotationStr", rotationStr);  		  		     	  
		return "/Sales/Rotation";       
    }      

    @RequestMapping("/Sales/JL_History")
    public String JL_History(Model model,HttpServletRequest request) {
    	List<Student> LStudent = new ArrayList<Student>();
		String student_seq = request.getParameter("student_seq");		  
    	
  		if(student_seq!=null && !student_seq.isEmpty()) {			
	    		LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
	    		if(LStudent.size()>0) {
		    		model.addAttribute("student_seq",student_seq);   
		    		model.addAttribute("student",LStudent.get(0));
		    		model.addAttribute("ch_name",LStudent.get(0).getCh_name());	
	    		}       		
    	}else{	
			return "/Sales/Student"; 
		}  	      
    	//???????????????????????????	
		  String sql = "SELECT a.*,b.eip_grade_seq from eip.JLM_gradeRegister a left join eip.JL_EIP_grade b on a.gradeId=b.JL_gradeId"
		  		+ " where a.student_no = '"+LStudent.get(0).getStudent_no()+"'";  
		  		  List<JLM_gradeRegister> LJLM_gradeRegister =
		  		  jdbcTemplate.query(sql,(result,rowNum)->new JLM_gradeRegister(
		  				  "",
		  				  "",
		  				  result.getString("gradeId"), 
		  				  result.getString("gradeName"),
		  				  result.getString("saleId"), 
		  				  result.getString("salePerson"),
		  				  result.getString("registerDate")==null?"":result.getString("registerDate").substring(0,10), 
		  				  result.getString("sitNo"),
		  				  result.getString("eip_grade_seq")==null?"?":result.getString("eip_grade_seq")			  
		  		  ));
		  		 for(int i=0;i<LJLM_gradeRegister.size();i++) {
		  			 if(LJLM_gradeRegister.get(i).getSitNo().contains("???")) {
		  				LJLM_gradeRegister.get(i).setSitNo("<span style='background-color:#FFFF66'>"+LJLM_gradeRegister.get(i).getSitNo()+"</span>");
		  			 }
		  		 }
		         model.addAttribute("LgradeRegister", LJLM_gradeRegister); 
  		    	
        return "/Sales/JL_History";
    } 
    
    
    @RequestMapping("/Sales/RegisterSingle")
    public String RegisterSingle(Model model) {
        return "/Sales/RegisterSingle";
    }  
    
    @RequestMapping("/Sales/RegisterCombo")
    public String RegisterCombo(Model model,Principal principal,HttpServletRequest request) {
    	model.addAttribute("editor",principal.getName());
    	model.addAttribute("principalName",principal.getName());
    	String student_seq = request.getParameter("student_seq");
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			} 			
		}    	

		
    	if(request.getParameter("paid")!=null && !request.getParameter("paid").isEmpty()) {
    		model.addAttribute("paid",request.getParameter("paid"));
    	}
    	
    	if(request.getParameter("page")!=null && !request.getParameter("page").isEmpty()) {
    		model.addAttribute("page",request.getParameter("page"));
    	}    	
		
    	String pop = request.getParameter("pop");
    	if(pop==null || pop.isEmpty()) {
    		pop = "0";
    	}	
    	model.addAttribute("pop",pop);
 
    	String drop = request.getParameter("drop");
    	if(drop==null || drop.isEmpty()) {
    		drop = "1";
    	}	
    	model.addAttribute("drop",drop);    	
    		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);		  
		Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0);
		//String SchoolCode = student.getSchool_code();
		String school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		String balanceTotal = student.getBalanceTotal();
		String SchoolName = accountService.getSchool(school_code,"").get(0).getName();
		model.addAttribute("mySchool",SchoolName);
		model.addAttribute("balanceTotal",balanceTotal);
		
/**********????????????***********/		
  String Register_seq = request.getParameter("Register_seq");
  model.addAttribute("Register_seq", Register_seq);
  //???????????????Video??????
  int useMakeUpNo = 0; //????????????
  int makeUpRefund = 0; //????????????
  List<MakeUpRecord> LMakeUpRecord = salesService.getMakeUpRecord(student_seq,Register_seq);
  for(int x=0;x<LMakeUpRecord.size();x++) {
	  if(Integer.valueOf(LMakeUpRecord.get(x).getType())>0) {
		  makeUpRefund += Integer.valueOf(LMakeUpRecord.get(x).getAmount());
	  }else if(Integer.valueOf(LMakeUpRecord.get(x).getType())<0) {
		  makeUpRefund -= Integer.valueOf(LMakeUpRecord.get(x).getAmount());
		  useMakeUpNo += Integer.valueOf(LMakeUpRecord.get(x).getAmount());
	  }  
  }
  model.addAttribute("useMakeUpNo",useMakeUpNo);
  model.addAttribute("makeUpRefund",makeUpRefund);
  
  String returnDivStr = "";
  if(Register_seq!=null && !Register_seq.isEmpty()) {
	  Register register = salesService.getRegister("",Register_seq).get(0);
	  model.addAttribute("originPriceTotal",register.getOriginPrice());
	  model.addAttribute("salePriceTotal",register.getActualPrice());
	  model.addAttribute("student_seq",register.getStudent_seq());
	  model.addAttribute("comment",register.getComment());
	   
	  List<Register_comboSale> LRegister_comboSale_0 = salesService.getComboSaleByRegister("",Register_seq,"","","","1",false,false,"");
	  ArrayList<String> tmp = new ArrayList<String>();
	  for(int a=0;a<LRegister_comboSale_0.size();a++) {
		  tmp.add(LRegister_comboSale_0.get(a).getComboSale_id());
	  }
	  List<String> Distinct_ComboSale_id = tmp.stream().distinct().collect(Collectors.toList()); //?????? Register_seq, Distinct ???  ComboSale_id (????????????comboSale_id??????????????????)

//????????????	  
	  for(int x=0;x<Distinct_ComboSale_id.size();x++) {	
    	   String reg_comboSale_seq = Distinct_ComboSale_id.get(x);
    	   ComboSale comboSale = courseSaleService.getComboSale(reg_comboSale_seq,"","","","","","","0").get(0); //???????????????????????????

    	        //?????????
    			List<Register_counseling> LRegister_counseling= salesService.getCounselingByRegisterSeq(Register_seq,reg_comboSale_seq);
    			String counselingItem = "";
    			for(int i=0;i<LRegister_counseling.size();i++) {
    				counselingItem +="&bull;<span>"+LRegister_counseling.get(i).getCounseling_name()+"</span><br>";
    			}
    	        //??????		
    			List<Register_lagnappe> LRegister_lagnappe= salesService.getRegister_lagnappeByRegister_seq(Register_seq,reg_comboSale_seq);
    			String lagnappeItem = "";
    			for(int i=0;i<LRegister_lagnappe.size();i++) {
    				lagnappeItem +="&bull;<span>"+LRegister_lagnappe.get(i).getLagnappe_name()+"*"+LRegister_lagnappe.get(i).getLagnappe_no()+"</span><br>";
    			}
    	        //??????		
    			List<Register_mock> LRegister_mock = salesService.getMockByRegisterSeq(Register_seq,reg_comboSale_seq,"1");
    			String mockItem = "";
    			for(int i=0;i<LRegister_mock.size();i++) {
    				mockItem +="&bull;<span>"+LRegister_mock.get(i).getMock_name()+"</span><br>";
    			}
    	        //???????????????		
    			List<ComboSale_outPublisher> LComboSale_outPublisher = courseSaleService.getComboSale_outPublisher(reg_comboSale_seq);    			
    			String outPublisherItem = "";
    			for(int i=0;i<LComboSale_outPublisher.size();i++) {
    				outPublisherItem +="<input type='hidden' name='outPublisher_seq' value='"+LComboSale_outPublisher.get(i).getBook_id()+"'>";
    				outPublisherItem +="<input type='hidden' class='outPublisherPrice'  value='"+LComboSale_outPublisher.get(i).getSellPrice()+"'>";    				
    				outPublisherItem +="&bull;<span>"+LComboSale_outPublisher.get(i).getBook_name()+"</span><br>";
    			}    					

		     //String returnStr = "<div class='css-table' style='border-spacing:1px;width:1210px'>";
   				
			 List<Register_comboSale> LRegister_comboSale = salesService.getComboSaleByRegister("",Register_seq,"","",reg_comboSale_seq,"1",false,false,"");
			 String returnStr = "";
			 
			 //???????????????????????????????????????????????????
			 returnStr += "<div class='css-table' style='border-spacing:1px;width:1210px'>";
			 for(int i=0;i<LRegister_comboSale.size();i++) {		    	
		    	 List<Register_comboSale_grade> LRegister_comboSale_grade = salesService.getRegister_comboSale_grade("","1",LRegister_comboSale.get(i).getRegister_comboSale_seq(),"(1,3)");
		    	 List<FreeClass> LFreeClass = salesService.getFreeClass(LRegister_comboSale.get(i).getRegister_comboSale_seq());
				    	ComboSale_subject ComboSale_subject = courseSaleService.getComboSale_subject(LRegister_comboSale.get(i).getComboSale_id(),"").get(0);
				    	Subject subject = courseService.getSubjectWithR("",LRegister_comboSale.get(i).getSubject_id(),"","",LRegister_comboSale.get(i).getClass_style()).get(0);

					  	String tmp1 = "";
					  	String tmp2 = "";
				        //A.????????????	
						if(i==0 && LRegister_comboSale.get(i).getIsCombo().equals("1")) {
							if(request.getParameter("page")!=null && request.getParameter("page").equals("3")) { //??????	
								tmp1="<A href='javascript:void(0)' title='????????????'  onclick='Del(this);'><img src='/images/delete.png' height='10px'/></A>";
							}else {
								tmp1="";
							}
							
							if(request.getParameter("page")!=null && request.getParameter("page").equals("4")) { //??????	
								tmp2="<A href='javascript:void(0)' title='?????? '  style=''  onclick='openExchange("+LRegister_comboSale.get(i).getComboSale_id()+")'><img src='/images/exchange3.png' height='14px'/></A>";
							}else {
								tmp2="";
							}							
							returnStr +=
							"<div class='tr' id='comboSaleName' style='font-size:small;background-color:#FEF8F8'>"+
							   "<div class='td2' style='width:250px;text-align:left;background-color:#FEF8F8;letter-spacing:1px'>"+
							   		tmp1+"&nbsp;<b>"+comboSale.getName()+"</b>"+tmp2+
							   "</div>";
							   //DM??????	   
							   returnStr +=
							   "<div class='td2' style='padding:1px;width:50px;font-weight:bold'><input type='text' value='"+comboSale.getSale_price()+"' style='width:100%;border:0px;background-color:#FEF8F8;text-align:right' readonly></div>";
						    returnStr +=
							"</div>";
						//B.?????????????????????
						}else if(!LRegister_comboSale.get(i).getIsCombo().equals("1")) {
							if(request.getParameter("page")!=null && request.getParameter("page").equals("3")) { //??????	
								tmp1="<A href='javascript:void(0)' title='????????????'  onclick='Del(this);'><img src='/images/delete.png' height='10px'/></A>";
							}else {
								tmp1="";
							}
							
							if(request.getParameter("page")!=null && request.getParameter("page").equals("4")) { //??????	
								tmp2="<A href='javascript:void(0)' title='?????? '  style=''  onclick='openExchange("+LRegister_comboSale.get(i).getComboSale_id()+")'><img src='/images/exchange3.png' height='14px'/></A>";
							}else {
								tmp2="";
							}							
							returnStr +=
							"<div class='tr' id='comboSaleName' style='font-size:small;background-color:#FEF8F8'>"+
							   "<div class='td2' style='width:250px;text-align:left;background-color:#FEF8F8;letter-spacing:1px'>"+
							   		tmp1+"&nbsp;<b>"+comboSale.getCategory_name()+"</b>"+tmp2+
							   "</div>";
							   //DM??????	   
							   returnStr +=
							   "<div class='td2' style='padding:1px;width:50px;font-weight:bold'><input type='text' value='"+comboSale.getSale_price()+"' style='width:100%;border:0px;background-color:#FEF8F8;text-align:right' readonly></div>";
						    returnStr +=
							"</div>";							
						}
						
						
						returnStr +=
						"<div class='tr com"+reg_comboSale_seq+"' id='com"+reg_comboSale_seq+"sub"+LRegister_comboSale.get(i).getSubject_id()+"'  style='font-size:small;font-weight:bold;height:20px;background-color:#FEF8F8' >"+
				        //???????????????????????????
							   "<div class='td2' style='padding:1px;width:250px;text-align:left;font-size:small'>"+
				               "<input type='hidden' id='replaceFrom' name='replaceFrom' value=''>"+
				               "<input type='hidden' id='add' name='add' value=''>"+
				               "<input type='hidden' name='comboSale_seq' value='"+reg_comboSale_seq+"'>"+
				               "<input type='hidden' name='Register_comboSale_seq' value='"+LRegister_comboSale.get(i).getRegister_comboSale_seq()+"'>"+
				               "<input type='hidden' id='subject_seq' name='subject_seq' value='"+LRegister_comboSale.get(i).getSubject_id()+"@"+subject.getHrPrice_R()+"@"+subject.getCounselingPrice_R()+"@"+subject.getLagnappePrice_R()+"@"+subject.getHandoutPrice_R()+"@"+subject.getHomeworkPrice_R()+"@"+subject.getMockPrice_R()+"'>";	
						       /**
						       if(request.getParameter("page")!=null && request.getParameter("page").equals("4")) {	
								   returnStr += "&nbsp;&nbsp;&nbsp;&nbsp;<A href='javascript:void(0)' title='?????? '  style='text-decoration:underline;color:blue'  onclick='openExchange("+LRegister_comboSale.get(i).getRegister_comboSale_seq()+",this,"+LRegister_comboSale.get(i).getSubject_id()+")'><span id='Subject_name'>"+subject.getName()+"</span></A>";
							   }else {
								   returnStr += "&nbsp;&nbsp;&nbsp;&nbsp;<span id='Subject_name' style='color:darkblue'>"+subject.getName()+"</span>";
							   }
							   **/
						 	   returnStr += "&nbsp;&nbsp;&nbsp;&nbsp;<span id='Subject_name' style='color:darkblue'>"+subject.getName()+"</span>";
						   if(LFreeClass.size()>0) {
							       returnStr +="<input type='hidden' name='freeChoice' value='2'>";
								   returnStr += "<span style='margin-left:1px;background-color:aquamarine;font-size:x-small;border:1px #aaaaaa solid;border-radius:3px;padding:1px'>???</span>";
						   }else {
							       returnStr +="<input type='hidden' name='freeChoice' value='1'>";
						   }
						   if(LRegister_comboSale_grade.size()>0) {
							   returnStr += "<span style='margin-left:1px;background-color:greenyellow;font-size:x-small;border:1px #aaaaaa solid;border-radius:3px;padding:1px'>???</span>";
						   }
						   if(LRegister_comboSale.get(i).getReplaceFrom()!=null && LRegister_comboSale.get(i).getReplaceFrom().equals("1")) {
							   returnStr += "<span style='margin-left:1px;background-color:pink;font-size:x-small;border:1px #aaaaaa solid;border-radius:3px;padding:1px'>???</span>";
						   }						   
						   returnStr +=
						   "</div><div class='td2' style=''>&nbsp;</div>";
				           //????????????
						   if(LFreeClass.size()>0) {
							   returnStr +=	   
							           "<div class='td2' style='padding:1px;width:50px'><input type='text' class='originPrice' value='0' style='width:100%;border:1px dotted #FEF8F8;background-color:#FEF8F8;text-align:right' readonly></div>";							   
						   }else{
							   returnStr +=	   
									   "<div class='td2' style='padding:1px;width:50px'><input type='text' class='originPrice' value='"+LRegister_comboSale.get(i).getSubject_price()+"' style='width:100%;border:1px dotted #FEF8F8;background-color:#FEF8F8;text-align:right' readonly></div>";
						   }
						   returnStr +=
						   "<div class='td2' style='padding:1px;width:50px'><input type='text' class='salePrice' name='salePrice' style='width:100%;border:0px;text-align:right' readonly></div>"+
				           "<div class='td2' style='padding:1px;width:50px'><input type='text' class='hrPrice'   style='width:100%;border:0px;background-color:#FEF8F8;text-align:right' readonly></div>"+		           
				           "<div class='td2' style='padding:1px;width:150px'><input type='text' class='counselingPrice' style='width:100%;border:0px;background-color:#FEF8F8;text-align:right' readonly /><div class='counselingName'>"+ComboSale_subject.getCounselingStr()+"</div></div>"+	
				           "<div class='td2' style='padding:1px;width:150px'><input type='text' class='lagnappePrice' style='width:100%;border:0px;background-color:#FEF8F8;text-align:right' readonly /><div class='lagnappeName'>"+ComboSale_subject.getLagnappeStr()+"</div></div>"+		           
				           "<div class='td2' style='padding:1px;width:50px'><input type='text' class='handoutPrice' style='width:100%;border:0px;background-color:#FEF8F8;text-align:right' readonly></div>"+
				           "<div class='td2' style='padding:1px;width:50px'><input type='text' class='homeworkPrice' style='width:100%;border:0px;background-color:#FEF8F8;text-align:right' readonly></div>"+
				           "<div class='td2' style='padding:1px;width:150px'><input type='text' class='mockPrice' style='width:100%;border:0px;background-color:#FEF8F8;text-align:right' readonly /><div class='mockName'>"+ComboSale_subject.getMockStr()+"</div></div>"+		           
				           "<div class='td2' style='padding:1px;width:50px'><input type='text' class='coursePrice' style='width:100%;border:0px;background-color:#FEF8F8;text-align:right' readonly></div>"+
				           "<div class='td2' style='padding:1px;width:150px'><input type='text' class='outPublisherPrice' style='width:100%;border:0px;background-color:#FEF8F8;text-align:right' readonly /><div class='outPublisherName'>"+ComboSale_subject.getOutPublisherStr()+"</div></div>"+		           
				        "</div>";
		     }
		
			//????????????????????????
			if((!counselingItem.equals("") || !lagnappeItem.equals("") || !mockItem.equals(""))) {	
				returnStr +=		
				 "<div class='tr' style='font-size:x-small;height:5px;font-weight:bold' >"+
		           "<div class='td2' style='padding:1px;width:250px'></div>"+    
		           "<div class='td2' style='padding:1px;width:50px'></div>"+
		           "<div class='td2' style='padding:1px;width:50px'></div>"+
				   "<div class='td2' style='padding:1px;width:50px'></div>"+ 
		           "<div class='td2' style='padding:1px;width:50px'></div>"+ 
		           "<div class='td2' style='padding:1px;width:150px;text-align:left;font-size:x-small'>"+counselingItem+"</div>"+ 
		           "<div class='td2' style='padding:1px;width:150px;text-align:left;font-size:x-small'>"+lagnappeItem+"</div>"+ 
		           "<div class='td2' style='padding:1px;width:50px'></div>"+ 
		           "<div class='td2' style='padding:1px;width:50px'></div>"+ 
		           "<div class='td2' style='padding:1px;width:150px;text-align:left;font-size:x-small'>"+mockItem+"</div>"+ 
		           "<div class='td2' style='padding:1px;width:50px'></div>"+
		           "<div class='td2' style='padding:1px;width:150px;text-align:left;font-size:x-small'>"+outPublisherItem+"</div>"+ 
		         "</div>";
			}	
			
		returnDivStr += returnStr+"</div>";
	}
/**********************************************????????????????????????********************************************************/	  
	    //???????????????
		List<Register_counseling> LRegister_counseling= salesService.getCounselingByRegisterSeq(Register_seq,"-1");
		String counselingItem = "";
		for(int i=0;i<LRegister_counseling.size();i++) {
			counselingItem +="&bull;<span>"+LRegister_counseling.get(i).getCounseling_name()+"</span><br>";
		}
        //????????????		
		List<Register_lagnappe> LRegister_lagnappe= salesService.getRegister_lagnappeByRegister_seq(Register_seq,"-1");
		String lagnappeItem = "";
		for(int i=0;i<LRegister_lagnappe.size();i++) {
			lagnappeItem +="&bull;<span>"+LRegister_lagnappe.get(i).getLagnappe_name()+"*"+LRegister_lagnappe.get(i).getLagnappe_no()+"</span><br>";
		}
        //????????????		
		List<Register_mock> LRegister_mock = salesService.getMockByRegisterSeq(Register_seq,"-1","1");
		String mockItem = "";
		for(int i=0;i<LRegister_mock.size();i++) {
			mockItem +="&bull;<span>"+LRegister_mock.get(i).getMock_name()+"</span><br>";
		}
        //???????????????		
		List<Register_outPublisher> LRegister_outPublisher = salesService.getOutPublisherByRegisterSeq(Register_seq,"-1");
		String outPublisherItem = "";
		for(int i=0;i<LRegister_outPublisher.size();i++) {
			outPublisherItem +="<input type='hidden' name='outPublisher_seq' value='"+LRegister_outPublisher.get(i).getBook_id()+"'>";
			outPublisherItem +="<input type='hidden' class='outPublisherPrice' value='"+LRegister_outPublisher.get(i).getSellPrice()+"'>";  			
			outPublisherItem +="&bull;<span>"+LRegister_outPublisher.get(i).getBook_name()+" "+LRegister_outPublisher.get(i).getSellPrice()+"???</span><br>";
		}		
		
	  if(!counselingItem.equals("") || !lagnappeItem.equals("") || !mockItem.equals("") || !outPublisherItem.equals("")) {	
	      returnDivStr +=
			  "<div class='css-table' style='border-spacing:1px;width:1210px'>"+
				 "<div class='tr' style='color:darkblue;font-size:x-small;height:5px'>"+
		           "<div class='td2' style='padding:1px;width:250px'>&nbsp;</div>"+    
		           "<div class='td2' style='padding:1px;width:50px'>&nbsp;</div>"+ 
				   "<div class='td2' style='padding:1px;width:50px'>&nbsp;</div>"+ 
		           "<div class='td2' style='padding:1px;width:50px'>&nbsp;</div>"+ 
		           "<div class='td2' style='padding:1px;width:150px;background-color:#eeeeee;text-align:left;font-size:x-small'>"+counselingItem+"</div>"+ 
		           "<div class='td2' style='padding:1px;width:150px;background-color:#eeeeee;text-align:left;font-size:x-small'>"+lagnappeItem+"</div>"+ 
		           "<div class='td2' style='padding:1px;width:50px'>&nbsp;</div>"+ 
		           "<div class='td2' style='padding:1px;width:50px'>&nbsp;</div>"+ 
		           "<div class='td2' style='padding:1px;width:150px;background-color:#eeeeee;text-align:left;font-size:x-small'>"+mockItem+"</div>"+ 
		           "<div class='td2' style='padding:1px;width:50px'>&nbsp;</div>"+ 
		           "<div class='td2' style='padding:1px;width:150px;background-color:#eeeeee;text-align:left;font-size:x-small'>"+outPublisherItem+"</div>"+ 
		         "</div>"+ 
		      "</div>";
      }
  }
  model.addAttribute("returnDivStr",returnDivStr);
  
/**********????????????***********/		
		int currentDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		List<RegisterPromo> LRegisterPromo = new ArrayList<RegisterPromo>();
		if(Register_seq!=null && !Register_seq.isEmpty() && !pop.equals("re_register")) {
			LRegisterPromo = salesService.getRegisterPromo(Register_seq);
		}
		//lecture
		String lectureStr = "<div style='height:300px;overflow:auto'>";
		List<ClassPromotion> LClassPromotion = marketingService.getClassPromotion("",school_code,"1","lecture","1","1","1");
		if(LClassPromotion.size()==0) {lectureStr +="<div>&nbsp;</div>";}
		for(int i=0;i<LClassPromotion.size();i++) {
			String checkStr="";
				for(int j=0;j<LRegisterPromo.size();j++) {
					if(LRegisterPromo.get(j).getClassPromotion_id().equals(LClassPromotion.get(i).getClassPromotion_seq())) {
						checkStr = "checked";
					}	
				}
			lectureStr += "<div>"+
				             "<input type='checkbox' id='classPromotion_id' name='classPromotion_id' value='"+LClassPromotion.get(i).getClassPromotion_seq()+"' "+checkStr+" style='vertical-align:bottom;zoom:1.2'><A style='color:blue;text-decoration:underline' href='javascript:void(0)' onclick='javascript:openPromo(\"lecture\",\""+LClassPromotion.get(i).getClassPromotion_seq()+"\",\""+student_seq+"\")'>"+LClassPromotion.get(i).getPromoName()+"</A>"+
				             "</br><span style='color:#bbbbbb;letter-spacing:0px;font-size:xx-small'>("+LClassPromotion.get(i).getStartDate()+"~"+LClassPromotion.get(i).getEndDate()+")</span>"+				
						  "</div>";
		}
		lectureStr += "</div>";
		model.addAttribute("lectureStr",lectureStr);
		//regular
		String regularStr = "<div style='height:300px;overflow:auto'>";
		List<ClassPromotion> LClassPromotion1 = marketingService.getClassPromotion("",school_code,"1","regular","1","1","1");
		if(LClassPromotion1.size()==0) {regularStr +="<div>&nbsp;</div>";}
		for(int i=0;i<LClassPromotion1.size();i++) {
			String checkStr="";
			for(int j=0;j<LRegisterPromo.size();j++) {
				if(LRegisterPromo.get(j).getClassPromotion_id().equals(LClassPromotion1.get(i).getClassPromotion_seq())) {
					checkStr = "checked";
				}	
			}			
			regularStr += "<div>"+
							 "<input type='checkbox' id='classPromotion_id' name='classPromotion_id' value='"+LClassPromotion1.get(i).getClassPromotion_seq()+"' "+checkStr+" style='vertical-align:bottom;zoom:1.2'><A style='color:blue;text-decoration:underline' href='javascript:void(0)' onclick='javascript:openPromo(\"regular\",\""+LClassPromotion1.get(i).getClassPromotion_seq()+"\")'>"+LClassPromotion1.get(i).getPromoName()+"</A>"+
			                 "</br><span style='color:#bbbbbb;letter-spacing:0px;font-size:xx-small'>("+LClassPromotion1.get(i).getStartDate()+"~"+LClassPromotion1.get(i).getEndDate()+")</span>"+	
			              "</div>";
		}
		regularStr +="</div>";
		model.addAttribute("regularStr",regularStr);	
		//combine
		String combineStr = "<div style='height:300px;overflow:auto'>";
		List<ClassPromotion> LClassPromotion2 = marketingService.getClassPromotion("",school_code,"1","combine","1","1","1");
		if(LClassPromotion2.size()==0) {combineStr +="<div>&nbsp;</div>";}
		for(int i=0;i<LClassPromotion2.size();i++) {
			String checkStr="";
			for(int j=0;j<LRegisterPromo.size();j++) {
				if(LRegisterPromo.get(j).getClassPromotion_id().equals(LClassPromotion2.get(i).getClassPromotion_seq())) {
					checkStr = "checked";
				}	
			}			
			combineStr += "<div>"+
							 "<input type='checkbox' id='classPromotion_id' name='classPromotion_id' value='"+LClassPromotion2.get(i).getClassPromotion_seq()+"' "+checkStr+" style='vertical-align:bottom;zoom:1.2'><A style='color:blue;text-decoration:underline' href='javascript:void(0)' onclick='javascript:openPromo(\"combine\",\""+LClassPromotion2.get(i).getClassPromotion_seq()+"\")'>"+LClassPromotion2.get(i).getPromoName()+"</A>"+
			                 "</br><span style='color:#bbbbbb;letter-spacing:0px;font-size:xx-small'>("+LClassPromotion2.get(i).getStartDate()+"~"+LClassPromotion2.get(i).getEndDate()+")</span>"+	
			              "</div>";
		}
		combineStr += "</div>";
		model.addAttribute("combineStr",combineStr);
		//old
		String oldStr = "<div style='height:300px;overflow:auto'>";
		List<ClassPromotion> LClassPromotion3 = marketingService.getClassPromotion("",school_code,"1","old","1","1","1");
		if(LClassPromotion3.size()==0) {oldStr +="<div>&nbsp;</div>";}
		for(int i=0;i<LClassPromotion3.size();i++) {
			String checkStr="";
			for(int j=0;j<LRegisterPromo.size();j++) {
				if(LRegisterPromo.get(j).getClassPromotion_id().equals(LClassPromotion3.get(i).getClassPromotion_seq())) {
					checkStr = "checked";
				}	
			}			
			oldStr += "<div>"+
						 "<input type='checkbox' id='classPromotion_id' name='classPromotion_id' value='"+LClassPromotion3.get(i).getClassPromotion_seq()+"' "+checkStr+" style='vertical-align:bottom;zoom:1.2'><A style='color:blue;text-decoration:underline' href='javascript:void(0)' onclick='javascript:openPromo(\"old\",\""+LClassPromotion3.get(i).getClassPromotion_seq()+"\")'>"+LClassPromotion3.get(i).getPromoName()+"</A>"+
			             "</br><span style='color:#bbbbbb;letter-spacing:0px;font-size:xx-small'>("+LClassPromotion3.get(i).getStartDate()+"~"+LClassPromotion3.get(i).getEndDate()+")</span>"+	
			          "</div>";
		}
		oldStr += "</div>";
		model.addAttribute("oldStr",oldStr);
		//monthly
		String monthlyStr = "<div style='height:300px;overflow:auto'>";
		List<ClassPromotion> LClassPromotion4 = marketingService.getClassPromotion("",school_code,"1","monthly","1","1","1");
		if(LClassPromotion4.size()==0) {monthlyStr +="<div>&nbsp;</div>";}
		for(int i=0;i<LClassPromotion4.size();i++) {
			String checkStr="";
			for(int j=0;j<LRegisterPromo.size();j++) {
				if(LRegisterPromo.get(j).getClassPromotion_id().equals(LClassPromotion4.get(i).getClassPromotion_seq())) {
					checkStr = "checked";
				}	
			}			
			monthlyStr += "<div>"+
							 "<input type='checkbox' id='classPromotion_id' name='classPromotion_id' value='"+LClassPromotion4.get(i).getClassPromotion_seq()+"' "+checkStr+" style='vertical-align:bottom;zoom:1.2'><A style='color:blue;text-decoration:underline' href='javascript:void(0)' onclick='javascript:openPromo(\"monthly\",\""+LClassPromotion4.get(i).getClassPromotion_seq()+"\")'>"+LClassPromotion4.get(i).getPromoName()+"</A>"+
							 "</br><span style='color:#bbbbbb;letter-spacing:0px;font-size:xx-small'>("+LClassPromotion4.get(i).getStartDate()+"~"+LClassPromotion4.get(i).getEndDate()+")</span>"+	
			               "</div>";
		}
		monthlyStr += "</div>";
		model.addAttribute("monthlyStr",monthlyStr);
		//other
		String otherStr = "<div style='height:300px;overflow:auto'>";
		List<ClassPromotion> LClassPromotion5 = marketingService.getClassPromotion("",school_code,"1","other","1","1","1");
		if(LClassPromotion5.size()==0) {otherStr +="<div>&nbsp;</div>";}
		for(int i=0;i<LClassPromotion5.size();i++) {
			String checkStr="";
			for(int j=0;j<LRegisterPromo.size();j++) {
				if(LRegisterPromo.get(j).getClassPromotion_id().equals(LClassPromotion5.get(i).getClassPromotion_seq())) {
					checkStr = "checked";
				}	
			}
			otherStr += "<div>"+
						   "<input type='checkbox' id='classPromotion_id' name='classPromotion_id' value='"+LClassPromotion5.get(i).getClassPromotion_seq()+"' "+checkStr+" style='vertical-align:bottom;zoom:1.2'><A style='color:blue;text-decoration:underline' href='javascript:void(0)' onclick='javascript:openPromo(\"other\",\""+LClassPromotion5.get(i).getClassPromotion_seq()+"\")'>"+LClassPromotion5.get(i).getPromoName()+"</A>"+
						   "</br><span style='color:#bbbbbb;letter-spacing:0px;font-size:xx-small'>("+LClassPromotion5.get(i).getStartDate()+"~"+LClassPromotion5.get(i).getEndDate()+")</span>"+	
			            "</div>";
		}
		//otherStr += "<div class='tr'><div class='td'><input type='text' style='width:180px'></div></div>";
		otherStr += "</div>";
		model.addAttribute("otherStr",otherStr);
		
        return "/Sales/RegisterCombo";
    } 
    
    
	@RequestMapping("/Sales/SelectStr")
	@ResponseBody
	public String SelectStr(HttpServletRequest request) {
        String returnStr = "";
		String selectName = request.getParameter("selectName");
		
//????????????
		if(selectName.equals("category")) {
        	returnStr = "<div style='padding:5px'><select id='categorySel' class='td' style='width:100px' onchange=GenSelectStr('subjectSel',this.value)><option value='-1'></option>";
        	List<Category> categoryGroup = courseService.getCategory("","");
        	for(int i=0;i<categoryGroup.size();i++)	{
        		returnStr += "<option value="+categoryGroup.get(i).getCategory_seq()+">"+categoryGroup.get(i).getName()+"</option>";
        	}
            returnStr += "</select></div><div style='padding:5px'><select id='subjectSel' class='td' style='width:100px'><option value='-1'></option></select></div>";
	    }
//????????????
		if(selectName.equals("counseling")) {
		    returnStr = "<div style='font-size:x-small;color:darkblue;font-weight:bold' align=left>&nbsp;&#8227; ????????????  </div><select id='categorySel' style='width:100px' onchange=GenSelectStr('counselingSel',this.value)><option value='-1'></option>";
		    List<Category> categoryGroup = courseService.getCategory("","");
		    for(int i=0;i<categoryGroup.size();i++)	{
		        returnStr += "<option value="+categoryGroup.get(i).getCategory_seq()+">"+categoryGroup.get(i).getName()+"</option>";
		    }
		    returnStr += "</select>&nbsp;<br><div><select id='counselingSel' style='width:100px'><option value='-1'></option></select></div>";
	    }		
		

        return returnStr;
	}
	
	@RequestMapping("/Sales/AddSubjectStr")
	@ResponseBody
	public String AddSubjecteStr(HttpServletRequest request) {	
		Subject subject = courseService.getSubject("",request.getParameter("subject_seq"),"","","","0").get(0);
		String returnStr = 		
		"<div class='tr' style='padding:2px;height:20px;background-color:lightyellow;'>"+
			"<div class='td' style='width:200px;font-weight: bold; color:darkblue;border: 1px #eeeeee solid;'>"+subject.getName()+"</div>"+
			"<div class='td' style='width:100px;'>"+subject.getPrice()+"</div>"+
			"<div class='td' style='width:100px;'><input type='text' name='subject_salePrice' style='width:55px' class='salePrice' value='"+subject.getPrice()+"' onchange='PriceAdd();'></div>"+
		"</div>"; 		
		return returnStr;
	}


	@RequestMapping("/Sales/ChgSubjectStr")
	@ResponseBody
	public String ChgSubjectStr(HttpServletRequest request) {
		/*
		 * Subject subject =
		 * courseSaleService.getSubjectReplace(request.getParameter("old_subject_id"),
		 * request.getParameter("new_subject_id")); String returnStr =
		 * "<div id='' class='tr' style='padding:5px;height:20px;'>"+
		 * "<div class='td2' style='width:240px;font-weight:bold;color:red;text-align:left'>&nbsp;&nbsp;"
		 * +subject.getName()+"</div>"+ "<div class='td2' style='width:90px;'></div>"+
		 * "<div class='td2' style='width:90px;'></div>"+
		 * "<div class='td2' style='width:90px;'></div>"+
		 * "<div class='td2' style='width:90px; color:red;font-size:x-small;font-weight:bold'>?????????: <span class='priceDifference'>"
		 * +subject.getRemark()+"</span></div>"+ "</div>";
		 */
		
		return "";
	}

	@RequestMapping("/Sales/AddCounselingStr")
	@ResponseBody
	public String AddCounselingStr(HttpServletRequest request) {	
		List<Counseling> counseling= courseSaleService.getCounseling(request.getParameter("counseling_seq"));
		String returnStr = 		
		"<div class='tr' style='padding:5px;height:20px;background-color:#D8BFD8;'>"+
			"<div class='td' style='width:200px;font-weight: bold; color:darkblue;border: 1px #eeeeee solid;'>"+counseling.get(0).getCounseling_name()+"</div>"+
			"<div class='td' style='width:100px;'>"+counseling.get(0).getOrigin_price()+"</div>"+
			"<div class='td' style='width:100px;'><input type='text' name='subject_salePrice' style='width:55px' class='salePrice' value='"+counseling.get(0).getOrigin_price()+"' onchange='PriceAdd();'></div>"+
			"<div class='td' style='width:100px;'><input type='text' style='width:30px'>???</div>"+
			"<div class='td'></div>"+
		"</div>";  		
		return returnStr;
	}	
	
    @RequestMapping("/Sales/getCurrentCourse")
    @ResponseBody
    public String getCurrentCourse(HttpServletRequest request){
    	String category_id = request.getParameter("category_id");
    	String school_code = request.getParameter("school_code");
    	
    	List<Grade> Lgrade = courseService.getGradeList("","","",school_code,category_id,"","","","4","1","","","","","order by a.grade_date asc","","","1","");
    	List<Subject> Lsubject = courseService.getSubject(category_id,"","","","","0");

		String returnStr = "";
		String class_date = "";
		String wk = "";
		String timeFrom_0 = "";
		String timeFrom = "";
		String timeTo_0 = "";
		String timeTo = "";
		String classTeacherName = "";
		//??????
		for(int x=0;x<Lsubject.size();x++) {
	        int existGrade = 0;
	        //??????
		    for(int i=0;i<Lgrade.size();i++) {
			     if(Lgrade.get(i).getSubject_id().equals(Lsubject.get(x).getSubject_seq())) {
			    	 existGrade++;
			    	 if(existGrade==1) {
			            returnStr+=
	          			"<div class='css-table' style='border-spacing:1px'>"+
		        		      "<div class='tr'>"+
									"<div class='th2' style='color:white;background-color:#3e7e99;width:35px'><div style='width:35px'>??????</div></div>"+ 
									"<div class='th2' style='letter-spacing:1px;font-size:medium;font-weight:bold;color:white;background-color:#266489;text-align:center;vertical-align:bottom'>"+Lsubject.get(x).getName()+"</div>"+
		        		      "</div>"+
		        		      "<div class='tr'>"+
									"<div class='th2' style='color:white;background-color:#3e7e99;vertical-align:top'>"+
										"<div style='text-align:center;height:15px;vertical-align:middle'>??????</div>"+
										"<div style='text-align:center;height:20px;vertical-align:middle'>??????</div>"+
										"<div style='text-align:center;height:20px'>??????</div>"+
										"<div style='text-align:center;height:30px'>&nbsp;<p>??????</div>"+
									"</div>";	
			    	 }  
			            
			            //?????????
				    	 timeFrom_0 = Lgrade.get(i).getTimeFrom();
				    	 if(timeFrom_0.length()==4) {timeFrom = timeFrom_0.substring(0,2)+":"+timeFrom_0.substring(2,4);}
				    	 timeTo_0 = Lgrade.get(i).getTimeTo();
				    	 if(timeTo_0.length()==4) {timeTo = timeTo_0.substring(0,2)+":"+timeTo_0.substring(2,4);}
				    	 String wk_0 = "";
				    	 if(Lgrade.get(i).getClass_start_date_0()!=null && !Lgrade.get(i).getClass_start_date_0().isEmpty()) {
				    		 wk_0 = DateToWeek.getDateToWeek(Lgrade.get(i).getClass_start_date_0());
				    	 }	 

			            returnStr+=		
				          "<div style='vertical-align:top;display:inline-block;border-width:1px;border-color:#eeeeee;border-left-style:solid;border-right-style:solid;border-bottom-style:solid'>"+
				        		  "<div style='font-weight:bold;color:white;background-color:#7ba6b6;text-align:center'>"+wk_0+"&nbsp;"+(Lgrade.get(i).getVideo_date().equals("")?Lgrade.get(i).getSlot_name():"&nbsp;")+"</div>"+
						          "<div style='padding:3px;background-color:#eeeeee;text-align:center;font-size:x-small'>"+(Lgrade.get(i).getVideo_date().equals("")?timeFrom+" ~ "+timeTo:"&nbsp;")+"</div>"+
						          "<div style='padding:1px;font-size:x-small'>"+
						        		 "<div style='padding:1px;width:45px;text-align:center;display:inline-block'>"+Lgrade.get(i).getTeacher_name()+"</div><div style='padding:3px;display:inline-block;margin-left:5px'>?????? : "+(Lgrade.get(i).getVideo_date().equals("")?Lgrade.get(i).getClass_start_date().subSequence(2,6):Lgrade.get(i).getVideo_date())+"</div>"+
					              "</div>"+
						          "<div style='height:10px'></div>"+	 
						        		  "<div style='padding:1px;font-size:x-small;background-color:white'>";			            
						            	   if(Lgrade.get(i).getVideo_date().equals("")) {
						            			List<Classes> Lclasses = courseService.getClasses(Lgrade.get(i).getGrade_seq(),"","","","","","","","","");						
						            			//??????????????????
						            			Boolean isDispaly = false;
						            			for(int a=0;a<Lclasses.size();a++) {
						            				if(Lclasses.get(a).getClass_style().equals("1") && !Lgrade.get(i).getTeacher_id().equals(Lclasses.get(a).getTeacher_id())) {
						            					isDispaly = true;
						            				}
						            			}
						            			
						            			
						            			
						            			String class_time ="";
						            			String gradeName = "";
						            			for(int j=0;j<Lclasses.size();j++) {
						            				if(Lclasses.get(j).getClass_th().equals("1")) {
						            					gradeName = Lgrade.get(i).getGradeName()==null?"":Lgrade.get(i).getGradeName();
						            				}else {
						            					gradeName = "";
						            				}
						            				class_date = Lclasses.get(j).getClass_date();
						            				wk = "";
						            				if(class_date!=null && class_date.length()>5) {
						            					if(class_date.length()==10) {wk = DateToWeek.getDateToWeek(class_date);} //?????????
						            					class_date = class_date.substring(0,5);
						            				}
						            				if(isDispaly) {
						            					classTeacherName = Lclasses.get(j).getTeacher_name()==null?"":Lclasses.get(j).getTeacher_name();
						            				}else {
						            					classTeacherName = "";
						            				}
						            				//??????????????????
						            				if(Lclasses.get(j).getTime_from().equals(timeFrom_0) && Lclasses.get(j).getTime_to().equals(timeTo_0)) {
						            					class_time = "";
						            				}else {
						            					//?????????video
						            					if(Integer.valueOf(Lclasses.get(j).getClass_th())<0 || !Lclasses.get(j).getClass_style().equals("1")) {
						            						class_time = "";
						            					}else { 						
						            						class_time = Lclasses.get(j).getTime_from()+"~"+Lclasses.get(j).getTime_to();
						            					}
						            				}
						            				if(Lgrade.get(i).getGrade_seq().equals(Lclasses.get(j).getGrade_id())) {
						            					if(Lclasses.get(j).getClass_style().equals("1")) {					
						            						returnStr+= "<div style='padding:3px;width:30px;text-align:center;display:inline-block'>"+gradeName+(Integer.valueOf(Lclasses.get(j).getClass_th())<0?"??????":Lclasses.get(j).getClass_th())+"</div><div style='padding:3px;display:inline-block;margin-left:15px'>"+class_date+wk+"&nbsp;"+classTeacherName+"&nbsp;"+class_time+"</div><br>";
						            					}else {
						            						returnStr+= "<div style='padding:3px;width:30px;text-align:center;display:inline-block'>"+gradeName+(Integer.valueOf(Lclasses.get(j).getClass_th())<0?"??????":Lclasses.get(j).getClass_th())+"</div><div style='padding:3px;display:inline-block;margin-left:15px'>"+Lclasses.get(j).getClass_name()+"(V)&nbsp;"+classTeacherName+"&nbsp;"+class_time+"</div><br>";
						            					}
						            				}
						            			}
						            		}else {
						            			returnStr+= "<div>&nbsp;</div><div style='text-align:center'>Video??????<br>??????????????????</div>";
						            		}							        
								          returnStr+=
						                  "</div><br>";
					              returnStr+=
						          "</div>";		            
			     }       
		    }
		    if(existGrade!=0){
			    returnStr+= "</div>";
			    returnStr+= "</div>";
			    returnStr+= "<div>&nbsp;</div>";
		    }    
       }
		return returnStr;
    } 	
    @RequestMapping("/Sales/StudentCalendar")
    public String StudentCalendar(Model model,HttpServletRequest request,HttpSession session) {
    	String student_seq = request.getParameter("student_seq");
    	if(session.getAttribute("backURL")!=null) {
    		model.addAttribute("backURL",session.getAttribute("backURL"));
    	}
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			}		
		}
    	
    	String mock = request.getParameter("mock");
    	if(mock!=null && mock.equals("1")) {
    		model.addAttribute("mock","1");
    	}else {
    		model.addAttribute("mock","0");
    	}
	

    	String school_code = request.getParameter("school_code");
    	model.addAttribute("school_code",school_code);

    	
        return "/Sales/StudentCalendar";
    } 

    @RequestMapping("/Sales/RegisterSave")
    public String RegisterSave(HttpServletRequest request,Principal principal) {		
    	String student_seq = request.getParameter("student_seq");
    	String InstalNoSel = request.getParameter("InstalNoSel");
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}
		 
		String pop = request.getParameter("pop");

		//????????????
    	String[] A_classPromotion_id = request.getParameterValues("classPromotion_id");
    	
    	//??????eip.Register
    	String rebate = request.getParameter("rebate"); 
    	String originPrice = request.getParameter("originPriceTotal");
    	String actualPrice = request.getParameter("salePriceTotal");
    	String commentThis = request.getParameter("commentThis");
    	String[] A_comboSale_seq = request.getParameterValues("comboSale_seq"); //???????????????
    	String[] A_Register_comboSale_seq = request.getParameterValues("Register_comboSale_seq");    	
    	String[] A_subject_seq = request.getParameterValues("subject_seq");
    	String[] A_replaceFrom = request.getParameterValues("replaceFrom"); //????????????
    	String[] A_Register_comboSale_seq_off = request.getParameterValues("Register_comboSale_seq_off"); //????????????
    	String[] A_addMoney = request.getParameterValues("addMoney");
    	String[] A_salePrice = request.getParameterValues("salePrice");
    	String[] A_freeChoice = request.getParameterValues("freeChoice"); 
    	String creater = principal.getName();
    	String Register_seq = request.getParameter("Register_seq");
    	String payWay = request.getParameter("payWay");
    	String cashRefund1 = request.getParameter("cashRefund1"); //??????
    	String cashRefund2 = request.getParameter("cashRefund2"); //??????

    	//?????????????????????????????????
 		if(A_replaceFrom!=null){
 			Boolean exchange = false;
 			for(int x=0;x<A_replaceFrom.length;x++) {
 				if(A_replaceFrom[x].equals("1")) {
 					exchange = true;
 				}
 			}
 			
 			//??????????????????
 			if(exchange) {
	 	    	String remarkSystem = "";	    	
		 	   	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 	   	Date cal = new Date();
		 	    String actualPrice_or = salesService.getRegisterList("","",Register_seq,"").get(0).getActualPrice();
		 			    	
			 	    remarkSystem +="<div style=width:100%;text-align:center;background-color:#eeeeee>[ ?????? ] "+creater+" "+dateFormat.format(cal)+"</div>";				
				if(A_Register_comboSale_seq_off!=null) {	
					for(int i=0;i<A_Register_comboSale_seq_off.length;i++) {
						remarkSystem += courseService.getSubject("",A_Register_comboSale_seq_off[i].split("@")[1],"","","","0").get(0).getName()+"<br>";
					}
				}	
				remarkSystem +=" ?????? &#8680;<br>";	
		 	    for(int i=0;i<A_replaceFrom.length;i++) {
		 			  if(A_replaceFrom[i]!=null && A_replaceFrom[i].equals("1")) {	
			 				remarkSystem +=courseService.getSubject("",A_subject_seq[i].split("@")[0],"","","","0").get(0).getName()+"<br>";
		 			  }		
	 			}
							remarkSystem +="??????: "+actualPrice_or;
							remarkSystem +=" ?????? &#8680; ";
							remarkSystem +=actualPrice;
							remarkSystem +="<br>"; 	 	    
	 			commentThis = remarkSystem+commentThis;
 			}	
 		} 
 		
 		
    	String commentTotal = commentThis;
    	if(Register_seq != null && !Register_seq.isEmpty() && !pop.equals("re_register")) {
    		commentTotal = commentThis+salesService.getRegister("",Register_seq).get(0).getComment();
    	}	
    	//register???????????????
    	Register register = new Register(Register_seq,student_seq,originPrice,actualPrice,"0","0","",commentTotal,creater,"1","","","","","","0");
    	    	
    	//??????eip.Register_counseling
    	String[] A_counseling_seq = request.getParameterValues("counseling_seq");
    	//??????eip.Register_lagnappe
    	String[] A_lagnappe_seq = request.getParameterValues("lagnappe_seq");
    	String[] A_lagnappe_no = request.getParameterValues("lagnappe_no");
    	//??????eip.Register_mock
    	String[] A_mock_seq = request.getParameterValues("mock_seq");
    	//??????eip.Register_outPublisher
    	String[] A_books_seq = request.getParameterValues("books_seq");

    	//????????????+??????+???????????? 
    	//??????orderChange
    	int newSeq = salesService.RegisterSave(pop,register,A_comboSale_seq,A_Register_comboSale_seq,A_subject_seq,A_classPromotion_id,A_lagnappe_seq,A_lagnappe_no,A_mock_seq,A_counseling_seq,A_replaceFrom,A_books_seq,A_addMoney,A_salePrice,A_freeChoice,A_Register_comboSale_seq_off);
        
        
    	//???????????? : ?????????or??????or?????? 
    	if(Register_seq==null || Register_seq.isEmpty() || (pop!=null && pop.equals("re_register")) || (pop!=null && pop.equals("exchange"))) {
	    	StudentPayRecord studentPayRecord = new StudentPayRecord();
	    	studentPayRecord.setTakePerson(accountService.getAccountByID("",principal.getName()).getCh_name());
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	    	studentPayRecord.setPayDate(formatter.format(new Date()));
	    	studentPayRecord.setSchool_code(accountService.getAccountByID("",principal.getName()).getSchool());    	
	    	
	    	//????????????????????????register_id
	    	if(pop!=null && pop.equals("exchange")) {
	    		studentPayRecord.setRegister_id(Register_seq);
	    	}else {
	    		studentPayRecord.setRegister_id(String.valueOf(newSeq));
	    	}
	    	
	    	studentPayRecord.setActualPrice(actualPrice);
	    	//payMoney_or:????????????
	    	String payMoney_or = salesService.StudentPaySaveRecord(
    			studentPayRecord,
    			request.getParameter("payStyle_1_money"),
    			request.getParameter("payStyle_2_money"),
    			request.getParameterValues("payStyle_3_date"),
    			request.getParameterValues("payStyle_3_money"),
    			request.getParameter("payStyle_4_code"),
    			request.getParameter("payStyle_4_money"),
    			request.getParameter("payStyle_5_periods"),
    			request.getParameter("payStyle_5_code"),
    			request.getParameter("payStyle_5_money"),
    			request.getParameter("payStyle_6_code"),
    			request.getParameter("payStyle_6_money"),
    			request.getParameter("payStyle_7_code"),
    			request.getParameter("payStyle_7_money"),
    			student_seq,
    			principal.getName(),
    			request.getParameter("new_paid"), //????????????
    			payWay, //1:??????,-1:??????
    			rebate,
    			cashRefund1, //??????
    			cashRefund2,  //??????
    			InstalNoSel
	    	); 
	    	
	        //orderChange(??????:?????????????????????????????????)
	    	//??????
	    	if(pop.equals("re_register")) {
	    		salesService.orderChangeSave(String.valueOf(newSeq),student_seq,"10","","","","","",request.getParameter("new_paid"),"",actualPrice,principal.getName(),"");
	    		if(payWay.equals("1") && !cashRefund1.equals("0")) { //11.??????????????????
	    			if(rebate!=null && rebate.equals("1")) { //??????
	    				salesService.orderChangeSave(String.valueOf(newSeq),student_seq,"14","","","","","",payMoney_or,"",actualPrice,principal.getName(),"");
	    			}else {
	    				salesService.orderChangeSave(String.valueOf(newSeq),student_seq,"11","","","","","",payMoney_or,"",actualPrice,principal.getName(),"");
	    			}	
	    		}else if(payWay.equals("-1") && !cashRefund2.equals("0")) { //12.??????????????????
	    			if(rebate!=null && rebate.equals("1")) { //??????
	    				salesService.orderChangeSave(String.valueOf(newSeq),student_seq,"15","","","","","",cashRefund2,"",actualPrice,principal.getName(),"");
	    			}else { //????????????
	    				salesService.orderChangeSave(String.valueOf(newSeq),student_seq,"12","","","","","",payMoney_or,"",actualPrice,principal.getName(),"");
	    			}	
	    		}
	    		String deductPrice = request.getParameter("deductPrice");
	    		if(deductPrice!=null && !deductPrice.isEmpty() && !deductPrice.equals("0")) {//13.??????
	    			salesService.orderChangeSave(String.valueOf(newSeq),student_seq,"13","","","","","",deductPrice,"",actualPrice,principal.getName(),"");
	    		}
	    	}else {
	    		//?????????
	    		salesService.orderChangeSave(String.valueOf(newSeq),student_seq,"9","","","","","",String.valueOf(payMoney_or),"",actualPrice,principal.getName(),""); 
	    	}
    	}
    	
        //??????eip.student.[remarkTotal]????????????
 		if(commentThis!=null && !commentThis.isEmpty()) {
 			String remarkTotalOri = salesService.getStudent(student_seq,"","","","","","","","","").get(0).getRemarkTotal();
 			if(remarkTotalOri==null) {remarkTotalOri="";}
		    String remarkTotal = commentThis+remarkTotalOri; 
 		    jdbcTemplate.update("Update eip.student set remarkTotal=?,update_time=NOW() where student_seq=?;",
 				remarkTotal, 
 				student_seq
 		    );
 		}    


    	//????????????
    	String schoolCode = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();		
    	String experience_id = "";
    	String experience_content = "";
    	String experience_state = "";
    	if(pop!=null && (pop.equals("0") || pop.equals("re_register"))) {
    		List<Register_comboSale> LRegister_comboSale = salesService.getComboSaleByRegister("",String.valueOf(newSeq),"","","","",false,false,"");
    		List<String> tmp = new ArrayList<String>();
    		for(int i=0;i<LRegister_comboSale.size();i++) {
    			if(LRegister_comboSale.get(i).getIsCombo().equals("0")) {
    				experience_content += LRegister_comboSale.get(i).getSubject_name()+"<br>";
    			}else {     				
    				Boolean containsThis = false;
    				for(int x=0;x<tmp.size();x++) {
    					if(LRegister_comboSale.get(i).getComboSale_id().equals(tmp.get(x))) {
    						containsThis = true;
    					}
    				}
    				if(!containsThis) {
    					experience_content += "<b>"+LRegister_comboSale.get(i).getComboSale_name()+"</b><br>";
    				}
    				tmp.add(LRegister_comboSale.get(i).getComboSale_id());
    			}
    		}
    		//??????
    		String orderStatus = salesService.getRegister("",String.valueOf(newSeq)).get(0).getOrderStatus();
    		if(orderStatus.equals("1")) {
    			experience_state="?????????";
    		}else if(orderStatus.equals("2")) {
    			experience_state="??????";
    			   		
	    	    //??????????????????Video??????
    			String openRegister = "<A href='javascript:void(0)' onclick='openRegister("+newSeq+","+student_seq+")' style='color:blue;font-size:small;font-weight:bold;text-decoration:underline'>no."+newSeq+"</A>";
	    			List<Register_lagnappe> LRegister_lagnappe = salesService.getRegister_lagnappe("","","",String.valueOf(newSeq),"0","2");
	    			for(int i=0;i<LRegister_lagnappe.size();i++) {
	    					salesService.updateRegister_lagnappe(LRegister_lagnappe.get(i).getRegister_lagnappe_seq(),"1","System","");   	    	
			    		    Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0);    		   		    		
			    		    salesService.updateMakeUpTotal(
					    				student.getStudent_no(),
					    				Integer.valueOf(student.getMakeUpTotal())+Integer.valueOf(LRegister_lagnappe.get(i).getLagnappe_no()),
					    				LRegister_lagnappe.get(i).getLagnappe_no(),
					    				student_seq,
					    				principal.getName(),
					    				"1", //1:????????????
					    				"", //remark
					    				openRegister, //content
					    				String.valueOf(newSeq)
			    		    );
	    			}	     		
    		}
    		
    		experience_id = "1";
    		salesService.insertStudentExperience(student_seq,schoolCode,experience_id,experience_state,experience_content,"",principal.getName(),String.valueOf(newSeq));  		
    	}
    	
    	if(pop!=null && pop.equals("re_register")) {
    		experience_id = "6";
    		salesService.insertStudentExperience(student_seq,schoolCode,experience_id,"",experience_content,"",principal.getName(),Register_seq);  		
    	}
        if(pop!=null && pop.equals("exchange")) {
    		experience_id = "7";
    		salesService.insertStudentExperience(student_seq,schoolCode,experience_id,"",experience_content,"",principal.getName(),Register_seq);  		
    	}
        if(pop!=null && pop.equals("refund")) {
    		experience_id = "8";
    		salesService.insertStudentExperience(student_seq,schoolCode,experience_id,"",experience_content,"",principal.getName(),Register_seq);  		
    	}
 
	    //????????????Video??????
        String openRegister = "<A href='javascript:void(0)' onclick='openRegister("+Register_seq+","+student_seq+")' style='color:blue;font-size:small;font-weight:bold;text-decoration:underline'>no."+Register_seq+"</A>";
        String makeUpRefund = request.getParameter("makeUpRefund");
        if(makeUpRefund!=null && !makeUpRefund.isEmpty() && !makeUpRefund.equals("0")) {
		    Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0);
		    int makeUpTotal = Integer.valueOf(student.getMakeUpTotal());
	
		    if(makeUpTotal>=Integer.valueOf(makeUpRefund)) {
		    	makeUpTotal = makeUpTotal-Integer.valueOf(makeUpRefund);
		    }else {
		    	makeUpTotal = 0;
		    }
			jdbcTemplate.update("UPDATE eip.student set makeUpTotal=? where student_seq=?;", 
					makeUpTotal,
					student_seq
			);
			
			//?????????????????????????????????
			jdbcTemplate.update("INSERT INTO eip.makeUpRecord VALUES (default,?,?,?,?,?,?,NOW(),?);", 
					student_seq, 
					-2, //??????????????????
					makeUpRefund,
					openRegister, 
					"", 
					principal.getName(),
					Register_seq
			);
        }	
        
        
    	
    	 	
    	if(pop!=null && (pop.equals("exchange") ||  pop.equals("re_register"))) {
        	return "/common/closeAndReload"; //??????
        }else {
        	return "redirect:/Sales/FeeRecord?student_seq="+student_seq; //????????????,??????
        }
    }
    
    @RequestMapping("/Sales/FeeRecord")
    public String FeeRecord(Model model,HttpServletRequest request) {
	
    	String student_seq = request.getParameter("student_seq");
    	String student_no = "";
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			}
			
    		List<Register> LRegister = salesService.getRegisterList(student_seq,"Fee","","");// [?????????/??????]
    		model.addAttribute("LRegister", LRegister);
    		Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0);
    		model.addAttribute("balanceTotal",student.getBalanceTotal());
    		model.addAttribute("makeUpTotal",student.getMakeUpTotal());			
		}		

//??????		  
		//String sql = "SELECT * from eip.JLM_studentPay where student_no = '"+student_no+"'";	  
		  List<JLM_studentPay> LJLM_studentPay = salesService.getJLM_studentPay(student_no,"","","");
	  
		  model.addAttribute("LJLM_studentPay", LJLM_studentPay);
		  
        return "/Sales/FeeRecord";
    }
    
    @RequestMapping("/Sales/Book_grade")
    public String Book_grade(HttpServletRequest request,Model model) {
    	String student_seq = request.getParameter("student_seq");
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			}
			
    		//0?????????,1????????????,2???????????? 
			List<Register> LRegister = salesService.getRegisterList(student_seq,"Book","","(0,1,2)");//????????????(??????,??????,??????)
			model.addAttribute("LRegister", LRegister);  	
	        return "/Sales/Book_grade";  
    	}    
    }     


    
    @RequestMapping("/Sales/getGradeToSelect")
    @ResponseBody
    public String getGradeToSelect(HttpServletRequest request,Principal principal) {
    	String schoolCode = request.getParameter("schoolCode");
    	String subject_id = request.getParameter("subject_id");
    	String section = request.getParameter("section");
    	String student_seq = request.getParameter("student_seq");
    	String teacher_id = request.getParameter("teacher_id");
    	String style1="";
    	String style3="";
    	String showIcon1 = "&minus;";
    	String showIcon3 = "&minus;";
    	String gradeName = "";
		
    	//??????????????????????????????
    	List<Grade> LGrade= courseService.getGradeList(teacher_id,subject_id,"","","","","","","4,5","1","","","","","","","","0",""); 	
    	//????????????
		List<Pre_grade> LPre_grade = salesService.getPre_grade2(subject_id,teacher_id);	
    	List<Grade> LGradeNew = new ArrayList<Grade>();  //????????????
    	List<Grade> LGradeNewV = new ArrayList<Grade>(); //Video??????
    	List<Grade> LGradeNewC = new ArrayList<Grade>(); //????????????
    	List<Grade> LGradeNewV0 = new ArrayList<Grade>(); //???Video???????????????????????????   
    	List<Grade> LGradeNewVDnt = new ArrayList<Grade>(); //Video????????????
    	
    	
    	//<1.????????????>---------------------------------------------------------------------------------------------------------------------
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		String nowDate = sdFormat.format(new Date());  
		int number = 0;
    	for(int i=0;i<LGrade.size();i++) {
    		if(LGrade.get(i).getClass_style().equals("1")) { //????????????Video
	    		String attendStatus  = salesService.getAttendStatus(LGrade.get(i).getGrade_seq(),"1");
	    		String attendStatusV = salesService.getAttendStatus(LGrade.get(i).getGrade_seq(),"2");
	    		LGrade.get(i).setAttendStatus(attendStatus); //????????????+????????????+?????????
	    		LGrade.get(i).setAttendStatusV(attendStatusV); //????????????+?????????
	    		
	    		Boolean continuousNext = true;
	    		if(LGrade.get(i).getStatus_code().equals("4") && number<20) {//??????????????????????????????, more??????20???
	    			//???0?????????????????????
	    			if(section.equals("0") && salesService.afterThenNow(LGrade.get(i).getOffSell(),nowDate)) {
	    				continuousNext = true;
	    				
	    			}else if(section.equals("1") && !salesService.afterThenNow(LGrade.get(i).getOffSell(),nowDate)) {
	    			//???0?????????????????????	(more...??????20???)
	    				continuousNext = true;
	    				
	    			}else{
	    				continuousNext=false;
	    			}
	    			if(continuousNext) {
		    			  if(LGrade.get(i).getClass_style().equals("1") && LGrade.get(i).getSchool_code().equals(schoolCode)){
			    			    LGradeNew.add(LGrade.get(i));
			    			    if(section.equals("1")){number++;}
			    		  } 
	    			}	  
	    		}
	    		
	    		
	    		//Video 4:??????,5:??????
	    		if(LGrade.get(i).getStatus_code().equals("4") || LGrade.get(i).getStatus_code().equals("5")) { 
	 			   //LGradeNewV0-->???Video???????????????????????????   			   
		    		   if(salesService.openVideoToSchoolArea(schoolCode,LGrade.get(i).getGrade_seq())){
		 				      LGradeNewV0.add(LGrade.get(i));				   
		 		       }	   
	 		    }
    		}else if(LGrade.get(i).getClass_style().equals("3")) { //??????
	 				   LGradeNewC.add(LGrade.get(i));
	 		}	    			
    	}	
    	
    	
    	//<2.Video>--------------------------------------------------------------------------------------------------------------------			   		  
		   //LGradeNewVDnt--->Video????????????
		   for(int a=0;a<LGradeNewV0.size();a++) {
			   Boolean exist = false;
			   for(int b=0;b<LGradeNewVDnt.size();b++){
				   if((LGradeNewV0.get(a).getTeacher_id()+LGradeNewV0.get(a).getGradeName()).equals(LGradeNewVDnt.get(b).getTeacher_id()+LGradeNewVDnt.get(b).getGradeName())) {
					   exist = true;
				   }					   				   
			   }
			   if(!exist){LGradeNewVDnt.add(LGradeNewV0.get(a));}
		   }
		   
		   int limitShow = 0;
		   for(int a=0;a<LGradeNewVDnt.size();a++) {
			   int x = 0;
			   if(limitShow>20) {break;}
			   for(int b=0;b<LGradeNewV0.size();b++) {
				   if((LGradeNewVDnt.get(a).getTeacher_id()+LGradeNewVDnt.get(a).getGradeName()).equals(LGradeNewV0.get(b).getTeacher_id()+LGradeNewV0.get(b).getGradeName())) {
					   x++;
					   if(section.equals("0")) {  //Video??????????????????????????????
						   if(x<3) {LGradeNewV.add(LGradeNewV0.get(b));}
					   }else if(section.equals("2")) { //Video???????????????20???
						   if(x>2) {
							   limitShow ++;
							   if(limitShow>20) {break;}
							   LGradeNewV.add(LGradeNewV0.get(b));
						   }	
					   }	   
				   }	   
			   }		   
		   }	   

		   
//<????????????>---------------------------------------------------------------------------------------------------------------------			   
		   String cols = 
				"<div class='tr' style='background-color:#3e7e99;color:white;font-size:small;font-weight:bold'>"+
					"<div class='td2' style='width:70px;padding:0px;text-align:center;border-radius:2px'>????????????</div>"+
					"<div class='td2' style='width:230px;text-align:center;border-radius:2px'>??? ???  (??????)</div>"+
					"<div class='td2' style='width:50px;text-align:center;border-radius:2px'>??? ???</div>"+			
					"<div class='td2' style='width:150px;text-align:center;border-radius:2px'>??? ??? (??????/??????)</div>"+
					"<div class='td2' style='width:80px;text-align:center;border-radius:2px'>??? ???</div>"+	
					"<div class='td2' style='width:120px;text-align:center;border-radius:2px'>??? ??? ???</div>"+					
				"</div>";

		   String top_title_1= 
			  "<div style='margin-left:50px;text-align:left'><img src='/images/teacher.png' height='13px'/> ?????? &nbsp;<span style='font-size:x-small;color:#555555;letter-spacing:1px'>(???3?????????)</span></div>"+				   
			  "<div class='css-table' style='border-spacing:1px;margin-top:0px'>"+
				cols+
			  "</div>";
		   String top_title_2= 
		      "<div style='margin-left:50px;text-align:left'><img src='/images/earphone.png' height='9px'/> Video&nbsp;<span style='font-size:x-small;color:#555555;letter-spacing:1px'>(?????????????????????)</span></div>"+		   
			  "<div class='css-table' style='border-spacing:1px;margin-top:0px'>"+
				cols+
			  "</div>";	
		   String top_title_3= 
			  "<div style='margin-left:50px;text-align:left'><img src='/images/cloud.png' height='12px'/> ??????</span></div>"+	   
			  "<div class='css-table' style='border-spacing:1px;margin-top:0px'>"+
				cols+
			  "</div>";			   
  
			  String returnStr ="";
	    	  returnStr +="<div><span style='margin-left:10px;background-color:tomato;color:white;padding:2px'><A href='javascript:void(0)' onclick='View1(this)' style='font-weight:bold;color:white'>"+showIcon1+"</A></span><span style='border:1px #dddddd dashed;border-radius:5px;font-weight:bold;letter-spacing:10px;color:black;background-color:#ffefff'>&nbsp;??????</span></div>";
	    	  returnStr +="<div style='margin-left:20px;background-color:#ffffff;width:800px' align='center'>";	
//<1.A ????????????>---------------------------------------------------------------------------------------------------------------------		   
	    	  if(section.equals("0") || section.equals("1")) {
				    	if(section.equals("0")) {
				    		returnStr += top_title_1;
				    	}else if(section.equals("1")) { //??????more...
				    		returnStr = "";
				    		style1 = "";
				    	} 	
			    	returnStr +="<div class='css-table child1' style='text-align:center;border-spacing:1px;background-color:#eeffee;"+style1+"'>";
			    	
					List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("",student_seq,"","","(1)","","","(0,1,-1)","","","");
			    	for(int i=0;i<LGradeNew.size();i++) {
					    Boolean conflict = false; 
			    		for(int j=0;j<LSignRecordHistory.size();j++) {
						    String ConflictDateTime = salesService.compare2GradeTime(LGradeNew.get(i).getGrade_seq(),LSignRecordHistory.get(j).getGrade_id());
						    if(!ConflictDateTime.equals("")) {
						    	conflict = true; //???????????????
						    }
						    
					    }
			    		//?????????????????????
			    		Boolean isPreGrade = false;
			    		for(int a=0;a<LPre_grade.size();a++) {
			    			if(LPre_grade.get(a).getGrade_id().equals(LGradeNew.get(i).getGrade_seq())){
			    				isPreGrade = true;
			    			}
			    		}
			    		
				        if(isPreGrade) {
				        	LGradeNew.get(i).setGradeConflict("-");
				        }else if(!conflict) { //?????????
						    LGradeNew.get(i).setGradeConflict("<input type='checkbox' onclick='checkTeacherSel(this)' style='zoom:1.6' name='gradeATime' value='"+LGradeNew.get(i).getGrade_seq()+"@"+LGradeNew.get(i).getClass_no()+"@1"+"@"+LGradeNew.get(i).getGradeNo2()+"@"+LGradeNew.get(i).getGradeNoATime()+"@"+LGradeNew.get(i).getTeacher_id()+"'>");
				        }else {
				        	LGradeNew.get(i).setGradeConflict("<img src='/images/cancel.jpg' height='15px'/>???????????????");
				        }
			  		
				        if(LGradeNew.get(i).getGradeName()!=null && !LGradeNew.get(i).getGradeName().isEmpty()) {
				        	gradeName = " ( "+LGradeNew.get(i).getGradeName()+" )";
				        }else {
				        	gradeName = "";
				        }
				        
			    		returnStr +=
			    		      "<div class='tr' style='font-size:small;padding:3px'>"+
			    				    "<div class='td2' style='width:70px;border-bottom:1px solid #dddddd;padding:5px;text-align:center;background-color:#eeffee'>"+LGradeNew.get(i).getSchool_name()+"</div>";           									
						returnStr +="<div class='td2' style='width:230px;text-align:left;border-bottom:1px solid #dddddd;background-color:#eeffee'>&nbsp;<A href='javascript:void(0)' onclick='javascript:openGrade("+LGradeNew.get(i).getGrade_seq()+")' style='text-decoration:underline;color:blue'>"+LGradeNew.get(i).getClass_start_date()+" "+LGradeNew.get(i).getSubject_name()+gradeName+"</A><b>"+LGradeNew.get(i).getVideo_date()+"</b></div>"; 			
						returnStr +=			 
			    				    "<div class='td2' style='width:50px;border-bottom:1px solid #dddddd;padding:5px;text-align:center;background-color:#eeffee'>"+LGradeNew.get(i).getSlot_name()+"</div>"+ 
			    				    "<div class='td2' style='width:150px;border-bottom:1px solid #dddddd;text-align:center;background-color:#eeffee'>"+LGradeNew.get(i).getTeacher_name()+" ("+LGradeNew.get(i).getGradeNo2()+"/"+LGradeNew.get(i).getGradeNoATime()+")</div>"+     						
			    				    "<div class='td2' style='width:80px;border-bottom:1px solid #dddddd;text-align:center;background-color:#eeffee'>"+LGradeNew.get(i).getAttendStatus()+"</div>"+ 
			    				    "<div class='td2' style='width:120px;border-bottom:1px solid #dddddd;text-align:center;background-color:#eeffee'>"+LGradeNew.get(i).getGradeConflict()+"</div>"+                                   
			    		      "</div>";  		
			    	}
			    	returnStr += "</div>";
			    	returnStr +="<div class='more1 child1' style='width:750px;text-align:right;padding-bottom:10px;"+style1+"'><A href='javascript:void(0)' onclick='javascript:GetMore(1)' style='font-size:small;font-weight:bold;letter-spacing:2px;color:blue'><i>??????20???&hellip;</i></A></div>";
			    	returnStr += "<div style='background-color:#ffffff;width:750px;height:5px'>&nbsp;</div>";
			    	returnStr += "<div id='more1'></div>";    	
			  }	
			  
//<2.A Video>--------------------------------------------------------------------------------------------------------------------	
			  if(section.equals("0") || section.equals("2")) {
			    	if(section.equals("0")) {
			    		returnStr += top_title_2;
			    	}else if(section.equals("2")) { //Video more....
			    		returnStr = "";
			    		style1="";
			    	}	
			    	
			    	returnStr += "<div class='css-table child1' style='border-spacing:1px;background-color:white;"+style1+"'>";
			    	for(int i=0;i<LGradeNewV.size();i++) {
			    		//???????????????
			    		Boolean isPreGrade = false;
			    		for(int a=0;a<LPre_grade.size();a++) {
			    			if(LPre_grade.get(a).getGrade_id().equals(LGradeNewV.get(i).getGrade_seq())){
			    				isPreGrade = true;
			    			}
			    		}
				        if(isPreGrade){
				        	//LGradeNewV.get(i).setGradeConflict("&check;&nbsp;??????????????????");
				        	LGradeNewV.get(i).setGradeConflict("<input type='checkbox' onclick='checkTeacherSel(this)' checked style='zoom:1.6' name='gradeATime' value='"+LGradeNewV.get(i).getGrade_seq()+"@"+LGradeNewV.get(i).getClass_no()+"@2"+"@"+LGradeNewV.get(i).getGradeNo2()+"@"+LGradeNewV.get(i).getGradeNoATime()+"@"+LGradeNewV.get(i).getTeacher_id()+"'><font color='darkblue'>?????????</font>");
				        }else {    		
				        	LGradeNewV.get(i).setGradeConflict("<input type='checkbox' onclick='checkTeacherSel(this)' style='zoom:1.6' name='gradeATime'  value='"+LGradeNewV.get(i).getGrade_seq()+"@"+LGradeNewV.get(i).getClass_no()+"@2"+"@"+LGradeNewV.get(i).getGradeNo2()+"@"+LGradeNewV.get(i).getGradeNoATime()+"@"+LGradeNewV.get(i).getTeacher_id()+"'>");
			    		}
				        
				        if(LGradeNewV.get(i).getGradeName()!=null && !LGradeNewV.get(i).getGradeName().isEmpty()) {
				        	gradeName = " ( "+LGradeNewV.get(i).getGradeName()+" )";
				        }else {
				        	gradeName = "";
				        }
				        
				        returnStr +=
			    		      "<div class='tr' style='font-size:small;padding:3px'>"+
			    				    "<div class='td2' style='width:70px;border-bottom:1px solid #dddddd;padding:5px;text-align:center'>"+LGradeNewV.get(i).getSchool_name()+"</div>";              											
						returnStr +="<div class='td2' style='width:230px;text-align:left;border-bottom:1px solid #dddddd'>&nbsp;<A href='javascript:void(0)' onclick='javascript:openGrade("+LGradeNewV.get(i).getGrade_seq()+")' style='text-decoration:underline;color:blue'>"+LGradeNewV.get(i).getClass_start_date()+" "+LGradeNewV.get(i).getSubject_name()+gradeName+"</A><b>"+LGradeNewV.get(i).getVideo_date()+"</b></div>";			
						returnStr +=			 
			    				    "<div class='td2' style='width:50px;border-bottom:1px solid #dddddd;padding:5px;text-align:center'>-</div>"+ 
			    				    "<div class='td2' style='width:150px;border-bottom:1px solid #dddddd;text-align:center'>"+LGradeNewV.get(i).getTeacher_name()+" ("+LGradeNewV.get(i).getGradeNo2()+"/"+LGradeNewV.get(i).getGradeNoATime()+")</div>"+    						
			    				    "<div class='td2' style='width:80px;border-bottom:1px solid #dddddd;text-align:center'>"+LGradeNewV.get(i).getAttendStatusV()+"</div>"+ 
			    				    "<div class='td2' style='width:120px;border-bottom:1px solid #dddddd;text-align:center'>"+LGradeNewV.get(i).getGradeConflict()+"</div>"+                                  
			    		      "</div>";  		
			    	}
			    	returnStr += "</div>";
			    	returnStr +="<div class='more2 child1' style='width:750px;text-align:right;padding-bottom:10px;"+style1+"'><A href='javascript:void(0)' onclick='javascript:GetMore(2)' style='font-size:small;font-weight:bold;letter-spacing:2px;color:blue'><i>??????20???&hellip;</i></A></div>";
			    	returnStr += "<div style='background-color:#ffffff;width:750px;height:5px'>&nbsp;</div>";
			    	returnStr += "<div id='more2'></div>";
			    	returnStr += "</div>";
			   }	
//<3.A ??????>--------------------------------------------------------------------------------------------------------------------
			   if(section.equals("0") || section.equals("3")) {	 	
			    	returnStr += "<div>&nbsp;</div>";
			    	returnStr +="<div><span style='margin-left:10px;background-color:tomato;color:white;padding:2px'><A href='javascript:void(0)' onclick='View3(this)' style='font-weight:bold;font-size:medium;color:white'>"+showIcon3+"</A></span><span style='border:1px #dddddd dashed;font-weight:bold;letter-spacing:10px;color:black;background-color:#ffefff;border-radius:5px'>&nbsp;??????</span></div>";
			    	returnStr +="<div style='margin-left:20px;background-color:#ffffff;width:800px' align='center'>";	
			    	returnStr += top_title_3;
			    	

			    	returnStr += "<div class='css-table child3' style='border-spacing:1px;background-color:white;"+style3+"'>";
			    	for(int i=0;i<LGradeNewC.size();i++) {
			    		//???????????????
			    		Boolean isPreGrade = false;
			    		for(int a=0;a<LPre_grade.size();a++) {
			    			if(LPre_grade.get(a).getGrade_id().equals(LGradeNewC.get(i).getGrade_seq())){
			    				isPreGrade = true;
			    			}
			    		}
				        if(isPreGrade){
				        	//LGradeNewV.get(i).setGradeConflict("&check;&nbsp;??????????????????");
				        	LGradeNewC.get(i).setGradeConflict("<input type='checkbox' onclick='checkTeacherSel(this)' checked style='zoom:1.6' name='gradeATime' value='"+LGradeNewC.get(i).getGrade_seq()+"@"+LGradeNewC.get(i).getClass_no()+"@3"+"@"+LGradeNewC.get(i).getGradeNo2()+"@"+LGradeNewC.get(i).getGradeNoATime()+"@"+LGradeNewC.get(i).getTeacher_id()+"'><font color='darkblue'>?????????</font>");
				        }else {    		
				        	LGradeNewC.get(i).setGradeConflict("<input type='checkbox' onclick='checkTeacherSel(this)' style='zoom:1.6' name='gradeATime'  value='"+LGradeNewC.get(i).getGrade_seq()+"@"+LGradeNewC.get(i).getClass_no()+"@3"+"@"+LGradeNewC.get(i).getGradeNo2()+"@"+LGradeNewC.get(i).getGradeNoATime()+"@"+LGradeNewC.get(i).getTeacher_id()+"'>");
			    		}
				        
				        if(LGradeNewC.get(i).getGradeName()!=null && !LGradeNewC.get(i).getGradeName().isEmpty()) {
				        	gradeName = " ( "+LGradeNewC.get(i).getGradeName()+" )";
				        }else {
				        	gradeName = "";
				        }
				        
				        returnStr +=
					    		"<div class='tr' style='font-size:small;padding:3px;background-color:#E9FFC6'>"+
					    			"<div class='td2' style='width:70px;border-bottom:1px solid #dddddd;padding:5px;text-align:center'>"+LGradeNewC.get(i).getSchool_name()+"</div>";              													
						returnStr +="<div class='td2' style='width:230px;text-align:left;border-bottom:1px solid #dddddd'>&nbsp;<A href='javascript:void(0)' onclick='javascript:openGrade("+LGradeNewC.get(i).getGrade_seq()+")' style='text-decoration:underline;color:blue'>"+LGradeNewC.get(i).getClass_start_date()+" "+LGradeNewC.get(i).getSubject_name()+gradeName+"</A><b>"+LGradeNewC.get(i).getVideo_date()+"</b></div>";			
						returnStr +=			 
			    				    "<div class='td2' style='width:50px;border-bottom:1px solid #dddddd;padding:5px;text-align:center'>-</div>"+ 
			    				    "<div class='td2' style='width:150px;border-bottom:1px solid #dddddd;text-align:center'>"+LGradeNewC.get(i).getTeacher_name()+" ("+LGradeNewC.get(i).getGradeNo2()+"/"+LGradeNewC.get(i).getGradeNoATime()+")</div>"+    						
			    				    "<div class='td2' style='width:80px;border-bottom:1px solid #dddddd;text-align:center'>"+LGradeNewC.get(i).getAttendStatusV()+"</div>"+ 
			    				    "<div class='td2' style='width:120px;border-bottom:1px solid #dddddd;text-align:center'>"+LGradeNewC.get(i).getGradeConflict()+"</div>"+                                  
			    		        "</div>"; 
			    	}	
					returnStr += "</div>";
			 
			    	returnStr += "</div>";
			    }
    	return returnStr;
 }
    
    
    @RequestMapping("/Sales/getMockVideoToSelect")
    @ResponseBody
    public String getMockVideoToSelect(HttpServletRequest request,Principal principal) {
    	String schoolCode = request.getParameter("schoolCode");
    	String subject_id = request.getParameter("subject_id");
    	String student_seq = request.getParameter("student_seq");

 
	
    	//??????????????????????????????
    	List<Grade> LGrade= courseService.getGradeList("",subject_id,"","","","","","","4,5","1","","","","","","","","1",""); 	

		   
//<????????????>---------------------------------------------------------------------------------------------------------------------			   
    	  String returnStr = 
    	  "<div class='css-table child1' style='text-align:center;border-spacing:1px;background-color:#F2F4FF'>"+ 
				"<div class='tr' style='background-color:#3e7e99;color:white;font-size:small;font-weight:bold'>"+
					"<div class='td2' style='width:70px;padding:0px;text-align:center;border-radius:2px'>????????????</div>"+
					"<div class='td2' style='width:300px;text-align:center;border-radius:2px'>??? ???</div>"+			
					"<div class='td2' style='width:120px;text-align:center;border-radius:2px'>??? ???</div>"+
					"<div class='td2' style='width:80px;text-align:center;border-radius:2px'>??? ???</div>"+	
					"<div class='td2' style='width:80px;text-align:center;border-radius:2px'>??? ??? ???</div>"+					
				"</div>";	
			    	
			for(int i=0;i<LGrade.size();i++) {			        
		        returnStr +=
	    		"<div class='tr' style='font-size:small;padding:3px'>"+
	    		    "<div class='td2' style='border-bottom:1px solid #dddddd;padding:5px;text-align:center;letter-spacing:0px'>"+LGrade.get(i).getSchool_name()+"</div>";              											
				returnStr +=
					"<div class='td2' style='text-align:left;border-bottom:1px solid #dddddd;letter-spacing:0px'>&nbsp;<A href='javascript:void(0)' onclick='javascript:openGrade("+LGrade.get(i).getGrade_seq()+")' style='text-decoration:underline;color:blue'>"+LGrade.get(i).getClass_start_date()+" "+LGrade.get(i).getSubject_name()+" ("+LGrade.get(i).getGradeName()+")</A><b>"+LGrade.get(i).getVideo_date()+"</b></div>";			
				returnStr +=			 
				    "<div class='td2' style='border-bottom:1px solid #dddddd;text-align:center;letter-spacing:0px'>"+LGrade.get(i).getTeacher_name()+"</div>"+    						
				    "<div class='td2' style='border-bottom:1px solid #dddddd;text-align:center;letter-spacing:0px'>"+LGrade.get(i).getAttendStatusV()+"</div>"+ 
				    "<div class='td2' style='border-bottom:1px solid #dddddd;text-align:center;letter-spacing:0px'>"+
				    	"<input type='checkbox' onclick='checkTeacherSel(this)' style='zoom:1.6' id='grade_seq'  value='"+LGrade.get(i).getGrade_seq()+"'>"+				    
				    "</div>"+                                  
		        "</div>";  		
			}
	      returnStr += "</div>";	

    	return returnStr;
 }    
   
    @RequestMapping("/Sales/getGradeToSelectJL")
    @ResponseBody
    public String getGradeToSelectJL(HttpServletRequest request,HttpSession session,Principal principal) {
    	String eip_grade_seq = request.getParameter("eip_grade_seq");
    	List<Grade> LGrade = courseService.getGradeList("","",eip_grade_seq,"","","","","","","1","","","","","","","","1","");
    	Grade grade = null;
    	if(LGrade.size()>0) {grade = LGrade.get(0);}
    	String returnStr = "";
   
    		returnStr +=
    		"<div class='tr' style='font-size:small;padding:3px'>"+
    				"<div class='td2' style='width:70px;border-bottom:1px solid #dddddd;padding:5px;text-align:center'>"+grade.getSchool_name()+"</div>"+ 
    				"<div class='td2' style='width:70px;border-bottom:1px solid #dddddd;text-align:center'><img src='/images/teacher.png' height='13px'/> ??????</div>"+            							
					"<div class='td2' style='width:230px;text-align:center;border-bottom:1px solid #dddddd'>"+grade.getClass_start_date()+grade.getSubject_name()+" "+grade.getGradeName()+"<br>"+grade.getVideo_date()+"</div>"+			 
    				"<div class='td2' style='width:50px;border-bottom:1px solid #dddddd;padding:5px;text-align:center'>"+grade.getSlot_name()+"</div>"+ 
    				"<div class='td2' style='width:100px;border-bottom:1px solid #dddddd;text-align:center'>"+grade.getTeacher_name()+"</div>"+ 
    				"<div class='td2' style='width:80px;border-bottom:1px solid #dddddd;text-align:center'></div>"+
    				"<div class='td2' style='width:100px;border-bottom:1px solid #dddddd;text-align:center'></div>"+
    				"<div class='td2' style='width:100px;border-bottom:1px solid #dddddd;text-align:center'>"+
    				    "<input type='checkbox' onclick='checkTeacherSel(this)' name='gradeATime'  value='"+grade.getGrade_seq()+"@"+grade.getClass_no()+"@1"+"@"+grade.getGradeNo2()+"@"+grade.getGradeNoATime()+"@"+grade.getTeacher_id()+"'>?????????"+
    				"</div>"+
    				"<div class='td2' style='width:50px;border-bottom:1px solid #dddddd;text-align:center'><A href='javascript:void(0)' onclick='javascript:openGrade("+grade.getGrade_seq()+")' style='font-size:large'>&hellip;</A></div>"+                                   
    		"</div>"; 
    		
    		returnStr +=
    		"<div class='tr' style='font-size:small;padding:3px'>"+
    				"<div class='td2' style='width:70px;border-bottom:1px solid #dddddd;padding:5px;text-align:center'>"+grade.getSchool_name()+"</div>"+ 
    				"<div class='td2' style='width:70px;border-bottom:1px solid #dddddd;text-align:center'><img src='/images/earphone.png' height='8px'/> ??????</div>"+            							
					"<div class='td2' style='width:230px;text-align:center;border-bottom:1px solid #dddddd'>"+grade.getClass_start_date()+grade.getSubject_name()+" "+grade.getGradeName()+"<br>"+grade.getVideo_date()+"</div>"+			 
    				"<div class='td2' style='width:50px;border-bottom:1px solid #dddddd;padding:5px;text-align:center'>"+grade.getSlot_name()+"</div>"+ 
    				"<div class='td2' style='width:100px;border-bottom:1px solid #dddddd;text-align:center'>"+grade.getTeacher_name()+"</div>"+ 
    				"<div class='td2' style='width:80px;border-bottom:1px solid #dddddd;text-align:center'></div>"+ 
    				"<div class='td2' style='width:100px;border-bottom:1px solid #dddddd;text-align:center'></div>"+
    				"<div class='td2' style='width:100px;border-bottom:1px solid #dddddd;text-align:center'>"+
    				    "<input type='checkbox' onclick='checkTeacherSel(this)' name='gradeATime'  value='"+grade.getGrade_seq()+"@"+grade.getClass_no()+"@2"+"@"+grade.getGradeNo2()+"@"+grade.getGradeNoATime()+"@"+grade.getTeacher_id()+"'>?????????"+
    				"</div>"+
    				"<div class='td2' style='width:50px;border-bottom:1px solid #dddddd;text-align:center'><A href='javascript:void(0)' onclick='javascript:openGrade("+grade.getGrade_seq()+")' style='font-size:large'>&hellip;</A></div>"+                                   
    		"</div>";     		
    	return returnStr;    	
    }
    
    @RequestMapping("/Sales/openGradeToSelect")
    public String openGradeToSelect(Model model,HttpServletRequest request,Principal principal) {
    	String subject_id = request.getParameter("subject_id");
    	String student_seq = request.getParameter("student_seq");
    	String gradeNoLeft = request.getParameter("gradeNoLeft");
    	model.addAttribute("gradeNoLeft",gradeNoLeft);
    	String teacher_id = request.getParameter("teacher_id");
    	model.addAttribute("teacher_id",teacher_id);
    	String class_style = request.getParameter("class_style");
    	model.addAttribute("class_style",class_style);    	
    	//????????????
    	List<School> LSchool = accountService.getSchool("","");
    	String SchoolCode = accountService.getAccountByID("",principal.getName()).getSchool_code();
    	String schoolStr = "";
        for(int i=0;i<LSchool.size();i++) {
        	String sel = "";
        	if(LSchool.get(i).getCode().equals(SchoolCode)) {sel = "selected";}
        	schoolStr += "<option value='"+LSchool.get(i).getCode()+"' "+sel+">"+LSchool.get(i).getName()+"</option>";
        }
        model.addAttribute("schoolStr",schoolStr);
        

		List<Register_comboSale> LRegister_comboSale = salesService.getComboSaleByRegister((String)request.getParameter("Register_comboSale_seq"),"","","","","1",false,false,"");
		String parent_subject_id = LRegister_comboSale.get(0).getSubject_id_virtual();    	

    	model.addAttribute("Register_comboSale_seq",request.getParameter("Register_comboSale_seq"));
		model.addAttribute("Register_seq",request.getParameter("Register_seq"));
		model.addAttribute("comboSale_seq",request.getParameter("comboSale_seq"));
		model.addAttribute("subject_id",subject_id );
		model.addAttribute("student_seq",student_seq );

		if(parent_subject_id !=null && !parent_subject_id.isEmpty()) {
			model.addAttribute("parent_subject_id",parent_subject_id);
		}else {
			model.addAttribute("parent_subject_id","");
		}
		
		List<SubjectTeacher> LSubjectTeacher =  courseService.getSubjectTeacher(subject_id);
		model.addAttribute("LSubjectTeacher",LSubjectTeacher);
		
        return "/Sales/openGradeToSelect";
 }
    
    @RequestMapping("/Sales/openMockVideoToSelect")
    public String openMockVideoToSelect(Model model,HttpServletRequest request,Principal principal) {
    	String Register_seq = request.getParameter("Register_seq");
    	String subject_id = request.getParameter("subject_id");
    	String student_seq = request.getParameter("student_seq");
    	String register_mock_seq = request.getParameter("register_mock_seq");
    	String mockVideo_id = request.getParameter("mockVideo_id");
    	model.addAttribute("Register_seq",Register_seq);
		model.addAttribute("subject_id",subject_id);
		model.addAttribute("student_seq",student_seq);
		model.addAttribute("register_mock_seq",register_mock_seq);
		model.addAttribute("mockVideo_id",mockVideo_id); 
	
    	//??????????????????
    	List<School> LSchool = accountService.getSchool("","");
    	String SchoolCode = accountService.getAccountByID("",principal.getName()).getSchool_code();
    	String schoolStr = "";
        for(int i=0;i<LSchool.size();i++) {
        	String sel = "";
        	if(LSchool.get(i).getCode().equals(SchoolCode)) {sel = "selected";}
        	schoolStr += "<option value='"+LSchool.get(i).getCode()+"' "+sel+">"+LSchool.get(i).getName()+"</option>";
        }
        model.addAttribute("schoolStr",schoolStr);
        
        return "/Sales/openMockVideoToSelect";
 }    
    
    @RequestMapping("/Sales/openGradeToSelectJL")    
    public String openGradeToSelectJL(Model model,HttpServletRequest request,HttpSession session,Principal principal) {
    	String eip_grade_seq = request.getParameter("eip_grade_seq"); //EIP??????
    	String gradeId = request.getParameter("gradeId"); //????????????

    	model.addAttribute("eip_grade_seq",eip_grade_seq);
		model.addAttribute("gradeId",gradeId);		
        return "/Sales/openGradeToSelectJL";
 }     
    
    @RequestMapping("/Sales/openSubjectToSelect")
    public String openSubjectToSelect(Model model,HttpServletRequest request) {
    	String costShare = request.getParameter("costShare");
    	String subject_id = request.getParameter("subject_id");
    	String Register_seq = request.getParameter("Register_seq");
    	String comboSale_seq = request.getParameter("comboSale_seq");
    	String viewSide = request.getParameter("viewSide");
    	String Register_comboSale_seq = request.getParameter("Register_comboSale_seq");    	
    	List<VirtualSubject> LVirtualSubject = salesService.getVirtualSubjectForStudent(subject_id);
    	List<InterestSubject> LInterestSubject = salesService.getInterestSubject(Register_comboSale_seq);
    	String radioStr="";
    	String checkStr = "";
    	for(int i=0;i<LVirtualSubject.size();i++) {
    		checkStr = "";
    		for(int j=0;j<LInterestSubject.size();j++) {
    			if(LVirtualSubject.get(i).getChild_subject_id().equals(LInterestSubject.get(j).getChild_subject_id())) {
    				checkStr = "checked";
    			}
    		}
    		if(viewSide!=null && viewSide.equals("right")) {
    			radioStr +=
    					"<div class='tr' style='font-size:small;font-weight:bold'>"+
    							"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:120px;text-align:center'>"+LVirtualSubject.get(i).getChild_category_name()+"</div>"+
    							"<div class='td2' style='border-bottom:1px #eeeeee solid;width:220px;text-align:center'>"+LVirtualSubject.get(i).getChild_subject_name()+"</div>"+
    							"<div class='td2' style='border-bottom:1px #eeeeee solid;width:100px;text-align:center;border-radius:2px'>"+LVirtualSubject.get(i).getSale_price()+"</div>"+
    							"<div class='td2' style='border-bottom:1px #eeeeee solid;width:100px;text-align:center;border-radius:2px'><A href='javascript:void(0)' style='text-decoration:underline;letter-spacing:2px' onclick='SaveSubjectSelect("+Register_comboSale_seq+","+Register_seq+","+comboSale_seq+","+LVirtualSubject.get(i).getSubject_id()+","+LVirtualSubject.get(i).getChild_subject_id()+","+costShare+")'>????????????</A> </b></div>"+
    					"</div>";
    		}else {
        		radioStr +=
        	    		"<div class='tr' style='font-size:small'>"+
        	    				"<div class='td2' style='border-bottom:1px #eeeeee solid;width:110px;padding:2px;text-align:center;border-radius:2px'><input type='checkbox' name='child_subject_id' id='child_subject_id' value='"+LVirtualSubject.get(i).getChild_subject_id()+"' "+checkStr+"></div>"+
        	    				"<div class='td2' style='border-bottom:1px #eeeeee solid;width:120px;border-radius:2px'>"+LVirtualSubject.get(i).getChild_category_name()+"</div>"+
        	    				"<div class='td2' style='border-bottom:1px #eeeeee solid;width:220px;border-radius:2px'>"+LVirtualSubject.get(i).getChild_subject_name()+"</div>"+
        	    				"<div class='td2' style='border-bottom:1px #eeeeee solid;width:100px;text-align:center;border-radius:2px'>"+LVirtualSubject.get(i).getOrigin_price()+"</div>"+
        	    				"<div class='td2' style='border-bottom:1px #eeeeee  solid;width:100px;text-align:center;border-radius:2px'>"+LVirtualSubject.get(i).getSale_price()+"</div>"+
        	    		"</div>";    			
    		}
    	}
    	model.addAttribute("viewSide",viewSide);
    	model.addAttribute("radioStr",radioStr);
    	model.addAttribute("comboSale_seq",comboSale_seq);
    	model.addAttribute("Register_comboSale_seq",Register_comboSale_seq);
    	return "/Sales/openSubjectToSelect";
    }

    @RequestMapping("/Sales/SaveSubjectSelect")
    @ResponseBody
    public String SaveSubjectSelect(HttpServletRequest request) {
    	
    	String Register_comboSale_seq = request.getParameter("Register_comboSale_seq");
    	String Register_id  = request.getParameter("Register_id");
    	String comboSale_id  = request.getParameter("comboSale_id");
    	String subject_id  = request.getParameter("subject_id");
    	String subject_id_virtual  = request.getParameter("subject_id_virtual");
    	String costShare = request.getParameter("costShare");

 	    salesService.SaveRegister_comboSale(Register_comboSale_seq,Register_id,comboSale_id,subject_id,subject_id_virtual,costShare);

 	    return "successful";
    }
    
    @RequestMapping("/Sales/set_grade")
    @ResponseBody
    public String set_grade(Model model,HttpServletRequest request,Principal principal) {
    	String Register_seq = request.getParameter("Register_seq");
    	String Register_comboSale_seq  = request.getParameter("Register_comboSale_seq");
 	    String subject_id    = request.getParameter("subject_id");
 	    String parent_subject_id  = request.getParameter("parent_subject_id");
 	    String student_seq = request.getParameter("student_seq");
 	    String updater = principal.getName();
 	    String gradeATimeStr = request.getParameter("gradeATimeStr");
 	    String gradeNo2 = request.getParameter("gradeNo2");
 	    String schoolCode = request.getParameter("schoolCode");
 	    String experience_content = "";
 	    String[] tmp1 = gradeATimeStr.split("\\^");
 	    for(int a=0;a<tmp1.length;a++) {
 	    	String[] tmp2 = tmp1[a].split("\\@");
 	    	String grade_seq = tmp2[0];
 	    	String class_no = tmp2[1];
 	    	String class_style = tmp2[2];
 	    	salesService.BookGradeSave(Register_seq,Register_comboSale_seq,grade_seq,subject_id,class_no,student_seq,updater,class_style,parent_subject_id,schoolCode,gradeNo2);
 	    	Grade grade = courseService.getGrade(grade_seq,"","","","","","","","","","","","").get(0);
 	    	experience_content += grade.getGrade_date()+grade.getSubject_name()+(grade.getGradeName()==null?"":grade.getGradeName())+"<br>";
 	    }
 	    //????????????		
 	    String experience_id = "2";
 	    salesService.insertStudentExperience(student_seq,schoolCode,experience_id,"",experience_content,"",updater,Register_seq); 
 	    return "successful";
    }
    
    @RequestMapping("/Sales/set_mock")
    @ResponseBody
    public String set_mock(Model model,HttpServletRequest request,Principal principal) {
    	String Register_seq = request.getParameter("Register_seq");
    	String student_id = request.getParameter("student_seq");
    	String school_code = request.getParameter("schoolCode");
    	String grade_id = request.getParameter("grade_seq");
    	String register_mock_id = request.getParameter("register_mock_seq");
    	String mockVideo_id = request.getParameter("mockVideo_id");

 	    //????????????????????????
    	salesService.MockVideoSave(student_id,school_code,grade_id,register_mock_id,principal.getName(),mockVideo_id);
 	    
 	    //????????????
 	    String experience_id = "12"; //????????????
 	    salesService.insertStudentExperience(student_id,school_code,experience_id,"","","",principal.getName(),Register_seq);  
 	    return "successful";
    }    
    
    @RequestMapping("/sales/getEvent")
    @ResponseBody
    public String getEvent(Model model,HttpSession session,HttpServletRequest request) {
    	String school_code    = request.getParameter("school_code");
    	String student_id     = request.getParameter("student_id");

    	String jsonMsg = null;
        List<Event> events = new ArrayList<Event>();
     
    	//mock??????
  		List<MockBaseBook2> LMockBaseBook2 = salesService.getMockBaseBook(student_id,"1","","","","","","","","");   	      
  		for(int j=0;j<LMockBaseBook2.size();j++) {
  			Event event = new Event();  
  			event.setTitle("");
  			event.setTitle("<div style='padding:1px;background-color:lightyellow;color:blue;font-weight:bold'>??????: "+LMockBaseBook2.get(j).getMockSubjectName()+"</div>"); 

  				event.setDescription(
  					"<div style='padding:1px;background-color:lightyellow;font-weight:bold'>"+LMockBaseBook2.get(j).getMockParaName()+"</div>"+
  					"<div style='padding:1px;background-color:lightyellow;font-weight:bold'>"+LMockBaseBook2.get(j).getPanelName()+"</div>"
  				);
	
  			event.setStart(LMockBaseBook2.get(j).getSetDate());
  			events.add(event);   	                	    	
        }        	  

    	//mock????????????
  		List<MockVideoHistory> LMockVideoHistory = courseService.getMockVideoHistory("","1","","(1)",student_id,"","","","","");
  		for(int j=0;j<LMockVideoHistory.size();j++) {
  			Event event = new Event();  
  			event.setTitle("");
  			event.setTitle("<div style='padding:1px;background-color:lightyellow;color:blue;font-weight:bold'>????????????:</div>"); 
  			event.setDescription("<div style='padding:1px;background-color:lightyellow;color:blue;font-weight:bold'>"+LMockVideoHistory.get(j).getMockVideo_name()+"</div>");        		    	
  			event.setStart(LMockVideoHistory.get(j).getAttend_date());
  			events.add(event);   	                	    	
        } 

   
      //??????
      List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory2("","","","","","1","(1)","(0,1,-1)","",student_id,"","","","","","","","");
      
      for(int i=0;i<LSignRecordHistory.size();i++) {
    	//????????????,??????????????????,??????Video??????
    	  if(    
    			  LSignRecordHistory.get(i).getActive().equals("1") &&  //????????????
    			  !LSignRecordHistory.get(i).getAttend().equals("2") && //??????????????????
    			  (
    				LSignRecordHistory.get(i).getClass_style().equals("1") || //??????
    				(LSignRecordHistory.get(i).getClass_style().equals("2") && !LSignRecordHistory.get(i).getAttend_date().isEmpty()) || //video????????????
    				(LSignRecordHistory.get(i).getClass_style().equals("3") && !LSignRecordHistory.get(i).getAttend_date().isEmpty())) //???????????????
    			  ) { 
    		  
			    String slot_name = "";
			    if(LSignRecordHistory.get(i).getClass_style().equals("2")) {
        		    switch(LSignRecordHistory.get(i).getSlot()) { 
		            	case "1":slot_name = "(Video???)";break;
		            	case "2":slot_name = "(Video???)";break;
		            	case "3":slot_name = "(Video???)";break; 
			        }
			    }else if(LSignRecordHistory.get(i).getClass_style().equals("3")) {
        		    switch(LSignRecordHistory.get(i).getSlot()) { 
		            	case "1":slot_name = "(?????????)";break;
		            	case "2":slot_name = "(?????????)";break;
		            	case "3":slot_name = "(?????????)";break; 
		            }
		        }
	  		    
     		    Event event = new Event(); 
     		    event.setTitle("<div style='font-weight:bold;background-color:#ffefff;text-align:center'>&nbsp;"+LSignRecordHistory.get(i).getClass_start_date()+" "+LSignRecordHistory.get(i).getSubject_name()+"</div>");
     		    if(Integer.valueOf(LSignRecordHistory.get(i).getClass_th())<0){
     		    	event.setDescription("<div style='background-color:#ffefff;text-align:center;color:red'>??????</div>");
     		    }else {       		    	
     		    	event.setDescription("<div style='background-color:#ffefff;text-align:center'>???"+LSignRecordHistory.get(i).getClass_th()+"??? "+slot_name+"</div>");         		    	
     		    }
     		    	if(!LSignRecordHistory.get(i).getAttend_date().equals("")) {
          			event.setStart(LSignRecordHistory.get(i).getAttend_date());
          		  }else {
          			event.setStart(LSignRecordHistory.get(i).getClass_date());  
          		  }
     		    
     		    events.add(event);         		  
 
    	  }
      }

	      //???????????? apply to every students
	      List<Suspension> LSuspension = admService.getSuspension("");   	      
	      for(int i=0;i<LSuspension.size();i++) { 	    
	    	    Event event = new Event();  
	    	    event.setTitle("<div style='padding:10px;letter-spacing:2px;background-color:hotpink;text-align:center;font-weight:bold;color:white'>"+LSuspension.get(i).getReason()+"</div>");
	            event.setStart(LSuspension.get(i).getSuspension_date());
	            events.add(event);  	                	    	
	      } 
	      
          ObjectMapper mapper = new ObjectMapper();
          try {
			jsonMsg =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

      
        return jsonMsg; 
    }
    
    @RequestMapping("/sales/getEventTeacher")
    @ResponseBody
    public String getEventTeacher(Model model,HttpServletRequest request,HttpSession session) {
    	String teacher_id = (String)session.getAttribute("teacher_id_hidden");
        List<NoClass> LNoClass = salesService.getNoClass(teacher_id,"","");  
        
    	String jsonMsg = null;
        List<Event> events = new ArrayList<Event>();
        
        try {	
		    for(int j=0;j<LNoClass.size();j++) { 	    
	    	    Event event = new Event();  
	    	    if(LNoClass.get(j).getNoClassType().equals("1")) {
	    	    	event.setTitle("<div style='background-color:lightcoral;color:white'><span style='background-color:white'><img src='/images/no1.png' height='15px'/></span>????????????("+LNoClass.get(j).getTimeFrom()+"~"+LNoClass.get(j).getTimeTo()+")</div>");
	    	    }else if(LNoClass.get(j).getNoClassType().equals("2")) {
	    	    	event.setTitle("<div style='background-color:indianred;color:white'><span style='background-color:white'><img src='/images/no2.png' height='15px'/></span>?????????("+LNoClass.get(j).getTimeFrom()+"~"+LNoClass.get(j).getTimeTo()+")</div>");
	    	    }	
	    	    event.setStart(LNoClass.get(j).getDateSel());
	            events.add(event);   	                	    	
	        } 
  		  //????????????      
  		  List<Classes> Lclasses = courseService.getClasses("",teacher_id,"","","","","","","","");   	      
  	      for(int j=0;j<Lclasses.size();j++) {
  	    	    String school_name = courseService.getGradeList("","",Lclasses.get(j).getGrade_id(),"","","","","","","1","","","","","","","","1","").get(0).getSchool_name();
  	    	    Event event = new Event();  
  	    	    event.setTitle("<div style='background-color:khaki;text-align:center;font-weight:bold'>"+Lclasses.get(j).getSubject_name()+" "+Lclasses.get(j).getClass_start_date()+" ???</div>");
  	            event.setDescription("<div style='background-color:khaki;text-align:center'>"+school_name+" ("+(Lclasses.get(j).getClass_th())+"/"+Lclasses.get(j).getClass_no()+") "+Lclasses.get(j).getTime_from()+"~"+Lclasses.get(j).getTime_to()+"</div>");
  	            event.setStart(Lclasses.get(j).getClass_date());
  	            events.add(event);   	                	    	
  	      }        	

	      //???????????? apply to every students
	      List<Suspension> LSuspension = admService.getSuspension("");   	      
	      for(int i=0;i<LSuspension.size();i++) { 	    
	    	    Event event = new Event();  
	    	    event.setTitle("<div style='padding:10px;letter-spacing:2px;background-color:hotpink;text-align:center;font-weight:bold;color:white'>"+LSuspension.get(i).getReason()+"</div>");
	            event.setStart(LSuspension.get(i).getSuspension_date());
	            events.add(event);  	                	    	
	      } 
	      
          ObjectMapper mapper = new ObjectMapper();
          jsonMsg =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
        return jsonMsg; 
    }
    
    @RequestMapping("/sales/getEventLecture")
    @ResponseBody
    public String getEventLecture(Model model,HttpServletRequest request,HttpSession session) {
    	String jsonMsg = null;
        List<Event> events = new ArrayList<Event>();
        
        try {
          //?????????
          List<Classes> LClasses = courseService.getClasses("","","","","","","1","","","");
          for(int i=0;i<LClasses.size();i++) {
        	  //if(LClasses.get(i).getClass_th().equals("1")) {
        		  Event event = new Event();
        		  event.setTitle("<div style='background-color:khaki;text-align:center;font-weight:bold;font-size:x-small'>"+LClasses.get(i).getClass_start_date()+" "+LClasses.get(i).getSubject_name()+"</div>");
    	    	    event.setDescription(
      	    	    		"<div style='background-color:#7ba6b6;text-align:center;font-size:x-small;color:white'>("+LClasses.get(i).getSchool_name()+") "+LClasses.get(i).getTime_from()+"~"+LClasses.get(i).getTime_to()+"</div>"+
      	    	    		"<div style='background-color:#7ba6b6;text-align:center;font-size:x-small;color:white'>???1??? "+LClasses.get(i).getTeacher_name()+"??????"+"</div>"
      	    	    );
      	            event.setStart(LClasses.get(i).getClass_date());
      	            events.add(event);       	    	    
        	  //}
          }
          
      	
  		  //??????      
          List<Lecture> LLecture = marketingService.getLecture("","","","1","","");
      
  	      for(int j=0;j<LLecture.size();j++) {
  	    	    Event event = new Event();  
  	    	    event.setTitle("<div style='background-color:tan;text-align:center;font-weight:bold'>"+LLecture.get(j).getLectureName()+"</div>");
  	    	    if(LLecture.get(j).getSchool_name()==null) {LLecture.get(j).setSchool_name("");}
  	    	    event.setDescription(
  	    	    		"<div style='background-color:tan;text-align:center;'>("+LLecture.get(j).getSchool_name()+LLecture.get(j).getLocation()+") "+LLecture.get(j).getLectureTimeFrom()+"~"+LLecture.get(j).getLectureTimeTo()+"</div>"+
  	    	    		"<div style='background-color:tan;text-align:center;'>"+LLecture.get(j).getTeacher_name()+"??????"+"</div>"
  	    	    );
  	            event.setStart(LLecture.get(j).getLectureDate());
  	            events.add(event);   	                	    	
  	      }        	

	      //???????????? 
	      List<Suspension> LSuspension = admService.getSuspension("");   	      
	      for(int i=0;i<LSuspension.size();i++) { 	    
	    	    Event event = new Event();  
	    	    event.setTitle("<div style='padding:10px;letter-spacing:2px;background-color:hotpink;text-align:center;font-weight:bold;color:white'>"+LSuspension.get(i).getReason()+"</div>");
	            event.setStart(LSuspension.get(i).getSuspension_date());
	            events.add(event);   	                	    	
	      } 
	      
          ObjectMapper mapper = new ObjectMapper();
          jsonMsg =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
        return jsonMsg; 
    }  
    
	@RequestMapping("/Sales/Book_attend")
	public String Book_attend(Model model,HttpServletRequest request) throws ParseException {
		String student_seq = request.getParameter("student_seq");
		String grade_id = request.getParameter("grade_id");
		if(student_seq==null || student_seq.isEmpty()) {
			return "/Sales/Student"; 
		}else {    	
			Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0);
	    	model.addAttribute("student_seq",student_seq);
	    	model.addAttribute("grade_id",grade_id);
			model.addAttribute("ch_name",student.getCh_name());
			model.addAttribute("student_no",student.getStudent_no()); 
			model.addAttribute("makeUpTotal",student.getMakeUpTotal());  
	 	    return "/Sales/Book_attend";
		}    	
	}

    @RequestMapping("/Sales/getBook_attend")
    @ResponseBody
    public String getBook_attend(Model model,HttpServletRequest request) {
    	String student_seq = request.getParameter("student_seq");
    	String grade_id = request.getParameter("grade_id"); 
    	
    	//Video??????????????????
    	if(request.getParameter("ch_name")!=null || request.getParameter("student_no")!=null) {
			List<Student> LStudent = salesService.getStudent("",request.getParameter("ch_name"),"",request.getParameter("student_no"),"","","","","","");
			if(LStudent.size()>0) {
				student_seq = LStudent.get(0).getStudent_seq();
			}else {
				return "/Adm/TodayBook";
			}    	
    	}
    	
        //????????????_??????????????? + ??????
    	List<Register_comboSale> LRegister_comboSale = salesService.getComboSaleByRegister("","",student_seq,"","","1",false,false,"");

    	//????????????????????????
    	List<Grade> LGrade = new ArrayList<Grade>();
   	
	    for(int i=0;i<LRegister_comboSale.size();i++) {    	
			List<Register_comboSale_grade> LRegister_comboSale_grade = courseService.getRegister_comboSale_grade(LRegister_comboSale.get(i).getRegister_comboSale_seq(),"","(1,2,3)","");	
			for(int k=0;k<LRegister_comboSale_grade.size();k++) {
			  String register_comboSale_grade_statusStr = "";	
			  if(LRegister_comboSale_grade.get(k).getRegister_status().equals("2")) {
				  register_comboSale_grade_statusStr = "<span style='font-size:x-small;color:red'><img src='/images/cancel.jpg' height='11px'/>??? ???</span>"; 
			  }else if(LRegister_comboSale_grade.get(k).getRegister_status().equals("3")) {
				  register_comboSale_grade_statusStr = "<span style='font-size:x-small;color:red'><img src='/images/cancel.jpg' height='11px'/>??? ???</span>"; 
			  } 
	    	 
			  List<Grade> LGrade0 = courseService.getGradeList("","",LRegister_comboSale_grade.get(k).getGrade_id(),"","","","","","","1","","","","","","","","1","");
	//+-??????
			  if(LGrade0.size()>0) {
	    			Grade grade = LGrade0.get(0);
	    			if(LRegister_comboSale_grade.get(k).getClass_style().equals("1")) {
	    				grade.setClass_style_img("<img src='/images/teacher.png' height='14px'/>");
	    			}else if(LRegister_comboSale_grade.get(k).getClass_style().equals("2")) {
	    				grade.setClass_style_img("<img src='/images/earphone.png' height='14px'/>");
	    			}else if(LRegister_comboSale_grade.get(k).getClass_style().equals("3")) {
	    				grade.setClass_style_img("<img src='/images/cloud.png' height='14px'/>");
	    			}	
	    			grade.setRegister_comboSale_grade_statusStr(register_comboSale_grade_statusStr); 
	    			//??????????????????
	    			List<Pre_grade> LpreGrade = salesService.getPre_grade("",grade.getGrade_seq());
	    			if(LpreGrade.size()>0) {
	    				grade.setPre_grade_remark(LpreGrade.get(0).getGrade_remark());
	    			}	
	    			grade.setSchool_name(LRegister_comboSale_grade.get(k).getSchool_name()); //??????????????????
	    			grade.setSchool_code(LRegister_comboSale_grade.get(k).getSchool_code());
				    List<Classes> LClasses = courseService.getClasses(LRegister_comboSale_grade.get(k).getGrade_id(),"","","","","","","","","");
				    //????????????	
	    			List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("",student_seq,"","1","(1)","",LRegister_comboSale_grade.get(k).getRegister_comboSale_grade_seq(),"","","","");
 			
	    			String attendImg = "";
	    			StringBuilder tmp = new StringBuilder("");
	    			String ymd = ""; //?????????
	    			String wk =""; //??????
	    			String hm = "";//??????
	    			String bookDate = "";//??????????????????
	    			String signRecordHistory_seq = "";
	    			String slot_name = "";
	    			List<String> Ltmp = new ArrayList<String>();
	    			
	    		for(int j=0;j<LClasses.size();j++) {    			
	    			attendImg = "WhiteSquare.png";//?????????
	        		if(LClasses.get(j).getClass_style().equals("2")){
						attendImg = "White_repeat.png";						
						bookDate="-";
	        		}else if(LClasses.get(j).getClass_style().equals("3")){
						attendImg = "WhiteCloud.png";
						bookDate="-";	
	         		}else { 
	         			if(LClasses.get(j).getClass_date().length()>9) {
		         			wk = DateToWeek.getDateToWeek(LClasses.get(j).getClass_date());
	         				if(LClasses.get(j).getTime_from().length()==4) {
	         					hm = LClasses.get(j).getTime_from().substring(0,2)+":"+LClasses.get(j).getTime_from().substring(2,4);
	         				}else {
	         					hm = "";
	         				}
	         				ymd = LClasses.get(j).getClass_date();    					
	         				bookDate = ymd;
	         			}
	         		}
	        		
	
	        		
	        		String makeUpNo = "";
	        		int allowFreeAttend = 0; 
	        		String remark = ""; //???????????????
	        		String attend_date = "";
	        		String attendDateCancel = ""; //????????????????????????
	        		Boolean futureClass = false; //??????????????????, ?????????????????????Video??????
	        		String attend = "";
	        		String bgcolor = "#ffffff";
	        		String signRecordHistory_classStyle = "";
	        		String attendReal = ""; //???????????????
	      		
	        		for(int x=0;x<LSignRecordHistory.size();x++) {//????????????
	  			
	        			slot_name = "";
	        			if(LClasses.get(j).getClass_th().equals(LSignRecordHistory.get(x).getClass_th())) { 
	        						
	        				signRecordHistory_classStyle = LSignRecordHistory.get(x).getClass_style();
	        				attend =  LSignRecordHistory.get(x).getAttend();
	        				if(LSignRecordHistory.get(x).getAllowAttend().equals("1")){
	        					bgcolor = "#F7FE8D";
	        				}
	        				if(attend.equals("0")) { //????????????
	        					if(LSignRecordHistory.get(x).getAttend_date()!=null && LSignRecordHistory.get(x).getAttend_date().length()==10) {
	        						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	        						String current = sdf.format(new Date());
		        					//???????????????????????????(???)???????????????
		        					if(LSignRecordHistory.get(x).getAttend_date().length()==10) {
		        						String attend_dateBook = LSignRecordHistory.get(x).getAttend_date().substring(6,10)+LSignRecordHistory.get(x).getAttend_date().substring(0,2)+LSignRecordHistory.get(x).getAttend_date().substring(3,5);
		        						if(Integer.valueOf(current) <= Integer.valueOf(attend_dateBook)) {
		        							attendDateCancel = "1";
		        						}else {
		        							attendDateCancel = "0";
		        						}
		        					}else { attendDateCancel = "0";}
	        					}	
	        				}
	        				
	        				signRecordHistory_seq = LSignRecordHistory.get(x).getSignRecordHistory_seq();
	        				makeUpNo = LSignRecordHistory.get(x).getMakeUpNo(); //????????????(?????????)?????????
	        				
	        				       			
	        				int free_makeUpNo = 1;
	        				Grade grade2 = courseService.getGrade2(LSignRecordHistory.get(x).getGrade_id(),"","","","","","","","").get(0);
	        				List<Subject> LSubject = courseService.getSubject("",grade2.getSubject_id(),"","","","0");
	        				
	        				//free_makeUpNo : ?????????????????????????????????
	        				if(LSubject.size()>0) {free_makeUpNo = Integer.valueOf(LSubject.get(0).getFree_makeUpNo());}
	        				
	        				//attend : 0??????,1??????,-1??????,2 ?????????????????? //allowFreeAttend : ??????????????????????????? //makeUpNo : ?????????????????????
	        				
	        				
	        				//*****A:???????????????*****//
	        				if(!LSignRecordHistory.get(x).getAttend().equals("2")) {
	        					
	        					//?????????????????????????????????Real or Video
	        					Boolean everAttend = salesService.EverAttend(student_seq,LSignRecordHistory.get(x).getRegister_comboSale_grade_id(),LClasses.get(j).getClass_th());
	        					if(everAttend) {
	        						attendReal = "(??????)";
	        					}else{
	        						//if(!LSignRecordHistory.get(x).getClass_style().equals("2")) {
	        							attendReal = "(??????)";
	        						//}	
	        					}
	        					
	        					//????????????????????????????????????????????????
	        					Boolean firstRealAttend = salesService.firstRealAttend(student_seq,LSignRecordHistory.get(x).getRegister_comboSale_grade_id(),LClasses.get(j).getClass_th());
	        					if(firstRealAttend){          						
			        					allowFreeAttend = free_makeUpNo-Integer.valueOf(makeUpNo)-1;
			        			}else{			        				
			        					allowFreeAttend = free_makeUpNo-Integer.valueOf(makeUpNo);
		        				}
	        					
		        				if(allowFreeAttend>0) {
		        					remark = attendReal+"???"+(Integer.valueOf(makeUpNo)+1)+"?????????,????????????";
		        				}else {
		        					remark = attendReal+"???"+(Integer.valueOf(makeUpNo)+1)+"?????????,????????????";
		        				}
	        				}	
	       				
	        				
	        				if(LSignRecordHistory.get(x).getAttend_date().length()>9) {
	        					attend_date = LSignRecordHistory.get(x).getAttend_date().substring(6,10)+LSignRecordHistory.get(x).getAttend_date().substring(0,2)+LSignRecordHistory.get(x).getAttend_date().substring(3,5);        				
	        				}
	        				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
	        				String nowDate = sdFormat.format(new Date());
	
	        				if(!attend_date.isEmpty()) {
	        					if(Integer.valueOf(attend_date)>Integer.valueOf(nowDate)) {
	        						futureClass = true;
	        					}
	        				}
	        		
	        				
	        				//****B:??????????????????
	        				if(LSignRecordHistory.get(x).getAttend().equals("2")) {	 
	        					bookDate="-";
	        					allowFreeAttend = free_makeUpNo;
	        				//??????
	        				}else if(LSignRecordHistory.get(x).getAttend().equals("0")) {
	        					//Video????????????
	        					if(LSignRecordHistory.get(x).getClass_style().equals("2")){
	        						attendImg = "White_repeat.png";
	        				        switch(LSignRecordHistory.get(x).getSlot()) { 
	        			            	case "1":slot_name = "???";break;
	        			                case "2":slot_name = "???";break; 
	        			                case "3":slot_name = "???";break; 
	        				        }    
	            			    	if(!LSignRecordHistory.get(x).getAttend_date().equals("")) {
	            			    		bookDate="<font color='darkblue'>"+LSignRecordHistory.get(x).getAttend_date()+" "+slot_name+"</font>";
	            			    	}else {
	            			    		bookDate="-";
	            			    	}
	        					//????????????
	        					}else if(LSignRecordHistory.get(x).getClass_style().equals("3")){
	        						attendImg = "WhiteCloud.png";
	        				        switch(LSignRecordHistory.get(x).getSlot()) { 
	        			            	case "1":slot_name = "???";break;
	        			                case "2":slot_name = "???";break; 
	        			                case "3":slot_name = "???";break; 
	        				        }    
	            			    	if(!LSignRecordHistory.get(x).getAttend_date().equals("")) {
	            			    		bookDate="<font color='darkblue'>"+LSignRecordHistory.get(x).getAttend_date()+" "+slot_name+"</font>";
	            			    	}else {
	            			    		bookDate="-";
	            			    	}
	        					}	
	        					//???????????????,???attend_date?????????
	        			    	if(!LSignRecordHistory.get(x).getAttend_date().equals("")) {
	        			    		bookDate="<font color='darkblue'>"+LSignRecordHistory.get(x).getAttend_date()+" "+slot_name+"</font>";
	        			    	}        					
	
	        			    //?????? 
	        			    }else if(LSignRecordHistory.get(x).getAttend().equals("1")) {       						
	        					attendImg = "GreenSquare.png";
	        			    	if(!LSignRecordHistory.get(x).getAttend_date().equals("")) {
	        			    		bookDate="<font color='darkblue'>"+LSignRecordHistory.get(x).getAttend_date()+" "+slot_name+"</font>";        					
	        			    	}
	
	         	        			if(LSignRecordHistory.get(x).getClass_style().equals("2")){//Video????????????
	         	        				attendImg = "Green_repeat.png";
	         	        			}
	         	        	//??????		
	        	        	}else if(LSignRecordHistory.get(x).getAttend().equals("-1")) {
	        	        			attendImg = "RedSquare.png";
	
	        	        			if(LSignRecordHistory.get(x).getClass_style().equals("2")){//Video????????????
	        	        				attendImg = "Red_repeat.png";
	        	        			}
	        	        	}	    
	        			}        			
	        		} 


	        		tmp.append("<div class='th2' style='font-size:x-small;width:120px;padding:0px;border:1px #aaaaaa solid'>");
	    		        if(Integer.valueOf(LClasses.get(j).getClass_th())<0){
	    		        	tmp.append("<div style='text-align:center;color:red'>??????</span></div>");
	    		        }else if(bookDate.contains("-")) {
	    					tmp.append("<div style='padding:2px;text-align:center;background-color:"+bgcolor+";color:#333333'><img src='/images/"+attendImg+"' height='10px'/>&nbsp;&nbsp;???<b>"+LClasses.get(j).getClass_th()+"</b>???<span style='letter-spacing:0px'>&nbsp;"+LClasses.get(j).getClass_name()+"</span></div>");
	    				}else {
	    					tmp.append("<div style='padding:2px;text-align:center;background-color:"+bgcolor+"'><img src='/images/"+attendImg+"' height='10px'/>&nbsp;&nbsp;<A href='javascript:void(0)' style='color:blue' onclick='openSignRecord("+signRecordHistory_seq+","+attend+")' title='??????/??????/????????????'>???<b>"+LClasses.get(j).getClass_th()+"</b>???</div></A>");
	    				}
	    		        		    		        
	    				    tmp.append("<div style='border-top:1px #eeeeee dotted;letter-spacing:0px;text-align:center;color:#333333'>"+bookDate+"</div>"+
	    					     "<div style='border-top:1px #eeeeee dotted;text-align:center'>");
	    			   
	    		        
	    				//if(!attendImg.equals("WhiteSquare.png") && !LClasses.get(j).getClass_th().equals("-1")) {	
	    				if(!LClasses.get(j).getClass_th().equals("-1")) {
	    					if(LClasses.get(j).getClass_style().equals("3")) {
	    						tmp.append("<div><A href='javascript:void(0)' style='letter-spacing:1px' onclick='openOnlineClass(\""+LClasses.get(j).getClass_seq()+"\")'><img src='/images/play.png' height='15px'/></A></div>");
	    					}else {
	    						tmp.append("<div><A href='javascript:void(0)' style='letter-spacing:1px' onclick='ClassBook(\""+LRegister_comboSale_grade.get(k).getRegister_comboSale_grade_seq()+"\",\""+grade.getSchool_code()+"\",\""+grade.getSubject_id()+"\",\""+grade.getClass_start_date_0()+"\",\""+LClasses.get(j).getClass_th()+"\",\""+signRecordHistory_classStyle+"\",\""+signRecordHistory_seq+"\",\""+futureClass+"\",\""+makeUpNo+"\",\""+grade.getGrade_seq()+"\",\""+allowFreeAttend+"\",\""+attendDateCancel+"\",\""+remark+"\",\""+grade.getTeacher_id()+"\")' title='?????? / ????????????'><img src='/images/book.jpg' height='15px'/></A></div>");
	    					}	
	    			    }
							tmp.append("</div>"+
	    					     "<div style='color:red;letter-spacing:0px;border-top:1px #eeeeee dotted'>&nbsp;"+LClasses.get(j).getClass_remark()+"</div>"+
	    				  "</div>");	
	    			
	    			if((j+1)%10==0 || (j+1)==LClasses.size()) {
	    					Ltmp.add(tmp.toString());    				 
	    				    tmp.setLength(0);  
	    			} 
	    		}    
	    			grade.setClassGroupStr(Ltmp);
	    			LGrade.add(grade);
	    	  }		
			}    	
	    }
	    
	    //??????????????????	    
	    String gradeName = "";
	    String showFlag = "";
	    String showStyle = "";
		Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0);    
	    String tmpStr = "<input type='hidden' id='student_seq' value='"+student_seq+"'>";
	    		tmpStr+="<div style='letter-spacing:2px'>&bull;<A href='javascript:void(0)' onclick=openClassMakeUp(\'"+student_seq+"\') style='font-weight:bold;text-decoration:underline;color:blue'>Video???????????? :  "+student.getMakeUpTotal()+"???</A></div>"; 	    		
	    for(int a=0;a<LGrade.size();a++) {
	    	if(LGrade.get(a).getGradeName()!=null) {gradeName = LGrade.get(a).getGradeName();}
	    	if(LGrade.get(a).getGrade_seq().equals(grade_id)) {
	    		showFlag="&minus;";
	    		showStyle="block";
	    	}else{
	    		showFlag="&plus;";
	    		showStyle="none";
	    	}
			String video_date = LGrade.get(a).getVideo_date();
			if(video_date!=null && !video_date.isEmpty()) {
				video_date = "/"+video_date;
			}
			
           	String fontColor="color:#000000;";
           	if(LGrade.get(a).getRegister_comboSale_grade_statusStr().contains("cancel")) {
           		fontColor="color:#aaaaaa;"; //?????????????????????
           	}
           	
	    	tmpStr+=
	    	"<div class='css-table' style='margin:10px'>"+
	    	    "<div class='tr'>"+
		           "<div class='css-table' style='font-size:small;"+fontColor+"'>"+
		             "<div class='tr'>"+
		                "<div class='td2'><span style='background-color:#777777'><A href='javascript:void(0)' onclick='View(this)' style='font-weight:bold;font-size:medium;color:white'>"+showFlag+"</A></span></div>&nbsp;&nbsp;";	    	
			           	if(LGrade.get(a).getPre_grade_remark()!="") {
			           		tmpStr+="<div class='td2' style='text-decoration:underline;text-decoration-color:#cccccc;color:red'>"+LGrade.get(a).getPre_grade_remark()+"</div>";
			           	}
			           	

			           	tmpStr+=
			           	"<div class='td2' style='font-weight:bold;text-decoration:underline;text-decoration-color:#cccccc;'>"+LGrade.get(a).getClass_style_img()+" "+LGrade.get(a).getRegister_comboSale_grade_statusStr()+" "+LGrade.get(a).getClass_start_date()+video_date+LGrade.get(a).getSubject_name()+" "+gradeName+"&nbsp;&#8226;&nbsp;</div>"+
			           	"<div class='td2' style='text-decoration:underline;text-decoration-color:#cccccc;'>"+LGrade.get(a).getTeacher_name()+"&nbsp;&#8226;&nbsp;</div>"+
			           	"<div class='td2' style='text-decoration:underline;text-decoration-color:#cccccc;'>"+LGrade.get(a).getClass_no()+"???&nbsp;&#8226;&nbsp;</div>"+
			           	"<div class='td2' style='text-decoration:underline;text-decoration-color:#cccccc;'>"+LGrade.get(a).getSchool_name()+"</div>&nbsp;&nbsp;"+            	  
		             "</div>"+	
		           "</div>"+ 	
	        "</div>"+
	       
	        "<div id='child' class='tr' style='display:"+showStyle+"'>"+
	           "<div class='css-table' style='border-spacing:2px;background-color:white'>";
	             for(int b=0;b<LGrade.get(a).getClassGroupStr().size();b++) {
		             tmpStr+=	 
		             "<div class='tr'>"+LGrade.get(a).getClassGroupStr().get(b)+"</div>";
	             }
	             tmpStr+=
	             "</div>"+              	
	           "</div>"+
	        "</div>"; 	    	
	    }

    	
    	return tmpStr;
    }
	@RequestMapping("/Sales/Book_attend2")
	public String Book_attend2(Model model,HttpServletRequest request) throws ParseException {
		//?????????????????????
		String returnBack = "";
		String student_seq = "-1";
		if(request.getParameter("TodayBook")!=null && request.getParameter("TodayBook").equals("1")) {
			returnBack = "/Adm/TodayBook";
			List<Student> LStudent = salesService.getStudent("",request.getParameter("ch_name"),"",request.getParameter("student_no"),"","","","","","");
			if(LStudent.size()==1) {
				student_seq = LStudent.get(0).getStudent_seq();
				model.addAttribute("student_seq",student_seq);
			}else {
				return returnBack;
			}
		}else{
			returnBack = "/Sales/Book_attend";	
	
	    	student_seq = request.getParameter("student_seq");
	    	if(student_seq==null) {
				return "/Sales/Student"; 
			}else {
		    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
				if(LStudent.size()>0) {
					String ch_name = LStudent.get(0).getCh_name();
					String student_no = LStudent.get(0).getStudent_no();
		    		model.addAttribute("student_seq",student_seq);
		    		model.addAttribute("ch_name",ch_name);
		    		model.addAttribute("student_no",student_no);
				}		
			}	
		}
		
		String grade_id = request.getParameter("grade_id");
		model.addAttribute("grade_id",grade_id);
		
		String pop = request.getParameter("pop");
		if(pop!=null) {
			model.addAttribute("pop",pop);
		}else {
			model.addAttribute("pop","0");
		}
	        //????????????_??????????????? + ??????
	    	List<Register_comboSale> LRegister_comboSale = salesService.getComboSaleByRegister("","",student_seq,"","","1",false,false,"");
	    	//????????????????????????
	    	List<Grade> LGrade = new ArrayList<Grade>();
	   	
	    for(int i=0;i<LRegister_comboSale.size();i++) {    	
			List<Register_comboSale_grade> LRegister_comboSale_grade = courseService.getRegister_comboSale_grade(LRegister_comboSale.get(i).getRegister_comboSale_seq(),"","(1,2,3)","");	
			for(int k=0;k<LRegister_comboSale_grade.size();k++) {
			  String register_comboSale_grade_statusStr = "";	
			  if(LRegister_comboSale_grade.get(k).getRegister_status().equals("2")) {
				  register_comboSale_grade_statusStr = "<span style='font-size:x-small;color:red'><img src='/images/cancel.jpg' height='11px'/>??? ???</span>"; 
			  }else if(LRegister_comboSale_grade.get(k).getRegister_status().equals("3")) {
				  register_comboSale_grade_statusStr = "<span style='font-size:x-small;color:red'><img src='/images/cancel.jpg' height='11px'/>??? ???</span>"; 
			  } 
	    	 
			  List<Grade> LGrade0 = courseService.getGradeList("","",LRegister_comboSale_grade.get(k).getGrade_id(),"","","","","","","1","","","","","","","","1","");
	//+-??????
			  if(LGrade0.size()>0) {
	    			Grade grade = LGrade0.get(0);
	    			if(LRegister_comboSale_grade.get(k).getClass_style().equals("1")) {
	    				grade.setClass_style_img("<img src='/images/teacher.png' height='12px'/>");
	    			}else if(LRegister_comboSale_grade.get(k).getClass_style().equals("2")) {
	    				grade.setClass_style_img("<img src='/images/earphone.png' height='12px'/>");
	    			}else if(LRegister_comboSale_grade.get(k).getClass_style().equals("3")) {
	    				grade.setClass_style_img("<img src='/images/cloud.png' height='12px'/>");
	    			}	
	    			grade.setRegister_comboSale_grade_statusStr(register_comboSale_grade_statusStr); 
	    			//??????????????????
	    			List<Pre_grade> LpreGrade = salesService.getPre_grade("",grade.getGrade_seq());
	    			if(LpreGrade.size()>0) {
	    				grade.setPre_grade_remark(LpreGrade.get(0).getGrade_remark());
	    			}	
	    			grade.setSchool_name(LRegister_comboSale_grade.get(k).getSchool_name()); //??????????????????
	    			grade.setSchool_code(LRegister_comboSale_grade.get(k).getSchool_code());
				    List<Classes> LClasses = courseService.getClasses(LRegister_comboSale_grade.get(k).getGrade_id(),"","","","","","","","","");
	    			List<SignRecordHistory> LSignRecordHistory = admService.getSignRecordHistory("",student_seq,"","1","(1)","",LRegister_comboSale_grade.get(k).getRegister_comboSale_grade_seq(),"","","","");//????????????	   			
	    			String attendImg = "";
	    			StringBuilder tmp = new StringBuilder("");
	    			String ymd = ""; //?????????
	    			String wk =""; //??????
	    			String hm = "";//??????
	    			String bookDate = "";//??????????????????
	    			String signRecordHistory_seq = "";
	    			String slot_name = "";
	    			List<String> Ltmp = new ArrayList<String>();
	    			
	    		for(int j=0;j<LClasses.size();j++) {    			
	    			attendImg = "WhiteSquare.png";//?????????
	        		if(LClasses.get(j).getClass_style().equals("2") || LClasses.get(j).getClass_style().equals("3")){//Video???????????????????????????
						attendImg = "";
						bookDate="-";
	         		}else { 
	         			if(LClasses.get(j).getClass_date().length()>9) {
		         			wk = DateToWeek.getDateToWeek(LClasses.get(j).getClass_date());
		         			hm = LClasses.get(j).getTime_from().substring(0,2)+":"+LClasses.get(j).getTime_from().substring(2,4);
		         			ymd = LClasses.get(j).getClass_date();    					
		         			bookDate = ymd+wk+hm;
	         			}
	         		}
	        		
	
	        		
	        		String makeUpNo = "";
	        		int allowFreeAttend = 0; 
	        		String remark = ""; //???????????????
	        		String attend_date = "";
	        		String attendDateCancel = ""; //????????????????????????
	        		Boolean futureClass = false; //??????????????????, ?????????????????????Video??????
	        		String attend = "";
	        		String bgcolor = "#ffffff";
	        		String signRecordHistory_classStyle = ""; 
	        		String attendReal = "";
	      		
	        		for(int x=0;x<LSignRecordHistory.size();x++) {//????????????
	        			slot_name = "";
	        			if(LClasses.get(j).getClass_th().equals(LSignRecordHistory.get(x).getClass_th())) { 
	        				signRecordHistory_classStyle = LSignRecordHistory.get(x).getClass_style();
	        				attend =  LSignRecordHistory.get(x).getAttend();
	        				if(LSignRecordHistory.get(x).getAllowAttend().equals("1")){
	        					bgcolor = "#F7FE8D";
	        				}
	        				if(attend.equals("0")) { //????????????
	        					if(LSignRecordHistory.get(x).getAttend_date()!=null && LSignRecordHistory.get(x).getAttend_date().length()==10) {
	        						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	        						String current = sdf.format(new Date());
		        					//???????????????????????????(???)???????????????
		        					if(LSignRecordHistory.get(x).getAttend_date().length()==10) {
		        						String attend_dateBook = LSignRecordHistory.get(x).getAttend_date().substring(6,10)+LSignRecordHistory.get(x).getAttend_date().substring(0,2)+LSignRecordHistory.get(x).getAttend_date().substring(3,5);
		        						if(Integer.valueOf(current) <= Integer.valueOf(attend_dateBook)) {
		        							attendDateCancel = "1";
		        						}else {
		        							attendDateCancel = "0";
		        						}
		        					}else { attendDateCancel = "0";}
	        					}	
	        				}
	        				
	        				signRecordHistory_seq = LSignRecordHistory.get(x).getSignRecordHistory_seq();
	        				makeUpNo = LSignRecordHistory.get(x).getMakeUpNo();
	 
	        				
	        				       			
	        				int free_makeUpNo = 1;
	        				Grade grade2 = courseService.getGrade2(LSignRecordHistory.get(x).getGrade_id(),"","","","","","","","").get(0);
	        				List<Subject> LSubject = courseService.getSubject("",grade2.getSubject_id(),"","","","0");
	        				
	        				//free_makeUpNo : ?????????????????????????????????
	        				if(LSubject.size()>0) {free_makeUpNo = Integer.valueOf(LSubject.get(0).getFree_makeUpNo());}
	        				
	        				//attend : 0??????,1??????,-1??????,2 ?????????????????? //allowFreeAttend : ??????????????????????????? //makeUpNo : ?????????????????????
	        				//*****???????????????*****//
	        				if(!LSignRecordHistory.get(x).getAttend().equals("2")) {
		        				//*****????????????????????????*****//
		        				if(LSignRecordHistory.get(x).getClass_style().equals("1") && LSignRecordHistory.get(x).getAttend().equals("1")){        					
			        					allowFreeAttend = free_makeUpNo-Integer.valueOf(makeUpNo)-1;
			        					attendReal = "(????????????)";
			        			}else{
			        					allowFreeAttend = free_makeUpNo-Integer.valueOf(makeUpNo);
			        					attendReal = "(???????????????)";
		        				}	
		        				
		        				if(allowFreeAttend>0) {
		        					remark = attendReal+"???"+(Integer.valueOf(makeUpNo)+1)+"?????????,????????????";
		        				}else {
		        					remark = attendReal+"???"+(Integer.valueOf(makeUpNo)+1)+"?????????,????????????";
		        				}
	        				}	
	       				
	        				
	        				if(LSignRecordHistory.get(x).getAttend_date().length()>9) {
	        					attend_date = LSignRecordHistory.get(x).getAttend_date().substring(6,10)+LSignRecordHistory.get(x).getAttend_date().substring(0,2)+LSignRecordHistory.get(x).getAttend_date().substring(3,5);        				
	        				}
	        				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
	        				String nowDate = sdFormat.format(new Date());
	
	        				if(!attend_date.isEmpty()) {
	        					if(Integer.valueOf(attend_date)>Integer.valueOf(nowDate)) {
	        						futureClass = true;
	        					}
	        				}
	        		
	        				
	        				//??????????????????
	        				if(LSignRecordHistory.get(x).getAttend().equals("2")) {	 
	        					attendImg = "";
	        					bookDate="-";
	        				//??????
	        				}else if(LSignRecordHistory.get(x).getAttend().equals("0")) {
	        					//Video????????????
	        					if(LSignRecordHistory.get(x).getClass_style().equals("2")){
	        						attendImg = "White_repeat.png";
	        				        switch(LSignRecordHistory.get(x).getSlot()) { 
	        			            	case "1":slot_name = "???";break;
	        			                case "2":slot_name = "???";break; 
	        			                case "3":slot_name = "???";break; 
	        				        }    
	            			    	if(!LSignRecordHistory.get(x).getAttend_date().equals("")) {
	            			    		bookDate="<font color='darkblue'>"+LSignRecordHistory.get(x).getAttend_date()+" "+slot_name+"</font>";
	            			    	}else {
	            			    		bookDate="-";
	            			    	}
	        					//????????????
	        					}else if(LSignRecordHistory.get(x).getClass_style().equals("3")){
	        						attendImg = "WhiteCloud.png";
	        				        switch(LSignRecordHistory.get(x).getSlot()) { 
	        			            	case "1":slot_name = "???";break;
	        			                case "2":slot_name = "???";break; 
	        			                case "3":slot_name = "???";break; 
	        				        }    
	            			    	if(!LSignRecordHistory.get(x).getAttend_date().equals("")) {
	            			    		bookDate="<font color='darkblue'>"+LSignRecordHistory.get(x).getAttend_date()+" "+slot_name+"</font>";
	            			    	}else {
	            			    		bookDate="-";
	            			    	}
	        					}	
	        					//???????????????,???attend_date?????????
	        			    	if(!LSignRecordHistory.get(x).getAttend_date().equals("")) {
	        			    		bookDate="<font color='darkblue'>"+LSignRecordHistory.get(x).getAttend_date()+" "+slot_name+"</font>";
	        			    	}        					
	
	        			    //?????? 
	        			    }else if(LSignRecordHistory.get(x).getAttend().equals("1")) {       						
	        					attendImg = "GreenSquare.png";
	        			    	if(!LSignRecordHistory.get(x).getAttend_date().equals("")) {
	        			    		bookDate="<font color='darkblue'>"+LSignRecordHistory.get(x).getAttend_date()+" "+slot_name+"</font>";        					
	        			    	}
	
	         	        			if(LSignRecordHistory.get(x).getClass_style().equals("2")){//Video????????????
	         	        				attendImg = "Green_repeat.png";
	         	        			}
	         	        	//??????		
	        	        	}else if(LSignRecordHistory.get(x).getAttend().equals("-1")) {
	        	        			attendImg = "RedSquare.png";
	
	        	        			if(LSignRecordHistory.get(x).getClass_style().equals("2")){//Video????????????
	        	        				attendImg = "Red_repeat.png";
	        	        			}
	        	        	}	    
	        			}	
	        		} 
	        	//************????????????***************//        		
	        		tmp.append("<div class='th2' style='font-size:x-small;width:110px;padding:0px;border:1px #cccccc solid'>");
	    		        if(Integer.valueOf(LClasses.get(j).getClass_th())<0){
	    		        	tmp.append("<div style='text-align:center;color:red'>??????</span></div>");
	    		        }else if(bookDate.contains("-")) {
	    					tmp.append("<div style='padding:2px;text-align:center;background-color:"+bgcolor+";color:#333333'><img src='/images/"+attendImg+"' height='10px'/>&nbsp;&nbsp;???<b>"+LClasses.get(j).getClass_th()+"</b>???<span style='letter-spacing:0px'>&nbsp;"+LClasses.get(j).getClass_name()+"</span></div>");
	    				}else {
	    					tmp.append("<div style='padding:2px;text-align:center;background-color:"+bgcolor+"'><img src='/images/"+attendImg+"' height='10px'/>&nbsp;&nbsp;<A href='javascript:void(0)' style='text-decoration:underline;color:blue' onclick='openSignRecord("+signRecordHistory_seq+","+attend+")' title='??????/??????/????????????'>???<b>"+LClasses.get(j).getClass_th()+"</b>???</div></A>");
	    				}
	    				    tmp.append("<div style='border-top:1px #eeeeee dotted;letter-spacing:0px;text-align:center;color:#333333'>"+bookDate+"</div>"+
	    					     "<div style='border-top:1px #eeeeee dotted;text-align:center'>");
	    			
	    				if(!attendImg.equals("WhiteSquare.png") && !LClasses.get(j).getClass_th().equals("-1")) {	
	    					tmp.append("<div><A href='javascript:void(0)' style='letter-spacing:1px' onclick='ClassBook(\""+LRegister_comboSale_grade.get(k).getRegister_comboSale_grade_seq()+"\",\""+grade.getSchool_code()+"\",\""+grade.getSubject_id()+"\",\""+grade.getClass_start_date_0()+"\",\""+LClasses.get(j).getClass_th()+"\",\""+signRecordHistory_classStyle+"\",\""+signRecordHistory_seq+"\",\""+futureClass+"\",\""+makeUpNo+"\",\""+grade.getGrade_seq()+"\",\""+allowFreeAttend+"\",\""+attendDateCancel+"\",\""+remark+"\")' title='?????? / ????????????'><img src='/images/book.jpg' height='13px'/></A></div>");
	    			    }
							tmp.append("</div>"+
	    					     "<div style='color:red;letter-spacing:0px;border-top:1px #eeeeee dotted'>&nbsp;"+LClasses.get(j).getClass_remark()+"</div>"+
	    				  "</div>");	
	    			
	    			if((j+1)%10==0 || (j+1)==LClasses.size()) {
	    					Ltmp.add(tmp.toString());    				 
	    				    tmp.setLength(0);  
	    			} 
	    		}    
	    			grade.setClassGroupStr(Ltmp);
	    			LGrade.add(grade);
	    	  }		
			}
			
	    }
			Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0);
			model.addAttribute("makeUpTotal",student.getMakeUpTotal()); 
	    	model.addAttribute("LGrade",LGrade);
	    	model.addAttribute("student_seq",student_seq);
	 	    return returnBack;
	} 
    
    @RequestMapping("/Sales/GradeBookHistory")
    public String GradeBookHistory(Model model,HttpServletRequest request) {
    	String student_seq = request.getParameter("student_seq");
    	model.addAttribute("student_seq",student_seq);
        return "/Sales/GradeBookHistory";
    }
    
    @RequestMapping("/Sales/getGradeBookHistory")
    @ResponseBody
    public List<Register_log> getGradeBookHistory(Model model,HttpServletRequest request) {
    	String student_seq = request.getParameter("student_seq");
    	List<Register_log> LRegister_log = admService.getRegisterLog(student_seq,"");
    	for(int i=0;i<LRegister_log.size();i++) {
    		if(LRegister_log.get(i).getRegister_status().equals("1")) {
    			LRegister_log.get(i).setRegister_status("??????");
    		}else if(LRegister_log.get(i).getRegister_status().equals("2")) {
    			if(LRegister_log.get(i).getIsUpdate().equals("1")) {
    				//????????????
    			}else{
	    			String Register_comboSale_grade_seq = LRegister_log.get(i).getRegister_comboSale_grade_seq();
	    			String register_log_seq = LRegister_log.get(i).getRegister_log_seq();   			
	    			LRegister_log.get(i).setRegister_log_seq("<A href='javascript:void(0)' onclick=gradeChange2("+register_log_seq+","+Register_comboSale_grade_seq+","+student_seq+") style='text-decoration:underline;color:blue;font-weight:bold'>"+LRegister_log.get(i).getRegister_log_seq()+"</A>");
    			}
    			LRegister_log.get(i).setRegister_status("??????");
    		}else if(LRegister_log.get(i).getRegister_status().equals("3")) {
    			LRegister_log.get(i).setRegister_status("??????");
    		}
    		if(LRegister_log.get(i).getReason_option().equals("-1") || LRegister_log.get(i).getReason_option().isEmpty()) {
    			LRegister_log.get(i).setReason_option("");
    		}else {
    			String tmp = "( ??????"+LRegister_log.get(i).getReason_option()+" )<br>";
    			if(LRegister_log.get(i).getClassCharge()!=null && !LRegister_log.get(i).getClassCharge().isEmpty()) {
    				tmp += "????????????:"+LRegister_log.get(i).getClassCharge()+"<br>";
    			}
    			if(LRegister_log.get(i).getOperationCharge()!=null && !LRegister_log.get(i).getOperationCharge().isEmpty()) {
    				tmp += "????????????:"+LRegister_log.get(i).getOperationCharge();
    			}
    			LRegister_log.get(i).setReason_option(tmp);
    		}
    	}
        return LRegister_log;
    }    
    
    @RequestMapping("/Sales/ClassBook")
    public String ClassBook(Model model,HttpServletRequest request,HttpSession session) {
    	String backURL = request.getRequestURL().append('?').append(request.getQueryString()).toString();
    	session.setAttribute("backURL",backURL);
    	model.addAttribute("backURL",backURL);
	
		String student_seq = request.getParameter("student_seq");
		model.addAttribute("student_seq",student_seq);
		
    	String school_code = request.getParameter("school_code");
    	model.addAttribute("school_code",school_code);   
    	
		List<School> LSchool = accountService.getSchool("","");
		model.addAttribute("LSchool",LSchool);	
		
    	String subject_id = request.getParameter("subject_id");
    	model.addAttribute("subject_id",subject_id);
		List<Subject> LSubject = courseService.getSubject("","","","","","0");
		model.addAttribute("LSubject",LSubject);		
		
		String grade_seq = request.getParameter("grade_seq");
		model.addAttribute("grade_id",grade_seq);
		
		String teacher_id = request.getParameter("teacher_id");
		
		List<Grade> LGrade = courseService.getGrade("","4,5",subject_id,"","","",teacher_id,"200","","","","","");
		
		model.addAttribute("LGrade",LGrade);
		
		String class_th = request.getParameter("class_th");
		model.addAttribute("class_th",class_th);
		
		String class_style = request.getParameter("class_style");
		model.addAttribute("class_style",class_style);
		
		String signRecordHistory_seq = request.getParameter("signRecordHistory_seq");
		model.addAttribute("signRecordHistory_seq",signRecordHistory_seq);
		
		String Register_comboSale_grade_id = request.getParameter("Register_comboSale_grade_id");
		model.addAttribute("Register_comboSale_grade_id",Register_comboSale_grade_id);
		
		String makeUpNo = request.getParameter("makeUpNo");
		model.addAttribute("makeUpNo",makeUpNo);
		
		String remark = request.getParameter("remark");
		model.addAttribute("remark",remark);		
		
		//???????????????(????????????)	
		  SimpleDateFormat sdFormat0 = new SimpleDateFormat("yyyy-MM-dd"); 
		  String curDate = sdFormat0.format(new Date());
		  model.addAttribute("curDate",curDate);
		  
			/**		 		
			//??????????????????????????????
			SignRecordHistory signRecordHistory = admService.getSignRecordHistory(signRecordHistory_seq,"","","","","","","","","","").get(0);
			String attend_date = signRecordHistory.getAttend_date();
			if(attend_date.length()>9) {
				//attend_date= attend_date.replace("-","");
				attend_date = attend_date.substring(6, 10)+attend_date.substring(0, 2)+attend_date.substring(3, 5);        							
				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
				String nowDate = sdFormat.format(new Date());
				if(Integer.valueOf(attend_date)>=Integer.valueOf(nowDate)) {
					List<Register> LRegister = salesService.getRegister3(Register_comboSale_grade_id);
					model.addAttribute("register_id",LRegister.get(0).getRegister_seq());
					return "/Sales/ClassBookCancel";
				}
			}
			**/	
		  
		//?????????????????????
		if(request.getParameter("cancelFlag")!=null) {
				List<Register> LRegister = salesService.getRegister3(Register_comboSale_grade_id);
				model.addAttribute("register_id",LRegister.get(0).getRegister_seq());
				return "/Sales/ClassBookCancel";
		}		  

        return "/Sales/ClassBook";
    }  
    
    @RequestMapping("/Sales/ClassBookHistory")
    public String ClassBookHistory(Model model,HttpSession session) {
    	String student_seq = (String) session.getAttribute("student_student_seq");
    	List<SignRecordChange> LSignRecordChange = salesService.getSignRecordChange(student_seq);
    	model.addAttribute("LSignRecordChange",LSignRecordChange);    	
        return "/Sales/ClassBookHistory";
    }
    
    @RequestMapping("/Sales/ClassBookSave")
    @ResponseBody
    public Boolean ClassBookSave(Model model,HttpServletRequest request,Principal principal) {
    	String signRecordHistory_seq = request.getParameter("signRecordHistory_seq");
    	String Register_comboSale_grade_id = request.getParameter("Register_comboSale_grade_id");
    	String attend_date = request.getParameter("attend_date");
    	String student_seq = request.getParameter("student_seq");
    	String class_style = request.getParameter("class_style");
    	String school_code = request.getParameter("school_code");
    	String grade_id    = request.getParameter("grade_id");
    	String class_th    = request.getParameter("class_th");
    	String class_th_ex = request.getParameter("class_th_ex");
    	String slot        = request.getParameter("slot");
    	String makeUpNo    = request.getParameter("makeUpNo");
    	String remark      = request.getParameter("remark");
    	String updater     = principal.getName();
    	String comment     = request.getParameter("comment");

    	if(attend_date.length()==10 && attend_date.contains("-")) {
    		attend_date = attend_date.substring(5,7)+"/"+attend_date.substring(8,10)+"/"+attend_date.substring(0,4);
    	}
    	//?????????????????????+1
    	if(makeUpNo!=null && !makeUpNo.isEmpty()) {
    		makeUpNo = String.valueOf(Integer.valueOf(makeUpNo)+1);
    	}

    	if(class_style.equals("1")) {
    		String grade_classesSel = request.getParameter("grade_classesSel");
    		if(grade_classesSel!=null && !grade_classesSel.isEmpty()) {
	    		String[] tmp = grade_classesSel.split("@");
	    		class_th_ex = tmp[0];
	    		attend_date = tmp[1];
    		}	
    	}
    	salesService.ClassBookSave(Register_comboSale_grade_id,student_seq,class_style,school_code,grade_id,class_th,slot,updater,attend_date,signRecordHistory_seq,makeUpNo,class_th_ex,remark,comment);
    	return true;
    }
    
    @RequestMapping("/Sales/ClassBookCancel")
    @ResponseBody
    public Boolean ClassBookCancel(HttpServletRequest request,HttpSession session,Principal principal) {
    	String signRecordHistory_seq = request.getParameter("signRecordHistory_seq");
    	String Register_comboSale_grade_id = request.getParameter("Register_comboSale_grade_id");
    	String class_th    = request.getParameter("class_th");
    	String makeUpNo    = request.getParameter("makeUpNo");
    	String student_seq =  request.getParameter("student_seq");
    	String register_id = request.getParameter("register_id"); 
    	salesService.ClassBookCancel(Register_comboSale_grade_id,class_th,signRecordHistory_seq,makeUpNo,student_seq,principal.getName(),register_id);
    	return true;
    }    
 
    @RequestMapping("/Sales/getJLM_studentPayRecord")
    @ResponseBody
    public String getJLM_studentPayRecord(HttpServletRequest request){
    	String student_no = request.getParameter("student_no");
    	String gradeId = request.getParameter("gradeId");
    	List<JLM_studentPayRecord> LJLM_studentPayRecord = salesService.getJLM_studentPayRecord(student_no,gradeId,"");

		String returnStr = "<div class='css-table'>";
		for(int x=0;x<LJLM_studentPayRecord.size();x++) { 
			returnStr += "<div class='tr' style='font-size:small'>";
			returnStr +=    "<div class='td2' style='padding:5px;width:180px'>"+LJLM_studentPayRecord.get(x).getReceiptNo()+"</div>";
			returnStr +=    "<div class='td2' style='padding:5px;width:100px'>"+LJLM_studentPayRecord.get(x).getPayMoney()+"</div>"; 
			returnStr +=    "<div class='td2' style='padding:5px;width:190px'>"+LJLM_studentPayRecord.get(x).getPayDate()+"</div>"; 
			returnStr +=    "<div class='td2' style='padding:5px;width:190px'>"+LJLM_studentPayRecord.get(x).getPayTime()+"</div>"; 
			returnStr +=    "<div class='td2' style='padding:5px;width:100px'>"+LJLM_studentPayRecord.get(x).getTakePerson()+"</div>"; 
			returnStr +=    "<div class='td2' style='padding:5px;width:100px'>"+LJLM_studentPayRecord.get(x).getPayStyle()+"</div>"; 
			returnStr += "</div>";
		}
		returnStr += "</div>";

		return returnStr;
    }
    
    @RequestMapping("/Sales/openRemarkDetail")
    public String openRemarkDetail(Model model,HttpServletRequest request) {
       //??????	
    	List<RegisterPromo> LRegisterPromo = salesService.getRegisterPromo(request.getParameter("Register_seq"));
    	String registerPromoStr = "";
    	for(int i=0;i<LRegisterPromo.size();i++) {
    		registerPromoStr +="<div>&bull; "+LRegisterPromo.get(i).getClassPromotion_name()+"</div>";
    	}
    	model.addAttribute("registerPromoStr",registerPromoStr);
       //??????
    	Register register = salesService.getRegisterList("","",request.getParameter("Register_seq"),"").get(0); 
    	model.addAttribute("commentStr",register.getComment());
    	
        return "/Sales/openRemarkDetail";
    } 
    
    @RequestMapping("/Sales/studentPay")
    public String studentPay(Model model,HttpServletRequest request,Principal principal,HttpSession session) {
    	String register_id = request.getParameter("Register_seq");	
    	model.addAttribute("register_id",register_id);
    	String student_seq = "-1";
    	if(register_id !=null && !register_id.isEmpty()) {
    		Register register = salesService.getRegisterList("","",register_id,"(0)").get(0);
    		student_seq = register.getStudent_seq();
    		String stillNeed = String.valueOf(Integer.valueOf(register.getActualPrice())-Integer.valueOf(register.getPaid()));
    		model.addAttribute("paidActual",register.getPaid()+" / <font color='red'>"+stillNeed+"</font> / "+register.getActualPrice());
    	}	
		Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0);
		model.addAttribute("balanceTotal",student.getBalanceTotal());  	
		
		//*******??????????????????5******//	
		int periods = 0;
		List<StudentPayRecordDetail> LStudentPayRecordDetail = salesService.getStudentPayRecordDetail("",register_id);
		if(LStudentPayRecordDetail.size()>0) {
			if(LStudentPayRecordDetail.get(0).getPeriods()!=null && !LStudentPayRecordDetail.get(0).getPeriods().isEmpty()) {
				periods = Integer.valueOf(LStudentPayRecordDetail.get(0).getPeriods()); 
			}	
		}
			
		String selectStr5 = "";
		int tmp = 0;
		for(int x=0;x<LStudentPayRecordDetail.size();x++){
			if(LStudentPayRecordDetail.get(x).getPayStyleID().equals("5")) {
				model.addAttribute("periods_5",periods);
				tmp++;
			    selectStr5 +=
				"<span>"+
				   "<input type='text' value='"+LStudentPayRecordDetail.get(x).getPayStyleDate()+"'  style='background-color:#ffeeff;width:75px;border:1px #aaaaaa solid' readonly>-"+
			       "<input type='text' value='"+LStudentPayRecordDetail.get(x).getPayStyleMoney()+"' style='width:65px;border:1px #aaaaaa solid' readonly>"+
				"</span>&nbsp;&nbsp;";
			}
		}
		//????????????
    	for(int i=tmp;i<periods;i++){
    		selectStr5 += 
    		"<span><input type='text' class='payStyle_5_date' name='payStyle_5_date'  onclick=\"$(this).datepicker({dateFormat:'yymmdd',});$(this).datepicker('show');\" style='background-color:#ffeeff;width:75px;border:1px #aaaaaa solid' placeholder='??? "+(i+1)+" ???'>-"+
    		"<input type='text' class='payStyle_5_money' name='payStyle_5_money' style='width:65px;border:1px #aaaaaa solid' placeholder='$'></span>&nbsp;&nbsp;";
    	}
    	model.addAttribute("selectStr5",selectStr5);
    	
		//*******??????????????????3*********//   	
		String selectStr3 = "";
		int tmp3 = 0;
		for(int x=0;x<LStudentPayRecordDetail.size();x++){
			if(LStudentPayRecordDetail.get(x).getPayStyleID().equals("3")) {
				model.addAttribute("periods_3",periods);
				tmp3++;
			    selectStr3 +=
				"<span>"+
				   "<input type='text' value='"+LStudentPayRecordDetail.get(x).getPayStyleDate()+"'  style='background-color:#ffeeff;width:75px;border:1px #aaaaaa solid' readonly>-"+
			       "<input type='text' value='"+LStudentPayRecordDetail.get(x).getPayStyleMoney()+"' style='width:65px;border:1px #aaaaaa solid' readonly>"+
				"</span>&nbsp;&nbsp;";
			}
		}
		//????????????
    	for(int i=tmp3;i<periods;i++){
    		selectStr3 += 
    		"<span><input type='text' class='payStyle_3_date' name='payStyle_3_date'  onclick=\"$(this).datepicker({dateFormat:'yymmdd',});$(this).datepicker('show');\" style='background-color:#ffeeff;width:75px;border:1px #aaaaaa solid' placeholder='??? "+(i+1)+" ???'>-"+
    		"<input type='text' class='payStyle_3_money' name='payStyle_3_money' style='width:65px;border:1px #aaaaaa solid' placeholder='$'></span>&nbsp;&nbsp;";
    	}
    	model.addAttribute("selectStr3",selectStr3);     	
		
        return "/Sales/studentPay";
    }     
 
	/*
	 * @RequestMapping(value="/Sales/StudentPaySave", method=RequestMethod.POST)
	 * public String StudentPaySave(@Valid StudentPayRecord
	 * studentPayRecord,BindingResult bindingResult,Model model,HttpServletRequest
	 * request,Principal principal) { String ch_name =
	 * accountService.getAccountByID("",principal.getName()).getCh_name();
	 * studentPayRecord.setTakePerson(ch_name);
	 * 
	 * salesService.StudentPaySave(studentPayRecord); return "true"; }
	 */
    
    @RequestMapping(value="/Sales/StudentPaySave")
    public String StudentPaySave(Model model,HttpServletRequest request,Principal principal) {
    	String register_id = request.getParameter("register_id");
    	String InstalNoSel = request.getParameter("InstalNoSel"); 
    	//????????????
    	StudentPayRecord studentPayRecord = new StudentPayRecord();
    	studentPayRecord.setTakePerson(accountService.getAccountByID("",principal.getName()).getCh_name());
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    	studentPayRecord.setPayDate(formatter.format(new Date()));
    	studentPayRecord.setSchool_code(accountService.getAccountByID("",principal.getName()).getSchool());    	
    	studentPayRecord.setRegister_id(register_id); 
    	studentPayRecord.setActualPrice(salesService.getRegisterList("","",register_id,"(0)").get(0).getActualPrice());
    	String student_seq = salesService.getRegisterList("","",register_id,"").get(0).getStudent_seq();
    	String payMoney = salesService.StudentPaySaveRecord(
    			studentPayRecord,
    			request.getParameter("payStyle_1_money"),
    			request.getParameter("payStyle_2_money"),
    			request.getParameterValues("payStyle_3_date"),
    			request.getParameterValues("payStyle_3_money"),
    			request.getParameter("payStyle_4_code"),
    			request.getParameter("payStyle_4_money"),
    			request.getParameter("payStyle_5_periods"),
    			request.getParameter("payStyle_5_code"),
    			request.getParameter("payStyle_5_money"),
    			request.getParameter("payStyle_6_code"),
    			request.getParameter("payStyle_6_money"),
    			request.getParameter("payStyle_7_code"),
    			request.getParameter("payStyle_7_money"),
    			student_seq,
    			studentPayRecord.getTakePerson(),
    			request.getParameter("new_paid"),
    			"1", //payWay??????
    			"",  //rebate
    			"",  //cashRefund1 ??????
    			"",   //cashRefund2 ??????
    			InstalNoSel //????????????
    	);
    	//????????????
    	String schoolCode = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();		
    	String experience_state = "";
    	String experience_id = "5";//??????

    		//??????
    		String orderStatus = salesService.getRegister("",register_id).get(0).getOrderStatus();
    		if(orderStatus.equals("1")) {
    			experience_state="?????????";
    		}else if(orderStatus.equals("2")) {
    			experience_state="??????";

	    	    //????????????Video??????
	    			List<Register_lagnappe> LRegister_lagnappe = salesService.getRegister_lagnappe("","","",register_id,"0","2");
	    			for(int i=0;i<LRegister_lagnappe.size();i++) {
	    					salesService.updateRegister_lagnappe(LRegister_lagnappe.get(i).getRegister_lagnappe_seq(),"1","System",""); 	    					
			    		    Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0); 
			    		    salesService.updateMakeUpTotal(
					    				student.getStudent_no(),
					    				Integer.valueOf(student.getMakeUpTotal())+Integer.valueOf(LRegister_lagnappe.get(i).getLagnappe_no()),
					    				LRegister_lagnappe.get(i).getLagnappe_no(),
					    				student_seq,
					    				principal.getName(),
					    				"1", //1:????????????
					    				"", //remark
					    				"", //content
					    				register_id
			    		    );
	    			}		    
    		}	

    	salesService.insertStudentExperience(student_seq,schoolCode,experience_id,experience_state,"","",principal.getName(),register_id);  		
    	    	
    	
    //orderChange???????????? 9:??????
    	salesService.orderChangeSave(register_id,student_seq,"9","","","","","",String.valueOf(payMoney),"","",principal.getName(),"");    	
    	return "/common/closeAndReload";
    } 
    
    @RequestMapping(value="/common/closeAndReload")
    public String closeAndReload() {  	
    	return "/common/closeAndReload";
    }
    
    @RequestMapping(value="/Sales/StudentSave",method=RequestMethod.POST)
    public String StudentSave(@Valid Student student,@Valid Student_degree student_degree,BindingResult bindingResult,Model model,HttpServletRequest request,Principal principal,@RequestParam("file") MultipartFile file,@Value("${UploadPath}") String UploadPath) {
    	Employee employee = accountService.getEmployee("","","","","","",principal.getName(),"").get(0);
    	String degree = request.getParameter("degree");
    	String internation = request.getParameter("internation");
    	String commentThis = request.getParameter("commentThis");
    	String drowssap = request.getParameter("drowssap");
    	student.setPasswd(drowssap);
    	if(student.getSex().equals("1")) {
    		student.setSex("???");
    	}else if(student.getSex().equals("0")) {
    		student.setSex("???");
    	}else {
    		student.setSex("");
    	}
    	String student_no = salesService.StudentSave(degree,student,employee.getCh_name(),principal.getName(),commentThis); 
    	
    	if(student.getDegree().equals("2")) {
    		student_degree.setInternation(internation);
    		salesService.Student_degreeSave(student_no,student_degree);
    	}
    	//????????????
        if (!file.isEmpty()) {
           try {
           	byte[] bytes = file.getBytes();
           	String fileName = file.getOriginalFilename();
           	fileName = student_no+fileName.substring(fileName.indexOf('.'));
    		//??????????????????
    		File dir = new File(UploadPath+"studentPhoto");
    		File[] files = dir.listFiles((d, name) -> name.startsWith(student_no));
    		for(int i=0;i<files.length;i++) {
    			files[i].delete();
    		}
            //??????
           	Path path = Paths.get(UploadPath+"studentPhoto/" + fileName);
           	Files.write(path, bytes);
           }catch(Exception e) {
               e.printStackTrace();
           }
        } 
        if(student.getStudent_seq() != null && !student.getStudent_seq().isEmpty()) {
        	return "redirect:/Sales/StudentProfile?message=1&student_seq="+student.getStudent_seq();
        }else {
        	return "/Sales/Student";
        }	
    }

    @RequestMapping("/Sales/getStudentPayRecord")
    @ResponseBody
    public String getStudentPayRecord(HttpServletRequest request){
    	
    	List<StudentPayRecord> LStudentPayRecord = salesService.getStudentPayRecord(request.getParameter("register_id"));
 		String returnStr = "<div class='css-table'>";
 		for(int x=0;x<LStudentPayRecord.size();x++) {
 		  if(!LStudentPayRecord.get(x).getPayMoney().equals("0")) {	
 			returnStr += "<div class='tr' style='font-size:small'>";
 			returnStr +=    "<div class='td2' style='padding:5px;width:100px;text-align:center;border-bottom:1px #ffffff solid'>"+LStudentPayRecord.get(x).getSchool_code()+LStudentPayRecord.get(x).getReceiptNo()+"</div>";
 			returnStr +=    "<div class='td2' style='padding:5px;width:100px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold'>"+LStudentPayRecord.get(x).getPayMoney()+"</div>"; 
 			returnStr +=    "<div class='td2' style='padding:5px;width:190px;text-align:center;border-bottom:1px #ffffff solid'>"+LStudentPayRecord.get(x).getPayDate()+"</div>";  
 			returnStr +=    "<div class='td2' style='padding:5px;width:100px;text-align:center;border-bottom:1px #ffffff solid'>"+LStudentPayRecord.get(x).getTakePerson()+"</div>"; 
 			returnStr +=    "<div class='td2' style='padding:5px;width:250px;text-align:left;border-bottom:1px #ffffff solid'>"+LStudentPayRecord.get(x).getPayStyle()+"</div>";
 			returnStr +=    "<div class='td2' style='padding:5px;width:100px;text-align:center;border-bottom:1px #ffffff solid'><img src='/images/print.png' height='12px'/></div>";
 			returnStr += "</div>";
 		  }
 		}
 		returnStr += "</div>";

 		return returnStr;
    }
    
    @RequestMapping(value="/Sales/getStudentPayRecordDetail")
    @ResponseBody
    public String getStudentPayRecordDetail(Model model,HttpServletRequest request) {
		String studentPayRecord_id = request.getParameter("studentPayRecord_id");
		List<StudentPayRecordDetail> LStudentPayRecordDetail = salesService.getStudentPayRecordDetail(studentPayRecord_id,"");		
 		String returnStr = "<div class='css-table'>";
 		for(int x=0;x<LStudentPayRecordDetail.size();x++) {
 			returnStr += "<div class='tr' style='font-size:small'>";
 			returnStr +=    "<div class='td2' style='padding:5px;width:100px;text-align:center;border-bottom:1px #eeeeee solid'>["+LStudentPayRecordDetail.get(x).getPayStyleCodeName()+LStudentPayRecordDetail.get(x).getPayStyleCode()+"]</div>"; 
 			returnStr +=    "<div class='td2' style='padding:5px;width:100px;text-align:center;border-bottom:1px #eeeeee solid'>"+LStudentPayRecordDetail.get(x).getPayStyleDate()+"</div>"; 
 			returnStr +=    "<div class='td2' style='padding:5px;width:100px;text-align:center;border-bottom:1px #eeeeee solid'>"+LStudentPayRecordDetail.get(x).getPayStyleMoney()+"</div>";
 			returnStr +=    "<div class='td2' style='padding:5px;width:100px;text-align:center;border-bottom:1px #eeeeee solid'>"+LStudentPayRecordDetail.get(x).getPeriods()+"</div>";
 			returnStr += "</div>";
 		}
 		returnStr += "</div>";
 		
 		return returnStr;
    }     

	/*
	 * @RequestMapping(value="/Sales/getStudentPayRecordAll")
	 * 
	 * @ResponseBody public List<StudentPayRecord>
	 * getStudentPayRecordAll(HttpServletRequest request){
	 * 
	 * List<StudentPayRecord> LStudentPayRecord =
	 * salesService.getStudentPayRecord("");
	 * 
	 * return LStudentPayRecord; }
	 */    
    
    @RequestMapping(value="/Sales/getOrderChange")
    @ResponseBody 
    public List<orderChange> getOrderChange(Model model,HttpServletRequest request) {
    	String school_code = request.getParameter("school_code");
    	String ch_name = request.getParameter("ch_name");
    	
    	List<orderChange> LOrderChange = salesService.getOrderChange(school_code,ch_name);
    	for(int i=0;i<LOrderChange.size();i++) {
    		if(!LOrderChange.get(i).getSubject_from().equals("")) {
    			LOrderChange.get(i).setSubject_from(courseService.getSubject("",LOrderChange.get(i).getSubject_from(),"","1","","0").get(0).getName());
    		}
    		if(!LOrderChange.get(i).getSubject_to().equals("")) {
    			LOrderChange.get(i).setSubject_to(courseService.getSubject("",LOrderChange.get(i).getSubject_to(),"","1","","0").get(0).getName());    	
    		}
    	/**	
    		if(LOrderChange.get(i).getCancelRegister()!=null && LOrderChange.get(i).getCancelRegister().equals("1")) {
    			LOrderChange.get(i).setCancelRegister("&#10007; ?????????");
    		}else if(LOrderChange.get(i).getCancelRegister()!=null && LOrderChange.get(i).getCancelRegister().equals("2")) {
    			LOrderChange.get(i).setCancelRegister("&#10007; ?????????");
    		}else {
    			LOrderChange.get(i).setCancelRegister("");
    		}
    	**/	
    	}	
    	return LOrderChange;
    } 
    
    @RequestMapping(value="/Sales/ConsultRecord")
    public String ConsultRecord(Model model,HttpServletRequest request) {
		String student_seq = request.getParameter("student_seq");
    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
		if(LStudent.size()>0) {
			String ch_name = LStudent.get(0).getCh_name();
			String student_no = LStudent.get(0).getStudent_no();
    		model.addAttribute("student_seq",student_seq);
    		model.addAttribute("ch_name",ch_name);
    		model.addAttribute("student_no",student_no);
		}     	
    	return "/Sales/ConsultRecord";
    } 
    
    @RequestMapping(value="/Sales/getConsultRecord")
    @ResponseBody
    public List<ConsultRecord>  getConsultRecord(Model model,HttpServletRequest request,HttpSession session) {
    	String student_seq = request.getParameter("student_seq");
		if(student_seq==null) {
			return new ArrayList<ConsultRecord>(); 
		}    	
    	List<ConsultRecord> LConsultRecord = salesService.getConsultRecord("",student_seq,"");
    	return LConsultRecord;
    }     
    
     
    @RequestMapping(value="/Sales/editConsultRecord")
    public String editConsultRecord(Model model,HttpServletRequest request,Principal principal,HttpSession session,@Value("${UploadPath}") String UploadPath) {
    	
	    	String student_seq = request.getParameter("student_seq");
	    	if(student_seq==null) {
				return "/Sales/Student"; 
			}else {
		    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
				if(LStudent.size()>0) {
					String ch_name = LStudent.get(0).getCh_name();
					String student_no = LStudent.get(0).getStudent_no();
		    		model.addAttribute("student_seq",student_seq);
		    		model.addAttribute("ch_name",ch_name);
		    		model.addAttribute("student_no",student_no);
				} 			
			}
    	
    	String consultRecord_seq = request.getParameter("consultRecord_seq"); //??????????????????
    	List<ClassCategorySel> LClassCategorySel = new ArrayList<ClassCategorySel>();//????????????
    	List<ConsultReasonSel> LConsultReasonSel = new ArrayList<ConsultReasonSel>();//????????????
    	List<InfoFromSel> LInfoFromSel = new ArrayList<InfoFromSel>();//????????????
    	List<NameFromSel> LNameFromSel = new ArrayList<NameFromSel>();//????????????
    	ConsultRecord consultRecord = null;
    	if(consultRecord_seq!=null && !consultRecord_seq.isEmpty()) {
    		consultRecord = salesService.getConsultRecord(consultRecord_seq,"","").get(0);
    		LClassCategorySel = salesService.getClassCategorySel(consultRecord_seq);
    		LConsultReasonSel = salesService.getConsultReasonSel(consultRecord_seq);
    		LInfoFromSel = salesService.getInfoFromSel(consultRecord_seq);
    		LNameFromSel = salesService.getNameFromSel(consultRecord_seq);
    	}else {
    		consultRecord = new ConsultRecord();
    		Employee employee = accountService.getEmployee("","","","","","",principal.getName(),"").get(0);
    		consultRecord.setEmployee_name(employee.getCh_name());
    		consultRecord.setEmployee_school(employee.getSchoolName());    	
    		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    		consultRecord.setCreateDate(formatter.format(new Date()));
    	}
    	model.addAttribute("consultRecord",consultRecord);
    	//????????????
    	List<Category> LCategory = courseService.getCategory("","");
    	ArrayList<String> ACategory = new ArrayList<String>();
    	String tmp = null;
    	for(int i=0;i<LCategory.size();i++) {
    		tmp = "";
    		for(int j=0;j<LClassCategorySel.size();j++) {
    			if(LCategory.get(i).getCategory_seq().equals(LClassCategorySel.get(j).getCategory_id())) {tmp="checked";} 
    		}
    		ACategory.add("&nbsp;<input type='checkbox' id='category_id' name='category_id' value='"+LCategory.get(i).getCategory_seq()+"' "+tmp+">"+LCategory.get(i).getName()+"&nbsp;&nbsp;");
    	}
    	if(consultRecord.getCategory_id_text_1()==null) {consultRecord.setCategory_id_text_1("");}
    	ACategory.add("<input type='text' style='height:20px;width:120px;border:1px #cccccc solid' id='category_id_text_1' name='category_id_text_1' value='"+consultRecord.getCategory_id_text_1()+"'>&nbsp;&nbsp;");
    	model.addAttribute("ACategory", ACategory);
    	
    	//????????????
    	List<ConsultCategory> LConsultCategory  = salesService.getConsultCategory();
    	model.addAttribute("LConsultCategory", LConsultCategory);
    	//????????????    
    /**	
    	List<ConsultReason> LConsultReason = salesService.getConsultReason();
    	ArrayList<String> AConsultReason = new ArrayList<String>();
    	for(int i=0;i<LConsultReason.size();i++) {
    		tmp = "";
    		for(int j=0;j<LConsultReasonSel.size();j++) {
    			if(LConsultReason.get(i).getId().equals(LConsultReasonSel.get(j).getConsultReason_id())) {tmp="checked";} 
    		}
    		AConsultReason.add("&nbsp;<input type='checkbox' id='consultReason' name='consultReason' value='"+LConsultReason.get(i).getId()+"' "+tmp+">"+LConsultReason.get(i).getName()+"&nbsp;");
    	}
    	model.addAttribute("AConsultReason", AConsultReason);
    **/	    	
    	//????????????
    	List<InfoFrom> LInfoFrom = salesService.getInfoFrom();
    	ArrayList<String> AInfoFrom = new ArrayList<String>();
    	for(int i=0;i<LInfoFrom.size();i++) {
    		tmp = "";
    		for(int j=0;j<LInfoFromSel.size();j++) {
    			if(LInfoFrom.get(i).getId().equals(LInfoFromSel.get(j).getInfoFrom_id())) {tmp="checked";} 
    		}
    		AInfoFrom.add("&nbsp;<input type='checkbox' id='infoFrom' name='infoFrom' value='"+LInfoFrom.get(i).getId()+"' "+tmp+">"+LInfoFrom.get(i).getName()+"&nbsp;&nbsp;");
    	}
    	if(consultRecord.getInfoFrom_text_1()==null) {consultRecord.setInfoFrom_text_1("");}
    	AInfoFrom.add("&nbsp;??????<input type='text' style='height:20px;width:120px;border:1px #cccccc solid' id='infoFrom_text_1' name='infoFrom_text_1' value='"+consultRecord.getInfoFrom_text_1()+"'>&nbsp;&nbsp;");
    	model.addAttribute("AInfoFrom", AInfoFrom);
    	
    	//????????????
    	List<NameFrom> LNameFrom = salesService.getNameFrom();
    	ArrayList<String> ANameFrom = new ArrayList<String>();
    	for(int i=0;i<LNameFrom.size();i++) {
    		tmp = "";
    		for(int j=0;j<LNameFromSel.size();j++) {
    			if(LNameFrom.get(i).getId().equals(LNameFromSel.get(j).getNameFrom_id())) {tmp="checked";} 
    		}
    		ANameFrom.add("&nbsp;<input type='checkbox' id='nameFrom' name='nameFrom' value='"+LNameFrom.get(i).getId()+"' "+tmp+">"+LNameFrom.get(i).getName()+"&nbsp;&nbsp;");
    	}
    	if(consultRecord.getNameFrom_text_1()==null) {consultRecord.setNameFrom_text_1("");}
    	ANameFrom.add("&nbsp;????????????<input type='text' style='height:20px;width:120px;border:1px #cccccc solid' id='nameFrom_text_1' name='nameFrom_text_1' value='"+consultRecord.getNameFrom_text_1()+"'>&nbsp;&nbsp;");
    	if(consultRecord.getNameFrom_text_2()==null) {consultRecord.setNameFrom_text_2("");}
    	ANameFrom.add("&nbsp;??????<input type='text' style='height:20px;width:120px;border:1px #cccccc solid' id='nameFrom_text_2' name='nameFrom_text_2' value='"+consultRecord.getNameFrom_text_2()+"'>&nbsp;&nbsp;");
    	model.addAttribute("ANameFrom", ANameFrom);    	
    	
        File dir = new File(UploadPath+"/consult/");
	      FilenameFilter filter = new FilenameFilter() {
	         public boolean accept (File dir, String name) { 
	            return name.startsWith(consultRecord_seq+".");
	         } 
	      }; 
	      String[] children = dir.list(filter);
	    	  if(children!=null && children.length>0) {   
	    	      model.addAttribute("uploadFile","<img src='/images/consult/"+children[0]+"?"+new Random().nextInt(10000)+"'/>"); 
	    	      model.addAttribute("deleteFun","<span style='padding:5px;background-color:white'><A href='javascript:void(0)' style='text-decoration:underline;color:blue' onclick=deleteUploadFile('"+children[0]+"')><img src='/images/delete.png' style='width:15px'> ????????????</A></span>");
	    	  }
    	return "/Sales/editConsultRecord";
    }     
  
       
    @RequestMapping(value="/Sales/ConsultRecordSave",method=RequestMethod.POST)
    public String ConsultRecordSave(@RequestParam("file") MultipartFile file,@ModelAttribute ConsultRecord consultRecord,Model model,HttpServletRequest request,Principal principal,@Value("${UploadPath}") String UploadPath) {
		String infoFrom_text_1 = request.getParameter("infoFrom_text_1");
		String nameFrom_text_1 = request.getParameter("nameFrom_text_1");
		String nameFrom_text_2 = request.getParameter("nameFrom_text_2");
		String category_id_text_1 = request.getParameter("category_id_text_1");
		String student_seq = request.getParameter("student_seq");
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			} 			
		}
    	Employee employee = accountService.getEmployee("","","","","","",principal.getName(),"").get(0);
    	consultRecord.setStudent_id(student_seq);
    	consultRecord.setEmployee_id(employee.getEmployee_seq());
    	consultRecord.setEmployee_name(employee.getCh_name());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		consultRecord.setCreateDate(formatter.format(new Date()));    	
    	consultRecord.setEmployee_school(employee.getSchoolName());
    	consultRecord.setInfoFrom_text_1(infoFrom_text_1);
    	consultRecord.setNameFrom_text_1(nameFrom_text_1);
    	consultRecord.setNameFrom_text_2(nameFrom_text_2);
    	consultRecord.setCategory_id_text_1(category_id_text_1);
    	
    	String category_id[] = request.getParameterValues("category_id");
    	String consultReason[] = request.getParameterValues("consultReason");
    	String nameFrom[] = request.getParameterValues("nameFrom");
    	String infoFrom[] = request.getParameterValues("infoFrom");
    	int consultRecord_seq = salesService.ConsultRecordSave(consultRecord,category_id,consultReason,infoFrom,nameFrom);
    	
    	//????????????
    	String experience_id = "";
    	String validDate = "";
    	String experience_state = "";
    	 
    	//1:1?????????,2:60?????????, 3:????????????, 4:????????????, 5:????????????
    	if(consultRecord.getConsultCategory_id().equals("1")) {
    		experience_id = "9";
    		validDate = consultRecord.getOneDayValid();
    	}else if(consultRecord.getConsultCategory_id().equals("2")) {
    		experience_id = "10";
    		validDate = consultRecord.getValidDate();
    	}
    	
    	String schoolCode = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();

    	salesService.insertStudentExperience(student_seq,schoolCode,experience_id,experience_state,"System",validDate,principal.getName(),"");  		
    	    	
    	
    	//????????????
        if (!file.isEmpty()) {
           try {
        	   
           	byte[] bytes = file.getBytes();
           	String fileName = file.getOriginalFilename();
           	fileName = consultRecord_seq+fileName.substring(fileName.indexOf('.'));
           	String pathStr = UploadPath+"/consult/";
           	
    		//??????????????????
    		File dir = new File(pathStr);
    		File[] files = dir.listFiles((d, name) -> name.startsWith(String.valueOf(consultRecord_seq)));
    		for(int i=0;i<files.length;i++) {
    			files[i].delete();
    		}
    		
           	Path path = Paths.get(pathStr + fileName);
           	Files.write(path, bytes);
           }catch(Exception e) {
               e.printStackTrace();
           }
        }       	
    	
        return "/common/closeAndReload";
    } 
/**    
    @RequestMapping(value="/Sales/UpdateRegister_comboSale")
    @ResponseBody
    public String UpdateRegister_comboSale(HttpServletRequest request) {
    	  String child_subject_id = request.getParameter("child_subject_id");
    	  String Register_comboSale_seq = request.getParameter("Register_comboSale_seq");
    	  String subject_id = request.getParameter("subject_id");
    	  String grade_id = request.getParameter("grade_id");
    	  if(Register_comboSale_seq!=null && !Register_comboSale_seq.isEmpty() && subject_id!=null && !subject_id.isEmpty()) {
    		  jdbcTemplate.update("update eip.Register_comboSale set subject_id=?,grade_id=?  where Register_comboSale_seq =? and subject_id=?",
    				  child_subject_id,
    				  grade_id,
    				  Register_comboSale_seq,
    				  subject_id   				  
    		  );
    	  }	  
		  return "successful";
	} 
**/
    
    @RequestMapping("/Sales/openBalance")
    public String openBalance(Model model,Principal principal,HttpServletRequest request) {
    	//String ch_name = accountService.getAccountByID("",principal.getName()).getCh_name();
    	model.addAttribute("creater",principal.getName());
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
    	model.addAttribute("createTime",formatter.format(new Date()));
    	model.addAttribute("student_id",request.getParameter("student_seq"));
        return "/Sales/openBalance";
    } 
    
    @RequestMapping("/Sales/openClassMakeUp")
    public String openClassMakeUp(Model model,Principal principal,HttpServletRequest request) {
    	model.addAttribute("student_seq",request.getParameter("student_seq"));
        return "/Sales/openClassMakeUp";
    } 
    
    @RequestMapping("/Sales/getClassMakeUp")
    @ResponseBody
    public List<MakeUpRecord> getClassMakeUp(Model model,Principal principal,HttpServletRequest request) {
    	List<MakeUpRecord> LMakeUpRecord = salesService.getMakeUpRecord(request.getParameter("student_seq"),"");
    	for(int i=0;i<LMakeUpRecord.size();i++) {
    		if(Integer.valueOf(LMakeUpRecord.get(i).getType())<0 && !LMakeUpRecord.get(i).getAmount().equals("0")) {
    			LMakeUpRecord.get(i).setAmount("-"+LMakeUpRecord.get(i).getAmount());
    		}else if(Integer.valueOf(LMakeUpRecord.get(i).getType())>0 && !LMakeUpRecord.get(i).getAmount().equals("0")) {
    			LMakeUpRecord.get(i).setAmount("+"+LMakeUpRecord.get(i).getAmount());
    		}
    	}
        return LMakeUpRecord;
    }    
    
    @RequestMapping("/Sales/LagnappeRecord")
    public String LagnappeRecord(Model model,HttpServletRequest request) {
    	String student_seq = request.getParameter("student_seq");
    	String student_no = "";
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			}  
			
			List<Category> LCategory = courseService.getCategory("","");
			model.addAttribute("LCategory",LCategory); 
	        return "/Sales/LagnappeRecord";
    	}    
    } 
    
    @RequestMapping("/Sales/getLagnappeRecord")
    @ResponseBody
    public List<Register_lagnappe> getLagnappeRecord(HttpServletRequest request,Model model,HttpSession session) {
    	String student_seq = request.getParameter("student_seq");
	   	if(student_seq==null) {
    		return new ArrayList<Register_lagnappe>();
    	}else {		
    		//????????????+????????????		
    		List<Register_lagnappe> LRegister_lagnappe = salesService.getRegister_lagnappe(student_seq,"","","","","");
    		return LRegister_lagnappe;
    	}
        
    } 
    
    
    @RequestMapping("/Sales/getOutPublisher")
    @ResponseBody
    public List<Register_outPublisher> getOutPublisher(HttpServletRequest request,Model model) {
    	String student_seq = request.getParameter("student_seq");
	   	if(student_seq==null) {
    		return new ArrayList<Register_outPublisher>();
    	}else {		
    		//????????????+????????????		
    		List<Register_outPublisher> LRegister_outPublisher = salesService.getRegister_outPublisher(student_seq,"","","");
    		return LRegister_outPublisher;
    	}      
    }    

    @RequestMapping("/Sales/openIssue")
    public String openIssue(HttpServletRequest request,Model model,Principal principal) {
    	String student_seq = request.getParameter("student_seq");
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			} 
			String register_id = request.getParameter("register_id");
			model.addAttribute("register_id",register_id);
			List<Register> LRegister = salesService.getRegisterList(student_seq,"Fee",register_id,"");
			String fee = LRegister.get(0).getActualPrice()+" / "+LRegister.get(0).getPaid();
			model.addAttribute("fee",fee);
						
	    	String ch_name = accountService.getAccountByID("",principal.getName()).getCh_name();
	    	model.addAttribute("creater",ch_name);
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	    	model.addAttribute("createTime",formatter.format(new Date())); 
	    	String register_lagnappe_seq = request.getParameter("register_lagnappe_seq");
	    	model.addAttribute("register_lagnappe_seq",register_lagnappe_seq);
	    	String isMakeup = request.getParameter("isMakeup");
	    	model.addAttribute("isMakeup",isMakeup);
	    	String comboSale_name = request.getParameter("comboSale_name");
	    	model.addAttribute("comboSale_name",comboSale_name);
	    	
	    	String payOffRelease = request.getParameter("payOffRelease");
	    	model.addAttribute("payOffRelease",payOffRelease);
	    	
	    	String payOffRelease0 = request.getParameter("payOffRelease0");
			if(payOffRelease0!=null && (payOffRelease0.equals("1") || payOffRelease0.equals("2"))) {
				if(Integer.valueOf(LRegister.get(0).getActualPrice()) > Integer.valueOf(LRegister.get(0).getPaid())) {
					model.addAttribute("reason","*????????????<input type='text' id='reason' style='width:350px;height:25px'>");
				}
			}
			
	    	
    	}	
        return "/Sales/openIssue";
    }
    
    @RequestMapping("/Sales/openIssue2")
    public String openIssue2(HttpServletRequest request,Model model,Principal principal) {
    	String student_seq = request.getParameter("student_seq");
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			}
			
			String register_id = request.getParameter("register_id");
			model.addAttribute("register_id",register_id);
			List<Register> LRegister = salesService.getRegisterList(student_seq,"Fee",register_id,"");
			String fee = LRegister.get(0).getActualPrice()+" / "+LRegister.get(0).getPaid();
			model.addAttribute("fee",fee);			
   	
	    	String ch_name = accountService.getAccountByID("",principal.getName()).getCh_name();
	    	model.addAttribute("creater",ch_name);
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	    	model.addAttribute("createTime",formatter.format(new Date())); 
	    	String register_outPublisher_seq = request.getParameter("register_outPublisher_seq");
	    	model.addAttribute("register_outPublisher_seq",register_outPublisher_seq);
	    	
	    	String payOffRelease = request.getParameter("payOffRelease");
	    	model.addAttribute("payOffRelease",payOffRelease);
	    	
	    	String payOffRelease0 = request.getParameter("payOffRelease0");
			if(payOffRelease0!=null && (payOffRelease0.equals("1") || payOffRelease0.equals("2"))) {
				if(Integer.valueOf(LRegister.get(0).getActualPrice()) > Integer.valueOf(LRegister.get(0).getPaid())) {
					model.addAttribute("reason","*????????????<input type='text' id='reason' style='width:350px;height:25px'>");
				}
			}	    	
    	}	
        return "/Sales/openIssue2";
    }    
    
    @RequestMapping("/Sales/setIssue")
    @ResponseBody
    public String setIssue(HttpServletRequest request,Model model,Principal principal,HttpSession session) {
    	String student_seq = request.getParameter("student_seq");
    	if(student_seq==null) {
    		return "/Sales/Student";
    	}else {     	
	    	String register_id = request.getParameter("register_id"); 
    		String register_lagnappe_seq = request.getParameter("register_lagnappe_seq");
	    	String isIssue = request.getParameter("isIssue");
	    	String comboSale_name = request.getParameter("comboSale_name");
	    	String ch_name = accountService.getAccountByID("",principal.getName()).getCh_name();
	    	String reason = request.getParameter("reason");
	    	salesService.updateRegister_lagnappe(register_lagnappe_seq,isIssue,ch_name,reason);
	    	
	    	String isMakeup = request.getParameter("isMakeup");
	    	if(isMakeup.equals("1") && isIssue.equals("1")) { //??????Video??????,?????????student.[makeUpTotal]
		    		Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0);
		    		List<Register_lagnappe> LRegister_lagnappe = salesService.getRegister_lagnappe(student_seq,"",register_lagnappe_seq,"","1","2");
		    		if(LRegister_lagnappe.size()>0) {
			    		Register_lagnappe register_lagnappe = LRegister_lagnappe.get(0);	    		
			    		salesService.updateMakeUpTotal(
			    				student.getStudent_no(),
			    				Integer.valueOf(student.getMakeUpTotal())+Integer.valueOf(register_lagnappe.getLagnappe_no()),
			    				register_lagnappe.getLagnappe_no(),
			    				student_seq,
			    				principal.getName(),
			    				"1", //1:????????????
			    				"", //remark
			    				comboSale_name, //content
			    				register_id
			    		);
		    		}
	    	}
    	}	
        return "success";
    } 
    
    @RequestMapping("/Sales/setIssue2")
    @ResponseBody
    public String setIssue2(HttpServletRequest request,Model model,Principal principal) {
    	String student_seq = request.getParameter("student_seq");
    	if(student_seq==null) {
    		return "/Sales/Student";
    	}else {     	
	    	String register_outPublisher_seq = request.getParameter("register_outPublisher_seq");
	    	String isIssue = request.getParameter("isIssue");
	    	String reason = request.getParameter("reason");
	    	String ch_name = accountService.getAccountByID("",principal.getName()).getCh_name();
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	    	String createTime = formatter.format(new Date());  
	    	salesService.updateRegister_outPublisher(register_outPublisher_seq,isIssue,ch_name,createTime,reason);
    	}	
        return "success";
    }     
    
    @RequestMapping("/Sales/BalanceSave")
    public String BalanceSave(HttpServletRequest request,Principal principal) {
    	String student_id = request.getParameter("student_id");
    	String balanceTotal = salesService.getStudent(student_id,"","","","","","","","","").get(0).getBalanceTotal();
    	if(balanceTotal==null || balanceTotal.isEmpty()) {balanceTotal="0";}
    	
    	String OverPaytype = request.getParameter("OverPaytype");
    	String amount = request.getParameter("amount");
    	String remark = request.getParameter("remark");
    	
    	//Employee employee = accountService.getEmployee("","","","","","",principal.getName()).get(0);
    	String creater = principal.getName();  	
    	
    	salesService.BalanceSave(student_id,OverPaytype,amount,remark,creater,balanceTotal);   
        return "redirect:/Sales/openBalance?student_seq="+student_id;
    } 
    
    @RequestMapping("/Sales/getBalanceRecord")
    @ResponseBody
    public List<BalanceRecord> getBalanceRecord(HttpServletRequest request) {
		List<BalanceRecord> LBalanceRecord = salesService.getBalanceRecord(request.getParameter("student_id"));
        return LBalanceRecord;
    }
/**    
    @RequestMapping("/Sales/MakeUpSave")
    public String MakeUpSave(HttpServletRequest request,Principal principal) {
    	String student_id = request.getParameter("student_id");
    	String makeUpTotal = salesService.getStudent(student_id,"","","","","","","").get(0).getMakeUpTotal()==null?"0":salesService.getStudent(student_id,"","","","","","","").get(0).getMakeUpTotal();
    	String type = request.getParameter("type");
    	String amount = request.getParameter("amount");
    	String remark = request.getParameter("remark");
    	
    	Employee employee = accountService.getEmployee("","","","","","",principal.getName()).get(0);
    	String creater = employee.getCh_name();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String createTime = formatter.format(new Date());     	
    	
    	salesService.MakeUpSave(student_id,type,amount,remark,creater,createTime,makeUpTotal);   
        return "redirect:/Sales/openClassMakeUp?student_seq="+student_id;
    }
 **/   
    @RequestMapping("/Sales/getMakeUpRecord")
    @ResponseBody
    public List<MakeUpRecord> getMakeUpRecord(HttpServletRequest request) {
		List<MakeUpRecord> LMakeUpRecord = salesService.getMakeUpRecord(request.getParameter("student_id"),"");
        return LMakeUpRecord;
    } 
    
    @RequestMapping(value="/Sales/interestSubjectSave")
    public String interestSubjectSave(HttpServletRequest request) {
    	String Register_comboSale_id = request.getParameter("Register_comboSale_id");
    	String[] child_subject_id = request.getParameterValues("child_subject_id");
    	if(Register_comboSale_id!=null && !Register_comboSale_id.isEmpty()) {
  			salesService.deleteInterestSubject(Register_comboSale_id); 		
  			salesService.saveInterestSubject(Register_comboSale_id,child_subject_id);
  		}	
    	return "/common/closeAndReload";
    } 

    @RequestMapping(value="/Sales/salesInfo")
    public String salesInfo(Model model,HttpServletRequest request,HttpSession session) {

		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
    	return "/Sales/salesInfo";
    }     
 
    @RequestMapping(value="/Sales/Book_mock")
    public String Book_mock(Model model,Principal principal,HttpServletRequest request) { 
    	String student_seq = request.getParameter("student_seq");
    	String school_code = "";
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				school_code = LStudent.get(0).getSchool_code();
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			}  
    	}
      //???????????????????????????	
      if(school_code.equals("")) {school_code = "TPXC";}
      
	  List<Register> LRegister = salesService.getRegister(student_seq,"");
	  String mockStr = "";
	  for(int i=0;i<LRegister.size();i++) { //????????????
		String tmp = ""; //?????????
		String tmp2 = ""; //????????????
		String tmp3 = ""; //??????
		String tmp4 = ""; //????????????
		String roundName = "";
		List<Register_mock> LRegister_mock = salesService.getMockByRegisterSeq(LRegister.get(i).getRegister_seq(),"","1");
		if(LRegister_mock.size()>0) {
		  mockStr+=
		  "<div class='tr' style=''>"+
				 "<div class='td2' style='width:80px;font-size:xx-small;border-bottom:1px dashed #dddddd;letter-spacing:0px;text-align:center'>"+LRegister.get(i).getUpdate_time()+"<br>"+LRegister.get(i).getCreater()+"</div>"+
				 "<div class='td2' style='width:350px;border-bottom:1px dashed #dddddd'>";
			  		  
				  for(int j=0;j<LRegister_mock.size();j++) { //??????????????????Mock			  
					  mockStr+="<div style='font-size:small;font-weight:bold;padding:5px'>"+
							  		"<span style='background-color:#DFF3FF;padding:2px'>"+LRegister_mock.get(j).getMock_name()+"</span>";
					  tmp+="<div style='font-size:small;font-weight:bold;padding:5px'>&nbsp;";
					  tmp2+="<div style='font-size:small;font-weight:bold;padding:5px'>&nbsp;";
					  tmp3+="<div style='font-size:small;font-weight:bold;padding:5px'>&nbsp;";
					  tmp4+="<div style='font-size:small;font-weight:bold;padding:5px'>&nbsp;";
					  				//*****A>????????????????????????*****//
					  				List<MockDetail> LMockDetail = courseService.getMockDetail(LRegister_mock.get(j).getMock_id(),"");
							  		for(int x=0;x<LMockDetail.size();x++) {
							  			if(LMockDetail.get(x).getNoName()!=null && !LMockDetail.get(x).getNoName().isEmpty()) {
							  				roundName = "???"+LMockDetail.get(x).getNoName()+"???";
							  			}else {
							  				roundName = "";
							  			}
							  			mockStr+=
							  					"<div class='css-table'>"+
							  						"<div class='tr' style='font-weight:normal'>"+
							  							"<div class='th3'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&bull;&nbsp;</div>"+
							  							"<div class='th3' style='font-weight:bold'>"+LMockDetail.get(x).getCategory_name()+"</div><div class='td2'> . </div>"+
							  							"<div class='th3'>"+roundName+"</div><div class='td2'> . </div>"+
							  							"<div class='th3'>"+LMockDetail.get(x).getTestStyleName()+"</div><div class='td2'> . </div>"+
							  							"<div class='th3'>"+LMockDetail.get(x).getTestMethodName()+"</div>"+
							  				        "</div>"+
							  					"</div>";
							  			        tmp+=
							  					"<div class='css-table'>"+
								  						"<div class='tr' style='font-weight:normal'>"+
								  							"<div class='th3'>&nbsp;</div>"+
								  				        "</div>"+
								  				"</div>";
							  			
							  			        tmp2+=
							  					"<div class='css-table'>"+
								  						"<div class='tr' style='font-weight:normal'>"+
								  							"<div class='th3'>&nbsp;</div>"+
								  				        "</div>"+
								  				"</div>";
							  			        String bookImg = "<img src='/images/book.jpg' height='15px' title='??????' />";
							  			        String attend = "";
							  			        String setDate = "";
                                                List<MockBaseBook2> LMockBaseBook2 = salesService.getMockBaseBook(student_seq,"1",LMockDetail.get(x).getMockDetail_seq(),"","","","",LRegister_mock.get(j).getRegister_mock_seq(),"","");
                                                if(LMockBaseBook2.size()>0) {
                                                	attend = LMockBaseBook2.get(0).getAttend();
                                                	if(attend.equals("1")) {  //??????                                              	
	                                                	List<MockBase> LMockBase = admService.getMockBase("","",LMockBaseBook2.get(0).getMockBase_id(),"");
	                                                	setDate = LMockBase.get(0).getSetDate();
									  			        tmp3+=
									  					"<div class='css-table'>"+
									  						"<div class='tr' style='font-weight:normal'>"+
									  							"<div class='th3'><A href='javascript:void(0)' style='color:blue;text-decoration:underline' onclick=cancelMockBook('"+student_seq+"','"+LMockBaseBook2.get(0).getMockBaseBook_seq()+"')>"+setDate.substring(4,6)+"/"+setDate.substring(6,8)+"</A></div>"+
									  				        "</div>"+
									  				    "</div>";
                                                	}else if(attend.equals("2")) {  //??????
                        			  			        tmp3+=
                        			  					"<div class='css-table'>"+
                        			  						"<div class='tr' style='font-weight:normal'>"+
                        			  							"<div class='th3'><img src='/images/GreenSquare.png' height='14px'/></div>"+
                        			  				        "</div>"+
                        			  				    "</div>";   									  			        
                                                	}else {
                                        				tmp3+=
									  					"<div class='css-table'>"+
									  						"<div class='tr' style='font-weight:normal'>"+
									  							"<div class='th3'><A href='javascript:void(0)' onclick=openMockBook('"+student_seq+"','"+LMockDetail.get(x).getMockDetail_seq()+"','"+school_code+"','"+LRegister.get(i).getRegister_seq()+"','"+LRegister_mock.get(j).getRegister_mock_seq()+"')>"+bookImg+"</A></div>"+
									  				        "</div>"+
									  				    "</div>";                                                 		
                                                	}
                                                	
    									  			tmp4+=
												  	"<div class='css-table'>"+
													  		"<div class='tr' style='font-weight:normal'>"+
									                    		"<div class='th3'><A href='javascript:void(0)'  onclick=mockBaseBookHistory('"+student_seq+"','"+LMockBaseBook2.get(0).getMockBase_id()+"')  style='color:blue;text-decoration:underline;font-weight:bold'>&hellip;</A></div>"+
													  		"</div>"+
									                "</div>";                                                 	
                                                }else {
								  			        tmp3+=
								  					"<div class='css-table'>"+
								  						"<div class='tr' style='font-weight:normal'>"+
								  							"<div class='th3'><A href='javascript:void(0)' onclick=openMockBook('"+student_seq+"','"+LMockDetail.get(x).getMockDetail_seq()+"','"+school_code+"','"+LRegister.get(i).getRegister_seq()+"','"+LRegister_mock.get(j).getRegister_mock_seq()+"')>"+bookImg+"</A></div>"+
								  				        "</div>"+
								  				    "</div>";                                               	
                                                }
                                                
									  			tmp4+=
											  	"<div class='css-table'>"+
												  		"<div class='tr' style='font-weight:normal'>"+
								                    		"<div class='th3'>&nbsp;</div>"+
												  		"</div>"+
								                "</div>";                                                 
							  		}
							  		
							  		
					                //*****B>??????????????????????????????*****//
					  				
					  				Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0);
					  				String makeUpTotal = student.getMakeUpTotal();
		        					
		        					List<MockVideo> LMockVideo = courseService.getMockVideo(LRegister_mock.get(j).getMock_id());
					  				
							  		for(int x=0;x<LMockVideo.size();x++) {
							  			List<MockVideoHistory> LMockVideoHistory = courseService.getMockVideoHistory(LRegister_mock.get(j).getRegister_mock_seq(),"1",LMockVideo.get(x).getMockVideo_seq(),"(1,2)","","","","","","");
							  			//?????????
							  			mockStr+=
							  					"<div class='css-table'>"+
							  						"<div class='tr' style='font-weight:normal'>"+
							  							"<div class='th3'>&nbsp;&nbsp;<img src='/images/earphone.png' height='7px'/>&nbsp;</div>"+
							  							"<div class='th3'>"+LMockVideo.get(x).getSubject_name()+"</div>"+
							  				        "</div>"+
							  					"</div>";
							  			
							  			    //????????????????????????
							  			    if(LMockVideoHistory.size()==0) {
							  			        tmp+=
							  					"<div class='css-table'>"+
							  						"<div class='tr' style='font-weight:normal'>"+
							  							"<div class='th3'><A href='javascript:void(0)' onclick=openMockVideoToSelect("+LRegister_mock.get(j).getRegister_mock_seq()+","+LRegister.get(i).getRegister_seq()+","+LMockVideo.get(x).getSubject_id()+","+LMockVideo.get(x).getMockVideo_seq()+") style='text-decoration:underline;color:blue'>??????</A></div>"+
							  				        "</div>"+
							  					"</div>";
							  			        
							  			        tmp2+=
							  					"<div class='css-table'>"+
							  						"<div class='tr' style='font-weight:normal'>"+
							  							"<div class='td2'>&nbsp;</div>"+
							  				        "</div>"+
							  					"</div>";
							  			        
							  			        tmp3+=
							  					"<div class='css-table'>"+
								  						"<div class='tr' style='font-weight:normal'>"+
								  							"<div class='td2'>&nbsp;</div>"+
								  				        "</div>"+
								  				"</div>";
							  			        
									  			tmp4+=
											  	"<div class='css-table'>"+
												  		"<div class='tr' style='font-weight:normal'>"+
								                    		"<div class='th3'><A href='javascript:void(0)'  onclick='mockHistory()'  style='color:blue;text-decoration:underline;font-weight:bold'>&hellip;</A></div>"+
												  		"</div>"+
								                "</div>"; 							  			        
							  			    
							  			    //????????????????????????  
							  			    }else {
							  			    	    String attendDateCancel = "0";
						        					if(LMockVideoHistory.get(0).getAttend_date()!=null && !LMockVideoHistory.get(0).getAttend_date().isEmpty()) {
						        						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						        						String current = sdf.format(new Date());
							        						//???????????????????????????(???)???????????????
							        						String attend_dateBook =  LMockVideoHistory.get(0).getAttend_date();
							        						if(Integer.valueOf(current) <= Integer.valueOf(attend_dateBook)) {
							        							attendDateCancel = "1";
							        						}else {
							        							attendDateCancel = "0";
							        						}
						        					}
					        					
							  			        tmp+=
							  					"<div class='css-table'>"+
							  						"<div class='tr' style='font-weight:normal'>"+
							  							"<div class='td2'>-</div>"+
							  				        "</div>"+
							  					"</div>";
							  			        
							  			        Grade grade = courseService.getGrade(LMockVideoHistory.get(0).getGrade_id(),"","","","","","","","","","","","").get(0);
							  			        
							  			        tmp2+=
							  					"<div class='css-table'>"+
							  						"<div class='tr' style='font-weight:normal'>"+
							  							"<div class='th3'>"+grade.getClass_start_date()+" "+grade.getSubject_name()+grade.getGradeName()+"</div>"+
							  				        "</div>"+
							  					"</div>";
							  			        
							  			        int allowFreeAttend = 1; //???????????????????????????`							  			        
							  			        tmp3+=
							  					"<div class='css-table'>"+
								  						"<div class='tr' style='font-weight:normal'>";
							  			                    if(LMockVideoHistory.get(0).getAttend_date()!=null && !LMockVideoHistory.get(0).getAttend_date().isEmpty()) {
							  			                    	String attend_dateStr = LMockVideoHistory.get(0).getAttend_date().substring(4,6)+"/"+LMockVideoHistory.get(0).getAttend_date().substring(6,8);
							  			                    	tmp3+=
							  			                    	"<div class='th3'><A href='javascript:void(0)' onclick=ClassBook2('"+LMockVideoHistory.get(0).getMockVideoHistory_seq()+"','"+student_seq+"','"+school_code+"','"+makeUpTotal+"','"+allowFreeAttend+"','"+attendDateCancel+"') style='text-decoration:underline;color:blue'>"+attend_dateStr+"</A></div>";							  			                    	
							  			                    }else {
							  			                    	tmp3+=
							  			                    	"<div class='th3'><A href='javascript:void(0)' onclick=ClassBook2('"+LMockVideoHistory.get(0).getMockVideoHistory_seq()+"','"+student_seq+"','"+school_code+"','"+makeUpTotal+"','"+allowFreeAttend+"','"+attendDateCancel+"') style='text-decoration:underline'><img src='/images/book.jpg' height='15px'/></A></div>";
							  			                    }
							  							tmp3+=
							  							"</div>"+
								  				"</div>";
							  								
									  			tmp4+=
											  	"<div class='css-table'>"+
												  		"<div class='tr' style='font-weight:normal'>"+
								                    		"<div class='th3'><A href='javascript:void(0)'  onclick=mockVideoHistory('"+LMockVideoHistory.get(0).getStudent_id()+"','"+LMockVideoHistory.get(0).getMockVideo_id()+"','"+LMockVideoHistory.get(0).getRegister_mock_id()+"')  style='color:blue;text-decoration:underline;font-weight:bold'>&hellip;</A></div>"+
												  		"</div>"+
								                "</div>"; 							  							
							  			    }  								  			    
							  		}							  		
					  mockStr+="</div>";
					  tmp+="</div>";
					  tmp2+="</div>";
					  tmp3+="</div>";
					  tmp4+="</div>";
  
				  }		  
			    mockStr+=
			    "</div>";			
		
		        mockStr+=
		        "<div class='td2' style='width:60px;border-bottom:1px dashed #dddddd' align='center'>"+tmp+"</div>"+
		        "<div class='td2' style='width:250px;border-bottom:1px dashed #dddddd;text-align:center'>"+tmp2+"</div>"+
		        "<div class='td2' style='width:80px;border-bottom:1px dashed #dddddd' align='center'>"+tmp3+"</div>"+
		        "<div class='td2' style='width:100px;border-bottom:1px dashed #dddddd' align='center'>"+tmp4+"</div>";
	        mockStr+=
	        "</div>";				
		}
	  }	
		
	    model.addAttribute("mockStr",mockStr);
    	return "/Sales/Book_mock";
    } 
    
    @RequestMapping("/sales/getNoClassEvent")
    @ResponseBody
    public String getNoClassEvent(Model model,HttpSession session) {
    	String teacher_id = (String) session.getAttribute("teacher_id_hidden");
    	List<NoClass> LNoClass = salesService.getNoClass(teacher_id,"","");
    	
    	String jsonMsg = null;
        List<Event> events = new ArrayList<Event>();
     
        try {    		  
    		    for(int j=0;j<LNoClass.size();j++) { 	    
    	    	    Event event = new Event();  
    	    	    event.setTitle("");
    	    	    if(LNoClass.get(j).getNoClassType().equals("1")) {
    	    	    	event.setTitle("<div style='background-color:lightcoral;color:white'><span style='background-color:white'><img src='/images/no1.png' height='15px'/></span>????????????("+LNoClass.get(j).getTimeFrom()+"~"+LNoClass.get(j).getTimeTo()+")</div>");
    	    	    }else if(LNoClass.get(j).getNoClassType().equals("2")) {
    	    	    	event.setTitle("<div style='background-color:lightcoral;color:white'><span style='background-color:white'><img src='/images/no2.png' height='15px'/></span>?????????("+LNoClass.get(j).getTimeFrom()+"~"+LNoClass.get(j).getTimeTo()+")</div>");
    	    	    }	
    	    	    event.setStart(LNoClass.get(j).getDateSel());
    	            events.add(event);   	                	    	
    	        }
 
              ObjectMapper mapper = new ObjectMapper();
              jsonMsg =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);

          }catch(IOException ioex) {
                System.out.println(ioex.getMessage());
          }         
          return jsonMsg; 
      }   
    
    
    @RequestMapping("/Sales/saveRegister_mockBook")
    @ResponseBody
    public String saveRegister_mockBook(Model model,HttpServletRequest request,HttpSession session,Principal principal) {

    	String mockSet_id  = request.getParameter("mockSet_id");
    	String mockDetail_id     = request.getParameter("mockDetail_id");
    	String Register_seq  = request.getParameter("Register_seq");
 	    String mock_id    = request.getParameter("mock_id");
        Register_mockBook register_mockBook = new Register_mockBook("",Register_seq,mock_id,mockDetail_id,mockSet_id,"","","-1","-1");
 	    salesService.saveRegister_mockBook(register_mockBook);
 	    return "successful";
    }  
    

    @RequestMapping(value="/Sales/Book_counseling")
    public String Book_counseling(Model model,HttpServletRequest request,Principal principal) {
    	String student_seq = request.getParameter("student_seq");
    	String school_code = "";
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				school_code = LStudent.get(0).getSchool_code();
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			}  
    	}
        //???????????????????????????	
        if(school_code.equals("")) {school_code = "TPXC";}
        
  	  List<Register> LRegister = salesService.getRegister(student_seq,"");
  	  String counselingStr = "";
  	  String tmp1 = "";
  	  String tmp3 = "";
  	  String tmp4 = "";
  	  for(int i=0;i<LRegister.size();i++) { //????????????
  		tmp1 = "";
  		tmp3 = "";
  		tmp4 = "";
  		List<Register_counseling> LRegister_counseling = salesService.getCounselingByRegisterSeq(LRegister.get(i).getRegister_seq(),"");
  		if(LRegister_counseling.size()>0) {
  		  tmp1+=
  		  "<div class='tr' style=''>"+
  			"<div class='td2' style='width:80px;font-size:xx-small;border-bottom:1px dashed #dddddd;letter-spacing:0px'>"+LRegister.get(i).getUpdate_time()+"<br>"+LRegister.get(i).getCreater()+"</div>"+
  			"<div class='td2' style='width:350px;border-bottom:1px dashed #dddddd'>"; 		  		  
		  		  for(int j=0;j<LRegister_counseling.size();j++) { //??????????????????Counseling	
		  			ComboSale comboSale = courseSaleService.getComboSale(LRegister_counseling.get(j).getComboSale_id(),"","","","","","","0").get(0);
		  			tmp1+="<div style='padding:5px'><span style='font-size:x-small'>["+comboSale.getName()+"]</span> - <span style='font-size:small;font-weight:bold'>"+LRegister_counseling.get(j).getCounseling_name()+"</div>";

  			        String bookImg = "<img src='/images/book.jpg' height='15px' title='??????' />";
  			        String attend = "";
  			        String setDate = "";
		  			    
                        List<CounselingBaseBook> LCounselingBaseBook = salesService.getCounselingBaseBook(student_seq,"1","","","","",LRegister_counseling.get(j).getRegister_counseling_seq(),"","","");
                        if(LCounselingBaseBook.size()>0) {
                        	attend = LCounselingBaseBook.get(0).getAttend();
                        	if(attend.equals("1")) {  //??????                                              	
                            	setDate = LCounselingBaseBook.get(0).getSetDate();
                            	
			  			        tmp3+=
			  					"<div class='css-table'>"+
			  						"<div class='tr' style='font-weight:normal'>"+
			  							"<div class='th3'><A href='javascript:void(0)' style='color:blue;text-decoration:underline' onclick=cancelCounselingBook('"+student_seq+"','"+LCounselingBaseBook.get(0).getCounselingBaseBook_seq()+"')>"+setDate.substring(4,6)+"/"+setDate.substring(6,8)+"</A></div>"+
			  				        "</div>"+
			  				    "</div>"; 
                        	}else if(attend.equals("2")) {  //??????
			  			        tmp3+=
			  					"<div class='css-table'>"+
			  						"<div class='tr' style='font-weight:normal'>"+
			  							"<div class='th3'><img src='/images/GreenSquare.png' height='14px'/></div>"+
			  				        "</div>"+
			  				    "</div>";                         			
                        	}else {
                				tmp3+=
			  					"<div class='css-table'>"+
			  						"<div class='tr' style='font-weight:normal'>"+
			  							"<div class='th3'><A href='javascript:void(0)' onclick=openCounselingBaseTitle('"+student_seq+"','"+school_code+"','"+LRegister.get(i).getRegister_seq()+"','"+LRegister_counseling.get(j).getRegister_counseling_seq()+"','"+LRegister_counseling.get(j).getCategory_id()+"')>"+bookImg+"</A></div>"+
			  				        "</div>"+
			  				    "</div>";                                                 		
                        	}
    			  				tmp4+=
							  	"<div class='css-table'>"+
								  		"<div class='tr' style='font-weight:normal'>"+
				                    		"<div class='th3'><A href='javascript:void(0)'  onclick=getCounselingBaseBook('"+student_seq+"','"+ LCounselingBaseBook.get(0).getCounselingBase_id()+"')  style='color:blue;text-decoration:underline;font-weight:bold'>&hellip;</A></div>"+
								  		"</div>"+
				                "</div>";                         	
                        }else {
		  			        tmp3+=
		  					"<div class='css-table'>"+
		  						"<div class='tr' style='font-weight:normal'>"+
		  							"<div class='th3'><A href='javascript:void(0)' onclick=openCounselingBaseTitle('"+student_seq+"','"+school_code+"','"+LRegister.get(i).getRegister_seq()+"','"+LRegister_counseling.get(j).getRegister_counseling_seq()+"','"+LRegister_counseling.get(j).getCategory_id()+"')>"+bookImg+"</A></div>"+
		  				        "</div>"+
		  				    "</div>";                                               	
                        } 
                        
	                        
		  		  }
		 tmp1+="</div>";  
  		 
  		     			
            tmp1+=
  			"<div class='td2' style='width:80px;border-bottom:1px dashed #dddddd' align='center'>"+tmp3+"</div>"+
  			"<div class='td2' style='width:100px;border-bottom:1px dashed #dddddd' align='center'>"+tmp4+"</div>";
  			
		  			
  		  tmp1+=
  		 "</div>";				
  		}
  		counselingStr+= tmp1;
  	  }	
  		model.addAttribute("counselingStr",counselingStr);    	
    	return "/Sales/Book_counseling";
    } 
    
    @RequestMapping(value="/Sales/openCommentEdit")
    public String openCommentEdit(Model model,Principal principal) {
    	model.addAttribute("account0",principal.getName());
        return "/Sales/openCommentEdit";
    }
    
    @RequestMapping(value="/Sales/openHandover")
    public String openHandover(Model model,Principal principal) {
    	model.addAttribute("account0",principal.getName());
        return "/Sales/openHandover";
    }    
    
    @RequestMapping(value="/Sales/interestSubject")
    public String interestSubject(HttpServletRequest request) {
        return "/Sales/interestSubject";
    }      
      
      
    @RequestMapping("/Sales/bookSubjectCancel")
    @ResponseBody
    public String bookSubjectCancel(Model model,HttpServletRequest request,HttpSession session,Principal principal) {

    	String Register_comboSale_seq  = request.getParameter("Register_comboSale_seq");
 	    salesService.bookSubjectCancel(Register_comboSale_seq);
 	    return "successful";
    } 
    
    @RequestMapping(value="/Sales/pay_status")
    public String pay_status(Model model,HttpServletRequest request,Principal principal) {
    	String ch_name = accountService.getAccountByID("",principal.getName()).getCh_name();
    	model.addAttribute("creater",ch_name);
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    	model.addAttribute("createTime",formatter.format(new Date())); 
    	String Register_comboSale_grade_seq = request.getParameter("Register_comboSale_grade_seq");
    	model.addAttribute("Register_comboSale_grade_seq",Register_comboSale_grade_seq);    	
        return "/Sales/pay_status";
    }    

/**    
    @RequestMapping("/Sales/pay_statusSave")
    @ResponseBody
    public String pay_statusSave(Model model,HttpServletRequest request,HttpSession session,Principal principal) {
        String Register_comboSale_id = request.getParameter("Register_comboSale_id"); 
    	String Register_comboSale_grade_seq = request.getParameter("Register_comboSale_grade_seq");
    	String allow_attend =  request.getParameter("allow_attend");
    	String creater = accountService.getAccountByID("",principal.getName()).getCh_name();
 	    salesService.pay_statusSave(Register_comboSale_id,Register_comboSale_grade_seq,allow_attend,creater);
 	    return "successful";
    }
**/    
    @RequestMapping(value="/Sales/gradeChange")
    public String gradeChange(Model model,HttpServletRequest request) {
    	String Register_comboSale_grade_seq = request.getParameter("Register_comboSale_grade_seq");   	   	
    	String order_grade = request.getParameter("order_grade");   	
    	String student_seq = request.getParameter("student_seq");    	
    	String register_id = request.getParameter("register_id");
    	Register_log register_log = new Register_log();
    	
    	model.addAttribute("Register_comboSale_grade_seq",Register_comboSale_grade_seq);
    	model.addAttribute("order_grade",order_grade);
    	model.addAttribute("student_seq",student_seq);
    	model.addAttribute("register_id",register_id);
    	model.addAttribute("register_log_seq","-1");
    	model.addAttribute("register_log",register_log);
    	
    	
        return "/Sales/gradeChange";
    }
    
    @RequestMapping(value="/Sales/gradeChange2")
    public String gradeChange2(Model model,HttpServletRequest request) {
    	String Register_comboSale_grade_seq = request.getParameter("Register_comboSale_grade_seq");   	
    	String student_seq = request.getParameter("student_seq");    	
    	String register_log_seq = request.getParameter("register_log_seq");
    	Register_log register_log = admService.getRegisterLog("",register_log_seq).get(0);
    	Register register = salesService.getRegister3(Register_comboSale_grade_seq).get(0);
       
    	Register_comboSale_grade register_comboSale_grade = courseService.getRegister_comboSale_grade("",Register_comboSale_grade_seq,"","").get(0);
    	
    	Grade grade = courseService.getGrade(register_comboSale_grade.getGrade_id(),"","","","","","","","","","","","").get(0);
    	String order_grade = grade.getGrade_date()+" "+grade.getSubject_name();

    	model.addAttribute("register_log_seq",register_log_seq);
    	model.addAttribute("Register_comboSale_grade_seq",Register_comboSale_grade_seq);
    	model.addAttribute("order_grade",order_grade);
    	model.addAttribute("student_seq",student_seq);
    	model.addAttribute("register_id",register.getRegister_seq());
    	model.addAttribute("register_log",register_log);
    
    	
        return "/Sales/gradeChange";
    }     
    
    @RequestMapping(value="/Sales/openReceipDetail")
    public String openReceipDetail(Model model,HttpServletRequest request) {
		String register_id = request.getParameter("register_id");
		model.addAttribute("register_id",register_id);
        return "/Sales/openReceipDetail";
    }
    
    @RequestMapping(value="/Sales/openPayRecord")
    public String openPayRecord(Model model,HttpServletRequest request) {
		String studentPayRecord_id = request.getParameter("studentPayRecord_id");
		model.addAttribute("studentPayRecord_id",studentPayRecord_id);
        return "/Sales/openPayRecord";
    }    

    @RequestMapping(value="/Sales/cancelRegister")
    @ResponseBody
    public String cancelRegister(Model model,HttpServletRequest request,Principal principal,HttpSession session) {
		String Register_seq = request.getParameter("Register_seq");
		String commentThis = request.getParameter("commentThis");
		String cashRefund = request.getParameter("cashRefund");
		String saveAccount = request.getParameter("saveAccount");
		String student_seq = request.getParameter("student_seq");
		String makeUpRefund = request.getParameter("makeUpRefund");

    	String commentTotal = commentThis;
    	if(Register_seq != null && !Register_seq.isEmpty()) {
    		commentTotal = commentThis+"\n"+salesService.getRegister("",Register_seq).get(0).getComment();
    	}
    	//????????????
		salesService.cancelRegister(Register_seq,principal.getName(),commentTotal,cashRefund,saveAccount,student_seq);
		
		//???????????? register_status=2:??????
		List<Register_comboSale_grade> LRegister_comboSale_grade = salesService.getRegister_comboSale_grade(Register_seq,"1","","");
		for(int a=0;a<LRegister_comboSale_grade.size();a++) {
	 		jdbcTemplate.update("Update eip.Register_comboSale_grade set register_status=2 where Register_comboSale_grade_seq=?;",
	 				LRegister_comboSale_grade.get(a).getRegister_comboSale_grade_seq()
	 		); 			
		}

        //??????eip.student.[remarkTotal]????????????
 		
 		if(commentThis!=null && !commentThis.isEmpty()) {
 			String remarkTotalOri = salesService.getStudent(student_seq,"","","","","","","","","").get(0).getRemarkTotal();
 			String remarkTotal = commentThis+"\n"+remarkTotalOri;	
	 		jdbcTemplate.update("Update eip.student set remarkTotal=?,update_time=NOW() where student_seq=?;",
	 				remarkTotal, 
	 				student_seq
	 		); 
 		}
		
	     //????????????
	    String experience_id = "8";
	    String schoolCode = accountService.getAccountByID("",principal.getName()).getSchool();
	    salesService.insertStudentExperience(student_seq,schoolCode,experience_id,"","","",principal.getName(),Register_seq); 
		
	    //??????Video??????
	    Student student = salesService.getStudent(student_seq,"","","","","","","","","").get(0);
	    int makeUpTotal = Integer.valueOf(student.getMakeUpTotal());

	    if(makeUpTotal>=Integer.valueOf(makeUpRefund)) {
	    	makeUpTotal = makeUpTotal-Integer.valueOf(makeUpRefund);
	    }else {
	    	makeUpTotal = 0;
	    }
		jdbcTemplate.update("UPDATE eip.student set makeUpTotal=? where student_seq=?;", 
				makeUpTotal,
				student_seq
		);
		
		//?????????????????????????????????
		String openRegister = "<A href='javascript:void(0)' onclick='openRegister("+Register_seq+","+student_seq+")' style='color:blue;font-size:small;font-weight:bold;text-decoration:underline'>no."+Register_seq+"</A>";
		jdbcTemplate.update("INSERT INTO eip.makeUpRecord VALUES (default,?,?,?,?,?,?,NOW(),?);", 
				student_seq, 
				-2, //??????????????????
				makeUpRefund,
				openRegister, 
				"", 
				principal.getName(),
				Register_seq
		);			
	    return "success";
    }
    
    @RequestMapping(value="/Sales/studentHistory")
    public String studentHistory(Model model,HttpServletRequest request) {
    	String student_seq = request.getParameter("student_seq");
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			}  

			List<School> LSchool = accountService.getSchool("",""); 
			model.addAttribute("LSchool", LSchool);
			List<ExperienceHistory> LExperienceHistory = salesService.getExperienceHistory(); 
			model.addAttribute("LExperienceHistory", LExperienceHistory);		
	        return "/Sales/studentHistory";
		}    
    }
       
    @RequestMapping(value="/Sales/getStudentExperience")
    @ResponseBody
    public List<StudentExperience> getStudentExperience(Model model,HttpServletRequest request) {
    	String student_seq = request.getParameter("student_seq");
    	String experience_id = request.getParameter("experience_id");
    	List<StudentExperience> LStudentExperience = salesService.getStudentExperience(student_seq,experience_id,"",""); 
        return LStudentExperience;
    } 
    
    @RequestMapping(value="/Sales/studentHistoryEdit")
    public String studentHistoryEdit(Model model,HttpServletRequest request) {
    	String student_seq = request.getParameter("student_seq");
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			} 
		}	
		List<ExperienceHistory> LExperienceHistory = salesService.getExperienceHistory(); 
		model.addAttribute("LExperienceHistory", LExperienceHistory);
		
    	StudentExperience studentExperience = new StudentExperience();
    	String studentExperience_seq = request.getParameter("studentExperience_seq");
    	if(studentExperience_seq!=null) {
    		studentExperience = salesService.getStudentExperience("","",studentExperience_seq,"").get(0);
    	}
    	model.addAttribute("studentExperience",studentExperience);
    	
        return "/Sales/studentHistoryEdit";
    }  
    
     
    @RequestMapping("/Sales/studentExperienceSave")
    public String studentExperienceSave(Model model,HttpServletRequest request,@ModelAttribute StudentExperience studentExperience,Principal principal,RedirectAttributes redirectAttributes) {
    	String student_seq = request.getParameter("student_seq");
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			} 
		}	

    	String school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
    	String Register_id = "";
    	salesService.insertStudentExperience(student_seq,school_code,studentExperience.getExperience_id(),studentExperience.getExperience_state(),studentExperience.getExperience_content(),studentExperience.getValidDate(),principal.getName(),Register_id);
    	return "redirect:/Sales/studentHistory?student_seq="+student_seq;        
    }
    
    @RequestMapping("/Sales/studentExperienceSave2")
    public String studentExperienceSave2(Model model,HttpServletRequest request,Principal principal,RedirectAttributes redirectAttributes) {
    	String student_seq = request.getParameter("student_seq");
    	String experience_content = request.getParameter("experience_content");
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}

    	String school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
    	salesService.insertStudentExperience(student_seq,school_code,"4","",experience_content,"",principal.getName(),"");
    	
    	return "redirect:/Sales/SpecialCase?student_seq="+student_seq;        
    }    

    @RequestMapping(value="/Sales/OutPublisher")
    public String OutPublisher(Model model,HttpServletRequest request) {
    	String student_seq = request.getParameter("student_seq");
    	if(student_seq==null) {
			return "/Sales/Student"; 
		}else {
	    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
			if(LStudent.size()>0) {
				String ch_name = LStudent.get(0).getCh_name();
				String student_no = LStudent.get(0).getStudent_no();
	    		model.addAttribute("student_seq",student_seq);
	    		model.addAttribute("ch_name",ch_name);
	    		model.addAttribute("student_no",student_no);
			}
		}	
        return "/Sales/OutPublisher";
    }
    
    
    @RequestMapping(value="/Sales/checkStudentProfile_DirtyRead")
    @ResponseBody
    public String checkStudentProfile_DirtyRead(Model model,HttpServletRequest request) {
    	String message = "";
    	String student_seq = request.getParameter("student_seq");
    	String ch_name = request.getParameter("ch_name");
    	String mobile_1 = request.getParameter("mobile_1");
    	String update_time = request.getParameter("update_time");
    	List<Student> LStudent = salesService.getStudent("",ch_name,"","","",mobile_1,"","","","");
    	//????????????,???????????????,????????????????????????
    	if(student_seq!=null && !student_seq.isEmpty()){
    		if(LStudent.get(0).getUpdate_time()!=null && !LStudent.get(0).getUpdate_time().isEmpty() && LStudent.size()>0) {
    			if(!LStudent.get(0).getUpdate_time().equals(update_time)) {
    				message = "existNew";
    			}
    		}
    	//????????????,????????????????????????
    	}else{	
    		if(LStudent.size()>0) {
    			message = "duplicate";
    		}
    	}    	
        return message;
    } 
       
    @RequestMapping(value="/Sales/openStudentRecord")
    public String openStudentRecord(Model model,HttpServletRequest request) {
    	model.addAttribute("student_seq",request.getParameter("student_seq"));
        return "/Sales/openStudentRecord";
    } 
    
    @RequestMapping("/Sales/SpecialCase")
    public String SpecialCase(Model model,HttpServletRequest request) {
		String student_seq = request.getParameter("student_seq");
    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
		if(LStudent.size()>0) {
			String ch_name = LStudent.get(0).getCh_name();
			String student_no = LStudent.get(0).getStudent_no();
			model.addAttribute("student_seq",student_seq);
    		model.addAttribute("ch_name",ch_name);
    		model.addAttribute("student_no",student_no);
		}

		List<StudentExperience> LStudentExperience = salesService.getStudentExperience(student_seq,"4","","");
		String experience_contentStr = "";
		for(int i=0;i<LStudentExperience.size();i++) {
			String tmp = LStudentExperience.get(i).getExperience_content().replaceAll("\n", "<br />");
			experience_contentStr += 
			"<div class='tr' style='font-size:small'>"+
				"<div class='td2' style='width:120px;height:50px;border:1px #dddddd solid;vertical-align:middle'><div style='letter-spacing:0px;text-align:center;font-size:x-small'>"+LStudentExperience.get(i).getCreater()+"<br>"+LStudentExperience.get(i).getCreateTime()+"</div></div>"+		
				"<div class='td2' style='width:450px;height:50px;border:1px #dddddd solid'><div style='letter-spacing:0px'>"+tmp+"</div></div>"+
		    "</div>";
		}

		model.addAttribute("experience_contentStr",experience_contentStr);
        return "/Sales/SpecialCase";
    }
    
    
    @RequestMapping(value="/Sales/studentContract")
    public String studentContract(Model model,HttpServletRequest request,@Value("${UploadPath}") String UploadPath,Principal principal) {
		String school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("school_code",school_code);
		
    	String student_seq = request.getParameter("student_seq");
    	List<Student> LStudent = salesService.getStudent(student_seq,"","","","","","","","","");
		if(LStudent.size()>0) {
			String ch_name = LStudent.get(0).getCh_name();
			String student_no = LStudent.get(0).getStudent_no();
    		model.addAttribute("student_seq",student_seq);
    		model.addAttribute("ch_name",ch_name);
    		model.addAttribute("student_no",student_no);
		}
		
		List<String> Lfile = new ArrayList<String>();
		File[] files = new File(UploadPath+"/studentContract/").listFiles();
		for (File file : files) {
		    if (file.isFile()) {
		    	if(file.getName().contains("_"+student_seq+"_")) {
		    		Lfile.add(file.getName());
		    	}
		    }
		}
		Collections.reverse(Lfile);
		String LfileStr = "";
		for(int i=0;i<Lfile.size();i++) {
			LfileStr += "<div style='padding:2px'><A href='javascript:void(0)' onclick=studentContractDelete('"+Lfile.get(i)+"')><img src='/images/delete.png' height='10px'/></A>&nbsp;&nbsp;&nbsp;&nbsp;<a href='/images/studentContract/"+Lfile.get(i)+"' style='color:blue;text-decoration:underline'>"+Lfile.get(i)+"</a></div>";
		}
		model.addAttribute("LfileStr",LfileStr);			
		
		
        return "/Sales/studentContract";
    }
    
    @RequestMapping("/Sales/ClassBook2")
        public String ClassBook2(Model model,HttpServletRequest request,HttpSession session) {
        	String backURL = request.getRequestURL().append('?').append(request.getQueryString()).toString();
        	session.setAttribute("backURL",backURL);
        	model.addAttribute("backURL",backURL);
        	
        	
    		String mockVideoHistory_seq = request.getParameter("mockVideoHistory_seq");
    		model.addAttribute("mockVideoHistory_seq",mockVideoHistory_seq);
    		
    		String student_seq = request.getParameter("student_seq");
    		model.addAttribute("student_seq",student_seq);
    		
        	String school_code = request.getParameter("school_code");
     
        	model.addAttribute("school_code",school_code);   
        	
    		List<School> LSchool = accountService.getSchool("","");
    		model.addAttribute("LSchool",LSchool);	
    		
    		
    		String makeUpNo = request.getParameter("makeUpNo");
    		model.addAttribute("makeUpNo",makeUpNo);
    		
    		//???????????????(????????????)	
    		  SimpleDateFormat sdFormat0 = new SimpleDateFormat("yyyy-MM-dd"); 
    		  String curDate = sdFormat0.format(new Date());
    		  model.addAttribute("curDate",curDate);
    		  
    		  
    		//?????????????????????
    		  /**
    		if(request.getParameter("cancelFlag")!=null) {
    				List<Register> LRegister = salesService.getRegister3(Register_comboSale_grade_id);
    				model.addAttribute("register_id",LRegister.get(0).getRegister_seq());
    				return "/Sales/ClassBookCancel";
    		}
    		**/		  

            return "/Sales/ClassBook2";
    }
    
    @RequestMapping(value="/Sales/studentContractDelete")
    @ResponseBody
    public String studentContractDelete(Model model,HttpServletRequest request,@Value("${UploadPath}") String UploadPath) {  
    	String fileName = request.getParameter("fileName");
    	File file_or = new File(UploadPath+"/studentContract/"+fileName);
    	File file_new = new File(UploadPath+"/studentContract/"+fileName.replace('_','@'));
    	boolean success = file_or.renameTo(file_new);
    	return "success";
    }
    
    @RequestMapping(value="/Sales/deleteUploadFile")
    @ResponseBody
    public String deleteUploadFile(Model model,HttpServletRequest request,@Value("${UploadPath}") String UploadPath) {  
    	String fileName = request.getParameter("fileName");
    	File file = new File(UploadPath+"/consult/"+fileName);
        if(file.exists()){
            file.delete();
        }    	
    	return "success";
    }  

    @RequestMapping("/Sales/updateMockBaseBook")
    public String updateMockBaseBook(Model model,HttpServletRequest request,Principal principal) {
    	String register_mock_seq = request.getParameter("register_mock_seq");
		String student_seq = request.getParameter("student_seq");
		String mockBase_id = request.getParameter("mockBase_id");
		String mockDetail_id = request.getParameter("mockDetail_id");
		String setDate = request.getParameter("setDate");
		String slot = request.getParameter("slot");
		String school_code = request.getParameter("school_code");
		String mockPanel_id = request.getParameter("mockPanel_id");
		String testSubjectSelection2_seq = request.getParameter("testSubjectSelection2_seq");
		String panelName = "";
		if(mockPanel_id!=null && !mockPanel_id.isEmpty()) {
			List<MockPanel> LMockPanel =  admService.getMockPanel("",mockPanel_id);
			panelName = LMockPanel.get(0).getPanelName();
		}

		salesService.updateMockBaseBook(student_seq,mockBase_id,principal.getName(),mockDetail_id,setDate,slot,school_code,panelName,testSubjectSelection2_seq,register_mock_seq);    	
		return "/common/closeAndReload"; 
    }
    
    
    @RequestMapping("/Sales/updateCounselingBaseBook")
    public String updateCounselingBaseBook(Model model,HttpServletRequest request,Principal principal) {
    	String register_counseling_seq = request.getParameter("register_counseling_seq");
		String student_seq = request.getParameter("student_seq");
		String counselingBase_id = request.getParameter("counselingBase_id");
		String setDate = request.getParameter("setDate");
		String slot = request.getParameter("slot");
		String school_code = request.getParameter("school_code");
		String counselingLimit_seq = request.getParameter("counselingLimit_seq");

		if(slot==null || slot.isEmpty()) {slot="3";} //????????????????????????
		salesService.updateCounselingBaseBook(student_seq,counselingBase_id,principal.getName(),setDate,slot,school_code,counselingLimit_seq,register_counseling_seq);    	
		return "/common/closeAndReload"; 
    }    
    
    
    @RequestMapping("/Sales/updateLimitDate")
    public String updateLimitDate(Model model,HttpServletRequest request,Principal principal) {
		String student_seq = request.getParameter("student_seq");
		String mockBase_id = request.getParameter("mockBase_id");
		String mockDetail_id = request.getParameter("mockDetail_id");
		String setDate = request.getParameter("setDate");
		String slot = request.getParameter("slot");
		String school_code = request.getParameter("school_code");
		String mockPanel_id = request.getParameter("mockPanel_id");
		String limitName = request.getParameter("limitName");
		List<MockPanel> LMockPanel =  admService.getMockPanel("",mockPanel_id);
		String panelName = LMockPanel.get(0).getPanelName();
		//salesService.updateMockBaseBook(student_seq,mockBase_id,principal.getName(),mockDetail_id,setDate,slot,school_code,panelName,limitName,mockBaseBook_seq);    	
		return "/common/closeAndReload"; 
    }    
    
    @RequestMapping("/Sales/MockVideoUpdate")
    @ResponseBody
    public Boolean MockVideoUpdate(Model model,HttpServletRequest request,Principal principal) {
       
    	String mockVideoHistory_seq = request.getParameter("mockVideoHistory_seq");
    	String school_code = request.getParameter("school_code");  	
    	String comment = request.getParameter("comment");
    	String attend_date = request.getParameter("attend_date");   	
    	String slot = request.getParameter("slot");   	
    	salesService.MockVideoUpdate(mockVideoHistory_seq,school_code,comment,attend_date.replace("-",""),slot,principal.getName());
    	return true;
    }
        
    @RequestMapping("/Sales/cancelMockBook")
    @ResponseBody
    public Boolean cancelMockBook(Model model,HttpServletRequest request,Principal principal) {      
    	String student_id = request.getParameter("student_id");
    	String mockBaseBook_seq = request.getParameter("mockBaseBook_seq");  	 	
    	salesService.cancelMockBook(student_id,mockBaseBook_seq,principal.getName());
    	return true;
    }
    
    @RequestMapping("/Sales/cancelCounselingBook")
    @ResponseBody
    public Boolean cancelCounselingBook(Model model,HttpServletRequest request,Principal principal) {      
    	String student_id = request.getParameter("student_id");
    	String counselingBaseBook_seq = request.getParameter("counselingBaseBook_seq");  	 	
    	salesService.cancelCounselingBook(student_id,counselingBaseBook_seq,principal.getName());
    	return true;
    } 
 
    @RequestMapping("/Sales/cancelMockVideo")
    @ResponseBody
    public Boolean cancelMockVideo(Model model,HttpServletRequest request,Principal principal) {      
    	String student_id = request.getParameter("student_id");
    	String mockVideoHistory_seq = request.getParameter("mockVideoHistory_seq");  	 	
    	salesService.cancelMockVideo(student_id,mockVideoHistory_seq,principal.getName());
    	return true;
    }     
    
    
    @RequestMapping("/Sales/mockBaseBookHistory")
    public String mockBaseBookHistory(Model model,HttpServletRequest request,Principal principal) {
    	String student_seq = request.getParameter("student_seq");
    	String mockBase_id = request.getParameter("mockBase_id");
    		
    	List<MockBaseBook2> LMockBaseBook2 = salesService.getMockBaseBook(student_seq,"","","","","","","","",mockBase_id);
		String returnStr = 
		"<div class='css-table'>";
		for(int x=0;x<LMockBaseBook2.size();x++) {
			String attend = LMockBaseBook2.get(x).getAttend();
			String slot = LMockBaseBook2.get(x).getSlot();
			if(attend.equalsIgnoreCase("1")) {
				attend = "??????";
			}else if(attend.equalsIgnoreCase("2")) {
				attend = "??????";
			}else if(attend.equalsIgnoreCase("3")) {
				attend = "??????";
			}else if(attend.equalsIgnoreCase("-1")) {
				attend = "??????";
			}
			
			if(slot.equals("1")) {
				slot="???";
			}else if(slot.equals("2")) {
				slot="???";
			}else if(slot.equals("3")) {
				slot="???";
			}
			
			returnStr += 
			"<div class='tr' style='font-size:small'>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:100px;font-size:xx-small;letter-spacing:0px'>"+LMockBaseBook2.get(x).getUpdate_time()+"</div>"+ 
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:100px'>"+LMockBaseBook2.get(x).getUpdater()+"</div>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:50px'>"+attend+"</div>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:100px'>"+LMockBaseBook2.get(x).getSetDate()+"</div>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:50px;text-align:center'>"+slot+"</div>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:150px'>"+LMockBaseBook2.get(x).getComment()+"</div>"+
			"</div>";
		}
		returnStr += 
		"</div>";
		
		model.addAttribute("returnStr",returnStr);
		
		return "/Sales/mockBaseBookHistory";
    }
        
    
    @RequestMapping("/Sales/mockVideoHistory")
    public String mockVideoHistory(Model model,HttpServletRequest request,Principal principal) {
    	String student_id = request.getParameter("student_id");
    	String mockVideo_id = request.getParameter("mockVideo_id");
    	String register_mock_id = request.getParameter("register_mock_id");
    	
    	List<MockVideoHistory> LMockVideoHistory = courseService.getMockVideoHistory(register_mock_id,"",mockVideo_id,"",student_id,"","","","","");
		String returnStr = 
		"<div class='css-table'>";
		for(int x=0;x<LMockVideoHistory.size();x++) {
			String attend = LMockVideoHistory.get(x).getAttend();
			String slot = LMockVideoHistory.get(x).getSlot();
			String register_status = LMockVideoHistory.get(x).getRegister_status();
			
			if(attend.equals("1")) {
				attend = "??????";
			}else if(attend.equals("-1")) {
				attend = "??????";
			}else {
				attend = "";
			}
			
			if(slot.equals("1")) {
				slot="???";
			}else if(slot.equals("2")) {
				slot="???";
			}else if(slot.equals("3")) {
				slot="???";
			}else {
				slot="";
			}
			
			if(register_status.equals("1")) {
				register_status="??????";
			}else if(register_status.equals("2")) {
				register_status="??????";
			}else if(register_status.equals("3")) {
				register_status="??????";
			}			
			
			returnStr += 
			"<div class='tr' style='font-size:small'>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:100px;font-size:xx-small;letter-spacing:0px'>"+LMockVideoHistory.get(x).getUpdate_time()+"</div>"+ 
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:100px'>"+LMockVideoHistory.get(x).getUpdater()+"</div>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:50px;text-align:center'>"+register_status+"</div>"+					
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:50px;text-align:center'>"+attend+"</div>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:100px'>"+LMockVideoHistory.get(x).getAttend_date()+"</div>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:50px;text-align:center'>"+slot+"</div>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:150px'>"+LMockVideoHistory.get(x).getComment()+"</div>"+
			"</div>";
		}
		returnStr += 
		"</div>";
		
		model.addAttribute("returnStr",returnStr);    		   	
    	return "/Sales/mockVideoHistory";
    }
    
    

    @RequestMapping("/Sales/getCounselingBaseBook")
    public String getCounselingBaseBook(Model model,HttpServletRequest request,Principal principal) {
    	String student_id = request.getParameter("student_id");
    	String counselingBase_id = request.getParameter("counselingBase_id");
    	List<CounselingBaseBook> LCounselingBaseBook = salesService.getCounselingBaseBook(student_id,"","","","","","","",counselingBase_id,"");
		String returnStr = 
		"<div class='css-table'>";
		for(int x=0;x<LCounselingBaseBook.size();x++) {
			String attend = LCounselingBaseBook.get(x).getAttend();
			String slot = LCounselingBaseBook.get(x).getSlot();
			
			if(attend.equalsIgnoreCase("1")) {
				attend = "??????";
			}else if(attend.equalsIgnoreCase("2")) {
				attend = "??????";
			}else if(attend.equalsIgnoreCase("3")) {
				attend = "??????";
			}else if(attend.equalsIgnoreCase("-1")) {
				attend = "??????";
			}
			
			if(slot.equals("1")) {
				slot="???";
			}else if(slot.equals("2")) {
				slot="???";
			}else if(slot.equals("3")) {
				slot="???";
			}else {
				slot="";
			}
				
			
			returnStr += 
			"<div class='tr' style='font-size:small'>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:100px;font-size:xx-small;letter-spacing:0px'>"+LCounselingBaseBook.get(x).getUpdate_time()+"</div>"+ 
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:100px'>"+LCounselingBaseBook.get(x).getUpdater()+"</div>"+			
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:50px;text-align:center'>"+attend+"</div>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:100px;font-size:xx-small;letter-spacing:0px;text-align:center'>"+LCounselingBaseBook.get(x).getSetDate()+"</div>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:50px;text-align:center'>"+slot+"</div>"+
					"<div class='td2' style='padding:5px;border-bottom:1px #eeeeee solid;width:150px'>"+LCounselingBaseBook.get(x).getComment()+"</div>"+
			"</div>";
		}
		returnStr += 
		"</div>";    	

		model.addAttribute("returnStr",returnStr);    		   	
    	return "/Sales/getCounselingBaseBook";
    }  
    
       
    @RequestMapping(value="/Sales/openOnlineClass")
    public String openOnlineClass(Model model,HttpServletRequest request) {
    	 String class_id = request.getParameter("class_id");
		 List<OnlineClass> LOnlineClass = admService.getOnlineClass(class_id);
		 if(LOnlineClass.size()>0) {
			 model.addAttribute("link",LOnlineClass.get(0).getLink());
		 }    			

        return "/Sales/openOnlineClass";
    }    
}    



