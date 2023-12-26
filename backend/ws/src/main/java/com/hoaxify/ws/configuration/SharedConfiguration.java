package com.hoaxify.ws.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharedConfiguration {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
