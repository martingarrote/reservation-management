package com.martingarrote.reservationmanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Reservation Management APIs",
                version = "1.0",
                description = "Documentation of Reservation Management APIs"
        )
)
public class ReservationManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationManagementApplication.class, args);
    }

}
