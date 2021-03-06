package com.wordgod.eip.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToWeek {	
	public static String getDateToWeek(String dateStr){		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", java.util.Locale.TAIWAN);
		Date myDate = null;
		try {
			myDate = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sdf.applyPattern("EEE");
		String sMyDate = sdf.format(myDate);
		return "("+sMyDate.replace("星期","")+")";
	}	
}

