package com.zerobase.socialgroupplatform.dto;

import com.zerobase.socialgroupplatform.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
  private Long id;
  private String userId;
  private String email;
  private String nickname;
  private String role;

  public UserResponseDto(User user) {
    this.id = user.getId();
    this.userId = user.getUserId();
    this.email = user.getEmail();
    this.nickname = user.getNickname();
    this.role = user.getRole().toString();
  }
}
