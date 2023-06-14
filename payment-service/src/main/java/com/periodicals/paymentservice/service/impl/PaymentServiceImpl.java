package com.periodicals.paymentservice.service.impl;

import com.periodicals.paymentservice.model.dto.PaymentDTO;
import com.periodicals.paymentservice.model.dto.PublicationDTO;
import com.periodicals.paymentservice.model.dto.UserDTO;
import com.periodicals.paymentservice.model.entity.Payment;
import com.periodicals.paymentservice.model.enums.PaymentStatus;
import com.periodicals.paymentservice.model.exception.EntityNotFoundException;
import com.periodicals.paymentservice.model.exception.ServiceException;
import com.periodicals.paymentservice.model.mapper.PaymentMapper;
import com.periodicals.paymentservice.repository.PaymentRepository;
import com.periodicals.paymentservice.repository.remote.PublicationRepository;
import com.periodicals.paymentservice.repository.remote.UserRepository;
import com.periodicals.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PaymentDTO createPayment(Long userId, String publicationId, Integer subscriptionPeriod) {

        userRepository.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        PublicationDTO publication = publicationRepository.getPublicationById(publicationId)
                .orElseThrow(() -> new EntityNotFoundException("Publication was not found"));

        if (publication.getQuantity() < 1) {
            log.error("Publication(id:{}) is not available", publication.getId());
            throw new ServiceException("Selected publication is not available");
        }

        // (total price) = (count of month) * (price)
        BigDecimal totalPrice = BigDecimal.valueOf(subscriptionPeriod)
                                          .multiply(publication.getPrice());

        Payment payment = new Payment();
            payment.setUserId(userId);
            payment.setPublicationId(publicationId);
            payment.setPaymentStatus(PaymentStatus.PAID);
            payment.setTotalPrice(totalPrice);
            payment.setDateOfPayment(Timestamp.valueOf(LocalDateTime.now()));

        // quantity of publication -1
        decreaseQuantityByOne(publication, publicationId);
        paymentRepository.save(payment);

        log.info("New payment was created successfully");
        return PaymentMapper.INSTANCE.mapToPaymentDto(payment);
    }

    @Override
    public List<PaymentDTO> getAllPayments(Long userId) {
         userRepository.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        List<Payment> paymentList = paymentRepository.findAllByUserId(userId);
        log.info("Successful getting a list of payments from the repository");
        return PaymentMapper.INSTANCE.mapToListOfPaymentsDto(paymentList);
    }

    private void decreaseQuantityByOne(PublicationDTO publication, String publicationId) {

        publication.setQuantity(publication.getQuantity() - 1);
        publicationRepository.updatePublication(publication, publicationId);

    }

}
