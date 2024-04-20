package com.project.jdbc.starter.dao;


import com.project.jdbc.starter.entity.TicketEntity;
import com.project.jdbc.starter.util.ConnectionManager;

import java.sql.*;

public class TicketDao {

    private static final TicketDao INSTANCE = new TicketDao();

    private static final String DELETE_SQL = """
            DELETE FROM ticket
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
        INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost)
        values (?, ?, ?, ?, ?);
        """;

    public boolean deleteById(Long ticketId) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, ticketId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Long save(TicketEntity ticketEntity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, ticketEntity.getPassengerNo());
            preparedStatement.setString(2, ticketEntity.getPassengerName());
            preparedStatement.setLong(3, ticketEntity.getFlightId());
            preparedStatement.setString(4, ticketEntity.getSeatNo());
            preparedStatement.setBigDecimal(5, ticketEntity.getCost());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                Long generatedId = generatedKeys.getLong("id");
                ticketEntity.setId(generatedId);
                return generatedId;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1L;
    }

    private TicketDao() {

    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }
}
