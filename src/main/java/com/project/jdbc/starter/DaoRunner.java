package com.project.jdbc.starter;

import com.project.jdbc.starter.dao.TicketDao;
import com.project.jdbc.starter.dto.TicketFilter;
import com.project.jdbc.starter.entity.TicketEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DaoRunner {

    public static void main(String[] args) {
        TicketEntity ticketById = findTicketById(2L);
        List<TicketEntity> all = findAll(new TicketFilter(10, 5));


        System.out.println(ticketById);
        System.out.println(all);
    }

    private static List<TicketEntity> findAll(TicketFilter ticketFilter) {
        TicketDao instance = TicketDao.getInstance();

        List<TicketEntity> ticketEntities = instance.findAll(ticketFilter);

        return ticketEntities;
    }

    private static void saveTicket() {
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

    private static TicketEntity findTicketById(Long id) {
        TicketDao instance = TicketDao.getInstance();

        Optional<TicketEntity> ticketEntity = instance.findById(id);

        return ticketEntity.orElseThrow();
    }
}
