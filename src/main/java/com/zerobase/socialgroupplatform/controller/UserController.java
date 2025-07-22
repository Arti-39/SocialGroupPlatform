package com.zerobase.socialgroupplatform.controller;

import com.zerobase.socialgroupplatform.dto.UserLoginRequestDto;
import com.zerobase.socialgroupplatform.dto.UserLoginResponseDto;
import com.zerobase.socialgroupplatform.dto.UserResponseDto;
import com.zerobase.socialgroupplatform.dto.UserSignUpRequestDto;
import com.zerobase.socialgroupplatform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<UserResponseDto> signUp(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
    UserResponseDto userResponseDto = userService.signUp(userSignUpRequestDto);
    return ResponseEntity.ok(userResponseDto);
  }

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
    UserLoginResponseDto userLoginResponseDto = userService.login(userLoginRequestDto);
    return ResponseEntity.ok(userLoginResponseDto);
  }
}
