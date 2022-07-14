package com.application.meetingroom.repository;

import com.application.meetingroom.model.Reservations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReservationsRepository extends MongoRepository<Reservations,Integer> {
    Optional<Reservations> findByRoomId(int roomId);
    @Query(value="{'startTime':{$gt:?0}}",fields = "{'reservationId':1, {$set:{'}}}")
    void updateReservationData(LocalDateTime date);
}
