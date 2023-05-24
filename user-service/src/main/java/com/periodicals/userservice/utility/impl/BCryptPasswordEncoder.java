package com.periodicals.userservice.utility.impl;

import com.periodicals.userservice.utility.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Pavlo Mrochko
 */
public class BCryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    @Override
    public boolean matches(String enteredPassword, String encodedPassword) {
        return BCrypt.checkpw(enteredPassword, encodedPassword);
    }

}
