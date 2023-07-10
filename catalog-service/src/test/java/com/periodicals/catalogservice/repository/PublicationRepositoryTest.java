package com.periodicals.catalogservice.repository;

import com.periodicals.catalogservice.model.entity.Publication;
import com.periodicals.catalogservice.util.PublicationTestDataUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Pavlo Mrochko
 */
@DataMongoTest
class PublicationRepositoryTest {

    @Autowired
    private PublicationRepository publicationRepository;

    @AfterEach
    void tearDown() {
        // Remove all publications from repository
        publicationRepository.deleteAll();
    }

    @Test
    void savePublicationTest() {
        // Create a publication and save it in the repository
        Publication publication = PublicationTestDataUtil.createPublicationEntity();
        publication.setId(null);

        publicationRepository.save(publication);

        // Assert that the publication ID is not empty
        assertThat(publication.getId()).isNotEmpty();
    }

    @Test
    void findPublicationByIdTest() {
        // Create a publication and save it in the repository
        Publication publication = PublicationTestDataUtil.createPublicationEntity();
        publication.setId(null);
        publicationRepository.save(publication);

        // Retrieve the ID of the saved publication
        String publicationId = publication.getId();

        // Use the publicationRepository to find the publication by its ID
        Optional<Publication> foundPublication = publicationRepository.findById(publicationId);

        // Assert that the foundPublication is present and matches the expected publication
        assertThat(foundPublication).isPresent();
        assertThat(foundPublication.get()).isEqualTo(publication);
    }

    @Test
    void existsPublicationByTitleTest() {
        // Create a publication and save it in the repository
        Publication publication = PublicationTestDataUtil.createPublicationEntity();
        publication.setId(null);
        publicationRepository.save(publication);

        // Check if a publication with the given title exists in the repository
        boolean exists = publicationRepository.existsByTitle(publication.getTitle());

        // Assert that the publication exists
        assertThat(exists).isTrue();
    }

    @Test
    void findAllPublicationsByTopicNameTest() {
        // Create a publications with a specific topic and save them the repository
        final String TOPIC_NAME = PublicationTestDataUtil.TOPIC_NAME;
        Publication publication1 = PublicationTestDataUtil.createPublicationEntity();
        publication1.setId(null);
        Publication publication2 = PublicationTestDataUtil.createPublicationEntity();
        publication2.setId(null);
        publicationRepository.saveAll(List.of(publication1, publication2));

        // Retrieve all publications with the given topic name from the repository
        List<Publication> publications = publicationRepository.findAllByTopicName(TOPIC_NAME);

        // Assert that the list of publications is not empty and contains the saved publication
        assertThat(publications).hasSize(2);
        assertThat(publications).contains(publication1);
        assertThat(publications).contains(publication2);
    }

    @Test
    void updatePublicationTest() {
        // Create a publication and save it in the repository
        Publication publication = PublicationTestDataUtil.createPublicationEntity();
        publication.setId(null);
        publicationRepository.save(publication);

        // Update the publication's title
        String updatedTitle = "Updated Title";
        publication.setTitle(updatedTitle);

        // Save the updated publication in the repository
        publicationRepository.save(publication);

        // Retrieve the publication by its ID
        Optional<Publication> updatedPublication = publicationRepository.findById(publication.getId());

        // Assert that the updated publication's title has been changed
        assertThat(updatedPublication).isPresent();
        assertThat(updatedPublication.get().getTitle()).isEqualTo(updatedTitle);
    }

    @Test
    void deletePublicationTest() {
        // Create a publication and save it in the repository
        Publication publication = PublicationTestDataUtil.createPublicationEntity();
        publication.setId(null);
        publicationRepository.save(publication);

        // Delete the publication from the repository
        publicationRepository.deleteById(publication.getId());

        // Check if the publication still exists in the repository
        boolean exists = publicationRepository.existsById(publication.getId());

        // Assert that the publication has been deleted
        assertThat(exists).isFalse();
    }

}
