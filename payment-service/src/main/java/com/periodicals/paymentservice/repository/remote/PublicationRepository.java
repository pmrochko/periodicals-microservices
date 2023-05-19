package com.periodicals.paymentservice.repository.remote;

import com.periodicals.paymentservice.model.dto.PublicationDTO;

import java.util.Optional;

/**
 * @author Pavlo Mrochko
 */
public interface PublicationRepository {

    Optional<PublicationDTO> getPublicationById(Long id);

    Boolean updatePublication(PublicationDTO publicationDTO);

}
