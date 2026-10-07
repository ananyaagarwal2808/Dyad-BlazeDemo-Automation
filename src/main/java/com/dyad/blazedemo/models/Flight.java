package com.dyad.blazedemo.models;

import java.math.BigDecimal;

public record Flight(
        String airline,
        String flightNumber,
        String departureTime,
        String arrivalTime,
        BigDecimal price,
        int rowIndex) {
}
