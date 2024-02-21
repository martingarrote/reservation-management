package com.martingarrote.reservationmanagement.controllers;

import com.martingarrote.reservationmanagement.models.dtos.RoomDTO;
import com.martingarrote.reservationmanagement.services.RoomService;
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
@RequestMapping(value = "/api/rooms")
@Tag(name = "Room", description = "Room endpoints")
public class RoomController {

    @Autowired
    RoomService service;

    @Operation(description = "Create a new room", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create a new room"),
            @ApiResponse(responseCode = "400", description = "Bad request: any problem was occurred")
    })
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody RoomDTO room) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(room));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(description = "List all existing rooms", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List with all rooms"),
            @ApiResponse(responseCode = "204", description = "Not found any room")
    })
    @GetMapping
    public ResponseEntity<List<RoomDTO>> listAll() {
        List<RoomDTO> roomsDTO = service.listAll();

        if (roomsDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(roomsDTO);
    }

    @Operation(description = "Get a room based in given ID", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get a room with given id"),
            @ApiResponse(responseCode = "404", description = "Not found a room with given id")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        RoomDTO room = service.findById(id);

        if (room == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(room);
    }

    @Operation(description = "Get all rooms based in busy status", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get rooms with expected busy status"),
            @ApiResponse(responseCode = "404", description = "Not found any room with the determined busy status")
    })
    @GetMapping(value = "/status")
    public ResponseEntity<Object> findByBusy(@RequestParam("busy") boolean busy) {
        List<RoomDTO> roomDTOS = service.findByBusy(busy);

        if (roomDTOS.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(roomDTOS);
    }

    @Operation(description = "Update a room based in given ID", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update the room"),
            @ApiResponse(responseCode = "404", description = "Not found a room with given id"),
            @ApiResponse(responseCode = "400", description = "Bad request: any problem was occurred")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody RoomDTO room, @PathVariable Long id) throws Exception {
        try {
            Long serviceReturn = service.update(room, id);

            if (serviceReturn == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok().body(serviceReturn);
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @Operation(description = "Delete a room based in given ID", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete a room with given id"),
            @ApiResponse(responseCode = "404", description = "Not found a room with given id"),
            @ApiResponse(responseCode = "409", description = "Probably the room is associated with a reservation")
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