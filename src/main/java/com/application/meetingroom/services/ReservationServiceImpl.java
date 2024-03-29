package com.application.meetingroom.services;

import com.application.meetingroom.exception.ResourceNotFoundException;
import com.application.meetingroom.model.MeetingRoom;
import com.application.meetingroom.model.Reservations;
import com.application.meetingroom.repository.MeetingRoomRepository;
import com.application.meetingroom.repository.ReservationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.application.meetingroom.model.Reservations.SEQUENCE_NAME;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationsRepository reservationsRepository;
    @Autowired
    MeetingRoomRepository meetingRoomRepository;
    @Autowired
    MeetingRoomServiceImpl mrsi;
    @Autowired
    SequenceGeneratorService sgs;

    public ReservationServiceImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ReservationServiceImpl(com.application.meetingroom.repository.ReservationsRepository reservationsRepository) {
        super();
        reservationsRepository = reservationsRepository;
    }

    @Override
    public List<Reservations> getReservations() {
        return (List<Reservations>) reservationsRepository.findAll();
    }

    @Override
    public Reservations findById(Integer id) {
        Optional<Reservations> optReservation = reservationsRepository.findById(id);
        if (optReservation.isPresent()) {
            return optReservation.get();
        } else {
            throw new ResourceNotFoundException("Reservation", "Id", id);
        }
    }

    @Override
    public Boolean deleteReservationsById(Integer id) {
        Optional<Reservations> optReservation = reservationsRepository.findById(id);
        if (optReservation.isPresent()) {
            List<MeetingRoom> rooms = meetingRoomRepository.findAll();
            MeetingRoom meetroom = new MeetingRoom();
            for(MeetingRoom mr: rooms){
                if(mr.getId() == optReservation.get().getRoomId()) {
                    meetroom.setId(mr.getId());
                    meetroom.setRoomName(mr.getRoomName());
                    meetroom.setCapacity(mr.getCapacity());
                    meetroom.setStatus("unoccupied");
                    meetroom.setFromTime(null);
                    meetroom.setToTime(null);
                    meetingRoomRepository.save(meetroom);
                }
            }
            reservationsRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("Reservation", "Id", id);
        }
    }

    @Scheduled(fixedRate = 1000)
    @Override
    public void deleteReservationAutomatically() {
        List<Reservations> lr = reservationsRepository.findAll();
        for(Reservations reser: lr){
            if(reser.getStartTime().plusMinutes(reser.getDurationInMin()).compareTo(LocalDateTime.now())<0) {
                System.out.println("About to delete");
                List<MeetingRoom> rooms = meetingRoomRepository.findAll();
                MeetingRoom meetroom = new MeetingRoom();
                rooms.stream().forEach(r->{
                    if(r.getId() == reser.getRoomId() && r.getToTime().compareTo(reser.getStartTime().plusMinutes(reser.getDurationInMin()))==0) {
                        meetroom.setId(r.getId());
                        meetroom.setRoomName(r.getRoomName());
                        meetroom.setCapacity(r.getCapacity());
                        meetroom.setStatus("unoccupied");
                        meetroom.setFromTime(null);
                        meetroom.setToTime(null);
                        meetingRoomRepository.save(meetroom);
                    }
                });
                reservationsRepository.deleteById(reser.getReservationId());
            }
        }
    }

    @Override
    public Reservations createReservation(Reservations reservation) {
        List<Optional<MeetingRoom>> room = Collections.singletonList(meetingRoomRepository.findById(reservation.getRoomId()));
        Reservations r = null;
        if(!room.isEmpty()){
            for(Optional<MeetingRoom> mroom: room){
                LocalDateTime st = reservation.getStartTime();
                LocalDateTime newst = null;
                LocalDateTime newet = null;
                long min = 0;
                if(mroom.get().getToTime()!=null && mroom.get().getFromTime()!=null) {
                    newst = mroom.get().getFromTime();
                    Duration dur = Duration.between(mroom.get().getToTime(),reservation.getStartTime());
                    min = dur.toMinutes();
                }
                else{
                    newst = reservation.getStartTime();
                }
                newet = reservation.getStartTime().plusMinutes(reservation.getDurationInMin());
                if((mroom.get().getStatus().equals("unoccupied")) || (min>0)) {
                    reservation.setReservationId(sgs.getSequenceNumber(SEQUENCE_NAME));
                    r = reservationsRepository.save(reservation);
                    mrsi.updateAvailability(reservation.getRoomId(), newst, newet);
                }
            }
            return r;
        }
        else
            return null;
    }

    @Override
    public Reservations update(Reservations reservations, Integer id) {
        Optional<Reservations> optReservations = reservationsRepository.findById(id);
        if (optReservations.isPresent()) {
            Reservations reserve = optReservations.get();
            reserve.setUserId(reservations.getUserId());
            reserve.setRoomId(reservations.getRoomId());
            reserve.setPurpose(reservations.getPurpose());
            reserve.setDurationInMin(reservations.getDurationInMin());
            reserve.setStartTime(reservations.getStartTime());
            Reservations updatedReservation = reservationsRepository.save(reserve);
            return updatedReservation;
        } else {
            throw new ResourceNotFoundException("Reservations", "Id",id);
        }
    }
}