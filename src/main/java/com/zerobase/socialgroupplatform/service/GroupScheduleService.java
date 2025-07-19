package com.zerobase.socialgroupplatform.service;

import com.zerobase.socialgroupplatform.domain.Group;
import com.zerobase.socialgroupplatform.domain.GroupSchedule;
import com.zerobase.socialgroupplatform.dto.ScheduleAttendanceResponseDto;
import com.zerobase.socialgroupplatform.dto.ScheduleCreateRequestDto;
import com.zerobase.socialgroupplatform.dto.ScheduleDetailResponseDto;
import com.zerobase.socialgroupplatform.dto.ScheduleResponseDto;
import com.zerobase.socialgroupplatform.exception.CustomException;
import com.zerobase.socialgroupplatform.exception.ErrorCode;
import com.zerobase.socialgroupplatform.repository.GroupRepository;
import com.zerobase.socialgroupplatform.repository.GroupScheduleRepository;
import com.zerobase.socialgroupplatform.repository.ScheduleAttendanceRepository;
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
        .build();

    groupScheduleRepository.save(groupSchedule);

    return ScheduleResponseDto.fromEntity(groupSchedule);
  }

  // 일정 목록 조회
  @Transactional(readOnly = true)
  public List<ScheduleResponseDto> getSchedule(Long groupId) {
    List<GroupSchedule> groupSchedules = groupScheduleRepository.findByGroupId(groupId);

    return groupSchedules.stream().map(ScheduleResponseDto::fromEntity).toList();
  }

  // 단일 일정 상세 조회
  @Transactional(readOnly = true)
  public ScheduleDetailResponseDto getScheduleDetail(Long groupId, Long scheduleId) {
    GroupSchedule groupSchedule =  groupScheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));

    List<ScheduleAttendanceResponseDto> scheduleAttendanceResponseDto =
        scheduleAttendanceRepository.findByGroupSchedule_Id(groupSchedule.getId())
            .stream().map(ScheduleAttendanceResponseDto::fromEntity)
            .toList();

    return ScheduleDetailResponseDto.of(groupSchedule, scheduleAttendanceResponseDto);
  }
}
