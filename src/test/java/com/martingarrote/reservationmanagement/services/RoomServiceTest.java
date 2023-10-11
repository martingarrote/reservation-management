package com.martingarrote.reservationmanagement.services;

import com.martingarrote.reservationmanagement.models.dtos.RoomDTO;
import com.martingarrote.reservationmanagement.models.entities.Reservation;
import com.martingarrote.reservationmanagement.models.entities.Room;
import com.martingarrote.reservationmanagement.repositories.ReservationRepository;
import com.martingarrote.reservationmanagement.repositories.RoomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoomServiceTest {

    @Mock
    RoomRepository repository;

    @Mock
    ModelMapper mapper;

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    RoomService service;

    private Long defaultId;

    @Before
    public void setUp() {
        this.defaultId = 1L;
    }

    @Test
    public void save_ShouldSave() throws Exception {
        // Arrange
        Room room = createRoomEntity();
        Room createdRoom = createRoomEntity();
        RoomDTO roomDTO = createRoomDTO();
        when(mapper.map(roomDTO, Room.class)).thenReturn(room);
        when(repository.save(room)).thenReturn(createdRoom);

        // Act
        Long id = service.save(roomDTO);

        // Asserts
        verify(mapper).map(roomDTO, Room.class);
        verify(repository, times(1)).save(room);
        verifyNoMoreInteractions(repository);
        assertThat(id).isEqualTo(defaultId);
    }

    @Test
    public void save_ShouldNotSave() throws Exception {
        RoomDTO roomDTO = createRoomDTO();

        Throwable exception = catchThrowable(() -> service.save(roomDTO));

        assertThat(exception.getMessage()).isEqualTo(INSERT_ERROR);
    }

    @Test
    public void create_ShouldCreate() throws Exception {
        // Arrange
        Room room = createRoomEntity();
        Room createdRoom = createRoomEntity();
        RoomDTO roomDTO = createRoomDTO();
        when(mapper.map(roomDTO, Room.class)).thenReturn(room);
        when(repository.save(room)).thenReturn(createdRoom);

        // Act
        Long id = service.create(roomDTO);

        // Asserts
        assertThat(id).isEqualTo(defaultId);
    }

    @Test(expected = Exception.class)
    public void create_ShouldNotCreate() throws Exception {
        RoomDTO roomDTO = createRoomDTO();

        when(service.save(roomDTO)).thenThrow(new Exception());
    }

    @Test
    public void listAll_ShouldList() {
        List<RoomDTO> roomsDTO = repository.findAll().stream()
                .map(room -> mapper.map(room, RoomDTO.class)).toList();
        when(repository.findAll().stream()
                .map(room -> mapper.map(room, RoomDTO.class)).toList()).thenReturn(roomsDTO);

        List<RoomDTO> expectedRoomsDTO = service.listAll();

        assertArrayEquals(expectedRoomsDTO.toArray(), roomsDTO.toArray());
    }

    @Test()
    public void listAll_ShouldNotListAnyRoom() {
        List<Room> emptyRoomList = repository.findAll();
        when(repository.findAll()).thenReturn(emptyRoomList);

        List<RoomDTO> returnedRoomsDTO = service.listAll();

        assertArrayEquals(emptyRoomList.toArray(), returnedRoomsDTO.toArray());
    }

    @Test
    public void findById_ShouldGet() {
        Room room = createRoomEntity();
        RoomDTO expectedRoomDTO = mapper.map(room, RoomDTO.class);

        when(repository.findById(defaultId)).thenReturn(Optional.of(room));
        when(mapper.map(room, RoomDTO.class)).thenReturn(expectedRoomDTO);

        RoomDTO returnedRoomDTO = service.findById(defaultId);

        assertThat(returnedRoomDTO).isEqualTo(expectedRoomDTO);
    }

    @Test
    public void findById_ShouldNotGetAnyRoom() {
        when(repository.findById(defaultId)).thenReturn(Optional.empty());

        RoomDTO returnedRoomDTO = service.findById(defaultId);

        assertThat(returnedRoomDTO).isNull();
    }

    @Test
    public void findByBusy_ShouldGetBusyRooms() {
        Room roomBusy = createRoomEntity();
        roomBusy.setBusy(true);

        List<RoomDTO> expectedRoomsDTO = repository.findByBusy(true).stream()
                .map(room -> mapper.map(room, RoomDTO.class)).toList();

        when(repository.findByBusy(true).stream()
                .map(room -> mapper.map(room, RoomDTO.class)).toList()).thenReturn(expectedRoomsDTO);

        List<RoomDTO> receivedRoomsDTO = service.findByBusy(true);

        System.out.println(receivedRoomsDTO);

        assertArrayEquals(receivedRoomsDTO.toArray(), expectedRoomsDTO.toArray());
    }

    @Test
    public void findByBusy_ShouldNotGetAnyRoom() {
        List<RoomDTO> expectedRoomsDTO = List.of();
        when(repository.findByBusy(true).stream()
                .map(room -> mapper.map(room, RoomDTO.class)).toList()).thenReturn(expectedRoomsDTO);
        when(repository.findByBusy(false).stream()
                .map(room -> mapper.map(room, RoomDTO.class)).toList()).thenReturn(expectedRoomsDTO);

        List<RoomDTO> returnedNotBusyRoomsDTO = service.findByBusy(false);
        List<RoomDTO> returnedBusyRoomsDTO = service.findByBusy(true);

        assertThatList(expectedRoomsDTO).isEqualTo(returnedNotBusyRoomsDTO);
        assertThatList(expectedRoomsDTO).isEqualTo(returnedBusyRoomsDTO);
    }

    @Test
    public void deleteById_ShouldDelete() throws Exception {
        when(repository.existsById(defaultId)).thenReturn(true);

        boolean returned = service.deleteById(defaultId);

        assertThat(returned).isTrue();
    }

    @Test
    public void deleteById_ShouldNotDelete_NotFound() throws Exception {
        when(repository.existsById(defaultId)).thenReturn(false);

        boolean returned = service.deleteById(defaultId);

        assertThat(returned).isFalse();
    }

    @Test
    public void deleteById_ShouldNotDelete_UnableToDeleteException() {
        Room room = createRoomEntity();
        Reservation reservation = new Reservation(1, "ABC123", null, room, 100.0,
                "Test reservation", 3, LocalDate.now(),
                LocalDate.now().plusDays(3), true);
        when(repository.existsById(defaultId)).thenReturn(true);
        when(reservationRepository.findByReservedRoomId(defaultId)).thenReturn(List.of(reservation));

        Throwable exception = catchThrowable(() -> service.deleteById(defaultId));

        assertThat(exception.getMessage()).isEqualTo(UNABLE_TO_DELETE);
    }

    @Test
    public void update_ShouldUpdate() throws Exception {
        Room roomToUpdate = createRoomEntity();
        RoomDTO roomDTO = createRoomDTO();
        when(repository.findById(defaultId)).thenReturn(Optional.of(roomToUpdate));

        Long returnedId = service.update(roomDTO, defaultId);

        assertThat(returnedId).isEqualTo(defaultId);
    }

    @Test
    public void update_ShouldNotUpdate_NotFound() throws Exception {
        RoomDTO roomDTO = createRoomDTO();

        var returnedByService = service.update(roomDTO, defaultId);

        assertThat(returnedByService).isNull();
    }

    @Test
    public void update_ShouldNotUpdate_UpdateError() {
        RoomDTO roomDTO = createRoomDTO();

        when(repository.existsById(defaultId)).thenReturn(true);

        Throwable exception = catchThrowable(() -> service.update(roomDTO, defaultId));

        assertThat(exception.getMessage()).isEqualTo(UPDATE_ERROR);
    }

    public Room createRoomEntity() {
        return new Room(1L, 1, "Room 1", 30d, 500d, true, true);
    }

    public RoomDTO createRoomDTO() {
        return new RoomDTO(1L, 1, "Room 1", 30D, 500D, true, true);
    }
}
