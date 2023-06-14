package com.periodicals.paymentservice.repository.remote.impl;

import com.periodicals.paymentservice.model.dto.PublicationDTO;
import com.periodicals.paymentservice.model.exception.EntityNotFoundException;
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

    private final WebClient.Builder webClientBuilder;
    private static final String CATALOG_SERVICE_URI = "lb://catalog-service/api/v1/";

    @Override
    public Optional<PublicationDTO> getPublicationById(String publicationId) {

        PublicationDTO publicationDTO = webClientBuilder.build()
                .get()
                .uri(CATALOG_SERVICE_URI + "/publications/" + publicationId)
                .retrieve()
                .bodyToMono(PublicationDTO.class)
                .onErrorResume(throwable -> {
                    throw new EntityNotFoundException("Publication was not found");
                })
                .block();

        return Optional.ofNullable(publicationDTO);
    }

    @Override
    public void updatePublication(PublicationDTO publicationDTO, String publicationId) {

        Boolean result = webClientBuilder.build()
                .put()
                .uri(CATALOG_SERVICE_URI + "/publications/" + publicationId)
                .body(Mono.just(publicationDTO), PublicationDTO.class)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(Boolean.FALSE)
                .block();

    }
}
