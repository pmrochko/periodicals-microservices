package com.periodicals.catalogservice.service.impl;

import com.periodicals.catalogservice.model.dto.TopicDTO;
import com.periodicals.catalogservice.model.entity.Topic;
import com.periodicals.catalogservice.model.exception.EntityNotFoundException;
import com.periodicals.catalogservice.model.exception.NotUniqueParameterException;
import com.periodicals.catalogservice.model.mapper.TopicMapper;
import com.periodicals.catalogservice.repository.TopicRepository;
import com.periodicals.catalogservice.service.TopicService;
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
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Override
    public TopicDTO createTopic(TopicDTO topicDTO) {
        if (topicRepository.existsByName(topicDTO.getName())) {
            log.warn("Topic name(value:{}) has already exist", topicDTO.getName());
            throw new NotUniqueParameterException("Topic name has already exist");
        }

        Topic topic = TopicMapper.INSTANCE.mapToTopic(topicDTO);
        topic = topicRepository.save(topic);
        log.info("New topic was created successfully");
        return TopicMapper.INSTANCE.mapToTopicDto(topic);
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        List<Topic> topicList = topicRepository.findAll();
        log.info("Successful getting a list of topics from the repository");
        return TopicMapper.INSTANCE.mapToListOfTopicsDto(topicList);
    }

    @Override
    public void updateTopicName(Long topicId, TopicDTO topicDTO) {
        if (!topicRepository.existsById(topicId)) {
            log.warn("Topic(id:{}) was not found", topicId);
            throw new EntityNotFoundException("Topic was not found");
        }

        topicRepository.updateTopicName(topicDTO.getName(), topicId);
        log.info("Successful updating a topic in the repository");
    }

    @Override
    public void deleteTopic(Long topicId) {
        if (!topicRepository.existsById(topicId)) {
            log.warn("Topic(id:{}) was not found", topicId);
            throw new EntityNotFoundException("Topic was not found");
        }

        topicRepository.deleteById(topicId);
        log.info("Successful deleting a topic from the repository");
    }

}
