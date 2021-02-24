package com.makolyte.springsnsstarter.service;

import com.makolyte.springsnsstarter.config.AwsProperties;
import com.makolyte.springsnsstarter.model.EventType;
import com.makolyte.springsnsstarter.model.Message;
import com.makolyte.springsnsstarter.model.SnsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagePublisherImplTest {
    private MessagePublisher messagePublisher;
    private Message message;

    @Mock
    private SnsClient snsClient;

    @Mock
    private AwsProperties awsProperties;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private PublishResponse publishResponse;

    @Mock
    private SdkHttpResponse httpResponse;

    @BeforeEach
    void setup() {
        messagePublisher = new MessagePublisherImpl(snsClient, awsProperties);
        message = new Message("Books",
                "Keep Sharp: Build a Better Brain at Any Age",
                EventType.INCREASE,
                "Amazon",
                new BigDecimal("16.80"));

        when(awsProperties.getTopicArn()).thenReturn("topicArn");
    }

    @Test
    void publish() {
        when(snsClient.publish(any(PublishRequest.class))).thenReturn(publishResponse);
        when(publishResponse.sdkHttpResponse().statusCode()).thenReturn(200);
        when(publishResponse.sdkHttpResponse().statusText()).thenReturn(Optional.of("ok"));
        when(publishResponse.messageId()).thenReturn("2021-02-23");

        SnsResponse response = messagePublisher.publish(message);

        verify(snsClient).publish(any(PublishRequest.class));
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("ok");
        assertThat(response.getPublishedMessageId()).isEqualTo("2021-02-23");
    }

    @Test
    void publish_snsException() {
        SnsException exception = mock(SnsException.class);
        AwsErrorDetails errorDetails = mock(AwsErrorDetails.class);
        when(exception.statusCode()).thenReturn(500);
        when(exception.awsErrorDetails()).thenReturn(errorDetails);
        when(errorDetails.toString()).thenReturn("error details");

        when(snsClient.publish(any(PublishRequest.class))).thenThrow(exception);

        assertThatThrownBy(() -> messagePublisher.publish(message))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("SnsResponse{statusCode=500, message='SnsException: error details', publishedMessageId='null'}");
    }

    @Test
    void publish_sdkServiceException() {
        SdkServiceException exception = mock(SdkServiceException.class);
        when(exception.statusCode()).thenReturn(409);
        when(exception.getMessage()).thenReturn("sdk exception");

        when(snsClient.publish(any(PublishRequest.class))).thenThrow(exception);

        assertThatThrownBy(() -> messagePublisher.publish(message))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("SnsResponse{statusCode=409, message='SdkServiceException: sdk exception', publishedMessageId='null'}");
    }

    @Test
    void publish_sdkClientException() {
        SdkClientException exception = mock(SdkClientException.class);
        when(exception.getMessage()).thenReturn("sdk client exception");

        when(snsClient.publish(any(PublishRequest.class))).thenThrow(exception);

        assertThatThrownBy(() -> messagePublisher.publish(message))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("SnsResponse{statusCode=null, message='SdkClientException: sdk client exception', publishedMessageId='null'}");
    }

    @Test
    void publish_sdkException() {
        SdkException exception = mock(SdkException.class);
        when(exception.getMessage()).thenReturn("sdk exception");

        when(snsClient.publish(any(PublishRequest.class))).thenThrow(exception);

        assertThatThrownBy(() -> messagePublisher.publish(message))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("SnsResponse{statusCode=null, message='SdkException: sdk exception', publishedMessageId='null'}");
    }
}