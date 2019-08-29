package com.main.auto.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import com.main.auto.model.Car;

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
	
	
}
