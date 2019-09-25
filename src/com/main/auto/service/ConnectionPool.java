package com.main.auto.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    //    private Connection connection;
	private static ConnectionPool instance = new ConnectionPool();
    private final String driver = ConfigurationManager.getProperty("connection_db", "db.driver");
    private final String url = ConfigurationManager.getProperty("connection_db", "db.url");
    private final String user = ConfigurationManager.getProperty("connection_db", "db.user");
    private final String password = ConfigurationManager.getProperty("connection_db", "db.password");
    private final static int DB_MAX_CONNECTION = 15;
    //public List<Connection> availableConnections = new ArrayList<>(DB_MAX_CONNECTION);
    private List<Connection> availableConnections = new ArrayList<>(DB_MAX_CONNECTION);

    private ConnectionPool() {
        initializeConnectionPool();
    }


    //    Method which gets a connection using url, user and password
    private Connection createConnection() {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    } //createConnection()

    private void initializeConnectionPool() {
        while (availableConnections.size() < DB_MAX_CONNECTION) {
            availableConnections.add(createConnection());
        }
    }

    public synchronized Connection receiveConnection() {
        Connection connection = null;
        int index = availableConnections.size()-1;
        if (index >= 0) {
            connection = availableConnections.get(index);
            availableConnections.remove(index);
        }
         return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        availableConnections.add(connection);
    }
    
    public synchronized static ConnectionPool getInstance() {
    	return instance;
    }
    
    
}
