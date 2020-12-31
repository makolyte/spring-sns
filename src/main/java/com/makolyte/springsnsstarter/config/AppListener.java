package com.makolyte.springsnsstarter.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

public class AppListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        // Add AWS access key and secret key to Java system properties as soon as the environment is available
        System.setProperty("aws.accessKeyId", "your_access_key");
        System.setProperty("aws.secretAccessKey", "your_secret_key");
    }
}
