package com.main.auto.dao.MySQLDAO;

import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.LocationDAO;
import com.main.auto.model.Location;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//location (address, city_id, zip)

public class MySQLLocationDAO implements LocationDAO {
    private String sqlQuery;
    private final String property = "location_query";

    @Override
    public boolean edit(Location location) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(location, statement);
            statement.setInt(4, location.getId());
            statement.executeUpdate();
            statement.close();
            result = true;
            System.out.println(getClass().getSimpleName() + " with following details was updated: \n" + location.toString());
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
            System.out.println(getClass().getSimpleName() + " with following details was successfully deleted:\n id=" + id);
            result = true;
        } catch (SQLException e) {
            System.out.println("Failed to remove " + getClass().getSimpleName() + " with following details:\n id=" + id);
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return result;
    } //delete

    @Override
    public int add(Location location) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(location, statement);
            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    id = generatedKey.getInt(1);
                    System.out.println("ID= " + id);
                }
            }
            statement.close();
            System.out.println(getClass().getSimpleName() + " with following details was saved in the database: \n" + location.toString());
        } catch (SQLException e) {
            System.out.println(getClass().getSimpleName() + " with following details wasn't saved in the database: \n" + location.toString());
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return id;
    } //add

    @Override
    public Location getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        Location location = new Location();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(location, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return location;
    } //getById

    @Override
    public List<Location> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        List<Location> locations = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                Location location = new Location();
                modelParameters(location, resultSet);
                locations.add(location);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return locations;
    } //getAll

    public Location modelParameters(Location location, ResultSet resultSet) throws SQLException {
        location.setId(resultSet.getInt("id"));
        location.setAddress(resultSet.getString("address"));
        location.setCity(DAOFactory.getDAOFactory(DBType.MYSQL).getCityDAO().getById(resultSet.getInt("city_id")));
        location.setZip(resultSet.getString("zip"));
        return location;
    } //modelParameters

    public void dbParameters(Location location, PreparedStatement statement) throws SQLException {
        statement.setString(1, location.getAddress());
        statement.setInt(2, location.getCity().getId());
        statement.setString(3, location.getZip());
    }//dbParameters

	@Override
	public List<Location> getByCityId(int cityId) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        List<Location> locations = new ArrayList<>();
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, cityId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    Location location = new Location();
                    modelParameters(location, resultSet);
                    locations.add(location);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return locations;
	}

} //class MySQLLocationDAO
