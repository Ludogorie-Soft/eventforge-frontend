package com.example.EventForgeFrontend.config;

import com.example.EventForgeFrontend.deserializer.PageDeserializer;
import com.example.EventForgeFrontend.exception.decoder.CustomErrorDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.codec.ErrorDecoder;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

@Configuration
public class ApplicationConfig {

    @Value("${space.bucket.origin.url}")
    private String spaceBucketOriginUrl;
    @Value("${digital.ocean.access.key}")
    private String digitalOceanAccessKey;
    @Value("${digital.ocean.secret.key}")
    private String digitalOceanSecretKey;
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new SimpleModule()
                .addDeserializer(Page.class, new PageDeserializer<>())
        );

        return objectMapper;
    }
    @Bean
    public MinioClient minioClient (){
        return MinioClient.builder().endpoint(spaceBucketOriginUrl)
                .region("fra1")
                .credentials(digitalOceanAccessKey ,digitalOceanSecretKey).build();
    }
}
