package com.project.jdbc.starter;

import com.project.jdbc.starter.dao.TicketDao;
import com.project.jdbc.starter.entity.TicketEntity;

import java.math.BigDecimal;
import java.util.Optional;

public class DaoRunner {

    public static void main(String[] args) {
        TicketEntity ticketById = findTicketById(2L);

        System.out.println(ticketById);
    }

    public static void saveTicket() {
        TicketDao instance = TicketDao.getInstance();

        TicketEntity ticketEntity = TicketEntity.builder()
                .id(1L)
                .passengerName("passenger name")
                .passengerNo("1")
                .flightId(1L)
                .seatNo("1")
                .cost(new BigDecimal(12000))
                .build();

        instance.save(ticketEntity);
    }

    public static TicketEntity findTicketById(Long id) {
        TicketDao instance = TicketDao.getInstance();

        Optional<TicketEntity> ticketEntity = instance.findById(id);

        return ticketEntity.orElseThrow();
    }
}
