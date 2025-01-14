package org.zelator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZelatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZelatorApplication.class, args);
    }

}
