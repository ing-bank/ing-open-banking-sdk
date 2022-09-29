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
                "<a href=\"/account\">Go to account demo endpoint</a><br/>" +
                "<a href=\"/greetings/single\">Call greetings endpoint of the premium showcase API</a><br/>" +
                "<a href=\"/greetings/mtls\">Call greetings endpoint of the premium showcase API using mTLS pinning</a><br/>" +
                "<a href=\"/greetings/jws\">Call greetings endpoint of the premium showcase API using JWS</a><br/>";
    }

}
