package com.sivebo.ms_admision.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

        @Bean
        @LoadBalanced
        public WebClient.Builder webClientBuilder() {
                return WebClient.builder();
        }

        @Bean
        public WebClient clientesWebClient(WebClient.Builder builder) {
                return builder.baseUrl("http://ms-clientes").build();
        }

        @Bean
        public WebClient sucursalesWebClient(WebClient.Builder builder) {
                return builder.baseUrl("http://ms-sucursales").build();
        }

        @Bean
        public WebClient authWebClient(WebClient.Builder builder) {
                return builder.baseUrl("http://ms-auth").build();
        }

        @Bean
        public WebClient trackingWebClient(WebClient.Builder builder) {
                return builder.baseUrl("http://ms-tracking").build();
        }
}
