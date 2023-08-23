package com.periodicals.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.periodicals.userservice.model.dto.UserDTO;
import com.periodicals.userservice.model.exception.EntityNotFoundException;
import com.periodicals.userservice.model.exception.NotUniqueParameterException;
import com.periodicals.userservice.service.UserService;
import com.periodicals.userservice.util.UserTestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.periodicals.userservice.util.UserTestDataUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    public static final String USER_API_URL = "/api/v1/users";

    @Test
    void createUser_whenValidInput_thenReturnsUserDtoAnd201Status() throws Exception {
        UserDTO userDTO = UserTestDataUtil.createUserDTO();

        String jsonRequestBody = "{\"id\":34,\"login\":\"login\"," +
                "\"email\":\"my_email@gmail.com\",\"userRole\":\"ROLE_READER\"," +
                "\"name\":\"Jack\",\"surname\":\"Strong\",\"phone\":\"+380123456789\"," +
                "\"access\":true,\"password\":\"TestPassword123\"," +
                "\"repeatPassword\":\"TestPassword123\"}";

        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post(USER_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.userRole").value(USER_ROLE.name()))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.surname").value(SURNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phone").value(PHONE))
                .andExpect(jsonPath("$.access").value(ACCESS))
                .andExpect(jsonPath("$.address").value(ADDRESS));
    }

    @Test
    void createUser_whenNotUniqueParameter_thenReturnsErrorAnd400Status() throws Exception {

        String jsonRequestBody = "{\"id\":34,\"login\":\"login\"," +
                "\"email\":\"my_email@gmail.com\",\"userRole\":\"ROLE_READER\"," +
                "\"name\":\"Jack\",\"surname\":\"Strong\",\"phone\":\"+380123456789\"," +
                "\"access\":true,\"password\":\"TestPassword123\"," +
                "\"repeatPassword\":\"TestPassword123\"}";

        doThrow(new NotUniqueParameterException())
                .when(userService)
                .createUser(any(UserDTO.class));

        mockMvc.perform(post(USER_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

    @Test
    void getAllUsers_shouldReturnsListOfUsersAnd200Status() throws Exception {
        final int sizeOfList = 5;
        List<UserDTO> userList = UserTestDataUtil.createListOfUserDTOs(sizeOfList);

        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get(USER_API_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(sizeOfList)));
    }

    @Test
    void getUserById_whenValidInput_thenReturnsUserDtoAnd200Status() throws Exception {
        final Long userID = ID;
        UserDTO userDTO = UserTestDataUtil.createUserDTO();

        when(userService.getUserByID(userID)).thenReturn(userDTO);

        mockMvc.perform(get(USER_API_URL + "/" + userID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.userRole").value(USER_ROLE.name()))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.surname").value(SURNAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.phone").value(PHONE))
                .andExpect(jsonPath("$.access").value(ACCESS))
                .andExpect(jsonPath("$.address").value(ADDRESS))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.repeatPassword").doesNotExist());
    }

    @Test
    void getUserById_whenEntityNotFound_thenReturnsErrorAnd400Status() throws Exception {
        final Long userID = ID;

        doThrow(new EntityNotFoundException())
                .when(userService)
                .getUserByID(userID);

        mockMvc.perform(get(USER_API_URL + "/" + userID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

    @Test
    void updateUser_whenValidInput_thenReturnsUserDtoAnd200Status() throws Exception {
        final Long userID = ID;
        UserDTO userDTO = UserTestDataUtil.createUserDTO();
        String jsonRequestBody = objectMapper.writeValueAsString(userDTO);

        when(userService.getUserByID(userID)).thenReturn(userDTO);

        mockMvc.perform(put(USER_API_URL + "/" + userID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(ID));
    }

    @Test
    void updateUser_whenEntityNotFound_thenReturnsErrorAnd400Status() throws Exception {
        final Long userID = ID;
        UserDTO userDTO = UserTestDataUtil.createUserDTO();
        String jsonRequestBody = objectMapper.writeValueAsString(userDTO);

        doThrow(new EntityNotFoundException())
                .when(userService)
                .updateUser(anyLong(),  any(UserDTO.class));

        mockMvc.perform(put(USER_API_URL + "/" + userID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorType").exists())
                .andExpect(jsonPath("$.timeStamp").exists());
    }

}
