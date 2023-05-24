package com.periodicals.userservice.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.periodicals.userservice.model.enums.UserRole;
import com.periodicals.userservice.model.validation.ValidPassword;
import com.periodicals.userservice.model.validation.ValidPhoneNumberUa;
import com.periodicals.userservice.model.validation.ValueOfEnum;
import com.periodicals.userservice.model.validation.gpoup.OnCreate;
import com.periodicals.userservice.model.validation.gpoup.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.periodicals.userservice.model.entity.User} entity
 * @author Pavlo Mrochko
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_EMPTY)
public class UserDTO {

    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    @ValueOfEnum(enumClass = UserRole.class, groups = {OnCreate.class, OnUpdate.class})
    private UserRole userRole;

    @NotBlank(message = "'name' shouldn't be absent in the request", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(message = "'surname' shouldn't be absent in the request", groups = {OnCreate.class, OnUpdate.class})
    private String surname;

    @Email(message = "'email' should be valid in the request")
    private String email;

    @ValidPhoneNumberUa
    private String phone;

    @JsonProperty(access = Access.WRITE_ONLY)
    @ValidPassword(message = "'password' should be valid in the request", groups = OnCreate.class)
    @Null(message = "'repeatPassword' should be absent in the request", groups = OnUpdate.class)
    private String password;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Null(message = "'repeatPassword' should be absent in the request", groups = OnUpdate.class)
    private String repeatPassword;

    @NotBlank(message = "'address' shouldn't be empty in the request")
    private String address;

    @AssertTrue(message = "'access' should be TRUE in the request", groups = OnCreate.class)
    @NotNull(message = "'access' shouldn't be absent in the request", groups = OnUpdate.class)
    private Boolean access;

    @AssertTrue(message = "'password' and 'repeatPassword' should match", groups = OnCreate.class)
    private boolean isPasswordsMatch() {
        return password != null && password.equals(repeatPassword);
    }

}
