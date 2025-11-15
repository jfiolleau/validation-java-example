package com.example.dto;

import com.idjea.verifnow.spring.annotations.VerifNowEmail;
import com.idjea.verifnow.spring.annotations.VerifNowPhone;

public class UserDto {

  @VerifNowEmail
  private String email;

  @VerifNowPhone
  private String phone;

  // getters & setters
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
}
