package com.martingarrote.reservationmanagement.controllers;

import com.martingarrote.reservationmanagement.models.dtos.ReservationDTO;
import com.martingarrote.reservationmanagement.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/reservations")
@Tag(name = "Reservation", description = "Reservation endpoints")
public class ReservationController {

    @Autowired
    ReservationService service;

    @Operation(description = "Create a new reservation", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create a new reservation"),
            @ApiResponse(responseCode = "400", description = "Bad request: any problem was occurred")
    })
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ReservationDTO reservation) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(reservation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(description = "List all existing reservations", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List with all reservations"),
            @ApiResponse(responseCode = "404", description = "Not found any reservation")
    })
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> listAll() {
        List<ReservationDTO> reservationDTOS = service.listAll();

        if (reservationDTOS.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(reservationDTOS);
    }

    @Operation(description = "Get a reservation based in ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get a reservation with given id"),
            @ApiResponse(responseCode = "404", description = "Not found a reservation with given id")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        ReservationDTO reservation = service.findById(id);

        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(reservation);
    }

    @Operation(description = "Get the reservations based ", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get the reservations with customized preferences of search"),
            @ApiResponse(responseCode = "404", description = "Not found any reservation with determinate preferences")
    })
    @GetMapping(value = "/search")
    public ResponseEntity<List<ReservationDTO>> search(
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "endsIn", required = false) Integer endsIn
    ) {
        List<ReservationDTO> reservations = service.search(active, endsIn);

        if (reservations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(reservations);
    }

    @Operation(description = "Get the reservations of a determined customer, base in the given ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get the reservations with given customer id"),
            @ApiResponse(responseCode = "404", description = "Not found a reservation with given customer id")
    })
    @GetMapping(value = "/customer/{id}")
    public ResponseEntity<List<ReservationDTO>> findByCustomer(@PathVariable Long id) {
        List<ReservationDTO> reservations = service.findByCustomer(id);

        if (reservations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(reservations);
    }

    @Operation(description = "Update a reservation based in given ID", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update the reservation"),
            @ApiResponse(responseCode = "404", description = "Not found a reservation with given id"),
            @ApiResponse(responseCode = "400", description = "Bad request: any problem was occurred")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody ReservationDTO reservationDTO, @PathVariable Long id) throws Exception {
        try {
            Long serviceReturn = service.update(reservationDTO, id);

            if (serviceReturn == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok().body(serviceReturn);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @Operation(description = "Delete a reservation based in given ID", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete a reservation with given id"),
            @ApiResponse(responseCode = "404", description = "Not found a reservation with given id")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean serviceReturn = service.deleteById(id);

        if (serviceReturn) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
