package com.zerobase.socialgroupplatform.service;

import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.exception.CustomException;
import com.zerobase.socialgroupplatform.exception.ErrorCode;
import com.zerobase.socialgroupplatform.repository.UserRepository;
import com.zerobase.socialgroupplatform.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {
  private final UserRepository userRepository;

  public User validateUser(CustomUserDetails customUserDetails) {
    return userRepository.findByUserId(customUserDetails.getUserId())
        .orElseThrow(() -> new CustomException(ErrorCode.NO_PERMISSION));
  }
}