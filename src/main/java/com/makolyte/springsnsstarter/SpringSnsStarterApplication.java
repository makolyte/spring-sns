package com.makolyte.springsnsstarter;

import com.makolyte.springsnsstarter.config.AppListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSnsStarterApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringSnsStarterApplication.class);
        app.addListeners(new AppListener());
        app.run(args);
    }
}
