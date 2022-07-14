package com.application.meetingroom.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.application.meetingroom.exception.ResourceNotFoundException;
import com.application.meetingroom.model.MeetingRoom;
import com.application.meetingroom.model.Reservations;
import com.application.meetingroom.repository.MeetingRoomRepository;
import com.application.meetingroom.repository.ReservationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService{

    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    @Autowired
    ReservationsRepository reservationsRepository;

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
//        String strCmd = "{\n" +
//                "        createIndex:\"meetingroom\",\n" +
//                "        writeConcern:{expireAfterSeconds:1,partialFilterExpression:{toTime:{$lt:new Date()}}}\n" +
//                "        }";
//        Document bsonCmd = Document.parse(strCmd);
//        Document result = db.runCommand​(bsonCmd);
//        Document cursor = (Document) result.get("cursor");
//        List<Document> docs = (List<Document>) cursor.get("firstBatch");
//        docs.forEach(System.out::println);
        return (List<MeetingRoom>) meetingRoomRepository.findAll();
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
            for(Optional<MeetingRoom> mroom: room){
                mr.setId(id);
                mr.setRoomName(mroom.get().getRoomName());
                mr.setCapacity(mroom.get().getCapacity());
                mr.setStatus("occupied");
                if(mroom.get().getFromTime()!=null){
                    mr.setFromTime(mroom.get().getFromTime());
                }
                else{
                    mr.setFromTime(startTime);
                }
                mr.setToTime(endTime);

            }
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

    @Scheduled(cron = "0 0/30 * * * *")
    @Override
    public int findUpdatedMeetingRooms() {
        List<MeetingRoom> rooms = meetingRoomRepository.findAll();
        MeetingRoom meetroom = new MeetingRoom();
        List<MeetingRoom> mroom = new ArrayList<MeetingRoom>();
        int flag = 0;
        for(MeetingRoom mr: rooms){
            if(mr.getToTime()!=null) {
                Duration dur = Duration.between(mr.getToTime(), LocalDateTime.now());
                long min = dur.toMinutes();
                if (min >= 0) {
                    meetroom.setId(mr.getId());
                    meetroom.setRoomName(mr.getRoomName());
                    meetroom.setCapacity(mr.getCapacity());
                    meetroom.setStatus("unoccupied");
                    meetroom.setFromTime(null);
                    meetroom.setToTime(null);
                    MeetingRoom temp = meetingRoomRepository.save(meetroom);
                    mroom.add(temp);
                    flag++;
                }
                else{
                    mroom.add(mr);
                }
            }else{
                mroom.add(mr);
            }
        }
        for(MeetingRoom mr: mroom){
            System.out.println("Room Id: "+mr.getId());
            System.out.println("Room Name: "+mr.getRoomName());
            System.out.println("Room Capacity: "+mr.getCapacity());
            System.out.println("Room Status :"+mr.getStatus());
            System.out.println("Meeting Start Time: "+mr.getFromTime());
            System.out.println("Meeting End Time: "+mr.getToTime()+"\n");
        }
        return flag>0?1:0;
    }
}


//db.runCommand({
//        createIndex:"reservation",
//        writeConcern:{expireAfterSeconds:1,partialFilterExpression:{toTime:{$lt:new Date()}}}
//        })