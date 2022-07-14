package com.application.meetingroom.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "meetingroom")
public class MeetingRoom {
    @Id
    private int id;
    private String roomName;
    private String capacity;
    private String status;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
}

