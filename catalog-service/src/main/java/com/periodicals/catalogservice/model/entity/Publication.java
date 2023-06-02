package com.periodicals.catalogservice.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("publication")
public class Publication {

    @Id
    private Long id;

    private String title;

    private String description;

    private Topic topic;

    private Integer quantity;

    private BigDecimal price;

}
