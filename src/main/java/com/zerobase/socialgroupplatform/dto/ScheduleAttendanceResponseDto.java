package com.zerobase.socialgroupplatform.dto;

import com.zerobase.socialgroupplatform.domain.ScheduleAttendance;
import com.zerobase.socialgroupplatform.domain.common.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleAttendanceResponseDto {
  private Long scheduleId;
  private Long userId;
  private AttendanceStatus status;

  public ScheduleAttendanceResponseDto(ScheduleAttendance scheduleAttendance) {
     this.scheduleId = scheduleAttendance.getGroupSchedule().getId();
     this.userId = scheduleAttendance.getUser().getId();
     this.status = scheduleAttendance.getStatus();
  }
}
