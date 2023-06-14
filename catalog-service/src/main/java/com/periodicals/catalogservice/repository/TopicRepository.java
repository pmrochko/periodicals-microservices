package com.periodicals.catalogservice.repository;

import com.periodicals.catalogservice.model.entity.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TopicRepository extends MongoRepository<Topic, String> {

    boolean existsByName(String topicName);

    Optional<Topic> findByName(String topicName);

}
