package com.application.meetingroom.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.application.meetingroom.exception.ResourceNotFoundException;
import com.application.meetingroom.model.MeetingRoom;
import com.application.meetingroom.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService{

    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    public MeetingRoomServiceImpl(MeetingRoomRepository meetingRoomRepository) {
        super();
        this.meetingRoomRepository = meetingRoomRepository;
    }

    public MeetingRoomServiceImpl() {
        super();
        // TODO Auto-generated constructor stub
    }


    @Override
    public void createMeetingRoom(MeetingRoom meetingroom) {
        meetingRoomRepository.save(meetingroom);
    }

    @Override
    public List<MeetingRoom> getMeetingRoom() {
        return meetingRoomRepository.findAll();
    }

    @Override
    public MeetingRoom findById(Integer id) {
        Optional<MeetingRoom> optMeetingRoom = meetingRoomRepository.findById(id); // returns java8 optional
        if (optMeetingRoom.isPresent()) {
            return optMeetingRoom.get();
        } else {
            throw new ResourceNotFoundException("MeetingRoom", "Id", id);
        }
    }

    @Override
    public MeetingRoom update(MeetingRoom meetingroom, Integer id) {
        Optional<MeetingRoom> optMeetingRoom = meetingRoomRepository.findById(id);
        if (optMeetingRoom.isPresent()) {
            MeetingRoom newRoom = optMeetingRoom.get();
            newRoom.setRoomName(meetingroom.getRoomName());
            newRoom.setCapacity(meetingroom.getCapacity());
            newRoom.setStatus(meetingroom.getStatus());
            newRoom.setFromTime(meetingroom.getFromTime());
            newRoom.setToTime(meetingroom.getToTime());

            MeetingRoom updatedroom = meetingRoomRepository.save(newRoom);
            return updatedroom;
        } else {
            throw new ResourceNotFoundException("MeetingRoom", "Id",id);
        }
    }

    public void updateAvailability(Integer id, LocalDateTime startTime, LocalDateTime endTime) {
        List<Optional<MeetingRoom>> room = Collections.singletonList(meetingRoomRepository.findById(id));
        MeetingRoom mr = new MeetingRoom();
        if (!room.isEmpty()) {
            room.stream().forEach(r->{
                mr.setId(id);
                mr.setRoomName(r.get().getRoomName());
                mr.setCapacity(r.get().getCapacity());
                mr.setStatus("occupied");
                if(r.get().getFromTime()!=null){
                    mr.setFromTime(r.get().getFromTime());
                }
                else{
                    mr.setFromTime(startTime);
                }
                mr.setToTime(endTime);
            });
            meetingRoomRepository.save(mr);

        } else {
            throw new ResourceNotFoundException("MeetingRoom", "Id",id);
        }
    }

    @Override
    public Boolean deleteMeetingRoomById(Integer id) {
        Optional<MeetingRoom> optMeetingRoom = meetingRoomRepository.findById(id);
        if (optMeetingRoom.isPresent()) {
            meetingRoomRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("MeetingRoom", "Id",id);
        }
    }

    @Override
    public List<MeetingRoom> findMeetingRoomAvailable(String status) {
        return (List<MeetingRoom>) meetingRoomRepository.findByStatus(status);
    }
}

//db.runCommand({
//        createIndex:"reservation",
//        writeConcern:{expireAfterSeconds:1,partialFilterExpression:{toTime:{$lt:new Date()}}}
//        })