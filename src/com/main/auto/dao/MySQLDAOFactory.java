package com.main.auto.dao;

import com.main.auto.dao.MySQLDAO.*;
import com.main.auto.dao.MySQLDAO.MySQLCarBrandDAO;
import com.main.auto.dao.MySQLDAO.MySQLCarCategoryDAO;
import com.main.auto.dao.MySQLDAO.MySQLCarColorDAO;
import com.main.auto.dao.MySQLDAO.MySQLCarDAO;
import com.main.auto.dao.MySQLDAO.MySQLCarDamageDAO;
import com.main.auto.dao.MySQLDAO.MySQLCarTransmissionDAO;
import com.main.auto.dao.MySQLDAO.MySQLCityDAO;
import com.main.auto.dao.MySQLDAO.MySQLClientDAO;
import com.main.auto.dao.MySQLDAO.MySQLCountryDAO;
import com.main.auto.dao.MySQLDAO.MySQLDamageTypeDAO;
import com.main.auto.dao.MySQLDAO.MySQLEmployeeUserDAO;
import com.main.auto.dao.MySQLDAO.MySQLLocationDAO;
import com.main.auto.dao.MySQLDAO.MySQLOfficeDAO;
import com.main.auto.dao.MySQLDAO.MySQLPaymentDAO;
import com.main.auto.dao.MySQLDAO.MySQLCreditCardTypeDAO;
import com.main.auto.dao.MySQLDAO.MySQLPermissionDAO;
import com.main.auto.dao.MySQLDAO.MySQLReservationDAO;

//import java.sql.Connection;

public class MySQLDAOFactory extends DAOFactory {

    public MySQLDAOFactory(){
    }

    @Override
    public AccessoryDAO getAccessoryDAO() {
        return new MySQLAccessoryDAO();
    }

    @Override
    public AccessoryTypeDAO getAccessoryTypeDAO() {
        return new MySQLAccessoryTypeDAO();
    }

    @Override
    public CarDAO getCarDAO() {
        return new MySQLCarDAO();
    }

    @Override
    public CarBrandDAO getCarBrandDAO() {
        return new MySQLCarBrandDAO();
    }

    @Override
    public CarCategoryDAO getCarCategoryDAO() {
        return new MySQLCarCategoryDAO();
    }

    @Override
    public CarColorDAO getCarColorDAO() {
        return new MySQLCarColorDAO();
    }

    @Override
    public CarDamageDAO getCarDamageDAO() {
        return new MySQLCarDamageDAO();
    }

    @Override
    public CarTransmissionDAO getCarTransmissionDAO() {
        return new MySQLCarTransmissionDAO();
    }

    @Override
    public CityDAO getCityDAO() {
        return new MySQLCityDAO();
    }

    @Override
    public ClientDAO getClientDAO() {
        return new MySQLClientDAO();
    }
    
    @Override
    public CountryDAO getCountryDAO() {
        return new MySQLCountryDAO();
    }

    @Override
    public DamageTypeDAO getDamageTypeDAO() {
        return new MySQLDamageTypeDAO();
    }

    @Override
    public EmployeeUserDAO getEmployeeUserDAO() {
        return new MySQLEmployeeUserDAO();
    }

    @Override
    public LocationDAO getLocationDAO() {
        return new MySQLLocationDAO();
    }

    @Override
    public OfficeDAO getOfficeDAO() {
        return new MySQLOfficeDAO();
    }

    @Override
    public PaymentDAO getPaymentDAO() {
        return new MySQLPaymentDAO();
    }

	@Override
	public CreditCardTypeDAO getCreditCardTypeDAO() {
		return new MySQLCreditCardTypeDAO();
	}
	
    @Override
    public PermissionDAO getPermissionDAO() {
        return new MySQLPermissionDAO();
    }
	
    @Override
    public ReservationDAO getReservationDAO() {
        return new MySQLReservationDAO();
    }

    @Override
    public ReservationStatusDAO getReservationStatusDAO() {
    	return new MySQLReservationStatusDAO();
    }
    
	@Override
	public ReservationStatusCombsDAO getReservationStatusCombsDAO() {
		return new MySQLReservationStatusCombsDAO();
	}
}

