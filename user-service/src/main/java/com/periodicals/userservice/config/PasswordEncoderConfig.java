package com.periodicals.userservice.config;

import com.periodicals.userservice.utility.PasswordEncoder;
import com.periodicals.userservice.utility.impl.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Pavlo Mrochko
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
