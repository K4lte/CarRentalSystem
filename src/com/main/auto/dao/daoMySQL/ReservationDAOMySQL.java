package com.main.auto.dao.daoMySQL;

import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.daoInterfaces.ReservationDAO;
import com.main.auto.model.Reservation;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

// reservation (unique_number, car_id, client_id, pick_up_date, return_date, pick_up_location_id, return_location_id, accessory_id, status, note)

public class ReservationDAOMySQL implements ReservationDAO {
	private static final Logger logger = Logger.getLogger(ReservationDAOMySQL.class.getName());
	private String sqlQuery;
    private final String property = "reservation_query";

    @Override
    public boolean edit(Reservation reservation) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(reservation, statement);
            statement.setInt(10, reservation.getId());
            statement.executeUpdate();
            statement.close();
            result = true;
            logger.info("Field was saved");
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
    public int add(Reservation reservation) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(reservation, statement);
            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    id = generatedKey.getInt(1);
                }
            }
            statement.close();
            logger.info("Field was saved");
            logger.log(Level.DEBUG, reservation.toString());
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return id;
    } //add

    @Override
    public Reservation getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        Reservation reservation = new Reservation();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(reservation, resultSet);
                }
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return reservation;
    } //getById

    @Override
    public List<Reservation> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<Reservation> reservations = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                modelParameters(reservation, resultSet);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return reservations;
    } //getAll

    public Reservation modelParameters(Reservation reservation, ResultSet resultSet) throws SQLException {
        reservation.setId(resultSet.getInt("id"));        
    	reservation.setUniqueNumber(resultSet.getString("unique_number"));
        reservation.setCar(DAOFactory.getDAOFactory(DBType.MYSQL).getCarDAO().getById(resultSet.getInt("car_id")));
        reservation.setClient(DAOFactory.getDAOFactory(DBType.MYSQL).getClientDAO().getById(resultSet.getInt("client_id")));
        reservation.setPickUpDate(resultSet.getDate("pick_up_date"));
        reservation.setReturnDate(resultSet.getDate("return_date"));
        reservation.setPickUpLocation(DAOFactory.getDAOFactory(DBType.MYSQL).getOfficeDAO().getById(resultSet.getInt("pick_up_location_id")));
        reservation.setReturnLocation(DAOFactory.getDAOFactory(DBType.MYSQL).getOfficeDAO().getById(resultSet.getInt("return_location_id")));
        reservation.setStatus(DAOFactory.getDAOFactory(DBType.MYSQL).getReservationStatusDAO().getById(resultSet.getInt("status_id")));
        reservation.setNote(resultSet.getString("note"));
        return reservation;
    } //modelParameters

    public void dbParameters(Reservation reservation, PreparedStatement statement) throws SQLException {
        statement.setString(1, reservation.getUniqueNumber());
        statement.setInt(2, reservation.getCar().getId());
        statement.setInt(3, reservation.getClient().getId());
        statement.setDate(4, reservation.getPickUpDate());
        statement.setDate(5, reservation.getReturnDate());
        statement.setInt(6, reservation.getPickUpLocation().getId());
        statement.setInt(7, reservation.getReturnLocation().getId());
        statement.setInt(8,  reservation.getStatus().getId());
        statement.setString(9, reservation.getNote());
    } //dbParameters

	@Override
	public List<Reservation> getByStatus(String status) {
		sqlQuery = ConfigurationManager.getProperty(property, "query.getByStatus");
	    List<Reservation> reservations = new ArrayList<>();
	    Connection connection = ConnectionPool.getInstance().receiveConnection();
	    try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
	        statement.setString(1, status);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {                    
	            	Reservation reservation = new Reservation();
	                modelParameters(reservation, resultSet);
	                reservations.add(reservation);
	            }
	        }
	    } catch (SQLException e) {
	    	logger.log(Level.ERROR, "Exception: ", e);
	    } finally {
	      	ConnectionPool.getInstance().releaseConnection(connection);
	    }
	 	return reservations;
	}

	@Override
	public List<Reservation> getByStatusId(int statusId) {
		sqlQuery = ConfigurationManager.getProperty(property, "query.getByStatusId");
	    List<Reservation> reservations = new ArrayList<>();
	    Connection connection = ConnectionPool.getInstance().receiveConnection();
	    try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
	        statement.setInt(1, statusId);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {                    
	            	Reservation reservation = new Reservation();
	                modelParameters(reservation, resultSet);
	                reservations.add(reservation);
	            }
	        }
	    } catch (SQLException e) {
	    	logger.log(Level.ERROR, "Exception: ", e);
	    } finally {
	      	ConnectionPool.getInstance().releaseConnection(connection);
	    }
	 	return reservations;
	}

	@Override
	public List<Reservation> getByClientId(int clientId) {
		sqlQuery = ConfigurationManager.getProperty(property, "query.getByClientId");
	    List<Reservation> reservations = new ArrayList<>();
	    Connection connection = ConnectionPool.getInstance().receiveConnection();
	    try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
	        statement.setInt(1, clientId);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {                    
	            	Reservation reservation = new Reservation();
	                modelParameters(reservation, resultSet);
	                reservations.add(reservation);
	            }
	        }
	    } catch (SQLException e) {
	    	logger.log(Level.ERROR, "Exception: ", e);
	    } finally {
	      	ConnectionPool.getInstance().releaseConnection(connection);
	    }
	 	return reservations;
	}

	@Override
	public List<Reservation> getByClientIdStatus(int clientId, String status) {
		sqlQuery = ConfigurationManager.getProperty(property, "query.getByClientIdStatus");
	    List<Reservation> reservations = new ArrayList<>();
	    Connection connection = ConnectionPool.getInstance().receiveConnection();
	    try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
	        statement.setInt(1, clientId);
	        statement.setString(2, status);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {                    
	            	Reservation reservation = new Reservation();
	                modelParameters(reservation, resultSet);
	                reservations.add(reservation);
	            }
	        }
	    } catch (SQLException e) {
	    	logger.log(Level.ERROR, "Exception: ", e);
	    } finally {
	      	ConnectionPool.getInstance().releaseConnection(connection);
	    }
	 	return reservations;
	}

	@Override
	public boolean changeStatus(int statusId, int reservationId) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.changeStatus");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, statusId);
            statement.setInt(2, reservationId);
            statement.executeUpdate();
            statement.close();
            result = true;
            System.out.println(getClass().getSimpleName() + " status was updated: \n");
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return result;	
	}

} //class MySQLReservationDAO
