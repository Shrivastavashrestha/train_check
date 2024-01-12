package com.TrainTicketBooking.service;

import com.TrainTicketBooking.config.TicketDefaults;
import com.TrainTicketBooking.payload.TicketDTO;
import com.TrainTicketBooking.entities.Ticket;
import com.TrainTicketBooking.entities.User;
import com.TrainTicketBooking.exceptions.APIException;
import com.TrainTicketBooking.exceptions.ResourceNotFoundException;
import com.TrainTicketBooking.repository.TicketRepo;
import com.TrainTicketBooking.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketServiceImpl implements TicketService{

    private TicketRepo ticketRepo;
    private ModelMapper modelMapper;
    private UserRepo userRepo;

    private final TicketDefaults ticketDefaults;

//    private static final double TICKET_PRICE = 20.0;
//    private static final String FROM_LOCATION = "London";
//    private static final String TO_LOCATION = "France";
//    private static final int MAX_SEATS_PER_SECTION = 10;

    public TicketServiceImpl(TicketRepo ticketRepo, ModelMapper modelMapper, UserRepo userRepo, TicketDefaults ticketDefaults) {
        this.ticketRepo = ticketRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.ticketDefaults = ticketDefaults;
    }

    @Override
    public TicketDTO purchaseTicket(Long userId) {
        Ticket ticket = new Ticket();
        ticket.setDepartureFrom(ticketDefaults.getFromLocation());
        ticket.setDepartureTo(ticketDefaults.getToLocation());
        ticket.setPrice(ticketDefaults.getTicketPrice());
        ticket.setSeat(assignSeat());

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        ticket.setUser(user);

        Ticket savedTicket = ticketRepo.save(ticket);
        return modelMapper.map(savedTicket, TicketDTO.class);
    }

    @Override
    public TicketDTO getTicketDetails(Long ticketId) {
        Ticket ticket = ticketRepo.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket", "ticketId", ticketId));
        return modelMapper.map(ticket, TicketDTO.class);
    }


    @Override
    public TicketDTO modifySeat(Long ticketId, String newSeat) {
        String seatPattern = "^(A[1-9]|A10|B[1-9]|B10)$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(seatPattern);

        // Create a Matcher to match the newSeat against the pattern
        Matcher matcher = pattern.matcher(newSeat);

        if (!matcher.matches()) {
            throw new APIException("Seat " + newSeat + " is not in the format A1 to A10 or B1 to B10");
        }

        if (!isSeatAvailable(newSeat)) {
            throw new APIException("Seat "+newSeat+" is not available");
        }
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "ticketId", ticketId));
        ticket.setSeat(newSeat);
        Ticket updatedTicket = ticketRepo.save(ticket);
        return modelMapper.map(updatedTicket, TicketDTO.class);
    }

    private boolean isSeatAvailable(String seat) {
        return ticketRepo.findBySeat(seat).isEmpty();
    }

    public List<String> getAvailableSeats() {
        //Creating an empty ArrayList called availableSeats to store the available seat labels.
        List<String> availableSeats = new ArrayList<>();
        //Using a loop to iterate from 1 to the maximum number of seats per section
        for (int i = 1; i <= ticketDefaults.getMaxSeatsPerSection(); i++) {
        //Create two seat labels, seatA and seatB, by appending the seat number to "A" and "B" respectively.
            String seatA = "A" + i;
            String seatB = "B" + i;
        //check if a ticket with seat label seatA exists in the repository.
            //If the result is empty (no existing ticket for that seat), add seatA to the availableSeats list.
            if (ticketRepo.findBySeat(seatA).isEmpty()) {
                availableSeats.add(seatA);
            }
            if (ticketRepo.findBySeat(seatB).isEmpty()) {
                availableSeats.add(seatB);
            }
        }
        return availableSeats;
    }

    @Override
    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets = ticketRepo.findAll();
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(ticket -> modelMapper.map(ticket, TicketDTO.class)
                ).collect(Collectors.toList());
        return ticketDTOs;
    }

    @Override
    public String removeTicket(Long ticketId) {
        ticketRepo.deleteById(ticketId);
        return "Ticket with ticketId: "+ticketId+" deleted successfully!";
    }

    @Override
    public String deleteAllTickets() {
        ticketRepo.deleteAll();
        return "All tickets deleted successfully!";
    }

//    private String assignSeat() {
//        List<Ticket> tickets = ticketRepo.findAll(Sort.by(Sort.Direction.DESC, "seat"));
//
//        int countA = 0;
//        int countB = 0;
//
//        for (Ticket ticket : tickets) {
//            String seat = ticket.getSeat();
//            if (seat.startsWith("A")) {
//                countA++;
//            } else if (seat.startsWith("B")) {
//                countB++;
//            }
//        }
//
//        if (countA < ticketDefaults.getMaxSeatsPerSection()) {
//            return "A" + (countA + 1);
//        } else if (countB < ticketDefaults.getMaxSeatsPerSection()) {
//            return "B" + (countB + 1);
//        } else {
//            throw new RuntimeException("No available seats");
//        }
//    }

    private String assignSeat() {
        List<Ticket> tickets = ticketRepo.findAll(Sort.by(Sort.Direction.ASC, "seat"));
        //Create two boolean arrays, sectionA and sectionB,
        // each representing the availability status of seats in the corresponding section.
        boolean[] sectionA = new boolean[ticketDefaults.getMaxSeatsPerSection()];
        boolean[] sectionB = new boolean[ticketDefaults.getMaxSeatsPerSection()];

        // Mark the occupied seats in both sections
        for (Ticket ticket : tickets) {
            String seat = ticket.getSeat();
            if (seat.startsWith("A")) {
                sectionA[Integer.parseInt(seat.substring(1)) - 1] = true;

            } else if (seat.startsWith("B")) {
                sectionB[Integer.parseInt(seat.substring(1)) - 1] = true;
            }
        }

        // Find the first available seat in Section A
        //Iterate through the sectionA array to find the first available seat.
        //If a seat is not occupied (!sectionA[i]), return the seat as "A" + (i + 1).
        for (int i = 0; i < sectionA.length; i++) {
            if (!sectionA[i]) {
                return "A" + (i + 1);
            }
        }

        // Find the first available seat in Section B
        //If all seats in Section A are occupied, iterate through the sectionB array to find the first available seat in Section B.
        //If a seat is not occupied (!sectionB[i]), return the seat as "B" + (i + 1).
        for (int i = 0; i < sectionB.length; i++) {
            if (!sectionB[i]) {
                return "B" + (i + 1);
            }
        }

        throw new APIException("No seats are available.");
    }


}
