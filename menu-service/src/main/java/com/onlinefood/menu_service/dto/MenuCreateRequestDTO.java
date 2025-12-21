package com.onlinefood.menu_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MenuCreateRequestDTO {

    @NotBlank(message = "Menu name is required")
    @Size(min = 3, max = 30, message = "Menu name must be 3–30 characters")
    private String name; // Breakfast / Lunch / Dinner

    @Size(max = 200, message = "Description too long")
    private String description;
}
