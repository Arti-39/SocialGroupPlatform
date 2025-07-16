package com.zerobase.socialgroupplatform.dto;

import com.zerobase.socialgroupplatform.domain.GroupApplication;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupApplicationResponseDto {
  private Long applicationId;
  private Long groupId;
  private Long userId;
  private String nickname;
  private String status;
  private LocalDateTime appliedDate;

  public static GroupApplicationResponseDto fromEntity(GroupApplication groupApplication) {
    return GroupApplicationResponseDto.builder()
        .applicationId(groupApplication.getId())
        .groupId(groupApplication.getGroup().getId())
        .userId(groupApplication.getUser().getId())
        .nickname(groupApplication.getUser().getNickname())
        .status(groupApplication.getStatus().toString())
        .appliedDate(groupApplication.getAppliedDate())
        .build();
  }
}
