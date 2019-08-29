package com.main.auto.dao.MySQLDAO;

import com.main.auto.dao.EmployeeUserDAO;
import com.main.auto.dao.DAOAuthorization;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.model.EmployeeUser;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

//employee_user (first_name, last_name, employee_office_id, permission, login, password)

public class MySQLEmployeeUserDAO implements EmployeeUserDAO, DAOAuthorization<EmployeeUser> {
    private String sqlQuery;
    private final String property = "employee_user_query";

    @Override
    public boolean edit(EmployeeUser user) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(user, statement);
            statement.setInt(7, user.getId());
            statement.executeUpdate();
            statement.close();
            result = true;
            System.out.println(getClass().getSimpleName() + " with following details was updated: \n" + user.toString());
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
    public int add(EmployeeUser user) {
    	int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(user, statement);
            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    id = generatedKey.getInt(1);
                  //  System.out.println("ID= " + id);
                }
            }
            statement.close();
            System.out.println(getClass().getSimpleName() + " with following details was saved in the database: \n" + user.toString());
        } catch (SQLException e) {
            System.out.println(getClass().getSimpleName() + " with following details wasn't saved in the database: \n" + user.toString());
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return id;
    } //add

    @Override
    public EmployeeUser getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        EmployeeUser user = new EmployeeUser();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(user, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return user;
    } //getById

    @Override
    public List<EmployeeUser> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<EmployeeUser> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                EmployeeUser user = new EmployeeUser();
                modelParameters(user, resultSet);
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return users;
    } //getAll

    public EmployeeUser modelParameters(EmployeeUser user, ResultSet resultSet) throws SQLException {
        user.setId(resultSet.getInt("id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setOffice(DAOFactory.getDAOFactory(DBType.MYSQL).getOfficeDAO().getById(resultSet.getInt("employee_office_id")));
        user.setPermission(DAOFactory.getDAOFactory(DBType.MYSQL).getPermissionDAO().getById(resultSet.getInt("permission_id")));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        return user;
    } //modelParameters

    public void dbParameters(EmployeeUser user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getFirstName());
        statement.setString(2, user.getLastName());
        statement.setInt(3, user.getOffice().getId());
        statement.setInt(4, user.getPermission().getId());
        statement.setString(5, user.getLogin());
        statement.setString(6, user.getPassword());
    } //dbParameters
    
	@Override
	public EmployeeUser login(String login, String password) {
		EmployeeUser user = new EmployeeUser();
		sqlQuery = ConfigurationManager.getProperty(property, "query.authorization");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, login);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    modelParameters(user, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Invalid user");
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }

        return user;
	} //login
    
} //class MySQLEmployeeUserDAO
