package dbutil.bingo.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by incognito on 2016-12-02.
 * Create database connection
 */
public class ConnectionFactory {

    private static ConnectionFactory instance = new ConnectionFactory();
    private static final String PATH = "jdbc:mysql://localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "javadb";
    private static final String USER = "root";
    private static final String PASSWORD = "1234abcd";
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER_CLASS);
            System.out.println("[!] Driver has been loaded.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("[!] Unable to find driver class.");
        }
    }

    public static ConnectionFactory getInstance() {
        return instance;
    }

    private Connection createConnection() {
        Connection conn = null;
        // ?useSSL=true
        String fullPath = String.format("%s:%s/%s", PATH, PORT, DB_NAME);
//        System.out.println("[!] DB path: " + fullPath);

        try {
            conn = DriverManager.getConnection(fullPath, USER, PASSWORD);
            System.out.println("[!] You are good to go. :D");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[!] Unable to get connection.");
        }

        return conn;
    }

    public Connection getConnection() {
        return createConnection();
    }

}