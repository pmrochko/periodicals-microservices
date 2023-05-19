package com.periodicals.userservice.service;

import com.periodicals.userservice.model.dto.UserDTO;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserByID(long userId);

    void updateUser(long userId, UserDTO userDTO);

    List<UserDTO> getAllUsers();

}
