package com.periodicals.catalogservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.periodicals.catalogservice.model.dto.TopicDTO;
import com.periodicals.catalogservice.model.exception.EntityNotFoundException;
import com.periodicals.catalogservice.model.exception.NotUniqueParameterException;
import com.periodicals.catalogservice.service.TopicService;
import com.periodicals.catalogservice.util.TopicTestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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
@WebMvcTest(TopicController.class)
class TopicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TopicService topicService;

    public static final String TOPIC_API_URL = "/api/v1/topics";

    @Test
    void createTopic_whenValidInput_thenReturnsTopicDtoAnd201Status() throws Exception {
        TopicDTO topicDTO = TopicTestDataUtil.createTopicDTO();

        String jsonRequestBody = objectMapper.writeValueAsString(topicDTO);

        when(topicService.createTopic(any(TopicDTO.class))).thenReturn(topicDTO);

        mockMvc.perform(post(TOPIC_API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TopicTestDataUtil.ID))
                .andExpect(jsonPath("$.name").value(TopicTestDataUtil.NAME));
    }

    @Test
    void createTopic_whenDuplicateTopicName_thenReturnsErrorAnd400Status() throws Exception {
        TopicDTO topicRequest = TopicTestDataUtil.createTopicDTO();

        String jsonRequestBody = objectMapper.writeValueAsString(topicRequest);

        when(topicService.createTopic(any(TopicDTO.class)))
                .thenThrow(new NotUniqueParameterException());

        mockMvc.perform(post(TOPIC_API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

    @Test
    void createTopic_whenBlankInput_thenReturnsErrorAnd400Status() throws Exception {
        mockMvc.perform(post(TOPIC_API_URL)
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].message").exists())
                .andExpect(jsonPath("$[*].errorType").exists())
                .andExpect(jsonPath("$[*].timeStamp").exists());
    }

    @Test
    void getAllTopics_ReturnsListOfTopics() throws Exception {
        final String ID_1 = TopicTestDataUtil.ID + "1";
        final String ID_2 = TopicTestDataUtil.ID + "2";
        final String NAME_1 = TopicTestDataUtil.NAME + "1";
        final String NAME_2 = TopicTestDataUtil.NAME + "2";

        TopicDTO topic1 = TopicDTO.builder()
                .id(ID_1)
                .name(NAME_1).build();

        TopicDTO topic2 = TopicDTO.builder()
                .id(ID_2)
                .name(NAME_2).build();

        List<TopicDTO> listOfTopics = List.of(topic1, topic2);

        when(topicService.getAllTopics()).thenReturn(listOfTopics);

        mockMvc.perform(get(TOPIC_API_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(ID_1))
                .andExpect(jsonPath("$[0].name").value(NAME_1))
                .andExpect(jsonPath("$[1].id").value(ID_2))
                .andExpect(jsonPath("$[1].name").value(NAME_2));
    }

    @Test
    void updateTopic_whenValidInput_thenReturnsTopicIDAnd200Status() throws Exception {
        final String topicID = TopicTestDataUtil.ID;
        TopicDTO topicDTO = TopicTestDataUtil.createTopicDTO();
        String jsonRequestBody = objectMapper.writeValueAsString(topicDTO);

        mockMvc.perform(put(TOPIC_API_URL + "/{topicId}", topicID)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(topicID));
    }

    @Test
    void updateTopic_whenTopicNotFound_thenReturnsErrorAnd400Status() throws Exception {
        final String topicID = TopicTestDataUtil.ID;
        TopicDTO topicDTO = TopicTestDataUtil.createTopicDTO();
        String jsonRequestBody = objectMapper.writeValueAsString(topicDTO);

        doThrow(new EntityNotFoundException())
                .when(topicService)
                .updateTopicName(anyString(), any(TopicDTO.class));

        mockMvc.perform(put(TOPIC_API_URL + "/{topicId}", topicID)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

    @Test
    void updateTopic_whenBlankInput_thenReturnsErrorAnd400Status() throws Exception {
        final String topicID = TopicTestDataUtil.ID;

        mockMvc.perform(put(TOPIC_API_URL + "/{topicId}", topicID)
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].message").hasJsonPath())
                .andExpect(jsonPath("$[*].errorType").exists())
                .andExpect(jsonPath("$[*].timeStamp").exists());
    }

    @Test
    void deleteTopic_whenValidTopicID_thenReturnsTopicIDAnd200Status() throws Exception {
        final String topicID = TopicTestDataUtil.ID;

        mockMvc.perform(delete(TOPIC_API_URL + "/{topicId}", topicID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(topicID));
    }

    @Test
    void deleteTopic_whenTopicNotFound_thenReturnsErrorAnd400Status() throws Exception {
        final String topicID = TopicTestDataUtil.ID;

        doThrow(new EntityNotFoundException())
                .when(topicService)
                .deleteTopic(anyString());

        mockMvc.perform(delete(TOPIC_API_URL + "/{topicId}", topicID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

}
