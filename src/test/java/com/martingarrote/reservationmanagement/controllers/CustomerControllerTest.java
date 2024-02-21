package com.martingarrote.reservationmanagement.controllers;

import com.martingarrote.reservationmanagement.models.dtos.CustomerDTO;
import com.martingarrote.reservationmanagement.services.CustomerService;
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
public class CustomerControllerTest {

    @Mock
    CustomerService service;

    @InjectMocks
    CustomerController controller;

    Long defaultId;

    @Before
    public void setUp() {
        this.defaultId = 1L;
    }

    @Test
    public void create_StatusCreated() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        when(service.create(customerDTO)).thenReturn(defaultId);

        ResponseEntity<?> response = controller.create(customerDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(defaultId);
    }

    @Test
    public void create_StatusBadRequest() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        when(service.create(customerDTO)).thenThrow(Exception.class);

        ResponseEntity<?> response = controller.create(customerDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void listAll_StatusOk() {
        List<CustomerDTO> customersDTO = List.of(new CustomerDTO(), new CustomerDTO());
        when(service.listAll()).thenReturn(customersDTO);

        ResponseEntity<?> response = controller.listAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(customersDTO);
    }

    @Test
    public void listAll_NoContent() {
        when(service.listAll()).thenReturn(List.of());

        ResponseEntity<?> response = controller.listAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void findById_StatusOk() {
        CustomerDTO customerDTO = new CustomerDTO();
        when(service.findById(defaultId)).thenReturn(customerDTO);

        ResponseEntity<?> response = controller.findById(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(customerDTO);
    }

    @Test
    public void findById_StatusNotFound() {
        when(service.findById(defaultId)).thenReturn(null);

        ResponseEntity<?> response = controller.findById(defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void update_StatusOk() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        when(service.update(customerDTO, defaultId)).thenReturn(defaultId);

        ResponseEntity<?> response = controller.update(customerDTO, defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(defaultId);
    }

    @Test
    public void update_StatusNotFound() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        when(service.update(customerDTO, defaultId)).thenReturn(null);

        ResponseEntity<?> response = controller.update(customerDTO, defaultId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void update_StatusBadRequest() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        when(service.update(customerDTO, defaultId)).thenThrow(Exception.class);

        ResponseEntity<?> response = controller.update(customerDTO, defaultId);

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
