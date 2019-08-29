package com.main.auto.dao;

import java.sql.Date;
import java.util.List;

import com.main.auto.model.Car;

public interface CarDAO extends DAO<Car> {
	
	List<Car> searchByLocationDate(int locationCityId,  Date pickUpDate, Date dropOffDate);
	
	//VIN
	Car getByNumber(String number);
	
}
