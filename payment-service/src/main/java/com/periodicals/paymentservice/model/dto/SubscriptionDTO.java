package com.periodicals.paymentservice.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Pavlo Mrochko
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionDTO {

    @NotNull
    private Long publicationId;

    @NotNull
    private Integer subscriptionPeriod;

}
