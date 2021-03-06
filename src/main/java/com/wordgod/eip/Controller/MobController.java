package com.wordgod.eip.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.wordgod.eip.Model.*;
import com.wordgod.eip.Service.AccountService;
import com.wordgod.eip.Service.AdmService;
import com.wordgod.eip.Service.CourseSaleService;
import com.wordgod.eip.Service.CourseService;
import com.wordgod.eip.Service.MarketingService;
import com.wordgod.eip.Service.SalesService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class MobController {

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


		
    @RequestMapping(value="/Mob/home")
    public String home() {
        return "/Mob/home";
    }
    
    @RequestMapping(value="/Mob/home1")
    public String home1() {
        return "/Mob/home1";
    }   
    
    @RequestMapping(value="/Mob/booter")
    public String booter() {
        return "/Mob/booter";
    }   
    
    @RequestMapping(value="/Mob/CourseSetting")
    public String CourseSetting() {
        return "/Mob/CourseSetting";
    }      
}