package com.periodicals.catalogservice.model.mapper;

import com.periodicals.catalogservice.model.dto.PublicationDTO;
import com.periodicals.catalogservice.model.entity.Publication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Mapper
public interface PublicationMapper {

    PublicationMapper INSTANCE = Mappers.getMapper(PublicationMapper.class);

    Publication mapToPublication(PublicationDTO publicationDTO);

    @Mapping(source = "topic.id", target = "topicId")
    PublicationDTO mapToPublicationDto(Publication publication);

    List<PublicationDTO> mapToListOfPublicationsDto(List<Publication> publicationList);

}
