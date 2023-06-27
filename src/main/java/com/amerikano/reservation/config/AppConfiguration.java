package com.amerikano.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    /**
     * Authorization HTTP header Bean
     */
    @Bean
    public String getAuthHeader() {
        return "Authorization";
    }
}
