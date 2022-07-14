package com.application.meetingroom.repository;

import com.application.meetingroom.model.MeetingRoom;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeetingRoomRepository extends MongoRepository<MeetingRoom,Integer> {
    @Aggregation(pipeline = {
            "{'$match':{'status':'unoccupied'}}",
    })
    List<MeetingRoom> findByOccupiedRooms();

    List<MeetingRoom> findByStatus(String status);

    @Query(value= "db.getCollection('meetingroom').updateMany({ status:'occupied' }, [ { $replaceWith: { $cond: { if: { $lt: ['$fromTime', new Date()] }, then: { id:'$id', roomName:'$roomName', capacity:'$capacity', status: 'unoccupied', fromTime: null, toTime: null }, else: null } } } ])")
    List<MeetingRoom> findUpdatedRoomsByCurrentDateTime();
}
