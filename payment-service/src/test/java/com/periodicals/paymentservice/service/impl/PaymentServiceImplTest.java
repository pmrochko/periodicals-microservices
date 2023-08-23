package com.periodicals.paymentservice.service.impl;

import com.periodicals.paymentservice.model.dto.PaymentDTO;
import com.periodicals.paymentservice.model.dto.PublicationDTO;
import com.periodicals.paymentservice.model.dto.UserDTO;
import com.periodicals.paymentservice.model.entity.Payment;
import com.periodicals.paymentservice.model.enums.PaymentStatus;
import com.periodicals.paymentservice.model.exception.EntityNotFoundException;
import com.periodicals.paymentservice.model.exception.ServiceException;
import com.periodicals.paymentservice.repository.PaymentRepository;
import com.periodicals.paymentservice.repository.remote.PublicationRepository;
import com.periodicals.paymentservice.repository.remote.UserRepository;
import com.periodicals.paymentservice.util.PaymentTestDataUtil;
import com.periodicals.paymentservice.util.PublicationTestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PublicationRepository publicationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void createPayment_shouldCreateSuccessful() {
        final long userID = 123L;
        final int subscriptionPeriod = 4;

        UserDTO userDTO = UserDTO.builder().id(userID).build();
        PublicationDTO publication = PublicationTestDataUtil.createPublicationDTO();
        when(userRepository.getUserById(anyLong())).thenReturn(Optional.of(userDTO));
        when(publicationRepository.getPublicationById(anyString())).thenReturn(Optional.of(publication));

        PaymentDTO actualPayment = paymentService
                .createPayment(userID, publication.getId(), subscriptionPeriod);

        PaymentDTO expectedPayment = PaymentDTO.builder()
                .userId(userID)
                .publicationId(publication.getId())
                .paymentStatus(PaymentStatus.PAID)
                .totalPrice(publication.getPrice().multiply(BigDecimal.valueOf(subscriptionPeriod)))
                .dateOfPayment(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        assertThat(publication.getQuantity()).isEqualTo(PublicationTestDataUtil.QUANTITY - 1);
        assertThatPaymentsAreEquals(actualPayment, expectedPayment);
    }

    @Test
    void createPayment_whenUserNotFound_shouldThrowException() {
        final long userID = 123L;
        final String publicationID = "abc123";
        final int subscriptionPeriod = 4;

        when(userRepository.getUserById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> paymentService.createPayment(userID, publicationID, subscriptionPeriod));

        verify(publicationRepository, never()).updatePublication(any(PublicationDTO.class), anyString());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void createPayment_whenPublicationNotFound_shouldThrowException() {
        final long userID = 123L;
        final String publicationID = "abc123";
        final int subscriptionPeriod = 4;
        UserDTO userDTO = UserDTO.builder().id(userID).build();

        when(userRepository.getUserById(anyLong())).thenReturn(Optional.of(userDTO));
        when(publicationRepository.getPublicationById(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> paymentService.createPayment(userID, publicationID, subscriptionPeriod));

        verify(publicationRepository, never()).updatePublication(any(PublicationDTO.class), anyString());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void createPayment_whenPublicationNotAvailable_shouldThrowException() {
        final long userID = 123L;
        final int subscriptionPeriod = 4;
        UserDTO userDTO = UserDTO.builder().id(userID).build();
        PublicationDTO publication = PublicationTestDataUtil.createPublicationDTO();
        publication.setQuantity(0);

        when(userRepository.getUserById(anyLong())).thenReturn(Optional.of(userDTO));
        when(publicationRepository.getPublicationById(anyString())).thenReturn(Optional.of(publication));

        assertThrows(ServiceException.class,
                () -> paymentService.createPayment(userID, publication.getId(), subscriptionPeriod));

        verify(publicationRepository, never()).updatePublication(any(PublicationDTO.class), anyString());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void getAllPayments_shouldGetListSuccessful() {
        final long userID = PaymentTestDataUtil.USER_ID;
        UserDTO userDTO = UserDTO.builder().id(userID).build();
        List<Payment> paymentList = PaymentTestDataUtil.createListOfPayments(3);

        when(userRepository.getUserById(anyLong())).thenReturn(Optional.of(userDTO));
        when(paymentRepository.findAllByUserId(anyLong())).thenReturn(paymentList);

        List<PaymentDTO> paymentDTOList = paymentService.getAllPayments(userID);

        assertThat(paymentDTOList.size()).isEqualTo(paymentList.size());
        for (int i = 0; i < paymentList.size(); i++) {
            assertThatPaymentsAreEquals(paymentDTOList.get(i), paymentList.get(i));
        }
    }

    @Test
    void getAllPayments_whenUserNotFound_shouldThrowException() {
        final long userID = PaymentTestDataUtil.USER_ID;
        when(userRepository.getUserById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.getAllPayments(userID));
        verify(paymentRepository, never()).findAllByUserId(anyLong());
    }

    private void assertThatPaymentsAreEquals(PaymentDTO actualPayment, PaymentDTO expectedPayment) {
        assertThat(actualPayment.getUserId()).isEqualTo(expectedPayment.getUserId());
        assertThat(actualPayment.getPublicationId()).isEqualTo(expectedPayment.getPublicationId());
        assertThat(actualPayment.getPaymentStatus()).isEqualTo(expectedPayment.getPaymentStatus());
        assertThat(actualPayment.getTotalPrice()).isEqualTo(expectedPayment.getTotalPrice());
        assertThat(actualPayment.getDateOfPayment()).isEqualToIgnoringSeconds(expectedPayment.getDateOfPayment());
    }

    private void assertThatPaymentsAreEquals(PaymentDTO actualPaymentDTO, Payment expectedPaymentEntity) {
        assertThat(actualPaymentDTO.getUserId()).isEqualTo(expectedPaymentEntity.getUserId());
        assertThat(actualPaymentDTO.getPublicationId()).isEqualTo(expectedPaymentEntity.getPublicationId());
        assertThat(actualPaymentDTO.getPaymentStatus()).isEqualTo(expectedPaymentEntity.getPaymentStatus());
        assertThat(actualPaymentDTO.getTotalPrice()).isEqualTo(expectedPaymentEntity.getTotalPrice());
        assertThat(actualPaymentDTO.getDateOfPayment()).isEqualToIgnoringSeconds(expectedPaymentEntity.getDateOfPayment());
    }

}