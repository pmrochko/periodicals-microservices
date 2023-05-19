package com.periodicals.catalogservice.repository;

import com.periodicals.catalogservice.model.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

    List<Publication> findAllByTopic_Id(Long topicId);

    @Transactional
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
    );


}
