package com.zerobase.socialgroupplatform.controller;

import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.dto.ScheduleCreateRequestDto;
import com.zerobase.socialgroupplatform.dto.ScheduleDetailResponseDto;
import com.zerobase.socialgroupplatform.dto.ScheduleResponseDto;
import com.zerobase.socialgroupplatform.exception.CustomException;
import com.zerobase.socialgroupplatform.exception.ErrorCode;
import com.zerobase.socialgroupplatform.repository.UserRepository;
import com.zerobase.socialgroupplatform.security.CustomUserDetails;
import com.zerobase.socialgroupplatform.service.GroupScheduleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group/{groupId}/schedule")
public class GroupScheduleController {
  private final GroupScheduleService groupScheduleService;
  private final UserRepository userRepository;

  // 일정 등록(모임장)
  @PostMapping
  public ResponseEntity<ScheduleResponseDto> createSchedule (
      @PathVariable Long groupId,
      @RequestBody @Valid ScheduleCreateRequestDto scheduleCreateRequestDto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = validateUser(customUserDetails);
    ScheduleResponseDto scheduleResponseDto =
        groupScheduleService.createSchedule(groupId, scheduleCreateRequestDto, currentUser.getId());

    return ResponseEntity.status(HttpStatus.CREATED).body(scheduleResponseDto);
  }

  // 일정 목록 조회
  @GetMapping
  public ResponseEntity<List<ScheduleResponseDto>> getSchedule (@PathVariable Long groupId) {
    List<ScheduleResponseDto> scheduleResponseDto = groupScheduleService.getSchedule(groupId);

    return ResponseEntity.ok(scheduleResponseDto);
  }

  // 단일 일정 상세 조회
  @GetMapping("/{scheduleId}")
  public ResponseEntity<ScheduleDetailResponseDto> getScheduleDetail (
      @PathVariable Long groupId,
      @PathVariable Long scheduleId) {

    ScheduleDetailResponseDto scheduleDetailResponseDto =
        groupScheduleService.getScheduleDetail(groupId, scheduleId);

    return ResponseEntity.ok(scheduleDetailResponseDto);
  }

  // 모임 참석 여부 등록/수정
  // 참석 여부 전체 조회(모임장)

  // 사용자 인증 확인
  public User validateUser(CustomUserDetails customUserDetails) {
    return userRepository.findByUserId(customUserDetails.getUserId())
        .orElseThrow(() -> new CustomException(ErrorCode.NO_PERMISSION));
  }
}
