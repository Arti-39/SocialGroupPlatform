package com.zerobase.socialgroupplatform.dto;

import com.zerobase.socialgroupplatform.domain.GroupSchedule;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public class ScheduleResponseDto {
  private Long scheduleId;
  private Long groupId;
  private LocalDateTime scheduleDate;
  private String location;
  private String description;
  private LocalDateTime createdDate;

  public static ScheduleResponseDto fromEntity(GroupSchedule groupSchedule) {
    return ScheduleResponseDto.builder()
        .scheduleId(groupSchedule.getId())
        .groupId(groupSchedule.getGroup().getId())
        .scheduleDate(groupSchedule.getScheduleDate())
        .location(groupSchedule.getLocation())
        .description(groupSchedule.getDescription())
        .createdDate(groupSchedule.getCreatedDate())
        .build();
  }
}
