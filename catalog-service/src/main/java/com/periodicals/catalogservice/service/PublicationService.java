package com.periodicals.catalogservice.service;

import com.periodicals.catalogservice.model.dto.PublicationDTO;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface PublicationService {

    PublicationDTO createPublication(Long topicId, PublicationDTO publicationDTO);

    List<PublicationDTO> getAllPublications(Long topicId);

    PublicationDTO getPublicationById(Long publicationId);

    void updatePublication(Long publicationId, PublicationDTO publicationDTO);

    void deletePublication(Long publicationId);

}
