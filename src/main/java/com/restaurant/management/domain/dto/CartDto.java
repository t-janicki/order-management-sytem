package com.restaurant.management.domain.dto;

import java.util.List;

public class CartDto {

    private Long id;
    private String uniqueId;
    private Boolean isOpen;
    private CustomerDto customer;
    private List<LineItemDto> lineItems;

    public CartDto(Long id,
                   String uniqueId,
                   Boolean isOpen,
                   CustomerDto customer,
                   List<LineItemDto> lineItems) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.isOpen = isOpen;
        this.customer = customer;
        this.lineItems = lineItems;
    }

    public Long getId() {
        return id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public List<LineItemDto> getLineItems() {
        return lineItems;
    }
}
