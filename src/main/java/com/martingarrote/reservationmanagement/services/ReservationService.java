package com.martingarrote.reservationmanagement.services;

import com.martingarrote.reservationmanagement.models.dtos.ReservationDTO;
import com.martingarrote.reservationmanagement.models.entities.Customer;
import com.martingarrote.reservationmanagement.models.entities.Reservation;
import com.martingarrote.reservationmanagement.models.entities.Room;
import com.martingarrote.reservationmanagement.repositories.ReservationRepository;
import com.martingarrote.reservationmanagement.utils.AuditUtils;
import com.martingarrote.reservationmanagement.utils.MathUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.ReservationExceptions.*;
import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.RoomExceptions.ROOM_IS_BUSY;
import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.UPDATE_ERROR;
import static com.martingarrote.reservationmanagement.utils.LocalDateUtils.getFutureDateBasedInMonthsQuantity;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ModelMapper mapper;

    public List<ReservationDTO> listAll() {
        List<ReservationDTO> reservationsDTO = reservationRepository.findAll().stream()
                .map(reservation -> mapper.map(reservation, ReservationDTO.class)).toList();

        return reservationsDTO;
    }

    public ReservationDTO findById(Long id) {
        Optional<Reservation> optional = reservationRepository.findById(id);
        ReservationDTO reservationDTO = null;

        if (optional.isPresent()) {
            reservationDTO = mapper.map(optional.get(), ReservationDTO.class);
        }

        return reservationDTO;
    }

    public List<ReservationDTO> search(Boolean active, Integer endsIn) {
        LocalDate endsInDate = getFutureDateBasedInMonthsQuantity(endsIn);

        List<ReservationDTO> reservationDTOS = reservationRepository
                .search(active, endsInDate)
                .stream()
                .map(reservation -> mapper.map(reservation, ReservationDTO.class))
                .toList();

        return reservationDTOS;
    }

    public List<ReservationDTO> findByCustomer(Long id) {
        List<ReservationDTO> reservationDTOS = reservationRepository
                .findByCustomerId(id)
                .stream()
                .map(reservation -> mapper.map(reservation, ReservationDTO.class))
                .toList();

        return reservationDTOS;
    }

    public boolean deleteById(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Long create(ReservationDTO reservationDTO) throws Exception {
        return save(reservationDTO);
    }

    public Long update(ReservationDTO reservationDTO, Long id) throws Exception {
        var optional = reservationRepository.findById(id);
        AuditUtils auditUtils = new AuditUtils();

        if (optional.isPresent()) {
            Reservation reservation = optional.get();

            if (!reservationDTO.getCode().isEmpty()) {
                reservation.setCode(reservationDTO.getCode());
            }
            if (!reservationDTO.getCustomer().toString().isEmpty()) {
                Customer customer = mapper.map(reservationDTO.getCustomer(), Customer.class);
                reservation.setCustomer(customer);
            }
            if (!reservationDTO.getReservedRoom().toString().isEmpty()) {
                Room room = mapper.map(reservationDTO.getReservedRoom(), Room.class);
                reservation.setReservedRoom(room);
            }
            if (!reservationDTO.getReservationPrice().toString().isEmpty()) {
                reservation.setReservationPrice(reservationDTO.getReservationPrice());
            }
            if (!reservationDTO.getDescription().isEmpty()) {
                reservation.setDescription(reservationDTO.getDescription());
            }
            if (!reservationDTO.getDuration().toString().isEmpty()) {
                reservation.setDuration(reservationDTO.getDuration());
            }
            if (reservationDTO.getStartDate() != null) {
                reservation.setStartDate(reservationDTO.getStartDate());
            }
            if (reservationDTO.getEndDate() != null) {
                reservation.setEndDate(reservationDTO.getStartDate());
            }
            if (!reservationDTO.getActive().toString().isEmpty()) {
                reservation.setActive(reservationDTO.getActive());
            }

            auditUtils.AuditDefineFields(reservation);

            reservationRepository.save(reservation);
            return reservation.getId();

        } else {
            if (!reservationRepository.existsById(id)) {
                return null;
            }
            throw new Exception(UPDATE_ERROR);
        }
    }

    public Long save(ReservationDTO reservationDTO) throws Exception {

        AuditUtils auditUtils = new AuditUtils();
        MathUtils mathUtils = new MathUtils();

        LocalDate futureDate = getFutureDateBasedInMonthsQuantity(reservationDTO.getDuration());
        Double expectedPrice = reservationDTO.getReservedRoom().getPricePerMonth() * reservationDTO.getDuration();

        if (!reservationDTO.getReservationPrice().equals(expectedPrice)) {
            reservationDTO.setReservationPrice(expectedPrice);
        }

        if (!futureDate.equals(reservationDTO.getEndDate())) {
            reservationDTO.setEndDate(futureDate);
        }

        if (reservationDTO.getDuration() >= DISCOUNT_MONTHS) {
            mathUtils.calculateValueWithDiscount(
                    reservationDTO.getReservationPrice(),
                    DISCOUNT_PERCENTAGE
            );
        }

        if (reservationDTO.getDuration() >= MAX_RESERVATION_DURATION) {
            throw new Exception(DURATION_OUT_OF_ALLOWED_RANGE);
        }

        if (!reservationDTO.getReservedRoom().getBusy()) {
            reservationDTO.getReservedRoom().setBusy(true);
        } else {
            throw new Exception(ROOM_IS_BUSY);
        }

        try {
            Reservation reservation = mapper.map(reservationDTO, Reservation.class);

            auditUtils.AuditDefineFields(reservation);

            Reservation created = reservationRepository.save(reservation);

            return created.getId();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
