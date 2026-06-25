package com.teczaleel.paymenthub.controller;

import com.teczaleel.paymenthub.service.PaymentHealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * REST Endpoint for monitoring PaymentHub connection health metrics.
 */
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentHealthController {
    private final PaymentHealthService healthService;

    // Constructor Dependency Injection - No @Autowired needed for single constructors in Spring 4.x+
    public PaymentHealthController(PaymentHealthService healthService){
        this.healthService = healthService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getGatewayHealth(){
        Map<String, Object> response = healthService.checkGatewayConnectivity();
        return ResponseEntity.ok(response);
    }
}