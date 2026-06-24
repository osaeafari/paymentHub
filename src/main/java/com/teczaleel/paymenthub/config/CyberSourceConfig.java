package com.teczaleel.paymenthub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Type-safe configuration bean binding CyberSource credentials from properties files.
 */
@Configuration
@ConfigurationProperties(prefix = "payments.cybersource")
public class CyberSourceConfig {

    private String merchantId;
    private String apiKey;
    private boolean sandboxMode;

    // Getters and Setters (Required for standard Spring properties binding)
    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public boolean isSandboxMode() { return sandboxMode; }
    public void setSandboxMode(boolean sandboxMode) { this.sandboxMode = sandboxMode; }
}