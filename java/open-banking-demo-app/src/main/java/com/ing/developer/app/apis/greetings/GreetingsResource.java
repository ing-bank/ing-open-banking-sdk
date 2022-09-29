package com.ing.developer.app.apis.greetings;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/greetings")
public class GreetingsResource {
  private final GreetingsAdapter greetingsAdapter;

  public GreetingsResource(GreetingsAdapter greetingsAdapter) {
    this.greetingsAdapter = greetingsAdapter;
  }

  @GetMapping
  @RequestMapping("/single")
  public String single() {
    return greetingsAdapter.getGreeting();
  }

  @RequestMapping("/mtls")
  public String mtls() {
    return greetingsAdapter.getGreeting();
  }

  @RequestMapping("/jws")
  public String jws() {
    return greetingsAdapter.getGreeting();
  }


}
