package com.TrainTicketBooking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
//The purpose of this relationship is to represent the fact that each user can have multiple tickets, and each ticket belongs to a specific user
    @OneToMany(mappedBy = "user")
    //List<Ticket> tickets field is used to store the collection of associated Ticket entities for a particular User
    private List<Ticket> tickets = new ArrayList<>();
}