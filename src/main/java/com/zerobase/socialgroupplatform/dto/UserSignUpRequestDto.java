package com.zerobase.socialgroupplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignUpRequestDto {
  private String email;
  private String userId;
  private String password;
  private String nickname;
}
