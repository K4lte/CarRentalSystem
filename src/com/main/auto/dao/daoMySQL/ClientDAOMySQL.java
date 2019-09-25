package com.main.auto.dao.daoMySQL;


import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.daoInterfaces.ClientDAO;
import com.main.auto.model.Client;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

//client (first_name, last_name, passport_number, drivers_license_number, birth_date, phone_number, email_address, login, password)

public class ClientDAOMySQL implements ClientDAO {
	private static final Logger logger = Logger.getLogger(ClientDAOMySQL.class.getName());
	
    private String sqlQuery;
    private final String property = "client_query";

    @Override
    public boolean edit(Client client) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
        	dbParameters(client, statement);
        	statement.setInt(11, client.getId());
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
            result = true;
            logger.info("Field was deleted");
        } catch (SQLException e) {       	
            logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return result;
    } //delete

    @Override
    public int add(Client client) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(client, statement);
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
    public Client getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        Client client = new Client();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    modelParameters(client, resultSet);
                }
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return client;
    } //getById

    @Override
    public List<Client> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<Client> clients = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                Client client = new Client();
                modelParameters(client, resultSet);
                clients.add(client);
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return clients;
    } //getAll()

    public Client modelParameters(Client client, ResultSet resultSet) throws SQLException {
        client.setId(resultSet.getInt("id"));
        client.setFirstName(resultSet.getString("first_name"));
        client.setLastName(resultSet.getString("last_name"));
        client.setPassportNumber(resultSet.getString("passport_number"));
        client.setDriverLicenseNumber(resultSet.getString("drivers_license_number"));
        client.setBirthDate(resultSet.getDate("birth_date"));
        client.setPhoneNumber(resultSet.getString("phone_number"));
        client.setEmail(resultSet.getString("email_address"));
        client.setPassword(resultSet.getString("password"));
        client.setPermission(DAOFactory.getDAOFactory(DBType.MYSQL).getPermissionDAO().getById(resultSet.getInt("permission_id")));
        return client;
    } //modelParameters

    public void dbParameters(Client client, PreparedStatement statement) throws SQLException {
        statement.setString(1, client.getFirstName());
        statement.setString(2, client.getLastName());
        statement.setString(3, client.getPassportNumber());
        statement.setString(4, client.getDriverLicenseNumber());
        statement.setDate(5, client.getBirthDate());
        statement.setString(6, client.getPhoneNumber());
        statement.setString(7, client.getEmail());
        statement.setString(8, client.getPassword());
        statement.setInt(9, client.getPermission().getId());
    } //dbParameters


	@Override
	public Client login(String login, String password) {
		Client client = new Client();
		sqlQuery = ConfigurationManager.getProperty(property, "query.authorization");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, login);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    modelParameters(client, resultSet);
                }
            }
        } catch (SQLException e) {
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }

        return client;
	} //login
	
	@Override
	public Client getByPassportNumber(String pasNumber) {
		Client client = new Client();
		sqlQuery = ConfigurationManager.getProperty(property, "query.getByPassportNumber");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, pasNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    modelParameters(client, resultSet);
                } else {
                	return null;
                }
            }
        } catch (SQLException e) {   // Invalid user
        	logger.log(Level.ERROR, "Exception: ", e);
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return client;
	} //getByPassportNumber

} // class MySQLClientDAO
