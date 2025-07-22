package com.zerobase.socialgroupplatform.controller;

import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.dto.ScheduleAttendanceRequestDto;
import com.zerobase.socialgroupplatform.dto.ScheduleAttendanceResponseDto;
import com.zerobase.socialgroupplatform.security.CustomUserDetails;
import com.zerobase.socialgroupplatform.service.ScheduleAttendanceService;
import com.zerobase.socialgroupplatform.service.UserValidationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/{scheduleId}/attendance")
public class ScheduleAttendanceController {
  private final ScheduleAttendanceService scheduleAttendanceService;
  private final UserValidationService userValidationService;

  // 일정 참석 여부 등록
  @PostMapping
  public ResponseEntity<ScheduleAttendanceResponseDto> createAttendance (
      @PathVariable Long scheduleId,
      @RequestBody @Valid ScheduleAttendanceRequestDto scheduleAttendanceRequestDto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);

    ScheduleAttendanceResponseDto scheduleAttendanceResponseDto =
        scheduleAttendanceService.createAttendance(
            scheduleId, currentUser.getId(), scheduleAttendanceRequestDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(scheduleAttendanceResponseDto);
  }

  // 일정 참석 여부 수정
  @PutMapping
  public ResponseEntity<ScheduleAttendanceResponseDto> updateAttendance (
      @PathVariable Long scheduleId,
      @RequestBody @Valid ScheduleAttendanceRequestDto scheduleAttendanceRequestDto,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);

    ScheduleAttendanceResponseDto scheduleAttendanceResponseDto =
        scheduleAttendanceService.updateAttendance(
            scheduleId, currentUser.getId(), scheduleAttendanceRequestDto);

    return ResponseEntity.ok(scheduleAttendanceResponseDto);
  }

  // 참석 여부 전체 조회(모임장)
  @GetMapping
  public ResponseEntity<List<ScheduleAttendanceResponseDto>> getAttendance(
      @PathVariable Long scheduleId,
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    User currentUser = userValidationService.validateUser(customUserDetails);

    List<ScheduleAttendanceResponseDto> scheduleAttendanceResponseDtoList =
        scheduleAttendanceService.getAllAttendance(scheduleId, currentUser.getId());

    return ResponseEntity.ok(scheduleAttendanceResponseDtoList);
  }
}
