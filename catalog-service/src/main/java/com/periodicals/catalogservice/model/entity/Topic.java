package com.periodicals.catalogservice.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("topic")
public class Topic {

    @Id
    private Long id;

    private String name;

    private Set<Publication> publications;

}
