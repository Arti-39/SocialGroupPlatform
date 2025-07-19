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

  public static ScheduleAttendanceResponseDto fromEntity(ScheduleAttendance scheduleAttendance) {
    return ScheduleAttendanceResponseDto.builder()
        .scheduleId(scheduleAttendance.getId())
        .userId(scheduleAttendance.getUser().getId())
        .status(scheduleAttendance.getStatus())
        .build();
  }
}
