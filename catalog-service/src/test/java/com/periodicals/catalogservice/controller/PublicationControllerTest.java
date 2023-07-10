package com.periodicals.catalogservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.periodicals.catalogservice.model.dto.PublicationDTO;
import com.periodicals.catalogservice.model.exception.EntityNotFoundException;
import com.periodicals.catalogservice.model.exception.ServiceException;
import com.periodicals.catalogservice.service.PublicationService;
import com.periodicals.catalogservice.util.PublicationTestDataUtil;
import com.periodicals.catalogservice.util.TopicTestDataUtil;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(PublicationController.class)
class PublicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublicationService publicationService;

    public static final String PUBLICATION_API_URL = TopicControllerTest.TOPIC_API_URL +
            "/{topicName}/publications";

    public static final String PUBLICATION_API_WITHOUT_TOPIC_NAME_URL =
            "/api/v1/publications/{publicationId}";

    @Test
    void createPublication_whenValidInput_thenReturnsPublicationDtoAnd201Status() throws Exception {
        final String topicName = TopicTestDataUtil.NAME;
        PublicationDTO publicationDTO = PublicationTestDataUtil.createPublicationDTO();
        String jsonRequestBody = objectMapper.writeValueAsString(publicationDTO);

        when(publicationService.createPublication(anyString(), any(PublicationDTO.class))).thenReturn(publicationDTO);

        mockMvc.perform(post(PUBLICATION_API_URL, topicName)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpectAll(checkAllFieldsOfPublication());
    }

    @Test
    void createPublication_whenServiceThrowsException_thenReturnsErrorAnd400Status() throws Exception {
        final String topicName = TopicTestDataUtil.NAME;
        PublicationDTO publicationRequest = PublicationTestDataUtil.createPublicationDTO();
        String jsonRequestBody = objectMapper.writeValueAsString(publicationRequest);

        when(publicationService.createPublication(anyString(), any(PublicationDTO.class)))
                .thenThrow(new ServiceException("Service error"));

        mockMvc.perform(post(PUBLICATION_API_URL, topicName)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

    @Test
    void createPublication_whenBlankInput_thenReturnsErrorAnd400Status() throws Exception {
        mockMvc.perform(post(PUBLICATION_API_URL, TopicTestDataUtil.NAME)
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].message").exists())
                .andExpect(jsonPath("$[*].errorType").exists())
                .andExpect(jsonPath("$[*].timeStamp").exists());
    }

    @Test
    void getAllPublications_ReturnsListOfPublications() throws Exception {
        final String topicName = TopicTestDataUtil.NAME;
        final int sizeOfList = 2;
        List<PublicationDTO> listOfPublications =
                PublicationTestDataUtil.createListOfPublicationDTOs(sizeOfList);

        when(publicationService.getAllPublications(anyString())).thenReturn(listOfPublications);

        mockMvc.perform(get(PUBLICATION_API_URL, topicName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(sizeOfList)))
                .andExpectAll(checkAllFieldsOfPublicationArray(sizeOfList));
    }

    @Test
    void getAllPublications_whenTopicNotFound_thenReturnsErrorAnd400Status() throws Exception {
        final String topicName = TopicTestDataUtil.NAME;
        when(publicationService.getAllPublications(anyString())).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get(PUBLICATION_API_URL, topicName))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

    @Test
    void getPublicationById_ReturnsPublicationDtoAnd200Status() throws Exception {
        final String publicationID = PublicationTestDataUtil.ID;
        PublicationDTO publicationDTO = PublicationTestDataUtil.createPublicationDTO();
        when(publicationService.getPublicationById(publicationID)).thenReturn(publicationDTO);

        mockMvc.perform(get(PUBLICATION_API_WITHOUT_TOPIC_NAME_URL, publicationID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(checkAllFieldsOfPublication());
    }

    @Test
    void getPublicationById_whenPublicationNotFound_thenReturnsErrorAnd400Status() throws Exception {
        when(publicationService.getPublicationById(anyString())).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get(PUBLICATION_API_WITHOUT_TOPIC_NAME_URL, PublicationTestDataUtil.ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

    @Test
    void updatePublication_whenValidInput_thenReturnsTopicIDAnd200Status() throws Exception {
        final String publicationID = PublicationTestDataUtil.ID;
        PublicationDTO publicationDTO = PublicationTestDataUtil.createPublicationDTO();
        String jsonRequestBody = objectMapper.writeValueAsString(publicationDTO);

        mockMvc.perform(put(PUBLICATION_API_WITHOUT_TOPIC_NAME_URL, publicationID)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(publicationID));
    }

    @Test
    void updatePublication_whenPublicationNotFound_thenReturnsErrorAnd400Status() throws Exception {
        final String publicationID = PublicationTestDataUtil.ID;
        PublicationDTO publicationDTO = PublicationTestDataUtil.createPublicationDTO();
        String jsonRequestBody = objectMapper.writeValueAsString(publicationDTO);

        doThrow(new EntityNotFoundException())
                .when(publicationService)
                .updatePublication(anyString(), any(PublicationDTO.class));

        mockMvc.perform(put(PUBLICATION_API_WITHOUT_TOPIC_NAME_URL, publicationID)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

    @Test
    void updatePublication_whenBlankInput_thenReturnsErrorAnd400Status() throws Exception {
        final String publicationID = PublicationTestDataUtil.ID;

        mockMvc.perform(put(PUBLICATION_API_WITHOUT_TOPIC_NAME_URL, publicationID)
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].message").hasJsonPath())
                .andExpect(jsonPath("$[*].errorType").exists())
                .andExpect(jsonPath("$[*].timeStamp").exists());
    }

    @Test
    void deletePublication_whenValidPublicationID_thenReturnsTopicIDAnd200Status() throws Exception {
        final String publicationID = PublicationTestDataUtil.ID;

        mockMvc.perform(delete(PUBLICATION_API_WITHOUT_TOPIC_NAME_URL, publicationID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(publicationID));
    }

    @Test
    void deletePublication_whenPublicationNotFound_thenReturnsErrorAnd400Status() throws Exception {
        final String publicationID = PublicationTestDataUtil.ID;

        doThrow(new EntityNotFoundException())
                .when(publicationService)
                .deletePublication(anyString());

        mockMvc.perform(delete(PUBLICATION_API_WITHOUT_TOPIC_NAME_URL, publicationID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

    private ResultMatcher[] checkAllFieldsOfPublication() {
        return List.of(jsonPath("$.id").value(PublicationTestDataUtil.ID),
                        jsonPath("$.title").value(PublicationTestDataUtil.TITLE),
                        jsonPath("$.description").value(PublicationTestDataUtil.DESCRIPTION),
                        jsonPath("$.topicName").value(PublicationTestDataUtil.TOPIC_NAME),
                        jsonPath("$.quantity").value(PublicationTestDataUtil.QUANTITY),
                        jsonPath("$.price").value(PublicationTestDataUtil.PRICE))
                .toArray(new ResultMatcher[0]);
    }

    private ResultMatcher[] checkAllFieldsOfPublicationArray(int sizeOfArray) {
        List<ResultMatcher> list = new ArrayList<>();
        for (int i = 0; i < sizeOfArray; i++) {
            list.addAll(
                    List.of(
                            jsonPath("$[" + i + "].id")
                                    .value(PublicationTestDataUtil.ID + i),
                            jsonPath("$[" + i + "].title")
                                    .value(PublicationTestDataUtil.TITLE + i),
                            jsonPath("$[" + i + "].description")
                                    .value(PublicationTestDataUtil.DESCRIPTION + i),
                            jsonPath("$[" + i + "].topicName")
                                    .value(PublicationTestDataUtil.TOPIC_NAME + i),
                            jsonPath("$[" + i + "].quantity")
                                    .value(PublicationTestDataUtil.QUANTITY + i),
                            jsonPath("$[" + i + "].price")
                                    .value(PublicationTestDataUtil.PRICE.add(BigDecimal.valueOf(i)))
                    )
            );
        }
        return list.toArray(new ResultMatcher[0]);
    }

}
