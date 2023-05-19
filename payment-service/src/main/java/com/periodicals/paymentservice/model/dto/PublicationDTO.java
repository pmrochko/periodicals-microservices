package com.periodicals.paymentservice.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Pavlo Mrochko
 */
@Data
@Builder
public class PublicationDTO {

    private Long id;
    private String title;
    private String description;
    private Long topicId;
    private Integer quantity;
    private BigDecimal price;

}
