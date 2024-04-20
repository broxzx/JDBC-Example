package com.project.jdbc.starter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Builder
public class TicketEntity {
    private Long id;

    private String passengerNo;

    private String passengerName;

    private Long flightId;

    private String seatNo;

    private BigDecimal cost;
}
