package com.teczaleel.paymenthub.service;

import com.teczaleel.paymenthub.config.CyberSourceConfig;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Enterprise cryptographic layer handling authorization headers and digital body digest generation.
 */
@Service
public class CyberSourceSecurityService {

    private final CyberSourceConfig config;

    public CyberSourceSecurityService(CyberSourceConfig config) {
        this.config = config;
    }

    /**
     * Generates a standard SHA-256 text hash signature string representation of the request body payload.
     */
    public String calculateDigest(String requestBody) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(requestBody.getBytes(StandardCharsets.UTF_8));
        return "SHA-256=" + Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Builds the final cryptographic authorization token header used to validate merchant account requests.
     */
    public String generateSignatureHeader(String gmtDate, String digest, String targetUri) throws Exception {
        // 1. Re-assemble the signature parameter payload string block exactly as demanded by CyberSource
        String signatureString = String.format(
                "host: apitest.cybersource.com\n" +
                        "date: %s\n" +
                        "(request-target): post %s\n" +
                        "digest: %s\n" +
                        "v-c-merchant-id: %s",
                gmtDate, targetUri, digest, config.getMerchantId()
        );

        // 2. Perform HMAC-SHA256 signature signing using your encoded Sandbox API Secret Key
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        byte[] secretKeyBytes = Base64.getDecoder().decode(config.getApiSecretKey());
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
        hmacSha256.init(secretKeySpec);

        byte[] signedBytes = hmacSha256.doFinal(signatureString.getBytes(StandardCharsets.UTF_8));
        String encodedSignature = Base64.getEncoder().encodeToString(signedBytes);

        // 3. Assemble components back out to standard header wrapper format
        return String.format(
                "keyid=\"%s\", algorithm=\"HmacSHA256\", headers=\"host date (request-target) digest v-c-merchant-id\", signature=\"%s\"",
                config.getApiKeyId(), encodedSignature
        );
    }

    /**
     * Helper to consistently provide standard HTTP GMT time format.
     */
    public String getGmtDateTimeString() {
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
    }
}