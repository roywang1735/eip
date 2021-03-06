package com.wordgod.eip.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wordgod.eip.Model.*;
import com.wordgod.eip.Service.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.ui.Model;

@Controller
public class CourseSaleController {

	@Autowired
	CourseService courseService;
	@Autowired
	AccountService accountService;
	@Autowired
	CourseSaleService courseSaleService;
	@Autowired	
	ManagerService managerService;
	@Autowired	
	AdmService admService;	

	@RequestMapping("/CourseSale/SingleSaleSetting")
	public String CourseSetting(Model model) {
		
		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);
				
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);
		 
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
		List<FlowStatus> flowStatusGroup = courseService.getFlowStatus();
		model.addAttribute("flowStatusGroup", flowStatusGroup);		
		
		return "/CourseSale/SingleSaleSetting";
	}
	
	@RequestMapping("/CourseSale/ComboSaleSetting")
	public String ComboSaleSetting(Model model,HttpSession session,HttpServletRequest request) {
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
		
		if(request.getParameter("sessionState")!=null) {
			model.addAttribute("sessionState","keep");
			
			String category_id = "";
			if(session.getAttribute("CourseController_category_id")!=null) {
				category_id = (String)session.getAttribute("CourseController_category_id");
			}  
			model.addAttribute("category_id",category_id);
			
			String isCombo = "";
			if(session.getAttribute("CourseController_isCombo")!=null) {
				isCombo = (String)session.getAttribute("CourseController_isCombo");
			}  
			model.addAttribute("isCombo",isCombo);
		
			String FlowStatus_code = "";
			if(session.getAttribute("CourseController_FlowStatus_code")!=null) {
				FlowStatus_code = (String)session.getAttribute("CourseController_FlowStatus_code");
			}  
			model.addAttribute("FlowStatus_code",FlowStatus_code);			
		}else {
			session.removeAttribute("CourseController_category_id");
			session.removeAttribute("CourseController_isCombo");
			session.removeAttribute("CourseController_FlowStatus_code");
		}			
		
		return "/CourseSale/ComboSaleSetting";
	}
	
	@RequestMapping("/CourseSale/CounselingSetting")
	public String CounselingSetting(Model model) {
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	
		//return "/common/empty";
		return "/CourseSale/CounselingSetting";
	}		
	
	
	@RequestMapping("/CourseSale/newSingleCourse")
	public String newSingleCourse(Model model,HttpServletRequest request) {

		List<School> schoolGroup = accountService.getSchool("","");
		model.addAttribute("schoolGroup", schoolGroup);		
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	
		
		List<Teacher> teacherGroup = courseService.getTeacher("","","","","","","");
		model.addAttribute("teacherGroup", teacherGroup);
		
 
		 Course course = new Course();
    	model.addAttribute("course", course);		
		return "/CourseSale/newSingleCourse";
	}
	
	@RequestMapping("/CourseSale/newComboSale")
	public String newComboSale(Model model,HttpServletRequest request) {

		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	
		
    	ComboSale comboSale = new ComboSale();
    	if(request.getParameter("comboSale_seq")!=null) {comboSale = courseSaleService.getComboSale(request.getParameter("comboSale_seq"),"","","","","","","0").get(0);}
    	model.addAttribute("comboSale", comboSale);
 
    	List<ComboSale_subject> LComboSale_subject = new ArrayList<ComboSale_subject>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_subject = courseSaleService.getComboSale_subject(request.getParameter("comboSale_seq"),"");}
    	model.addAttribute("comboSale_subject", LComboSale_subject);    	

    	List<ComboSale_material> LComboSale_material = new ArrayList<ComboSale_material>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_material = courseSaleService.getComboSale_material(request.getParameter("comboSale_seq"));}
    	model.addAttribute("comboSale_material", LComboSale_material); 
    	
    	List<ComboSale_mock> LComboSale_mock = new ArrayList<ComboSale_mock>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_mock = courseSaleService.getComboSale_mock(request.getParameter("comboSale_seq"));}
    	model.addAttribute("comboSale_mock", LComboSale_mock); 
    	
    	List<ComboSale_counseling> LComboSale_counseling = new ArrayList<ComboSale_counseling>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_counseling = courseSaleService.getComboSale_counseling(request.getParameter("comboSale_seq"));}
    	model.addAttribute("comboSale_counseling", LComboSale_counseling); 
    	
		return "/CourseSale/newComboSale";
	}
	
	@RequestMapping("/CourseSale/newSingleSale")
	public String newSingleSale(Model model,HttpServletRequest request) {

		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	
		
    	ComboSale comboSale = new ComboSale();
    	if(request.getParameter("comboSale_seq")!=null) {comboSale = courseSaleService.getComboSale(request.getParameter("comboSale_seq"),"","","","","","","0").get(0);}
    	model.addAttribute("comboSale", comboSale);
 
    	List<ComboSale_subject> LComboSale_subject = new ArrayList<ComboSale_subject>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_subject = courseSaleService.getComboSale_subject(request.getParameter("comboSale_seq"),"");}
    	model.addAttribute("comboSale_subject", LComboSale_subject);    	

    	ComboSale_material comboSale_material = new ComboSale_material();
    	if(request.getParameter("comboSale_seq")!=null) {
    		List<ComboSale_material> LComboSale_material = courseSaleService.getComboSale_material(request.getParameter("comboSale_seq"));
    		if(LComboSale_material.size()>0) {comboSale_material = LComboSale_material.get(0);}
    	}
    	model.addAttribute("comboSale_material", comboSale_material);
    	
    	List<ComboSale_mock> LComboSale_mock = new ArrayList<ComboSale_mock>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_mock = courseSaleService.getComboSale_mock(request.getParameter("comboSale_seq"));}
    	model.addAttribute("comboSale_mock", LComboSale_mock); 
    	
    	List<ComboSale_counseling> LComboSale_counseling = new ArrayList<ComboSale_counseling>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_counseling = courseSaleService.getComboSale_counseling(request.getParameter("comboSale_seq"));}
    	model.addAttribute("comboSale_counseling", LComboSale_counseling);
 
    	List<ComboSale_lagnappe> LComboSale_lagnappe = new ArrayList<ComboSale_lagnappe>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_lagnappe = courseSaleService.getComboSale_lagnappe(request.getParameter("comboSale_seq"));}
    	model.addAttribute("comboSale_lagnappe", LComboSale_lagnappe);
    	
		return "/CourseSale/newSingleSale";
	}
	
	@RequestMapping("/CourseSale/newSingleSaleVirtual")
	public String newSingleSaleVirtual(Model model,HttpServletRequest request) {

		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	
		
    	ComboSale comboSale = new ComboSale();
    	if(request.getParameter("comboSale_seq")!=null) {comboSale = courseSaleService.getComboSale(request.getParameter("comboSale_seq"),"","","","","","","0").get(0);}
    	model.addAttribute("comboSale", comboSale);
 
    	List<ComboSale_subject> LComboSale_subject = new ArrayList<ComboSale_subject>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_subject = courseSaleService.getComboSale_subject(request.getParameter("comboSale_seq"),"");}
    	model.addAttribute("comboSale_subject", LComboSale_subject);    	

    	ComboSale_material comboSale_material = new ComboSale_material();
    	if(request.getParameter("comboSale_seq")!=null) {
    		List<ComboSale_material> LComboSale_material = courseSaleService.getComboSale_material(request.getParameter("comboSale_seq"));
    		if(LComboSale_material.size()>0) {comboSale_material = LComboSale_material.get(0);}
    	}
    	model.addAttribute("comboSale_material", comboSale_material);
    	
    	List<ComboSale_mock> LComboSale_mock = new ArrayList<ComboSale_mock>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_mock = courseSaleService.getComboSale_mock(request.getParameter("comboSale_seq"));}
    	model.addAttribute("comboSale_mock", LComboSale_mock); 
    	
    	List<ComboSale_counseling> LComboSale_counseling = new ArrayList<ComboSale_counseling>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_counseling = courseSaleService.getComboSale_counseling(request.getParameter("comboSale_seq"));}
    	model.addAttribute("comboSale_counseling", LComboSale_counseling);
 
    	List<ComboSale_lagnappe> LComboSale_lagnappe = new ArrayList<ComboSale_lagnappe>();
    	if(request.getParameter("comboSale_seq")!=null) {LComboSale_lagnappe = courseSaleService.getComboSale_lagnappe(request.getParameter("comboSale_seq"));}
    	model.addAttribute("comboSale_lagnappe", LComboSale_lagnappe);
    	
		return "/CourseSale/newSingleSaleVirtual";
	}	

	@RequestMapping("/CourseSale/AddComboSaleList")
	@ResponseBody
	public String AddComboSaleList(HttpServletRequest request) {
		String returnSubject = "";
		
		//賦予下拉式科目隱藏值
		List<Subject> LSubject = courseService.getSubject(request.getParameter("SelectCategoryId"),"","","1","","0");
		for(int i=0;i<LSubject.size();i++){
			String CounselingStr = "";
			String LagnappeStr = "";
			String MockStr = "";
			String hrPrice_R1="",hrPrice_R3="";
			String counselingPrice_R1="",counselingPrice_R3="";
			String lagnappePrice_R1="",lagnappePrice_R3="";
			String handoutPrice_R1="",handoutPrice_R3="";
			String homeworkPrice_R1="",homeworkPrice_R3="";
			String mockPrice_R1="",mockPrice_R3="";
			//班內
			List<Subject> subject1 = courseService.getSubjectWithR("",LSubject.get(i).getSubject_seq(),"","1","1");
			//線上
			List<Subject> subject3 = courseService.getSubjectWithR("",LSubject.get(i).getSubject_seq(),"","1","3");
						
			
			if(subject1.size()>0) {
				hrPrice_R1 = subject1.get(0).getHrPrice_R();
				counselingPrice_R1 = subject1.get(0).getCounselingPrice_R();
				lagnappePrice_R1 = subject1.get(0).getLagnappePrice_R();
				handoutPrice_R1 = subject1.get(0).getHandoutPrice_R();
				homeworkPrice_R1 = subject1.get(0).getHomeworkPrice_R();
				mockPrice_R1 = subject1.get(0).getMockPrice_R();
			}
			
			if(subject3.size()>0) {
				hrPrice_R3 = subject3.get(0).getHrPrice_R();
				counselingPrice_R3 = subject3.get(0).getCounselingPrice_R();
				lagnappePrice_R3 = subject3.get(0).getLagnappePrice_R();
				handoutPrice_R3 = subject3.get(0).getHandoutPrice_R();
				homeworkPrice_R3 = subject3.get(0).getHomeworkPrice_R();
				mockPrice_R3 = subject3.get(0).getMockPrice_R();
			}			
			
			returnSubject += "<option value='"+
								LSubject.get(i).getSubject_seq()+"@"+
								LSubject.get(i).getPrice()+"@"+
								CounselingStr+"@"+
								LagnappeStr+"@"+
								MockStr+"@"+
								hrPrice_R1+"|"+hrPrice_R3+"@"+
								counselingPrice_R1+"|"+counselingPrice_R3+"@"+
								lagnappePrice_R1+"|"+lagnappePrice_R3+"@"+
								handoutPrice_R1+"|"+handoutPrice_R3+"@"+
								homeworkPrice_R1+"|"+homeworkPrice_R3+"@"+
								mockPrice_R1+"|"+mockPrice_R3+"'>"+
								LSubject.get(i).getName()+
							 "</option>";		
		}
		
		String returnStr =
				"<div class='tr' style='font-size:small;height:20px;background-color:lightyellow;width:1150px' >"+
		           "<div class='td2' style='padding:2px;width:330px'>"+
		               "<A href='javascript:void(0)' title='刪除此筆'  onclick='$(this).parent().parent().remove();'><img src='/images/delete.png' height='10px'/></A>&nbsp;"+
		               "<select style='border-radius:5px;padding:3px' name='subject_seq' id='subject_seq' class='subject_seq' onchange='SetOriginPrice(this,this.options[this.selectedIndex].value)'><option value=''>~ 科目 ~</option>"+returnSubject+"</select>&nbsp;"+
		            "</div>"+
		           "<div class='td2' style='padding:2px;width:70px'><input type='text' class='originPrice' style='width:100%;border:1px dotted #eeeeee' readonly></div>"+
				   "<div class='td2' style='padding:2px;width:50px'><input type='text' class='salePrice' style='width:100%;border:0px;background-color:lightyellow' readonly></div>"+
		           "<div class='td2' style='padding:2px;width:50px'><input type='text' class='hrPrice'   style='width:100%;border:0px;background-color:lightyellow' readonly></div>"+		           
		           "<div class='td2' style='padding:2px;width:120px'><input type='text' class='counselingPrice' style='width:100%;border:0px;background-color:lightyellow' readonly /><div class='counselingName'></div></div>"+	
		           "<div class='td2' style='padding:2px;width:120px'><input type='text' class='lagnappePrice' style='width:100%;border:0px;background-color:lightyellow' readonly /><div class='lagnappeName'></div></div>"+		           
		           "<div class='td2' style='padding:2px;width:60px'><input type='text' class='handoutPrice' style='width:100%;border:0px;background-color:lightyellow' readonly></div>"+
		           "<div class='td2' style='padding:2px;width:60px'><input type='text' class='homeworkPrice' style='width:100%;border:0px;background-color:lightyellow' readonly></div>"+
		           "<div class='td2' style='padding:2px;width:130px'><input type='text' class='mockPrice' style='width:100%;border:0px;background-color:lightyellow' readonly /><div class='mockName'></div></div>"+		           
		           "<div class='td2' style='padding:2px;width:60px'><input type='text' class='coursePrice' style='width:100%;border:0px;background-color:lightyellow' readonly></div>"+
		           "<div class='td2' style='padding:2px;width:100px'><input type='text' class='' style='width:100%;border:0px;background-color:lightyellow' readonly /><div class='outPublisherName'></div></div>"+
		           "</div>";
		
		return returnStr;
	}	
	
	@RequestMapping("/CourseSale/AddSubjectStr")
	@ResponseBody
	public String AddSubjecteStr(HttpServletRequest request) {	
		Subject subject = courseService.getSubject("",request.getParameter("subject_seq"),"","","","0").get(0);
		String returnStr =
				"<div>"+
						"<input type='hidden' name='subject_seq' value='"+subject.getSubject_seq()+"'>"+
						"<input type='hidden' name='comboSaleName' value='"+subject.getName()+"'>"+
						"<div class='col-md-6' style='color:darkblue;font-weight:bold;background-color:papayawhip;border: 1px #eeeeee solid'>"+subject.getName()+"</div>"+
						"<div class='col-md-2'><input type='text' name='subject_originPrice' style='width:70px' class='originPrice'  onchange='PriceAdd()'></div>"+
						"<div class='col-md-2'><input type='text' name='subject_originPrice' style='width:70px' class='originPrice'  onchange='PriceAdd()'></div>"+
						"<div class='col-md-2'><input type='text' name='subject_originPrice' style='width:70px' class='originPrice'  onchange='PriceAdd()'></div>"+
				"</div>&nbsp;<br>";
		
		return returnStr;
	}
	 
	@RequestMapping("/CourseSale/AddMockStr")
	@ResponseBody
	public String AddMockStr(HttpServletRequest request) {	
		Mock mock = courseService.getMock("",request.getParameter("mock_seq"),"").get(0);
		String returnStr =
				"<div>"+
						"<input type='hidden' name='mock_seq' value='"+mock.getMock_seq()+"'>"+
						"<div class='col-md-6' style='color:maroon;font-weight:bold;background-color:papayawhip;border: 1px #eeeeee solid'>"+mock.getMock_name()+"</div>"+
						//"<div class='col-md-2'><input type='text' readonly name='mock_originPrice' value='"+mock.getOrigin_price()+"' style='border-style:none; width:70px' class='originPrice'  onchange='PriceAdd()'></div>"+
				"</div>&nbsp;<br>";
		
		return returnStr;
	}
	
	@RequestMapping("/CourseSale/AddCounselingStr")
	@ResponseBody
	public String AddCounselingStr(HttpServletRequest request) {	
		Counseling counseling = courseService.getCounseling("",request.getParameter("counseling_seq"),"").get(0);
		String returnStr =
				"<div>"+
						"<input type='hidden' name='counseling_seq' value='"+counseling.getCounseling_seq()+"'>"+
						"<div class='col-md-8' style='color:darkblue;font-weight:bold;background-color:papayawhip;border: 1px #eeeeee solid'>"+counseling.getCounseling_name()+"</div>"+
						"<div class='col-md-2'><input type='text' readonly name='counseling_originPrice' value='"+counseling.getOrigin_price()+"' style='border-style:none; width:70px' class='originPrice'  onchange='PriceAdd()'></div>"+
				"</div>&nbsp;<br>";
		
		return returnStr;
	}
	
	@RequestMapping("/CourseSale/AddLagnappeStr")
	@ResponseBody
	public String AddLagnappeStr(HttpServletRequest request) {	
		Lagnappe lagnappe = courseSaleService.getLagnappe(request.getParameter("lagnappe_seq"),"").get(0);
		String returnStr =
				"<div>"+
						"<input type='hidden' name='lagnappe_seq' value='"+lagnappe.getLagnappe_seq()+"'>"+
						"<div class='col-md-8' style='color:darkblue;font-weight:bold;background-color:papayawhip;border: 1px #eeeeee solid'>"+lagnappe.getLagnappe_name()+"</div>"+
						"<div class='col-md-2'><input type='text' readonly name='lagnappe_originPrice' value='"+lagnappe.getOrigin_price()+"' style='border-style:none; width:70px' class='originPrice'  onchange='PriceAdd()'></div>"+
				"</div>&nbsp;<br>";
		
		return returnStr;
	}	
	
	/*
	 * @RequestMapping("/CourseSale/AddComboSale_subjectStr")
	 * 
	 * @ResponseBody public String AddComboSale_subjectStr(HttpServletRequest
	 * request) { List<ComboSale_subject> LComboSale_subject =
	 * courseSaleService.getComboSale_subject(request.getParameter("comboSale_seq"))
	 * ;
	 * 
	 * String returnStr = "<div class='col-md-12' style='padding:2px'>"+
	 * "<input type='hidden' name='subject_seq' value='"+LComboSale_subject.get(0).
	 * getSubject_id()+"'>"+
	 * "<input type='hidden' name='subject_originPrice' value='"+LComboSale_subject.
	 * get(0).getOrigin_price()+"'>"+
	 * "<div class='col-md-1'><A href='##' onclick='$(this).parent().parent().remove();'><img src='/images/delete.png' height='10px'/></A></div>"
	 * +
	 * "<div class='col-md-5' style='color:darkblue;font-weight:bold;background-color:#ffeeff;height:25px; border: 1px #eeeeee solid'>"
	 * +LComboSale_subject.get(0).getSubject_name()+"</div>"+
	 * "<div class='col-md-2 originPrice' style='border: 1px ffeeff solid'>"
	 * +LComboSale_subject.get(0).getOrigin_price()+"</div>"+
	 * "<div class='col-md-2'><input type='text' name='subject_salePrice' style='width:50px;background-color:lightyellow;border:1px #eeeeee solid' class='salePrice' value='"
	 * +LComboSale_subject.get(0).getOrigin_price()
	 * +"' onchange='PriceAdd();'></div>"+
	 * "<div class='col-md-2'><div style='display:inline-block'><input class='chk' type='checkbox' checked></div><div style='display:inline-block' class='downPrice'></div></div>"
	 * + "</div>";
	 * 
	 * return returnStr; }
	 */
	
	@RequestMapping("/CourseSale/AddComboSale_materialStr")
	@ResponseBody
	public String AddComboSale_materialStr(HttpServletRequest request) {
		String returnStr = "";
		if(request.getParameter("material_seq")!=null && !request.getParameter("material_seq").equals("")) {
			Material material = courseService.getMaterial("", request.getParameter("material_seq")).get(0);
			returnStr += 
				"<div class='col-md-12' style='padding:2px'>"+	
				"<input type='hidden' name='material_seq' value='"+material.getMaterial_seq()+"'>"+
				"<input type='hidden' name='material_originPrice' value='"+material.getOrigin_price()+"'>"+		
			    "<div class='col-md-1'><A href='javascript:void(0)' onclick='$(this).parent().parent().remove();'><img src='/images/delete.png' height='10px'/></A></div>"+			
				"<div class='col-md-5' style='color:darkblue;background-color:#ffeeff;height:25px; border: 1px #eeeeee solid;'><b>"+material.getMaterial_name()+"</b></div>"+
				"<div class='col-md-2 originPrice' style='border: 1px ffeeff solid;'>"+material.getOrigin_price()+"</div>"+
		        "<div class='col-md-2'><input type='text' name='material_salePrice' style='width:50px;background-color:lightyellow;border:1px #eeeeee solid' class='salePrice' value='"+material.getOrigin_price()+"' onchange='PriceAdd();'></div>"+			   
		        "<div class='col-md-2'><div style='display:inline-block'><input class='chk' type='checkbox'></div><div style='display:inline-block' class='downPrice'></div></div>"+
		        "</div>";
		}
		
		if(request.getParameter("comboSale_seq")!=null && !request.getParameter("comboSale_seq").equals("")) {
		   List<ComboSale_material> LComboSale_Material = courseSaleService.getComboSale_material(request.getParameter("comboSale_seq"));
		   for(int i=0;i<LComboSale_Material.size();i++) {
			    returnStr += 
			    		"<div class='col-md-12' style='padding:2px'>"+
						"<input type='hidden' name='material_seq' value='"+LComboSale_Material.get(i).getMaterial_id()+"'>"+
						"<input type='hidden' name='material_originPrice' value='"+LComboSale_Material.get(i).getOrigin_price()+"'>"+		
					    "<div class='col-md-1'><A href='javascript:void(0)' onclick='$(this).parent().parent().remove();'><img src='/images/delete.png' height='10px'/></A></div>"+
						"<div class='col-md-5' style='color:darkblue;background-color:#ffeeff;height:25px; border: 1px #eeeeee solid'><b>"+LComboSale_Material.get(i).getMaterial_name()+"</b></div>"+
						"<div class='col-md-2 originPrice' style='border: 1px ffeeff solid'>"+LComboSale_Material.get(i).getOrigin_price()+"</div>"+
				        "<div class='col-md-2'><input type='text' name='material_salePrice' style='width:50px;background-color:lightyellow;border:1px #eeeeee solid' class='salePrice' value='"+LComboSale_Material.get(i).getOrigin_price()+"' onchange='PriceAdd();'></div>"+			   
				        "<div class='col-md-2'><div style='display:inline-block'><input class='chk' type='checkbox'></div><div style='display:inline-block' class='downPrice'></div></div>"+
		                "</div>";
		   }		   
		}
		
		return returnStr;
	}	
	
	@RequestMapping("/CourseSale/AddComboSale_mockStr")
	@ResponseBody
	public String AddComboSale_mockStr(HttpServletRequest request) {
		
		List<String> LMock_seq = new ArrayList<>(); 
		if(request.getParameter("mock_seq")!=null && !request.getParameter("mock_seq").equals("")) {
			LMock_seq.add(request.getParameter("mock_seq"));
		}
		if(request.getParameter("comboSale_seq")!=null && !request.getParameter("comboSale_seq").equals("")) {
		   List<ComboSale_mock> LComboSale_mock = courseSaleService.getComboSale_mock(request.getParameter("comboSale_seq"));
		   for(int i=0;i<LComboSale_mock.size();i++) {
			   LMock_seq.add(LComboSale_mock.get(i).getMock_id());
		   }		   
		}
		String returnStr = "";
		for(int i=0;i<LMock_seq.size();i++) {
			Mock mock = courseService.getMock("", LMock_seq.get(i),"").get(0);
			returnStr +=
				"<div class='col-md-12' style='padding:2px'>"+	
				"<input type='hidden' name='mock_seq' value='"+mock.getMock_seq()+"'>"+
				//"<input type='hidden' name='mock_originPrice' value='"+mock.getOrigin_price()+"'>"+
			    "<div class='col-md-1'><A href='javascript:void(0)' onclick='$(this).parent().parent().remove();'><img src='/images/delete.png' height='10px'/></A></div>"+						
				"<div class='col-md-5' style='color:darkblue;background-color:#ffeeff;height:25px; border: 1px #eeeeee solid'><b>"+mock.getMock_name()+"</b></div>"+
				//"<div class='col-md-2 originPrice' style='border: 1px ffeeff solid'>"+mock.getOrigin_price()+"</div>"+
				//"<div class='col-md-2'><input type='text' name='mock_salePrice' style='width:50px;background-color:lightyellow;border:1px #eeeeee solid' class='salePrice' value='"+mock.getOrigin_price()+"' onchange='PriceAdd();'></div>"+
				"<div class='col-md-2'><div style='display:inline-block'><input class='chk' type='checkbox' checked></div><div style='display:inline-block' class='downPrice'></div></div>"+
				"</div>";
		}
		return returnStr;
	}	
	
	@RequestMapping("/CourseSale/AddComboSale_counselingStr")
	@ResponseBody
	public String AddComboSale_counselingStr(HttpServletRequest request) {
		
		List<String> LCounseling_seq = new ArrayList<>(); 
		if(request.getParameter("counseling_seq")!=null && !request.getParameter("counseling_seq").equals("")) {
			LCounseling_seq.add(request.getParameter("counseling_seq"));
		}
		if(request.getParameter("comboSale_seq")!=null && !request.getParameter("comboSale_seq").equals("")) {
		   List<ComboSale_counseling> LComboSale_counseling = courseSaleService.getComboSale_counseling(request.getParameter("comboSale_seq"));
		   for(int i=0;i<LComboSale_counseling.size();i++) {
			   LCounseling_seq.add(LComboSale_counseling.get(i).getCounseling_id());
		   }		   
		}
		String returnStr = "";		
		for(int i=0;i<LCounseling_seq.size();i++) {
			Counseling counseling = courseService.getCounseling("",LCounseling_seq.get(i),"").get(0);		

		    returnStr +=
		    	"<div class='col-md-12' style='padding:2px'>"+	
				"<input type='hidden' name='counseling_seq' value='"+counseling.getCounseling_seq()+"'>"+
				"<input type='hidden' name='counseling_originPrice' value='"+counseling.getOrigin_price()+"'>"+
			    "<div class='col-md-1'><A href='javascript:void(0)' onclick='$(this).parent().parent().remove();'><img src='/images/delete.png' height='10px'/></A></div>"+						
				"<div class='col-md-5' style='color:darkblue;background-color:#ffeeff;height:25px; border: 1px #eeeeee solid'><b>"+counseling.getCounseling_name()+"</b></div>"+
				"<div class='col-md-2 originPrice' id='originPrice' style='border: 1px ffeeff solid'>"+counseling.getOrigin_price()+"</div>"+
			    "<div class='col-md-2'><input type='text' name='counseling_salePrice' style='width:50px;background-color:lightyellow;border:1px #eeeeee solid' class='salePrice' value='"+counseling.getOrigin_price()+"' onchange='PriceAdd();'></div>"+
			    "<div class='col-md-2'><div style='display:inline-block'><input class='chk' type='checkbox' checked></div><div style='display:inline-block' class='downPrice'></div></div>"+
			    "</div>";
		}		
		return returnStr;
	}
	
	@RequestMapping("/CourseSale/AddComboSale_lagnappeStr")
	@ResponseBody
	public String AddComboSale_lagnappeStr(HttpServletRequest request) {
		List<String> LLagnappe_seq = new ArrayList<>(); 
		if(request.getParameter("lagnappe_seq")!=null && !request.getParameter("lagnappe_seq").equals("")) {
			LLagnappe_seq.add(request.getParameter("lagnappe_seq"));
		}
		if(request.getParameter("comboSale_seq")!=null && !request.getParameter("comboSale_seq").equals("")) {
		   List<ComboSale_lagnappe> LComboSale_lagnappe = courseSaleService.getComboSale_lagnappe(request.getParameter("comboSale_seq"));
		   for(int i=0;i<LComboSale_lagnappe.size();i++) {
			   LLagnappe_seq.add(LComboSale_lagnappe.get(i).getLagnappe_id());
		   }		   
		}
		String returnStr = "";		
		for(int i=0;i<LLagnappe_seq.size();i++) {
			Lagnappe lagnappe = courseSaleService.getLagnappe(LLagnappe_seq.get(i),"").get(0);		

		    returnStr = 
		    	"<div class='col-md-12' style='padding:2px'>"+
		    	"<input type='hidden' name='lagnappe_seq' value='"+lagnappe.getLagnappe_seq()+"'>"+
				"<input type='hidden' name='lagnappe_originPrice' value='"+lagnappe.getOrigin_price()+"'>"+
			    "<div class='col-md-1'><A href='javascript:void(0)' onclick='$(this).parent().parent().remove();'><img src='/images/delete.png' height='10px'/></A></div>"+						
				"<div class='col-md-5' style='color:darkblue;background-color:#ffeeff;height:25px; border: 1px #eeeeee solid'><b>"+lagnappe.getLagnappe_name()+"</b></div>"+
				"<div class='col-md-2 originPrice' id='originPrice' style='border: 1px ffeeff solid'>"+lagnappe.getOrigin_price()+"</div>"+
		        "<div class='col-md-2'><input type='text' name='lagnappe_salePrice' style='width:50px;background-color:lightyellow;border:1px #eeeeee solid' class='salePrice' value='"+lagnappe.getOrigin_price()+"' onchange='PriceAdd();'></div>"+
				"<div class='col-md-2'><div style='display:inline-block'><input class='chk' type='checkbox'></div><div style='display:inline-block' class='downPrice'></div></div>"+
		        "</div>";
		}
		return returnStr;
	}	
	
	@RequestMapping("/CourseSale/AddComboStr")
	@ResponseBody
	public String AddComboStr(HttpServletRequest request) {	
		String combo_seq = request.getParameter("combo_seq");
		String returnStr = 
			"<div>"+
				"<input type='hidden' name='subject_sel' value=''>"+
				"<div class='col-md-4' style='height:35px; border: 1px lightblue solid; padding-top:5px; border-top:0px; background-color: #ffeeff'>"+combo_seq+"</div>"+
				"<div class='col-md-2' style='height:35px; border: 1px lightblue solid; padding-top:5px; border-top:0px;'>"+
					"<select><option></option><option>1</option><option>2</option><option>3</option><option>4</option><option>5</option></select>"+
				"</div>"+
				"<div class='col-md-2' style='height:35px; border: 1px lightblue solid; padding-top:5px; border-top:0px;'>15999</div>"+
				"<div class='col-md-2' style='height:35px; border: 0px lightblue solid; padding-top:5px; border-top:0px;'></div>"+
				"<div class='col-md-2' style='height:35px; border: 0px lightblue solid; padding-top:5px; border-top:0px;'></div>"+
			"</div>";	
		return returnStr;
	}	
	
	@RequestMapping("/CourseSale/PromoSetting")
	public String PromoSetting(Model model,HttpServletRequest request) {
	
		return "/CourseSale/PromoSetting";
	}	
	
	@RequestMapping("/CourseSale/CourseCost")
	public String CourseCost(Model model,HttpServletRequest request) {
	
		return "/CourseSale/CourseCost";
	}
	
	@RequestMapping("/CourseSale/MockCost")
	public String MockCost(Model model) {
		List<Mock> mocks = courseSaleService.getMock();
		model.addAttribute("mocks", mocks);				
		return "/CourseSale/MockCost";
	}
	
	@RequestMapping("/CourseSale/CounselingCost")
	public String CounselingCost(Model model) {
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);		
		return "/CourseSale/CounselingCost";
	}
		
	@RequestMapping("/CourseSale/MaterialCost")
	public String MaterialCost(Model model) {
		List<Material> materials = courseSaleService.getMaterial("");
		model.addAttribute("materials", materials);				
		return "/CourseSale/MaterialCost";
	}	

	@RequestMapping("/CourseSale/ComboSaleSave")
	@ResponseBody
	public Boolean ComboSaleSave(HttpServletRequest request,Principal principal) {
		   String FlowStatus_code = request.getParameter("status_code");
		   String comboSale_seq = request.getParameter("comboSale_seq");
		   String activeEdit = request.getParameter("activeEdit"); //上架中修改
		   String col1 = "";
		   ComboSale comboSale = null;
	       switch(FlowStatus_code) {
	            case "1": col1 = "暫存"; break;
	       		case "2": col1 = "送審"; break;
	       		case "3": col1 = "待上架"; break;
	       		case "4": col1 = "上架"; break;
	       		case "5": col1 = "下架"; break;
	       		case "6": col1 = "退件"; break;
	       		case "7": col1 = "刪除"; break;
	       }
	       
		   //Update Flow status only
		   if(!(FlowStatus_code.equals("1") || FlowStatus_code.equals("3") || (FlowStatus_code.equals("4") && activeEdit.equals("yes")))) { //!=1:暫存,3:待上架,4+yes:上架中修改
			   if(FlowStatus_code.equals("7")) { //7:刪除
				  comboSale = courseSaleService.getComboSale(comboSale_seq,"","","","","","","0").get(0);
			   }			   
				  courseSaleService.UpdatecomboSaleStatus(comboSale_seq,FlowStatus_code);	  
		   //(1,2,yes才存)
		   }else{
			   String isCombo = request.getParameter("isCombo");
			   String category_id = request.getParameter("category_id");
			   String comboSaleName = request.getParameter("comboSaleName");
			   String class_makeup = request.getParameter("class_makeup");
			   String origin_price = request.getParameter("originPriceTotal");
			   String sale_price = request.getParameter("salePriceTotal");
			   String schedule_time = request.getParameter("schedule_time");
			   String favor_id = request.getParameter("favor_id");
		 		 
			   //subject
			   String[] A_subject_seq = request.getParameterValues("subject_seq");	//ex. 79@19900@@@@20|10@0|0@0|0@0|0@0|0@0|0
			   //mock		 
			   String[] A_mock_seq = request.getParameterValues("mock_seq");
			   //counseling		 
			   String[] A_counseling_seq = request.getParameterValues("counseling_seq");		 
			   //lagnappe		 
			   String[] A_lagnappe_seq = request.getParameterValues("lagnappe_seq");
			   String[] A_lagnappe_no = request.getParameterValues("lagnappe_no");
			   //outPublisher
			   String[] A_books_seq = request.getParameterValues("books_seq");
			   
			   //保留儲存原來單科的科目名稱
			   String originSubjectName = "";
			   if(isCombo.equals("0")) {
				   originSubjectName = courseService.getSubject("",A_subject_seq[0].split("@")[0],"","","","0").get(0).getName();
			   }
	 
			   comboSale = new ComboSale("",isCombo,category_id,"",FlowStatus_code,"",comboSaleName,origin_price,sale_price,"",principal.getName(),"","",class_makeup,"",originSubjectName,favor_id,"0");		 
//Save via update				 
			   if(comboSale_seq != null && !comboSale_seq.isEmpty()) {
				   comboSale.setComboSale_seq(comboSale_seq);
				   courseSaleService.ComboSaleUpdate(comboSale,A_subject_seq,A_mock_seq,A_counseling_seq,A_lagnappe_seq,A_lagnappe_no,A_books_seq);
//Save via new					 
			   }else {
			       comboSale_seq = courseSaleService.ComboSaleSave(comboSale,A_subject_seq,A_mock_seq,A_counseling_seq,A_lagnappe_seq,A_lagnappe_no,A_books_seq,schedule_time,"0");
			   }    
		   }
		   
		   if(!FlowStatus_code.equals("7")) {
			   comboSale = courseSaleService.getComboSale(comboSale_seq,"","","","","","","0").get(0);
		   }	
		   managerService.applicationLogSave(principal.getName(),"3","-1","-1",col1,comboSale.getCategory_name(),comboSale.getName(),"",comboSale.getSale_price(),"");		   
		return true;
	}
	
	@RequestMapping("/CourseSale/SingleCourseSaleSave")
	public String SingleCourseSaleSave(HttpServletRequest request) {	
				
		SinCourseSale sinCourseSale = new SinCourseSale(
				"", 
				request.getParameter("SelectCategoryId"), 
				request.getParameter("SelectSubjectId"), 
				request.getParameter("originPrice"), 
				request.getParameter("salePrice"),
				request.getParameter("hrPrice"), 
				request.getParameter("counselingPrice"), 
				request.getParameter("lagnappePrice"), 
				request.getParameter("handoutPrice"), 
				request.getParameter("homeworkPrice"), 
				request.getParameter("mockPrice"), 
				request.getParameter("coursePrice"),
				"",
				"1"
		);
		String[] A_counseling_seq = request.getParameterValues("counseling_seq");
		courseSaleService.SingleCourseSaleSave(sinCourseSale,A_counseling_seq);
		
		return "/CourseSale/ComboSaleSetting";
	}
	
	
    @RequestMapping("/CourseSale/getComboSale")
    @ResponseBody
    public List<ComboSale> getComboSale(HttpServletRequest request,HttpSession session){
    	String scheduleOnly = request.getParameter("scheduleOnly");
    	String haveRead = request.getParameter("haveRead");
    	
    	String category_id = request.getParameter("category_id");
        if(category_id!=null) {
        	session.setAttribute("CourseController_category_id",category_id);
        }
        
    	String isCombo = request.getParameter("isCombo");
        if(isCombo!=null) {
        	session.setAttribute("CourseController_isCombo",isCombo);
        } 
        
    	String FlowStatus_code = request.getParameter("FlowStatus_code");
        if(FlowStatus_code!=null) {
        	session.setAttribute("CourseController_FlowStatus_code",FlowStatus_code);
        }  
        
        return courseSaleService.getComboSale("",category_id,isCombo,FlowStatus_code,scheduleOnly,"",haveRead,"0");
    }
    
    @RequestMapping("/CourseSale/getComboSale2")
    @ResponseBody
    public String getComboSale2(HttpServletRequest request){
    	String subjectSel = "";
    	String isCombo = request.getParameter("isCombo");
    	List<ComboSale> LComboSale = courseSaleService.getComboSale("",request.getParameter("category_id"),isCombo,request.getParameter("FlowStatus_code"),"","subject_id","","0");
        if(isCombo.equals("0")) {
	    	for(int i=0;i<LComboSale.size();i++) {
	    		String bgcolor="lavender";
	        	subjectSel +="<div onclick='AddComboSale("+LComboSale.get(i).getComboSale_seq()+",\"add\","+LComboSale.get(i).getIsCombo()+",this)' style='display:inline-block;border:1px #aaaaaa solid;border-radius:5px;background-color:"+bgcolor+";margin:5px;padding:0px;letter-spacing:2px;cursor:pointer;padding-left:3px;padding-right:3px'>"+LComboSale.get(i).getName()+"</div>";
	        	if(i!=0 && (i+1)%9==0) {
	        		subjectSel +="<br>";
	        	}
	        }
        }else if(isCombo.equals("1")) {
        	String comboName="";
	    	for(int i=0;i<LComboSale.size();i++) {
	    		if(LComboSale.get(i).getName().equals(comboName)) {
	    			subjectSel +="<div onclick='AddComboSale("+LComboSale.get(i).getComboSale_seq()+",\"add\","+LComboSale.get(i).getIsCombo()+",this)' style='display:inline-block;border:1px #aaaaaa solid;border-radius:5px;background-color:lightyellow;margin:5px;padding:0px;letter-spacing:2px;cursor:pointer;padding-left:3px;padding-right:3px;color:#bbbbbb'>"+LComboSale.get(i).getName()+"</div>";
	    		}else{
	    			comboName = LComboSale.get(i).getName();
	    			subjectSel +="<div onclick='AddComboSale("+LComboSale.get(i).getComboSale_seq()+",\"add\","+LComboSale.get(i).getIsCombo()+",this)' style='display:inline-block;border:1px #aaaaaa solid;border-radius:5px;background-color:lightyellow;margin:5px;padding:0px;letter-spacing:2px;cursor:pointer;padding-left:3px;padding-right:3px'>"+LComboSale.get(i).getName()+"</div>";	    			
	    		}
	        	if(i!=0 && (i+1)%9==0) {
	        		subjectSel +="<br>";
	        	}
	        }
        }	
    	return subjectSel;
    }    
 
    @RequestMapping("/CourseSale/ComboSaleContent")
    public String ComboSaleContent(Model model,HttpServletRequest request){
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	
		
    	List<ComboSale> LComboSale = courseSaleService.getComboSale(request.getParameter("comboSale_seq"),"","","","","","","0");
    	model.addAttribute("comboSale", LComboSale.get(0));
 
    	List<ComboSale_subject> LComboSale_subject = courseSaleService.getComboSale_subject(request.getParameter("comboSale_seq"),"");
    	model.addAttribute("comboSale_subject", LComboSale_subject);    	

    	List<ComboSale_mock> LComboSale_mock = courseSaleService.getComboSale_mock(request.getParameter("comboSale_seq"));
    	model.addAttribute("comboSale_mock", LComboSale_mock); 
    	
    	List<ComboSale_counseling> LComboSale_counseling = courseSaleService.getComboSale_counseling(request.getParameter("comboSale_seq"));
    	model.addAttribute("comboSale_counseling", LComboSale_counseling); 
    	return "/CourseSale/ComboSaleContent";    	
    } 
    
    @RequestMapping("/CourseSale/ComboSaleContentDetail")
    public String ComboSaleContentDetail(Model model,HttpServletRequest request){
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	
		
    	List<ComboSale> LComboSale = courseSaleService.getComboSale(request.getParameter("comboSale_seq"),"","","","","","","0");
    	model.addAttribute("comboSale", LComboSale.get(0));
 
    	List<ComboSale_subject> LComboSale_subject = courseSaleService.getComboSale_subject(request.getParameter("comboSale_seq"),"");
    	 
    	
    	List<Grade> LGrade = new ArrayList<Grade>();
    	for(int i=0;i<LComboSale_subject.size();i++) {   					
			/*
			 * if(LComboSale_subject.get(i).getSql_str()!=null &&
			 * !(LComboSale_subject.get(i).getSql_str().contentEquals(""))) {
			 * LComboSale_subject.get(i).
			 * setSubject_name("[ <A href='javascript:void(0)'  onclick='openFreeChoice("
			 * +LComboSale_subject.get(i).getSubject_id()
			 * +");' style='text-decoration: underline;'>"+LComboSale_subject.get(i).
			 * getSubject_name()+"</A> ]"); }
			 */
			 
    		LGrade= courseService.getGradeList("",LComboSale_subject.get(i).getSubject_id(),"","","","","","","","1","","","","","","","","1","");
    		String gradeStr = "";
    		for(int j=0;j<LGrade.size();j++) {
    			gradeStr += "<div class='tr' style='background-color:gold;'>"+
    					        "<div class='td' style='width:100px;background-color:#ffffff'></div><div style='background-color:#ffffff'><img src='/images/class.png' height='15px'/></div>"+			        
    					        "<div class='td' style='width:75px;text-align:center'>"+LGrade.get(j).getSchool_name()+"</div>"+
    					        "<div class='td' style='width:85px;text-align:center'><A href='javascript:void(0)'  onclick='gradeInfo();' style='text-decoration: underline;'>"+LGrade.get(j).getClass_start_date()+"期</A></div>"+    					        		        
    					        "<div class='td' style='width:100px;text-align:center'>"+LGrade.get(j).getTeacher_name()+"老師</div>"+
    					        "<div class='td' style='width:30px;text-align:center'>"+LGrade.get(j).getGradeName()+"班</div>"+	
    					        "<div class='td' style='width:80px;text-align:center'>"+LGrade.get(j).getStatus_name()+"</div>"+
    					    "</div>";    
    		}
    		LComboSale_subject.get(i).setGradeStr(gradeStr);
    	}
    	model.addAttribute("comboSale_subject", LComboSale_subject); 

    	List<ComboSale_material> LComboSale_material = courseSaleService.getComboSale_material(request.getParameter("comboSale_seq"));
    	model.addAttribute("comboSale_material", LComboSale_material); 
    	
    	List<ComboSale_mock> LComboSale_mock = courseSaleService.getComboSale_mock(request.getParameter("comboSale_seq"));
    	model.addAttribute("comboSale_mock", LComboSale_mock); 
    	
    	List<ComboSale_counseling> LComboSale_counseling = courseSaleService.getComboSale_counseling(request.getParameter("comboSale_seq"));
    	model.addAttribute("comboSale_counseling", LComboSale_counseling); 

    	List<ComboSale_lagnappe> LComboSale_lagnappe = courseSaleService.getComboSale_lagnappe(request.getParameter("comboSale_seq"));
    	model.addAttribute("comboSale_lagnappe", LComboSale_lagnappe);
    	
    	return "/CourseSale/ComboSaleContentDetail";    	
    }

    @RequestMapping("/CourseSale/ComboSaleAppend")
    @ResponseBody
    public String ComboSaleAppend(Model model,HttpServletRequest request){

    	String comboSale_seq = request.getParameter("comboSale_seq");
    	String freeChoice = request.getParameter("freeChoice");
    	//單科套裝基本資料		
    			ComboSale comboSale = courseSaleService.getComboSale(comboSale_seq,"","","","","","","0").get(0);
    	//單科所有資料
    			List<ComboSale_subject> LComboSale_subject = courseSaleService.getComboSale_subject(comboSale_seq,"");
    	//套裝充電站		
    			List<ComboSale_counseling> LComboSale_counseling = courseSaleService.getComboSale_counseling(comboSale_seq);
    			String counselingItem = "";
    			for(int i=0;i<LComboSale_counseling.size();i++) {
    				counselingItem +="&bull;<span>"+LComboSale_counseling.get(i).getCounseling_name()+"</span><br>";
    			}
    	//套裝贈品		
    			List<ComboSale_lagnappe> LComboSale_lagnappe = courseSaleService.getComboSale_lagnappe(comboSale_seq);
    			String lagnappeItem = "";
    			for(int i=0;i<LComboSale_lagnappe.size();i++) {
    				lagnappeItem +="&bull;<span>"+LComboSale_lagnappe.get(i).getLagnappe_name()+"*"+LComboSale_lagnappe.get(i).getLagnappe_no()+"</span><br>";
    			}
    	//套裝模考		
    			List<ComboSale_mock> LComboSale_mock = courseSaleService.getComboSale_mock(comboSale_seq);
    			String mockItem = "";
    			for(int i=0;i<LComboSale_mock.size();i++) {
    				mockItem +="&bull;<span>"+LComboSale_mock.get(i).getMock_name()+"</span><br>";
    			}	
    	//套裝外版書		
    			List<ComboSale_outPublisher> LComboSale_outPublisher = courseSaleService.getComboSale_outPublisher(comboSale_seq);
    			String outPublisherItem = "";
    			for(int i=0;i<LComboSale_outPublisher.size();i++) {
    				outPublisherItem +="<input type='hidden' name='outPublisher_seq' value='"+LComboSale_outPublisher.get(i).getBook_id()+"'>";
    				outPublisherItem +="<input type='hidden' class='outPublisherPrice' value='"+LComboSale_outPublisher.get(i).getSellPrice()+"'>";    				
    				outPublisherItem +="&bull;<span>"+LComboSale_outPublisher.get(i).getBook_name()+"</span><br>";
    			}    			
    	
    			String returnStr = "<div class='css-table' style='border-spacing:1px;width:1210px'>";

    				returnStr +=
    				"<div class='tr' style='font-size:small' id='comboSaleName'>"+
    				   "<div class='td2' style='width:200px;text-align:left;letter-spacing:1px'>";
    				        if(comboSale.getIsCombo().equals("1")) {
    				        	returnStr += "<A href='javascript:void(0)' title='刪除此筆'  onclick='Del(this);'><img src='/images/delete.png' height='10px'/></A>&nbsp;<b>["+(comboSale.getName()==null?"":comboSale.getName())+"]</b>";
    				        }else {
								if(comboSale.getName().equals(comboSale.getOriginSubjectName())) {
									returnStr += "<A href='javascript:void(0)' title='刪除此筆'  onclick='Del(this);'><img src='/images/delete.png' height='10px'/></A>&nbsp;<b>"+(comboSale.getCategory_name()==null?"":comboSale.getCategory_name())+"</b>";
								}else {
									returnStr += "<A href='javascript:void(0)' title='刪除此筆'  onclick='Del(this);'><img src='/images/delete.png' height='10px'/></A>&nbsp;<b>"+(comboSale.getName()==null?"":comboSale.getName())+"</b>";
								}	        	
    				        }
    				returnStr +=   "<A href='javascript:void(0)' title='換課 '  style=''  onclick='openExchange("+comboSale_seq+")'><img src='/images/exchange3.png' height='11px'/></A>"+
    				   "</div>";
    				
				    //DM售價 
					returnStr += "<div class='td2' style='padding:1px;width:50px'><input type='text'  value='"+comboSale.getSale_price()+"' style='font-weight:bold;background-color:#FEF8F8;width:100%;border:1px dotted #ffffff;text-align:right' readonly></div>";		      				
    				returnStr +=
    				"</div>";    				

//內含單科    	 	
    		for(int i=0;i<LComboSale_subject.size();i++) {
    			returnStr +=
				"<div class='tr com"+comboSale_seq+"' id='com"+comboSale_seq+"sub"+LComboSale_subject.get(i).getSubject_id()+"'  style='font-size:small;height:20px' >"+
		           "<div class='td2' style='padding:1px;width:250px;text-align:left'>"+
				       "<input type='hidden' name='freeChoice' value='"+freeChoice+"'>"+
		               "<input type='hidden' id='replaceFrom' name='replaceFrom' value=''>"+	
		               "<input type='hidden' name='comboSale_seq' value='"+comboSale_seq+"'>"+
		               "<input type='hidden' id='subject_seq' name='subject_seq' value='"+LComboSale_subject.get(i).getSubject_id()+"@"+LComboSale_subject.get(i).getHrPrice_R()+"@"+LComboSale_subject.get(i).getCounselingPrice_R()+"@"+LComboSale_subject.get(i).getLagnappePrice_R()+"@"+LComboSale_subject.get(i).getHandoutPrice_R()+"@"+LComboSale_subject.get(i).getHomeworkPrice_R()+"@"+LComboSale_subject.get(i).getMockPrice_R()+"'>"+	
		               "&nbsp;&nbsp;&nbsp;&nbsp;<span id='Subject_name' style='font-weight:bold;color:darkblue'>"+LComboSale_subject.get(i).getSubject_name()+"</span>";
					   if(freeChoice!=null && freeChoice.equals("2")) {
						   returnStr += "<span style='background-color:aquamarine;font-size:x-small;border:1px #aaaaaa solid;border-radius:3px;padding:1px'>贈</span>";
					   }
				   returnStr +=	   
		           "</div><div></div>";
				   

				   //科目原價
				   if(freeChoice!=null && freeChoice.equals("1")) {
			    	   returnStr += "<div class='td2' style='padding:1px;width:50px'><input type='text' class='originPrice' value='"+LComboSale_subject.get(i).getSubject_price()+"' style='background-color:#FEF8F8;width:100%;border:1px dotted #ffffff;text-align:right' readonly></div>";
			       }else if(freeChoice!=null && freeChoice.equals("2")) {
			    	   returnStr += "<div class='td2' style='padding:1px;width:50px'><input type='text' class='originPrice' value='0' style='background-color:#FEF8F8;width:100%;border:1px dotted #ffffff;text-align:right' readonly></div>";
			       }
		       
				   returnStr +=						   
				   "<div class='td2' style='padding:1px;width:50px'><input type='text' class='salePrice' name='salePrice' style='background-color:#FEF8F8;width:100%;border:0px;text-align:right' readonly></div>"+
		           "<div class='td2' style='padding:1px;width:50px'><input type='text' class='hrPrice'   style='background-color:#FEF8F8;width:100%;border:0px;text-align:right' readonly></div>"+		           
		           "<div class='td2' style='padding:1px;width:150px'><input type='text' class='counselingPrice' style='background-color:#FEF8F8;width:100%;border:0px;text-align:right' readonly /><div class='counselingName'>"+LComboSale_subject.get(i).getCounselingStr()+"</div></div>"+	
		           "<div class='td2' style='padding:1px;width:150px'><input type='text' class='lagnappePrice' style='background-color:#FEF8F8;width:100%;border:0px;text-align:right' readonly /><div class='lagnappeName'>"+LComboSale_subject.get(i).getLagnappeStr()+"</div></div>"+		           
		           "<div class='td2' style='padding:1px;width:50px'><input type='text' class='handoutPrice' style='background-color:#FEF8F8;width:100%;border:0px;text-align:right' readonly></div>"+
		           "<div class='td2' style='padding:1px;width:50px'><input type='text' class='homeworkPrice' style='background-color:#FEF8F8;width:100%;border:0px;text-align:right' readonly></div>"+
		           "<div class='td2' style='padding:1px;width:150px'><input type='text' class='mockPrice' style='background-color:#FEF8F8;width:100%;border:0px;text-align:right' readonly /><div class='mockName'>"+LComboSale_subject.get(i).getMockStr()+"</div></div>"+		           
		           "<div class='td2' style='padding:1px;width:50px'><input type='text' class='coursePrice' style='background-color:#FEF8F8;width:100%;border:0px;text-align:right' readonly></div>"+
		           "<div class='td2' style='padding:1px;width:150px'><input type='text' class='outPublisherPrice' style='background-color:#FEF8F8;width:100%;border:0px;text-align:right' readonly /><div class='outPublisherName'>"+LComboSale_subject.get(i).getOutPublisherStr()+"</div></div>"+	
		        "</div>";
    			}    			

    			
//套裝內含東西    			
    		//if(comboSale.getIsCombo().equals("1") && (!counselingItem.equals("") || !lagnappeItem.equals("") || !mockItem.equals(""))) {
    
    		if( (!counselingItem.equals("") || !lagnappeItem.equals("") || !mockItem.equals(""))) {
    			returnStr +=		
				 "<div class='tr' style='font-size:small;height:20px' >"+
		           "<div class='td2' style='padding:2px;width:250px'></div>"+    
		           "<div class='td2' style='padding:2px;width:50px'></div>"+
		           "<div class='td2' style='padding:2px;width:50px'></div>"+
				   "<div class='td2' style='padding:2px;width:50px'></div>"+ 
		           "<div class='td2' style='padding:2px;width:50px'></div>"+ 
		           "<div class='td2' style='padding:2px;width:150px;text-align:left'>"+counselingItem+"</div>"+ 
		           "<div class='td2' style='padding:2px;width:150px;text-align:left'>"+lagnappeItem+"</div>"+ 
		           "<div class='td2' style='padding:2px;width:50px'></div>"+ 
		           "<div class='td2' style='padding:2px;width:50px'></div>"+ 
		           "<div class='td2' style='padding:2px;width:150px;text-align:left'>"+mockItem+"</div>"+ 
		           "<div class='td2' style='padding:2px;width:50px'></div>"+ 
		           "<div class='td2' style='padding:2px;width:150px;text-align:left'>"+outPublisherItem+"</div>"+ 	
		         "</div>"+ 
		        "</div>";
    		}
    			  			
    	return returnStr;  
    }	
  

        
	@RequestMapping("/CourseSale/getComboSaleByCategory")
	@ResponseBody
	public String SubjectSearch(HttpServletRequest request) {	
		String returnOption = "<option></option>";
		List<ComboSale> LComboSale = courseSaleService.getComboSale("",request.getParameter("category_id"),"0","","","","","0");
		//List<Subject> subjectGroup = courseService.getSubject(request.getParameter("category_id"),"");
		for(ComboSale comboSale : LComboSale){
			returnOption += "<option value='"+comboSale.getComboSale_seq()+"'>"+comboSale.getName()+"</option>";
		}
		return returnOption;
	}    
    @RequestMapping("/CourseSale/openSelect")
    public String openSelect(Model model,HttpServletRequest request) {
    	
    	String category_id = request.getParameter("category_id");
    	model.addAttribute("category_id", category_id);
    	
    	String returnAreaId = request.getParameter("returnAreaId");
    	model.addAttribute("returnAreaId", returnAreaId);
    	
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	 
		
    	if(returnAreaId.contentEquals("counselingArea")) {
    		return "/CourseSale/openSelectCounseling";
    	}else if(returnAreaId.contentEquals("lagnappeArea")) {
    		return "/CourseSale/openSelectLagnappe";
    	}else if(returnAreaId.contentEquals("outPublisherArea")) {
    		return "/CourseSale/openSelectOutPublisher";    		
    	}else { //mockArea   		
    		return "/CourseSale/openSelectMock";
    	}
    }     
    
    @RequestMapping("/CourseSale/getCounseling")
    @ResponseBody
    public String getCounseling(Model model,HttpServletRequest request) { 
    	List<Counseling> LCounseling = courseService.getCounseling(request.getParameter("category_id"),"","1");
    	String returnOption = "";
    	for(Counseling counseling : LCounseling){
			returnOption += "<option value='"+counseling.getCounseling_seq()+"'>"+counseling.getCounseling_name()+"</option>";
		}  
    	return returnOption;
    }
    
    @RequestMapping("/CourseSale/getCounseling2")
    @ResponseBody
    public List<Counseling> getCounseling2(Model model,HttpServletRequest request) { 
    	List<Counseling> LCounseling = courseService.getCounseling(request.getParameter("category_id"),"",request.getParameter("active"));  
    	return LCounseling;
    }    
    
    @RequestMapping("/CourseSale/getLagnappe")
    @ResponseBody
    public String getLagnappe(Model model,HttpServletRequest request) { 
		List<Lagnappe> LLagnappe = courseSaleService.getLagnappe("","1");
		
    	String returnOption = "";
    	for(Lagnappe lagnappe : LLagnappe){
			returnOption += "<option value='"+lagnappe.getLagnappe_seq()+"'>"+lagnappe.getLagnappe_name()+"</option>";
		}  
    	return returnOption;
    }
    
    @RequestMapping("/CourseSale/getOutPublisher")
    @ResponseBody
    public String getOutPublisher(Model model,HttpServletRequest request) { 
		List<Books> LBooks = admService.getBooks(request.getParameter("category_id"),"","1");
		
    	String returnOption = "";
    	for(int i=0;i<LBooks.size();i++){
			returnOption += "<option value='"+LBooks.get(i).getBooks_seq()+"@"+LBooks.get(i).getSellPrice()+"'>"+LBooks.get(i).getBookName()+" "+LBooks.get(i).getSellPrice()+"元</option>";
		}  
    	return returnOption;
    }    
    
    @RequestMapping("/CourseSale/getMock")
    @ResponseBody
    public String getMock(Model model,HttpServletRequest request) { 
    	List<Mock> LMock = courseService.getMock(request.getParameter("category_id"),"","1");
    	String returnOption = "";
    	for(Mock mock : LMock){
			returnOption += "<option value='"+mock.getMock_seq()+"'>"+mock.getMock_name()+"</option>";
		}  
    	return returnOption;
    }    
    
    @RequestMapping("/CourseSale/editComboSale")
    public String editComboSale(Model model,HttpServletRequest request){
		if(request.getParameter("haveRead")!=null && request.getParameter("haveRead").equals("1") && request.getParameter("comboSale_seq")!=null) {
			courseService.updateComboSaleHaveRead(request.getParameter("comboSale_seq"));
		}
		
		List<Category> categoryGroup = courseService.getCategory("","");
		model.addAttribute("categoryGroup", categoryGroup);	
		String comboSale_seq = request.getParameter("comboSale_seq");

//單科套裝基本資料		
		ComboSale comboSale = courseSaleService.getComboSale(comboSale_seq,"","","","","","","0").get(0);
		model.addAttribute("comboSale", comboSale);	
//單科所有資料
		List<ComboSale_subject> LComboSale_subject = courseSaleService.getComboSale_subject(comboSale_seq,"");
		model.addAttribute("LComboSale_subject", LComboSale_subject);

//套裝或單科外掛的其他增加物 		
	        //套裝充電站		
			List<ComboSale_counseling> LComboSale_counseling = courseSaleService.getComboSale_counseling(comboSale_seq);
			model.addAttribute("LComboSale_counseling", LComboSale_counseling);
	        //套裝贈品		
			List<ComboSale_lagnappe> LComboSale_lagnappe = courseSaleService.getComboSale_lagnappe(comboSale_seq);
			model.addAttribute("LComboSale_lagnappe", LComboSale_lagnappe);
	        //套裝模考		
			List<ComboSale_mock> LComboSale_mock = courseSaleService.getComboSale_mock(comboSale_seq);
			model.addAttribute("LComboSale_mock", LComboSale_mock);
	        //套裝外版書		
			List<ComboSale_outPublisher> LComboSale_outPublisher = courseSaleService.getComboSale_outPublisher(comboSale_seq);
			model.addAttribute("LComboSale_outPublisher", LComboSale_outPublisher);			
		
    	model.addAttribute("action",request.getParameter("action"));
	    return "/CourseSale/editComboSale";    			
    }     	
  
	@RequestMapping("/CourseSale/GetSubjectComboSeq")
	@ResponseBody
	public String GetSubjectComboSeq(HttpServletRequest request) {	
		String returnOption = "<option></option>";
		
		List<ComboSale_subject> LComboSale_subject = courseSaleService.GetSubjectComboSeq(request.getParameter("category_id"),"","");
		
		for(ComboSale_subject comboSale_subject : LComboSale_subject){
			
			returnOption += "<option value='"+comboSale_subject.getComboSale_id()+"'>"+comboSale_subject.getSubject_name()+"</option>";
		}
		return returnOption;
	}	    

		
	@RequestMapping("/CourseSale/CounselingSettingLimit")
	public String CounselingSettingLimit(Model model,HttpServletRequest request) {
    	List<ComboSale_subject> LComboSale_subject = new ArrayList<ComboSale_subject>();
    	String comboSale_seq = request.getParameter("comboSale_seq");
    	if(comboSale_seq!=null) {
    		LComboSale_subject = courseSaleService.getComboSale_subject(comboSale_seq,"");
    	}
    	String comboSale_subjectStr = "";
    	for(int i=0;i<LComboSale_subject.size();i++) {
    		comboSale_subjectStr += "<div>&nbsp;<input type='checkbox' id='subject_id' name='subject_id' value='"+LComboSale_subject.get(i).getSubject_id()+"'>&nbsp;"+LComboSale_subject.get(i).getSubject_name()+"</div>";
    	}
    	model.addAttribute("comboSale_subjectStr",comboSale_subjectStr); 
    	model.addAttribute("comboSale_seq",comboSale_seq);
		return "/CourseSale/CounselingSettingLimit";
	}

    @RequestMapping("/CourseSale/getCounselingLimit")
    @ResponseBody
    public List<CounselingLimit> getCounselingLimit(HttpServletRequest request){
    	List<CounselingLimit> LCounselingLimit = courseSaleService.getCounselingLimit(request.getParameter("comboSale_seq"));
    	for(int i=0;i<LCounselingLimit.size();i++) {
    		String subjectNameStr = "";
    		List<CounselingLimitSubject> LCounselingLimitSubject = courseSaleService.getCounselingLimitSubject(LCounselingLimit.get(i).getCounselingLimit_seq());
    		for(int j=0;j<LCounselingLimitSubject.size();j++) {
    			subjectNameStr += LCounselingLimitSubject.get(j).getSubject_name();
    			if(j!=LCounselingLimitSubject.size()-1) {subjectNameStr += " &#10010; ";}
    		}
    		LCounselingLimit.get(i).setSubjectNameStr(subjectNameStr);
    	}
        return LCounselingLimit;
    }
    
	@RequestMapping("/CourseSale/CounselingSettingSave")
	public String CounselingSettingSave(HttpServletRequest request,Principal principal) {	
		String comboSale_id = request.getParameter("comboSale_seq");	
		String counselingLimitClass = request.getParameter("counselingLimitClass");	
		String[] A_subject_id = request.getParameterValues("subject_id");
		String creater = principal.getName();
		
		courseSaleService.CounselingSettingSave(comboSale_id,counselingLimitClass,A_subject_id,creater);
		
		return "redirect:/CourseSale/CounselingSettingLimit?comboSale_seq="+comboSale_id;
	}
	
	
	@RequestMapping("/CourseSale/CounselingCostEdit")
	public String CounselingCostEdit(HttpServletRequest request,Model model) {
		List<Category> LCategory = courseService.getCategory("","");
		model.addAttribute("LCategory",LCategory);		
		String counseling_seq = request.getParameter("counseling_seq");
		Counseling  counseling = new Counseling();
		if(counseling_seq!=null && !counseling_seq.isEmpty()) {
			List<Counseling> LCounseling= courseService.getCounseling("",counseling_seq,"");
			if(LCounseling.size()>0) {
				counseling = courseService.getCounseling("",counseling_seq,"").get(0);
			}
		}	
		model.addAttribute("counseling", counseling);				
		return "/CourseSale/CounselingCostEdit";
	}	
	
    @RequestMapping("/CourseSale/CounselingCostSave")
    public String CounselingCostSave(HttpServletRequest request,@ModelAttribute Counseling counseling,Principal principal,RedirectAttributes redirectAttributes) {
        String counseling_seq = request.getParameter("counseling_seq");
    	courseSaleService.counselingCostSave(counseling,counseling_seq);
    	return "redirect:/CourseSale/CounselingCost";
    }	
}    
