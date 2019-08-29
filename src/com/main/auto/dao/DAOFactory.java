package com.main.auto.dao;

public abstract class DAOFactory {

    public static DAOFactory getDAOFactory(DBType whichFactory) {
        switch (whichFactory) {
            case MYSQL:
                return new MySQLDAOFactory();
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
