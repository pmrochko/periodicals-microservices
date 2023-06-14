package com.periodicals.paymentservice.model.entity;

import com.periodicals.paymentservice.model.enums.PaymentStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "publication_Id", nullable = false)
    private String publicationId;

    @Column(name = "user_Id", nullable = false)
    private Long userId;

    @Column(name = "date_of_payment", nullable = false)
    private Timestamp dateOfPayment;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

}
