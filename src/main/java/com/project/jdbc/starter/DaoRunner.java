package com.project.jdbc.starter;

import com.project.jdbc.starter.dao.TicketDao;
import com.project.jdbc.starter.entity.TicketEntity;

import java.math.BigDecimal;

public class DaoRunner {

    public static void main(String[] args) {
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
}
