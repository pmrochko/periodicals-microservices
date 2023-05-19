package com.periodicals.paymentservice.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.periodicals.paymentservice.model.enums.UserRole;
import com.periodicals.paymentservice.model.validation.ValueOfEnum;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author Pavlo Mrochko
 */
@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
public class UserDTO {

    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    @ValueOfEnum(enumClass = UserRole.class)
    private UserRole userRole;

    @NotBlank(message = "'name' shouldn't be absent in the request")
    private String name;

    @NotBlank(message = "'surname' shouldn't be absent in the request")
    private String surname;

    @Email(message = "'email' should be valid in the request")
    private String email;

    private String phone;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Null(message = "'repeatPassword' should be absent in the request")
    private String password;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Null(message = "'repeatPassword' should be absent in the request")
    private String repeatPassword;

    @NotBlank(message = "'address' shouldn't be empty in the request")
    private String address;

    @AssertTrue(message = "'access' should be TRUE in the request")
    @NotNull(message = "'access' shouldn't be absent in the request")
    private Boolean access;

}
