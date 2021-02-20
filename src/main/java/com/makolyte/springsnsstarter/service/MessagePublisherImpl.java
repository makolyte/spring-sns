package com.makolyte.springsnsstarter.service;

import com.makolyte.springsnsstarter.config.AwsProperties;
import com.makolyte.springsnsstarter.model.Message;
import com.makolyte.springsnsstarter.model.RequestBuilder;
import com.makolyte.springsnsstarter.model.SnsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

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
    public SnsResponse publish(Message message) {
        SnsResponse response = null;

        try {
            PublishRequest request = RequestBuilder.build(awsProperties.getTopicArn(), message);
            LOG.info("Request: {}", request);

            PublishResponse publishResponse = snsClient.publish(request);
            LOG.info("Publish response: {}", publishResponse);

            SdkHttpResponse httpResponse = publishResponse.sdkHttpResponse();
            response = new SnsResponse(
                    httpResponse.statusCode(),
                    httpResponse.statusText().orElse(null),
                    publishResponse.messageId());
            LOG.info("Response details: {}", response);
        } catch (SnsException e) {
            rethrow(e.statusCode(), e.getClass().getSimpleName() + ": " + e.awsErrorDetails());
        } catch (SdkServiceException e) {
            rethrow(e.statusCode(), e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (SdkClientException e) {
            rethrow(null, e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (SdkException e) {
            rethrow(null, e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        return response;
    }

    private void rethrow(Integer statusCode, String detailedMessage) {
        SnsResponse response = new SnsResponse(statusCode, detailedMessage, null);
        throw new RuntimeException(response.toString());
    }
}
