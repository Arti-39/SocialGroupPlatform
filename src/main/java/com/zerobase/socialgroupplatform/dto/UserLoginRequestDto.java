package com.zerobase.socialgroupplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginRequestDto {
  private String userId;
  private String password;
}
