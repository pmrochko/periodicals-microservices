package com.periodicals.catalogservice.repository;

import com.periodicals.catalogservice.model.entity.Publication;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PublicationRepository extends MongoRepository<Publication, String> {

    List<Publication> findAllByTopicName(String topicName);

    Boolean existsByTitle(String title);

}
