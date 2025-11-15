package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "verifnow.api.api-key=dummy",
    "verifnow.api.base-url=http://localhost:9999"
})
class DemoApplicationTests {

  @Test
  void contextLoads() {
    // If the Spring context starts, this test passes.
  }
}

