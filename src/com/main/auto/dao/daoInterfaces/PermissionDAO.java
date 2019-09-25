package com.main.auto.dao.daoInterfaces;

import com.main.auto.model.Permission;

public interface PermissionDAO extends DAO<Permission> {
	
	Permission getByPermissionRole (String permission); 
}
