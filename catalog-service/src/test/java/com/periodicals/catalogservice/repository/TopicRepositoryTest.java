package com.periodicals.catalogservice.repository;

import com.periodicals.catalogservice.model.entity.Topic;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static com.periodicals.catalogservice.util.TopicTestDataUtil.NAME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Pavlo Mrochko
 */
@DataMongoTest
@TestMethodOrder(OrderAnnotation.class)
class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Test
    @Order(1)
    void saveTopicTest() {
        // Create a new topic
        Topic topic = new Topic();
        topic.setName(NAME);

        // Save the topic in the repository
        topicRepository.save(topic);

        // Assert that the topic ID is not empty
        assertThat(topic.getId()).isNotEmpty();
    }

    @Test
    @Order(2)
    void findTopicByNameTest() {
        // Find the topic by its name in the repository
        Optional<Topic> topic = topicRepository.findByName(NAME);

        // Assert that the topic is present and its name matches the expected name
        assertThat(topic).isPresent();
        assertThat(topic.get().getName()).isEqualTo(NAME);
    }

    @Test
    @Order(3)
    void existsTopicByNameTest() {
        // Check if a topic with the given name exists in the repository
        boolean exists = topicRepository.existsByName(NAME);

        // Assert that the topic exists
        assertThat(exists).isTrue();
    }

    @Test
    @Order(4)
    void findAllTopicsTest() {
        // Create a second topic and save it in the repository
        Topic secondTopic = new Topic();
        secondTopic.setName(NAME + "2");
        topicRepository.save(secondTopic);

        // Retrieve all topics from the repository
        List<Topic> topicList = topicRepository.findAll();

        // Assert that the topic list has a size of 2
        assertThat(topicList).hasSize(2);
    }

    @Test
    @Order(5)
    void updateTopicNameTest() {
        // Update the name of an existing topic
        final String updateTopicName = NAME + "updated";
        Topic topic = topicRepository.findByName(NAME).get();
        topic.setName(updateTopicName);

        // Save the updated topic in the repository
        Topic topicUpdated = topicRepository.save(topic);

        // Assert that the topic's name has been updated
        assertThat(topicUpdated.getName()).isEqualTo(updateTopicName);
    }

    @Test
    @Order(6)
    void deleteTopicTest() {
        // Delete a topic from the repository
        final String deleteTopicName = NAME + "2";
        Topic topic = topicRepository.findByName(deleteTopicName).get();
        topicRepository.deleteById(topic.getId());

        // Assert that the topic has been deleted and the repository size is updated
        assertThat(topicRepository.findAll()).hasSize(1);
        assertThat(topicRepository.findByName(deleteTopicName)).isEmpty();
    }

}
