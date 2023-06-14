package com.periodicals.catalogservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("publication")
public class Publication {

    @Id
    private String id;

    @Field(name = "title")
    @Indexed(unique = true)
    private String title;

    @Field(name = "description")
    private String description;

    @Field(name = "topic_name")
    private String topicName;

    @Field(name = "quantity")
    private Integer quantity;

    @Field(name = "price")
    private BigDecimal price;

}
