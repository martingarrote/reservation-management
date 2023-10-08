package com.martingarrote.reservationmanagement.services;

import com.martingarrote.reservationmanagement.models.dtos.RoomDTO;
import com.martingarrote.reservationmanagement.models.entities.Room;
import com.martingarrote.reservationmanagement.repositories.ReservationRepository;
import com.martingarrote.reservationmanagement.repositories.RoomRepository;
import com.martingarrote.reservationmanagement.utils.AuditUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.*;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    ReservationRepository reservationRepository;

    public List<RoomDTO> listAll() {
        List<RoomDTO> roomsDTO = roomRepository.findAll().stream()
                .map(room -> mapper.map(room, RoomDTO.class)).toList();

        return roomsDTO;
    }

    public RoomDTO findById(Long id) {
        Optional<Room> optional = roomRepository.findById(id);
        RoomDTO roomDTO = null;

        if (optional.isPresent()) {
            roomDTO = mapper.map(optional.get(), RoomDTO.class);
        }

        return roomDTO;
    }

    public List<RoomDTO> findByBusy(boolean busy) {
        List<RoomDTO> roomsDTO = roomRepository.findByBusy(busy).stream()
                .map(room -> mapper.map(room, RoomDTO.class)).toList();

        return roomsDTO;
    }

    public boolean deleteById(Long id) throws Exception {

        if (!roomRepository.existsById(id)) {
            return false;
        }

        if (!reservationRepository.findByReservedRoomId(id).isEmpty()) {
            throw new Exception(UNABLE_TO_DELETE);
        }

        roomRepository.deleteById(id);
        return true;
    }

    public Long create(RoomDTO roomDTO) throws Exception {
        return save(roomDTO);
    }

    public Long update(RoomDTO roomDTO, Long id) throws Exception {
        var optional = roomRepository.findById(id);
        AuditUtils auditUtils = new AuditUtils();

        if (optional.isPresent()) {
            Room room = optional.get();

            if (!roomDTO.getNumber().toString().isEmpty()) {
                room.setNumber(roomDTO.getNumber());
            }
            if (!roomDTO.getDescription().isEmpty()) {
                room.setDescription(roomDTO.getDescription());
            }
            if (!roomDTO.getSize().toString().isEmpty()) {
                room.setSize(roomDTO.getSize());
            }
            if (!roomDTO.getPricePerMonth().toString().isEmpty()) {
                room.setPricePerMonth(roomDTO.getPricePerMonth());
            }
            if (!roomDTO.getBusy().toString().isEmpty()) {
                room.setBusy(roomDTO.getBusy());
            }
            if (!roomDTO.getActive().toString().isEmpty()) {
                room.setActive(roomDTO.getActive());
            }

            auditUtils.AuditDefineFields(room);

            roomRepository.save(room);
            return room.getId();

        } else {
            if (!roomRepository.existsById(id)) {
                return null;
            }
            throw new Exception(UPDATE_ERROR);
        }
    }

    public Long save(RoomDTO roomDTO) throws Exception {

        AuditUtils auditUtils = new AuditUtils();

        // business rules and verifications

        try {
            Room room = mapper.map(roomDTO, Room.class);

            auditUtils.AuditDefineFields(room);

            Room created = roomRepository.save(room);

            return created.getId();
        } catch (Exception e) {
            throw new Exception(INSERT_ERROR);
        }
    }
}
