package com.periodicals.userservice.util;

import com.periodicals.userservice.model.dto.UserDTO;
import com.periodicals.userservice.model.entity.User;
import com.periodicals.userservice.model.enums.UserRole;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public class UserTestDataUtil {

    public static final Long ID = 123L;
    public static final UserRole USER_ROLE = UserRole.ROLE_READER;
    public static final String NAME = "UserTestName";
    public static final String SURNAME = "UserTestSurname";
    public static final String EMAIL = "testuser@mail.com";
    public static final String PHONE = "+380123456789";
    public static final String PASSWORD = "testPass123";
    public static final String ADDRESS = "Freedom st. 21";
    public static final Boolean ACCESS = Boolean.TRUE;

    public static User createUserEntity() {
        return new User(ID, USER_ROLE, NAME, SURNAME, EMAIL, PHONE, PASSWORD, ADDRESS, ACCESS);
    }

    public static UserDTO createUserDTO() {
        return UserDTO.builder()
                .id(ID)
                .userRole(USER_ROLE)
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .phone(PHONE)
                .password(PASSWORD)
                .repeatPassword(PASSWORD)
                .address(ADDRESS)
                .access(ACCESS)
                .build();
    }

    public static List<User> createListOfUsers(@Positive int size) {

        List<User> userList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            userList.add(new User(
                    ID + i,
                    USER_ROLE,
                    NAME + i,
                    SURNAME + i,
                    i + EMAIL,
                    PHONE + i,
                    PASSWORD,
                    ADDRESS,
                    ACCESS
            ));
        }

        return userList;
    }

    public static List<UserDTO> createListOfUserDTOs(@Positive int size) {

        List<UserDTO> userDTOList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            userDTOList.add(UserDTO.builder()
                    .id(ID + i)
                    .userRole(USER_ROLE)
                    .email(i + EMAIL)
                    .name(NAME + i)
                    .surname(SURNAME + i)
                    .access(ACCESS)
                    .address(ADDRESS)
                    .password(PASSWORD)
                    .repeatPassword(PASSWORD)
                    .build()
            );
        }

        return userDTOList;
    }
}
