package com.periodicals.catalogservice.controller;

import com.periodicals.catalogservice.model.dto.PublicationDTO;
import com.periodicals.catalogservice.service.PublicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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

    @PostMapping("/topics/{topicName}/publications")
    @ResponseStatus(HttpStatus.CREATED)
    public PublicationDTO createPublication(@PathVariable @NotBlank String topicName,
                                            @RequestBody @Valid PublicationDTO publicationDTO) {
        log.info("Creating a new publication");
        return publicationService.createPublication(topicName, publicationDTO);
    }

    @GetMapping("/topics/{topicName}/publications")
    @ResponseStatus(HttpStatus.OK)
    public List<PublicationDTO> getAllPublicationsByTopicName(@PathVariable @NotBlank String topicName) {
        log.info("Getting a list of all publications by a topicName: {}", topicName);
        return publicationService.getAllPublications(topicName);
    }

    @GetMapping("/publications/{publicationId}")
    @ResponseStatus(HttpStatus.OK)
    public PublicationDTO getPublicationById(@PathVariable @NotBlank String publicationId) {
        log.info("Getting a publication by a publicationId: {}", publicationId);
        return publicationService.getPublicationById(publicationId);
    }

    @PutMapping("/publications/{publicationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updatePublication(@PathVariable @NotBlank String publicationId,
                                                    @RequestBody @Valid PublicationDTO publicationDTO) {
        log.info("Updating a publication with id: {}", publicationId);
        publicationService.updatePublication(publicationId, publicationDTO);
        return new ResponseEntity<>(publicationId, HttpStatus.OK);
    }

    @DeleteMapping("/publications/{publicationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deletePublication(@PathVariable @NotBlank String publicationId) {
        log.info("Deleting a publication with id: {}", publicationId);
        publicationService.deletePublication(publicationId);
        return new ResponseEntity<>(publicationId, HttpStatus.OK);
    }

}
