package com.TrainTicketBooking.repository;

import com.TrainTicketBooking.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
