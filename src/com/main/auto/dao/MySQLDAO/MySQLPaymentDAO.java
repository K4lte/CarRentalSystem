package com.main.auto.dao.MySQLDAO;

import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.PaymentDAO;
import com.main.auto.model.Payment;
import com.main.auto.service.ConfigurationManager;
import com.main.auto.service.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//payment (credit_card_type, credit_card_number, total_amount, payment_type_id, payment_status, reservation_id)

public class MySQLPaymentDAO implements PaymentDAO {
    private String sqlQuery;
    private final String property = "payment_query";

    @Override
    public boolean edit(Payment payment) {
        boolean result = false;
        sqlQuery = ConfigurationManager.getProperty(property, "query.edit");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            dbParameters(payment, statement);
            statement.setInt(7, payment.getId());
            statement.executeUpdate();
            statement.close();
            result = true;
            System.out.println(getClass().getSimpleName() + " with following details was updated: \n" + payment.toString());
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
            System.out.println("Failed to remove" + getClass().getSimpleName() + " with id: " + id);
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return result;
    } //delete

    @Override
    public int add(Payment payment) {
        int id = -1;
        sqlQuery = ConfigurationManager.getProperty(property, "query.add");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            dbParameters(payment, statement);
            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    id = generatedKey.getInt(1);
                    System.out.println("ID= " + id);
                }
            }
            statement.close();
            System.out.println(getClass().getSimpleName() + " with following details was saved in the database: \n" + payment.toString());
        } catch (SQLException e) {
            System.out.println(getClass().getSimpleName() + " with following details wasn't saved in the database: \n" + payment.toString());
            e.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return id;
    } //add

    @Override
    public Payment getById(int id) {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getById");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        Payment payment = new Payment();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {                    
                    modelParameters(payment, resultSet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return payment;
    } //getById

    @Override
    public List<Payment> getAll() {
        sqlQuery = ConfigurationManager.getProperty(property, "query.getAll");
        Connection connection = ConnectionPool.getInstance().receiveConnection();
        List<Payment> payments = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                Payment payment = new Payment();
                modelParameters(payment, resultSet);
                payments.add(payment);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        	ConnectionPool.getInstance().releaseConnection(connection);
        }
        return payments;
    } //getAll

    public Payment modelParameters(Payment payment, ResultSet resultSet) throws SQLException {
        payment.setId(resultSet.getInt("id"));
        payment.setCreditCardType(DAOFactory.getDAOFactory(DBType.MYSQL).getCreditCardTypeDAO().getById(resultSet.getInt("credit_card_type_id")));
        payment.setCreditCardNumber(resultSet.getString("credit_card_number"));
        payment.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        payment.setPaymentStatus(resultSet.getString("payment_status"));
        payment.setReservation(DAOFactory.getDAOFactory(DBType.MYSQL).getReservationDAO().getById(resultSet.getInt("reservation_id")));
        return payment;
    } //modelParameters

    public void dbParameters(Payment payment, PreparedStatement statement) throws SQLException {
        statement.setInt(1, payment.getCreditCardType().getId());
        statement.setString(2, payment.getCreditCardNumber());
        statement.setBigDecimal(3, payment.getTotalAmount());
        statement.setString(4, payment.getPaymentStatus());
        statement.setInt(5, payment.getReservation().getId());
    } //dbParameters

} //class MySQLPaymentDAO
