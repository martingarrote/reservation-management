package com.martingarrote.reservationmanagement.controllers;

import com.martingarrote.reservationmanagement.models.dtos.RoomDTO;
import com.martingarrote.reservationmanagement.services.RoomService;
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
public class RoomControllerTest {

    @Mock
    RoomService service;

    @InjectMocks
    RoomController controller;

    Long defaultId;

    @Before
    public void setUp() {
        defaultId = 1L;
    }


    @Test
    public void create_StatusCreated() throws Exception {
        RoomDTO roomDTO = new RoomDTO();
        when(service.create(roomDTO)).thenReturn(defaultId);

        ResponseEntity<?> response = controller.create(roomDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(defaultId);
    }

    @Test
    public void create_StatusBadRequest() throws Exception {
        RoomDTO roomDTO = new RoomDTO();
        when(service.create(roomDTO)).thenThrow(Exception.class);

        ResponseEntity<?> response = controller.create(roomDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void listAll_StatusOk() {
        List<RoomDTO> roomDTO = List.of(new RoomDTO(), new RoomDTO());
        when(service.listAll()).thenReturn(roomDTO);

        ResponseEntity<?> response = controller.listAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(roomDTO);
    }

    @Test
    public void listAll_StatusNoContent() {
        when(service.listAll()).thenReturn(List.of());

        ResponseEntity<?> response = controller.listAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void findById_StatusOk() {
        RoomDTO roomDTO = new RoomDTO();
        when(service.findById(defaultId)).thenReturn(roomDTO);

        ResponseEntity<?> response = controller.findById(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(roomDTO);
    }

    @Test
    public void findById_StatusNotFound() {
        when(service.findById(defaultId)).thenReturn(null);

        ResponseEntity<?> response = controller.findById(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void findByBusy_StatusOk() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setBusy(true);
        List<RoomDTO> roomDTOS = List.of(roomDTO);
        when(service.findByBusy(true)).thenReturn(roomDTOS);

        ResponseEntity<?> response = controller.findByBusy(true);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(roomDTOS);
    }

    @Test
    public void findByBusy_StatusNotFound() {
        List<RoomDTO> roomDTOS = List.of();
        when(service.findByBusy(false)).thenReturn(roomDTOS);

        ResponseEntity<?> response = controller.findByBusy(false);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void update_StatusOk() throws Exception {
        RoomDTO roomDTO = new RoomDTO();
        when(service.update(roomDTO, defaultId)).thenReturn(defaultId);

        ResponseEntity<?> response = controller.update(roomDTO, defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(defaultId);
    }

    @Test
    public void update_StatusNotFound() throws Exception {
        RoomDTO roomDTO = new RoomDTO();
        when(service.update(roomDTO, defaultId)).thenReturn(null);

        ResponseEntity<?> response = controller.update(roomDTO, defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void update_StatusBadRequest() throws Exception {
        RoomDTO roomDTO = new RoomDTO();
        when(service.update(roomDTO, defaultId)).thenThrow(Exception.class);

        ResponseEntity<?> response = controller.update(roomDTO, defaultId);

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

    @Test
    public void delete_StatusConflict() throws Exception {
        when(service.deleteById(defaultId)).thenThrow(Exception.class);

        ResponseEntity<?> response = controller.delete(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
