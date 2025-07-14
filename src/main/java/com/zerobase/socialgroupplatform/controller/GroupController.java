package com.zerobase.socialgroupplatform.controller;

import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.dto.GroupCreateRequestDto;
import com.zerobase.socialgroupplatform.dto.GroupResponseDto;
import com.zerobase.socialgroupplatform.security.CustomUserDetails;
import com.zerobase.socialgroupplatform.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {
  private final GroupService groupService;

  // 모임 생성
  @PostMapping("/create")
  public ResponseEntity<GroupResponseDto> createGroup(
      @RequestBody @Valid GroupCreateRequestDto groupCreateRequestDto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User groupOwner = customUserDetails.getUser(); // 인증된 사용자 확인
    GroupResponseDto groupResponseDto = groupService.createGroup(groupCreateRequestDto, groupOwner);

    return ResponseEntity.status(HttpStatus.CREATED).body(groupResponseDto);
  }
}
