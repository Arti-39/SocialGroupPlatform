package com.zerobase.socialgroupplatform.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ScheduleCreateRequestDto {
  @NotNull
  private LocalDateTime scheduleDate;

  @NotNull
  private String location;

  private String description;
}
