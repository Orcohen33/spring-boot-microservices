package com.orcohen.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced   // Load balance between service instances running at different ports.
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
