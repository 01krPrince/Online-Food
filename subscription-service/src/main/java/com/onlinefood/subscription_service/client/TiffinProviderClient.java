package com.onlinefood.subscription_service.client;

import com.onlinefood.subscription_service.dto.ProviderResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TiffinProviderClient {

    private final WebClient webClient;

    public TiffinProviderClient(WebClient.Builder builder) {
        this.webClient = builder.build(); // @LoadBalanced
    }

    public ProviderResponseDTO getProviderById(String providerId) {
        return webClient.get()
                .uri("http://tiffin-provider-service/providers/{id}", providerId)
                .retrieve()
                .bodyToMono(ProviderResponseDTO.class)
                .block();
    }
}
