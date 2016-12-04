package dbutil.bingo.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by incognito on 2016-12-02.
 */
public class ConnectionFactory {

    private static ConnectionFactory instance = new ConnectionFactory();
    private static final String PATH = "jdbc:mysql://localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "put_your_db_name_here";
    private static final String USER = "put_your_db_account_name_here";
    private static final String PASSWORD = "put_your_db_password_here";
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER_CLASS);
            System.out.println("[!] Driver has been loaded.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("[!] Unable to find driver class. Please check out it is correct name.");
        }
    }

    public static ConnectionFactory getInstance() {
        return instance;
    }

    private Connection createConnection() {
        Connection conn = null;
        String fullPath = String.format("%s:%s/%s?useSSL=true", PATH, PORT, DB_NAME);
        System.out.println("[!] DB path: " + fullPath);

        try {
            conn = DriverManager.getConnection(fullPath, USER, PASSWORD);
            System.out.println("[!] You are ready to go. :D");
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
