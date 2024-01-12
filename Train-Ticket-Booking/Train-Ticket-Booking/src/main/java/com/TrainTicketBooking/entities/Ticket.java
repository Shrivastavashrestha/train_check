package com.TrainTicketBooking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;
    private String departureFrom;
    private String departureTo;
    private double price;

    //it indicates that many Ticket entities can be associated with one User.
    @ManyToOne
    //This annotation specifies the foreign key column used to join the Ticket table with the User table. In this example,
    // the user_id column is used as the foreign key.
    @JoinColumn(name = "user_id")
    private User user;

//    @Pattern(regexp = "^(A[1-9]|A10|B[1-9]|B10)$", message = "Seat should be in the format A1 to A10 or B1 to B10")
//    @Schema(description = "Seat should be in the format A1 to A10 or B1 to B10")
    private String seat;
}

