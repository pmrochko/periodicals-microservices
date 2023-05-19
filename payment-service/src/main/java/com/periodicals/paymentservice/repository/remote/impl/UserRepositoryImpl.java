package com.periodicals.paymentservice.repository.remote.impl;

import com.periodicals.paymentservice.model.dto.UserDTO;
import com.periodicals.paymentservice.repository.remote.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

/**
 * @author Pavlo Mrochko
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final WebClient webClient;

    public static final String USER_SERVICE_URI = "http://localhost:8083/api/v1/";

    @Override
    public Optional<UserDTO> getUserById(Long userId) {

        UserDTO userDTO = webClient
                .get()
                .uri(USER_SERVICE_URI + "/users/" + userId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();

        return Optional.ofNullable(userDTO);
    }

}
