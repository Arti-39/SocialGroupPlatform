package com.zerobase.socialgroupplatform.controller;

import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.dto.ScheduleCreateRequestDto;
import com.zerobase.socialgroupplatform.dto.ScheduleDetailResponseDto;
import com.zerobase.socialgroupplatform.dto.ScheduleResponseDto;
import com.zerobase.socialgroupplatform.security.CustomUserDetails;
import com.zerobase.socialgroupplatform.service.GroupScheduleService;
import com.zerobase.socialgroupplatform.service.UserValidationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group/{groupId}/schedule")
public class GroupScheduleController {
  private final GroupScheduleService groupScheduleService;
  private final UserValidationService userValidationService;

  // 일정 등록(모임장)
  @PostMapping
  public ResponseEntity<ScheduleResponseDto> createSchedule (
      @PathVariable Long groupId,
      @RequestBody @Valid ScheduleCreateRequestDto scheduleCreateRequestDto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);

    ScheduleResponseDto scheduleResponseDto =
        groupScheduleService.createSchedule(groupId, scheduleCreateRequestDto, currentUser.getId());

    return ResponseEntity.status(HttpStatus.CREATED).body(scheduleResponseDto);
  }

  // 일정 수정(모임장)
  @PutMapping("/{scheduleId}")
  public ResponseEntity<ScheduleResponseDto> updateSchedule(
      @PathVariable Long groupId,
      @PathVariable Long scheduleId,
      @RequestBody @Valid ScheduleCreateRequestDto scheduleCreateRequestDto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);

    ScheduleResponseDto scheduleResponseDto =
        groupScheduleService.updateSchedule(groupId, scheduleId, currentUser.getId(), scheduleCreateRequestDto);

    return ResponseEntity.ok(scheduleResponseDto);
  }

  // 일정 삭제(모임장)
  @DeleteMapping("/{scheduleId}")
  public ResponseEntity<ScheduleResponseDto> deleteSchedule(
      @PathVariable Long groupId,
      @PathVariable Long scheduleId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);

    groupScheduleService.deleteSchedule(groupId, scheduleId, currentUser.getId());

    return ResponseEntity.noContent().build();
  }

  // 일정 목록 조회
  @GetMapping
  public ResponseEntity<List<ScheduleResponseDto>> getSchedule (
      @PathVariable Long groupId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);

    List<ScheduleResponseDto> scheduleResponseDto = groupScheduleService.getSchedule(groupId, currentUser.getId());

    return ResponseEntity.ok(scheduleResponseDto);
  }

  // 단일 일정 상세 조회
  @GetMapping("/{scheduleId}")
  public ResponseEntity<ScheduleDetailResponseDto> getScheduleDetail (
      @PathVariable Long groupId,
      @PathVariable Long scheduleId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);

    ScheduleDetailResponseDto scheduleDetailResponseDto =
        groupScheduleService.getScheduleDetail(groupId, scheduleId, currentUser.getId());

    return ResponseEntity.ok(scheduleDetailResponseDto);
  }
}
