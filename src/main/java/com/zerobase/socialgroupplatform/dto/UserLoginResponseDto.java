package com.zerobase.socialgroupplatform.dto;

import com.zerobase.socialgroupplatform.domain.User;
import lombok.Getter;

@Getter
public class UserLoginResponseDto {
  private String token;
  private UserResponseDto user;

  public UserLoginResponseDto(String token, User user) {
    this.token = token;
    this.user = new UserResponseDto(user);
  }
}
