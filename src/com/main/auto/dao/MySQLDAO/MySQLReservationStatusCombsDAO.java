package com.main.auto.dao.MySQLDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.ReservationStatusCombsDAO;
import com.main.auto.dao.ReservationStatusDAO;
import com.main.auto.model.ReservationStatusCombs;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

public class MySQLReservationStatusCombsDAO implements ReservationStatusCombsDAO {

    private String sqlQuery;
    private final String property = "reservation_status_combs_query";

    @Override
    public boolean edit(ReservationStatusCombs status) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
        	dbParameters(status, statement);
        	statement.setInt(11, status.getId());
            statement.executeUpdate();
            statement.close();
            result = true;
            System.out.println(getClass().getSimpleName() + " with following details was updated: \n" + status.toString());
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
            result = true;
            System.out.println(getClass().getSimpleName() + " with following id was successfully deleted from DB:\n" + id);
        } catch (SQLException e) {
        	System.out.println("Failed to remove" + getClass().getSimpleName() + " with following id:\n " + id);
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return result;
    } //delete

    @Override
    public int add(ReservationStatusCombs status) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(status, statement);
            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    id = generatedKey.getInt(1);
                    System.out.println("ID= " + id);
                }
            }
            statement.close();
            System.out.println(getClass().getSimpleName() + " with following details was saved in the database: \n" + status.toString());
        } catch (SQLException e) {
            System.out.println(getClass().getSimpleName() + " with following details wasn't saved in the database: \n" + status.toString());
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return id;
    } //add
  
    @Override
    public ReservationStatusCombs getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        ReservationStatusCombs status = new ReservationStatusCombs();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    modelParameters(status, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return status;
    } //getById

    @Override
    public List<ReservationStatusCombs> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<ReservationStatusCombs> list = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
            	ReservationStatusCombs status = new ReservationStatusCombs();
                modelParameters(status, resultSet);
                list.add(status);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return list;
    } //getAll()

    public ReservationStatusCombs modelParameters(ReservationStatusCombs status, ResultSet resultSet) throws SQLException {
    	ReservationStatusDAO dao = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationStatusDAO();
    	status.setId(resultSet.getInt("id"));
        status.setStatus(dao.getById(resultSet.getInt("status_id")));
        status.setStatusMatchWith(dao.getById(resultSet.getInt("status_id_combs")));
        return status;
    } //modelParameters

    public void dbParameters(ReservationStatusCombs status, PreparedStatement statement) throws SQLException {
        statement.setInt(1, status.getStatus().getId());
        statement.setInt(2, status.getStatusMatchWith().getId());
    } //dbParameters

/*	@Override
	public List<ReservationStatusCombs> getStatusMatchedById(int statusId) {
		sqlQuery = ConfigurationManager.getProperty(property, "query.getByStatusId");
	    List<ReservationStatusCombs> list = new ArrayList<>();
	    Connection connection = ConnectionPool.getInstance().receiveConnection();
	    try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
	        statement.setInt(1, statusId);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {                    
	            	ReservationStatusCombs status = new ReservationStatusCombs();
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

	*/
}
