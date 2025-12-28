package com.onlinefood.menu_service.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BulkMenuItemUpdateDTO {

    private List<String> addItemIds;      // items to add into menu
    private List<String> removeItemIds;   // items to remove from menu
}
