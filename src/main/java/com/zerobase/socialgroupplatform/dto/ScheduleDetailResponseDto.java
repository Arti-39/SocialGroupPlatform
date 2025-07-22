package com.zerobase.socialgroupplatform.dto;

import com.zerobase.socialgroupplatform.domain.GroupSchedule;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class ScheduleDetailResponseDto {
  private Long scheduleId;
  private Long groupId;
  private LocalDateTime scheduleDate;
  private String location;
  private String description;
  private List<ScheduleAttendanceResponseDto> attendanceList;
  private LocalDateTime createdDate;

  public ScheduleDetailResponseDto(GroupSchedule groupSchedule,
      List<ScheduleAttendanceResponseDto> scheduleAttendanceResponseDto) {

    this.scheduleId = groupSchedule.getId();
    this.groupId = groupSchedule.getGroup().getId();
    this.scheduleDate = groupSchedule.getScheduleDate();
    this.location = groupSchedule.getLocation();
    this.description = groupSchedule.getDescription();
    this.attendanceList = scheduleAttendanceResponseDto;
    this.createdDate = groupSchedule.getCreatedDate();
  }
}
