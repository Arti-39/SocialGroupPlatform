package com.zerobase.socialgroupplatform.controller;

import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.dto.GroupCreateRequestDto;
import com.zerobase.socialgroupplatform.dto.GroupResponseDto;
import com.zerobase.socialgroupplatform.dto.GroupUpdateRequestDto;
import com.zerobase.socialgroupplatform.exception.CustomException;
import com.zerobase.socialgroupplatform.exception.ErrorCode;
import com.zerobase.socialgroupplatform.repository.UserRepository;
import com.zerobase.socialgroupplatform.security.CustomUserDetails;
import com.zerobase.socialgroupplatform.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {
  private final GroupService groupService;
  private final UserRepository userRepository;

  // 모임 생성
  @PostMapping("/create")
  public ResponseEntity<GroupResponseDto> createGroup(
      @RequestBody @Valid GroupCreateRequestDto groupCreateRequestDto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = validateUser(customUserDetails); // 인증된 사용자 확인
    GroupResponseDto groupResponseDto = groupService.createGroup(groupCreateRequestDto, currentUser);

    return ResponseEntity.status(HttpStatus.CREATED).body(groupResponseDto);
  }

  // 모임 수정
  @PutMapping("/{id}")
  public ResponseEntity<GroupResponseDto> updateGroup(
      @PathVariable Long id,
      @RequestBody @Valid GroupUpdateRequestDto groupUpdateRequestDto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = validateUser(customUserDetails);

    GroupResponseDto updatedGroup =
        groupService.updateGroup(id, currentUser.getId(), groupUpdateRequestDto);

    return ResponseEntity.ok(updatedGroup);
  }


  // 모임 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<GroupResponseDto> deleteGroup(
      @PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = validateUser(customUserDetails);

    groupService.deleteGroup(id, currentUser.getId());

    return ResponseEntity.noContent().build();
  }

  // 사용자 인증 확인
  public User validateUser(CustomUserDetails customUserDetails) {
    return userRepository.findByUserId(customUserDetails.getUserId())
        .orElseThrow(() -> new CustomException(ErrorCode.NO_PERMISSION));
  }
}
