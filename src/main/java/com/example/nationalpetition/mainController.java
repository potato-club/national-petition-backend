package com.example.nationalpetition;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class mainController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}
