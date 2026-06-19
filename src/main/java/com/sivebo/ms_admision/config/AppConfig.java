package com.sivebo.ms_admision.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

        @Value("${ms.clientes.url}")
        private String clientesBaseUrl;

        @Value("${ms.sucursales.url}")
        private String sucursalesBaseUrl;

        @Value("${ms.auth.url}")
        private String authBaseUrl;

        @Value("${ms.tracking.url}")
        private String trackingBaseUrl;

        @Bean
        public WebClient.Builder webClientBuilder() {
                return WebClient.builder();
        }

        @Bean
        public WebClient clientesWebClient(WebClient.Builder builder) {
                return builder.baseUrl(clientesBaseUrl).build();
        }

        @Bean
        public WebClient sucursalesWebClient(WebClient.Builder builder) {
                return builder.baseUrl(sucursalesBaseUrl).build();
        }

        @Bean
        public WebClient authWebClient(WebClient.Builder builder) {
                return builder.baseUrl(authBaseUrl).build();
        }

        @Bean
        public WebClient trackingWebClient(WebClient.Builder builder) {
                return builder.baseUrl(trackingBaseUrl).build();
        }
}
