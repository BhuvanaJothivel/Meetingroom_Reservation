package com.application.meetingroom.services;

import com.application.meetingroom.model.MeetingRoom;
import com.application.meetingroom.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class SchedulerClass {
    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    @Scheduled(cron = "0 0/30 * * * *")
    public List<MeetingRoom> getMeetingRoom() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now().plusSeconds(1);
        System.out.println("-----------------------------------------------");
        System.out.println("The meeting room status at: "+dtf.format(now));
        System.out.println("-----------------------------------------------");
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
