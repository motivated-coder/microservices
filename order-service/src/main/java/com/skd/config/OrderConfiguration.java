package com.skd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OrderConfiguration {

    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }
}
