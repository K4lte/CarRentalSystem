package com.main.auto.dao.daoInterfaces;

import java.util.List;

import com.main.auto.model.City;

public interface CityDAO extends DAO<City> {
	List<City> getOfficiesCities();
}
