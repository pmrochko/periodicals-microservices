package com.periodicals.paymentservice.repository.remote;

import com.periodicals.paymentservice.model.dto.UserDTO;

import java.util.Optional;

/**
 * @author Pavlo Mrochko
 */
public interface UserRepository {

    Optional<UserDTO> getUserById (Long userId);

}
