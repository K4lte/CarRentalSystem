package com.main.auto.dao.daoInterfaces;

import com.main.auto.model.Office;

public interface OfficeDAO extends DAO<Office> {
	
	Office getByLocationId(int locationId);
	
}
