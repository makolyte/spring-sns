package com.makolyte.springsnsstarter.controller;

import com.makolyte.springsnsstarter.model.Message;
import com.makolyte.springsnsstarter.model.SnsResponse;
import com.makolyte.springsnsstarter.service.MessagePublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController {

    private final MessagePublisher messagePublisher;

    public MessageController(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @PostMapping("/publish")
    public SnsResponse publishMessage(@RequestBody Message message) {
        return messagePublisher.publish(message);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    private String handleException(RuntimeException e) {
        return e.getMessage();
    }
}
