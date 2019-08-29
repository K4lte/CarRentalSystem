package com.main.auto.dao;

import com.main.auto.model.EmployeeUser;

public interface EmployeeUserDAO extends DAO<EmployeeUser> {
	EmployeeUser login(String login, String password);
}
