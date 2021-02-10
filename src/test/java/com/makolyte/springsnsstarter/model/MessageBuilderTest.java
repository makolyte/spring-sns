package com.makolyte.springsnsstarter.model;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MessageBuilderTest {

    private MessageBuilder messageBuilder = new MessageBuilder();

    @Test
    void buildMessage() {
        String expectedCategory = "Electronics";
        String expectedProductName = "LG 65UN7300PUF Alexa Built-In UHD 73 Series 65 4K Smart UHD TV (2020)";
        EventType expectedEventType = EventType.DROP;
        String expectedSeller = "Amazon";
        String expectedNewPrice = "646.99";

        PublishRequest publishRequest = messageBuilder.build(
                "topicArn",
                expectedCategory,
                expectedProductName,
                expectedEventType,
                expectedSeller,
                new BigDecimal(expectedNewPrice));

        assertThat(publishRequest.hasMessageAttributes()).isTrue();
        Map<String, MessageAttributeValue> actualAttributes = publishRequest.messageAttributes();
        assertThat(actualAttributes.get(MessageBuilder.CATEGORY).stringValue()).isEqualTo(expectedCategory);
        assertThat(actualAttributes.get(MessageBuilder.PRODUCT_NAME).stringValue()).isEqualTo(expectedProductName);
        assertThat(actualAttributes.get(MessageBuilder.EVENT_TYPE).stringValue()).isEqualTo(expectedEventType.toString());
        assertThat(actualAttributes.get(MessageBuilder.SELLER).stringValue()).isEqualTo(expectedSeller);
        assertThat(actualAttributes.get(MessageBuilder.NEW_PRICE).stringValue()).isEqualTo(expectedNewPrice);
        assertThat(publishRequest.message()).isEqualTo(MessageBuilder.DEFAULT_MESSAGE_BODY);
        assertThat(publishRequest.topicArn()).isEqualTo("topicArn");
    }
}