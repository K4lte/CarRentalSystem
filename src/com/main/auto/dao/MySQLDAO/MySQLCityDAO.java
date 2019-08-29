package com.main.auto.dao.MySQLDAO;

import com.main.auto.dao.CityDAO;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.model.City;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

//city (city_name, country_id)

public class MySQLCityDAO implements CityDAO {
    private String sqlQuery;
    private final String property = "city_query";

    @Override
    public boolean edit(City city) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(city, statement);
            statement.setInt(3, city.getId());
            statement.executeUpdate();
            statement.close();
            result = true;
            System.out.println(getClass().getSimpleName() + " with following details was updated: \n" + city.toString());
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
            System.out.println("Failed to remove" + getClass().getSimpleName() + " with following details:\n id=" + id);
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return result;
    } //delete

    @Override
    public int add(City city) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(city, statement);
            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    id = generatedKey.getInt(1);
                    System.out.println("ID= " + id);
                }
            }
            statement.close();
            System.out.println(getClass().getSimpleName() + " with following details was saved in the database: \n" + city.toString());
        } catch (SQLException e) {
            System.out.println(getClass().getSimpleName() + " with following details wasn't saved in the database: \n" + city.toString());
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return id;
    } //add

    @Override
    public City getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        City city = new City();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(city, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return city;
    } //getById

    @Override
    public List<City> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<City> cities = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                City city = new City();
                modelParameters(city, resultSet);
                cities.add(city);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return cities;
    } //getAll

    public City modelParameters(City city, ResultSet resultSet) throws SQLException {
    	city.setId(resultSet.getInt("id"));
        city.setCityName(resultSet.getString("city_name"));
        city.setCountry(DAOFactory.getDAOFactory(DBType.MYSQL).getCountryDAO().getById(resultSet.getInt("country_id")));
        return city;
    } //modelParameters

    public void dbParameters(City city, PreparedStatement statement) throws SQLException {
        statement.setString(1, city.getCityName());
        statement.setInt(2, city.getCountry().getId());
    } //dbParameters

	@Override
	public List<City> getOfficiesCities() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getOfficiesCities");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<City> cities = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                City city = new City();
                modelParameters(city, resultSet);
                cities.add(city);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return cities;
	} //getOfficiesCities()
	
} // class getCityDAO
