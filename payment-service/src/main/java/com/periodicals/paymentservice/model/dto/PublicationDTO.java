package com.periodicals.paymentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Pavlo Mrochko
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicationDTO {

    private Long id;
    private String title;
    private String description;
    private Long topicId;
    private Integer quantity;
    private BigDecimal price;

}
