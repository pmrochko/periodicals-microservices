package com.periodicals.catalogservice.controller;

import com.periodicals.catalogservice.model.dto.PublicationDTO;
import com.periodicals.catalogservice.service.PublicationService;
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
@RequestMapping("/api/v1")
public class PublicationController {

    private final PublicationService publicationService;

    @PostMapping("/topics/{topicId}/publications")
    @ResponseStatus(HttpStatus.CREATED)
    public PublicationDTO createPublication(@PathVariable @Positive Long topicId,
                                            @RequestBody @Valid PublicationDTO publicationDTO) {
        log.info("Creating a new publication with id: {}", publicationDTO.getId());
        return publicationService.createPublication(topicId, publicationDTO);
    }

    @GetMapping("/topics/{topicId}/publications")
    @ResponseStatus(HttpStatus.OK)
    public List<PublicationDTO> getAllPublicationsByTopicId(@PathVariable @Positive Long topicId) {
        log.info("Getting a list of all publications by a topicId: {}", topicId);
        return publicationService.getAllPublications(topicId);
    }

    @GetMapping("/publications/{publicationId}")
    @ResponseStatus(HttpStatus.OK)
    public PublicationDTO getPublicationById(@PathVariable @Positive Long publicationId) {
        log.info("Getting a publication by a publicationId: {}", publicationId);
        return publicationService.getPublicationById(publicationId);
    }

    @PutMapping("/publications/{publicationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> updatePublication(@PathVariable @Positive Long publicationId,
                                            @RequestBody @Valid PublicationDTO publicationDTO) {
        log.info("Updating a publication with id: {}", publicationId);
        publicationService.updatePublication(publicationId, publicationDTO);
        return new ResponseEntity<>(publicationId, HttpStatus.OK);
    }

    @DeleteMapping("/publications/{publicationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> deletePublication(@Positive @PathVariable Long publicationId) {
        log.info("Deleting a publication with id: {}", publicationId);
        publicationService.deletePublication(publicationId);
        return new ResponseEntity<>(publicationId, HttpStatus.OK);
    }

}
