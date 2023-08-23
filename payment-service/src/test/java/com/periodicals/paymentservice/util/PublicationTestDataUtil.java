package com.periodicals.paymentservice.util;

import com.periodicals.paymentservice.model.dto.PublicationDTO;

import java.math.BigDecimal;

/**
 * @author Pavlo Mrochko
 */
public class PublicationTestDataUtil {

    public static final String ID = "id123";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "desc";
    public static final String TOPIC_NAME = "topic";
    public static final Integer QUANTITY = 100;
    public static final BigDecimal PRICE = BigDecimal.valueOf(65L);

    public static PublicationDTO createPublicationDTO() {
        return PublicationDTO.builder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .topicName(TOPIC_NAME)
                .quantity(QUANTITY)
                .price(PRICE)
                .build();
    }

}
