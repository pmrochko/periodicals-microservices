package com.periodicals.paymentservice.controller;

import com.periodicals.paymentservice.model.dto.PaymentDTO;
import com.periodicals.paymentservice.model.exception.EntityNotFoundException;
import com.periodicals.paymentservice.service.PaymentService;
import com.periodicals.paymentservice.util.PaymentTestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.periodicals.paymentservice.util.PaymentTestDataUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    public static final String PAYMENT_API_URL = "/api/v1/payments";

    @Test
    void createPayment_whenValidInput_thenReturnsPaymentDtoAnd201Status() throws Exception {
        PaymentDTO paymentDTO = PaymentTestDataUtil.createPaymentDTO();

        when(paymentService.createPayment(anyLong(), anyString(), anyInt()))
                .thenReturn(paymentDTO);

        mockMvc.perform(post(PAYMENT_API_URL)
                        .param("userId", String.valueOf(USER_ID))
                        .param("publicationId", PUBLICATION_ID)
                        .param("subscriptionPeriod", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.totalPrice").value(TOTAL_PRICE))
                .andExpect(jsonPath("$.publicationId").value(PUBLICATION_ID))
                .andExpect(jsonPath("$.userId").value(USER_ID))
                .andExpect(jsonPath("$.dateOfPayment").isNotEmpty())
                .andExpect(jsonPath("$.paymentStatus").value(PAYMENT_STATUS.name()));
    }

    @Test
    void createPayment_whenEntityNotFound_thenReturnsErrorAnd503Status() throws Exception {
        doThrow(new EntityNotFoundException())
                .when(paymentService)
                .createPayment(anyLong(), anyString(), anyInt());

        mockMvc.perform(post(PAYMENT_API_URL)
                        .param("userId", String.valueOf(USER_ID))
                        .param("publicationId", PUBLICATION_ID)
                        .param("subscriptionPeriod", String.valueOf(10)))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

    @Test
    void getAllPayments_whenValidInput_thenReturnsListOfPaymentsAnd200Status() throws Exception {
        final int sizeOfList = 2;
        List<PaymentDTO> paymentDTOList =
                PaymentTestDataUtil.createListOfPaymentDTOs(sizeOfList);

        when(paymentService.getAllPayments(anyLong())).thenReturn(paymentDTOList);

        mockMvc.perform(get(PAYMENT_API_URL)
                        .param("userId", String.valueOf(USER_ID)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(sizeOfList)))
                .andExpectAll(checkAllFieldsOfPaymentArray(sizeOfList));
    }

    @Test
    void getAllPayments_whenUserNotFound_thenReturnsErrorAnd503Status() throws Exception {
        doThrow(new EntityNotFoundException())
                .when(paymentService)
                .getAllPayments(anyLong());

        mockMvc.perform(get(PAYMENT_API_URL)
                        .param("userId", String.valueOf(USER_ID)))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

    private ResultMatcher[] checkAllFieldsOfPaymentArray(int sizeOfArray) {
        List<ResultMatcher> list = new ArrayList<>();
        for (int i = 0; i < sizeOfArray; i++) {
            list.addAll(
                    List.of(
                            jsonPath("$[" + i + "].id").value(ID + i),
                            jsonPath("$[" + i + "].totalPrice").value(TOTAL_PRICE.add(BigDecimal.valueOf(i))),
                            jsonPath("$[" + i + "].publicationId").value(PUBLICATION_ID + i),
                            jsonPath("$[" + i + "].userId").value(USER_ID),
                            jsonPath("$[" + i + "].dateOfPayment").isNotEmpty(),
                            jsonPath("$[" + i + "].paymentStatus").value(PAYMENT_STATUS.name())
                    )
            );
        }
        return list.toArray(new ResultMatcher[0]);
    }

}