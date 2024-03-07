package it.fsants.dal;

import it.fsants.dal.DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection connect() {

        try {
            // Get database credentials from DatabaseConfig class
            var jdbcUrl = DBConfig.getDbUrl();
            var user = DBConfig.getDbUsername();
            var password = DBConfig.getDbPassword();

            // Open a connection
            return DriverManager.getConnection(jdbcUrl, user, password);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
