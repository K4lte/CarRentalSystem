package com.main.auto.dao.daoMySQL;

import com.main.auto.dao.daoInterfaces.DamageTypeDAO;
import com.main.auto.model.DamageType;
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

//damage_type (id, type_name, percentage)

public class DamageTypeDAOMySQL implements DamageTypeDAO {
	private static final Logger logger = Logger.getLogger(DamageTypeDAOMySQL.class.getName());
    private String sqlQuery;
    private final String property = "damage_type_query";


    @Override
    public boolean edit(DamageType damageType) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(damageType, statement);
            statement.setInt(3, damageType.getId());
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
    public int add(DamageType damageType) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(damageType, statement);
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
    public DamageType getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        DamageType damageType = new DamageType();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(damageType, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return damageType;
    } //getById

    @Override
    public List<DamageType> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<DamageType> damageTypes = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                DamageType damageType = new DamageType();
                modelParameters(damageType, resultSet);
                damageTypes.add(damageType);
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return damageTypes;
    } //getAll

    public DamageType modelParameters(DamageType damageType, ResultSet resultSet) throws SQLException {
    	damageType.setId(resultSet.getInt("id"));
    	damageType.setTypeName(resultSet.getString("type_name"));
        damageType.setPercentage(resultSet.getInt("percentage"));
        return damageType;
    } //modelParameters

    public void dbParameters(DamageType damageType, PreparedStatement statement) throws SQLException {
        statement.setString(1, damageType.getTypeName());
        statement.setInt(2, damageType.getPercentage());
    } //dbParameters
} // class MySQLDamageTypeDAO
