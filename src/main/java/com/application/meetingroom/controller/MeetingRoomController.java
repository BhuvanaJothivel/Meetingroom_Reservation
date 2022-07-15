package com.application.meetingroom.controller;

import com.application.meetingroom.model.MeetingRoom;
import com.application.meetingroom.repository.MeetingRoomRepository;
import com.application.meetingroom.services.MeetingRoomService;
import com.application.meetingroom.services.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/room")
public class MeetingRoomController {
    @Autowired
    MeetingRoomService mrs;

    @Autowired
    MeetingRoomRepository mrr;

    @Autowired
    SequenceGeneratorService sgs;

    @PostMapping("/createRoom")
    public String createMeetingRoom(@RequestBody MeetingRoom mr){
        mrs.createMeetingRoom(mr);
        return "Room has created Successfully";
    }

    @GetMapping("/getRooms")
    public List<MeetingRoom> getMeetingRoom(){
        return mrs.getMeetingRoom();
    }

    @GetMapping("/getRoomById/{id}")
    public MeetingRoom findById(@PathVariable Integer id){
        return mrs.findById(id);
    }

    @PutMapping("/updateRoom/{id}")
    public MeetingRoom update(@RequestBody MeetingRoom meetingroom,@PathVariable Integer id){
        return mrs.update(meetingroom,id);
    }

    @DeleteMapping("/deleteRoom/{id}")
    public Boolean deleteMeetingRoomById(@PathVariable Integer id){
        return mrs.deleteMeetingRoomById(id);
    }

    @GetMapping("/getRoomByAvailability/{status}")
    public List<MeetingRoom> findMeetingRoomAvailable(@PathVariable String status){
        return mrs.findMeetingRoomAvailable(status);
    }

}
