package com.main.auto.dao.MySQLDAO;

import com.main.auto.dao.CreditCardTypeDAO;
import com.main.auto.model.CreditCardType;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLCreditCardTypeDAO implements CreditCardTypeDAO {
    private String sqlQuery;
    private final String property = "credit_card_type_query";

    @Override
    public boolean edit(CreditCardType creditCardType) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(creditCardType, statement);
            statement.setInt(2, creditCardType.getId());
            statement.executeUpdate();
            statement.close();
            result = true;
            System.out.println(getClass().getSimpleName() + " with following details was updated: \n" + creditCardType.toString());
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
    public int add(CreditCardType creditCardType) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(creditCardType, statement);
            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    id = generatedKey.getInt(1);
                    System.out.println("ID= " + id);
                }
            }
            statement.close();
            System.out.println(getClass().getSimpleName() + " with following details was saved in the database: \n" + creditCardType.toString());
        } catch (SQLException e) {
            System.out.println(getClass().getSimpleName() + " with following details wasn't saved in the database: \n" + creditCardType.toString());
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return id;
    } //add

    @Override
    public CreditCardType getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        CreditCardType creditCardType = new CreditCardType();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1,id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(creditCardType, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return creditCardType;
    } //getById

    @Override
    public List<CreditCardType> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<CreditCardType> creditCardTypes = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
            	CreditCardType type = new CreditCardType();
                modelParameters(type, resultSet);
                creditCardTypes.add(type);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return creditCardTypes;
    } //getAll

    public CreditCardType modelParameters(CreditCardType creditCardType, ResultSet resultSet) throws SQLException {
        creditCardType.setId(resultSet.getInt("id"));
        creditCardType.setName(resultSet.getString("type_name"));
        return creditCardType;
    } //modelParameters

    public void dbParameters(CreditCardType creditCardType, PreparedStatement statement) throws SQLException {
//        statement.setInt(1, transmission.getId());
        statement.setString(1, creditCardType.getName());
    } //dbParameters

}
