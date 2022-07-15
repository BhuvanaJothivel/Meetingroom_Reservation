package com.application.meetingroom.services;

import com.application.meetingroom.model.MeetingRoom;
import com.application.meetingroom.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerClass {
    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    @Scheduled(cron = "0 0/30 * * * *")
    public List<MeetingRoom> getMeetingRoom() {

        List<MeetingRoom> mrl= meetingRoomRepository.findAll();
        mrl.stream().forEach(l ->{
            System.out.println("Room Id: "+l.getId());
            System.out.println("Room Name: "+l.getRoomName());
            System.out.println("Room Capacity: "+l.getCapacity());
            System.out.println("Room Status :"+l.getStatus());
            System.out.println("Meeting Start Time: "+l.getFromTime());
            System.out.println("Meeting End Time: "+l.getToTime()+"\n");
        });
        return mrl;
    }
}
