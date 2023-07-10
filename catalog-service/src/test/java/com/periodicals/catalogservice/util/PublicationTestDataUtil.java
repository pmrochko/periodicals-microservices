package com.periodicals.catalogservice.util;

import com.periodicals.catalogservice.model.dto.PublicationDTO;
import com.periodicals.catalogservice.model.entity.Publication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    public static Publication createPublicationEntity() {
        return new Publication(
                ID,
                TITLE,
                DESCRIPTION,
                TOPIC_NAME,
                QUANTITY,
                PRICE
        );
    }

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

    public static List<PublicationDTO> createListOfPublicationDTOs(int size) {
        List<PublicationDTO> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(PublicationDTO.builder()
                    .id(ID + i)
                    .title(TITLE + i)
                    .description(DESCRIPTION + i)
                    .topicName(TOPIC_NAME + i)
                    .quantity(QUANTITY + i)
                    .price(PRICE.add(BigDecimal.valueOf(i)))
                    .build());
        }
        return list;
    }

}
