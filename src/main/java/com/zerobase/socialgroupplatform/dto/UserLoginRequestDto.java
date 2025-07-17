package com.zerobase.socialgroupplatform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginRequestDto {
  @NotBlank
  private String userId;

  @NotBlank
  private String password;
}
