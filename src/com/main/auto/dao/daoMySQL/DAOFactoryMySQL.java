package com.main.auto.dao.daoMySQL;

import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.daoInterfaces.AccessoryDAO;
import com.main.auto.dao.daoInterfaces.AccessoryTypeDAO;
import com.main.auto.dao.daoInterfaces.CarBrandDAO;
import com.main.auto.dao.daoInterfaces.CarCategoryDAO;
import com.main.auto.dao.daoInterfaces.CarColorDAO;
import com.main.auto.dao.daoInterfaces.CarDAO;
import com.main.auto.dao.daoInterfaces.CarDamageDAO;
import com.main.auto.dao.daoInterfaces.CarTransmissionDAO;
import com.main.auto.dao.daoInterfaces.CityDAO;
import com.main.auto.dao.daoInterfaces.ClientDAO;
import com.main.auto.dao.daoInterfaces.CountryDAO;
import com.main.auto.dao.daoInterfaces.CreditCardTypeDAO;
import com.main.auto.dao.daoInterfaces.DamageTypeDAO;
import com.main.auto.dao.daoInterfaces.EmployeeUserDAO;
import com.main.auto.dao.daoInterfaces.LocationDAO;
import com.main.auto.dao.daoInterfaces.OfficeDAO;
import com.main.auto.dao.daoInterfaces.PaymentDAO;
import com.main.auto.dao.daoInterfaces.PermissionDAO;
import com.main.auto.dao.daoInterfaces.ReservationDAO;
import com.main.auto.dao.daoInterfaces.ReservationStatusCombsDAO;
import com.main.auto.dao.daoInterfaces.ReservationStatusDAO;

//import java.sql.Connection;

public class DAOFactoryMySQL extends DAOFactory {

    public DAOFactoryMySQL(){
    }

    @Override
    public AccessoryDAO getAccessoryDAO() {
        return new AccessoryDAOMySQL();
    }

    @Override
    public AccessoryTypeDAO getAccessoryTypeDAO() {
        return new AccessoryTypeDAOMySQL();
    }

    @Override
    public CarDAO getCarDAO() {
        return new CarDAOMySQL();
    }

    @Override
    public CarBrandDAO getCarBrandDAO() {
        return new CarBrandDAOMySQL();
    }

    @Override
    public CarCategoryDAO getCarCategoryDAO() {
        return new CarCategoryDAOMySQL();
    }

    @Override
    public CarColorDAO getCarColorDAO() {
        return new CarColorDAOMySQL();
    }

    @Override
    public CarDamageDAO getCarDamageDAO() {
        return new CarDamageDAOMySQL();
    }

    @Override
    public CarTransmissionDAO getCarTransmissionDAO() {
        return new CarTransmissionDAOMySQL();
    }

    @Override
    public CityDAO getCityDAO() {
        return new CityDAOMySQL();
    }

    @Override
    public ClientDAO getClientDAO() {
        return new ClientDAOMySQL();
    }
    
    @Override
    public CountryDAO getCountryDAO() {
        return new CountryDAOMySQL();
    }

    @Override
    public DamageTypeDAO getDamageTypeDAO() {
        return new DamageTypeDAOMySQL();
    }

    @Override
    public EmployeeUserDAO getEmployeeUserDAO() {
        return new EmployeeUserDAOMySQL();
    }

    @Override
    public LocationDAO getLocationDAO() {
        return new LocationDAOMySQL();
    }

    @Override
    public OfficeDAO getOfficeDAO() {
        return new OfficeDAOMySQL();
    }

    @Override
    public PaymentDAO getPaymentDAO() {
        return new PaymentDAOMySQL();
    }

	@Override
	public CreditCardTypeDAO getCreditCardTypeDAO() {
		return new CreditCardTypeDAOMySQL();
	}
	
    @Override
    public PermissionDAO getPermissionDAO() {
        return new PermissionDAOMySQL();
    }
	
    @Override
    public ReservationDAO getReservationDAO() {
        return new ReservationDAOMySQL();
    }

    @Override
    public ReservationStatusDAO getReservationStatusDAO() {
    	return new ReservationStatusDAOMySQL();
    }
    
	@Override
	public ReservationStatusCombsDAO getReservationStatusCombsDAO() {
		return new ReservationStatusCombsDAOMySQL();
	}
}

