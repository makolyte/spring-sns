package com.makolyte.springsnsstarter.service;

import com.makolyte.springsnsstarter.model.SnsResponse;

public interface MessagePublisher {
    SnsResponse publish();
}
