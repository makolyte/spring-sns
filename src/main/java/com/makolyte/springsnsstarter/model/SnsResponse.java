package com.makolyte.springsnsstarter.model;

public class SnsResponse {
    private Integer statusCode;
    private String message;
    private String publishedMessageId;

    public SnsResponse(Integer statusCode, String message, String publishedMessageId) {
        this.statusCode = statusCode;
        this.message = message;
        this.publishedMessageId = publishedMessageId;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getPublishedMessageId() {
        return publishedMessageId;
    }

    @Override
    public String toString() {
        return "SnsResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", publishedMessageId='" + publishedMessageId + '\'' +
                '}';
    }
}
