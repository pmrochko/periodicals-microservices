package com.periodicals.paymentservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.periodicals.paymentservice.model.enums.PaymentStatus;
import com.periodicals.paymentservice.model.validation.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * A DTO for the {@link com.periodicals.paymentservice.model.entity.Payment} entity
 * @author Pavlo Mrochko
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {

    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    @Positive
    private BigDecimal totalPrice;

    @Positive
    private Long publicationId;

    @Positive
    private Long userId;

    @NotNull
    private Timestamp dateOfPayment;

    @ValueOfEnum(enumClass = PaymentStatus.class)
    private PaymentStatus paymentStatus;

}
