package com.periodicals.catalogservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.periodicals.catalogservice.model.entity.Publication} entity
 * @author Pavlo Mrochko
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicationDTO {

    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @Positive
    private Long topicId;

    @PositiveOrZero
    private Integer quantity;

    @Positive
    private BigDecimal price;

}
