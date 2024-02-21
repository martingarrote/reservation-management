package com.martingarrote.reservationmanagement.controllers;

import com.martingarrote.reservationmanagement.models.dtos.CustomerDTO;
import com.martingarrote.reservationmanagement.models.dtos.ReservationDTO;
import com.martingarrote.reservationmanagement.models.dtos.RoomDTO;
import com.martingarrote.reservationmanagement.services.ReservationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationControllerTest {

    @Mock
    ReservationService service;

    @InjectMocks
    ReservationController controller;

    Long defaultId;

    @Before
    public void setUp() {
        this.defaultId = 1L;
    }

    @Test
    public void create_StatusCreated() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(service.create(reservationDTO)).thenReturn(defaultId);

        ResponseEntity<?> response = controller.create(reservationDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(defaultId);
    }

    @Test
    public void create_StatusBadRequest() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(service.create(reservationDTO)).thenThrow(Exception.class);

        ResponseEntity<?> response = controller.create(reservationDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void listAll_StatusOk() {
        List<ReservationDTO> reservationDTO = List.of(new ReservationDTO(), new ReservationDTO());
        when(service.listAll()).thenReturn(reservationDTO);

        ResponseEntity<?> response = controller.listAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(reservationDTO);
    }

    @Test
    public void listAll_StatusNoContent() {
        when(service.listAll()).thenReturn(List.of());

        ResponseEntity<?> response = controller.listAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void findById_StatusOk() {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(service.findById(defaultId)).thenReturn(reservationDTO);

        ResponseEntity<?> response = controller.findById(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(reservationDTO);
    }

    @Test
    public void findById_StatusNotFound() {
        when(service.findById(defaultId)).thenReturn(null);

        ResponseEntity<?> response = controller.findById(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void search_StatusOk() {
        boolean active = true;
        int endsIn = 5;
        List<ReservationDTO> reservationDTOS = List.of(new ReservationDTO(), new ReservationDTO());
        when(service.search(active, endsIn)).thenReturn(reservationDTOS);

        ResponseEntity<?> response = controller.search(active, endsIn);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(reservationDTOS);
    }

    @Test
    public void search_StatusNotFound() {
        boolean active = true;
        int endsIn = 5;
        when(service.search(active, endsIn)).thenReturn(List.of());

        ResponseEntity<?> response = controller.search(active, endsIn);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void findByCustomer_StatusOk() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(defaultId);
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setCustomer(customerDTO);

        List<ReservationDTO> reservationDTOS = List.of(reservationDTO);
        when(service.findByCustomer(defaultId)).thenReturn(reservationDTOS);

        ResponseEntity<?> response = controller.findByCustomer(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(reservationDTOS);
    }

    @Test
    public void findByCustomer_StatusNotFound() {
        List<ReservationDTO> reservationDTOS = List.of();
        when(service.findByCustomer(defaultId)).thenReturn(reservationDTOS);

        ResponseEntity<?> response = controller.findByCustomer(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void findByRoom_StatusOk() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(defaultId);
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservedRoom(roomDTO);

        List<ReservationDTO> reservationDTOS = List.of(reservationDTO);
        when(service.findByRoom(defaultId)).thenReturn(reservationDTOS);

        ResponseEntity<?> response = controller.findByRoom(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(reservationDTOS);
    }

    @Test
    public void findByRoom_StatusNotFound() {
        List<ReservationDTO> reservationDTOS = List.of();
        when(service.findByRoom(defaultId)).thenReturn(reservationDTOS);

        ResponseEntity<?> response = controller.findByRoom(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void update_StatusOk() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(service.update(reservationDTO, defaultId)).thenReturn(defaultId);

        ResponseEntity<?> response = controller.update(reservationDTO, defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(defaultId);
    }

    @Test
    public void update_StatusNotFound() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(service.update(reservationDTO, defaultId)).thenReturn(null);

        ResponseEntity<?> response = controller.update(reservationDTO, defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void update_StatusBadRequest() throws Exception {
        ReservationDTO reservationDTO = new ReservationDTO();
        when(service.update(reservationDTO, defaultId)).thenThrow(Exception.class);

        ResponseEntity<?> response = controller.update(reservationDTO, defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void delete_StatusNoContent() throws Exception {
        when(service.deleteById(defaultId)).thenReturn(true);

        ResponseEntity<?> response = controller.delete(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void delete_StatusNotFound() throws Exception {
        when(service.deleteById(defaultId)).thenReturn(false);

        ResponseEntity<?> response = controller.delete(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
