package com.user_service.user_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Address {

    @NotBlank
    private String streetAddress;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid postal code")
    private String postalCode;

    @NotBlank
    private String country;

    private String landmark; // Optional, could be used to specify a nearby landmark

    // Optional fields
    private String additionalDetails; // Any extra address details like building name, floor, etc.

    // You can also add methods to format the address properly if needed
    public String getFullAddress() {
        return streetAddress + ", " + landmark + ", " + city + ", " + state + " - " + postalCode + ", " + country;
    }
}
