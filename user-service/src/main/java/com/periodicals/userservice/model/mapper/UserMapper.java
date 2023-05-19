package com.periodicals.userservice.model.mapper;

import com.periodicals.userservice.model.dto.UserDTO;
import com.periodicals.userservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapToUser(UserDTO userDTO);

    UserDTO mapToUserDto(User user);

    List<UserDTO> mapToListOfUsersDto(List<User> userList);

}
