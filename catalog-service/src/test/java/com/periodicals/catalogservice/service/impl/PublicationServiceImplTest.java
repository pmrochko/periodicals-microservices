package com.periodicals.catalogservice.service.impl;

import com.periodicals.catalogservice.model.dto.PublicationDTO;
import com.periodicals.catalogservice.model.entity.Publication;
import com.periodicals.catalogservice.model.entity.Topic;
import com.periodicals.catalogservice.model.exception.EntityNotFoundException;
import com.periodicals.catalogservice.model.exception.NotUniqueParameterException;
import com.periodicals.catalogservice.repository.PublicationRepository;
import com.periodicals.catalogservice.repository.TopicRepository;
import com.periodicals.catalogservice.util.PublicationTestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(MockitoExtension.class)
public class PublicationServiceImplTest {

    @Mock
    private PublicationRepository publicationRepository;

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private PublicationServiceImpl publicationService;

    @Test
    public void createPublication_UniqueTitle_ShouldReturnPublicationDTO() {
        // Arrange
        String topicName = "Topic";
        Topic topic = new Topic();
        topic.setName(topicName);
        Publication publicationEntity = PublicationTestDataUtil.createPublicationEntity();
        PublicationDTO publicationDTO = PublicationTestDataUtil.createPublicationDTO();

        when(publicationRepository.existsByTitle(anyString())).thenReturn(false);
        when(topicRepository.findByName(anyString())).thenReturn(Optional.of(topic));
        when(publicationRepository.save(any(Publication.class))).thenReturn(publicationEntity);

        // Act
        PublicationDTO result = publicationService.createPublication(topicName, publicationDTO);

        // Assert
        verify(publicationRepository, times(1)).existsByTitle(publicationDTO.getTitle());
        verify(publicationRepository, times(1)).save(any(Publication.class));
        verify(topicRepository, times(1)).findByName(topicName);
        assertThat(result).isNotNull();
        assertThatFieldsAreEqual(result, publicationEntity);
    }

    @Test
    public void createPublication_DuplicateTitle_ShouldThrowException() {
        // Arrange
        String topicName = "Topic";
        PublicationDTO publicationDTO = PublicationTestDataUtil.createPublicationDTO();

        when(publicationRepository.existsByTitle(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(NotUniqueParameterException.class,
                () -> publicationService.createPublication(topicName, publicationDTO));

        verify(publicationRepository, times(1)).existsByTitle(publicationDTO.getTitle());
        verify(publicationRepository, never()).save(any(Publication.class));
        verify(topicRepository, never()).findByName(topicName);
    }

    @Test
    public void getAllPublications_ExistingTopic_ShouldReturnListOfPublicationDTOs() {
        // Arrange
        String topicName = "Topic";

        Publication publication1 = PublicationTestDataUtil.createPublicationEntity();
        publication1.setId("p1");
        publication1.setTitle("t1");

        Publication publication2 = PublicationTestDataUtil.createPublicationEntity();
        publication2.setId("p2");
        publication2.setTitle("t2");

        List<Publication> publicationList = List.of(publication1, publication2);

        when(topicRepository.existsByName(topicName)).thenReturn(true);
        when(publicationRepository.findAllByTopicName(topicName)).thenReturn(publicationList);

        // Act
        List<PublicationDTO> result = publicationService.getAllPublications(topicName);

        // Assert
        assertThat(result.size()).isEqualTo(publicationList.size());
        assertThatFieldsAreEqual(result.get(0), publicationList.get(0));
        assertThatFieldsAreEqual(result.get(1), publicationList.get(1));
    }

    @Test
    public void getAllPublications_NonexistentTopic_ShouldThrowException() {
        // Arrange
        String topicName = "Nonexistent Topic";

        when(topicRepository.existsByName(topicName)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> publicationService.getAllPublications(topicName));
        verify(publicationRepository, never()).findAllByTopicName(topicName);
    }

    @Test
    public void getPublicationById_ExistingPublication_ShouldReturnPublicationDTO() {
        // Arrange
        Publication publicationEntity = PublicationTestDataUtil.createPublicationEntity();
        String publicationId = publicationEntity.getId();
        when(publicationRepository.findById(publicationId)).thenReturn(Optional.of(publicationEntity));

        // Act
        PublicationDTO result = publicationService.getPublicationById(publicationId);

        // Assert
        verify(publicationRepository, times(1)).findById(publicationId);
        assertThat(result).isNotNull();
        assertThatFieldsAreEqual(result, publicationEntity);
    }

    @Test
    public void getPublicationById_NonexistentPublication_ShouldThrowException() {
        // Arrange
        String publicationId = "testID";
        when(publicationRepository.findById(publicationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> publicationService.getPublicationById(publicationId));
        verify(publicationRepository, times(1)).findById(publicationId);
    }

    @Test
    public void updatePublication_ExistingPublication_ShouldUpdatePublication() {
        // Arrange
        Publication publicationEntity = PublicationTestDataUtil.createPublicationEntity();
        String publicationId = publicationEntity.getId();
        PublicationDTO updatedPublication = PublicationDTO.builder()
                .title("Updated title")
                .topicName("Updated topic")
                .description("Updated desc")
                .build();
        Topic topic = new Topic();
        topic.setName("Topic");

        when(publicationRepository.findById(publicationId)).thenReturn(Optional.of(publicationEntity));
        when(publicationRepository.existsByTitle(updatedPublication.getTitle())).thenReturn(false);
        when(topicRepository.findByName(updatedPublication.getTopicName())).thenReturn(Optional.of(topic));

        // Act
        publicationService.updatePublication(publicationId, updatedPublication);

        // Assert
        verify(publicationRepository, times(1)).save(publicationEntity);
    }

    @Test
    public void updatePublication_NonexistentPublication_ShouldThrowException() {
        // Arrange
        PublicationDTO publicationDTO = PublicationTestDataUtil.createPublicationDTO();
        String publicationId = "testID";

        when(publicationRepository.findById(publicationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> publicationService.updatePublication(publicationId, publicationDTO));
        verify(publicationRepository, never()).save(any(Publication.class));
    }

    @Test
    public void deletePublication_ExistingPublication_ShouldDeletePublication() {
        // Arrange
        String publicationId = "testID";
        when(publicationRepository.existsById(publicationId)).thenReturn(true);

        // Act
        publicationService.deletePublication(publicationId);

        // Assert
        verify(publicationRepository, times(1)).deleteById(publicationId);
    }

    @Test
    public void deletePublication_NonexistentPublication_ShouldThrowException() {
        // Arrange
        String publicationId = "testID";
        when(publicationRepository.existsById(publicationId)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> publicationService.deletePublication(publicationId));
        verify(publicationRepository, never()).deleteById(publicationId);
    }

    private void assertThatFieldsAreEqual(PublicationDTO actualDto, Publication expectedEntity) {
        // check id
        assertThat(actualDto.getId()).isEqualTo(expectedEntity.getId());
        // check title
        assertThat(actualDto.getTitle()).isEqualTo(expectedEntity.getTitle());
        // check topic name
        assertThat(actualDto.getTopicName()).isEqualTo(expectedEntity.getTopicName());
        // check description
        assertThat(actualDto.getDescription()).isEqualTo(expectedEntity.getDescription());
        // check quantity
        assertThat(actualDto.getQuantity()).isEqualTo(expectedEntity.getQuantity());
        // check price
        assertThat(actualDto.getPrice()).isEqualTo(expectedEntity.getPrice());
    }

}
