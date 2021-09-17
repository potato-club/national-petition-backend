package com.example.nationalpetition.controller.oauth2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2TestController {

    @GetMapping("oauth")
    public String test() {
        return "oauth";
    }
}
