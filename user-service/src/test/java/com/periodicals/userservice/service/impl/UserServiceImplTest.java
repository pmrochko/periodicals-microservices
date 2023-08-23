package com.periodicals.userservice.service.impl;

import com.periodicals.userservice.model.dto.UserDTO;
import com.periodicals.userservice.model.entity.User;
import com.periodicals.userservice.model.enums.UserRole;
import com.periodicals.userservice.model.exception.EntityNotFoundException;
import com.periodicals.userservice.model.exception.NotUniqueParameterException;
import com.periodicals.userservice.repository.UserRepository;
import com.periodicals.userservice.util.UserTestDataUtil;
import com.periodicals.userservice.utility.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.periodicals.userservice.util.UserTestDataUtil.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldCreateSuccessful() {
        User userEntity = UserTestDataUtil.createUserEntity();
        UserDTO requestUserDTO = UserTestDataUtil.createUserDTO();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhone(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        UserDTO actualUserDTO = userService.createUser(requestUserDTO);

        assertThatUsersAreEquals(actualUserDTO, userEntity);
    }

    @Test
    void createUser_whenEmailAlreadyExists_shouldThrowException() {
        UserDTO requestUserDTO = UserTestDataUtil.createUserDTO();

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(NotUniqueParameterException.class,
                () -> userService.createUser(requestUserDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_whenPhoneAlreadyExists_shouldThrowException() {
        UserDTO requestUserDTO = UserTestDataUtil.createUserDTO();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhone(anyString())).thenReturn(true);

        assertThrows(NotUniqueParameterException.class,
                () -> userService.createUser(requestUserDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserByID_shouldGetSuccessful() {
        final long userID = UserTestDataUtil.ID;
        User userEntity = UserTestDataUtil.createUserEntity();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        UserDTO actualUserDTO = userService.getUserByID(userID);

        assertThatUsersAreEquals(actualUserDTO, userEntity);
    }

    @Test
    void getUserByID_whenUserNotFound_shouldThrowException() {
        final long userID = UserTestDataUtil.ID;

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.getUserByID(userID));
    }

    @Test
    void getAllUsers_shouldGetSuccessful() {
        final int sizeOfList = 3;
        List<User> users = UserTestDataUtil.createListOfUsers(sizeOfList);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> actualUserDTOList = userService.getAllUsers();

        IntStream.range(0, sizeOfList)
                .forEach(i -> assertThatUsersAreEquals(
                        actualUserDTOList.get(i), users.get(i))
                );
    }

    @Test
    void updateUser_shouldUpdateSuccessful() {
        final long userID = UserTestDataUtil.ID;
        UserDTO requestUserDTO = UserTestDataUtil.createUserDTO();
        when(userRepository.existsById(userID)).thenReturn(true);
        doNothing().when(userRepository)
                .updateUser(anyString(), any(UserRole.class), anyString(), anyString(), anyString(), any(), anyBoolean(), anyLong());

        userService.updateUser(userID, requestUserDTO);

        verify(userRepository, times(1))
                .updateUser(anyString(), any(UserRole.class), anyString(), anyString(), anyString(), any(), anyBoolean(), anyLong());
    }

    @Test
    void updateUser_whenUserNotFound_shouldThrowException() {
        final long userID = UserTestDataUtil.ID;
        UserDTO requestUserDTO = UserTestDataUtil.createUserDTO();
        when(userRepository.existsById(userID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> userService.updateUser(userID, requestUserDTO));

        verify(userRepository, never())
                .updateUser(anyString(), any(UserRole.class), anyString(), anyString(), anyString(), any(), anyBoolean(), anyLong());
    }

    private void assertThatUsersAreEquals(UserDTO actualUserDTO, User expectedUserEntity) {
        assertThat(actualUserDTO.getId()).isEqualTo(expectedUserEntity.getId());
        assertThat(actualUserDTO.getEmail()).isEqualTo(expectedUserEntity.getEmail());
        assertThat(actualUserDTO.getUserRole()).isEqualTo(expectedUserEntity.getUserRole());
        assertThat(actualUserDTO.getName()).isEqualTo(expectedUserEntity.getName());
        assertThat(actualUserDTO.getSurname()).isEqualTo(expectedUserEntity.getSurname());
        assertThat(actualUserDTO.getPassword()).isEqualTo(expectedUserEntity.getPassword());
        assertThat(actualUserDTO.getPhone()).isEqualTo(expectedUserEntity.getPhone());
        assertThat(actualUserDTO.getAddress()).isEqualTo(expectedUserEntity.getAddress());
        assertThat(actualUserDTO.getAccess()).isEqualTo(expectedUserEntity.getAccess());
    }

}