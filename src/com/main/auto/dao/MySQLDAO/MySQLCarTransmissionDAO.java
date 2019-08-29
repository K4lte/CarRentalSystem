package com.main.auto.dao.MySQLDAO;

import com.main.auto.dao.CarTransmissionDAO;
import com.main.auto.model.CarTransmission;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLCarTransmissionDAO implements CarTransmissionDAO {
    private String sqlQuery;
    private final String property = "car_transmission_query";

    @Override
    public boolean edit(CarTransmission transmission) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(transmission, statement);
            statement.setInt(2, transmission.getId());
            statement.executeUpdate();
            statement.close();
            result = true;
            System.out.println(getClass().getSimpleName() + " with following details was updated: \n" + transmission.toString());
        } catch (SQLException e) {
            e.printStackTrace();
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
            System.out.println(getClass().getSimpleName() + " with following details was successfully deleted from DB:\n id=" + id);
            result = true;
        } catch (SQLException e) {
            System.out.println("Failed to remove" + getClass().getSimpleName() + " with following details:\n id=" + id);
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return result;
    } //delete

    @Override
    public int add(CarTransmission transmission) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(transmission, statement);
            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    id = generatedKey.getInt(1);
                    System.out.println("Id= " + id);
                }
            }
            statement.close();
            System.out.println(getClass().getSimpleName() + " with following details was saved in the database: \n" + transmission.toString());
        } catch (SQLException e) {
            System.out.println(getClass().getSimpleName() + " with following details wasn't saved in the database: \n" + transmission.toString());
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return id;
    } //add

    @Override
    public CarTransmission getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        CarTransmission transmission = new CarTransmission();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1,id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(transmission, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return transmission;
    } //getById

    @Override
    public List<CarTransmission> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<CarTransmission> transmissions = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                CarTransmission transmission = new CarTransmission();
                modelParameters(transmission, resultSet);
                transmissions.add(transmission);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return transmissions;
    } //getAll

    public CarTransmission modelParameters(CarTransmission transmission, ResultSet resultSet) throws SQLException {
        transmission.setId(resultSet.getInt("id"));
        transmission.setName(resultSet.getString("transmission_type"));
        return transmission;
    } //modelParameters

    public void dbParameters(CarTransmission transmission, PreparedStatement statement) throws SQLException {
//        statement.setInt(1, transmission.getId());
        statement.setString(1, transmission.getName());
    } //dbParameters

}
