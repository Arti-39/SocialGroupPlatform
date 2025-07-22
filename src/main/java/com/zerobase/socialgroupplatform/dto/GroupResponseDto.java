package com.zerobase.socialgroupplatform.dto;

import com.zerobase.socialgroupplatform.domain.Group;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GroupResponseDto {
  private Long id;
  private String title;
  private String description;
  private String category;
  private String location;
  private String timeSlot;
  private int maxMembers;
  private boolean isPublic;
  private boolean autoAccept;
  private String groupOwnerNickname;
  private LocalDateTime createdDate;

  public GroupResponseDto(Group group) {
    this.id = group.getId();
    this.title = group.getTitle();
    this.description = group.getDescription();
    this.category = group.getCategory().getName();
    this.location = group.getLocation().getName();
    this.timeSlot = group.getTimeSlot().getName();
    this.maxMembers = group.getMaxMembers();
    this.isPublic = group.isPublic();
    this.autoAccept = group.isAutoAccept();
    this.groupOwnerNickname = group.getGroupOwner().getNickname();
    this.createdDate = group.getCreatedDate();
  }
}
