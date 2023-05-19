package com.periodicals.catalogservice.controller;

import com.periodicals.catalogservice.model.dto.TopicDTO;
import com.periodicals.catalogservice.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/topics")
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TopicDTO createTopic(@RequestBody @Valid TopicDTO topicDTO) {
        log.info("Creating a new topic with name: {}", topicDTO.getName());
        return topicService.createTopic(topicDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TopicDTO> getAllTopics() {
        log.info("Getting a list of all topics in a catalog");
        return topicService.getAllTopics();
    }

    @PutMapping("/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> updateTopic(@PathVariable @Positive Long topicId,
                                           @RequestBody @Valid TopicDTO topicDTO) {
        log.info("Updating a topic with id: {}", topicId);
        topicService.updateTopicName(topicId, topicDTO);
        return new ResponseEntity<>(topicId, HttpStatus.OK);
    }

    @DeleteMapping("/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> deleteTopic(@Positive @PathVariable Long topicId) {
        log.info("Deleting a topic with id: {}", topicId);
        topicService.deleteTopic(topicId);
        return new ResponseEntity<>(topicId, HttpStatus.OK);
    }

}
