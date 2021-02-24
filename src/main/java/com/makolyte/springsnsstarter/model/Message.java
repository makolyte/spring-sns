package com.makolyte.springsnsstarter.model;

import java.math.BigDecimal;

public class Message {
    private String category;
    private String productName;
    private EventType eventType;
    private String seller;
    private BigDecimal newPrice;

    public Message() {}

    public Message(String category, String productName, EventType eventType, String seller, BigDecimal newPrice) {
        this.category = category;
        this.productName = productName;
        this.eventType = eventType;
        this.seller = seller;
        this.newPrice = newPrice;
    }

    public String getCategory() {
        return category;
    }

    public String getProductName() {
        return productName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getSeller() {
        return seller;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }
}
