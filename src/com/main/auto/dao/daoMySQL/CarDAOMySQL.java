package com.main.auto.dao.daoMySQL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.daoInterfaces.CarDAO;
import com.main.auto.model.Car;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

//car (number, category_id, brand_id, model, production_year, color_id, rental_price, total_price, rate_number, transmission_id, number_of_seats,
// number_of_suitcases, air_conditioning, current_office_id, default_office_id)

public class CarDAOMySQL implements CarDAO {
	private static final Logger logger = Logger.getLogger(CarDAOMySQL.class.getName());
	private String sqlQuery;
    private final String property = "car_query";

    @Override
    public boolean edit(Car car) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(car, statement);
            statement.setInt(16, car.getId());
            statement.executeUpdate();
            statement.close();
            result = true;
            logger.info("Field was updated");
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return result;
    } //edit

    @Override
    public boolean delete(int id) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.delete");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
            logger.info("Field was deleted");
            result = true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return result;
    } //delete

    @Override
    public int add(Car car) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(car, statement);
            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    id = generatedKey.getInt(1);
                }
            }
            statement.close();
            logger.info("Field was saved");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return id;
    } //add

    @Override
    public Car getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        Car car = new Car();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(car, resultSet);
                }
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return car;
    } //getById

    @Override
    public List<Car> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<Car> cars = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                Car car = new Car();
                modelParameters(car, resultSet);
                cars.add(car);
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return cars;
    } //getAll

    @Override
    public List<Car> searchByLocationDate(int locationOfficeId, Date pickUpDate, Date dropOffDate) {
    	//SELECT DISTINCT * FROM car_rental_system.car 
    	//WHERE current_office_id=? 
    	//AND id NOT IN (select reservation.car_id FROM car_rental_system.reservation WHERE (reservation.pick_up_date >=? OR reservation.pick_up_date<=?) 
    	//AND (reservation.return_date >=? AND reservation.return_date<=?) GROUP BY car_id)
    	sqlQuery = ConfigurationManager.getProperty(property, "query.searchByLocationDate");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<Car> cars = new ArrayList<>();
    
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, locationOfficeId);
            statement.setDate(2, pickUpDate);
            statement.setDate(3, dropOffDate);
            statement.setDate(4, pickUpDate);
            statement.setDate(5, dropOffDate);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    Car car = new Car();
                    modelParameters(car, resultSet);
                    cars.add(car);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return cars;
    } // searching



    public Car modelParameters(Car car, ResultSet resultSet) throws SQLException {
        car.setId(resultSet.getInt("id"));
        car.setNumber(resultSet.getString("number"));
        car.setCategory(DAOFactory.getDAOFactory(DBType.MYSQL).getCarCategoryDAO().getById(resultSet.getInt("category_id")));
        car.setBrand(DAOFactory.getDAOFactory(DBType.MYSQL).getCarBrandDAO().getById(resultSet.getInt("brand_id")));
        car.setModel(resultSet.getString("model"));
        car.setProductionYear(resultSet.getInt("production_year"));
        car.setColor(DAOFactory.getDAOFactory(DBType.MYSQL).getCarColorDAO().getById(resultSet.getInt("color_id")));
        car.setRentalPrice(resultSet.getBigDecimal("rental_price"));
        car.setTotalPrice(resultSet.getBigDecimal("total_price"));
        car.setRateNumber(resultSet.getInt("rate_number"));
        car.setTransmission(DAOFactory.getDAOFactory(DBType.MYSQL).getCarTransmissionDAO().getById(resultSet.getInt("transmission_id")));
        car.setNumberOfSeats(resultSet.getInt("number_of_seats"));
        car.setNumberOfSuitcases(resultSet.getInt("number_of_suitcases"));
        car.setAirConditioning(resultSet.getBoolean("air_conditioning"));
        car.setCurrentOffice(DAOFactory.getDAOFactory(DBType.MYSQL).getOfficeDAO().getById(resultSet.getInt("current_office_id")));
        car.setDefaultOffice(DAOFactory.getDAOFactory(DBType.MYSQL).getOfficeDAO().getById(resultSet.getInt("default_office_id")));
        return car;
    } //modelParameters

    public void dbParameters(Car car, PreparedStatement statement) throws SQLException {
        statement.setString(1, car.getNumber());
        statement.setInt(2, car.getCategory().getId());
        statement.setInt(3, car.getBrand().getId());
        statement.setString(4, car.getModel());
        statement.setInt(5, car.getProductionYear());
        statement.setInt(6, car.getColor().getId());
        statement.setBigDecimal(7, car.getRentalPrice());
        statement.setBigDecimal(8, car.getTotalPrice());
        statement.setInt(9, car.getRateNumber());
        statement.setInt(10, car.getTransmission().getId());
        statement.setInt(11, car.getNumberOfSeats());
        statement.setInt(12, car.getNumberOfSuitcases());
        statement.setBoolean(13, car.getAirConditioning());
        statement.setInt(14, car.getCurrentOffice().getId());
        statement.setInt(15, car.getDefaultOffice().getId());
    } //dbParameters

	@Override
	public Car getByNumber(String number) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getByNumber");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        Car car = new Car();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, number);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(car, resultSet);
                }
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return car;
	} //getByNumber

} // class MySQLCarDAO
