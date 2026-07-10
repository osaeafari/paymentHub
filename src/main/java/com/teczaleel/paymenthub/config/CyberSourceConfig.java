package com.teczaleel.paymenthub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Enterprise gateway initialization bean mapping configuration settings
 * and preparing the central RestClient communication channel.
 */
@Configuration
public class CyberSourceConfig {

    @Value("${cybersource.base-url}")
    private String baseUrl;

    @Value("${cybersource.merchant-id}")
    private String merchantId;

    @Value("${cybersource.api-key-id}")
    private String apiKeyId;

    @Value("${cybersource.api-secret-key}")
    private String apiSecretKey;

    /**
     * Instantiates and registers a customized, globally accessible RestClient bean
     * bound permanently to the CyberSource Base URL.
     */
    @Bean
    public RestClient cyberSourceRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("v-c-merchant-id", merchantId)
                .build();
    }

    // Standard high-performance getters to expose credential details cleanly to security services
    public String getBaseUrl() { return baseUrl; }
    public String getMerchantId() { return merchantId; }
    public String getApiKeyId() { return apiKeyId; }
    public String getApiSecretKey() { return apiSecretKey; }

    public boolean isSandboxMode() {
        return baseUrl != null && baseUrl.contains("apitest");
    }
}

