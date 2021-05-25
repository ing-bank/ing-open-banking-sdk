package com.ing.developer.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

    @GetMapping
    public String getRoot() {
        return "<h1>My Awesome 3rd Party Demo App</h1>" +
                "<a href=\"/account\">Go to account demo endpoint</a>";
    }

}
