package com.example.EventForgeFrontend.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.EventForgeFrontend.deserializer.PageDeserializer;
import com.example.EventForgeFrontend.exception.decoder.CustomErrorDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

@Configuration
public class ApplicationConfig {

    @Value("${digital.ocean.access.key}")
    private String digitalOceanAccessKey;
    @Value("${digital.ocean.secret.key}")
    private String digitalOceanSecretKey;

    @Value("${space.bucket.origin.url}")
    private String spaceBucketOriginUrl;

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
    @Bean("dospaces")
    public AmazonS3 s3client() {

        BasicAWSCredentials creds = new BasicAWSCredentials(digitalOceanAccessKey, digitalOceanSecretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(spaceBucketOriginUrl, Regions.EU_CENTRAL_1.toString()))
                .withCredentials(new AWSStaticCredentialsProvider(creds)).build();

    }


}
