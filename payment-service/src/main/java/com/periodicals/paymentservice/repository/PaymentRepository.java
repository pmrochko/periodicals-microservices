package com.periodicals.paymentservice.repository;

import com.periodicals.paymentservice.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByUser_Id(Long userId);

    Optional<Payment> findByUser_IdAndPublication_Id(Long userId, Long publicationId);

    boolean existsByUser_IdAndPublication_Id(Long userId,Long publicationId);

}
