package com.periodicals.paymentservice.model.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Pavlo Mrochko
 */
@Data
@Builder
public class SubscriptionDTO {

    @NotNull
    private Long publicationId;

    @NotNull
    private Integer subscriptionPeriod;

}
