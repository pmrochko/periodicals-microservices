package com.periodicals.paymentservice.controller;

import com.periodicals.paymentservice.model.dto.PaymentDTO;
import com.periodicals.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/publications/{publicationId}/subscription")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDTO createPayment(@RequestParam @Positive Long userId,
                                    @PathVariable @Positive Long publicationId,
                                    @RequestParam @Positive Integer subscriptionPeriod) {
        log.info("Creating a payment for publication with id: {}", publicationId);
        return paymentService.createPayment(userId, publicationId, subscriptionPeriod);
    }

    @GetMapping("/users/{userId}/payments")
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentDTO> getAllPayments(@PathVariable @Positive Long userId) {
        log.info("Getting a list of payments for user with id: {}", userId);
        return paymentService.getAllPayments(userId);
    }

}
