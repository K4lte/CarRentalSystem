package com.main.auto.dao;

import com.main.auto.model.Client;

public interface ClientDAO extends DAO<Client> {

    Client login(String login, String password);
    
    Client getByPassportNumber(String pasNumber);

}
