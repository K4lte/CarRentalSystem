package com.main.auto.dao;

public interface DAOAuthorization <T> {
	
  T login(String login, String password);

}
