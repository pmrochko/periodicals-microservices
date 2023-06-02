package com.periodicals.catalogservice.repository;

import com.periodicals.catalogservice.model.entity.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<Topic, Long> {

    boolean existsByName(String topicName);

    /*@Transactional
    @Modifying
    @Query("update Topic t set t.name = ?1 where t.id = ?2")
    void updateTopicName(String name, Long id);*/

}
