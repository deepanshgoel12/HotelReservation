package com.hotelreservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Main Spring Boot Application Class
 * Implements Spring Boot autoconfiguration and embedded server (BO3 - Spring Boot)
 */
@SpringBootApplication
@CrossOrigin(origins = "http://localhost:3000")
public class HotelReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelReservationApplication.class, args);
    }
}

