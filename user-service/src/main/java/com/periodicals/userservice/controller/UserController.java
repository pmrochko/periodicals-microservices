package com.periodicals.userservice.controller;

import com.periodicals.userservice.model.dto.UserDTO;
import com.periodicals.userservice.model.validation.gpoup.OnCreate;
import com.periodicals.userservice.model.validation.gpoup.OnUpdate;
import com.periodicals.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody @Validated(OnCreate.class) UserDTO userDTO) {
        log.info("Creating a new user with email: {}", userDTO.getEmail());
        return userService.createUser(userDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers() {
        log.info("Getting a list of all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserById(@PathVariable @Positive Long userId) {
        log.info("Getting a user by id: {}", userId);
        return userService.getUserByID(userId);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> updateUser(@PathVariable @Positive Long userId,
                                           @RequestBody @Validated(OnUpdate.class) UserDTO userDTO) {
        log.info("Updating a user with id: {}", userId);
        userService.updateUser(userId, userDTO);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

}
