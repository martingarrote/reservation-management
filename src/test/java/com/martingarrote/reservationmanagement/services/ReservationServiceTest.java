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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.INSERT_ERROR;
import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.ReservationExceptions.DURATION_OUT_OF_ALLOWED_RANGE;
import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.ReservationExceptions.MAX_RESERVATION_DURATION;
import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.RoomExceptions.ROOM_IS_BUSY;
import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.UPDATE_ERROR;
import static com.martingarrote.reservationmanagement.utils.LocalDateUtils.getFutureDateBasedInMonthsQuantity;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

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

    @Test
    public void save_ShouldSave() throws Exception {
        Reservation reservation = createReservationEntity();
        Reservation createdReservation = createReservationEntity();
        ReservationDTO reservationDTO = createReservationDTO();
        when(mapper.map(reservationDTO, Reservation.class)).thenReturn(reservation);
        when(repository.save(reservation)).thenReturn(createdReservation);

        Long returnedId = service.save(reservationDTO);

        verify(mapper).map(reservationDTO, Reservation.class);
        verify(repository, times(1)).save(reservation);
        verifyNoMoreInteractions(repository);
        assertThat(returnedId).isEqualTo(defaultId);
    }

    @Test(expected = Exception.class)
    public void save_ShouldNotSave() throws Exception {
        ReservationDTO reservationDTO = createReservationDTO();

        when(repository.save(any(Reservation.class))).thenThrow(new Exception(INSERT_ERROR));

        Throwable exception = catchThrowable(() -> service.save(reservationDTO));

        assertThat(exception.getMessage()).isEqualTo(INSERT_ERROR);
    }

    @Test
    public void save_ShouldNotSave_DurationOutOfAllowedRangeException() throws Exception {
        ReservationDTO reservationDTO = createReservationDTO();
        reservationDTO.setDuration(MAX_RESERVATION_DURATION);

        Throwable exception = catchThrowable(() -> service.save(reservationDTO));

        assertThat(exception.getMessage()).isEqualTo(DURATION_OUT_OF_ALLOWED_RANGE);
    }

    @Test
    public void save_ShouldNotSave_RoomIsBusyException() throws Exception {
        ReservationDTO reservationDTO = createReservationDTO();
        reservationDTO.getReservedRoom().setBusy(true);

        Throwable exception = catchThrowable(() -> service.save(reservationDTO));

        assertThat(exception.getMessage()).isEqualTo(ROOM_IS_BUSY);
    }

    @Test
    public void create_ShouldCreate() throws Exception {
        // Arrange
        Reservation reservation = createReservationEntity();
        Reservation createdReservation = createReservationEntity();
        ReservationDTO reservationDTO = createReservationDTO();
        when(mapper.map(reservationDTO, Reservation.class)).thenReturn(reservation);
        when(repository.save(reservation)).thenReturn(createdReservation);

        // Act
        Long id = service.create(reservationDTO);

        // Asserts
        assertThat(id).isEqualTo(defaultId);
    }

    @Test(expected = Exception.class)
    public void create_ShouldNotCreate() throws Exception {
        ReservationDTO reservationDTO = createReservationDTO();

        when(service.save(reservationDTO)).thenThrow(new Exception());
    }

    @Test
    public void listAll_ShouldList() {
        List<ReservationDTO> reservationDTOS = repository.findAll().stream()
                .map(reservation -> mapper.map(reservation, ReservationDTO.class)).toList();
        when(repository.findAll().stream()
                .map(reservation -> mapper.map(reservation, ReservationDTO.class)).toList()).thenReturn(reservationDTOS);

        List<ReservationDTO> expectedReservationsDTO = service.listAll();

        assertArrayEquals(expectedReservationsDTO.toArray(), reservationDTOS.toArray());
    }

    @Test()
    public void listAll_ShouldNotListAnyReservation() {
        List<Reservation> emptyReservationList = repository.findAll();
        when(repository.findAll()).thenReturn(emptyReservationList);

        List<ReservationDTO> returnedReservationsDTO = service.listAll();

        assertArrayEquals(emptyReservationList.toArray(), returnedReservationsDTO.toArray());
    }

    @Test
    public void findById_ShouldGet() {
        Reservation reservation = createReservationEntity();
        ReservationDTO expectedReservationDTO = mapper.map(reservation, ReservationDTO.class);

        when(repository.findById(defaultId)).thenReturn(Optional.of(reservation));
        when(mapper.map(reservation, ReservationDTO.class)).thenReturn(expectedReservationDTO);

        ReservationDTO returnedReservationDTO = service.findById(defaultId);

        assertThat(returnedReservationDTO).isEqualTo(expectedReservationDTO);
    }

    @Test
    public void findById_ShouldNotGetAnyReservation() {
        when(repository.findById(defaultId)).thenReturn(Optional.empty());

        ReservationDTO returnedReservationsDTO = service.findById(defaultId);

        assertThat(returnedReservationsDTO).isNull();
    }

    @Test
    public void search_ShouldSearch() {
        boolean active = true;
        int endsIn = 5;

        LocalDate endsInDate = getFutureDateBasedInMonthsQuantity(endsIn);

        List<ReservationDTO> expectedReservationsDTO = repository
                .search(active, endsInDate)
                .stream()
                .map(reservation -> mapper.map(reservation, ReservationDTO.class))
                .toList();

        when(repository.search(active, endsInDate)
                .stream()
                .map(reservation -> mapper.map(reservation, ReservationDTO.class))
                .toList()
        ).thenReturn(expectedReservationsDTO);

        List<ReservationDTO> returnedReservationsDTO = service.search(active, endsIn);

        assertThatList(expectedReservationsDTO).isEqualTo(returnedReservationsDTO);
    }

    @Test
    public void search_ShouldNotSearch() {
        List<ReservationDTO> expectedReservationDTO = List.of();
        when(
                repository
                        .findByCustomerId(defaultId)
                        .stream()
                        .map(reservation -> mapper.map(reservation, ReservationDTO.class))
                        .toList()
        ).thenReturn(expectedReservationDTO);

        List<ReservationDTO> returnedReservationsDTO = service.findByCustomer(defaultId);

        assertThatList(returnedReservationsDTO).isEmpty();
    }

    @Test
    public void findByCustomer_ShouldGet() {
        List<ReservationDTO> expectedReservationDTO = repository
                .findByCustomerId(defaultId)
                .stream()
                .map(reservation -> mapper.map(reservation, ReservationDTO.class))
                .toList();
        when(
                repository
                        .findByCustomerId(defaultId)
                        .stream()
                        .map(reservation -> mapper.map(reservation, ReservationDTO.class))
                        .toList()
        ).thenReturn(expectedReservationDTO);

        List<ReservationDTO> returnedReservationsDTO = service.findByCustomer(defaultId);

        assertThatList(expectedReservationDTO).isEqualTo(returnedReservationsDTO);
    }

    @Test
    public void findByCustomer_ShouldNotGet() {
        List<ReservationDTO> expectedReservationDTO = List.of();
        when(
                repository
                        .findByCustomerId(defaultId+1)
                        .stream()
                        .map(reservation -> mapper.map(reservation, ReservationDTO.class))
                        .toList()
        ).thenReturn(expectedReservationDTO);

        List<ReservationDTO> returnedReservationsDTO = service.findByCustomer(defaultId+1);

        assertThatList(returnedReservationsDTO).isEmpty();
    }

    @Test
    public void findByRoom_ShouldGet() {
        List<ReservationDTO> expectedReservationsDTO = repository
                .findByReservedRoomId(defaultId)
                .stream().map(reservation -> mapper.map(reservation, ReservationDTO.class))
                .toList();
        when(
                repository
                        .findByReservedRoomId(defaultId)
                        .stream()
                        .map(reservation -> mapper.map(reservation, ReservationDTO.class))
                        .toList()
        ).thenReturn(expectedReservationsDTO);

        List<ReservationDTO> returnedReservationsDTO = service.findByRoom(defaultId);

        assertThatList(expectedReservationsDTO).isEqualTo(returnedReservationsDTO);
    }

    @Test
    public void findByRoom_ShouldNotGet() {
        List<ReservationDTO> expectedReservationDTO = List.of();
        when(
                repository
                        .findByReservedRoomId(defaultId+1)
                        .stream()
                        .map(reservation -> mapper.map(reservation, ReservationDTO.class))
                        .toList()
        ).thenReturn(expectedReservationDTO);

        List<ReservationDTO> returnedReservationsDTO = service.findByRoom(defaultId+1);

        assertThatList(returnedReservationsDTO).isEmpty();
    }

    @Test
    public void deleteById_ShouldDelete() {
        when(repository.existsById(defaultId)).thenReturn(true);

        boolean returned = service.deleteById(defaultId);

        assertThat(returned).isTrue();
    }

    @Test
    public void deleteById_ShouldNotDelete() {
        when(repository.existsById(defaultId)).thenReturn(false);

        boolean returned = service.deleteById(defaultId);

        assertThat(returned).isFalse();
    }

    @Test
    public void update_ShouldUpdate() throws Exception {
        Reservation reservationToUpdate = createReservationEntity();
        ReservationDTO reservationDTO = createReservationDTO();
        when(repository.findById(defaultId)).thenReturn(Optional.of(reservationToUpdate));

        Long returnedId = service.update(reservationDTO, defaultId);

        assertThat(returnedId).isEqualTo(defaultId);
    }

    @Test
    public void update_ShouldNotUpdate_NotFound() throws Exception {
        ReservationDTO reservationDTO = createReservationDTO();

        var returnedByService = service.update(reservationDTO, defaultId);

        assertThat(returnedByService).isNull();
    }

    @Test
    public void update_ShouldNotUpdate_UpdateError() {
        ReservationDTO reservationDTO = createReservationDTO();
        when(repository.existsById(defaultId)).thenReturn(true);

        Throwable exception = catchThrowable(() -> service.update(reservationDTO, defaultId));

        assertThat(exception.getMessage()).isEqualTo(UPDATE_ERROR);
    }












    public Reservation createReservationEntity() {
        LocalDate dateOfBirth = LocalDate.of(1990, 10, 5);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        Customer customer = new Customer(1L, "Jose", dateOfBirth, "12345678910", "email@gmail.com");
        Room room = new Room(1L, 1, "Room 1", 30d, 500d, false, true);

        return new Reservation(1L, "RES001", customer, room, 1000.0, "Reservation test",
                12, LocalDate.now(), LocalDate.now(), true);
    }

    public ReservationDTO createReservationDTO() {
        LocalDate dateOfBirth = LocalDate.of(1990, 10, 5);

        CustomerDTO customerDTO = new CustomerDTO(1L, "Jose", dateOfBirth, "12345678910", "email@gmail.com");
        RoomDTO roomDTO = new RoomDTO(1L, 1, "Room 1", 30D, 500D, false, true);

        return new ReservationDTO(1L, "RES001", customerDTO, roomDTO, 1000.0, "Reservation test",
                12, LocalDate.now(), LocalDate.now(), true);
    }

}
