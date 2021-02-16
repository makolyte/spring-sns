package com.makolyte.springsnsstarter.service;

import com.makolyte.springsnsstarter.config.AwsProperties;
import com.makolyte.springsnsstarter.model.EventType;
import com.makolyte.springsnsstarter.model.MessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.math.BigDecimal;

@Service
public class MessagePublisherImpl implements MessagePublisher {
    private final static Logger LOG = LoggerFactory.getLogger(MessagePublisherImpl.class);

    private final SnsClient snsClient;

    private final AwsProperties awsProperties;

    public MessagePublisherImpl(SnsClient snsClient, AwsProperties awsProperties) {
        this.snsClient = snsClient;
        this.awsProperties = awsProperties;
    }

    @Override
    public void publish() {
        PublishRequest request = MessageBuilder.build(
                awsProperties.getTopicArn(),
                "Electronics",
                "LG 65UN7300PUF Alexa Built-In UHD 73 Series 65 4K Smart UHD TV 2020",
                EventType.DROP,
                "Amazon",
                new BigDecimal("646.99"));

        System.out.println(request);

        PublishResponse response = snsClient.publish(request);

        LOG.info("Response: {}", response);
    }
}
