package com.TrainTicketBooking.repository;

import com.TrainTicketBooking.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepo extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findBySeat(String seat);
//    Ticket findTopBySeatStartingWithOrderBySeatDesc(String section);
//    Ticket findTopByOrderBySeatDesc();
}
