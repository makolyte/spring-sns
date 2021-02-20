package com.makolyte.springsnsstarter.model;

import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {
    public static final String CATEGORY = "Category";
    public static final String PRODUCT_NAME = "ProductName";
    public static final String EVENT_TYPE = "EventType";
    public static final String SELLER = "Seller";
    public static final String NEW_PRICE = "NewPrice";
    public static final String DEFAULT_MESSAGE_BODY = "Please see attributes.";


    public static PublishRequest build(String topicArn, Message message) {
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        attributes.put(CATEGORY, buildAttribute(message.getCategory(), "String"));
        attributes.put(PRODUCT_NAME, buildAttribute(message.getProductName(), "String"));
        attributes.put(EVENT_TYPE, buildAttribute(message.getEventType().toString(), "String"));
        attributes.put(SELLER, buildAttribute(message.getSeller(), "String"));
        attributes.put(NEW_PRICE, buildAttribute(message.getNewPrice().toString(), "Number"));

        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(DEFAULT_MESSAGE_BODY)
                .messageAttributes(attributes)
                .build();

        return request;
    }

    private static MessageAttributeValue buildAttribute(String value, String dataType) {
        return MessageAttributeValue.builder()
                .dataType(dataType)
                .stringValue(value)
                .build();
    }
}
