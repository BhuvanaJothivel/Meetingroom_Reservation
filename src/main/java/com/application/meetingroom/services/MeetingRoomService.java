package com.application.meetingroom.services;

import java.util.List;
import com.application.meetingroom.model.MeetingRoom;
import org.springframework.stereotype.Service;

@Service
public interface MeetingRoomService {

    public void createMeetingRoom(MeetingRoom meetingroom);
    public List<MeetingRoom> getMeetingRoom();
    public MeetingRoom findById(Integer id);
    public MeetingRoom update(MeetingRoom meetingroom, Integer id);
    public Boolean deleteMeetingRoomById(Integer id);
    List<MeetingRoom> findMeetingRoomAvailable(String status);

}

