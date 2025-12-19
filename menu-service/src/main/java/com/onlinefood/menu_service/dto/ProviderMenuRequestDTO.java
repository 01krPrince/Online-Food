package com.onlinefood.menu_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProviderMenuRequestDTO {

    @NotBlank
    private String menuItemId;

    private double price;
    private boolean available;
}
