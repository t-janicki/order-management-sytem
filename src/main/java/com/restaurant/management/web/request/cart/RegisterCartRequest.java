package com.restaurant.management.web.request.cart;

import javax.validation.constraints.NotNull;

public final class RegisterCartRequest {

    @NotNull(message = "phone number cannot be null")
    private Long phoneNumber;

    public RegisterCartRequest() {
    }

    public RegisterCartRequest(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }
}
