package com.application.meetingroom.controller;

import com.application.meetingroom.model.MeetingRoom;
import com.application.meetingroom.model.Reservations;
import com.application.meetingroom.repository.MeetingRoomRepository;
import com.application.meetingroom.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReservationController {
    @Autowired
    ReservationService rs;

    @Autowired
    MeetingRoomRepository mrr;

    @PostMapping("/createReservation")
    public String createReservation(@RequestBody Reservations reservation){
        if(rs.createReservation(reservation)!=null){
            return "Reservation done successfully";
        }else{
            return "The room looking for is not available currently, look for another room or book the same room after sometime";
        }
    }

    @PutMapping("/updateReservation/{id}")
    public Reservations update(@RequestBody Reservations reservations,@PathVariable Integer id){
        return rs.update(reservations,id);
    }

    @GetMapping("/getReservations")
    public List<Reservations> getReservations(){
        return rs.getReservations();
    }

    @GetMapping("/getReservationById/{id}")
    public Reservations findById(@PathVariable Integer id){
        return rs.findById(id);
    }

    @DeleteMapping("/deleteReservation/{id}")
    public Boolean deleteReservationsById(@PathVariable Integer id){
        return rs.deleteReservationsById(id);
    }

    @GetMapping("/viewAllUnoccupiedRooms")
    public List<MeetingRoom> getUnoccupiedRooms(){
        return mrr.findByOccupiedRooms();
    }
}
