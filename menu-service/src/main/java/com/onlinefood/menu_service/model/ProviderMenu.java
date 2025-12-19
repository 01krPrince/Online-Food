package com.onlinefood.menu_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "provider_menu")
@Data
public class ProviderMenu {

    @Id
    private String id;

    private String providerId;
    private String menuItemId;

    private double price;
    private boolean available;
}
