package com.onlinefood.menu_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "menu_items")
@Data
public class MenuItem {

    @Id
    private String id;

    private String name;        // Rajma Chawal
    private String categoryId;  // reference Category
    private boolean veg;        // veg / non-veg
}
