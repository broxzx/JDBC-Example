package com.project.jdbc.starter.dao;


import com.project.jdbc.starter.dto.TicketFilter;
import com.project.jdbc.starter.entity.TicketEntity;
import com.project.jdbc.starter.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private static final String UPDATE_SQL = """
            UPDATE ticket
            SET passenger_no = ?,
            passenger_name = ?,
            flight_id = ?,
            seat_no = ?,
            cost = ?
            WHERE id = ?;
            """;

    private static final String FIND_BY_ID = """
            SELECT *  FROM ticket
            WHERE id = ?;
            """;

    private static final String FIND_ALL = """
            SELECT * FROM ticket
            """;

    public List<TicketEntity> findAll(TicketFilter filter) {
        List<Object> parameters = new ArrayList<>();
        parameters.add(filter.limit());
        parameters.add(filter.offset());

        String sql = FIND_ALL + """
                LIMIT ?
                OFFSET ?
                """;

        try (Connection open = ConnectionManager.open();
             PreparedStatement preparedStatement = open.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<TicketEntity> result = new ArrayList<>();

            while (resultSet.next()) {
                TicketEntity ticketEntity = buildTicketDEntity(resultSet);

                result.add(ticketEntity);
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<TicketEntity> findAll() {
        List<TicketEntity> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                TicketEntity ticketEntity = buildTicketDEntity(resultSet);

                result.add(ticketEntity);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public void update(TicketEntity ticketEntity) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, ticketEntity.getPassengerNo());
            preparedStatement.setString(2, ticketEntity.getPassengerName());
            preparedStatement.setLong(3, ticketEntity.getFlightId());
            preparedStatement.setString(4, ticketEntity.getSeatNo());
            preparedStatement.setBigDecimal(5, ticketEntity.getCost());
            preparedStatement.setLong(6, ticketEntity.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<TicketEntity> findById(Long id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            TicketEntity ticketEntity = null;
            if (resultSet.next()) {
                ticketEntity = buildTicketDEntity(resultSet);
            }

            return Optional.ofNullable(ticketEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

    private static TicketEntity buildTicketDEntity(ResultSet resultSet) throws SQLException {
        return TicketEntity.builder()
                .id(resultSet.getLong("id"))
                .passengerNo(resultSet.getString("passenger_no"))
                .passengerName(resultSet.getString("passenger_name"))
                .flightId(resultSet.getLong("flight_id"))
                .seatNo(resultSet.getString("seat_no"))
                .cost(resultSet.getBigDecimal("cost"))
                .build();
    }
}
