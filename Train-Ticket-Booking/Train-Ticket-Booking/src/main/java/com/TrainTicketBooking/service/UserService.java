package com.TrainTicketBooking.service;

import com.TrainTicketBooking.payload.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserByUserId(Long userId);

    List<UserDTO> getAllUser();

    UserDTO updateUser(Long userId, UserDTO userDTO);

    String deleteUserByUserId(Long userId);
}
