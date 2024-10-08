package com.example.auth.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The ModelMapperConfig class is a Spring configuration class that defines a
 * bean for {@link ModelMapper}.
 * 
 * {@link ModelMapper} is a library used for object mapping, particularly useful
 * in converting DTOs (Data Transfer Objects) to entities and vice versa.
 * 
 * This class is annotated with {@link Configuration}, indicating that it
 * provides Spring configuration.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Defines a {@link Bean} for the {@link ModelMapper} instance.
     * 
     * This method is used by the Spring container to create and manage a singleton
     * instance of {@link ModelMapper} that can be injected wherever required.
     * 
     * @return A new instance of {@link ModelMapper}.
     */
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
