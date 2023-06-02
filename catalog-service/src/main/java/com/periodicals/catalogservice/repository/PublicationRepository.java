package com.periodicals.catalogservice.repository;

import com.periodicals.catalogservice.model.entity.Publication;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PublicationRepository extends MongoRepository<Publication, Long> {

    List<Publication> findAllByTopic_Id(Long topicId);

    /*@Transactional
    @Modifying
    @Query("update Publication p set p.title = ?1, " +
                                    "p.description = ?2, " +
                                    "p.topic.id = ?3, " +
                                    "p.quantity = ?4, " +
                                    "p.price = ?5 " +
            "where p.id = ?6")
    void updatePublication(
            String title,
            String description,
            Long topicId,
            Integer quantity,
            BigDecimal price,
            Long id
    );*/


}
