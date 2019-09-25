package com.main.auto.dao.daoInterfaces;

import java.util.List;

import com.main.auto.model.Location;

public interface LocationDAO extends DAO<Location> {
	
	List<Location> getByCityId(int cityId);
}
