package com.periodicals.catalogservice.service;

import com.periodicals.catalogservice.model.dto.PublicationDTO;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface PublicationService {

    PublicationDTO createPublication(String topicName, PublicationDTO publicationDTO);

    List<PublicationDTO> getAllPublications(String topicName);

    PublicationDTO getPublicationById(String publicationId);

    void updatePublication(String publicationId, PublicationDTO publicationDTO);

    void deletePublication(String publicationId);

}
