package com.zerobase.socialgroupplatform.dto;

import com.zerobase.socialgroupplatform.domain.GroupApplication;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GroupApplicationResponseDto {
  private Long applicationId;
  private Long groupId;
  private Long userId;
  private String nickname;
  private String status;
  private LocalDateTime appliedDate;

  public GroupApplicationResponseDto(GroupApplication groupApplication) {
    this.applicationId = groupApplication.getId();
    this.groupId = groupApplication.getGroup().getId();
    this.userId = groupApplication.getUser().getId();
    this.nickname = groupApplication.getUser().getNickname();
    this.status = groupApplication.getStatus().toString();
    this.appliedDate = groupApplication.getAppliedDate();
  }
}
