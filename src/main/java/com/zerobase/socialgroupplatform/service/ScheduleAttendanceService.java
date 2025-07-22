package com.zerobase.socialgroupplatform.service;

import com.zerobase.socialgroupplatform.domain.GroupSchedule;
import com.zerobase.socialgroupplatform.domain.ScheduleAttendance;
import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.domain.common.ApplicationStatus;
import com.zerobase.socialgroupplatform.dto.ScheduleAttendanceRequestDto;
import com.zerobase.socialgroupplatform.dto.ScheduleAttendanceResponseDto;
import com.zerobase.socialgroupplatform.exception.CustomException;
import com.zerobase.socialgroupplatform.exception.ErrorCode;
import com.zerobase.socialgroupplatform.repository.GroupApplicationRepository;
import com.zerobase.socialgroupplatform.repository.GroupScheduleRepository;
import com.zerobase.socialgroupplatform.repository.ScheduleAttendanceRepository;
import com.zerobase.socialgroupplatform.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleAttendanceService {
  private final ScheduleAttendanceRepository scheduleAttendanceRepository;
  private final GroupScheduleRepository groupScheduleRepository;
  private final UserRepository userRepository;
  private final GroupApplicationRepository groupApplicationRepository;

  // 일정 참석 여부 등록
  @Transactional
  public ScheduleAttendanceResponseDto createAttendance(
      Long scheduleId, Long userId, ScheduleAttendanceRequestDto scheduleAttendanceRequestDto) {

    GroupSchedule groupSchedule = groupScheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    if (!groupApplicationRepository.existsByGroupIdAndUserIdAndStatus(
        groupSchedule.getGroup().getId(), userId, ApplicationStatus.ACCEPTED) &&
        !groupSchedule.getGroup().getGroupOwner().getId().equals(user.getId())) {

      throw new CustomException(ErrorCode.NOT_IN_GROUP);
    }

    if (scheduleAttendanceRepository.existsByGroupSchedule_IdAndUser_Id(scheduleId, userId)) {
      throw new CustomException(ErrorCode.ALREADY_EXISTS_ATTENDANCE);
    }

    ScheduleAttendance scheduleAttendance = ScheduleAttendance.builder()
        .groupSchedule(groupSchedule)
        .user(user)
        .status(scheduleAttendanceRequestDto.getStatus())
        .build();

    return new ScheduleAttendanceResponseDto(scheduleAttendanceRepository.save(scheduleAttendance));
  }

  // 일정 참석 여부 수정
  @Transactional
  public ScheduleAttendanceResponseDto updateAttendance(
      Long scheduleId, Long userId, ScheduleAttendanceRequestDto scheduleAttendanceRequestDto) {

    ScheduleAttendance scheduleAttendance =
        scheduleAttendanceRepository.findByGroupSchedule_IdAndUser_Id(scheduleId, userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ATTENDANCE));

    scheduleAttendance.setStatus(scheduleAttendanceRequestDto.getStatus());

    return new ScheduleAttendanceResponseDto(scheduleAttendance);
  }

  // 참석 여부 전체 조회(모임장)
  @Transactional(readOnly = true)
  public List<ScheduleAttendanceResponseDto> getAllAttendance(Long scheduleId, Long userId) {
    GroupSchedule groupSchedule = groupScheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));

    // 모임장만 조회 가능
    if (!groupSchedule.getGroup().getGroupOwner().getId().equals(userId)) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    return scheduleAttendanceRepository.findByGroupSchedule_Id(groupSchedule.getId())
        .stream()
        .map(ScheduleAttendanceResponseDto::new)
        .toList();
  }
}
