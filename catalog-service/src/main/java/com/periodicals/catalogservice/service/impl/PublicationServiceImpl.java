package com.periodicals.catalogservice.service.impl;

import com.periodicals.catalogservice.model.dto.PublicationDTO;
import com.periodicals.catalogservice.model.entity.Publication;
import com.periodicals.catalogservice.model.entity.Topic;
import com.periodicals.catalogservice.model.exception.EntityNotFoundException;
import com.periodicals.catalogservice.model.mapper.PublicationMapper;
import com.periodicals.catalogservice.repository.PublicationRepository;
import com.periodicals.catalogservice.repository.TopicRepository;
import com.periodicals.catalogservice.service.PublicationService;
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
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;
    private final TopicRepository topicRepository;

    @Override
    public PublicationDTO createPublication(Long topicId, PublicationDTO publicationDTO) {
        Publication publication = PublicationMapper.INSTANCE.mapToPublication(publicationDTO);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(EntityNotFoundException::new);
        publication.setTopic(topic);
        publication = publicationRepository.save(publication);
        log.info("New publication was created successfully");
        return PublicationMapper.INSTANCE.mapToPublicationDto(publication);
    }

    @Override
    public List<PublicationDTO> getAllPublications(Long topicId) {
        if (!topicRepository.existsById(topicId)) {
            log.warn("Topic(id:{}) was not found", topicId);
            throw new EntityNotFoundException("Topic was not found");
        }

        List<Publication> publicationList = publicationRepository.findAllByTopic_Id(topicId);
        log.info("Successful getting a list of publications from the repository");
        return PublicationMapper.INSTANCE.mapToListOfPublicationsDto(publicationList);
    }

    @Override
    public PublicationDTO getPublicationById(Long publicationId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new EntityNotFoundException("Publication was not found"));

        return PublicationMapper.INSTANCE.mapToPublicationDto(publication);
    }

    @Override
    public void updatePublication(Long publicationId, PublicationDTO publicationDTO) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new EntityNotFoundException("Publication(id:" + publicationId + ") was not found"));

        // Update Title
        publication.setTitle(publicationDTO.getTitle());
        // Update Description
        publication.setDescription(publicationDTO.getDescription());
        // Update Quantity
        publication.setQuantity(publicationDTO.getQuantity());
        // Update Price
        publication.setPrice(publicationDTO.getPrice());
        // Update Topic
        if (!publication.getTopic().getId().equals(publicationDTO.getTopicId())) {
            Topic topic = topicRepository.findById(publicationDTO.getTopicId())
                    .orElseThrow(() -> new EntityNotFoundException("Topic(id:" + publicationDTO.getTopicId() + ") was not found"));
            publication.setTopic(topic);
        }

        publicationRepository.save(publication);

        log.info("Successful updating a publication in the repository");
    }

    @Override
    public void deletePublication(Long publicationId) {
        if (!publicationRepository.existsById(publicationId)) {
            log.warn("Publication(id:{}) was not found", publicationId);
            throw new EntityNotFoundException("Publication was not found");
        }

        publicationRepository.deleteById(publicationId);
        log.info("Successful deleting a publication from the repository");
    }

}
