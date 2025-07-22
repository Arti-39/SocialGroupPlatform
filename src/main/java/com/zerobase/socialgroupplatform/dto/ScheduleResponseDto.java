package com.zerobase.socialgroupplatform.dto;

import com.zerobase.socialgroupplatform.domain.GroupSchedule;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {
  private Long scheduleId;
  private Long groupId;
  private LocalDateTime scheduleDate;
  private String location;
  private String description;
  private LocalDateTime createdDate;

  public ScheduleResponseDto(GroupSchedule groupSchedule) {
    this.scheduleId = groupSchedule.getId();
    this.groupId = groupSchedule.getGroup().getId();
    this.scheduleDate = groupSchedule.getScheduleDate();
    this.location = groupSchedule.getLocation();
    this.description = groupSchedule.getDescription();
    this.createdDate = groupSchedule.getCreatedDate();
  }
}
