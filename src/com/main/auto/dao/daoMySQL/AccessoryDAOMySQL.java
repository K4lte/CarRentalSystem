package com.main.auto.dao.daoMySQL;

import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.daoInterfaces.AccessoryDAO;
import com.main.auto.model.Accessory;
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

//accessory (id, type_id, cost, quantity, reservation_id)

public class AccessoryDAOMySQL implements AccessoryDAO {
	private static final Logger logger = Logger.getLogger(AccessoryDAOMySQL.class.getName());
    private String sqlQuery;
    private final String property = "accessory_query";
    

    @Override
    public boolean edit(Accessory accessory) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(accessory, statement);
            statement.setInt(4, accessory.getId());
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
    } // edit

    @Override
    public boolean delete(int id) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.delete");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
            result = true;
            logger.info("Field was deleted");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return result;
    } // delete

    @Override
    public int add(Accessory accessory) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(accessory, statement);
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
    } // add

    @Override
    public Accessory getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        Accessory accessory = new Accessory();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                  
                    modelParameters(accessory, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return accessory;
    } // getById


    @Override
    public List<Accessory> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<Accessory> accessories = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                Accessory accessory = new Accessory();
                modelParameters(accessory, resultSet);
                accessories.add(accessory);
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return accessories;
    } // getAll

    public Accessory modelParameters(Accessory accessory, ResultSet resultSet) throws SQLException {
        accessory.setId(resultSet.getInt("id"));
        accessory.setType(DAOFactory.getDAOFactory(DBType.MYSQL).getAccessoryTypeDAO().getById(resultSet.getInt("type_id")));
        accessory.setCost(resultSet.getBigDecimal("cost"));
        accessory.setQuantity(resultSet.getInt("quantity"));
        return accessory;
    } //modelParameters

    public void dbParameters(Accessory accessory, PreparedStatement statement) throws SQLException {
        statement.setInt(1, accessory.getType().getId());
        statement.setBigDecimal(2, accessory.getCost());
        statement.setInt(3, accessory.getQuantity());
    } //dbParameters
} //class MySQLAccessoryDAO
