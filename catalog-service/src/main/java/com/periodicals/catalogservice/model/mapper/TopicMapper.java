package com.periodicals.catalogservice.model.mapper;

import com.periodicals.catalogservice.model.dto.TopicDTO;
import com.periodicals.catalogservice.model.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Mapper
public interface TopicMapper {

    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    Topic mapToTopic(TopicDTO topicDTO);

    TopicDTO mapToTopicDto(Topic topic);

    List<TopicDTO> mapToListOfTopicsDto(List<Topic> topicList);

}
