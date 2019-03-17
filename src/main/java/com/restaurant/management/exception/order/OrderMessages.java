package com.restaurant.management.exception.order;

public enum  OrderMessages {
    ORDER_LIST_EXISTS("Daily order list is already open. "),
    ORDER_LIST_NOT_FOUND("Order list not found. Please open it first. "),
    ORDER_LIST_DELETED("Order list deleted. "),
    ORDER_DELETED("Order deleted"),
    ORDER_LIST_NOT_OPEN("Order list required to close daily list."),
    ORDER_NUMBER_NOT_FOUND("Order with provided order number not found. ");

    private String errorMessage;

    OrderMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the errorMessage
     */
    public String getMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
