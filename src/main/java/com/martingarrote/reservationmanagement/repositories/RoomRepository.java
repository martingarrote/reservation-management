package com.martingarrote.reservationmanagement.repositories;

import com.martingarrote.reservationmanagement.models.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByBusy(boolean busy);

}
