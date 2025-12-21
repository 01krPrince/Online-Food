package com.onlinefood.menu_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MenuItemUpdateRequestDTO {

    @Size(min = 3, max = 50)
    private String name;

    @Size(max = 300)
    private String description;

    @DecimalMin(value = "1.0", message = "Price must be >= 1")
    private Double price;

    private String imageUrl;

    private List<@Size(max = 20) String> tags;
}
