package com.gmg.travice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig { // JSON <-> Java 각 직렬화, 역직렬화 담당하는
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();

        // LocalDateTime 매핑
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return om;
    }
}
