package com.makolyte.springsnsstarter.model;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RequestBuilderTest {

    private RequestBuilder requestBuilder = new RequestBuilder();

    @Test
    void buildMessage() {
        String expectedCategory = "Electronics";
        String expectedProductName = "LG 65UN7300PUF Alexa Built-In UHD 73 Series 65 4K Smart UHD TV (2020)";
        EventType expectedEventType = EventType.DROP;
        String expectedSeller = "Amazon";
        String expectedNewPrice = "646.99";

        PublishRequest publishRequest = requestBuilder.build(
                "topicArn",
                expectedCategory,
                expectedProductName,
                expectedEventType,
                expectedSeller,
                new BigDecimal(expectedNewPrice));

        assertThat(publishRequest.hasMessageAttributes()).isTrue();
        Map<String, MessageAttributeValue> actualAttributes = publishRequest.messageAttributes();
        assertThat(actualAttributes.get(RequestBuilder.CATEGORY).stringValue()).isEqualTo(expectedCategory);
        assertThat(actualAttributes.get(RequestBuilder.PRODUCT_NAME).stringValue()).isEqualTo(expectedProductName);
        assertThat(actualAttributes.get(RequestBuilder.EVENT_TYPE).stringValue()).isEqualTo(expectedEventType.toString());
        assertThat(actualAttributes.get(RequestBuilder.SELLER).stringValue()).isEqualTo(expectedSeller);
        assertThat(actualAttributes.get(RequestBuilder.NEW_PRICE).stringValue()).isEqualTo(expectedNewPrice);
        assertThat(publishRequest.message()).isEqualTo(RequestBuilder.DEFAULT_MESSAGE_BODY);
        assertThat(publishRequest.topicArn()).isEqualTo("topicArn");
    }
}