package com.periodicals.catalogservice.service;

import com.periodicals.catalogservice.model.dto.TopicDTO;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface TopicService {

    TopicDTO createTopic(TopicDTO topicDTO);

    List<TopicDTO> getAllTopics();

    void updateTopicName(String topicId, TopicDTO topicDTO);

    void deleteTopic(String topicId);

}
