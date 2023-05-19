package com.periodicals.userservice.service.impl;

import com.periodicals.userservice.model.dto.UserDTO;
import com.periodicals.userservice.model.entity.User;
import com.periodicals.userservice.model.exception.EntityNotFoundException;
import com.periodicals.userservice.model.exception.NotUniqueParameterException;
import com.periodicals.userservice.model.mapper.UserMapper;
import com.periodicals.userservice.repository.UserRepository;
import com.periodicals.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            log.warn("Email has already exist");
            throw new NotUniqueParameterException("Email has already exist");
        }
        if (userRepository.existsByPhone(userDTO.getPhone())) {
            log.warn("Phone number has already exist");
            throw new NotUniqueParameterException("Phone number has already exist");
        }

        User user = UserMapper.INSTANCE.mapToUser(userDTO);
        user = userRepository.save(user);
        log.info("New user was created successfully");
        return UserMapper.INSTANCE.mapToUserDto(user);
    }

    @Override
    public UserDTO getUserByID(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));
        log.info("Successful getting a user from the repository");
        return UserMapper.INSTANCE.mapToUserDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();
        log.info("Successful getting a list of users from the repository");
        return UserMapper.INSTANCE.mapToListOfUsersDto(userList);
    }

    @Override
    public void updateUser(long userId, UserDTO userDTO) {

        /*User authenticatedUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not authenticated"));

        if (authenticatedUser.getUserRole().equals(UserRole.ROLE_READER) &&
                authenticatedUser.getId() != userId) {
            log.warn("Student(id:{}) does not have access to other users", userId);
            throw new DoesNotHaveAccessException("Student does not have access to other users");
        }*/

        if (!userRepository.existsById(userId)) {
            log.warn("User(id:{}) was not found", userId);
            throw new EntityNotFoundException("User was not found");
        }

        userRepository.updateUser(
                userDTO.getEmail(),
                userDTO.getUserRole(),
                userDTO.getName(),
                userDTO.getSurname(),
                userDTO.getPhone(),
                userDTO.getAddress(),
                userDTO.getAccess(),
                userId
        );

        log.info("Successful updating a user in the repository");

    }

}
