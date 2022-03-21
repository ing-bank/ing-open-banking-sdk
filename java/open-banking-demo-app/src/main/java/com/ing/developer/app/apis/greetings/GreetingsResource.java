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

  @SuppressWarnings("unused")
  @GetMapping
  public String single() {
    return greetingsAdapter.getGreeting();
  }
}
