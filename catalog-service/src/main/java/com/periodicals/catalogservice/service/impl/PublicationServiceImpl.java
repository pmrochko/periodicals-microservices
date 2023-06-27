package com.periodicals.catalogservice.service.impl;

import com.periodicals.catalogservice.model.dto.PublicationDTO;
import com.periodicals.catalogservice.model.entity.Publication;
import com.periodicals.catalogservice.model.entity.Topic;
import com.periodicals.catalogservice.model.exception.EntityNotFoundException;
import com.periodicals.catalogservice.model.exception.NotUniqueParameterException;
import com.periodicals.catalogservice.model.mapper.PublicationMapper;
import com.periodicals.catalogservice.repository.PublicationRepository;
import com.periodicals.catalogservice.repository.TopicRepository;
import com.periodicals.catalogservice.service.PublicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;
    private final TopicRepository topicRepository;

    @Override
    public PublicationDTO createPublication(String topicName, PublicationDTO publicationDTO) {
        if (publicationRepository.existsByTitle(publicationDTO.getTitle())) {
            log.warn("Publication title(value:{}) has already exist", publicationDTO.getTitle());
            throw new NotUniqueParameterException("Publication title has already exist");
        }

        Publication publication = PublicationMapper.INSTANCE.mapToPublication(publicationDTO);

        Topic topic = topicRepository.findByName(topicName)
                .orElseThrow(() -> new EntityNotFoundException("Topic was not found"));
        publication.setTopicName(topic.getName());

        publication = publicationRepository.save(publication);
        log.info("New publication was created successfully");
        return PublicationMapper.INSTANCE.mapToPublicationDto(publication);
    }

    @Override
    public List<PublicationDTO> getAllPublications(String topicName) {
        if (!topicRepository.existsByName(topicName)) {
            log.warn("Topic(name:{}) was not found", topicName);
            throw new EntityNotFoundException("Topic was not found");
        }

        List<Publication> publicationList = publicationRepository.findAllByTopicName(topicName);
        log.info("Successful getting a list of publications from the repository");
        return PublicationMapper.INSTANCE.mapToListOfPublicationsDto(publicationList);
    }

    @Override
    public PublicationDTO getPublicationById(String publicationId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new EntityNotFoundException("Publication was not found"));

        log.info("Successful getting a publication from the repository");
        return PublicationMapper.INSTANCE.mapToPublicationDto(publication);
    }

    @Override
    @Transactional
    public void updatePublication(String publicationId, PublicationDTO publicationDTO) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new EntityNotFoundException("Publication(id:" + publicationId + ") was not found"));

        // Update Title
        if (titleHasAlreadyExist(publication, publicationDTO)) {
            log.warn("Publication title(value:{}) has already exist", publicationDTO.getTitle());
            throw new NotUniqueParameterException("Publication title has already exist");
        } else {
            publication.setTitle(publicationDTO.getTitle());
        }
        // Update Description
        publication.setDescription(publicationDTO.getDescription());
        // Update Quantity
        publication.setQuantity(publicationDTO.getQuantity());
        // Update Price
        publication.setPrice(publicationDTO.getPrice());
        // Update Topic name
        if (!publication.getTopicName().equals(publicationDTO.getTopicName())) {
            Topic topic = topicRepository.findByName(publicationDTO.getTopicName())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Topic(name:" + publicationDTO.getTopicName() + ") was not found"));
            publication.setTopicName(topic.getName());
        }

        publicationRepository.save(publication);

        log.info("Successful updating a publication in the repository");
    }

    @Override
    public void deletePublication(String publicationId) {
        if (!publicationRepository.existsById(publicationId)) {
            log.warn("Publication(id:{}) was not found", publicationId);
            throw new EntityNotFoundException("Publication was not found");
        }

        publicationRepository.deleteById(publicationId);
        log.info("Successful deleting a publication from the repository");
    }

    private boolean titleHasAlreadyExist(Publication publication, PublicationDTO publicationDTO) {
        return !publication.getTitle().equals(publicationDTO.getTitle()) &&
                publicationRepository.existsByTitle(publicationDTO.getTitle());
    }

}
