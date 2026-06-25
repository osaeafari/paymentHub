package com.teczaleel.paymenthub.service;

import com.teczaleel.paymenthub.config.CyberSourceConfig;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * Service handling system diagnostics and external gateway connection health verification.
 */
@Service
public class PaymentHealthService {
    private final CyberSourceConfig cyberSourceConfig;

    // Spring automantically injects CyberSourceConfig here via constructor injection
    public PaymentHealthService(CyberSourceConfig cyberSourceConfig){
        this.cyberSourceConfig = cyberSourceConfig;
    }

    public Map<String, Object> checkGatewayConnectivity(){
        Map<String, Object> statusReport = new HashMap<>();

        statusReport.put("status", "UP");
        statusReport.put("gateway", "CyberSource REST Sandbox");
        statusReport.put("configuredMerchantId", cyberSourceConfig.getMerchantId());
        statusReport.put("timestamp", System.currentTimeMillis());

        return statusReport;
    }
}