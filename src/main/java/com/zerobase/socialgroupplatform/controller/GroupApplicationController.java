package com.zerobase.socialgroupplatform.controller;

import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.domain.common.ApplicationStatus;
import com.zerobase.socialgroupplatform.dto.GroupApplicationResponseDto;
import com.zerobase.socialgroupplatform.security.CustomUserDetails;
import com.zerobase.socialgroupplatform.service.GroupApplicationService;
import com.zerobase.socialgroupplatform.service.UserValidationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class GroupApplicationController {
  private final GroupApplicationService groupApplicationService;
  private final UserValidationService userValidationService;

  // 모임 참가 신청(사용자)
  @PostMapping("/{groupId}")
  public ResponseEntity<?> applyToGroup(
      @PathVariable Long groupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);
    groupApplicationService.applyToGroup(groupId, currentUser.getId());

    return ResponseEntity.ok("참가 신청이 완료되었습니다.");
  }


  // 신청 승인(모임장)
  @PutMapping("/{applicationId}/accept")
  public ResponseEntity<?> acceptApplication(
      @PathVariable Long applicationId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);
    groupApplicationService.updateApplicationStatus(applicationId, currentUser.getId(),
            ApplicationStatus.ACCEPTED);

    return ResponseEntity.ok("신청이 승인되었습니다.");
  }

  // 신청 거절(모임장)
  @PutMapping("/{applicationId}/reject")
  public ResponseEntity<?> rejectApplication(
      @PathVariable Long applicationId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);
    groupApplicationService.updateApplicationStatus(applicationId, currentUser.getId(),
            ApplicationStatus.REJECTED);

    return ResponseEntity.ok("신청이 거절되었습니다.");
  }

  // 신청 취소(사용자)
  @PutMapping("/{applicationId}/cancel")
  public ResponseEntity<?> cancelApplication(
      @PathVariable Long applicationId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);
    groupApplicationService.cancelApplication(applicationId, currentUser.getId());

    return ResponseEntity.ok("신청을 취소하였습니다.");
  }

  // 신청 내역 조회(사용자)
  @GetMapping("/mine")
  public ResponseEntity<List<GroupApplicationResponseDto>> getMyApplication(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);
    List<GroupApplicationResponseDto> groupApplicationList =
        groupApplicationService.getApplicationByUser(currentUser.getId());

    return ResponseEntity.ok(groupApplicationList);
  }

  // 신청 내역 조회(모임장)
  @GetMapping("/group/{groupId}")
  public ResponseEntity<List<GroupApplicationResponseDto>> getGroupApplication(
      @PathVariable Long groupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);
    List<GroupApplicationResponseDto> groupApplicationList =
        groupApplicationService.getApplicationByGroup(groupId, currentUser.getId());

    return ResponseEntity.ok(groupApplicationList);
  }

  // 모임 탈퇴
  @PutMapping("/{groupId}/withdraw")
  public ResponseEntity<?> withdrawFromGroup(
      @PathVariable Long groupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);
    groupApplicationService.withdrawFromGroup(groupId, currentUser.getId());

    return ResponseEntity.ok("탈퇴하였습니다.");
  }
}
