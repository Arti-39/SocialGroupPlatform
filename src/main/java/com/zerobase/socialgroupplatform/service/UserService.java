package com.zerobase.socialgroupplatform.service;

import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.domain.common.UserRole;
import com.zerobase.socialgroupplatform.domain.common.UserStatus;
import com.zerobase.socialgroupplatform.dto.UserLoginRequestDto;
import com.zerobase.socialgroupplatform.dto.UserLoginResponseDto;
import com.zerobase.socialgroupplatform.dto.UserResponseDto;
import com.zerobase.socialgroupplatform.dto.UserSignUpRequestDto;
import com.zerobase.socialgroupplatform.exception.CustomException;
import com.zerobase.socialgroupplatform.exception.ErrorCode;
import com.zerobase.socialgroupplatform.repository.UserRepository;
import com.zerobase.socialgroupplatform.security.JwtTokenProvider;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  // 회원가입
  public UserResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto) {

    // 아이디, 이메일, 닉네임 중복 검사
    if (userRepository.existsByUserId(userSignUpRequestDto.getUserId())) {
      throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
    }

    if (userRepository.existsByEmail(userSignUpRequestDto.getEmail())) {
      throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
    }

    if (userRepository.existsByNickname(userSignUpRequestDto.getNickname())) {
      throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
    }

    // 패스워드 암호화
    String encodedPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());

    User user = User.builder()
        .email(userSignUpRequestDto.getEmail())
        .userId(userSignUpRequestDto.getUserId())
        .password(encodedPassword)
        .nickname(userSignUpRequestDto.getNickname())
        .role(UserRole.MEMBER)
        .status(UserStatus.ACTIVE)
        .createdDate(LocalDateTime.now())
        .build();

    userRepository.save(user);
    return new UserResponseDto(user);
  }

  // 로그인
  public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto) {
    User user = userRepository.findByUserId(userLoginRequestDto.getUserId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
      throw new CustomException(ErrorCode.LOGIN_FAILED);
    }

    // jwt 토큰 생성
    String token = jwtTokenProvider.createToken(user.getUserId(), user.getRole().name());

    return new UserLoginResponseDto(token, user);
  }
}
