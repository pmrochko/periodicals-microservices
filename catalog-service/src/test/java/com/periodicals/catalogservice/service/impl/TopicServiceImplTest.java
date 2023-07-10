package com.periodicals.catalogservice.service.impl;

import com.periodicals.catalogservice.model.dto.TopicDTO;
import com.periodicals.catalogservice.model.entity.Topic;
import com.periodicals.catalogservice.model.exception.EntityNotFoundException;
import com.periodicals.catalogservice.model.exception.NotUniqueParameterException;
import com.periodicals.catalogservice.repository.TopicRepository;
import com.periodicals.catalogservice.util.TopicTestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(MockitoExtension.class)
public class TopicServiceImplTest {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicServiceImpl topicService;

    @Test
    public void createTopic_UniqueName_ShouldReturnTopicDTO() {
        // Arrange
        TopicDTO topicDTO = TopicTestDataUtil.createTopicDTO();
        Topic topicEntity = TopicTestDataUtil.createTopicEntity();

        when(topicRepository.existsByName(anyString())).thenReturn(false);
        when(topicRepository.save(any(Topic.class))).thenReturn(topicEntity);

        // Act
        TopicDTO result = topicService.createTopic(topicDTO);

        // Assert
        verify(topicRepository, times(1)).existsByName(topicDTO.getName());
        verify(topicRepository, times(1)).save(any(Topic.class));
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(topicDTO.getName());
    }

    @Test
    public void createTopic_DuplicateName_ShouldThrowException() {
        // Arrange
        TopicDTO topicDTO = TopicTestDataUtil.createTopicDTO();

        when(topicRepository.existsByName(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(NotUniqueParameterException.class, () -> topicService.createTopic(topicDTO));

        verify(topicRepository, times(1)).existsByName(topicDTO.getName());
        verify(topicRepository, never()).save(any(Topic.class));
    }

    @Test
    public void getAllTopics_ShouldReturnListOfTopicDTOs() {
        // Arrange
        List<Topic> topicList = new ArrayList<>();
        topicList.add(new Topic("1", "Topic1"));
        topicList.add(new Topic("2", "Topic2"));

        when(topicRepository.findAll()).thenReturn(topicList);

        // Act
        List<TopicDTO> result = topicService.getAllTopics();

        // Assert
        assertThat(result.size()).isEqualTo(topicList.size());
        assertThat(result.get(0).getName()).isEqualTo(topicList.get(0).getName());
        assertThat(result.get(1).getName()).isEqualTo(topicList.get(1).getName());
    }

    @Test
    public void updateTopicName_ExistingTopic_ShouldUpdateTopic() {
        // Arrange
        String topicId = TopicTestDataUtil.ID;
        String updatedTopicName = "Updated Topic";
        TopicDTO topicDTO = TopicDTO.builder()
                .name(updatedTopicName).build();

        Topic topicEntity = new Topic(topicId, "Original Topic");
        when(topicRepository.findById(anyString())).thenReturn(Optional.of(topicEntity));

        // Act
        topicService.updateTopicName(topicId, topicDTO);

        // Assert
        assertThat(topicEntity.getName()).isEqualTo(updatedTopicName);
        verify(topicRepository, times(1)).save(topicEntity);
    }

    @Test
    public void updateTopicName_NonexistentTopic_ShouldThrowException() {
        // Arrange
        String topicId = TopicTestDataUtil.ID;
        TopicDTO topicDTO = TopicDTO.builder()
                .name("Updated Topic").build();
        when(topicRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> topicService.updateTopicName(topicId, topicDTO));

        verify(topicRepository, never()).save(any(Topic.class));
    }

    @Test
    public void deleteTopic_ExistingTopic_ShouldDeleteTopic() {
        // Arrange
        String topicId = TopicTestDataUtil.ID;
        when(topicRepository.existsById(anyString())).thenReturn(true);

        // Act
        topicService.deleteTopic(topicId);

        // Assert
        verify(topicRepository, times(1)).deleteById(topicId);
    }

    @Test
    public void deleteTopic_NonexistentTopic_ShouldThrowException() {
        // Arrange
        String topicId = TopicTestDataUtil.ID;
        when(topicRepository.existsById(topicId)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> topicService.deleteTopic(topicId));

        verify(topicRepository, never()).deleteById(topicId);
    }

}
