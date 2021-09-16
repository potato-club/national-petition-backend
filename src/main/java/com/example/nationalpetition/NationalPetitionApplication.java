package com.example.nationalpetition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class NationalPetitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(NationalPetitionApplication.class, args);
    }

}
