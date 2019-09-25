package com.main.auto.dao.daoMySQL;

import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.daoInterfaces.CarDamageDAO;
import com.main.auto.model.CarDamage;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

//car_damage (id, car_id, damage_type_id, info)

public class CarDamageDAOMySQL implements CarDamageDAO {
	private static final Logger logger = Logger.getLogger(CarDamageDAOMySQL.class.getName());
    private String sqlQuery;
    private final String property = "car_damage_query";

    @Override
    public boolean edit(CarDamage carDamage) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(carDamage, statement);
            statement.setInt(6, carDamage.getId());
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
    public int add(CarDamage carDamage) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(carDamage, statement);
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
    public CarDamage getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        CarDamage carDamage = new CarDamage();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(carDamage, resultSet);
                }
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return carDamage;
    } //getById

    @Override
    public List<CarDamage> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<CarDamage> carDamages = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                CarDamage carDamage = new CarDamage();
                modelParameters(carDamage, resultSet);
                carDamages.add(carDamage);
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return carDamages;
    } //getAll

    public CarDamage modelParameters(CarDamage carDamage, ResultSet resultSet) throws SQLException {
    	carDamage.setId(resultSet.getInt("id"));
        carDamage.setCar(DAOFactory.getDAOFactory(DBType.MYSQL).getCarDAO().getById(resultSet.getInt("car_id")));    	
        carDamage.setDamageType(DAOFactory.getDAOFactory(DBType.MYSQL).getDamageTypeDAO().getById(resultSet.getInt("damage_type_id")));
        carDamage.setPayment(DAOFactory.getDAOFactory(DBType.MYSQL).getPaymentDAO().getById(resultSet.getInt("payment_id")));
        carDamage.setAmount(resultSet.getBigDecimal("amount"));
        carDamage.setInfo(resultSet.getString("info"));
        return carDamage;
    } //modelParameters

    public void dbParameters(CarDamage carDamage, PreparedStatement statement) throws SQLException {
        statement.setInt(1, carDamage.getCar().getId());
        statement.setInt(2, carDamage.getDamageType().getId());
        if (carDamage.getPayment() == null) {
        	statement.setNull(3, java.sql.Types.INTEGER);
        } else {
        	statement.setInt(3, carDamage.getPayment().getId());
        }       
        statement.setBigDecimal(4, carDamage.getAmount());
        statement.setString(5, carDamage.getInfo());
        
    } //dbParameters

	@Override
	public CarDamage getDamagedWithoutPayment(int reservationId) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getDamagedWithoutPayment");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        CarDamage carDamage = new CarDamage();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, reservationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    modelParameters(carDamage, resultSet);
                }
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return carDamage;
	}
} // class CarDamage
