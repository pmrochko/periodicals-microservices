package com.periodicals.catalogservice.service;

import com.periodicals.catalogservice.model.dto.TopicDTO;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface TopicService {

    TopicDTO createTopic(TopicDTO topicDTO);

    List<TopicDTO> getAllTopics();

    void updateTopicName(Long topicId, TopicDTO topicDTO);

    void deleteTopic(Long topicId);

}
