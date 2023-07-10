package com.periodicals.catalogservice.util;

import com.periodicals.catalogservice.model.dto.TopicDTO;
import com.periodicals.catalogservice.model.entity.Topic;

/**
 * @author Pavlo Mrochko
 */
public class TopicTestDataUtil {

    public static final String ID = "topicID";
    public static final String NAME = "topicName";

    public static Topic createTopicEntity() {
        return new Topic(ID, NAME);
    }

    public static TopicDTO createTopicDTO() {
        return new TopicDTO(ID, NAME);
    }

}
