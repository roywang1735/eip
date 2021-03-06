package com.wordgod.eip.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wordgod.eip.Model.*;
import com.wordgod.eip.Service.*;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class CourseController {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	CourseService courseService;
	@Autowired
	AccountService accountService;
	@Autowired
	ManagerService managerService;
	@Autowired
	SalesService salesService;
	@Autowired
	AdmService admService;
	@Autowired
	CourseSaleService courseSaleService;
	@Autowired
	MarketingService marketingService;
	@Autowired
	SystemService systemService;	

	@RequestMapping("/Course/CourseSetting")
	public String CourseSetting(Model model,Principal principal,HttpSession session,HttpServletRequest request) {
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);	
		
		String school_code = "";
		if(principal!=null && principal.getName()!=null) {	
			school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
			model.addAttribute("school_code",school_code);
		}
		
		if(request.getParameter("sessionState")!=null) {
			model.addAttribute("sessionState","keep");
			if(session.getAttribute("CourseController_school_code")!=null) {
				school_code = (String)session.getAttribute("CourseController_school_code");
			}  
			model.addAttribute("school_code",school_code);
			
			String category_id = "";	
		    if(session.getAttribute("CourseController_category_id")!=null) {						
				category_id = (String)session.getAttribute("CourseController_category_id");
			}
			model.addAttribute("category_id",category_id);
	
			String subject_id = "";		
			if(session.getAttribute("CourseController_subject_id")!=null) {
				subject_id = (String)session.getAttribute("CourseController_subject_id");
			}
			model.addAttribute("subject_id",subject_id);
			
			String status_code = "";		
			if(session.getAttribute("CourseController_status_code")!=null) {
				status_code = (String)session.getAttribute("CourseController_status_code");
			}		
			model.addAttribute("status_code",status_code);
			
			String teacher_id = "";
			if(session.getAttribute("CourseController_teacher_id")!=null) {
				teacher_id = (String)session.getAttribute("CourseController_teacher_id");
			}		
			model.addAttribute("teacher_id",teacher_id);			
		}else {
			session.removeAttribute("CourseController_school_code");
			session.removeAttribute("CourseController_category_id");
			session.removeAttribute("CourseController_subject_id");
			session.removeAttribute("CourseController_status_code");
			session.removeAttribute("CourseController_teacher_id");
		}
		
		return "/Course/CourseSetting";
	}
	
	@RequestMapping("/Course/SubjectSetting")
	public String SubjectSetting(Model model,HttpSession session,HttpServletRequest request){	
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		
		if(request.getParameter("sessionState")!=null) {
			model.addAttribute("sessionState","keep");
			String CourseController_category_id = (String)session.getAttribute("CourseController_category_id");
			if(CourseController_category_id!=null && !CourseController_category_id.isEmpty()) {
				model.addAttribute("CourseController_category_id", CourseController_category_id);
			}
		}else{
			session.removeAttribute("CourseController_category_id");
		}	
		return "/Course/SubjectSetting";
	}
	
	@RequestMapping("/Course/SubjectVer")
	public String SubjectVer(Model model,HttpServletRequest request){
		String subject_seq = request.getParameter("subject_seq");
		List<Subject_ver> LSubject_ver= courseService.getSubject_ver("",subject_seq,"","");
		String SubjectStr = "";
		String isVirtual = "";
		String isVideo = "";
		String active = "";
		
		for(int i=0;i<LSubject_ver.size();i++) {
			if(LSubject_ver.get(i).getIsVideo()!=null && LSubject_ver.get(i).getIsVideo().equals("0")) {isVideo = "可";}else {isVideo = "不可";}
			if(LSubject_ver.get(i).getIsVirtual()!=null && LSubject_ver.get(i).getIsVirtual().equals("1")) {isVirtual = "是";}else {isVirtual = "否";}
			if(LSubject_ver.get(i).getActive()!=null && LSubject_ver.get(i).getActive().equals("1")) {active = "啟用";}else {active = "停用";}
			SubjectStr +=
				"<div class='tr' style=''>"+
					"<div class='td2' style='width:100px;border-bottom:1px #eeeeee solid;text-align:center'>&nbsp;"+LSubject_ver.get(i).getUpdater()+"</div>"+
					"<div class='td2' style='width:100px;border-bottom:1px #eeeeee solid;text-align:center'>&nbsp;"+LSubject_ver.get(i).getUpdate_time()+"</div>"+			
					"<div class='td2' style='width:80px;border-bottom:1px #eeeeee solid;text-align:center'>&nbsp;"+LSubject_ver.get(i).getCategory_name()+"</div>"+ 
					"<div class='td2' style='width:200px;border-bottom:1px #eeeeee solid'>&nbsp;"+LSubject_ver.get(i).getName()+"</div>"+ 
					"<div class='td2' style='width:80px;border-bottom:1px #eeeeee solid;;text-align:center'>&nbsp;"+LSubject_ver.get(i).getAbbr()+"</div>"+ 
					"<div class='td2' style='width:100px;border-bottom:1px #eeeeee solid;text-align:right'>&nbsp;"+LSubject_ver.get(i).getPrice()+"</div>"+ 
					"<div class='td2' style='width:100px;border-bottom:1px #eeeeee solid;text-align:center'>&nbsp;"+LSubject_ver.get(i).getSubjectContent_name()+"</div>"+ 
					"<div class='td2' style='width:80px;border-bottom:1px #eeeeee solid;text-align:center'>&nbsp;"+LSubject_ver.get(i).getFree_makeUpNo()+"</div>"+ 
					"<div class='td2' style='width:80px;border-bottom:1px #eeeeee solid;text-align:center'>&nbsp;"+isVideo+"</div>"+ 
					"<div class='td2' style='width:80px;border-bottom:1px #eeeeee solid;text-align:center'>&nbsp;"+isVirtual+"</div>"+ 
					"<div class='td2' style='width:80px;border-bottom:1px #eeeeee solid;text-align:center'>&nbsp;"+active+"</div>"+ 
					"<div class='td2' style='width:200px;border-bottom:1px #eeeeee solid'>&nbsp;"+LSubject_ver.get(i).getRemark()+"</div>"+ 
				"</div>";	
		}
		model.addAttribute("SubjectStr",SubjectStr);
		return "/Course/SubjectVer";
	}	
	
	@RequestMapping("/Course/getSubjectSetting")
	@ResponseBody
	public List<Subject> getSubjectSetting(HttpServletRequest request,Principal principal,HttpSession session) {
		String category_id = "";
		if(request.getParameter("category_id")!=null) {
			session.setAttribute("CourseController_category_id",request.getParameter("category_id"));
			category_id = request.getParameter("category_id");
		}
		List<Subject> LSubject = courseService.getSubject(category_id,"","",request.getParameter("active"),"","0");
		
/**
		List<Account_authority> LAccount_authority = accountService.getAccount_authority(principal.getName());
		Boolean withApprove = false;
		for(int i=0;i<LAccount_authority.size();i++) {
			if(LAccount_authority.get(i).getAuthority().equals("ROLE_approve_mgr")) {
				withApprove = true;
			}
		}
		if(!withApprove) {
			for(int i=0;i<LSubject.size();i++) {
				LSubject.get(i).setHandoutPrice_R("");
				LSubject.get(i).setHrPrice_R("");
				LSubject.get(i).setCounselingPrice_R("");
				LSubject.get(i).setHomeworkPrice_R("");
				LSubject.get(i).setLagnappePrice_R("");
				LSubject.get(i).setMockPrice_R("");
			}
		}
**/
		String tmp = "";
		for(int i=0;i<LSubject.size();i++) {
			//List<SubjectTeacher> LSubjectTeacher =  courseService.getSubjectTeacher(LSubject.get(i).getSubject_seq());
			List<SubjectTeacher> LSubjectTeacher = courseService.getSubjectTeacher2("","",LSubject.get(i).getSubject_seq(),"","1");
			tmp  = "<div class='css-table' style='width:300px'>";
			for(int j=0;j<LSubjectTeacher.size();j++) {
				tmp += "<div class='tr' style='font-size:small'>";      
				      tmp += "<div class='td' style='border-radius:0px;width:100px;text-align:left;padding:3px'><A href='javascript:void(0)' onclick='teacherAndGradeEdit("+LSubjectTeacher.get(j).getSubjectTeacher_seq()+","+LSubjectTeacher.get(j).getTeacher_id()+","+LSubject.get(i).getSubject_seq()+");' style='color:blue;text-decoration:underline'>"+LSubjectTeacher.get(j).getTeacher_name()+"</A></div>";				      				      				      
				      tmp += "<div class='td' style='border-radius:0px;width:50px;text-align:center'>"+LSubjectTeacher.get(j).getGradeNo()+"</div>";
				      tmp += "<div class='td' style='border-radius:0px;width:50px;text-align:center'>"+LSubjectTeacher.get(j).getGradeNoATime()+"</div>";
				      tmp += "<div class='td' style='border-radius:0px;width:50px;text-align:center'>"+(LSubjectTeacher.get(j).getIsPreGrade().equals("1")?"&check;":"-")+"</div>";
				      tmp += "<div class='td' style='border-radius:0px;width:50px;text-align:center'>"+LSubjectTeacher.get(j).getTotalClassNo()+"</div>";
			    tmp += "</div>";
			}
			    tmp +="<div><div class='td' style='border-radius:0px;width:100px;text-align:right;padding:3px'><A href='#' onclick='teacherAndGradeEdit(\"\",\"\","+LSubject.get(i).getSubject_seq()+");' title='新增老師' style='font-weight:bold;font-size:x-small;padding:0px;color:orange;text-decoration:underline'>add</A></div>";
			tmp += "</div>";
			
			LSubject.get(i).setSubjectTeacherGrade(tmp);
		}
		return LSubject;
	}
	
	@RequestMapping("/Course/getSubjectOption")
	@ResponseBody
	public String getSubjectOption(HttpServletRequest request) {
		List<Subject> LSubject = courseService.getSubjectWithR(request.getParameter("category_id"),"","","1","1");//實體成本分攤
		String returnStr = "<option value=''></option>";
		for(int i=0;i<LSubject.size();i++) {
			returnStr += "<option value='"+LSubject.get(i).getName()+"@"+LSubject.get(i).getPrice()+"^"+LSubject.get(i).getSubject_seq()+"@"+LSubject.get(i).getHrPrice_R()+"@"+LSubject.get(i).getCounselingPrice_R()+"@"+LSubject.get(i).getLagnappePrice_R()+"@"+LSubject.get(i).getHandoutPrice_R()+"@"+LSubject.get(i).getHomeworkPrice_R()+"@"+LSubject.get(i).getMockPrice_R()+"'>"+LSubject.get(i).getName()+"</option>";   
		}
		return returnStr;
	}	
	
	@RequestMapping("/Course/SubjectSearch")
	@ResponseBody
	public String SubjectSearch(HttpServletRequest request,HttpSession session) {	
		String returnOption = "<option value=''></option>";
		List<Subject> subjectGroup = courseService.getSubject(request.getParameter("category_id"),"","","1","","0");
		String subject_id = "";
		String needSession = request.getParameter("needSession");
		if(needSession!=null && !needSession.equals("1")) {
			//無須session
		}else {
			if(session.getAttribute("CourseController_subject_id")!=null) {
				subject_id = (String)session.getAttribute("CourseController_subject_id");
			}	
		}	
		for(Subject subject : subjectGroup){
			String selValue = "";
			if(subject.getSubject_seq().equals(subject_id)) {
				selValue = "selected='selected'";
			}
			returnOption += "<option value='"+subject.getSubject_seq()+"' "+selValue+">"+subject.getName()+" ("+subject.getSubjectContent_name()+")</option>";
		}
		return returnOption;
	}
	
	@RequestMapping("/Course/GradeSearch")
	@ResponseBody
	public String GradeSearch(HttpServletRequest request) {	
		String returnOption = "<option></option>";
		List<Grade> LGrade = courseService.getGradeList("",request.getParameter("subject_id"),"","","","","","","4","1","","","","","","","","1","");
		for(Grade grade : LGrade){
			returnOption += "<option value='"+grade.getGrade_seq()+"'>"+grade.getClass_start_date()+"-"+grade.getTeacher_name()+"</option>";
		}
		return returnOption;
	}	
		
	@RequestMapping("/Course/MaterialSearch")
	@ResponseBody
	public String MaterialSearch(HttpServletRequest request) {
		String returnOption = "<option></option>";
		List<Material> materialGroup = courseService.getMaterial(request.getParameter("category_id"),"");
		for(Material material : materialGroup){
			returnOption += "<option value='"+material.getMaterial_seq()+"'>"+material.getMaterial_name()+"</option>";
		}
		return returnOption;
	}
	
	@RequestMapping("/Course/getSubjectTeacherSel")
	@ResponseBody
	public String getSubjectTeacher(HttpServletRequest request) {
		String subject_id = request.getParameter("subject_id");
		List<SubjectTeacher> LSubjectTeacher = courseService.getSubjectTeacher2("","",subject_id,"","1");
		String returnStr = "<option value='-1'></option>";
		for(int i=0;i<LSubjectTeacher.size();i++){
			returnStr += "<option value='"+LSubjectTeacher.get(i).getTeacher_id()+"'>"+LSubjectTeacher.get(i).getTeacher_name()+"("+LSubjectTeacher.get(i).getTeacher_code()+")</option>";
		}
		return returnStr;
	}	
	
	
	
	@RequestMapping("/Course/MockSearch")
	@ResponseBody
	public String MockSearch(HttpServletRequest request) {	
		String returnOption = "<option></option>";
		List<Mock> mockGroup = courseService.getMock(request.getParameter("category_id"),"","");
		for(Mock mock : mockGroup){
			returnOption += "<option value='"+mock.getMock_seq()+"'>"+mock.getMock_name()+"</option>";
		}
		return returnOption;
	}
	
	@RequestMapping("/Course/CounselingSearch")
	@ResponseBody
	public String CounselingSearch(HttpServletRequest request) {
		String returnOption = "<option></option>";
		List<Counseling> counselingGroup = courseService.getCounseling(request.getParameter("category_id"),"","");
		for(Counseling counseling : counselingGroup){
			returnOption += "<option value='"+counseling.getCounseling_seq()+"'>"+counseling.getCounseling_name()+"</option>";
		}
		return returnOption;
	}
	
	@RequestMapping("/Course/LagnappeSearch")
	@ResponseBody
	public String LagnappeSearch(HttpServletRequest request) {
		String returnOption = "<option></option>";
		List<Lagnappe> lagnappeGroup = courseSaleService.getLagnappe("","1");
		for(Lagnappe lagnappe : lagnappeGroup){
			returnOption += "<option value='"+lagnappe.getLagnappe_seq()+"'>"+lagnappe.getLagnappe_name()+"</option>";
		}
		return returnOption;
	}	
	
	/*
	 * @RequestMapping("/Course/getSubjectTrWithCost")
	 * 
	 * @ResponseBody public String getSubjectTrWithCost(HttpServletRequest request){
	 * return courseService.getSubjectTrWithCost(request.getParameter("category"));
	 * }
	 */
    
	/*
	 * @RequestMapping("/Course/getSubjectStr")
	 * 
	 * @ResponseBody public String getSubjectStr(HttpServletRequest request){ return
	 * courseService.getSubjectStr(request.getParameter("category")); }
	 */
	@RequestMapping("/Course/newCourse")
	public String newCourse(Model model,HttpServletRequest request) {
		if(request.getParameter("haveRead")!=null && request.getParameter("haveRead").equals("1") && request.getParameter("grade_seq")!=null) {
			courseService.updateGradeHaveRead(request.getParameter("grade_seq"));
		}
		String class_th = request.getParameter("class_th");
		if(class_th!=null && !class_th.isEmpty()) {
			model.addAttribute("class_th_point",class_th);
		}
		
		String action = request.getParameter("action");
		String JL_gradeId = request.getParameter("JL_gradeId");

		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);		
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	
		
		List<Teacher> teacherGroup1 = courseService.getTeacher("","","","","1","","");
		model.addAttribute("teacherGroup",teacherGroup1);
		
		List<Slot> slotGroup = courseService.getSlot();
		model.addAttribute("slotGroup",slotGroup);
		
		if(request.getParameter("grade_seq")!=null && !request.getParameter("grade_seq").isEmpty()) {
			List<classRoom> LclassRoom = systemService.getclassRoom("",request.getParameter("grade_seq"),"school_code,name","","");
			model.addAttribute("classRoomGroup",LclassRoom);
		}	
		
		Grade grade = new Grade();	
        String wk="";//星期
		  if(request.getParameter("grade_seq")!=null && !request.getParameter("grade_seq").isEmpty()) { //prevent URL key in access
			List<Grade> LGrade = courseService.getGradeList("","",request.getParameter("grade_seq"),"","","","","","","1","","","","","","","","1","");
		   if(LGrade.size()>0) { //存在此期別
			   grade = LGrade.get(0);
				List<Teacher> teacherGroup2 = courseService.getTeacher("","","","","","","");
				model.addAttribute("teacherGroup", teacherGroup2);			   
			    if(grade.getClass_start_date_0().length()==10) {
					wk = DateToWeek.getDateToWeek(grade.getClass_start_date_0());
					grade.setClass_start_date_0(grade.getClass_start_date_0()+" "+wk);
			    }
			if(action.equals("comment")){
	    		  if(grade.getStatus_code().equals("3") || grade.getStatus_code().equals("4")){
	    			if(!(request.isUserInRole("ROLE_adm") || request.isUserInRole("ROLE_adm_mgr"))){
	    				return "/Adm/GradeMessage";
	    			}
	    		  }else{
	    			return "/Adm/GradeMessage";
	    		  }				
			}else if(action.equals("edit")){			
    		  if(!(grade.getStatus_code().equals("4") || grade.getStatus_code().equals("5"))){
    			if(!(request.isUserInRole("ROLE_cou") || request.isUserInRole("ROLE_cou_mgr"))){
    				return "/Course/CourseSetting";
    			}
    		  }
			}else if(action.equals("copy")){			
	    		  if(grade.getStatus_code().equals("3") || grade.getStatus_code().equals("4") || grade.getStatus_code().equals("5")){
	    			if(!(request.isUserInRole("ROLE_cou") || request.isUserInRole("ROLE_cou_mgr"))){
	    				return "/Course/CourseSetting";
	    			}
	    		  }else{
	    			return "/Course/CourseSetting";
	    		  }
			}
			List<Subject> subjectGroup = courseService.getSubject(grade.getCategory_id(),"","","","","0");
			model.addAttribute("subjectGroup", subjectGroup);						
			List<Classes> Lclasses = courseService.getClasses(request.getParameter("grade_seq"),"","","","","","","","","");
			String wk2="";//星期
			for(int i=0;i<Lclasses.size();i++) {
				if(i==Lclasses.size()-1) {
					model.addAttribute("LastClass",Lclasses.get(i).getClass_date());
				}
				if(!Lclasses.get(i).getClass_date().isEmpty()) {
					wk2 = DateToWeek.getDateToWeek(Lclasses.get(i).getClass_date());
				}else {
					wk2 = "";
				}
				Lclasses.get(i).setClass_date(Lclasses.get(i).getClass_date()+" "+wk2);
			}
			model.addAttribute("Lclasses", Lclasses);
		  }	
		}  
		model.addAttribute("grade", grade);
		model.addAttribute("action", action);
		
    	
		//Video 開放校區
		ArrayList<String> LVideoOpenStr = new ArrayList<String>();  	
    	String tmp = null;
    	for(int i=0;i<schoolGroup.size();i++) {
    		tmp = "";
    		if(request.getParameter("grade_seq")!=null && !request.getParameter("grade_seq").isEmpty()) {
    			List<VideoOpen> LVideoOpen = courseService.getVideoOPen(request.getParameter("grade_seq"));
    			for(int j=0;j<LVideoOpen.size();j++) {
    				if(schoolGroup.get(i).getCode().equals(LVideoOpen.get(j).getSchoolCode())) {tmp="checked";} 
    			}
    		}else {
    			tmp = "checked"; //預設為全開放
    		}
    		LVideoOpenStr.add("<li><input type='checkbox' class='title' id='videoOpen' name='videoOpen' value='"+schoolGroup.get(i).getCode()+"' "+tmp+">"+schoolGroup.get(i).getName()+"</li>");
    		model.addAttribute("LVideoOpenStr", LVideoOpenStr);
    	}
    	
    	//目前訂班人數
    	if(request.getParameter("grade_seq")!=null && !request.getParameter("grade_seq").isEmpty()) {
	    	String grade_seq = request.getParameter("grade_seq");
			int gradeNo = salesService.getRegisterGradeNo(grade_seq);
			int gradeNoV = salesService.getVideoRegisterGradeNo(grade_seq);
			model.addAttribute("registerNo",gradeNo+gradeNoV);
    	}else {
    		model.addAttribute("registerNo","0");
    	}
    	
		return "/Course/newCourse";
	} 
	
	@RequestMapping("/Course/GetgradeNameSel")
	@ResponseBody
	public String GetgradeNameSel(HttpServletRequest request) {
		String subject_id = request.getParameter("subject_id");
		String teacher_id = request.getParameter("teacher_id");
		String gradeName  = request.getParameter("gradeName");
		List<GradeClassNo> LGradeClassNo = courseService.getGradeClassNo("",subject_id,teacher_id,"");
		
		String tmp = "<select id='gradeName' name='gradeName' class='form-control title' style='width:100px' onchange='GetTotalClassNoSel(this)'>";
		tmp+="<option value=''>~選擇~</option>";
		String selt;
		for(int i=0;i<LGradeClassNo.size();i++) {
			if(gradeName.equals(LGradeClassNo.get(i).getGradeName())) {
				selt = "selected";
			}else {
				selt = "";
			}
			tmp+="<option value='"+LGradeClassNo.get(i).getGradeName()+"' "+selt+">"+LGradeClassNo.get(i).getGradeName()+"</option>";
		}
		tmp +="</select>";
		
		if(LGradeClassNo.size()==0) {tmp="";}
		return tmp;
	}
	
	@RequestMapping("/Course/GetTotalClassNoSel")
	@ResponseBody
	public String GetTotalClassNoSel(HttpServletRequest request) {
		String subject_id = request.getParameter("subject_id");
		String teacher_id = request.getParameter("teacher_id");
		String gradeName  = request.getParameter("gradeName");

		String tmp = "";		
		//有班別, 使用eip.gradeClassNo : gradeClassNo
		if(gradeName!=null && !gradeName.isEmpty()) {
			List<GradeClassNo> LGradeClassNo = courseService.getGradeClassNo("",subject_id,teacher_id,gradeName);
			if(LGradeClassNo.size()>0) {
				for(int i=0;i<LGradeClassNo.size();i++) {
					tmp += "<option value='"+LGradeClassNo.get(i).getGradeClassNo()+"'>"+LGradeClassNo.get(i).getGradeClassNo()+"</option>";
				}	
			}
		//無班別, 使用eip.subjectTeacher : totalClassNo	
		}else{
			List<SubjectTeacher> LSubjectTeacher = courseService.getSubjectTeacher2("",teacher_id,subject_id,"","1");
			if(LSubjectTeacher.size()>0) {
				//tmp = LSubjectTeacher.get(0).getTotalClassNo();
				tmp = "<option value='"+LSubjectTeacher.get(0).getTotalClassNo()+"'>"+LSubjectTeacher.get(0).getTotalClassNo()+"</option>";
			}	
		}
	
		return tmp;
	}	
	
	@RequestMapping("/Course/AddCourseStr")
	@ResponseBody
	public String AddCourseStr(HttpServletRequest request) {
		return courseService.getCourseStr(
				request.getParameter("teacher_id"), 
				request.getParameter("class_date"), 
				request.getParameter("timeFrom"),
				request.getParameter("timeTo"),
				request.getParameter("class_no"),
				request.getParameter("class_style"),
				request.getParameter("slot_id"),
				request.getParameter("school_code")
		); 
	}

	@RequestMapping("/Course/AddCourseStr_2")
	@ResponseBody
	public String AddCourseStr_2(HttpServletRequest request) {
		return courseService.getCourseStr_2(
				request.getParameter("teacher_id"), 
				request.getParameter("class_date"), 
				request.getParameter("timeFrom"),
				request.getParameter("timeTo"),
				request.getParameter("class_style"),
				request.getParameter("slot_id"),
				request.getParameter("class_th"),
				request.getParameter("class_no"),
				request.getParameter("school_code")
		); 
	}	

 @RequestMapping(value="/Course/gradeSave", method=RequestMethod.POST)
 @ResponseBody
 public Boolean gradeSave(HttpServletRequest request,@ModelAttribute Grade grade,Principal principal){

	   String status_code = request.getParameter("status_code");
	   String grade_seq = grade.getGrade_seq();
		
	   if(grade.getClass_start_date_0() != null && !grade.getClass_start_date_0().isEmpty()) {
		  grade.setClass_start_date_0(grade.getClass_start_date_0().substring(0,10));
		  
		    //設訂期別
			if(grade.getClass_start_date_0().length()==10 && grade.getClass_start_date_0().contains("/")) { //ex.07/24/2019				
				grade.setGrade_date(grade.getClass_start_date_0().substring(8,10)+grade.getClass_start_date_0().substring(0,2)+grade.getClass_start_date_0().substring(3,5));
			}else {
				grade.setGrade_date(grade.getClass_start_date_0());
			}
	   }
		 
	   String col1 = "";
       switch(status_code) {
            case "1": col1 = "暫存"; break;
       		case "2": col1 = "送審"; break;
       		case "3": col1 = "待上架"; break;
       		case "4": col1 = "上架"; break;
       		case "5": col1 = "下架"; break;
       		case "6": col1 = "退件"; break;
       		case "7": col1 = "刪除"; break;
       		case "8": col1 = "上架修改"; break;
       }
//Update Grade Flow status only
	   if(!(status_code.equals("1") || status_code.equals("3") || status_code.equals("8") || status_code.equals("7"))) { //1:暫存,3:待上架,8:上架修改,7:刪除  都需儲存內容, 其餘改狀態即可

		  courseService.UpdateGradeStatus(grade_seq,status_code);
		  
//Save Content	  
	   }else{
			 if(status_code.equals("8")) { status_code = "4";} //上架修改視同上架 
			 grade.setStatus_code(status_code);
			 grade.setCreater(principal.getName());			 
			 
			 List<Classes> LClasses_or = new ArrayList<Classes>();
			 //A.更新-->Update Grade 
			 if(grade_seq != null && !grade_seq.isEmpty()) {
				 LClasses_or = courseService.getClasses(grade_seq,"","","","","","","","",""); //原有課堂資訊
				 courseService.updateGrade(grade);
				 //一律砍除課堂資訊
				 courseService.deleteClasses(grade_seq);
				 courseService.deleteVideoOpen(grade_seq);			 
			 }else { 			 
			 //B.新建-->Save Grade	 
				 grade_seq = courseService.saveGrade(grade);
			 }			 
			 //Save Class & videoOpen
			 String[] A_videoOpen = request.getParameterValues("videoOpen");		 
			 VideoOpen videoOpen = new VideoOpen();
			 if(A_videoOpen!=null) {
				 for(int i=0; i<A_videoOpen.length;i++) {
					 videoOpen.setGrade_id(grade_seq);
					 videoOpen.setSchoolCode(A_videoOpen[i]);		 
					 courseService.saveVideoOpen(videoOpen);			 
				 }	
			 }
			 
			 
			 String[] A_class_name = request.getParameterValues("c_class_name");
			 String[] A_class_date = request.getParameterValues("c_class_date");
			 String[] A_timeFrom = request.getParameterValues("c_timeFrom");
			 String[] A_timeTo = request.getParameterValues("c_timeTo");
			 String[] A_teacher_id = request.getParameterValues("c_teacher_id");
			 String[] A_class_style = request.getParameterValues("c_class_style");
			 String[] A_class_remark = request.getParameterValues("c_class_remark");
			 //String[] A_class_remarkAdm = request.getParameterValues("c_class_remarkAdm");
			 String[] A_slot_id = request.getParameterValues("c_slot_id");
			 String[] A_class_trial = request.getParameterValues("c_class_trial");
			 String[] A_classReceiveHidden =  request.getParameterValues("classReceiveHidden");
			 String[] A_classRoom = request.getParameterValues("c_classRoom");
			 //String[] A_class_th_seq = request.getParameterValues("class_th_seq");
			 String[] A_class_th = request.getParameterValues("class_th");
			 Classes classes = new Classes();

/**			 
for(int i=0;i<A_class_th_seq.length;i++) {			 
    System.out.println(A_class_th_seq[i]+"==="+A_class_th[i]);
}
**/			 
			 for(int i=0; i<A_class_date.length;i++) {
				 classes.setGrade_id(grade_seq);
				 classes.setClass_th(A_class_th[i]);
				 classes.setClass_th_seq(String.valueOf(i+1));
				 classes.setClass_name(A_class_name[i]);
	
				 if(A_class_date[i]!=null && A_class_date[i].length()>9) {
					 classes.setClass_date(A_class_date[i].substring(0,10));
				 }else {
					 classes.setClass_date("");
				 }
				 classes.setTime_from(A_timeFrom[i]);
				 classes.setTime_to(A_timeTo[i]);
				 classes.setTeacher_id(A_teacher_id[i]);
				 classes.setClass_style(A_class_style[i]);
				 classes.setClass_remark(A_class_remark[i]);
				 //classes.setClass_remarkAdm(A_class_remarkAdm[i]);
				 classes.setSlot_id(A_slot_id[i]);
				 classes.setClass_trial(A_class_trial[i]);
				 classes.setClassRoom(A_classRoom[i]);
				 int class_id = courseService.saveClasses(classes);
				 
				 //領取
				 if(A_classReceiveHidden!=null && A_classReceiveHidden[i].length()>0) {
					 String[] tmp1 = A_classReceiveHidden[i].split("@");
					 for(int x=0;x<tmp1.length;x++) {
						 if(tmp1[x].length()>0) {
							 String[] tmp2 = tmp1[x].split("#");
							 String material_id="",book_id="",inputTex="";
							 for(int y=0;y<tmp2.length;y++) {							 
								 if(tmp2[0].equals("1")) {
									 material_id = "1";
									 book_id = tmp2[1];
								 }else if(tmp2[0].equals("2")) {
									 material_id = "2";
									 if(tmp2.length==1) {
										 inputTex = ""; 
									 }else {
										 inputTex = tmp2[1];
									 }	 
								 }							 
							 }
							 courseService.saveClassesMaterial(String.valueOf(class_id),material_id,book_id,inputTex);
						 }
					 }
				 }
		     }
		 
			 //修改已訂期別學生紀錄
			 List<Classes> LClasses_new = courseService.getClasses(grade_seq,"","","","","","","","",""); //新的課堂資訊
			 Boolean exist = false; //此筆記錄不存在, 需新增
			 for(int b=0;b<LClasses_new.size();b++) {
				 exist = false;
				 for(int a=0;a<LClasses_or.size();a++) {
					 if(LClasses_or.get(a).getClass_th_seq().equals(LClasses_new.get(b).getClass_th_seq())) {
						 exist = true; //此筆記錄存在, 僅需修改
					 }
				 }
				 //每一課堂的變動反應到學生紀錄
				 courseService.updateSignRecordHistory(grade_seq,LClasses_new.get(b),classes.getClass_style(),principal.getName(),exist,LClasses_new.get(b).getClass_th_seq());
			 }
			 
			 		 		 
	   } 
	   if(!status_code.equals("7")) { //刪除狀態則無紀錄
		   Grade grade1 =courseService.getGradeList("","",grade_seq,"","","","","","","1","","","","","","","","1","").get(0);
		   managerService.applicationLogSave(principal.getName(),"2","-1","-1",col1,grade1.getSchool_name(),grade1.getCategory_name(),grade1.getSubject_name(),grade1.getClass_start_date(),grade_seq);		   
	   }	 
	   return true;
 }
	 
	 @RequestMapping(value="/Course/gradeCommentSave", method=RequestMethod.POST)
	 @ResponseBody
	 public Boolean gradeCommentSave(HttpServletRequest request,@ModelAttribute Grade grade,Principal principal){
		 String grade_seq = grade.getGrade_seq();
		 String[] A_class_remark = request.getParameterValues("c_class_remark");
		 //String[] A_class_remarkAdm = request.getParameterValues("c_class_remarkAdm");
		 String[] A_classRoom = request.getParameterValues("c_classRoom");
		 String[] A_class_seq = request.getParameterValues("class_seq");
		 for(int i=0; i<A_class_remark.length;i++) {
			 //courseService.updateClassesComment(grade.getGrade_seq(),i+1,A_class_remark[i],A_class_remarkAdm[i],A_classRoom[i]);
			 courseService.updateClassesComment(grade.getGrade_seq(),i+1,A_class_remark[i],"",A_classRoom[i]);			 
		 }	 
		 Grade grade1 =courseService.getGradeList("","",grade_seq,"","","","","","","1","","","","","","","","1","").get(0);
		 managerService.applicationLogSave(principal.getName(),"2","-1","-1","行政修改",grade1.getSchool_name(),grade1.getCategory_name(),grade1.getSubject_name(),grade1.getClass_start_date(),"");		   		 
		 
		 //領取
		 String[] A_classReceiveHidden =  request.getParameterValues("classReceiveHidden");
		 for(int i=0; i<A_classReceiveHidden.length;i++) {
			 if(A_classReceiveHidden!=null && A_classReceiveHidden[i].length()>0) {
				 String[] tmp1 = A_classReceiveHidden[i].split("@");
				 for(int x=0;x<tmp1.length;x++) {
					 if(tmp1[x].length()>0) {
						 String[] tmp2 = tmp1[x].split("#");
						 String material_id="",book_id="",inputTex="";
						 for(int y=0;y<tmp2.length;y++) {							 
							 if(tmp2[0].equals("1")) {
								 material_id = "1";
								 book_id = tmp2[1];
							 }else if(tmp2[0].equals("2")) {
								 material_id = "2";
								 if(tmp2.length==1) {
									 inputTex = ""; 
								 }else {
									 inputTex = tmp2[1];
								 }	 
							 }							 
						 }
						 courseService.saveClassesMaterial(A_class_seq[i],material_id,book_id,inputTex);
					 }
				 }
			 }
		 }
		 return true;
	 }	 
	
    @RequestMapping("/Course/getGradeList")
    @ResponseBody
    public List<Grade> getGradeList(HttpServletRequest request,HttpSession session,Model model){
    	String category_id = request.getParameter("category_id");
        if(category_id!=null) {
        	session.setAttribute("CourseController_category_id", category_id);
        }
    	String school_code = request.getParameter("school_code");
        if(school_code!=null) {
        	session.setAttribute("CourseController_school_code", school_code);
        }    	
    	String subject_id  = request.getParameter("subject_id");
        if(subject_id!=null) {
        	session.setAttribute("CourseController_subject_id", subject_id);
        }      	    	
    	String status_code = request.getParameter("status_code");
        if(status_code!=null) {
        	session.setAttribute("CourseController_status_code", status_code);
        }     	
    	String teacher_id  = request.getParameter("teacher_id");
        if(teacher_id!=null) {
        	session.setAttribute("CourseController_teacher_id", teacher_id);
        }     	
    	String class_start_date_0  = request.getParameter("class_start_date_0");
    	String video_date  = request.getParameter("video_date");

    	if(class_start_date_0!=null && class_start_date_0.length()>5) {
    		class_start_date_0 = class_start_date_0.trim().substring(2,4)+"/"+class_start_date_0.trim().substring(4,6)+"/20"+class_start_date_0.trim().substring(0,2);
    	}
    	
    	String videoPreserve  = request.getParameter("videoPreserve");
        if(videoPreserve!=null) {
        	session.setAttribute("CourseController_videoPreserve", videoPreserve);
        }     	

    	String haveRead  = request.getParameter("haveRead");
    	List<Grade> LGrade = courseService.getGradeList(teacher_id,subject_id,"",school_code,category_id,"",class_start_date_0,"",status_code,"1",video_date,"","200",haveRead,"","","","1",videoPreserve);
    	for(int i=0;i<LGrade.size();i++) {
	    	int gradeNo = salesService.getRegisterGradeNo(LGrade.get(i).getGrade_seq());
			LGrade.get(i).setGradeNo(String.valueOf(gradeNo));
    	}	
		 return LGrade;
    }

    @RequestMapping("/Course/getGradeListNotInJL")
    @ResponseBody
    public List<Grade> getGradeListNotInJL(HttpServletRequest request,HttpSession session,Model model){
    	List<Grade> LGrade = courseService.getGrade("","","","","","","","","a.subject_id","","select eip_grade_seq from eip.JL_EIP_grade","","");
    	for(int i=0;i<LGrade.size();i++) {
	    	int gradeNo = salesService.getRegisterGradeNo(LGrade.get(i).getGrade_seq());
			LGrade.get(i).setGradeNo(String.valueOf(gradeNo));
    	}	
		 return LGrade;    	
    }
    
    @RequestMapping("/Course/getGradeList2")
    @ResponseBody
    public List<Grade> getGradeList2(HttpServletRequest request){
    	String status_code = request.getParameter("status_code");
    	String school_code = request.getParameter("school_code");
    	String subject_id  = request.getParameter("subject_id"); 
    	String teacher_id  = request.getParameter("teacher_id");
    	String category_id  = request.getParameter("category_id");
    	String class_start_date_0  = request.getParameter("class_start_date_0");
   	
    	if(class_start_date_0!=null && class_start_date_0.length()==6) {
    		class_start_date_0 = class_start_date_0.substring(2,4)+"/"+class_start_date_0.substring(4,6)+"/20"+class_start_date_0.substring(0,2);
    	}

    	List<Grade> LGrade = courseService.getGrade("",status_code,subject_id,school_code,class_start_date_0,category_id,teacher_id,"200","","","","","");
    	for(int i=0;i<LGrade.size();i++) {
    		int gradeNo = salesService.getRegisterGradeNo(LGrade.get(i).getGrade_seq());
    		LGrade.get(i).setGradeNo(String.valueOf(gradeNo));
    		int gradeNoV = salesService.getVideoRegisterGradeNo(LGrade.get(i).getGrade_seq());
    		LGrade.get(i).setGradeNo_v(String.valueOf(gradeNoV));		
    	}
		 return LGrade;
    }
    
    @RequestMapping("/Course/getGradeList3")
    @ResponseBody
    public List<Grade> getGradeList3(HttpServletRequest request){
		
		DateFormat dateFormat = new SimpleDateFormat("MM/__/yyyy");
		
    	String status_code = request.getParameter("status_code");
    	String school_code = request.getParameter("school_code");
    	String subject_id  = request.getParameter("subject_id"); 
    	String teacher_id  = request.getParameter("teacher_id");
    	String category_id  = request.getParameter("category_id");

    	List<Grade> LGrade = courseService.getGrade2("",status_code,subject_id,school_code,"",category_id,teacher_id," a.grade_date desc",dateFormat.format(new Date()));
		return LGrade;
    }     
    
    @RequestMapping("/Course/getTodayGradeList")
    @ResponseBody
    public List<Classes> getTodayGradeList(HttpServletRequest request){
    	String school_code = request.getParameter("school_code");
    	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    	String class_date = dateFormat.format(new Date());
    	List<Classes> LClasses= courseService.getClasses("","",class_date,school_code,"","","","","","");
    	
    	for(int i=0;i<LClasses.size();i++) {
    		Grade grade = courseService.getGradeList("","",LClasses.get(i).getGrade_id(),"","","","","","","","","","","","","","1","1","").get(0);
    		LClasses.get(i).setSubject_name(grade.getClass_start_date()+" "+LClasses.get(i).getSubject_name()+" "+(grade.getGradeName()==null?"":grade.getGradeName()));
    	}
		 return LClasses;
    }    
    
	@RequestMapping("/Course/gradeInfo")
	public String gradeInfo(Model model) {					
		return "/Course/gradeInfo";
	}  
	

	
	@RequestMapping("/Course/MockSetting")
	public String MockSetting(Model model,HttpServletRequest request) {	
		List<Category> LCategory = courseService.getCategory("","");
		model.addAttribute("LCategory",LCategory);	
		//return "/common/empty";
		return "/Course/MockSetting";
	}
	
	@RequestMapping("/Course/MockEdit")
	public String MockEdit(Model model,HttpServletRequest request) {
	
		List<Category> LCategory = courseService.getCategory("","");
		model.addAttribute("LCategory",LCategory);		
		
		String mock_seq = request.getParameter("mock_seq");
		model.addAttribute("mock_seq",mock_seq);
						
		if(mock_seq != null && !mock_seq.isEmpty()) {			
			Mock mock= courseService.getMock("",mock_seq,"").get(0);
			model.addAttribute("category_id",mock.getCategory_id());
			model.addAttribute("mock_name",mock.getMock_name());
			model.addAttribute("original_price",mock.getOriginal_price());
			model.addAttribute("active",mock.getActive());
			
			//模考
			String returnStr = "";
			List<MockDetail> LMockDetail = courseService.getMockDetail(mock_seq,"");
			for(int i=0;i<LMockDetail.size();i++) {
				String testStyle1="",testStyle2="",testStyle3="";
				String testMethod1="",testMethod2="",testMethod3="",testMethod4="";
				if(LMockDetail.get(i).getTestStyle().equals("1")) {
					testStyle1 = " selected";
				}else if(LMockDetail.get(i).getTestStyle().equals("2")) {
					testStyle2 = " selected";
				}else if(LMockDetail.get(i).getTestStyle().equals("3")) {
					testStyle3 = " selected";
				}
				if(LMockDetail.get(i).getTestMethod().equals("1")) {
					testMethod1 = " selected";
				}else if(LMockDetail.get(i).getTestMethod().equals("2")) {
					testMethod2 = " selected";
				}else if(LMockDetail.get(i).getTestMethod().equals("3")) {
					testMethod3 = " selected";
				}else if(LMockDetail.get(i).getTestMethod().equals("4")) {
					testMethod4 = " selected";
				}
				
				
				
					returnStr +=
					"<div class='tr' style='border-bottom:1px #555555 solid'>"+	
					    "<div class='td2' style='width:50px;height:30px;text-align:center'><A href='javascript:void(0)' title='刪除此筆'  onclick='$(this).parent().parent().remove();'><img src='/images/delete.png' height='10px'/></A></div>"+
						"<div class='td2' style='width:80px' align='center'><input type='text' name='totalNo' value='"+LMockDetail.get(i).getTotalNo()+"' class='form-control' style='width:50px'/></div>"+
					    "<div class='td2' style='width:100px' align='center'>";								
						/**考科-回**/
					    String selected="";	    
						List<TestRound> LTestRound = courseService.getTestRound(mock.getCategory_id());
						returnStr +=						
							"<select class='form-control noName' style='width:80px' name='noName'>"+
								"<option value=''></option>";
						         for(int a=0;a<LTestRound.size();a++) {
						        	 if(LTestRound.get(a).getName().equals(LMockDetail.get(i).getNoName())) {
						        		 selected = "selected";
						        	 }else {
						        		 selected = "";
						        	 }
						        	 returnStr += "<option value='"+LTestRound.get(a).getName()+"' "+selected+">"+LTestRound.get(a).getName()+"</option>";
						         }	 
							returnStr +=
							"</select>"+
						"</div>"+	
									
						"<div class='td2' style='width:120px' align='center'>"+
							"<select class='form-control' style='width:80px' name='testStyle'>"+
								"<option value=''></option>"+
								"<option value='1' "+testStyle1+">個別</option>"+
								"<option value='2' "+testStyle2+">視訊</option>"+
								"<option value='3' "+testStyle3+">團體</option>"+
							"</select>"+
						"</div>"+		
						"<div class='td2' style='width:150px' align='center'>"+
							"<select class='form-control' style='width:120px' name='testMethod'>"+
								"<option value=''></option>"+
								"<option value='1' "+testMethod1+">電腦</option>"+
								"<option value='2' "+testMethod2+">口試</option>"+
								"<option value='3' "+testMethod3+">紙筆</option>"+
								"<option value='4' "+testMethod4+">真人評測</option>"+
							"</select>"+
						"</div>"+												
					"</div>";  				
			}
			model.addAttribute("returnStr",returnStr);
			
			//模考解題
			String subject_sel = "";
			List<Subject> LSubject = courseService.getSubject(mock.getCategory_id(),"","","1","%模考%","0");
			List<MockVideo> LMockVideo = courseService.getMockVideo(mock_seq);
			String returnStr2 = "";
			for(int x=0;x<LMockVideo.size();x++) {
				  returnStr2+=
				  "<div class='tr'>"+	
					    "<div class='td2' style='width:50px;height:30px;text-align:center'><A href='javascript:void(0)' title='刪除此筆'  onclick='$(this).parent().parent().remove();'><img src='/images/delete.png' height='10px'/></A></div>"+
						"<div class='td2' style='width:250px' align='center'>"+
							"<select name='subject_id' id='subject_id' class='form-control' style='width:230px'>"+
									"<option value=''></option>";
								for(int y=0;y<LSubject.size();y++) {
									subject_sel = "";
									if(LSubject.get(y).getSubject_seq().equals(LMockVideo.get(x).getSubject_id())) {
										subject_sel = "selected";
									}
									returnStr2+=	
							        "<option value='"+LSubject.get(y).getSubject_seq()+"' "+subject_sel+">"+LSubject.get(y).getName()+"</option>";
								}
							returnStr2+=
							"</select>"+
						"</div>"+													
				  "</div>";				
			}
			model.addAttribute("returnStr2",returnStr2);
			
		}else {
			model.addAttribute("returnStr","");
			model.addAttribute("returnStr2","");
		}
		return "/Course/MockEdit";
	}
	
	@RequestMapping("/Course/getTestSubjectOption")
	@ResponseBody
	public String getTestSubjectOption(HttpServletRequest request) {
		String category_id = request.getParameter("category_id");

			List<TestRound> LTestRound =  courseService.getTestRound(category_id);
			String returnStr = "<option value=''></option>";
			for(int i=0;i<LTestRound.size();i++) {
				returnStr+= "<option value='"+LTestRound.get(i).getName()+"'>"+LTestRound.get(i).getName()+"</option>";
			}
			return returnStr;	
	} 
	

	@RequestMapping("/Course/getRoundOption")
	@ResponseBody
	public String getRoundOption(HttpServletRequest request) {
		String testSubject_seq = request.getParameter("testSubject_seq");
		List<TestSubjectSelection> LTestSubjectSelection = courseService.getTestSubjectSelection(testSubject_seq,"");
		String returnStr = "<option value=''></option>";
		for(int i=0;i<LTestSubjectSelection.size();i++) {
			returnStr+= "<option value='"+LTestSubjectSelection.get(i).getTestRound()+"'>"+LTestSubjectSelection.get(i).getTestRound()+"</option>";
		}
		return returnStr;
	} 	
	
	@RequestMapping("/Course/VideoCross")
	public String VideoCross(Model model,HttpServletRequest request) {	

		return "/Course/VideoCross";
	} 
	
	@RequestMapping("/Course/SubjectEdit")
	public String SubjectEdit(Model model,HttpServletRequest request,Principal principal) {
		String message = request.getParameter("message");
		if(message!=null && !message.isEmpty()) {
			model.addAttribute("message", message);
		}
		List<Category> LCategory= courseService.getCategory("","");
		model.addAttribute("LCategory", LCategory);
		String subject_seq = request.getParameter("subject_seq");
		Subject subject  = new Subject();
		Subject subject_R1 = new Subject();
		Subject subject_R3 = new Subject();
		
		List<Account_authority> LAccount_authority = accountService.getAccount_authority(principal.getName());
		Boolean withApprove = false;
		for(int i=0;i<LAccount_authority.size();i++) {
			if(LAccount_authority.get(i).getAuthority().equals("ROLE_approve_mgr")) {
				withApprove = true;
			}
		}	
		
		List<SubjectContent> LSubjectContent = courseService.getSubjectContent();
		model.addAttribute("LSubjectContent", LSubjectContent);
		
		//成本分攤比率
		if(subject_seq != null && !subject_seq.isEmpty()) {
			subject = courseService.getSubject("",subject_seq,"","","","0").get(0);
			//班內
			List<Subject> LSubject1 = courseService.getSubjectWithR("",subject_seq,"","1","1");
			//線上
			List<Subject> LSubject3 = courseService.getSubjectWithR("",subject_seq,"","1","3");				

			if(LSubject1.size()>0) {
				subject_R1 = LSubject1.get(0);
				if(!withApprove) {
					subject_R1.setHandoutPrice_R("");
					subject_R1.setHrPrice_R("");
					subject_R1.setCounselingPrice_R("");
					subject_R1.setHomeworkPrice_R("");
					subject_R1.setLagnappePrice_R("");
					subject_R1.setMockPrice_R("");
				}
			}	
			if(LSubject3.size()>0) {
				subject_R3 = LSubject3.get(0);
				if(!withApprove) {
					subject_R3.setHandoutPrice_R("");
					subject_R3.setHrPrice_R("");
					subject_R3.setCounselingPrice_R("");
					subject_R3.setHomeworkPrice_R("");
					subject_R3.setLagnappePrice_R("");
					subject_R3.setMockPrice_R("");
				}								
			}
					
			//已選取之虛擬subject
			List<VirtualSubject> LVirtualSubject = salesService.getVirtualSubject(subject_seq);
			String virtualSubjectStr = "";
			for(int i=0;i<LVirtualSubject.size();i++) {
				virtualSubjectStr += 
						"<div>"+
							"<input type='hidden' name='selVirtualSubject_id' value='"+LVirtualSubject.get(i).getChild_subject_id()+"'>"+
							"<A href='javascript:void(0)' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete.png' height='8px'/></A>&nbsp;&nbsp;"+
						    "<span style='color:darkblue;font-size:small;font-weight:bold'>"+LVirtualSubject.get(i).getChild_subject_name()+"</span>"+
						"</div>";				
			}
			model.addAttribute("virtualSubjectStr", virtualSubjectStr);			
		}
		model.addAttribute("subject", subject);
		model.addAttribute("subject_R1",subject_R1);
		model.addAttribute("subject_R3",subject_R3);
		
		//檢視是否有行程
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String today = df.format(new Date());
		String schedule_flag = "";
		
		if(subject_seq != null && !subject_seq.isEmpty()) {
			List<Subject_tmp> LSubject_tmp = courseService.getSubject_tmp(subject_seq,today,"");
			for(int i=0;i<LSubject_tmp.size();i++) {
				schedule_flag += "&nbsp;<img src='/images/clock.png' height='13px' title='待跑行程'/> "+LSubject_tmp.get(i).getSchedule_time()+"&nbsp;";
			}
		}
		model.addAttribute("schedule_flag",schedule_flag);

		
		return "/Course/SubjectEdit";
	} 

    @RequestMapping("/Course/SubjectEditSave")
    public String SubjectEditSave(HttpServletRequest request,@ModelAttribute Subject subject,Principal principal,RedirectAttributes redirectAttributes) {
    	Category category = courseService.getCategory(subject.getCategory_id(),"").get(0); 
    	managerService.applicationLogSave(principal.getName(),"5","-1","-1","更新",category.getName(),subject.getName(),subject.getPrice(),"","");   	
    	
    	String[] A_selVirtualSubject_id = request.getParameterValues("selVirtualSubject_id");
    	String schedule_time = request.getParameter("schedule_time");
        String subject_seq = request.getParameter("subject_seq");
    	
    	Subject_R subject_R1 = new Subject_R(
    			"",
    			"",
    			"1",
    			request.getParameter("hrPrice_R1"),
    			request.getParameter("counselingPrice_R1"),
    			request.getParameter("lagnappePrice_R1"),
    			request.getParameter("handoutPrice_R1"),
    			request.getParameter("homeworkPrice_R1"),
    			request.getParameter("mockPrice_R1")
    	);
    	
    	Subject_R subject_R3 = new Subject_R(
    			"",
    			"",
    			"1",
    			request.getParameter("hrPrice_R3"),
    			request.getParameter("counselingPrice_R3"),
    			request.getParameter("lagnappePrice_R3"),
    			request.getParameter("handoutPrice_R3"),
    			request.getParameter("homeworkPrice_R3"),
    			request.getParameter("mockPrice_R3")
    	);    	
    	
    	subject.setUpdater(principal.getName());

    	if(request.getParameter("updateFlag").equals("true")) {
    		//無排程,立即修改
    		if(schedule_time==null || schedule_time.isEmpty()) {
    			courseService.SubjectUpdateSave(subject,subject_seq,A_selVirtualSubject_id,subject_R1,subject_R3);
    		}else {
    		//排定修改	
    			courseService.SubjectUpdateTmpSave(subject,A_selVirtualSubject_id,subject_R1,subject_R3,schedule_time);
    		}
    	}else {
    		//新增或進版
    		subject_seq = courseService.SubjectEditSave(subject,request.getParameter("subject_seq"),A_selVirtualSubject_id,subject_R1,subject_R3);
    	}	
    	//return "redirect:/Course/SubjectEdit?subject_seq="+subject_seq+"&message=Save Successfully!<br><a href='javascript:void(0)' onclick='parentReload()' style='text-decoration:underline;color:blue'><img src='/images/reload.png' style='width:28px'/>Close and reload main page!</A><br>";
    	return "/common/closeAndReload"; 
    }
    
    @RequestMapping("/Course/teacherAndGradeEditSave")
    public String teacherAndGradeEditSave(HttpServletRequest request,@ModelAttribute SubjectTeacher subjectTeacher,Principal principal,RedirectAttributes redirectAttributes) {

    	String[] A_pre_grade_seq = request.getParameterValues("pre_grade_seq");
    	String[] A_pre_grade_remark = request.getParameterValues("pre_grade_remark");    	
    	subjectTeacher.setUpdater(principal.getName());
    	String[] A_gradeName = request.getParameterValues("gradeName");
    	String[] A_gradeClassNo = request.getParameterValues("gradeClassNo");
    	
    	if(subjectTeacher.getSubjectTeacher_seq()!=null && !subjectTeacher.getSubjectTeacher_seq().isEmpty()) {
    		courseService.teacherAndGradeEditUpdate(subjectTeacher,A_pre_grade_seq,A_pre_grade_remark,A_gradeName,A_gradeClassNo);
    	}else {
    		courseService.teacherAndGradeEditInsert(subjectTeacher,A_pre_grade_seq,A_pre_grade_remark,A_gradeName,A_gradeClassNo);
    	}
    	return "/common/closeAndReload"; 
    }    
    
	@RequestMapping("/Course/AddSubjectVirtual")
	@ResponseBody
	public String AddSubjectVirtual(HttpServletRequest request) {
		String subject_seq = request.getParameter("virtualSubject_id");
		String subject_name = "";
		if(subject_seq != null && !subject_seq.isEmpty()) {
			subject_name = courseService.getSubject("",subject_seq,"","","","0").get(0).getName();
		}
		String returnStr = 
				"<div>"+
					"<input type='hidden' name='selVirtualSubject_id' value='"+subject_seq+"'>"+
					"<A href='javascript:void(0)' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete.png' height='8px'/></A>&nbsp;&nbsp;"+
				    "<span style='color:darkblue;font-size:small;font-weight:bold'>"+subject_name+"</span>"+
				"</div>";
		return returnStr;
	} 

    @RequestMapping("/Course/MockEditSave")
    public String MockEditSave(HttpServletRequest request,Principal principal) {
    	String mock_seq		       = request.getParameter("mock_seq");
    	String category_id         = request.getParameter("category_id");
    	String mock_name           = request.getParameter("mock_name");
    	String original_price      = request.getParameter("original_price");
    	String active              = request.getParameter("active");
    	String[] A_totalNo    	   = request.getParameterValues("totalNo");
    	String[] A_noName          = request.getParameterValues("noName");
    	String[] A_testStyle       = request.getParameterValues("testStyle");
    	String[] A_testMethod      = request.getParameterValues("testMethod");
    	String[] A_subject_id      = request.getParameterValues("subject_id");
    	Category category = courseService.getCategory(category_id,"").get(0);
    	managerService.applicationLogSave(principal.getName(),"6","-1","-1","更新",category.getName(),mock_name,"","","");   	
    	courseService.MockEditSave(mock_seq,category_id,mock_name,A_totalNo,A_noName,A_testStyle,A_testMethod,A_subject_id,original_price,active);
    	return "/common/closeAndReload"; 
    }

	@RequestMapping("/Course/getMockSetting")
	@ResponseBody
	public List<Mock> getMockSetting(HttpServletRequest request) {
		List<Mock> LMock = courseService.getMock(request.getParameter("category_id"),"",request.getParameter("active"));			
		return LMock;
	}
	
	@RequestMapping("/Course/noClass")
	public String noClass(Model model,HttpServletRequest request){
		String dateSel = request.getParameter("dateSel");
		model.addAttribute("dateSel",dateSel);
		String teacher_id = request.getParameter("teacher_id");
		String slot_id = request.getParameter("slot");
		String slot_name = "";
		if(slot_id!=null) {
			if(slot_id.equals("1")) {
				slot_name = "早";
			}else if(slot_id.equals("2")) {
				slot_name = "午";
			}else if(slot_id.equals("3")) {
				slot_name = "晚";
			}
		}
		model.addAttribute("slot",slot_id);
		model.addAttribute("teacher_id",teacher_id);
		model.addAttribute("slot_name",slot_name);
		
		List<NoClass> LNoClass= salesService.getNoClass(teacher_id,dateSel,slot_id);
		String checked1="";
		String checked2="";
		String checked3="";
		if(LNoClass.size()==0) {
			checked3 = "checked";
		}else {
			if(LNoClass.get(0).getNoClassType().equals("1")) {
				checked1 = "checked";
			}else if(LNoClass.get(0).getNoClassType().equals("2")) {
				checked2 = "checked";
			}else if(LNoClass.get(0).getNoClassType().equals("3")) {
				checked3 = "checked";
			} 
		}
		String radioItem=
				"<div style='padding:5px;font-weight:bold'>&nbsp;<input type='radio' name='noClassType' value='1' "+checked1+"> 希望不排課  </div>"+
				"<div style='padding:5px;font-weight:bold'>&nbsp;<input type='radio' name='noClassType' value='2' "+checked2+"> 不排課  </div>"+
				"<div style='padding:5px;font-weight:bold'>&nbsp;<input type='radio' name='noClassType' value='0' "+checked3+"> 可排  </div>";
		model.addAttribute("radioItem",radioItem);
		return "/Course/noClass";
	}
	
	@RequestMapping("/Course/noClass2")
	public String noClass2(Model model,HttpServletRequest request){	
		model.addAttribute("dateSel",request.getParameter("dateSel"));
		String teacher_id = request.getParameter("teacher_id");
		String slot = request.getParameter("slot");
		String slot_name = "";
		if(slot!=null) {
			if(slot.equals("1")) {
				slot_name = "早";
			}else if(slot.equals("2")) {
				slot_name = "午";
			}else if(slot.equals("3")) {
				slot_name = "晚";
			}
		}
		model.addAttribute("slot",slot);
		model.addAttribute("teacher_id",teacher_id);
		model.addAttribute("slot_name",slot_name);
		return "/Course/noClass2";
	}	
	
	@RequestMapping("/Course/noClassSave")
	@ResponseBody
	public String noClassSave(Model model,HttpServletRequest request){
		String teacher_id  = request.getParameter("teacher_id");
		String dateSel     = request.getParameter("dateSel");
		String timeFrom    = request.getParameter("timeFrom");
		String timeTo      = request.getParameter("timeTo");
		String noClassType = request.getParameter("noClassType");
		String slot_id     = request.getParameter("slot_id");
		NoClass noClass = new NoClass("",teacher_id,dateSel,timeFrom,timeTo,noClassType,slot_id);
		courseService.noClassSave(noClass);
		return "true";
	}
	
	@RequestMapping("/Course/mockSetSave")
	@ResponseBody
	public String mockSetSave(Model model,HttpServletRequest request){
		String dateSel     = request.getParameter("dateSel");
		String school_id   = request.getParameter("school_id");
		String category_id = request.getParameter("category_id");		
		String testMethod  = request.getParameter("testMethod");
		String[] A_slot    = request.getParameterValues("slot");
		courseService.mockSetSave(dateSel,school_id,category_id,testMethod,A_slot);
		return "true";
	}
		
	@RequestMapping("/Course/LectureSetting")
	public String LectureSetting(Model model,HttpServletRequest request){	
		//model.addAttribute("dateSel",request.getParameter("dateSel"));
		return "/Course/LectureSetting";
	}
	
	@RequestMapping("/Course/openGradeStudent")
	public String openGradeStudent(Model model,HttpServletRequest request){	
		String grade_seq = request.getParameter("grade_seq");
		model.addAttribute("grade_seq",grade_seq);
		
		Grade grade = courseService.getGradeList("","",grade_seq,"","","","","","","","","","","","","","","1","").get(0);
		model.addAttribute("status_code",grade.getStatus_code());
		model.addAttribute("disable",grade.getDisable());
		String registerNo = request.getParameter("registerNo");
		model.addAttribute("registerNo",registerNo);
		
		return "/Course/openGradeStudent";
	}
	
	@RequestMapping("/Course/openGradeStudent2")
	public String openGradeStudent2(Model model,HttpServletRequest request){	
		model.addAttribute("grade_seq",request.getParameter("grade_seq"));
		model.addAttribute("class_style",request.getParameter("class_style"));
		model.addAttribute("school_code",request.getParameter("school_code"));

		
		return "/Course/openGradeStudent2";
	}	
		
	@RequestMapping("/Course/getGradeStudent")
	@ResponseBody
	public List<Student> getGradeStudent(HttpServletRequest request) {
		 List<Student> LStudent = salesService.getGradeStudent("","","","","","","",request.getParameter("grade_seq"),"","","");		
		return LStudent;
	}
	
	
	@RequestMapping("/Course/getGradeStudent2")
	@ResponseBody
	public List<Student> getGradeStudent2(HttpServletRequest request) {
		String grade_id  = request.getParameter("grade_id");
		String class_style = request.getParameter("class_style");
		String school_code = request.getParameter("school_code");
		String register_status = request.getParameter("register_status");
		List<Student> LStudent = salesService.getGradeStudent2(grade_id,class_style,school_code,register_status);
		return LStudent;
	}	
	
	@RequestMapping("/Course/getGradeStudentShare")
	@ResponseBody
	public String getGradeStudentShare(HttpServletRequest request,Model model) {
		String grade_id  = request.getParameter("grade_id");
		String gradeName = request.getParameter("gradeName");
		Grade grade = courseService.getGrade(grade_id,"","","","","","","200","","","","","").get(0);
		//List<StudentGradeShare> LStudentGradeShare = salesService.getGradeStudentShare(grade_id,grade.getClass_style(),"","");
		List<StudentGradeShare> LStudentGradeShare = salesService.getGradeStudentShare(grade_id,"","","");		
		Subject subject = courseService.getSubjectByGrade(grade_id).get(0);
		
		float gradeNoRatio = 1;
		String gradeNoRatioStr = "1";
		String defaultMessage = "";
		List<SubjectTeacher> LSubjectTeacher =  courseService.getSubjectTeacher2("",grade.getTeacher_id(),subject.getSubject_seq(),"","1");
		if(LSubjectTeacher.size()==0) { //此科目此老師未設定
			defaultMessage = "* 堂數佔比 : 班別堂數 / 總堂數 尚未設定, 預設為 1";
		}else if(LSubjectTeacher.size()>0 && LSubjectTeacher.get(0).getGradeNo().equals("1")) { //此科目此老師只有一門課
			gradeNoRatio = 1;
			gradeNoRatioStr = "1";			
		}else{
				List<GradeClassNo> LGradeClassNo = courseService.getGradeClassNo("",grade.getSubject_id(),grade.getTeacher_id(),gradeName);
				if(LGradeClassNo.size()>0) {
					String totalClassNo = LGradeClassNo.get(0).getTotalClassNo();
					String gradeClassNo = LGradeClassNo.get(0).getGradeClassNo();			
					if(totalClassNo!=null && !totalClassNo.isEmpty() && gradeClassNo!=null && !totalClassNo.isEmpty()) {
						gradeNoRatio = Float.valueOf(gradeClassNo)/Float.valueOf(totalClassNo);
						gradeNoRatioStr = gradeClassNo+"/"+totalClassNo;
					}
				}else {
					defaultMessage = "* 堂數佔比 : 班別堂數 / 總堂數 尚未設定, 預設為 1";
				}
		}		
		String returnStr = "";
		List<Subject> LSubject1 = courseService.getSubjectWithR("",subject.getSubject_seq(),"","1",grade.getClass_style());
		for(int i=0;i<LStudentGradeShare.size();i++) {
			int costShare = 0;
			int costShare_paid = 0;
			int hrPrice_R = Integer.valueOf(LSubject1.get(0).getHrPrice_R());
			int counselingPrice_R = Integer.valueOf(LSubject1.get(0).getCounselingPrice_R());
			int lagnappePrice_R = Integer.valueOf(LSubject1.get(0).getLagnappePrice_R());
			int handoutPrice_R = Integer.valueOf(LSubject1.get(0).getHandoutPrice_R());
			int homeworkPrice_R = Integer.valueOf(LSubject1.get(0).getHomeworkPrice_R());
			int mockPrice_R = Integer.valueOf(LSubject1.get(0).getMockPrice_R());
			
			//各個科目各個老師各個期別班別成本分攤
			if(LStudentGradeShare.get(i).getCostShare()!=null && !LStudentGradeShare.get(i).getCostShare().isEmpty()) {			
				costShare = Integer.valueOf(LStudentGradeShare.get(i).getCostShare());
				costShare = (int) (costShare*gradeNoRatio); //該科成本*分擔比率
				float share= (float)(costShare*hrPrice_R)/(100);

				LStudentGradeShare.get(i).setHr_share(String.valueOf(Math.round(share)));
				costShare_paid += Math.round(share);
				
				share= (float)(costShare*counselingPrice_R)/(100);
				LStudentGradeShare.get(i).setCounseling_share(String.valueOf(Math.round(share)));
				costShare_paid += Math.round(share);
				
				share= (float)(costShare*lagnappePrice_R)/(100);
				LStudentGradeShare.get(i).setLagnappe_share(String.valueOf(Math.round(share)));	
				costShare_paid += Math.round(share);
				
				share= (float)(costShare*handoutPrice_R)/(100);
				LStudentGradeShare.get(i).setHandout_share(String.valueOf(Math.round(share)));
				costShare_paid += Math.round(share);
				
				share= (float)(costShare*homeworkPrice_R)/(100);
				LStudentGradeShare.get(i).setHomework_share(String.valueOf(Math.round(share)));
				costShare_paid += Math.round(share);
				
				share= (float)(costShare*mockPrice_R)/(100);
				LStudentGradeShare.get(i).setMock_share(String.valueOf(Math.round(share)));
				costShare_paid += Math.round(share);
				
				LStudentGradeShare.get(i).setGrade_share(String.valueOf(costShare-costShare_paid));
			}
		}
		
		int CostTotal=0,HrTotal=0,LagnappeTotal=0,HandoutTotal=0,HomeworkTotal=0,MockTotal=0,GradeTotal=0;
		int CostShare=0;
		String Hr_share,Lagnappe_share,Handout_share,Homework_share,Mock_share,Grade_share,class_style,school_name,register_status;
		for(int i=0;i<LStudentGradeShare.size();i++) {
			if(LStudentGradeShare.get(i)!=null && !LStudentGradeShare.get(i).getCostShare().isEmpty()) {
				CostShare = Integer.valueOf(LStudentGradeShare.get(i).getCostShare());
			}else {
				CostShare = 0;
			}
			CostShare = (int) (CostShare*gradeNoRatio);
			Hr_share = LStudentGradeShare.get(i).getHr_share();
			Lagnappe_share = LStudentGradeShare.get(i).getLagnappe_share();
			Handout_share = LStudentGradeShare.get(i).getHandout_share();
			Homework_share = LStudentGradeShare.get(i).getHomework_share();
			Mock_share = LStudentGradeShare.get(i).getMock_share();
			Grade_share = LStudentGradeShare.get(i).getGrade_share();
			class_style = LStudentGradeShare.get(i).getClass_style();
			String colorStr = "";
			if(class_style.equals("1")) {
				class_style = "正班";
			}else if(class_style.equals("2")) {
				class_style = "Video";
			}else if(class_style.equals("3")) {
				class_style = "線上";				
			}else {
				class_style = "?";
			}
			register_status = LStudentGradeShare.get(i).getRegister_status();
			if(register_status.equals("1")) {
				register_status = "訂班";
			}else if(register_status.equals("2")) {
				register_status = "<span style='background-color:#ffeeff;padding:1px;font-weight:bold'>取消</span>";
				colorStr = "color:red;text-decoration:line-through";
			}else if(register_status.equals("3")) {
				register_status = "<span style='background-color:#ffeeff;padding:1px;font-weight:bold'>保留</span>";
				colorStr = "color:red";
			}else {
				register_status = "?";
			}			
			school_name = LStudentGradeShare.get(i).getSchool_name();
			if(LStudentGradeShare.get(i).getRegister_status().equals("1")) {
				CostTotal += CostShare;
				if(Hr_share!=null && !Hr_share.isEmpty()) {HrTotal += Integer.valueOf(Hr_share);}
				if(Lagnappe_share!=null && !Lagnappe_share.isEmpty()) {LagnappeTotal += Integer.valueOf(Lagnappe_share);}
				if(Handout_share!=null && !Handout_share.isEmpty()) {HandoutTotal += Integer.valueOf(Handout_share);}
				if(Homework_share!=null && !Homework_share.isEmpty()) {HomeworkTotal += Integer.valueOf(Homework_share);}
				if(Mock_share!=null && !Mock_share.isEmpty()) {MockTotal += Integer.valueOf(Mock_share);}
				if(Grade_share!=null && !Grade_share.isEmpty()) {GradeTotal += Integer.valueOf(Grade_share);}
			}
			if(LStudentGradeShare.get(i).getClass_style().equals("1")) {
				returnStr += "<div class='tr' style='background-color:#FEFFE5'>";
			}else {
				returnStr += "<div class='tr' style='background-color:#ffffff'>";
			}
			returnStr+=
						"<div class='td2' style='width:30px;text-align:center;border-bottom:1px #ffeeff solid'>"+(i+1)+"</div>"+			
						"<div class='td2' style='width:100px;text-align:center;border-bottom:1px #ffeeff solid'><A href='javascript:void(0)' onclick='getCostChange("+grade_id+","+LStudentGradeShare.get(i).getStudent_seq()+")' style='font-size:small;font-weight:bold;text-decoration:underline'>"+LStudentGradeShare.get(i).getStudent_no()+"</A></div>"+	
						"<div class='td2' style='width:100px;text-align:center;border-bottom:1px #ffeeff solid'>"+LStudentGradeShare.get(i).getCh_name()+"</div>"+
						"<div class='td2' style='width:80px;text-align:right;border-bottom:1px #ffeeff solid; "+colorStr+"'>"+CostShare+"</div>"+
						"<div class='td2' style='width:80px;text-align:right;border-bottom:1px #ffeeff solid; "+colorStr+"'>"+Hr_share+"</div>"+
						"<div class='td2' style='width:80px;text-align:right;border-bottom:1px #ffeeff solid; "+colorStr+"'>"+Lagnappe_share+"</div>"+
						"<div class='td2' style='width:80px;text-align:right;border-bottom:1px #ffeeff solid; "+colorStr+"'>"+Handout_share+"</div>"+
						"<div class='td2' style='width:80px;text-align:right;border-bottom:1px #ffeeff solid; "+colorStr+"'>"+Homework_share+"</div>"+
						"<div class='td2' style='width:80px;text-align:right;border-bottom:1px #ffeeff solid; "+colorStr+"'>"+Mock_share+"</div>"+
						"<div class='td2' style='width:80px;text-align:right;border-bottom:1px #ffeeff solid; "+colorStr+"'>"+Grade_share+"</div>"+
						"<div class='td2' style='width:80px;text-align:center;border-bottom:1px #ffeeff solid'>"+register_status+"</div>"+
						"<div class='td2' style='width:70px;text-align:center;border-bottom:1px #ffeeff solid'>"+class_style+"</div>"+
						"<div class='td2' style='width:80px;text-align:center;border-bottom:1px #ffeeff solid'>"+school_name+"</div>"+
			        "</div>";
		}
		
	    //表頭總計值	
		returnStr =
		"<div style='font-weight:bold;color:red'>"+defaultMessage+"</div>@"+		
		"<div class='tr'>"+
				"<div class='td2' style='width:30px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'></div>"+
				"<div class='td2' style='width:100px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0;color:darkorange;font-size:x-small'>堂數佔比: "+gradeNoRatioStr+"</div>"+
				"<div class='td2' style='width:100px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'>總 計 &#10140;</div>"+
				"<div class='td2' style='width:80px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'>"+CostTotal+"</div>"+
				"<div class='td2' style='width:80px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'>"+HrTotal+"</div>"+
				"<div class='td2' style='width:80px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'>"+LagnappeTotal+"</div>"+
				"<div class='td2' style='width:80px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'>"+HandoutTotal+"</div>"+
				"<div class='td2' style='width:80px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'>"+HomeworkTotal+"</div>"+
				"<div class='td2' style='width:80px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'>"+MockTotal+"</div>"+
				"<div class='td2' style='width:80px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'>"+GradeTotal+"</div>"+
				"<div class='td2' style='width:80px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'></div>"+				
				"<div class='td2' style='width:70px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'></div>"+
				"<div class='td2' style='width:80px;text-align:center;border-bottom:1px #ffffff solid;font-weight:bold;background-color:#e4edf0'></div>"+
		"</div>" + 	returnStr;	
		return returnStr;
	}	
	
	@RequestMapping("/Course/SubjectExchange")
	public String SubjectExchange(Model model,HttpServletRequest request){	
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);		
		return "/Course/SubjectExchange";
	}
			
	@RequestMapping("/Course/subjectExchangeSave")
	public String subjectExchangeSave(Model model,HttpServletRequest request,Principal principal){	
		String[] A_subject_id_1  = request.getParameterValues("subject_id_1");
		String[] A_subject_id_2  = request.getParameterValues("subject_id_2");
		courseService.subjectExchangeSave(A_subject_id_1,A_subject_id_2,principal.getName());		
		return "redirect:/Course/SubjectExchange";
	}	

	@RequestMapping("/Course/getSubjectExchange")
	@ResponseBody
	public List<SubjectExchange> getSubjectExchange(HttpServletRequest request) {
		 List<SubjectExchange> LSubjectExchange = courseService.getSubjectExchange();		
		return LSubjectExchange;
	}	

	@RequestMapping("/Course/Teacher")
	public String Teacher(Model model,HttpServletRequest request,HttpSession session) {	
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		return "/Course/Teacher";
	}
	
	@RequestMapping("/Course/TeacherLimit")
	public String TeacherLimit(Model model,HttpServletRequest request) {	
		return "/Course/TeacherLimit";
	} 	
	
	@RequestMapping("/Course/getTeacher")
	@ResponseBody
	public List<Teacher> getTeacher(HttpServletRequest request) {	
		List<Teacher> LTeacher = courseService.getTeacher("",request.getParameter("name"),request.getParameter("realName"),request.getParameter("code"),"1","","");		
		return LTeacher;
	}
	
	@RequestMapping("/Course/getTeacherAll")
	@ResponseBody
	public List<Teacher> getTeacherAll(HttpServletRequest request) {	
		List<Teacher> LTeacher = courseService.getTeacher("",request.getParameter("name"),request.getParameter("realName"),request.getParameter("code"),"","","");		
		return LTeacher;
	}	


	@RequestMapping("/Course/TeacherSetting2")
	public String TeacherSetting2(Model model,HttpServletRequest request,HttpSession session) {
		model.addAttribute("page",request.getParameter("page"));
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		if(session.getAttribute("teacher_id_hidden")!=null) {
			model.addAttribute("teacher_id_hidden", session.getAttribute("teacher_id_hidden"));
		}
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","1","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int range = 2; //2個月間隔
		String beginYear = String.valueOf(year);
		String beginMonth = String.valueOf(month+1);
		String endMonth = String.valueOf(month+range>12?month+range-12:month+range);
		String endYear = "";
		if(month+range>12) {
			endYear = String.valueOf(Integer.valueOf(beginYear)+1);
		}else{
			endYear=beginYear;
		}
				
		model.addAttribute("beginYear",beginYear);
		model.addAttribute("endYear",endYear);
		model.addAttribute("beginMonth",beginMonth);
		model.addAttribute("endMonth",endMonth);
		
		return "/Course/TeacherSetting2";
	}
	
	@RequestMapping("/Course/TeacherSetting3")
	public String TeacherSetting3(Model model,HttpServletRequest request,HttpSession session) {
		model.addAttribute("page",request.getParameter("page"));
		List<School> schoolGroup = accountService.getSchool("",""); 
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		if(session.getAttribute("teacher_id_hidden")!=null) {
			model.addAttribute("teacher_id_hidden", session.getAttribute("teacher_id_hidden"));
		}
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","1","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int range = 2; //2個月間隔
		String beginYear = String.valueOf(year);
		String beginMonth = String.valueOf(month+1);
		String endMonth = String.valueOf(month+range>12?month+range-12:month+range);
		String endYear = "";
		if(month+range>12) {
			endYear = String.valueOf(Integer.valueOf(beginYear)+1);
		}else{
			endYear=beginYear;
		}
				
		model.addAttribute("beginYear",beginYear);
		model.addAttribute("endYear",endYear);
		model.addAttribute("beginMonth",beginMonth);
		model.addAttribute("endMonth",endMonth);
		
		return "/Course/TeacherSetting3";
	}	

	@RequestMapping("/Course/CalendarYear2")
	@ResponseBody
	public String CalendarYear2(Model model,HttpServletRequest request,HttpSession session) {
		String beginYear = request.getParameter("beginYear");
		String beginMonth = request.getParameter("beginMonth");
        String teacher_id  = request.getParameter("teacher_id");
        String school_code = request.getParameter("school_code");
        String category_id = request.getParameter("category_id");
        List<Classes> Lclasses = courseService.getClasses("",teacher_id,"",school_code,category_id,"(2,3,4,5)","","","","");//2:送審,3:待上架,4:上架
		List<NoClass> LNoClass = salesService.getNoClass(teacher_id,"","");
		List<Suspension> LSuspension = admService.getSuspension("");
        
	    Calendar dateFrom = Calendar.getInstance();
	    dateFrom.set(Calendar.YEAR,Integer.valueOf(beginYear));
	    dateFrom.set(Calendar.MONTH,Integer.valueOf(beginMonth)-1);
	    dateFrom.set(Calendar.DAY_OF_MONTH,0);
	    
	    Calendar dateTo = Calendar.getInstance();
	    dateTo.set(Calendar.YEAR,Integer.valueOf(beginYear));
	    dateTo.set(Calendar.MONTH,Integer.valueOf(beginMonth)+2);//3個月內
	    dateTo.set(Calendar.DAY_OF_MONTH,0);	    
	    
	    String dateFrom2=null;
		String dayCell1=null,dayCell2=null,dayCell3=null; //當日,早,午,晚
        String dayCell = "",dayCellXY = "";
        String returnStr = "<script>$(function () { $(\"[data-toggle='tooltip']\").tooltip({content: function() {return $(this).attr('title');}}); });</script>";
        	   returnStr += "<div class='tr' style='text-align:center;font-size:x-small'>";
		int emptyCell=0;
	    	while (dateFrom.before(dateTo)) {
	    		emptyCell++;
	    		dateFrom.add(Calendar.DATE,1);
			    //{"0星期日","1星期一","2星期二","3星期三",4"星期四","5星期五","6星期六"};
				int week_index = dateFrom.get(Calendar.DAY_OF_WEEK) - 1;
				if(week_index==0){week_index = 7;} //星期日改為7
				if(emptyCell==1) {
				   for(int i=0;i<(week_index-1);i++) {
	        		   returnStr += 
	        				"<div class='td' style='vertical-align:top'><div class='css-table' style='width:455px'></div></div>";					
				   }
				}   
				SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
				dateFrom2 = format1.format(dateFrom.getTime());
      	           	      				
				dayCell1="";dayCell2="";dayCell3="";
				
				/****放假****/
				for(int x=0;x<LSuspension.size();x++) {				
    	    	   if(LSuspension.get(x).getSuspension_date().equals(dateFrom2)) {
	    	    	    dayCell1 +="<div style='padding:2px;letter-spacing:1px;background-color:red;text-align:center;font-weight:bold;color:white'>&nbsp;</div>";
	    	    	    dayCell2 +="<div style='padding:2px;letter-spacing:1px;background-color:red;text-align:center;font-weight:bold;color:white'>"+LSuspension.get(x).getReason()+"</div>"; 	                	    	
	    	    	    dayCell3 +="<div style='padding:2px;letter-spacing:1px;background-color:red;text-align:center;font-weight:bold;color:white'>&nbsp;</div>"; 	    	    		     	    			   
    	    	   }
				}
				
	        	/****不排課****/
        		if(teacher_id!=null && !teacher_id.isEmpty()) {
	    		    for(int a=0;a<LNoClass.size();a++) { 	    
	    		    	if(LNoClass.get(a).getDateSel().equals(dateFrom2)) {
		    	    	    if(LNoClass.get(a).getNoClassType().equals("1")) {
		    	    	    	if(LNoClass.get(a).getSlot_id().equals("1")) {
		    	    	    		dayCell1 +="<div style='color:red;letter-spacing:3px;background-color:#eeeeee'><img src='/images/no1.png' height='10px'/>&nbsp;希望不排</div>";
		    	    	    	}
		    	    	    	if(LNoClass.get(a).getSlot_id().equals("2")) {
		    	    	    		dayCell2 +="<div style='color:red;letter-spacing:3px;background-color:#eeeeee'><img src='/images/no1.png' height='10px'/>&nbsp;希望不排</div>";
		    	    	    	}
		    	    	    	if(LNoClass.get(a).getSlot_id().equals("3")) {
		    	    	    		dayCell3 +="<div style='color:red;letter-spacing:3px;background-color:#eeeeee'><img src='/images/no1.png' height='10px'/>&nbsp;希望不排</div>";
		    	    	    	}			    	    	    	
		    	    	    }else if(LNoClass.get(a).getNoClassType().equals("2")) {
		    	    	    	if(LNoClass.get(a).getSlot_id().equals("1")) {
		    	    	    		dayCell1 +="<div style='color:darkred;letter-spacing:3px;background-color:#eeeeee'><span><img src='/images/no2.png' height='10px'/></span>&nbsp;不排課</div>";
		    	    	    	}
		    	    	    	if(LNoClass.get(a).getSlot_id().equals("2")) {
		    	    	    		dayCell2 +="<div style='color:darkred;letter-spacing:3px;background-color:#eeeeee'><span><img src='/images/no2.png' height='10px'/></span>&nbsp;不排課</div>";
		    	    	    	}
		    	    	    	if(LNoClass.get(a).getSlot_id().equals("3")) {
		    	    	    		dayCell3 +="<div style='color:darkred;letter-spacing:3px;background-color:#eeeeee'><span><img src='/images/no2.png' height='10px'/></span>&nbsp;不排課 </div>";
		    	    	    	}			    	    	    	
		    	    	    }
	    		    	}    
	    		    }
        		}
        		
		  		/****講座****/      
		          List<Lecture> LLecture = marketingService.getLecture("","","","1","","");
		      
		  	      for(int j=0;j<LLecture.size();j++) {
		  	    	  if(LLecture.get(j).getLectureDate().equals(dateFrom2)) {
		  	    		dayCell1 += 
		  	    				"<div style='background-color:tan;text-align:center;font-weight:bold'>講座:"+LLecture.get(j).getLectureName()+"</div>"+
		  	    	    		"<div style='background-color:tan;text-align:center;'>("+LLecture.get(j).getSchool_name()+LLecture.get(j).getLocation()+") "+LLecture.get(j).getLectureTimeFrom()+"~"+LLecture.get(j).getLectureTimeTo()+"</div>"+
		  	    	    		"<div style='background-color:tan;text-align:center;'>"+LLecture.get(j).getTeacher_name()+"老師"+"</div>";		  	    				
		  	    	  }  	                	    	
		  	      }         		
        		
        	    /****課程****/
        		String pop = "";
        		String popTitle="";      		
        		for(int x=0;x<Lclasses.size();x++) {
        			if(Lclasses.get(x).getClass_date().equals(dateFrom2)) {
        					popTitle="<div style='padding:2px;letter-spacing:1px;background-color:#FFFF66;color:black;text-align:center'>"+Lclasses.get(x).getClass_start_date()+" 期<br>"+Lclasses.get(x).getTime_from()+" ~ "+Lclasses.get(x).getTime_to()+"<br>"+Lclasses.get(x).getTeacher_name()+" 老師<br>"+Lclasses.get(x).getSchool_name()+"<br>("+Lclasses.get(x).getStatusName()+")</div>";
        					pop = "<A href=\"#\" style=\"color:white\" class=\"tooltip-test\" data-toggle=\"tooltip\" title=\""+popTitle+"\">";
        				if(Lclasses.get(x).getSlot_id().equals("1")) {
        					dayCell1 += "<div style='border:1px white solid;background-color:"+Lclasses.get(x).getBgColor()+";padding:1px;border-radius:5px'>"+pop+Lclasses.get(x).getSubject_name()+" "+Lclasses.get(x).getClass_th()+"("+Lclasses.get(x).getTeacher_name()+")</A></div>";
        				}else if(Lclasses.get(x).getSlot_id().equals("2")) {
        					dayCell2 += "<div style='border:1px white solid;background-color:"+Lclasses.get(x).getBgColor()+";padding:1px;border-radius:5px'>"+pop+Lclasses.get(x).getSubject_name()+" "+Lclasses.get(x).getClass_th()+"("+Lclasses.get(x).getTeacher_name()+")</A></div>";
        				}else if(Lclasses.get(x).getSlot_id().equals("3")) {
        					dayCell3 += "<div style='border:1px white solid;background-color:"+Lclasses.get(x).getBgColor()+";padding:1px;border-radius:5px'>"+pop+Lclasses.get(x).getSubject_name()+" "+Lclasses.get(x).getClass_th()+"("+Lclasses.get(x).getTeacher_name()+")</A></div>";
        				} 
        			}
        		}        		
        		
				//日期顯示
				dayCell = new SimpleDateFormat("MM/dd").format(dateFrom.getTime());
        		dayCellXY = new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime());     		
        		String nowColor="";
        		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        		Date dateNow = new Date();
        		String now = dateFormat.format(dateNow);
        		if(dayCellXY.equals(now)) {
        			nowColor="background-color:#FFFF66;font-weight:bold;";
        		}else {
        			nowColor="";
        		}
        		
        		//回傳每日格子內容
        		returnStr += 
        				"<div class='td' style='vertical-align:top'>"+
        		           "<div class='css-table' style='width:455px'>"+
        			          "<div class='tr'>"+
		        					"<div class='td' style='"+nowColor+"height:50px;width:35px;border:1px #ffefff solid;vertical-align:middle'>"+dayCell+"</div>"+
		        					"<div class='td' onclick=\"cellXY('"+dayCellXY+"','1')\" style='width:140px;border:1px #ffefff solid'>"+dayCell1+"</div>"+
		        					"<div class='td' onclick=\"cellXY('"+dayCellXY+"','2')\" style='width:140px;border:1px #ffefff solid'>"+dayCell2+"</div>"+
		        					"<div class='td' onclick=\"cellXY('"+dayCellXY+"','3')\" style='width:140px;border:1px #ffefff solid'>"+dayCell3+"</div>"+
        			          "</div>"+
        			        "</div>"+	
        			     "</div>";	 
				
    		    if(week_index%7==0) {returnStr +="</div><div class='tr'  style='text-align:center;font-size:x-small'>";}
		    }
	    	returnStr +="</div>";
		return returnStr;
	}
	
	@RequestMapping("/Course/Calendar_Grade")
	@ResponseBody
	public String Calendar_Grade(Model model,HttpServletRequest request,HttpSession session) {
		String beginYear = request.getParameter("beginYear");
		String beginMonth = request.getParameter("beginMonth");
        String teacher_id  = request.getParameter("teacher_id");
        String school_code = request.getParameter("school_code");
        String category_id = request.getParameter("category_id");
        String subject_id = request.getParameter("subject_id");
        String classth_1 = request.getParameter("classth_1"); //只顯示第一堂
        List<Classes> Lclasses = new ArrayList<Classes>();
        
        if(classth_1!=null && classth_1.equals("1")) {
        	Lclasses = courseService.getClasses("",teacher_id,"",school_code,category_id,"(2,3,4,5)","1",subject_id,"","");//2:送審,3:待上架,4:上架
        }else {
        	Lclasses = courseService.getClasses("",teacher_id,"",school_code,category_id,"(2,3,4,5)","",subject_id,"","");//2:送審,3:待上架,4:上架
        }

        //有此位老師的模考設定(評測)
        List<TestSubjectSelection2> LTestSubjectSelection2 = admService.getTestSubjectSelection2("","",teacher_id);	
        List<MockBase> LMockBaseTotal = new ArrayList<MockBase>();
        List<String> LString = new ArrayList<String>();
		for(int x=0;x<LTestSubjectSelection2.size();x++) {
			List<MockBase> LMockBase = admService.getMockBase(LTestSubjectSelection2.get(x).getMockBaseTitle_id(),"","","");
			for(int i=0;i<LMockBase.size();i++) {
				LMockBaseTotal.add(LMockBase.get(i));
				LString.add(LTestSubjectSelection2.get(x).getTestSubjectSelection2_seq());
			}
		}
		        
		List<Suspension> LSuspension = admService.getSuspension("");
        
	    Calendar dateFrom = Calendar.getInstance();
	    dateFrom.set(Calendar.YEAR,Integer.valueOf(beginYear));
	    dateFrom.set(Calendar.MONTH,Integer.valueOf(beginMonth)-1);
	    dateFrom.set(Calendar.DAY_OF_MONTH,0);
	    
	    Calendar dateTo = Calendar.getInstance();
	    dateTo.set(Calendar.YEAR,Integer.valueOf(beginYear));
	    dateTo.set(Calendar.MONTH,Integer.valueOf(beginMonth)+1);//2個月內
	    dateTo.set(Calendar.DAY_OF_MONTH,0);	    
	    
	    String dateFrom2=null;
	    String dateFrom3=null;
		String dayCell1=null,dayCell2=null,dayCell3=null; //當日,早,午,晚
        String dayCell = "",dayCellXY = "";
        String returnStr = "<script>$(function () { $(\"[data-toggle='tooltip']\").tooltip({content: function() {return $(this).attr('title');}}); });</script>";
        	   returnStr += "<div class='tr' style='text-align:center;font-size:xx-small'>";
		int emptyCell=0;
	    	while (dateFrom.before(dateTo)) {
	    		emptyCell++;
	    		dateFrom.add(Calendar.DATE,1);
			    //{"0星期日","1星期一","2星期二","3星期三",4"星期四","5星期五","6星期六"};
				int week_index = dateFrom.get(Calendar.DAY_OF_WEEK) - 1;
				if(week_index==0){week_index = 7;} //星期日改為7
				if(emptyCell==1) {
				   for(int i=0;i<(week_index-1);i++) {
	        		   returnStr += 
	        				"<div class='td' style='vertical-align:top'><div class='css-table' style='width:335px'></div></div>";					
				   }
				}   
				SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
				dateFrom2 = format1.format(dateFrom.getTime());
				
				SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
				dateFrom3 = format2.format(dateFrom.getTime());				
      	           	      				
				dayCell1="";dayCell2="";dayCell3="";
				
				/****放假****/
				for(int x=0;x<LSuspension.size();x++) {				
    	    	   if(LSuspension.get(x).getSuspension_date().equals(dateFrom2)) {
	    	    	    dayCell1 +="<div style='padding:2px;letter-spacing:1px;background-color:red;text-align:center;font-weight:bold;color:white'>&nbsp;</div>";
	    	    	    dayCell2 +="<div style='padding:2px;letter-spacing:1px;background-color:red;text-align:center;font-weight:bold;color:white'>"+LSuspension.get(x).getReason()+"</div>"; 	                	    	
	    	    	    dayCell3 +="<div style='padding:2px;letter-spacing:1px;background-color:red;text-align:center;font-weight:bold;color:white'>&nbsp;</div>"; 	    	    		     	    			   
    	    	   }
				}
				
		  		/****講座****/      
		          List<Lecture> LLecture = marketingService.getLecture("",category_id,"","1","","");
		      
		  	      for(int j=0;j<LLecture.size();j++) {
		  	    	  if(LLecture.get(j).getLectureDate().equals(dateFrom2)) {
		  	    		dayCell1 += 
		  	    				"<div style='background-color:tan;text-align:center;font-weight:bold'>講座:"+LLecture.get(j).getLectureName()+"</div>"+
		  	    	    		"<div style='background-color:tan;text-align:center;'>("+LLecture.get(j).getSchool_name()+LLecture.get(j).getLocation()+") "+LLecture.get(j).getLectureTimeFrom()+"~"+LLecture.get(j).getLectureTimeTo()+"</div>"+
		  	    	    		"<div style='background-color:tan;text-align:center;'>"+LLecture.get(j).getTeacher_name()+"</div>";		  	    				
		  	    	  }  	                	    	
		  	      } 				
				
        		
        	    /****課程****/
        		String pop = "";
        		String popTitle="";      		
        		for(int x=0;x<Lclasses.size();x++) {
        			if(Integer.valueOf(Lclasses.get(x).getClass_th())>0 && Lclasses.get(x).getClass_date().equals(dateFrom2)) {
        					popTitle="<div style='font-size:small;padding:1px;background-color:#FFFF66;color:black;text-align:center'>"+Lclasses.get(x).getClass_start_date()+"期<br>"+Lclasses.get(x).getTime_from()+"~"+Lclasses.get(x).getTime_to()+"<br>"+Lclasses.get(x).getTeacher_name()+"<br>"+Lclasses.get(x).getSchool_name()+"<br>("+Lclasses.get(x).getStatusName()+")</div>";
        					pop = "<A href=\"#\" style=\"color:white\" class=\"tooltip-test\" data-toggle=\"tooltip\" title=\""+popTitle+"\">";
        				if(Lclasses.get(x).getSlot_id().equals("1")) {
        					dayCell1 += "<div style='padding:2px;border:1px white solid;background-color:"+Lclasses.get(x).getBgColor()+";padding:0px;border-radius:2px'>"+pop+Lclasses.get(x).getSubject_name()+" "+Lclasses.get(x).getClass_th()+"("+Lclasses.get(x).getTeacher_name()+")</A></div>";
        				}else if(Lclasses.get(x).getSlot_id().equals("2")) {
        					dayCell2 += "<div style='padding:2px;border:1px white solid;background-color:"+Lclasses.get(x).getBgColor()+";padding:0px;border-radius:2px'>"+pop+Lclasses.get(x).getSubject_name()+" "+Lclasses.get(x).getClass_th()+"("+Lclasses.get(x).getTeacher_name()+")</A></div>";
        				}else if(Lclasses.get(x).getSlot_id().equals("3")) {
        					dayCell3 += "<div style='padding:2px;border:1px white solid;background-color:"+Lclasses.get(x).getBgColor()+";padding:0px;border-radius:2px'>"+pop+Lclasses.get(x).getSubject_name()+" "+Lclasses.get(x).getClass_th()+"("+Lclasses.get(x).getTeacher_name()+")</A></div>";
        				} 
        			}
        		} 
        		
        	    /****真人評測****/
        		String sectionStr ="";       		   
        			for(int i=0;i<LMockBaseTotal.size();i++) {
        				if(LMockBaseTotal.get(i).getSetDate().equals(dateFrom3)) {
                			sectionStr = "";
                			List<MockLimit> LMockLimit = admService.getMockLimit(LString.get(i),"");
            				for(int a=0;a<LMockLimit.size();a++) {
            					sectionStr = "評測"+LMockLimit.get(a).getFromx()+"~"+LMockLimit.get(a).getTox();
            					if(LMockLimit.get(a).getSlot().equals("1")) {
                					dayCell1 += "<div style='font-size:xx-small'>"+sectionStr+"</div>";
                				}else if(LMockLimit.get(a).getSlot().equals("2")) {
                					dayCell2 += "<div style='font-size:xx-small'>"+sectionStr+"</div>";
                				}else if(LMockLimit.get(a).getSlot().equals("3")) {
                					dayCell3 += "<div style='font-size:xx-small'>"+sectionStr+"</div>";
                				}
            				}       					
        				}
        			}
        			
        		/****充電站****/
        		String section2Str =""; 	
        		List<CounselingBase> LCounselingBase = admService.getCounselingBase("",dateFrom3,"");
        		if(LCounselingBase.size()>0) {
        			String slotStr = LCounselingBase.get(0).getSlotStr();
        			String slot_in = "("+slotStr.replace("#",",").substring(0,slotStr.length()-1)+")";
        			List<CounselingLimit1> LCounselingLimit1 = admService.getCounselingLimit1(LCounselingBase.get(0).getCounselingBaseTitle_seq(),"",teacher_id,slot_in);
        			for(int i=0;i<LCounselingLimit1.size();i++) {
        				List<CounselingLimit2> LCounselingLimit2 = admService.getCounselingLimit2(LCounselingLimit1.get(i).getCounselingLimit1_seq(),"");
        				for(int j=0;j<LCounselingLimit2.size();j++) {
        					section2Str = "充電站"+LCounselingLimit2.get(j).getFrom1()+"~"+LCounselingLimit2.get(j).getTo1();
        					if(LCounselingLimit1.get(i).getSlot().equals("1")) {
            					dayCell1 += "<div style='font-size:xx-small'>"+section2Str+"</div>";
            				}else if(LCounselingLimit1.get(i).getSlot().equals("2")) {
            					dayCell2 += "<div style='font-size:xx-small'>"+section2Str+"</div>";
            				}else if(LCounselingLimit1.get(i).getSlot().equals("3")) {
            					dayCell3 += "<div style='font-size:xx-small'>"+section2Str+"</div>";
            				}        					 
        				}
        			}        			
        		}
        		
				//日期顯示
				dayCell = new SimpleDateFormat("MM/dd").format(dateFrom.getTime());
        		dayCellXY = new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime());     		
        		String nowColor="";
        		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        		Date dateNow = new Date();
        		String now = dateFormat.format(dateNow);
        		if(dayCellXY.equals(now)) {
        			nowColor="background-color:#FFFF66;font-weight:bold;";
        		}else {
        			nowColor="";
        		}
        		
        		//回傳每日格子內容
        		returnStr += 
        				"<div class='td' style='vertical-align:top'>"+
        		           "<div class='css-table' style='width:455px'>"+
        			          "<div class='tr'>"+
		        					"<div class='td' style='"+nowColor+"height:50px;width:35px;border:1px #eeeeee dotted;vertical-align:middle;font-weight:bold'>"+dayCell+"</div>"+
		        					"<div class='td' onclick=\"cellXY('"+dayCellXY+"','1')\" style='width:140px;border:1px #eeeeee dotted'>"+dayCell1+"</div>"+
		        					"<div class='td' onclick=\"cellXY('"+dayCellXY+"','2')\" style='width:140px;border:1px #eeeeee dotted'>"+dayCell2+"</div>"+
		        					"<div class='td' onclick=\"cellXY('"+dayCellXY+"','3')\" style='width:140px;border:1px #eeeeee dotted'>"+dayCell3+"</div>"+
        			          "</div>"+
        			        "</div>"+	
        			     "</div>";	 
				
    		    if(week_index%7==0) {returnStr +="</div><div class='tr'  style='text-align:center;font-size:x-small'>";}
		    }
	    	returnStr +="</div>";
		return returnStr;
	}		

	@RequestMapping("/Course/Calendar_Suspension")
	@ResponseBody
	public String Calendar_Suspension(Model model,HttpServletRequest request,HttpSession session) {
		String beginYear = request.getParameter("beginYear");
		String beginMonth = request.getParameter("beginMonth");
      
	    Calendar dateFrom = Calendar.getInstance();
	    dateFrom.set(Calendar.YEAR,Integer.valueOf(beginYear));
	    dateFrom.set(Calendar.MONTH,Integer.valueOf(beginMonth)-1);
	    dateFrom.set(Calendar.DAY_OF_MONTH,0);
	    
	    Calendar dateTo = Calendar.getInstance();
	    dateTo.set(Calendar.YEAR,Integer.valueOf(beginYear));
	    dateTo.set(Calendar.MONTH,Integer.valueOf(beginMonth)+2);//3個月內
	    dateTo.set(Calendar.DAY_OF_MONTH,0);	    
	    
	    String dateFrom2=null;
		String dayCell1=null;
        String dayCell = "",dayCellXY = "";
        String returnStr = "<script>$(function () { $(\"[data-toggle='tooltip']\").tooltip({content: function() {return $(this).attr('title');}}); });</script>";
        	   returnStr += "<div class='tr' style='text-align:center;font-size:x-small'>";
		int emptyCell=0;
	    	while (dateFrom.before(dateTo)) {
	    		emptyCell++;
	    		dateFrom.add(Calendar.DATE,1);
			    //{原week_index-->"0星期日","1星期一","2星期二","3星期三",4"星期四","5星期五","6星期六"};
				int week_index = dateFrom.get(Calendar.DAY_OF_WEEK) - 1;
				if(week_index==0){week_index = 7;} //星期日改為7
				
				if(emptyCell==1) {
				   for(int i=0;i<(week_index-1);i++) { //前面空的格子
	        		   returnStr += "<div class='td' style='vertical-align:top'><div class='css-table' style='border:1px #ffefff solid;width:151px;height:51px;'>&nbsp;</div></div>";					
				   }
				}   
				SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
				dateFrom2 = format1.format(dateFrom.getTime());
      	           	      				
				dayCell1="";
				
				/****放假****/
				List<Suspension> LSuspension = admService.getSuspension("");
				for(int x=0;x<LSuspension.size();x++) {				
    	    	   if(LSuspension.get(x).getSuspension_date().equals(dateFrom2)) {
    	    		   
    	    		    List<Suspension_allow> LSuspension_allow = admService.getSuspensionAllow(dateFrom2);
    	    		    //String slot1="-",slot2="-",slot3="-";
    	    		    String slot1="",slot2="",slot3="";
	    	    	    for(int a=0;a<LSuspension_allow.size();a++){
	    	    	    	if(LSuspension_allow.get(a).getAllow_slot().equals("1")) {
	    	    	    		slot1 = "&#10003;";
	    	    	    	}else if(LSuspension_allow.get(a).getAllow_slot().equals("2")) {
	    	    	    		slot2 = "&#10003;";
	    	    	    	}else if(LSuspension_allow.get(a).getAllow_slot().equals("3")) {
	    	    	    		slot3 = "&#10003;";
	    	    	    	}
	    	    	    }
	    	    	    
    	    		    dayCell1 +="<div style='padding:2px;letter-spacing:1px;background-color:red;text-align:center;font-weight:bold;color:white'>"+LSuspension.get(x).getReason()+"<span style='font-weight:normal;font-size:xx-small'>"+slot1+slot2+slot3+"</span></div>"; 	                	    	    	    		     	    			   
    	    	   }
				}
      		        		
				//日期顯示
				dayCell = new SimpleDateFormat("MM/dd").format(dateFrom.getTime());
        		dayCellXY = new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime());     		
        		String nowColor="";
        		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        		Date dateNow = new Date();
        		String now = dateFormat.format(dateNow);
        		if(dayCellXY.equals(now)) {
        			nowColor="background-color:#FFFF66;font-weight:bold;";
        		}else {
        			nowColor="";
        		}
        		
        		//回傳每日格子內容
        		returnStr += 
        				"<div class='td' style='vertical-align:top'>"+
        		           "<div class='css-table' style='border:1px #ffefff solid;width:151px'>"+
        			          "<div class='tr'>"+
		        					"<div class='td' style='border-radius:0px;padding:0px;"+nowColor+"height:50px;width:35px;vertical-align:top'>"+dayCell+"</div>"+
		        					"<div class='td' onmouseout=\"this.style.background ='#ffffff';\" onMouseOver=\"this.style.cursor='pointer';this.style.background ='#ffefff';\" onclick=\"cellXY('"+dayCellXY+"','1')\" style=''>"+dayCell1+"</div>"+
        			          "</div>"+
        			        "</div>"+	
        			     "</div>";	 
				
    		    if(week_index%7==0) {returnStr +="</div><div class='tr'  style='text-align:center;font-size:x-small'>";}
		    }
	    	returnStr +="</div>";
		return returnStr;
	}		

	@RequestMapping("/Course/suspensionCheck")
	@ResponseBody
	public String suspensionCheck(Model model,HttpServletRequest request,HttpSession session) {
		String suspension_date = request.getParameter("suspension_date");
		if(suspension_date!=null && !suspension_date.isEmpty()) {
			List<Suspension> LSuspension = admService.getSuspension(suspension_date);
			if(LSuspension.size()>0) {
				return "1"; //為國定假日
			}else {
				return "0";
			}
		}else {
			return "0";
		}
	}
	
	
	@RequestMapping("/Course/TeacherProfile")
	public String TeacherProfile(Model model,HttpServletRequest request,HttpSession session){
		String teacher_id_hidden=request.getParameter("teacher_id");
		Teacher teacher = null;
		if((teacher_id_hidden==null && session.getAttribute("teacher_id_hidden")==null) || (teacher_id_hidden!=null && teacher_id_hidden.equals("-1"))) {
			teacher = new Teacher();
		}else {
			if(teacher_id_hidden!=null && !teacher_id_hidden.isEmpty()) {
				teacher = courseService.getTeacher(teacher_id_hidden,"","","","","","").get(0);
				session.setAttribute("teacher_id_hidden", teacher_id_hidden);
				session.setAttribute("teacher_name", teacher.getName());				
			}else {	
				teacher = courseService.getTeacher((String)session.getAttribute("teacher_id_hidden"),"","","","","","").get(0);
			}			
		}
		String chk1="",chk2="",chk3="",chk4="",chk5="";
		if(teacher.getEmail_contact()!=null && teacher.getEmail_contact().equals("on")) {chk1 = "checked";}
		if(teacher.getLine_contact()!=null && teacher.getLine_contact().equals("on")) {chk2 = "checked";}
		if(teacher.getFb_contact()!=null && teacher.getFb_contact().equals("on")) {chk3 = "checked";}
		if(teacher.getTel_contact()!=null && teacher.getTel_contact().equals("on")) {chk4 = "checked";}
		if(teacher.getFace_contact()!=null && teacher.getFace_contact().equals("on")) {chk5 = "checked";}
		String checkStr = 
	            "<input type='checkbox' name='email_contact' "+chk1+">Email"+
	            "&nbsp;&nbsp;<input type='checkbox' name='line_contact' "+chk2+"><img src='/images/line.png' style='width:18px'>"+
	            "&nbsp;&nbsp;<input type='checkbox' name='fb_contact' "+chk3+"><img src='/images/fb.png' style='width:17px'>"+
	            "&nbsp;&nbsp;<input type='checkbox' name='tel_contact' "+chk4+">手機"+
	            "&nbsp;&nbsp;<input type='checkbox' name='face_contact' "+chk5+">面談	";
		
		teacher.setCheckStr(checkStr);
		model.addAttribute("teacher",teacher);
		model.addAttribute("county",teacher.getCounty());
		model.addAttribute("district",teacher.getDistrict());
		
    	if(teacher.getVirtual_teacher()!=null && teacher.getVirtual_teacher().equals("1")) {
    		List<Virtual_teacher> LVirtual_teacher = courseService.getVirtual_teacher(teacher_id_hidden);
    		String virtual_teacherStr = "";
    		for(int i=0;i<LVirtual_teacher.size();i++) {
    			virtual_teacherStr +=
				    "<div>"+
						"<input type='hidden' name='teacher_seqSel' value='"+LVirtual_teacher.get(i).getTeacher_id_child()+"'>"+
						"<A href='javascript:void(0)' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete.png' height='8px'/></A>&nbsp;&nbsp;"+
					    "<span style='color:darkblue;font-size:small;font-weight:bold'>"+LVirtual_teacher.get(i).getTeacher_id_childName()+"</span>"+
					"</div>";    					
    		}
    		model.addAttribute("virtual_teacherStr",virtual_teacherStr);
    	}
    	    	
    	List<Teacher> LTeacher = courseService.getTeacher("","","","","","0","");
    	model.addAttribute("LTeacher", LTeacher);
    	
    	List<School> schoolGroup = accountService.getSchool("","");
    	model.addAttribute("schoolGroup",schoolGroup);     	
    	
		return "/Course/TeacherProfile";
	}
	
	@RequestMapping("/Course/Discontinued")
	public String Discontinued(Model model,HttpServletRequest request,HttpSession session){
		if(session.getAttribute("teacher_id_hidden")==null) {
			return "/Course/Teacher";
		}else {
			model.addAttribute("teacher_id", session.getAttribute("teacher_id_hidden"));
			
			List<School> schoolGroup = accountService.getSchool("",""); 
			model.addAttribute("schoolGroup", schoolGroup);
					
			List<Category> categoryGroup = courseService.getCategory("","");
			model.addAttribute("categoryGroup", categoryGroup);
		}

		return "/Course/Discontinued";
	}	
	
	
    @RequestMapping(value="/Course/TeacherProfileSave", method=RequestMethod.POST)
    public String TeacherProfileSave(@Valid Teacher teacher,BindingResult bindingResult,Model model,HttpServletRequest request,Principal principal) {

        if (bindingResult.hasErrors()) {       	
            return "/Course/TeacherProfile";        
        }
        
        String[] A_teacher_seqSel = request.getParameterValues("teacher_seqSel");
        teacher.setCounty(request.getParameter("county"));
        teacher.setDistrict(request.getParameter("district"));
        
    	courseService.TeacherProfileSave(teacher,principal.getName(),A_teacher_seqSel);
        return "/Course/Teacher";
    } 
   
	@RequestMapping("/Course/FaceOrVideo")
	public String FaceOrVideo(Model model,HttpServletRequest request) {
		model.addAttribute("JL_gradeId",request.getParameter("JL_gradeId"));
		model.addAttribute("grade_seq",request.getParameter("grade_seq"));
		return "/Course/FaceOrVideo";
	} 

	@RequestMapping("/Course/classReceive")
	public String classReceive(Model model,HttpServletRequest request) {		
		model.addAttribute("TextareaTH",request.getParameter("TextareaTH"));
		return "/Course/classReceive";
	} 	

	@RequestMapping("/Course/getClassesOption")
	@ResponseBody
	public String getClassesOption(HttpServletRequest request) {
		String grade_id = request.getParameter("grade_id");
		List<Classes> LClasses = courseService.getClasses(grade_id,"","","","","","","","","");
		String returnStr = "";
		for(int i=0;i<LClasses.size();i++) {
			if(!LClasses.get(i).getClass_th().equals("-1")) {
				returnStr +="<option value='"+LClasses.get(i).getClass_th()+"@"+LClasses.get(i).getClass_date()+"'>第"+LClasses.get(i).getClass_th()+"堂"+" - "+LClasses.get(i).getClass_date()+"</option>";
			}	
		}
		return returnStr;
	}
	
    @RequestMapping("/Course/BillBoard")
    public String BillBoard(Model model) {   	
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int range = 3; //3個月間隔
		String beginYear = String.valueOf(year);
		String beginMonth = String.valueOf(month+1);
		String endMonth = String.valueOf(month+range>12?month+range-12:month+range);
		String endYear = "";
		if(month+range>12) {
			endYear = String.valueOf(Integer.valueOf(beginYear)+1);
		}else{
			endYear=beginYear;
		}
				
		model.addAttribute("beginYear",beginYear);
		model.addAttribute("endYear",endYear);
		model.addAttribute("beginMonth",beginMonth);
		model.addAttribute("endMonth",endMonth);
        return "/Course/BillBoard";
    } 
    
    @RequestMapping("/Course/BillBoard_Setting")
    public String BillBoard_Setting(Model model,HttpServletRequest request) {
    	 String dateSel = request.getParameter("dateSel");
    	 model.addAttribute("dateSel",dateSel);
    	 
    	 String reason = "";
    	 List<Suspension> LSuspension = admService.getSuspension(dateSel);
    	 if(LSuspension.size()>0) {
    		 reason = LSuspension.get(0).getReason();
    	 }
    	 model.addAttribute("reason",reason);
    	 
    	 List<Suspension_allow> LSuspension_allow = admService.getSuspensionAllow(dateSel);
    	 String chk1="",chk2="",chk3="";
    	 for(int i=0;i<LSuspension_allow.size();i++) {
    		 if(LSuspension_allow.get(i).getAllow_slot().equals("1")) {
    			 chk1 = "checked";
    		 }else if(LSuspension_allow.get(i).getAllow_slot().equals("2")) {
    			 chk2 = "checked";
    		 }else if(LSuspension_allow.get(i).getAllow_slot().equals("3")) {
    			 chk3 = "checked";
    		 }
    	 }
	    	 String checkStr = 
				"<input type='checkbox' name='allow_slot' value='1' "+chk1+">早&nbsp;&nbsp;"+
				"<input type='checkbox' name='allow_slot' value='2' "+chk2+">午&nbsp;&nbsp;"+
				"<input type='checkbox' name='allow_slot' value='3' "+chk3+">晚"; 
	    	 
	    	 model.addAttribute("checkStr",checkStr);
    	 
    	 return "/Course/BillBoard_Setting";
    }

    @RequestMapping("/Course/SuspensionSave")
    public String SuspensionSave(HttpServletRequest request,Principal principal) {
    	String suspension_date = request.getParameter("suspension_date");
    	String reason = request.getParameter("reason");
    	String updater = principal.getName();
    	String[] A_allow_slot = request.getParameterValues("allow_slot");
    	admService.SuspensionSave(suspension_date,reason,updater,A_allow_slot); 
    	return "/common/closeAndReload";    	
    } 
    

    @RequestMapping("/Course/getSuspension")
    @ResponseBody
    public List<Suspension> getSuspension(HttpServletRequest request){
        return admService.getSuspension("");
    }  
    
    @RequestMapping("/Course/getCostChange")
    public String getCostChange(HttpServletRequest request,Model model){
    	String grade_id = request.getParameter("grade_id");
    	String student_no = request.getParameter("student_no");
    	List<orderChange> LOrderChange = salesService.getCostChange(grade_id,student_no);
    	for(int i=0;i<LOrderChange.size();i++) {
    		if(!LOrderChange.get(i).getSubject_from().equals("")) {
    			LOrderChange.get(i).setSubject_from(courseService.getSubject("",LOrderChange.get(i).getSubject_from(),"","1","","0").get(0).getName());
    		}
    		if(!LOrderChange.get(i).getSubject_to().equals("")) {
    			LOrderChange.get(i).setSubject_to(courseService.getSubject("",LOrderChange.get(i).getSubject_to(),"","1","","0").get(0).getName());
    	
    		}
    	}
    	String returnStr = "";
    	for(int i=0;i<LOrderChange.size();i++) {
    		String changeType = LOrderChange.get(i).getChangeType();
			if(changeType.equals("-1")){
				changeType = "溢繳退款";					
		    }else if(changeType.equals("1")){
		    	changeType = "換課";
			}else if(changeType.equals("2")){
				changeType = "報名新增";
			}else if(changeType.equals("3")){
				changeType = "報名繳費";
			}else if(changeType.equals("4")){
				changeType = "應繳更改";
			}else if(changeType.equals("5")){
				changeType = "繳費方式";
			}else if(changeType.equals("6")){
				changeType = "課程費用";
			}else if(changeType.equals("7")){
				changeType = "作業費用";
			}else if(changeType.equals("8")){
				changeType = "訂單退費";
			}else if(changeType.equals("9")){
				changeType = "繳費";
			}    		
    		returnStr+=
			"<div class='tr' style='background-color:#ffeeff;font-size:small'>"+
				"<div class='td2' style='width:75px;font-size:x-small'>"+LOrderChange.get(i).getUpdateTime()+"</div>"+	
				"<div class='td2' style='width:80px'>"+LOrderChange.get(i).getRegister_id()+"</div>"+
				"<div class='td2' style='width:80px'>"+changeType+"</div>"+
				"<div class='td2' style='width:110px'>"+(LOrderChange.get(i).getSubject_from().equals("")?"":LOrderChange.get(i).getSubject_from()+"<br>&#10139;")+LOrderChange.get(i).getSubject_to()+"</div>"+	
				"<div class='td2' style='width:180px'>"+(LOrderChange.get(i).getGradeName_to().equals("")?"":LOrderChange.get(i).getGradeName_to()+"<br>&#10139;")+LOrderChange.get(i).getGradeName_to()+"</div>"+
				"<div class='td2' style='width:100px'>"+(LOrderChange.get(i).getPayMoney_from().equals("")?"":LOrderChange.get(i).getPayMoney_from()+"<br>&#10139;")+LOrderChange.get(i).getPayMoney_to()+"</div>"+
				"<div class='td2' style='width:100px'>"+(LOrderChange.get(i).getActualPrice_from().equals("")?"":LOrderChange.get(i).getActualPrice_from()+"<br>&#10139;")+LOrderChange.get(i).getActualPrice_to()+"</div>"+
		   "</div>";   		
    	}
    	
    	model.addAttribute("returnStr",returnStr);
        return "/Course/costChange";
    } 
    
	@RequestMapping("/Course/existVideo")
	@ResponseBody
	public String existVideo(HttpServletRequest request) throws ParseException {
		String grade_id = request.getParameter("grade_id");
		String class_th_ex = request.getParameter("class_th_ex");
		String assginDate =  request.getParameter("assginDate"); 
		Classes classes = courseService.getClasses(grade_id,"","","","","",class_th_ex,"","","").get(0);
		if(classes.getClass_date()==null || classes.getClass_date().isEmpty()) {
			return "1"; //政龍課程無堂數時間
		}else if(classes.getClass_date().length()==10) {
			//課堂日期			
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Date class_th = sdf.parse(classes.getClass_date());	
			//指定日期前二日			
			Calendar cal = Calendar.getInstance(); 
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			cal.setTime(sdf2.parse(assginDate));
			int day1 = cal.get(Calendar.DATE);
			cal.set(Calendar.DATE, day1 - 1);
			if (cal.getTime().after(class_th)) {
				return "1";
			}else {
				return "0";
			}		
		}else {
		   return "0";
		}		
	} 
	

    @RequestMapping("/Course/teacherAndGrade")
    public String teacherAndGrade(Model model,HttpServletRequest request) { 
    	String subject_seq = request.getParameter("subject_seq");
    	model.addAttribute("subject_seq",subject_seq);

    	Subject subject = courseService.getSubject("",subject_seq,"","","","0").get(0);    	
    	model.addAttribute("subject_name",subject.getName());    	
        return "/Course/teacherAndGrade";
    }
    
    
    @RequestMapping("/Course/getSubjectTeacher")
    @ResponseBody
    public List<SubjectTeacher> getSubjectTeacher(Model model,HttpServletRequest request){
    	String subject_seq = request.getParameter("subject_seq");
        List<SubjectTeacher> LSubjectTeacher =   courseService.getSubjectTeacher(subject_seq);
        return LSubjectTeacher;
    }
    

    @RequestMapping("/Course/teacherAndGradeEdit")
    public String teacherAndGradeEdit(Model model,HttpServletRequest request) {
    	String subject_id = request.getParameter("subject_id");
    	String subjectTeacher_seq = request.getParameter("subjectTeacher_seq");
    	String teacher_id = request.getParameter("teacher_id");
    	Teacher teacher = courseService.getTeacher(teacher_id,"","","","","","").get(0);
    	model.addAttribute("teacher_name",teacher.getName());   	
    	if(subjectTeacher_seq==null || subjectTeacher_seq.equals("")) {subjectTeacher_seq="-1";}
    	SubjectTeacher subjectTeacher = new SubjectTeacher();
    	List<SubjectTeacher> LSubjectTeacher = courseService.getSubjectTeacher2(subjectTeacher_seq,teacher_id,subject_id,"","1");
    	//老師與期別限制存在
    	if(LSubjectTeacher.size()>0) {		
    		subjectTeacher = LSubjectTeacher.get(0);
    		model.addAttribute("newTeacher","0");
    		List<GradeClassNo> LGradeClassNo = new ArrayList<GradeClassNo>();
    		LGradeClassNo = courseService.getGradeClassNo(subjectTeacher.getSubjectTeacher_seq(),"","","");
    		model.addAttribute("LGradeClassNo",LGradeClassNo);
    	//老師有開課	
    	}else if(teacher_id!=null && !teacher_id.isEmpty()){
	    	subjectTeacher.setTeacher_id(teacher_id);
	    	subjectTeacher.setSubject_id(subject_id);
	    	model.addAttribute("newTeacher","0");
	    	
	    //老師未加入(新增老師)	
    	}else {
    		List<Subject> LSubject = courseService.getSubject("",subject_id,"","","","0");
    		subjectTeacher.setSubject_id(subject_id);
    		subjectTeacher.setSubject_name(LSubject.get(0).getName());
    		model.addAttribute("newTeacher","1");
    		List<Teacher> teacherGroup = courseService.getTeacher("","","","","1","","");
    		model.addAttribute("teacherGroup", teacherGroup);    		
    	}
    	model.addAttribute("subjectTeacher",subjectTeacher);
    
    	
		//預選期別
		List<Grade> LpreGrade = courseService.getGrade("","4,5",subject_id,"","","",teacher_id,"200","","","","","");
		model.addAttribute("LpreGrade", LpreGrade);	
		
		List<Pre_grade> LPre_grade = salesService.getPre_grade(subjectTeacher_seq,"");
		String preGradeStr = "";
		for(int i=0;i<LPre_grade.size();i++) {
			List<Grade> LGrade = courseService.getGrade(LPre_grade.get(i).getGrade_id(),"","","","","","","200","","","","","");
			if(LGrade.size()>0) {				
				LPre_grade.get(i).setGradeName(LGrade.get(0).getGradeName());
				LPre_grade.get(i).setClass_start_date(LGrade.get(0).getClass_start_date());
			}	
			
			preGradeStr += 
					"<div>"+
						"<input type='hidden' name='pre_grade_seq' value='"+LPre_grade.get(i).getGrade_id()+"'>"+
						"<A href='javascript:void(0)' title='刪除此筆'  onclick='$(this).parent().remove();'><img src='/images/delete.png' height='8px'/></A>&nbsp;&nbsp;"+
						"<div>"+
						    "期別: <span style='color:darkblue;font-size:small;font-weight:bold'>"+LPre_grade.get(i).getClass_start_date()+"-"+LPre_grade.get(i).getGradeName()+"-"+LPre_grade.get(i).getVideo_date()+"</span>"+
						"</div>"+
						"<div>"+
					    	"備註: <input type='text' name='pre_grade_remark' value='"+LPre_grade.get(i).getGrade_remark()+"' style='width:180px;border-radius:3px;border:1px #aaaaaa solid' placeholder='ex.實體課前先上'>"+
					    "</div>"+ 							
					"</div>";					
		}		
		model.addAttribute("preGradeStr",preGradeStr);
		
    	return "/Course/teacherAndGradeEdit";
    }   
 
    @RequestMapping("/Course/DeleteSubjectTeacher")
    public String DeleteSubjectTeacher(Model model,HttpServletRequest request,Principal principal) {
    	String subjectTeacher_seq = request.getParameter("subjectTeacher_seq");
    	String subject_id = request.getParameter("subject_id"); 
    	courseService.DeleteSubjectTeacher(subjectTeacher_seq,principal.getName());
    	
    	return "/common/closeAndReload";
    }
    
    @RequestMapping("/Course/programSetting")
    public String programSetting(Model model,HttpServletRequest request,HttpSession session) {
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);	
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int range = 2; //2個月間隔
		String beginYear = String.valueOf(year);
		String beginMonth = String.valueOf(month+1);
		String endMonth = String.valueOf(month+range>12?month+range-12:month+range);
		String endYear = "";
		if(month+range>12) {
			endYear = String.valueOf(Integer.valueOf(beginYear)+1);
		}else{
			endYear=beginYear;
		}
				
		model.addAttribute("beginYear",beginYear);
		model.addAttribute("endYear",endYear);
		model.addAttribute("beginMonth",beginMonth);
		model.addAttribute("endMonth",endMonth);
		
        return "/Course/programSetting";
    }
    
    
    @RequestMapping("/Course/OnsiteProgram")
    public String OnsiteProgram(Model model,HttpServletRequest request,Principal principal,HttpSession session) { 
		String menu = request.getParameter("menu");
		if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
		
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);		
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	

		String school_code = accountService.getEmployee("","","","","","",principal.getName(),"").get(0).getSchool();
		model.addAttribute("school_code",school_code);
		model.addAttribute("category_id","1"); //暫用實力培訓為預設值
		
		String pop = request.getParameter("pop");
		if(pop!=null && pop.equals("1")) {
			model.addAttribute("pop","1");
		}else {
			model.addAttribute("pop","0");
		}			
        return "/Course/OnsiteProgram";
    }
    
	@RequestMapping("/Course/teacherGradeSearch")
	@ResponseBody
	public String teacherGradeSearch(HttpServletRequest request) {
		String school_code = request.getParameter("school_code");
		String category_id = request.getParameter("category_id");
		String teacher_id  = request.getParameter("teacher_id");
		
		String grade_status_1 = request.getParameter("grade_status_1"); //未上架
		String grade_status_2 = request.getParameter("grade_status_2"); //上架
		String grade_status_3 = request.getParameter("grade_status_3"); //video未下架
		String status_code = "";
		if(grade_status_1.equals("true")) {status_code +="2,3,6,";}
		if(grade_status_2.equals("true")) {status_code +="4,";}
		if(grade_status_3.equals("true")) {status_code +="5,";}
		List<Grade> LGrade = courseService.getGrade("",status_code.substring(0,status_code.length()-1),"",school_code,"",category_id,teacher_id,"","a.school_code desc,a.category_id,a.subject_id,a.grade_date desc","1","","","");
		String returnStr ="";
		String showSchool = "";
		String tmpCategory = "";
		String showCategory = "";
		String showColor = "";
		String showEdit = "";
		for(int i=0;i<LGrade.size();i++) {
			if(!LGrade.get(i).getCategory_name().equals(tmpCategory)) {
				tmpCategory = LGrade.get(i).getCategory_name();
				showCategory = tmpCategory;
				showSchool = LGrade.get(i).getSchool_name();
				showColor = "aliceblue";
				showEdit = "<A href='javascript:void(0)' onclick=statusEdit('"+teacher_id+"','"+school_code+"','"+LGrade.get(i).getCategory_id()+"','-1','"+LGrade.get(i).getCategory_name()+"&nbsp;/&nbsp;**所有期別**')><img src='/images/gear2.jpg' height='14px'></A>";
			}else {
				showCategory = "";
				showSchool = "";
				showColor = "#ffffff";
				showEdit = "";
			}
			returnStr+=
			"<div class='tr' style='font-size:small;background-color:"+showColor+"'>"+
					"<div class='td2' style='padding:3px;width:100px; border:1px #f5f5f5 solid;border-top:0px;border-right:0px;text-align:center'>"+LGrade.get(i).getStatus_name()+"</div>"+
					"<div class='td2' style='width:100px;border:1px #f5f5f5 solid;border-top:0px;border-right:0px;text-align:center'>"+showSchool+"</div>"+
					"<div class='td2' style='width:150px;border:1px #f5f5f5 solid;border-top:0px;border-right:0px;text-align:center'>"+showCategory+"&nbsp;&nbsp;"+showEdit+"</div>"+
					"<div class='td2' style='width:150px;border:1px #f5f5f5 solid;border-top:0px;border-right:0px;text-align:center'>"+LGrade.get(i).getTeacher_name()+"</div>"+					
					"<div class='td2' style='width:300px;border:1px #f5f5f5 solid;border-top:0px;border-right:0px;'>&nbsp;"+LGrade.get(i).getClass_start_date()+" <b>"+LGrade.get(i).getSubject_name()+" "+(LGrade.get(i).getGradeName()==null?"":LGrade.get(i).getGradeName())+"</b></div>"+
					"<div class='td2' style='width:100px;border:1px #f5f5f5 solid;border-top:0px;border-right:0px;text-align:center'>"+LGrade.get(i).getLastClassDate()+"</div>"+
					"<div class='td2' style='width:100px;border:1px #f5f5f5 solid;border-top:0px;text-align:center'><A href='javascript:void(0)' style='text-decoration:underline' onclick=statusEdit('"+teacher_id+"','"+school_code+"','"+LGrade.get(i).getCategory_id()+"','"+LGrade.get(i).getGrade_seq()+"','"+LGrade.get(i).getCategory_name()+"&nbsp;/&nbsp;"+LGrade.get(i).getSubject_name()+"&nbsp;"+(LGrade.get(i).getGradeName()==null?"":LGrade.get(i).getGradeName())+"')><img src='/images/gear.jpg' height='12px'></A></div>"+						
		    "</div>";					
		}
		return returnStr;
	}
	

	@RequestMapping("/Course/statusEdit")
	public String statusEdit(Model model,HttpServletRequest request) {
		String teacher_id = request.getParameter("teacher_id");
		model.addAttribute("teacher_id",teacher_id);			
		String school_code = request.getParameter("school_code");
		model.addAttribute("school_code",school_code);		
		String category_id = request.getParameter("category_id");
		model.addAttribute("category_id",category_id);
		String grade_id = request.getParameter("grade_id");
		model.addAttribute("grade_id",grade_id);
		String name = request.getParameter("name");
		model.addAttribute("name",name);
		return "/Course/statusEdit";
	} 
	

	@RequestMapping("/Course/statusEditSave")
	public String statusEditSave(Model model,HttpServletRequest request) {
		String teacher_id  = request.getParameter("teacher_id");
		String school_code = request.getParameter("school_code");
		String category_id = request.getParameter("category_id");
		String grade_id    = request.getParameter("grade_id");
		String FlowStatus  = request.getParameter("FlowStatus");
		courseService.UpdateGradeStatus2(teacher_id,school_code,category_id,grade_id,FlowStatus);
		return "/common/closeAndReload"; 
	}
	
    @RequestMapping("/Course/getProgram")
    @ResponseBody
    public String getProgram(Model model,HttpServletRequest request,HttpSession session) {
		String school_code = request.getParameter("school_code");
		String beginYear = request.getParameter("beginYear");
		String beginMonth = request.getParameter("beginMonth");
		if(beginMonth.length()==1) {beginMonth = "0"+beginMonth;}
		String endYear = request.getParameter("endYear");
		String endMonth = request.getParameter("endMonth");			
		if(endMonth.length()==1) {endMonth = "0"+endMonth;}
		
	    Calendar dateFrom = Calendar.getInstance();
	    dateFrom.set(Calendar.YEAR,Integer.valueOf(beginYear));
	    dateFrom.set(Calendar.MONTH,Integer.valueOf(beginMonth)-1);
	    dateFrom.set(Calendar.DAY_OF_MONTH,0);
	    
	    Calendar dateTo = Calendar.getInstance();
	    dateTo.set(Calendar.YEAR,Integer.valueOf(endYear));
	    dateTo.set(Calendar.MONTH,Integer.valueOf(endMonth));//2個月內
	    dateTo.set(Calendar.DAY_OF_MONTH,0);
	    //課程
	    List<Classes> Lclasses = courseService.getClasses("","","",school_code,"","","","",beginMonth+"/"+"__/"+beginYear,endMonth+"/"+"__/"+endYear);    

	    //教室數量
	    List<classRoom> LclassRoom = systemService.getclassRoom(school_code,"","school_code,name","","");

	    List<classRoom> no_video = new ArrayList<classRoom>();
	    List<classRoom> with_video = new ArrayList<classRoom>();
	    List<classRoom> videoRoom = new ArrayList<classRoom>();
	    for(int i=0;i<LclassRoom.size();i++) {
	    	if(LclassRoom.get(i).getIsVideo().equals("0")) {
	    		with_video.add(LclassRoom.get(i));
	    	}else if(LclassRoom.get(i).getIsVideo().equals("1")) {	    		
	    		no_video.add(LclassRoom.get(i));
	    	}else if(LclassRoom.get(i).getIsVideo().equals("2")) {
	    		videoRoom.add(LclassRoom.get(i));
	    	}
	    }
	    
	    String title_width = "width:"+String.valueOf(75+(LclassRoom.size())*3*100)+"px";
	    
	    //今日顏色    		
		String nowColor="";
		String hotColor=""; //六日
		String now = new SimpleDateFormat("MM/dd/yyyy").format(new Date());	    
	    String titleStr = 
	    "<div class='tr' style='height:5px'><div class='td2' style='background-color:#7ba6ba'></div><div class='td2' style='background-color:#7ba6ba'></div><div class='td2' style='background-color:#7ba6ba'></div><div class='td2' style='background-color:#7ba6ba'></div><div class='td2' style='background-color:#7ba6ba'></div></div>"+		
	    "<div class='tr' style='font-size:small;font-weight:bold'>"+
			"<div class='td2' style='background-color:#FDFEF2;border:1px #cccccc solid;vertical-align:middle;width:75px;height:28px;text-align:center'>上課時段</div>"+ 
			"<div class='td2' style='background-color:#FFFFCC;border-bottom:1px #cccccc solid;vertical-align:middle'>"+
			 	"<div class='css-table'>"+
			 		"<div class='tr'><div class='td2'>早</div></div>"+
			 		"<div class='tr'>"+
			 		    "<div class='td2'>"+
			 		    	"<div class='css-table'>"+
			 		    		"<div class='tr'>";
	    						    for(int x=0;x<with_video.size();x++) {
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #cccccc solid;border-top:1px #cccccc solid'>"+with_video.get(x).getName()+"</div>";
	    						    }
	    						    for(int y=0;y<no_video.size();y++) {
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #cccccc solid;border-top:1px #cccccc solid'>"+no_video.get(y).getName()+"</div>";
	    						    }
	    						    for(int z=0;z<videoRoom.size();z++) {
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #cccccc solid;border-top:1px #cccccc solid'>"+videoRoom.get(z).getName()+"</div>";
	    						    }	    						    
	    						titleStr +=
			 		    		"</div>"+
			 		        "</div>"+
			 			"</div>"+
	    			"</div>"+
			 	"</div>"+
			"</div>"+	
			"<div class='td2' style='background-color:#FFDAFC;border-bottom:1px #cccccc solid;vertical-align:middle'>"+
			 	"<div class='css-table'>"+
			 		"<div class='tr'><div class='td2'>午</div></div>"+
			 		"<div class='tr'>"+
			 		    "<div class='td2'>"+
			 		    	"<div class='css-table'>"+
			 		    		"<div class='tr'>";
	    						    for(int x=0;x<with_video.size();x++) {
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #cccccc solid;border-top:1px #cccccc solid'>"+with_video.get(x).getName()+"</div>";
	    						    }
	    						    for(int y=0;y<no_video.size();y++) {
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #cccccc solid;border-top:1px #cccccc solid'>"+no_video.get(y).getName()+"</div>";
	    						    }
	    						    for(int z=0;z<videoRoom.size();z++) {
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #cccccc solid;border-top:1px #cccccc solid'>"+videoRoom.get(z).getName()+"</div>";
	    						    }	    						    
	    						titleStr +=
			 		    		"</div>"+
			 		        "</div>"+
			 			"</div>"+
	    			"</div>"+
			 	"</div>"+
			"</div>"+ 
			"<div class='td2' style='background-color:#9BFFCF;border-bottom:1px #cccccc solid;vertical-align:middle'>"+
			 	"<div class='css-table'>"+
			 		"<div class='tr'><div class='td2'>晚</div></div>"+
			 		"<div class='tr'>"+
			 		    "<div class='td2'>"+
			 		    	"<div class='css-table'>"+
			 		    		"<div class='tr'>";
    						    for(int x=0;x<with_video.size();x++) {
    						    	titleStr +=
    						    	"<div class='td2' style='width:100px;border-right:1px #cccccc solid;border-top:1px #cccccc solid'>"+with_video.get(x).getName()+"</div>";
    						    }
    						    for(int y=0;y<no_video.size();y++) {
    						    	titleStr +=
    						    	"<div class='td2' style='width:100px;border-right:1px #cccccc solid;border-top:1px #cccccc solid'>"+no_video.get(y).getName()+"</div>";
    						    }
    						    for(int z=0;z<videoRoom.size();z++) {
    						    	titleStr +=
    						    	"<div class='td2' style='width:100px;border-right:1px #cccccc solid;border-top:1px #cccccc solid'>"+videoRoom.get(z).getName()+"</div>";
    						    }
	    						titleStr +=
			 		    		"</div>"+
			 		        "</div>"+
			 			"</div>"+
	    			"</div>"+
			 	"</div>"+
			"</div>"+			 	
	    "</div>";
	    
		String Str ="<div class='css-table' style='letter-spacing:2px;text-align:center;"+title_width+"'>";	

		String week_name = "";
		int week_index = -1;

		List<Suspension> LSuspension = admService.getSuspension("");
		List<ClassRoomBook> LClassRoomBook = admService.getClassRoomBook(school_code);
		
		while (dateFrom.before(dateTo)) {
			dateFrom.add(Calendar.DATE,1);
			if(dateFrom.get(Calendar.DAY_OF_MONTH)==1) {
				Str += titleStr;
			}
			
			//早午晚課程
			List<Classes> Lclasses_slot1 = new ArrayList<Classes>();
			List<Classes> Lclasses_slot2 = new ArrayList<Classes>();
			List<Classes> Lclasses_slot3 = new ArrayList<Classes>();

			for(int x=0;x<Lclasses.size();x++) {
				if(Lclasses.get(x).getClass_date().equals(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime()))) {
					if(Lclasses.get(x).getSlot_id().equals("1")) {
						Lclasses_slot1.add(Lclasses.get(x));
					}else if(Lclasses.get(x).getSlot_id().equals("2")) {
						Lclasses_slot2.add(Lclasses.get(x));
					}else if(Lclasses.get(x).getSlot_id().equals("3")) {
						Lclasses_slot3.add(Lclasses.get(x));
					}
				}
			}	
			hotColor = "";
			//今日顏色
			if(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime()).equals(now)) {
				nowColor="background-color:#FFFF66;";
			}else {
				nowColor="background-color:#FDFEF2;";
			}			
			
		    //{"0星期日","1星期一","2星期二","3星期三",4"星期四","5星期五","6星期六"};
			week_index = dateFrom.get(Calendar.DAY_OF_WEEK) - 1;
			
		      switch(week_index){
		         case 0 :
		        	week_name = "日";
		        	hotColor="background-color:#FFD9FC;";
		            break;
		         case 1 :
		        	week_name = "一";
		            break;		            
		         case 2 :
		        	week_name = "二";
		            break;
		         case 3 :
		        	week_name = "三";
		            break;
		         case 4 :
		        	week_name = "四";
		            break;		            
		         case 5 :
		        	week_name = "五";
		            break;
		         case 6 :
		        	week_name = "六";
		        	hotColor="background-color:#FFD9FC;";
		            break;
		      }
		      
		    //假日
		    String reason =""; 
		    for(int x=0;x<LSuspension.size();x++) {
		    	if(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime()).equals(LSuspension.get(x).getSuspension_date())) {
		    		reason = "<div style='background-color:red;color:white;font-size:x-small'>"+LSuspension.get(x).getReason()+"</div>";
		    	}else {
		    		reason = "";
		    	}
		    }
		    
			Str +=
			"<div class='tr' style='text-align:center;font-size:small'>"+
		    	"<div class='td2' style='"+nowColor+" border-bottom:1px #cccccc solid;border-right:1px #dddddd solid;vertical-align:middle'>"+
		    	    reason+
		    	    "<div class='css-table'>"+
			    	    "<div class='tr'>"+ 
			    	    	"<div class='td2' style='width:50px;text-align:center;vertical-align:middle'>"+new SimpleDateFormat("MM/dd").format(dateFrom.getTime())+"</div>"+ 
			    	    	"<div class='td2' style='"+hotColor+" width:25px;text-align:center;vertical-align:middle'>"+week_name+"</div>"+ 
			    	    "</div>"+
			    	"</div>"+
			    "</div>"+
			    
			    //*****早*****//
			    "<div class='td2' style='border-bottom:1px #dddddd solid'>"+
			        "<div class='css-table'>"+
			           "<div class='tr'>";
			               for(int i=0;i<with_video.size();i++) {  //有錄影教室
			            	   //Str +="<div class='td2' style='font-size:xx-small;background-color:#ffffff;height:48px;width:100px;border-right:1px #dddddd solid'>";
			            	   Str +="<div class='td2' onmouseout=\"this.style.background ='#ffffff';\" onMouseOver=\"this.style.cursor='pointer';this.style.background ='#ffefff';\" onclick=\"cellXY('"+new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())+"','"+with_video.get(i).getName()+"','"+school_code+"','1')\" style='font-size:xx-small;background-color:#ffffff;height:50px;width:100px;border-right:1px #dddddd solid'>";
		            		   //手動預訂
			            	   for(int x=0;x<LClassRoomBook.size();x++) {
		            			   if(LClassRoomBook.get(x).getBookDate().equals(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())) && LClassRoomBook.get(x).getBookClassRoom().equals(with_video.get(i).getName()) && LClassRoomBook.get(x).getSlot().equals("1")){
		            				   Str +=
		            				   "<div style='font-size:x-small;background-color:darkorange;color:white'>"+LClassRoomBook.get(x).getBookContent()+"</div>";
		            			   }
		            		   }
			            	   for(int j=0;j<Lclasses_slot1.size();j++) {  //早課程			            		   
			            		   if(with_video.get(i).getName().equals(Lclasses_slot1.get(j).getClassRoom())) {
				            		      Str +=		  
		            	                  "<div>"+Lclasses_slot1.get(j).getClass_start_date()+Lclasses_slot1.get(j).getTeacher_name()+"</div>"+
		            	                  "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot1.get(j).getGrade_id()+","+Lclasses_slot1.get(j).getClass_th()+")'>"+Lclasses_slot1.get(j).getSubject_name()+"</A></div>"+
		            	                  "<div>"+Lclasses_slot1.get(j).getTime_from()+"~"+Lclasses_slot1.get(j).getTime_to()+"</div>";
				            	   }
			            	   }	   
				               Str +="</div>";
			               }
			            	   
			               for(int i=0;i<no_video.size();i++) { //無錄影教室
			            	   //Str +="<div class='td2' style='font-size:xx-small;background-color:#eeeeee;height:48px;width:100px;border-right:1px #dddddd solid'>";
			            	   Str +="<div class='td2' onmouseout=\"this.style.background ='#eeeeee';\" onMouseOver=\"this.style.cursor='pointer';this.style.background ='#ffefff';\" onclick=\"cellXY('"+new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())+"','"+no_video.get(i).getName()+"','"+school_code+"','1')\" style='font-size:xx-small;background-color:#eeeeee;height:50px;width:100px;border-right:1px #dddddd solid'>";
		            		   //手動預訂
			            	   for(int x=0;x<LClassRoomBook.size();x++) {
		            			   if(LClassRoomBook.get(x).getBookDate().equals(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())) && LClassRoomBook.get(x).getBookClassRoom().equals(no_video.get(i).getName()) && LClassRoomBook.get(x).getSlot().equals("1")){
		            				   Str +=
		            				   "<div style='font-size:x-small;background-color:darkorange;color:white'>"+LClassRoomBook.get(x).getBookContent()+"</div>";
		            			   }
		            		   }
			            	   
			            	   for(int j=0;j<Lclasses_slot1.size();j++) {  //早課程			            		   
			            		   if(no_video.get(i).getName().equals(Lclasses_slot1.get(j).getClassRoom())) {
				            		      Str +=
		            	                  "<div>"+Lclasses_slot1.get(j).getClass_start_date()+Lclasses_slot1.get(j).getTeacher_name()+"</div>"+
				            	          "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot1.get(j).getGrade_id()+","+Lclasses_slot1.get(j).getClass_th()+")'>"+Lclasses_slot1.get(j).getSubject_name()+"</A></div>"+
				            	          "<div>"+Lclasses_slot1.get(j).getTime_from()+"~"+Lclasses_slot1.get(j).getTime_to()+"</div>";
				            	   }
			            	   }	   
				               Str +="</div>";
			               }
			               
			               for(int i=0;i<videoRoom.size();i++) { //Video教室
			            	   //Str +="<div class='td2' style='font-size:xx-small;background-color:#ffefff;height:48px;width:100px;border-right:1px #dddddd solid'>";
			            	   Str +="<div class='td2' onmouseout=\"this.style.background ='#DFE0FF';\" onMouseOver=\"this.style.cursor='pointer';this.style.background ='#ffefff';\" onclick=\"cellXY('"+new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())+"','"+videoRoom.get(i).getName()+"','"+school_code+"','1')\" style='font-size:xx-small;background-color:#DFE0FF;height:50px;width:100px;border-right:1px #dddddd solid'>";
		            		   //手動預訂
			            	   for(int x=0;x<LClassRoomBook.size();x++) {
		            			   if(LClassRoomBook.get(x).getBookDate().equals(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())) && LClassRoomBook.get(x).getBookClassRoom().equals(videoRoom.get(i).getName()) && LClassRoomBook.get(x).getSlot().equals("1")){
		            				   Str +=
		            				   "<div style='font-size:x-small;background-color:darkorange;color:white'>"+LClassRoomBook.get(x).getBookContent()+"</div>";
		            			   }
		            		   }
			            	   for(int j=0;j<Lclasses_slot1.size();j++) {  //早課程			            		   
			            		   if(videoRoom.get(i).getName().equals(Lclasses_slot1.get(j).getClassRoom())) {
				            		      Str +=
		            	                  "<div>"+Lclasses_slot1.get(j).getClass_start_date()+Lclasses_slot1.get(j).getTeacher_name()+"</div>"+
						            	  "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot1.get(j).getGrade_id()+","+Lclasses_slot1.get(j).getClass_th()+")'>"+Lclasses_slot1.get(j).getSubject_name()+"</A></div>"+
						            	  "<div>"+Lclasses_slot1.get(j).getTime_from()+"~"+Lclasses_slot1.get(j).getTime_to()+"</div>";
				            	   }
			            	   }	   
				               Str +="</div>";
			               }			               			               
			    Str +="</div></div></div>";		    

			    //*****午*****//
			    Str +=
			    "<div class='td2' style='border-bottom:1px #dddddd solid'>"+
			        "<div class='css-table'>"+
			           "<div class='tr'>";
			               for(int i=0;i<with_video.size();i++) {  //有錄影教室
			            	   //Str +="<div class='td2' style='font-size:xx-small;background-color:#ffffff;height:48px;width:100px;border-right:1px #dddddd solid'>";
			            	   Str +="<div class='td2' onmouseout=\"this.style.background ='#ffffff';\" onMouseOver=\"this.style.cursor='pointer';this.style.background ='#ffefff';\" onclick=\"cellXY('"+new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())+"','"+with_video.get(i).getName()+"','"+school_code+"','2')\" style='font-size:xx-small;background-color:#ffffff;height:50px;width:100px;border-right:1px #dddddd solid'>";
		            		   //手動預訂
			            	   for(int x=0;x<LClassRoomBook.size();x++) {
		            			   if(LClassRoomBook.get(x).getBookDate().equals(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())) && LClassRoomBook.get(x).getBookClassRoom().equals(with_video.get(i).getName()) && LClassRoomBook.get(x).getSlot().equals("2")){
		            				   Str +=
		            				   "<div style='font-size:x-small;background-color:darkorange;color:white'>"+LClassRoomBook.get(x).getBookContent()+"</div>";
		            			   }
		            		   }
			            	   for(int j=0;j<Lclasses_slot2.size();j++) {  //午課程			            		   
			            		   if(with_video.get(i).getName().equals(Lclasses_slot2.get(j).getClassRoom())) {
				            		      Str +=
		            	                  "<div>"+with_video.get(i).getName()+Lclasses_slot2.get(j).getClass_start_date()+Lclasses_slot2.get(j).getTeacher_name()+"</div>"+
						            	  "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot2.get(j).getGrade_id()+","+Lclasses_slot2.get(j).getClass_th()+")'>"+Lclasses_slot2.get(j).getSubject_name()+"</A></div>"+
						            	  "<div>"+Lclasses_slot2.get(j).getTime_from()+"~"+Lclasses_slot2.get(j).getTime_to()+"</div>";
				            	   }
			            	   }	   
				               Str +="</div>";
			               }
			            	   
			               for(int i=0;i<no_video.size();i++) { //無錄影教室
			            	   //Str +="<div class='td2' style='font-size:xx-small;background-color:#eeeeee;height:48px;width:100px;border-right:1px #dddddd solid'>";
			            	   Str +="<div class='td2' onmouseout=\"this.style.background ='#eeeeee';\" onMouseOver=\"this.style.cursor='pointer';this.style.background ='#ffefff';\" onclick=\"cellXY('"+new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())+"','"+no_video.get(i).getName()+"','"+school_code+"','2')\" style='font-size:xx-small;background-color:#eeeeee;height:50px;width:100px;border-right:1px #dddddd solid'>";
		            		   //手動預訂
			            	   for(int x=0;x<LClassRoomBook.size();x++) {
		            			   if(LClassRoomBook.get(x).getBookDate().equals(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())) && LClassRoomBook.get(x).getBookClassRoom().equals(no_video.get(i).getName()) && LClassRoomBook.get(x).getSlot().equals("2")){
		            				   Str +=
		            				   "<div style='font-size:x-small;background-color:darkorange;color:white'>"+LClassRoomBook.get(x).getBookContent()+"</div>";
		            			   }
		            		   }
			            	   for(int j=0;j<Lclasses_slot2.size();j++) {  //午課程			            		   
			            		   if(no_video.get(i).getName().equals(Lclasses_slot2.get(j).getClassRoom())) {
				            		      Str +=
		            	                  "<div>"+no_video.get(i).getName()+Lclasses_slot2.get(j).getClass_start_date()+Lclasses_slot2.get(j).getTeacher_name()+"</div>"+
						            	  "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot2.get(j).getGrade_id()+","+Lclasses_slot2.get(j).getClass_th()+")'>"+Lclasses_slot2.get(j).getSubject_name()+"</A></div>"+
						            	  "<div>"+Lclasses_slot2.get(j).getTime_from()+"~"+Lclasses_slot2.get(j).getTime_to()+"</div>";
				            	   }
			            	   }	   
				               Str +="</div>";
			               }
			               
			               for(int i=0;i<videoRoom.size();i++) { //Video教室
			            	   //Str +="<div class='td2' style='font-size:xx-small;background-color:#ffefff;height:48px;width:100px;border-right:1px #dddddd solid'>";
			            	   Str +="<div class='td2' onmouseout=\"this.style.background ='#DFE0FF';\" onMouseOver=\"this.style.cursor='pointer';this.style.background ='#ffefff';\" onclick=\"cellXY('"+new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())+"','"+videoRoom.get(i).getName()+"','"+school_code+"','2')\" style='font-size:xx-small;background-color:#DFE0FF;height:50px;width:100px;border-right:1px #dddddd solid'>";
		            		   //手動預訂
			            	   for(int x=0;x<LClassRoomBook.size();x++) {
		            			   if(LClassRoomBook.get(x).getBookDate().equals(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())) && LClassRoomBook.get(x).getBookClassRoom().equals(videoRoom.get(i).getName()) && LClassRoomBook.get(x).getSlot().equals("2")){
		            				   Str +=
		            				   "<div style='font-size:x-small;background-color:darkorange;color:white'>"+LClassRoomBook.get(x).getBookContent()+"</div>";
		            			   }
		            		   }
			            	   for(int j=0;j<Lclasses_slot2.size();j++) {  //午課程			            		   
			            		   if(videoRoom.get(i).getName().equals(Lclasses_slot2.get(j).getClassRoom())) {
				            		      Str +=
		            	                  "<div>"+videoRoom.get(i).getName()+Lclasses_slot2.get(j).getClass_start_date()+Lclasses_slot2.get(j).getTeacher_name()+"</div>"+
						            	  "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot2.get(j).getGrade_id()+","+Lclasses_slot2.get(j).getClass_th()+")'>"+Lclasses_slot2.get(j).getSubject_name()+"</A></div>"+
						            	  "<div>"+Lclasses_slot2.get(j).getTime_from()+"~"+Lclasses_slot2.get(j).getTime_to()+"</div>";
				            	   }
			            	   }	   
				               Str +="</div>";
			               }			               			               
			    Str +="</div></div></div>";			    
			    
			    //*****晚*****//
			    Str +=
			    "<div class='td2' style='border-bottom:1px #dddddd solid'>"+
			        "<div class='css-table'>"+
			           "<div class='tr'>";
			               for(int i=0;i<with_video.size();i++) {  //有錄影教室
			            	   //Str +="<div class='td2' style='font-size:xx-small;background-color:#ffffff;height:48px;width:100px;border-right:1px #dddddd solid'>";
			            	   Str +="<div class='td2' onmouseout=\"this.style.background ='#ffffff';\" onMouseOver=\"this.style.cursor='pointer';this.style.background ='#ffefff';\" onclick=\"cellXY('"+new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())+"','"+with_video.get(i).getName()+"','"+school_code+"','3')\" style='font-size:xx-small;background-color:#ffffff;height:50px;width:100px;border-right:1px #dddddd solid'>";
		            		   //手動預訂
			            	   for(int x=0;x<LClassRoomBook.size();x++) {
		            			   if(LClassRoomBook.get(x).getBookDate().equals(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())) && LClassRoomBook.get(x).getBookClassRoom().equals(with_video.get(i).getName()) && LClassRoomBook.get(x).getSlot().equals("3")){
		            				   Str +=
		            				   "<div style='font-size:x-small;background-color:darkorange;color:white'>"+LClassRoomBook.get(x).getBookContent()+"</div>";
		            			   }
		            		   }
			            	   for(int j=0;j<Lclasses_slot3.size();j++) {  //晚課程			            		   
			            		   if(with_video.get(i).getName().equals(Lclasses_slot3.get(j).getClassRoom())) {
				            		      Str +=
		            	                  "<div>"+Lclasses_slot3.get(j).getClass_start_date()+Lclasses_slot3.get(j).getTeacher_name()+"</div>"+
						            	  "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot3.get(j).getGrade_id()+","+Lclasses_slot3.get(j).getClass_th()+")'>"+Lclasses_slot3.get(j).getSubject_name()+"</A></div>"+
						            	  "<div>"+Lclasses_slot3.get(j).getTime_from()+"~"+Lclasses_slot3.get(j).getTime_to()+"</div>";
				            	   }
			            	   }	   
				               Str +="</div>";
			               }
			            	   
			               for(int i=0;i<no_video.size();i++) { //無錄影教室
			            	   //Str +="<div class='td2' style='font-size:xx-small;background-color:#eeeeee;height:48px;width:100px;border-right:1px #dddddd solid'>";
			            	   Str +="<div class='td2' onmouseout=\"this.style.background ='#eeeeee';\" onMouseOver=\"this.style.cursor='pointer';this.style.background ='#ffefff';\" onclick=\"cellXY('"+new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())+"','"+no_video.get(i).getName()+"','"+school_code+"','3')\" style='font-size:xx-small;background-color:#eeeeee;height:50px;width:100px;border-right:1px #dddddd solid'>";
		            		   //手動預訂
			            	   for(int x=0;x<LClassRoomBook.size();x++) {
		            			   if(LClassRoomBook.get(x).getBookDate().equals(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())) && LClassRoomBook.get(x).getBookClassRoom().equals(no_video.get(i).getName()) && LClassRoomBook.get(x).getSlot().equals("3")){
		            				   Str +=
		            				   "<div style='font-size:x-small;background-color:darkorange;color:white'>"+LClassRoomBook.get(x).getBookContent()+"</div>";
		            			   }
		            		   }
			            	   for(int j=0;j<Lclasses_slot3.size();j++) {  //晚課程			            		   
			            		   if(no_video.get(i).getName().equals(Lclasses_slot3.get(j).getClassRoom())) {
				            		      Str +=
		            	                  "<div>"+Lclasses_slot3.get(j).getClass_start_date()+Lclasses_slot3.get(j).getTeacher_name()+"</div>"+
						            	  "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot3.get(j).getGrade_id()+","+Lclasses_slot3.get(j).getClass_th()+")'>"+Lclasses_slot3.get(j).getSubject_name()+"</A></div>"+
						            	  "<div>"+Lclasses_slot3.get(j).getTime_from()+"~"+Lclasses_slot3.get(j).getTime_to()+"</div>";
				            	   }
			            	   }	   
				               Str +="</div>";
			               }
			               
			               for(int i=0;i<videoRoom.size();i++) { //Video教室
			            	   //Str +="<div class='td2' style='font-size:xx-small;background-color:#ffefff;height:48px;width:100px;border-right:1px #dddddd solid'>";
			            	   Str +="<div class='td2' onmouseout=\"this.style.background ='#DFE0FF';\" onMouseOver=\"this.style.cursor='pointer';this.style.background ='#ffefff';\" onclick=\"cellXY('"+new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())+"','"+videoRoom.get(i).getName()+"','"+school_code+"','3')\" style='font-size:xx-small;background-color:#DFE0FF;height:50px;width:100px;border-right:1px #dddddd solid'>";
		            		   //手動預訂
			            	   for(int x=0;x<LClassRoomBook.size();x++) {
		            			   if(LClassRoomBook.get(x).getBookDate().equals(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime())) && LClassRoomBook.get(x).getBookClassRoom().equals(videoRoom.get(i).getName()) && LClassRoomBook.get(x).getSlot().equals("3")){
		            				   Str +=
		            				   "<div style='font-size:x-small;background-color:darkorange;color:white'>"+LClassRoomBook.get(x).getBookContent()+"</div>";
		            			   }
		            		   }
			            	   for(int j=0;j<Lclasses_slot3.size();j++) {  //晚課程			            		   
			            		   if(videoRoom.get(i).getName().equals(Lclasses_slot3.get(j).getClassRoom())) {
				            		      Str +=
		            	                  "<div>"+Lclasses_slot3.get(j).getClass_start_date()+Lclasses_slot3.get(j).getTeacher_name()+"</div>"+
						            	  "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot3.get(j).getGrade_id()+","+Lclasses_slot3.get(j).getClass_th()+")'>"+Lclasses_slot3.get(j).getSubject_name()+"</A></div>"+
						            	  "<div>"+Lclasses_slot3.get(j).getTime_from()+"~"+Lclasses_slot3.get(j).getTime_to()+"</div>";
				            	   }
			            	   }	   
				               Str +="</div>";
			               }			               			               
			    Str +="</div></div></div>";			    
			    
			    
			Str +="</div>";		    
		}
		Str += "</div><div>&nbsp;</div>";    
   	
        return Str;
    }	
    
    @RequestMapping("/Course/getProgramWait")
    @ResponseBody
    public String getProgramWait(Model model,HttpServletRequest request,HttpSession session) {
		String school_code = request.getParameter("school_code");
		String beginYear = request.getParameter("beginYear");
		String beginMonth = request.getParameter("beginMonth");
		if(beginMonth.length()==1) {beginMonth = "0"+beginMonth;}
		String endYear = request.getParameter("endYear");
		String endMonth = request.getParameter("endMonth");			
		if(endMonth.length()==1) {endMonth = "0"+endMonth;}
		
	    Calendar dateFrom = Calendar.getInstance();
	    dateFrom.set(Calendar.YEAR,Integer.valueOf(beginYear));
	    dateFrom.set(Calendar.MONTH,Integer.valueOf(beginMonth)-1);
	    dateFrom.set(Calendar.DAY_OF_MONTH,0);
	    
	    Calendar dateTo = Calendar.getInstance();
	    dateTo.set(Calendar.YEAR,Integer.valueOf(endYear));
	    dateTo.set(Calendar.MONTH,Integer.valueOf(endMonth));//2個月內
	    dateTo.set(Calendar.DAY_OF_MONTH,0);
	    //課程
	    List<Classes> Lclasses = courseService.getClasses("","","",school_code,"","(4,5)","","",beginMonth+"/"+"__/"+beginYear,endMonth+"/"+"__/"+endYear);    

	    //教室數量
	    List<classRoom> LclassRoom = systemService.getclassRoom(school_code,"","school_code,isVideo","","");

	    List<classRoom> no_video = new ArrayList<classRoom>();
	    List<classRoom> with_video = new ArrayList<classRoom>();
	    List<classRoom> videoRoom = new ArrayList<classRoom>();
	    for(int i=0;i<LclassRoom.size();i++) {
	    	if(LclassRoom.get(i).getIsVideo().equals("0")) {
	    		with_video.add(LclassRoom.get(i));	    		
	    	}else if(LclassRoom.get(i).getIsVideo().equals("1")) {
	    		no_video.add(LclassRoom.get(i));
	    	}else if(LclassRoom.get(i).getIsVideo().equals("2")) {
	    		videoRoom.add(LclassRoom.get(i));
	    	}
	    }
	    
	    String totalWidth = "width:"+String.valueOf(LclassRoom.size()*100)+"px";
	    
	    
	    //今日顏色    		
		String nowColor="";
		String hotColor=""; //六日
		String now = new SimpleDateFormat("MM/dd/yyyy").format(new Date());	
		int totalClassNo = 0;
	    String titleStr =
	    "<div class='tr' style='height:5px'><div class='td2' style='background-color:#7ba6ba'></div><div class='td2' style='background-color:#7ba6ba'></div><div class='td2' style='background-color:#7ba6ba'></div><div class='td2' style='background-color:#7ba6ba'></div><div class='td2' style='background-color:#7ba6ba'></div><div class='td2' style='background-color:#7ba6ba'></div><div class='td2' style='background-color:#7ba6ba'></div></div>"+		 		
	    "<div class='tr' style='font-size:small;font-weight:bold'>"+
			"<div class='td2' style='background-color:#FDFEF2;border-bottom:1px #dddddd solid;vertical-align:middle;width:75px;height:28px;text-align:center'>上課時段</div>"+ 
			"<div class='td2' style='background-color:#FFFFCC;border-bottom:1px #dddddd solid;vertical-align:middle'>"+
			 	"<div class='css-table' style='"+totalWidth+"'>"+
			 		"<div class='tr'><div class='td2' style='text-align:center'>早</div></div>"+
			 		"<div class='tr'>"+			 		
			 		    "<div class='td2'>"+
			 		    	"<div class='css-table'>"+
			 		    		"<div class='tr'>";
	    						    for(int x=0;x<with_video.size();x++) {
	    						    	totalClassNo ++;
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'></div>";	    						    			
	    						    	//"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'>No."+totalClassNo+"</div>";	    						    	
	    						    }
	    						    for(int y=0;y<no_video.size();y++) {
	    						    	totalClassNo ++;
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'></div>";		
	    						    	//"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'>No."+totalClassNo+"</div>";
	    						    }
	    						    for(int z=0;z<videoRoom.size();z++) {
	    						    	totalClassNo ++;
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'></div>";			
	    						    	//"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'>No."+totalClassNo+"</div>";
	    						    }	    						    
	    						titleStr +=
			 		    		"</div>"+
			 		        "</div>"+
			 			"</div>"+
    			    "</div>"+			 	
			 	"</div>"+
			"</div>"+
			"<div class='td2' style='background-color:#FDFEF2;border-bottom:1px #dddddd solid;vertical-align:middle;width:75px;height:28px;text-align:center'>上課時段</div>"+  	
			"<div class='td2' style='background-color:#FFDAFC;border-bottom:1px #dddddd solid;vertical-align:middle'>"+
			 	"<div class='css-table'>"+
			 		"<div class='tr'><div class='td2' style='text-align:center'>午</div></div>"+
			 		"<div class='tr'>"+
			 		    "<div class='td2'>"+
			 		    	"<div class='css-table' style='"+totalWidth+"'>"+
			 		    		"<div class='tr'>";
	    						    totalClassNo = 0;
	    						    for(int x=0;x<with_video.size();x++) {
	    						    	totalClassNo ++;
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'></div>";	    						  		
	    						    	//"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'>No."+totalClassNo+"</div>";	    						    	
	    						    }
	    						    for(int y=0;y<no_video.size();y++) {
	    						    	totalClassNo ++;
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'></div>"; 			
	    						    	//"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'>No."+totalClassNo+"</div>";
	    						    }
	    						    for(int z=0;z<videoRoom.size();z++) {
	    						    	totalClassNo ++;
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'></div>";			
	    						    	//"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'>No."+totalClassNo+"</div>";
	    						    }	    						    
	    						titleStr +=
			 		    		"</div>"+
			 		        "</div>"+
			 			"</div>"+
			        "</div>"+
			     "</div>"+		
			"</div>"+ 
			"<div class='td2' style='background-color:#FDFEF2;border-bottom:1px #dddddd solid;vertical-align:middle;width:75px;height:28px;text-align:center'>上課時段</div>"+  	     
			"<div class='td2' style='background-color:#9BFFCF;border-bottom:1px #dddddd solid;vertical-align:middle'>"+
			 	"<div class='css-table'>"+
			 		"<div class='tr'><div class='td2' style='text-align:center'>晚</div></div>"+
			 		"<div class='tr'>"+
			 		    "<div class='td2'>"+
			 		    	"<div class='css-table' style='"+totalWidth+"'>"+
			 		    		"<div class='tr'>";
	    						    totalClassNo = 0;
	    						    for(int x=0;x<with_video.size();x++) {
	    						    	totalClassNo ++;
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'></div>";	    						  			
	    						    	//"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'>No."+totalClassNo+"</div>";	    						    	
	    						    }
	    						    for(int y=0;y<no_video.size();y++) {
	    						    	totalClassNo ++;
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'></div>"; 			
	    						    	//"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'>No."+totalClassNo+"</div>";
	    						    }
	    						    for(int z=0;z<videoRoom.size();z++) {
	    						    	totalClassNo ++;
	    						    	titleStr +=
	    						    	"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'></div>"; 			
	    						    	//"<div class='td2' style='width:100px;border-right:1px #dddddd solid;border-top:1px #dddddd solid'>No."+totalClassNo+"</div>";
	    						    }	    						    
	    						titleStr +=
			 		    		"</div>"+
			 		        "</div>"+
			 			"</div>"+
			        "</div>"+			 	
			 	"</div>"+
			"</div>"+			 	
	    "</div>";
	    
		String Str ="<div class='css-table' style='letter-spacing:2px;text-align:center'>";	

		String week_name = "";
		int week_index = -1;

		List<Suspension> LSuspension = admService.getSuspension("");
		
		//此二月的格子內容
		while (dateFrom.before(dateTo)) {
			dateFrom.add(Calendar.DATE,1);
			if(dateFrom.get(Calendar.DAY_OF_MONTH)==1) {
				Str += titleStr;
			}
			
			//早午晚課程
			List<Classes> Lclasses_slot1 = new ArrayList<Classes>();
			List<Classes> Lclasses_slot2 = new ArrayList<Classes>();
			List<Classes> Lclasses_slot3 = new ArrayList<Classes>();

			for(int x=0;x<Lclasses.size();x++) {
				if(Lclasses.get(x).getClass_date().equals(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime()))) {
					if(Lclasses.get(x).getSlot_id().equals("1")) {
						Lclasses_slot1.add(Lclasses.get(x));
					}else if(Lclasses.get(x).getSlot_id().equals("2")) {
						Lclasses_slot2.add(Lclasses.get(x));
					}else if(Lclasses.get(x).getSlot_id().equals("3")) {
						Lclasses_slot3.add(Lclasses.get(x));
					}
				}
			}	
			hotColor = "";
			//今日顏色
			if(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime()).equals(now)) {
				nowColor="background-color:#FFFF66";
			}else {
				nowColor="background-color:#FDFEF2";
			}			
			
		    //{"0星期日","1星期一","2星期二","3星期三",4"星期四","5星期五","6星期六"};
			week_index = dateFrom.get(Calendar.DAY_OF_WEEK) - 1;
			
		      switch(week_index){
		         case 0 :
		        	week_name = "日";
		        	hotColor="background-color:#FFD9FC;";
		            break;
		         case 1 :
		        	week_name = "一";
		            break;		            
		         case 2 :
		        	week_name = "二";
		            break;
		         case 3 :
		        	week_name = "三";
		            break;
		         case 4 :
		        	week_name = "四";
		            break;		            
		         case 5 :
		        	week_name = "五";
		            break;
		         case 6 :
		        	week_name = "六";
		        	hotColor="background-color:#FFD9FC;";
		            break;
		      }
		      
		    //假日
		    String reason =""; 
		    for(int x=0;x<LSuspension.size();x++) {
		    	if(new SimpleDateFormat("MM/dd/yyyy").format(dateFrom.getTime()).equals(LSuspension.get(x).getSuspension_date())) {
		    		reason = "<div style='background-color:red;color:white;font-size:x-small'>"+LSuspension.get(x).getReason()+"</div>";
		    	}else {
		    		reason = "";
		    	}
		    }
		      
			Str +=
			"<div class='tr' style='font-size:small'>"+
		    	"<div class='td2' style='"+nowColor+";border-bottom:1px #dddddd solid;border-right:1px #dddddd solid;vertical-align:middle' align='center'>"+
					reason+
		    	    "<div class='css-table'>"+
			    	    "<div class='tr'>"+ 
			    	    	"<div class='td2' style='width:50px;text-align:center;vertical-align:middle'>"+new SimpleDateFormat("MM/dd").format(dateFrom.getTime())+"</div>"+ 
			    	    	"<div class='td2' style='width:25px;"+hotColor+" text-align:center;vertical-align:middle'>"+week_name+"</div>"+ 
			    	    "</div>"+
			    	"</div>"+
			    "</div>"+
			    	
			    //*****早*****//
			    "<div class='td2' style='border-bottom:1px #dddddd solid'>"+
			        "<div class='css-table'>"+
			           "<div class='tr'>";
			                String classRoomColor = "";
			    			for(int i=0;i<LclassRoom.size();i++) {  //錄影教室
			    				if(LclassRoom.get(i).getIsVideo().equals("1")) {
			    					classRoomColor = "#eeeeee";
			    				}else if(LclassRoom.get(i).getIsVideo().equals("2")) {
			    					classRoomColor = "#DFE0FF";
			    				}else {
			    					classRoomColor = "#ffffff";
			    				}
			    				if((i+1)<=Lclasses_slot1.size()) {
					            		  Str += 
					            		  "<div class='td2' style='vertical-align:bottom;font-size:xx-small;background-color:"+classRoomColor+";height:50px;width:100px;border-right:1px #dddddd solid;letter-spacing:0px'>"+		  
			            	                  "<div>"+Lclasses_slot1.get(i).getClass_start_date()+"<A style='color:brown;text-decoration:underline' href='javascript:void(0)' onclick='coursePreviousAttend("+Lclasses_slot1.get(i).getGrade_id()+")'>"+Lclasses_slot1.get(i).getTeacher_name()+"</A></div>"+
			            	                  "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot1.get(i).getGrade_id()+","+Lclasses_slot1.get(i).getClass_th()+")'>"+Lclasses_slot1.get(i).getSubject_name()+(Lclasses_slot1.get(i).getGradeName()==null?"":Lclasses_slot1.get(i).getGradeName())+"_"+Lclasses_slot1.get(i).getClass_th()+"</A></div>"+
			            	                  "<div>"+Lclasses_slot1.get(i).getTime_from()+"~"+Lclasses_slot1.get(i).getTime_to()+"&nbsp;<b>"+Lclasses_slot1.get(i).getClassRoom()+"</b></div>"+
			            	              "</div>";    	    					
			    				}else {
				    			   Str += "<div class='td2' style='font-size:xx-small;background-color:"+classRoomColor+";height:50px;width:100px;border-right:1px #dddddd solid'>&nbsp;</div>";
			    				}   
			    			}       
			    Str +="</div></div></div>";
			    Str +=
		    	"<div class='td2' style='"+nowColor+";border-bottom:1px #dddddd solid;border-right:1px #dddddd solid;vertical-align:middle' align='center'>"+
					reason+
		    	    "<div class='css-table'>"+
			    	    "<div class='tr'>"+ 
			    	    	"<div class='td2' style='width:50px;text-align:center;vertical-align:middle'>"+new SimpleDateFormat("MM/dd").format(dateFrom.getTime())+"</div>"+ 
			    	    	"<div class='td2' style='width:25px;"+hotColor+" text-align:center;vertical-align:middle'>"+week_name+"</div>"+ 
			    	    "</div>"+
			    	"</div>"+
			    "</div>";			    

			    //*****午*****//
			    Str +=
			    "<div class='td2' style='border-bottom:1px #dddddd solid'>"+
			        "<div class='css-table'>"+
			           "<div class='tr'>";
			    			for(int i=0;i<LclassRoom.size();i++) {  //錄影教室 
			    				if(LclassRoom.get(i).getIsVideo().equals("1")) {
			    					classRoomColor = "#eeeeee";
			    				}else if(LclassRoom.get(i).getIsVideo().equals("2")) {
			    					classRoomColor = "#DFE0FF";
			    				}else {
			    					classRoomColor = "#ffffff";
			    				}			    				
			    				if((i+1)<=Lclasses_slot2.size()) {
					            		  Str += 
					            		  "<div class='td2' style='vertical-align:bottom;font-size:xx-small;background-color:"+classRoomColor+";height:50px;width:100px;border-right:1px #dddddd solid;letter-spacing:0px'>"+		  
			            	                  "<div>"+Lclasses_slot2.get(i).getClass_start_date()+Lclasses_slot2.get(i).getTeacher_name()+"</div>"+
			            	                  "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot2.get(i).getGrade_id()+","+Lclasses_slot2.get(i).getClass_th()+")'>"+Lclasses_slot2.get(i).getSubject_name()+(Lclasses_slot2.get(i).getGradeName()==null?"":Lclasses_slot2.get(i).getGradeName())+"_"+Lclasses_slot2.get(i).getClass_th()+"</A></div>"+
			            	                  "<div>"+Lclasses_slot2.get(i).getTime_from()+"~"+Lclasses_slot2.get(i).getTime_to()+"&nbsp;<b>"+Lclasses_slot2.get(i).getClassRoom()+"</b></div>"+
			            	              "</div>";    	    					
			    				}else {
				    			   Str += "<div class='td2' style='font-size:xx-small;background-color:"+classRoomColor+";height:50px;width:100px;border-right:1px #dddddd solid'>&nbsp;</div>";
			    				}   
			    			}		               			               
			    Str +="</div></div></div>";
			    Str +=
		    	"<div class='td2' style='"+nowColor+";border-bottom:1px #dddddd solid;border-right:1px #dddddd solid;vertical-align:middle' align='center'>"+
					reason+
		    	    "<div class='css-table'>"+
			    	    "<div class='tr'>"+ 
			    	    	"<div class='td2' style='width:50px;text-align:center;vertical-align:middle'>"+new SimpleDateFormat("MM/dd").format(dateFrom.getTime())+"</div>"+ 
			    	    	"<div class='td2' style='width:25px;"+hotColor+" text-align:center;vertical-align:middle'>"+week_name+"</div>"+ 
			    	    "</div>"+
			    	"</div>"+
			    "</div>";			    
			    
			    //*****晚*****//
			    Str +=
			    "<div class='td2' style='border-bottom:1px #dddddd solid'>"+
			        "<div class='css-table'>"+
			           "<div class='tr'>";
			    			for(int i=0;i<LclassRoom.size();i++) {  //錄影教室 
			    				if(LclassRoom.get(i).getIsVideo().equals("1")) {
			    					classRoomColor = "#eeeeee";
			    				}else if(LclassRoom.get(i).getIsVideo().equals("2")) {
			    					classRoomColor = "#DFE0FF";
			    				}else {
			    					classRoomColor = "#ffffff";
			    				}			    				
			    				if((i+1)<=Lclasses_slot3.size()) {
					            		  Str += 
					            		  "<div class='td2' style='vertical-align:bottom;font-size:xx-small;background-color:"+classRoomColor+";height:50px;width:100px;border-right:1px #dddddd solid;letter-spacing:0px'>"+		  
			            	                  "<div>"+Lclasses_slot3.get(i).getClass_start_date()+Lclasses_slot3.get(i).getTeacher_name()+"</div>"+
			            	                  "<div style='font-weight:bold'><A style='color:blue;font-weight:bold;text-decoration:underline' href='javascript:void(0)' onclick='courseEdit("+Lclasses_slot3.get(i).getGrade_id()+","+Lclasses_slot3.get(i).getClass_th()+")'>"+Lclasses_slot3.get(i).getSubject_name()+(Lclasses_slot3.get(i).getGradeName()==null?"":Lclasses_slot3.get(i).getGradeName())+"_"+Lclasses_slot3.get(i).getClass_th()+"</A></div>"+
			            	                  "<div>"+Lclasses_slot3.get(i).getTime_from()+"~"+Lclasses_slot3.get(i).getTime_to()+"&nbsp;<b>"+Lclasses_slot3.get(i).getClassRoom()+"</b></div>"+
			            	              "</div>";    	    					
			    				}else {
				    			   Str += "<div class='td2' style='font-size:xx-small;background-color:"+classRoomColor+";height:50px;width:100px;border-right:1px #dddddd solid'>&nbsp;</div>";
			    				}   
			    			}	               			               
			    Str +="</div></div></div>";			    
			    
			    
			Str +="</div>";		    
		}
		Str += "</div><div>&nbsp;</div>";    
   	
        return Str;
    }	
    
       
	@RequestMapping("/Course/getClassRoom")
	@ResponseBody
	public String getClassRoom(HttpServletRequest request) {
		List<classRoom> LclassRoom = systemService.getclassRoom(request.getParameter("school_code"),"","school_code,name","","");
		String returnStr = "";
		String bg = "";
		for(int i=0;i<LclassRoom.size();i++) {
			if(LclassRoom.get(i).getIsVideo().equals("0")) {
				bg = "background-color:#ffffff";
			}else if(LclassRoom.get(i).getIsVideo().equals("1")) {
				bg = "background-color:#eeeeee";
			}else if(LclassRoom.get(i).getIsVideo().equals("2")) {
				bg = "background-color:#DFE0FF";
			}	
			returnStr += "<span style='border:1px #cccccc solid;"+bg+"'>"+LclassRoom.get(i).getName()+"</span>"+LclassRoom.get(i).getCapacity()+"人"+"&nbsp;&nbsp;";
			if(i>0 && (i+1)%8==0) {returnStr += "<div style='height:2px'></div>";}
		}
		return returnStr;
	}    
	
	@RequestMapping("/Course/getMockVideoSubject")
	@ResponseBody
	public String getMockVideoSubject(HttpServletRequest request) {
		List<Subject> LSubject = courseService.getSubject(request.getParameter("category_id"),"","","1","%模考%","0");
		String returnStr = "";
		for(int i=0;i<LSubject.size();i++) {
			returnStr +="<option value='"+LSubject.get(i).getSubject_seq()+"'>"+LSubject.get(i).getName()+"</option>";
		}
		return returnStr;
	}
	

	@RequestMapping("/Course/Add_JL")
	public String Add_JL(Model model,HttpServletRequest request) {
		model.addAttribute("eip_grade_seq",request.getParameter("grade_seq"));
		model.addAttribute("subject_id",request.getParameter("subject_id"));
		String schoolAbbr = request.getParameter("schoolAbbr");
		if(schoolAbbr.equals("TPXC")) {
			model.addAttribute("schoolAbbr","P");
		}else if(schoolAbbr.equals("TCFX")) {
			model.addAttribute("schoolAbbr","C");
		}else if(schoolAbbr.equals("KSZS")) {
			model.addAttribute("schoolAbbr","K");
		}else if(schoolAbbr.equals("TPSL")) {
			model.addAttribute("schoolAbbr","L");
		}	
		return "/Course/Add_JL";
	} 
	

	@RequestMapping("/Course/Add_JLSave")
	public String Add_JLSave(Model model,HttpServletRequest request) {
		String[] JL_gradeId  = request.getParameterValues("JL_gradeId");
		String[] class_style = request.getParameterValues("class_style");
		String[] schoolAbbr = request.getParameterValues("schoolAbbr");
		
		for(int i=0;i<JL_gradeId.length;i++) {
			if(!JL_gradeId[i].isEmpty() && !class_style[i].isEmpty() && !schoolAbbr[i].isEmpty()) {
				String Abbr = "";
				if(schoolAbbr[i].equals("P")) {
					Abbr = "TPXC";
				}else if(schoolAbbr[i].equals("K")) {
					Abbr = "KSZS";
				}else if(schoolAbbr[i].equals("C")) {
					Abbr = "TCFX";
				}else if(schoolAbbr[i].equals("L")) {
					Abbr = "TPSL";
				}
				jdbcTemplate.update("INSERT INTO eip.JL_EIP_grade VALUES (default,?,?,?,?,?);",
					request.getParameter("eip_grade_seq"),
					JL_gradeId[i],
					class_style[i],
					request.getParameter("subject_id"),
					Abbr
				);
			}	
		}	
		return "/common/closeAndReload"; 
	}
	
	@RequestMapping("/Course/gradeDisable")
	@ResponseBody
	public String gradeDisable(Model model,HttpServletRequest request,Principal principal){
		String grade_seq = request.getParameter("grade_seq");
		String col1 = "取消開課/刪除課程";
		Grade grade= courseService.getGradeList("","",grade_seq,"","","","","","","","","","","","","","1","1","").get(0);
		managerService.applicationLogSave(principal.getName(),"2","-1","-1",col1,grade.getSchool_name(),grade.getCategory_name(),grade.getSubject_name(),grade.getClass_start_date(),grade_seq);		   
		courseService.gradeDisable("1",grade_seq);
		
		return "Successful";	
	}
	
	@RequestMapping("/Course/gradeLock")
	@ResponseBody
	public String gradeLock(Model model,HttpServletRequest request,Principal principal){
		String grade_seq = request.getParameter("grade_seq");
		String disable = request.getParameter("disable");
		String col1 = "勿訂此課程";
		Grade grade= courseService.getGradeList("","",grade_seq,"","","","","","","","","","","","","","1","1","").get(0);
		managerService.applicationLogSave(principal.getName(),"2","-1","-1",col1,grade.getSchool_name(),grade.getCategory_name(),grade.getSubject_name(),grade.getClass_start_date(),grade_seq);		   
		courseService.gradeDisable(disable,grade_seq);
		
		return "Successful";	
	}	
	
	@RequestMapping("/Course/comboDisable")
	@ResponseBody
	public String comboDisable(Model model,HttpServletRequest request,Principal principal){
		String comboSale_seq = request.getParameter("comboSale_seq");
		String col1 = "刪除單科/套裝";
		ComboSale comboSale = courseSaleService.getComboSaleByCombo("",comboSale_seq).get(0);
		managerService.applicationLogSave(principal.getName(),"3","-1","-1",col1,comboSale.getCategory_name(),comboSale.getName(),comboSale.getSubjectStr(),comboSale.getSale_price(),comboSale_seq);		   
		courseService.comboDisable(comboSale_seq);
		
		return "Successful";	
	}
	
	@RequestMapping("/Course/subjectDisable")
	@ResponseBody
	public String subjectDisable(Model model,HttpServletRequest request,Principal principal){
		String subject_seq = request.getParameter("subject_seq");
		String col1 = "刪除科目";
		Subject subject = courseService.getSubject("",subject_seq,"","","","0").get(0);
		managerService.applicationLogSave(principal.getName(),"5","-1","-1",col1,subject.getCategory_name(),subject.getName(),"",subject.getPrice(),subject_seq);		   
		courseService.subjectDisable(subject_seq);
		
		return "Successful";	
	}
		
	@RequestMapping("/Course/checkGradeDuplicate")
	@ResponseBody
	public String checkGradeDuplicate(Model model,HttpServletRequest request){
		String school_code = request.getParameter("school_code");
		String category_id = request.getParameter("category_id");
		String subject_id = request.getParameter("subject_id");
		String teacher_id = request.getParameter("teacher_id");
		String slot_id = request.getParameter("slot_id");
		String gradeName = request.getParameter("gradeName");
		String class_start_date_0 = request.getParameter("class_start_date_0");
		if(class_start_date_0.length()>10) {
			class_start_date_0 = class_start_date_0.substring(0,10);
		}
		String class_style = request.getParameter("class_style");
		
		int gradeCount = courseService.getGrade3(school_code,category_id,subject_id,teacher_id,slot_id,gradeName,class_start_date_0,class_style);
		if(gradeCount>0) {
			return "duplicate";
		}else {
			return "";
		}
	}
	
    
    @RequestMapping("Course/testSubjectSetting")
    public String testSubjectSetting() {
    	return "/Course/testSubjectSetting";
    } 
    
    
    @RequestMapping("/Course/getTestRound")
    @ResponseBody
    public List<TestRound> getTestRoundGroupBy(Model model,HttpServletRequest request){   	
    	List<TestRound> LTestRound_1 =  courseService.getTestRoundGroupBy();
    	List<TestRound> LTestRound_3 = new ArrayList<TestRound>();
    	for(int i=0;i<LTestRound_1.size();i++) {
    		List<TestRound> LTestRound_2 =  courseService.getTestRound(LTestRound_1.get(i).getCategory_id());
    		String testRoundStr = "";
    		for(int j=0;j<LTestRound_2.size();j++) {
    			testRoundStr += "<b>"+LTestRound_2.get(j).getName()+"</b>"+"&nbsp;&nbsp;";
    		}
    		LTestRound_3.add(new TestRound("",LTestRound_1.get(i).getCategory_id(),testRoundStr,LTestRound_1.get(i).getCategory_name()));
    	}
        return LTestRound_3;
    }
    
    @RequestMapping("Course/testSubjectEdit")
    public String testSubjectEdit(Model model) {
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);    	
    	return "/Course/testSubjectEdit";
    }    
	
}  




