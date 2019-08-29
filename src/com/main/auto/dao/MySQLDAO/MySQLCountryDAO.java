package com.main.auto.dao.MySQLDAO;

import com.main.auto.dao.CountryDAO;
import com.main.auto.model.Country;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

//country (country_code, country_name)

public class MySQLCountryDAO implements CountryDAO {
    private String sqlQuery;
    private final String property = "country_query";

    @Override
    public boolean edit(Country country) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(country, statement);
            statement.setInt(3, country.getId());
            statement.executeUpdate();
            statement.close();
            result = true;
            System.out.println(getClass().getSimpleName() + " with following details was updated: \n" + country.toString());
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
    public int add(Country country) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(country, statement);
            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    id = generatedKey.getInt(1);
                    System.out.println("ID= " + id);
                }
            }
            statement.close();
            System.out.println(getClass().getSimpleName() + " with following details was saved in the database: \n" + country.toString());
        } catch (SQLException e) {
            System.out.println(getClass().getSimpleName() + " with following details wasn't saved in the database: \n" + country.toString());
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return id;
    } //add

    @Override
    public Country getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        Country country = new Country();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(country, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return country;
    } //getById

    @Override
    public List<Country> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<Country> countries = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                Country country = new Country();
                modelParameters(country, resultSet);
                countries.add(country);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return countries;
    } //getAll

    public Country modelParameters(Country country, ResultSet resultSet) throws SQLException {
    	country.setId(resultSet.getInt("id"));
        country.setCountryCode(resultSet.getString("country_code"));
        country.setCountryName(resultSet.getString("country_name"));
        return country;
    } //modelParameters

    public void dbParameters(Country country, PreparedStatement statement) throws SQLException {
        statement.setString(1, country.getCountryCode());
        statement.setString(2, country.getCountryName());
    } //dbParameters
} //class MySQLCountryDAO
