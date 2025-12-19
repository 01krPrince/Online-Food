package com.onlinefood.menu_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MenuItemRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String categoryId;

    private boolean veg;
}
