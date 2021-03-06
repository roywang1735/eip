package com.wordgod.eip.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.wordgod.eip.Model.*;
import com.wordgod.eip.Service.AccountService;
import com.wordgod.eip.Service.AdmService;
import com.wordgod.eip.Service.CourseSaleService;
import com.wordgod.eip.Service.CourseService;
import com.wordgod.eip.Service.MarketingService;
import com.wordgod.eip.Service.SalesService;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class TeacherController {

	@Autowired
	CourseService courseService;
	@Autowired
	AccountService accountService;	
	@Autowired
	SalesService salesService;
	@Autowired
	CourseSaleService courseSaleService;	
    @Autowired    
    MarketingService marketingService;
    @Autowired
    AdmService admService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
		 @RequestMapping("/Teacher/myCalendar")
		 public String myCalendar(Model model,Principal principal,HttpSession session,HttpServletRequest request) {
				String menu = request.getParameter("menu");
				if(menu!=null && !menu.isEmpty()) {session.setAttribute("menu",menu);}
				

				List<Teacher> LTeacher = courseService.getTeacher2("1",principal.getName());
				if(LTeacher.size()>0) {
					model.addAttribute("teacher_id", LTeacher.get(0).getTeacher_seq());
				}	
				
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
		     return "/Teacher/myCalendar";
		 }	    
}