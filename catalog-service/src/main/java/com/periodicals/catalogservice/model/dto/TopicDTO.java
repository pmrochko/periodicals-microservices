package com.periodicals.catalogservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * A DTO for the {@link com.periodicals.catalogservice.model.entity.Topic} entity
 * @author Pavlo Mrochko
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicDTO {

    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String name;

    @JsonProperty(access = Access.READ_ONLY)
    private Set<PublicationDTO> publications;

}
