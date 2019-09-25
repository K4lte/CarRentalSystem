package com.main.auto.dao;

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
import com.main.auto.dao.daoMySQL.DAOFactoryMySQL;

public abstract class DAOFactory {

    public static DAOFactory getDAOFactory(DBType whichFactory) {
        switch (whichFactory) {
            case MYSQL:
                return new DAOFactoryMySQL();
            default:
                return null;
        }
    }

    public abstract AccessoryDAO getAccessoryDAO();

    public abstract AccessoryTypeDAO getAccessoryTypeDAO();

    public abstract CarBrandDAO getCarBrandDAO();

    public abstract CarCategoryDAO getCarCategoryDAO();

    public abstract CarColorDAO getCarColorDAO();

    public abstract CarDamageDAO getCarDamageDAO();

    public abstract CarDAO getCarDAO();

    public abstract CarTransmissionDAO getCarTransmissionDAO();

    public abstract CityDAO getCityDAO();

    public abstract ClientDAO getClientDAO();
    
    public abstract CountryDAO getCountryDAO();

    public abstract DamageTypeDAO getDamageTypeDAO();

    public abstract EmployeeUserDAO getEmployeeUserDAO();

    public abstract LocationDAO getLocationDAO();

    public abstract OfficeDAO getOfficeDAO();

    public abstract PaymentDAO getPaymentDAO();
    
    public abstract CreditCardTypeDAO getCreditCardTypeDAO();
    
    public abstract PermissionDAO getPermissionDAO();

    public abstract ReservationDAO getReservationDAO();

    public abstract ReservationStatusDAO getReservationStatusDAO(); 
    
    public abstract ReservationStatusCombsDAO getReservationStatusCombsDAO();
}
