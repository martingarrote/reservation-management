package com.martingarrote.reservationmanagement.repositories;

import com.martingarrote.reservationmanagement.models.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.active = :active " +
            "OR r.endDate BETWEEN CURRENT_DATE AND :endsIn")
    List<Reservation> search(@Param("active") Boolean active, @Param("endsIn") LocalDate endsIn);

    List<Reservation> findByCustomerId(Long customerId);


    List<Reservation> findByReservedRoomId(Long roomId);
}