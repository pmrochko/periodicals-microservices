package com.periodicals.paymentservice.util;

import com.periodicals.paymentservice.model.dto.PaymentDTO;
import com.periodicals.paymentservice.model.entity.Payment;
import com.periodicals.paymentservice.model.enums.PaymentStatus;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public class PaymentTestDataUtil {

    public static final Long ID = 222L;
    public static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(100L);
    public static final String PUBLICATION_ID = "p23114";
    public static final Long USER_ID = 333L;
    public static final PaymentStatus PAYMENT_STATUS = PaymentStatus.PAID;

    public static PaymentDTO createPaymentDTO() {
        return PaymentDTO.builder()
                .id(ID)
                .totalPrice(TOTAL_PRICE)
                .publicationId(PUBLICATION_ID)
                .userId(USER_ID)
                .dateOfPayment(Timestamp.valueOf(LocalDateTime.now()))
                .paymentStatus(PAYMENT_STATUS)
                .build();
    }

    public static List<Payment> createListOfPayments(@Positive int size) {

        List<Payment> paymentList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            paymentList.add(new Payment(
                    ID + i,
                    TOTAL_PRICE.add(BigDecimal.valueOf(i)),
                    PUBLICATION_ID + i,
                    USER_ID,
                    Timestamp.valueOf(LocalDateTime.now()),
                    PAYMENT_STATUS
                    ));
        }

        return paymentList;
    }

    public static List<PaymentDTO> createListOfPaymentDTOs(@Positive int size) {

        List<PaymentDTO> paymentList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            paymentList.add(new PaymentDTO(
                    ID + i,
                    TOTAL_PRICE.add(BigDecimal.valueOf(i)),
                    PUBLICATION_ID + i,
                    USER_ID,
                    Timestamp.valueOf(LocalDateTime.now()),
                    PAYMENT_STATUS
            ));
        }

        return paymentList;
    }

}
