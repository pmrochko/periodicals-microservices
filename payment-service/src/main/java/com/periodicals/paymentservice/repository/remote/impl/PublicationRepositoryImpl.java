package com.periodicals.paymentservice.repository.remote.impl;

import com.periodicals.paymentservice.model.dto.PublicationDTO;
import com.periodicals.paymentservice.repository.remote.PublicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Pavlo Mrochko
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class PublicationRepositoryImpl implements PublicationRepository {

    private final WebClient webClient;
    private static final String CATALOG_SERVICE_URI = "http://localhost:8081/api/v1/";

    @Override
    public Optional<PublicationDTO> getPublicationById(Long publicationId) {

        PublicationDTO publicationDTO = webClient
                .get()
                .uri(CATALOG_SERVICE_URI + "/publications/" + publicationId)
                .retrieve()
                .bodyToMono(PublicationDTO.class)
                .block();

        return Optional.ofNullable(publicationDTO);
    }

    @Override
    public Boolean updatePublication(PublicationDTO publicationDTO) {

        Boolean result = webClient.put()
                .uri(CATALOG_SERVICE_URI + "/publications/" + publicationDTO.getId())
                .body(Mono.just(publicationDTO), PublicationDTO.class)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(Boolean.FALSE)
                .block();

        return result != Boolean.FALSE;
    }
}
