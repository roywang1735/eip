package com.wordgod.eip.Controller;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
  //HTTP 驗證規則	
  @Override
  protected void configure(HttpSecurity http) throws Exception {
	  
  http.sessionManagement().invalidSessionUrl("/login?logout=true");	  
 	  
  http
    .authorizeRequests() 
     
    
    //報表
    .antMatchers("/images/excel/report/**").hasAnyRole("approve_mgr")
                  
    //系統分析
    .antMatchers("/System/open/**").hasAnyRole("mis","mis_mgr","adm","adm_mgr","sal","sal_mgr","approve_mgr","cou","cou_mgr")
    //學生列表
	.antMatchers("/CourseSale/getComboSale2").hasAnyRole("sal","sal_mgr","approve_mgr")	
	.antMatchers("/CourseSale/ComboSaleAppend").hasAnyRole("sal","sal_mgr","approve_mgr")
	.antMatchers("/Course/existVideo").hasAnyRole("sal","sal_mgr","approve_mgr")
	.antMatchers("/Adm/RegisterStatusUpdate").hasAnyRole("sal","sal_mgr","approve_mgr")
	
 
	//學生歷程
	.antMatchers("/Sales/studentHistory").hasAnyRole("adm","adm_mgr","sal","sal_mgr","approve_mgr")
	.antMatchers("/Sales/getStudentExperience").hasAnyRole("adm","adm_mgr","sal","sal_mgr","approve_mgr")
	.antMatchers("/Sales/RegisterCombo").hasAnyRole("adm","adm_mgr","sal","sal_mgr","approve_mgr")
	//學生基本資料
	.antMatchers("/Sales/Student").hasAnyRole("adm","adm_mgr","cou","cou_mgr","sal","sal_mgr","approve_mgr")
	.antMatchers("/Sales/StudentProfile").hasAnyRole("adm","adm_mgr","cou","cou_mgr","sal","sal_mgr","approve_mgr")
	.antMatchers("/Sales/checkStudentProfile_DirtyRead").hasAnyRole("adm","adm_mgr","cou","cou_mgr","sal","sal_mgr","approve_mgr")
	.antMatchers("/Sales/StudentSave").hasAnyRole("adm","adm_mgr","cou","cou_mgr","sal","sal_mgr","approve_mgr")
	.antMatchers("/Sales/openStudentRecord").hasAnyRole("adm","adm_mgr","cou","cou_mgr","sal","sal_mgr","approve_mgr")
	.antMatchers("/Sales/getStudentRecord").hasAnyRole("adm","adm_mgr","cou","cou_mgr","sal","sal_mgr","approve_mgr")
	.antMatchers("/Sales/openHandover").hasAnyRole("adm","adm_mgr","cou","cou_mgr","sal","sal_mgr","approve_mgr")
	
	
	
	//課務-現場課表
	.antMatchers("/Sales/getCurrentCourse").hasAnyRole("adm","adm_mgr","sal","sal_mgr","cou","cou_mgr","approve_mgr")
	//課務-講座進班試聽
	.antMatchers("/Marketing/ClassTrial").hasAnyRole("design","design_mgr","mkt","mkt_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Marketing/getClassTrial").hasAnyRole("design","design_mgr","mkt","mkt_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Marketing/ClassTrialEdit").hasAnyRole("design","design_mgr","mkt","mkt_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Marketing/ClassTrialEditSave").hasAnyRole("design","design_mgr","mkt","mkt_mgr","cou","cou_mgr","approve_mgr")

	//課務-講座進班試聽-講座
	.antMatchers("/Marketing/LectureSetting2").hasAnyRole("design","design_mgr","mkt","mkt_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Marketing/getLectureGrade").hasAnyRole("design","design_mgr","mkt","mkt_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Marketing/LectureFlowEdit").hasAnyRole("design","design_mgr","mkt","mkt_mgr","cou","cou_mgr","approve_mgr")
	
	//課務-講座進班試聽-班級人數
	.antMatchers("/Marketing/Course").hasAnyRole("mkt","mkt_mgr","cou","cou_mgr","approve_mgr")
	
    //老師課表
	.antMatchers("/Course/TeacherSetting2").hasAnyRole("mkt","mkt_mgr","adm","adm_mgr","approve_mgr")
	.antMatchers("/Course/Calendar_Grade").hasAnyRole("teacher","cou","cou_mgr","mkt","mkt_mgr","adm","adm_mgr","approve_mgr")
	//行政模考設置
	.antMatchers("/Adm/openTitleLink").hasAnyRole("sal","sal_mgr","adm","adm_mgr","approve_mgr")
	//行政行事曆
	.antMatchers("/Course/getGradeList").hasAnyRole("adm","adm_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Course/newCourse").hasAnyRole("adm","adm_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Course/classReceive").hasAnyRole("adm","adm_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Course/getClassRoom").hasAnyRole("adm","adm_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Course/getProgramWait").hasAnyRole("adm_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Course/gradeCommentSave").hasAnyRole("adm_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Adm/classRemark").hasAnyRole("adm_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Adm/programSettingWait").hasAnyRole("adm_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Adm/admCalendarEdit").hasAnyRole("adm","adm_mgr","approve_mgr")
	//行政點名	
	.antMatchers("/Course/getTodayGradeList").hasAnyRole("adm","adm_mgr","cou","cou_mgr","approve_mgr")
	
	//行政訊息發布	
	.antMatchers("/Sales/getStudent").hasAnyRole("sal","sal_mgr","adm","adm_mgr","cou","cou_mgr","approve_mgr")
	
	//業務進班
	.antMatchers("/Adm/editSignRecord").hasAnyRole("sal","sal_mgr","adm","adm_mgr","approve_mgr")
	.antMatchers("/Adm/manualSignRecord").hasAnyRole("sal","sal_mgr","adm","adm_mgr","approve_mgr")
	
	//業務班級人數
	.antMatchers("/Course/getGradeList2").hasAnyRole("adm","adm_mgr","mkt","mkt_mgr","sal","sal_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Course/openGradeStudent").hasAnyRole("adm","adm_mgr","mkt","mkt_mgr","sal","sal_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Course/openGradeStudent2").hasAnyRole("adm","adm_mgr","mkt","mkt_mgr","sal","sal_mgr","cou","cou_mgr","approve_mgr")
	.antMatchers("/Course/getGradeStudent2").hasAnyRole("adm","adm_mgr","mkt","mkt_mgr","sal","sal_mgr","cou","cou_mgr","approve_mgr")
	
    //建置講座
	.antMatchers("/Marketing/LectureSetting").hasAnyRole("cou","cou_mgr","mkt","mkt_mgr","approve_mgr") 
	.antMatchers("/Marketing/getLectureList").hasAnyRole("cou","cou_mgr","mkt","mkt_mgr","approve_mgr")
	.antMatchers("/Marketing/LectureEdit").hasAnyRole("cou","cou_mgr","mkt","mkt_mgr","approve_mgr")
	.antMatchers("/Marketing/LectureCalendar").hasAnyRole("cou","cou_mgr","mkt","mkt_mgr","approve_mgr")
	.antMatchers("/Marketing/LectureEditSave").hasAnyRole("cou","cou_mgr","mkt","mkt_mgr","approve_mgr")
	
	
    //業務(換課)
    .antMatchers("/Course/getSubjectOption").hasAnyRole("cou","cou_mgr","approve_mgr","sal","sal_mgr")
    //業務(合約書)
    .antMatchers("/System/openContract").hasAnyRole("adm","adm_mgr","mis","mis_mgr","approve_mgr","sal","sal_mgr")
    .antMatchers("/System/serviceContractSave").hasAnyRole("adm","adm_mgr","mis","mis_mgr","approve_mgr","sal","sal_mgr")
    
   
    
    
    
    //*******通則權限***********//
    //老師
    .antMatchers("/Teacher/**").hasAnyRole("teacher")  
    
    
    .antMatchers("/Sales/**").hasAnyRole("adm","adm_mgr","sal","sal_mgr","approve_mgr")    
    .antMatchers("/Adm/**").hasAnyRole("adm","adm_mgr","approve_mgr")
    .antMatchers("/Marketing/**").hasAnyRole("mkt","mkt_mgr","approve_mgr")    
    .antMatchers("/Course/**").hasAnyRole("cou","cou_mgr","approve_mgr")
         
    .antMatchers("/Manager/**").hasAnyRole("approve_mgr")    
    .antMatchers("/System/**").hasAnyRole("mis","mis_mgr")
    
    .and()
    
    .formLogin()
    .loginPage("/login").failureUrl("/login-error")
    .usernameParameter("username").passwordParameter("password")
    
    .and()
    
    .logout()
    .logoutSuccessUrl("/login?logout=true")
    .invalidateHttpSession(true)
    .permitAll()  
    
    .and()   
    .exceptionHandling().accessDeniedPage("/noAuthority");
 
   }

   @Autowired
   private DataSource dataSource;
 
   
   //驗證組件 AuthenticationManagerBuilder
   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth
     .jdbcAuthentication()
     .passwordEncoder(passwordEncoder())
     .dataSource(dataSource)
     .usersByUsernameQuery(
    	    "select account0 as username,drowssap as password, enabled from eip.employee where account0=?")
     .authoritiesByUsernameQuery(
    	    "select account0 as username, authority from eip.account_authority where account0=?");
 }
 
 
 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

}
