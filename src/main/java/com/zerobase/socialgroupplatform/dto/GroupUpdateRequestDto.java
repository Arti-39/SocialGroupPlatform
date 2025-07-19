package com.zerobase.socialgroupplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupUpdateRequestDto {

  private String title;
  private String description;
  private Long categoryId;
  private Long locationId;
  private Long timeSlotId;

  @Min(2)
  private int maxMembers;

  @JsonProperty("isPublic")
  private boolean isPublic;
  private boolean autoAccept;
}
