package com.periodicals.userservice.utility;

/**
 * @author Pavlo Mrochko
 */
public interface PasswordEncoder {

    String encode(String password);

    boolean matches(String enteredPassword, String encodedPassword);

}
