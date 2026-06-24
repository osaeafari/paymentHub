package com.teczaleel.paymenthub;

import com.teczaleel.paymenthub.config.CyberSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class PaymentHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentHubApplication.class, args);

        ApplicationContext context = SpringApplication.run(PaymentHubApplication.class, args);

        // Extract and verify our bound configuration bean
        CyberSourceConfig config = context.getBean(CyberSourceConfig.class);
        System.out.println("--- PaymentHub Gateway Boot Verification ---");
        System.out.println("Loaded Merchant ID: " + config.getMerchantId());
        System.out.println("Loaded Sandbox Mode: " + config.isSandboxMode());
        System.out.println("--------------------------------------------");
    }

}
