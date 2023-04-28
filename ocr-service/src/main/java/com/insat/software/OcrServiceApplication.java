package com.insat.software;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OcrServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OcrServiceApplication.class, args);
    }
}
