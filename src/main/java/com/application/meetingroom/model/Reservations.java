package com.application.meetingroom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reservations")
public class Reservations {
    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";

    @Id
    private int reservationId;
    private String userId;
    private int roomId;
    private String purpose;
    private int durationInMin;
    private LocalDateTime startTime;
}
