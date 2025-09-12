package com.example.dto;

import com.idjea.validation.spring.annotations.RemoteEmail;
import com.idjea.validation.spring.annotations.RemotePhone;

public class UserDto {

  @RemoteEmail
  private String email;

//  @RemotePhone
  private String phone;

  // getters & setters
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
}
