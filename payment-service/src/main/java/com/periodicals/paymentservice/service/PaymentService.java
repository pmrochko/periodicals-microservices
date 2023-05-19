package com.periodicals.paymentservice.service;

import com.periodicals.paymentservice.model.dto.PaymentDTO;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface PaymentService {

    PaymentDTO createPayment(Long userId,
                             Long publicationId,
                             Integer subscriptionPeriod);

    List<PaymentDTO> getAllPayments(Long userId);

}
