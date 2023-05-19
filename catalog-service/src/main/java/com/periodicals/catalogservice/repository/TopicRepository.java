package com.periodicals.catalogservice.repository;

import com.periodicals.catalogservice.model.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    boolean existsByName(String topicName);

    @Transactional
    @Modifying
    @Query("update Topic t set t.name = ?1 where t.id = ?2")
    void updateTopicName(String name, Long id);

}
