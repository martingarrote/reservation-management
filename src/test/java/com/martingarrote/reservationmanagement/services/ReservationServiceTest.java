package com.martingarrote.reservationmanagement.services;

import com.martingarrote.reservationmanagement.models.dtos.CustomerDTO;
import com.martingarrote.reservationmanagement.models.dtos.ReservationDTO;
import com.martingarrote.reservationmanagement.models.dtos.RoomDTO;
import com.martingarrote.reservationmanagement.models.entities.Customer;
import com.martingarrote.reservationmanagement.models.entities.Reservation;
import com.martingarrote.reservationmanagement.models.entities.Room;
import com.martingarrote.reservationmanagement.repositories.ReservationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    @Mock
    ReservationRepository repository;

    @Mock
    ModelMapper mapper;

    @InjectMocks
    ReservationService service;

    private Long defaultId;

    @Before
    public void setUp() {
        defaultId = 1L;
    }

//    @Test
//    public void save_ShouldSave() throws Exception {
//        Reservation reservation = createReservationEntity();
//        Reservation createdReservation = createReservationEntity();
//        ReservationDTO reservationDTO = createReservationDTO();
//        when(mapper.map(reservationDTO, Reservation.class)).thenReturn(reservation);
//        when(repository.save(reservation)).thenReturn(createdReservation);
//
//        Long returnedId = service.save(reservationDTO);
//    }

    public Reservation createReservationEntity() {
        Customer customer = new Customer();
        Room room = new Room();

        return new Reservation(1L, "RES001", customer, room, 1000.0, "Reservation test",
                5, LocalDate.now(), LocalDate.now().plusMonths(5), true);
    }

    public ReservationDTO createReservationDTO() {
        CustomerDTO customerDTO= new CustomerDTO();
        RoomDTO roomDTO = new RoomDTO();

        return new ReservationDTO(1L, "RES001", customerDTO, roomDTO, 1000.0, "Reservation test",
                5, LocalDate.now(), LocalDate.now().plusMonths(5), true);
    }

}
