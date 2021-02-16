package com.makolyte.springsnsstarter.controller;

import com.makolyte.springsnsstarter.service.MessagePublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final MessagePublisher messagePublisher;

    public MessageController(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @PostMapping("/publish")
    public void publishMessage() {
        messagePublisher.publish();
    }
}
