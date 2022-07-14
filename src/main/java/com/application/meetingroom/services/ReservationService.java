package com.application.meetingroom.services;

import com.application.meetingroom.model.MeetingRoom;
import com.application.meetingroom.model.Reservations;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface ReservationService {
    Reservations createReservation(Reservations reservation);

    List<Reservations> getReservations();

    Reservations findById(Integer id);

    Boolean deleteReservationsById(Integer id);

    void deleteReservationAutomatically();

    public Reservations update(Reservations reservations, Integer id);

}

