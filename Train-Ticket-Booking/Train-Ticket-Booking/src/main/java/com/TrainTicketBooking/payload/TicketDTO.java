package com.TrainTicketBooking.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
    private Long ticketId;
    private String departureFrom; // Change the column name
    private String departureTo;
    private double price;
    private UserDTO user;
    private String seat;
}
