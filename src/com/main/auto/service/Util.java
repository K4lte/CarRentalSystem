package com.main.auto.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.main.auto.model.Car;
import com.main.auto.service.ConfigurationManager;

public final class Util {

	public static void  getTotalPrice(int period, Car car) {		    
		BigDecimal price = car.getRentalPrice().multiply(new BigDecimal(period));
		car.setTotalRental(price);				    
	}
	
	public static int getDifferenceDays(Date dateStart, Date dateEnd) {
	    long difference = dateEnd.getTime() - dateStart.getTime();
	    int days = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
	    return days;
	}
	
	public static String getLocale(HttpServletRequest httpRequest) {
		Cookie[] cookies = httpRequest.getCookies();
		String locale = null; 						
		for(int i = 0; i < cookies.length; i++) { 
			if (cookies[i].getName().equals("locale")) {
				locale = cookies[i].getValue();
			} else {
				locale = "en";
			}
		}
		return locale;  		
	}
	
	public static void setErrorMessage(HttpServletRequest httpRequest, String errorName, String propertyName) {
		String locale = getLocale(httpRequest);	
		String property = locale.equals("ru") ? "errMsg_locale_ru" : "errMsg_locale_en";
		
		JsonObject message = new JsonObject();
		message.addProperty("errorName", errorName);
		message.addProperty("errorMessage", ConfigurationManager.getProperty(property, propertyName));	
		
		Gson gson = new Gson();
		httpRequest.setAttribute("errMsg", gson.toJson(message));
	}

}
