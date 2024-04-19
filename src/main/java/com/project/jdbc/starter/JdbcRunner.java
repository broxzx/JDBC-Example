package com.project.jdbc.starter;

import com.project.jdbc.starter.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driver = Driver.class;
        String sql = """
                SELECT * FROM info;
                """;

        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            System.out.println(connection.getTransactionIsolation());
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println(resultSet);

            while (resultSet.next()) {
                System.out.println(resultSet.getLong("id"));
                System.out.println(resultSet.getString("data"));
            }
        }
    }
}
