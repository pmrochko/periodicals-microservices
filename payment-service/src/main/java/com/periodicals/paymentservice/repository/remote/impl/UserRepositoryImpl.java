package com.periodicals.paymentservice.repository.remote.impl;

import com.periodicals.paymentservice.model.dto.UserDTO;
import com.periodicals.paymentservice.model.exception.EntityNotFoundException;
import com.periodicals.paymentservice.model.exception.ServiceException;
import com.periodicals.paymentservice.repository.remote.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Pavlo Mrochko
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final WebClient.Builder webClientBuilder;

    public static final String USER_SERVICE_URI = "lb://user-service/api/v1/";

    @Override
    public Optional<UserDTO> getUserById(Long userId) {

        UserDTO userDTO = webClientBuilder.build()
                .get()
                .uri(USER_SERVICE_URI + "/users/" + userId)
                .retrieve()
                .onStatus(
                        HttpStatus.SERVICE_UNAVAILABLE::equals,
                        response -> Mono.error(new ServiceException("User Service is unavailable, please try again later"))
                )
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> Mono.error(new EntityNotFoundException("User was not found, please enter a valid ID"))
                )
                .bodyToMono(UserDTO.class)
                .block();

        return Optional.ofNullable(userDTO);
    }

}
