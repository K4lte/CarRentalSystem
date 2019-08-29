package com.main.auto.dao.MySQLDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.main.auto.dao.ReservationStatusDAO;
import com.main.auto.model.ReservationStatus;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

public class MySQLReservationStatusDAO implements ReservationStatusDAO {
    private String sqlQuery;
    private final String property = "reservation_status_query";

	@Override
	public boolean edit(ReservationStatus confirmation) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(confirmation, statement);
            statement.setInt(2, confirmation.getId());
            statement.executeUpdate();
            statement.close();
            result = true;
            System.out.println(getClass().getSimpleName() + " with following details was updated: \n" + confirmation.toString());
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
	public int add(ReservationStatus confirmation) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(confirmation, statement);
            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    id = generatedKey.getInt(1);
                    System.out.println("Id= " + id);
                }
            }
            statement.close();
            System.out.println(getClass().getSimpleName() + " with following details was saved in the database: \n" + confirmation.toString());
        } catch (SQLException e) {
            System.out.println(getClass().getSimpleName() + " with following details wasn't saved in the database: \n" + confirmation.toString());
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return id;
    } //add

	@Override
	public ReservationStatus getById(int id) {
		sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        ReservationStatus confirmation = new ReservationStatus();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(confirmation, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return confirmation;
    } //getById

	@Override
	public List<ReservationStatus> getAll() {
		 sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
	        Connection connection = ConnectionPool.getInstance().receiveConnection();
	        List<ReservationStatus> confirmations = new ArrayList<>();
	        try (Statement statement = connection.createStatement();
	             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
	            while (resultSet.next()) {
	            	ReservationStatus confirmation = new ReservationStatus();
	                modelParameters(confirmation, resultSet);
	                confirmations.add(confirmation);
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        } finally {
	        	ConnectionPool.getInstance().releaseConnection(connection);
	        }
	        return confirmations;
	    } //getAll
	
	@Override
	public ReservationStatus getByStatus(String status) {
		sqlQuery = ConfigurationManager.getProperty(property, "query.getByStatus");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        ReservationStatus confirmation = new ReservationStatus();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, status);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(confirmation, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return confirmation;
	} //getByStatus
	
    public ReservationStatus modelParameters(ReservationStatus status, ResultSet resultSet) throws SQLException {
        status.setId(resultSet.getInt("id"));
        status.setStatusName(resultSet.getString("status_name"));
        return status;
    } //modelParameters

    public void dbParameters(ReservationStatus status, PreparedStatement statement) throws SQLException {
        statement.setString(1, status.getStatusName());
    } //dbParameters

	@Override
	public List<ReservationStatus> getStatusMatchedById(int statusId) {
		sqlQuery = ConfigurationManager.getProperty(property, "query.getStatusMatchedById");
	    List<ReservationStatus> list = new ArrayList<>();
	    Connection connection = ConnectionPool.getInstance().receiveConnection();
	    try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
	        statement.setInt(1, statusId);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {                    
	            	ReservationStatus status = new ReservationStatus();
	                modelParameters(status, resultSet);
	                list.add(status);
	            }
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    } finally {
	      	ConnectionPool.getInstance().releaseConnection(connection);
	    }
	 	return list;
	}

}
