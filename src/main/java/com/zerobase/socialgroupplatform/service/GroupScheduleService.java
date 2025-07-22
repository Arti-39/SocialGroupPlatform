package com.zerobase.socialgroupplatform.service;

import com.zerobase.socialgroupplatform.domain.Group;
import com.zerobase.socialgroupplatform.domain.GroupSchedule;
import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.domain.common.ApplicationStatus;
import com.zerobase.socialgroupplatform.dto.ScheduleAttendanceResponseDto;
import com.zerobase.socialgroupplatform.dto.ScheduleCreateRequestDto;
import com.zerobase.socialgroupplatform.dto.ScheduleDetailResponseDto;
import com.zerobase.socialgroupplatform.dto.ScheduleResponseDto;
import com.zerobase.socialgroupplatform.exception.CustomException;
import com.zerobase.socialgroupplatform.exception.ErrorCode;
import com.zerobase.socialgroupplatform.repository.GroupApplicationRepository;
import com.zerobase.socialgroupplatform.repository.GroupRepository;
import com.zerobase.socialgroupplatform.repository.GroupScheduleRepository;
import com.zerobase.socialgroupplatform.repository.ScheduleAttendanceRepository;
import com.zerobase.socialgroupplatform.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupScheduleService {
  private final GroupScheduleRepository groupScheduleRepository;
  private final GroupRepository groupRepository;
  private final ScheduleAttendanceRepository scheduleAttendanceRepository;
  private final GroupApplicationRepository groupApplicationRepository;
  private final UserRepository userRepository;

  // 일정 등록(모임장)
  @Transactional
  public ScheduleResponseDto createSchedule(
      Long groupId, ScheduleCreateRequestDto scheduleCreateRequestDto, Long userId) {

    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GROUP));

    // 모임장만 일정 생성 가능
    if (!group.getGroupOwner().getId().equals(userId)) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    GroupSchedule groupSchedule = GroupSchedule.builder()
        .group(group)
        .scheduleDate(scheduleCreateRequestDto.getScheduleDate())
        .location(scheduleCreateRequestDto.getLocation())
        .description(scheduleCreateRequestDto.getDescription())
        .createdDate(LocalDateTime.now())
        .build();

    return new ScheduleResponseDto(groupScheduleRepository.save(groupSchedule));
  }

  // 일정 수정(모임장)
  @Transactional
  public ScheduleResponseDto updateSchedule(Long groupId, Long scheduleId, Long userId,
      ScheduleCreateRequestDto scheduleCreateRequestDto) {

    GroupSchedule groupSchedule = groupScheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));

    if (!groupSchedule.getGroup().getId().equals(groupId)) {
      throw new CustomException(ErrorCode.INVALID_GROUP_SCHEDULE);
    }
    if (!groupSchedule.getGroup().getGroupOwner().getId().equals(userId)) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    groupSchedule.setScheduleDate(scheduleCreateRequestDto.getScheduleDate());
    groupSchedule.setLocation(scheduleCreateRequestDto.getLocation());
    groupSchedule.setDescription(scheduleCreateRequestDto.getDescription());
    groupSchedule.setUpdatedDate(LocalDateTime.now());

    return new ScheduleResponseDto(groupSchedule);
  }

  // 일정 삭제(모임장)
  @Transactional
  public void deleteSchedule(Long groupId, Long scheduleId, Long userId) {
    GroupSchedule groupSchedule = groupScheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));

    // 유효성 검증
    if (!groupSchedule.getGroup().getId().equals(groupId)) {
      throw new CustomException(ErrorCode.INVALID_GROUP_SCHEDULE);
    }
    if (!groupSchedule.getGroup().getGroupOwner().getId().equals(userId)) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    groupScheduleRepository.deleteById(scheduleId);
  }

  // 일정 목록 조회
  @Transactional(readOnly = true)
  public List<ScheduleResponseDto> getSchedule(Long groupId, Long userId) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GROUP));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    if (!groupApplicationRepository.existsByGroupIdAndUserIdAndStatus(
        groupId, userId, ApplicationStatus.ACCEPTED) &&
        !group.getGroupOwner().getId().equals(user.getId())) {

      throw new CustomException(ErrorCode.NOT_IN_GROUP);
    }

    List<GroupSchedule> groupSchedules = groupScheduleRepository.findByGroupId(groupId);

    return groupSchedules.stream().map(ScheduleResponseDto::new).toList();
  }

  // 단일 일정 상세 조회
  @Transactional(readOnly = true)
  public ScheduleDetailResponseDto getScheduleDetail(Long groupId, Long scheduleId, Long userId) {
    GroupSchedule groupSchedule =  groupScheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));

    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GROUP));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    if (!groupApplicationRepository.existsByGroupIdAndUserIdAndStatus(
        groupId, userId, ApplicationStatus.ACCEPTED) &&
        !group.getGroupOwner().getId().equals(user.getId())) {

      throw new CustomException(ErrorCode.NOT_IN_GROUP);
    }

    List<ScheduleAttendanceResponseDto> scheduleAttendanceResponseDto =
        scheduleAttendanceRepository.findByGroupSchedule_Id(groupSchedule.getId())
            .stream()
            .map(ScheduleAttendanceResponseDto::new)
            .toList();

    return new ScheduleDetailResponseDto(groupSchedule, scheduleAttendanceResponseDto);
  }
}
