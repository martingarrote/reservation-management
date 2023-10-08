package com.martingarrote.reservationmanagement.controllers;


import com.martingarrote.reservationmanagement.models.dtos.CustomerDTO;
import com.martingarrote.reservationmanagement.services.CustomerService;
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
@RequestMapping(value = "/api/customers")
@Tag(name = "Customer", description = "Customer endpoints")
public class CustomerController {

    @Autowired
    CustomerService service;

    @Operation(description = "Create a new customer", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create a new customer"),
            @ApiResponse(responseCode = "400", description = "Bad request: any problem was occurred")
    })
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CustomerDTO customer) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(customer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(description = "List all existing customers", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List with all customers"),
            @ApiResponse(responseCode = "404", description = "Not found any customer")
    })
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> listAll() {
        List<CustomerDTO> costumersDTO = service.listAll();

        if (costumersDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(costumersDTO);
    }

    @Operation(description = "Get a customer based in ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get a customer with given id"),
            @ApiResponse(responseCode = "404", description = "Not found a customer with given id")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        CustomerDTO costumer = service.findById(id);

        if (costumer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(costumer);
    }

    @Operation(description = "Update a customer based in given ID", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update the customer"),
            @ApiResponse(responseCode = "404", description = "Not found a customer with given id"),
            @ApiResponse(responseCode = "400", description = "Bad request: any problem was occurred")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody CustomerDTO costumer, @PathVariable Long id) throws Exception {
        try {
            Long serviceReturn = service.update(costumer, id);

            if (serviceReturn == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok().body(serviceReturn);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @Operation(description = "Delete a customer based in given ID", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete a customer with given id"),
            @ApiResponse(responseCode = "404", description = "Not found a customer with given id")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        try {
            boolean serviceReturn = service.deleteById(id);

            if (serviceReturn) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

    }
}
