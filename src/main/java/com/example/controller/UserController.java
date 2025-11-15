package com.example.controller;

import com.example.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  @PostMapping
  public ResponseEntity<String> createUser(@RequestBody @Valid UserDto userDto) {
    // If validation passes, userDto.email and phone are valid according to your API
    return ResponseEntity.ok("User is valid and accepted!");
  }
}
