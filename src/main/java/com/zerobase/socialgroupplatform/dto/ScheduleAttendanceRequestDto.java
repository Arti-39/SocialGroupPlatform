package com.zerobase.socialgroupplatform.dto;

import com.zerobase.socialgroupplatform.domain.common.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleAttendanceRequestDto {
  @NotNull
  private AttendanceStatus status;
}
