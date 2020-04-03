package com.combanc.ncm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NcmApplication {

    public static void main(String[] args) {
        SpringApplication.run(NcmApplication.class, args);
    }

}
