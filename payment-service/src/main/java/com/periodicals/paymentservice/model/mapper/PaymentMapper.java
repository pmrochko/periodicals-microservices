package com.periodicals.paymentservice.model.mapper;

import com.periodicals.paymentservice.model.dto.PaymentDTO;
import com.periodicals.paymentservice.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Mapper
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentDTO mapToPaymentDto(Payment payment);

    List<PaymentDTO> mapToListOfPaymentsDto(List<Payment> paymentList);

}
