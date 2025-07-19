package com.zerobase.socialgroupplatform.dto;

import com.zerobase.socialgroupplatform.domain.GroupSchedule;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public class ScheduleDetailResponseDto {
  private Long scheduleId;
  private Long groupId;
  private LocalDateTime scheduleDate;
  private String location;
  private String description;
  private List<ScheduleAttendanceResponseDto> attendanceList;
  private LocalDateTime createdDate;

  public static ScheduleDetailResponseDto of(GroupSchedule groupSchedule,
      List<ScheduleAttendanceResponseDto> scheduleAttendanceResponseDto) {

    return ScheduleDetailResponseDto.builder()
        .scheduleId(groupSchedule.getId())
        .groupId(groupSchedule.getGroup().getId())
        .scheduleDate(groupSchedule.getScheduleDate())
        .location(groupSchedule.getLocation())
        .description(groupSchedule.getDescription())
        .attendanceList(scheduleAttendanceResponseDto)
        .createdDate(groupSchedule.getCreatedDate())
        .build();
  }
}
