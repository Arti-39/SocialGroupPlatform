package com.zerobase.socialgroupplatform.service;

import com.zerobase.socialgroupplatform.domain.Category;
import com.zerobase.socialgroupplatform.domain.Group;
import com.zerobase.socialgroupplatform.domain.Location;
import com.zerobase.socialgroupplatform.domain.TimeSlot;
import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.dto.GroupCreateRequestDto;
import com.zerobase.socialgroupplatform.dto.GroupResponseDto;
import com.zerobase.socialgroupplatform.dto.GroupUpdateRequestDto;
import com.zerobase.socialgroupplatform.exception.CustomException;
import com.zerobase.socialgroupplatform.exception.ErrorCode;
import com.zerobase.socialgroupplatform.repository.CategoryRepository;
import com.zerobase.socialgroupplatform.repository.GroupRepository;
import com.zerobase.socialgroupplatform.repository.LocationRepository;
import com.zerobase.socialgroupplatform.repository.TimeSlotRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {
  private final GroupRepository groupRepository;
  private final CategoryRepository categoryRepository;
  private final LocationRepository locationRepository;
  private final TimeSlotRepository timeSlotRepository;

  // 모임 생성
  @Transactional
  public GroupResponseDto createGroup(GroupCreateRequestDto groupCreateRequestDto,
                                        User groupOwner) {

    // 유효성 검사
    Category category = getCategory(groupCreateRequestDto.getCategoryId());
    Location location = getLocation(groupCreateRequestDto.getLocationId());
    TimeSlot timeSlot = getTimeSlot(groupCreateRequestDto.getTimeSlotId());

    Group group = Group.builder()
        .groupOwner(groupOwner)
        .title(groupCreateRequestDto.getTitle())
        .description(groupCreateRequestDto.getDescription())
        .category(category)
        .location(location)
        .timeSlot(timeSlot)
        .currentMembers(1)
        .maxMembers(groupCreateRequestDto.getMaxMembers())
        .isPublic(groupCreateRequestDto.isPublic())
        .autoAccept(groupCreateRequestDto.isAutoAccept())
        .createdDate(LocalDateTime.now())
        .build();

    return new GroupResponseDto(groupRepository.save(group));
  }

  // 모임 수정
  @Transactional
  public GroupResponseDto updateGroup(Long id, Long userId,
      GroupUpdateRequestDto groupUpdateRequestDto) {

    Group group = getGroup(id);
    validateGroupOwner(userId, group.getGroupOwner().getId());

    Category category = getCategory(groupUpdateRequestDto.getCategoryId());
    Location location = getLocation(groupUpdateRequestDto.getLocationId());
    TimeSlot timeSlot = getTimeSlot(groupUpdateRequestDto.getTimeSlotId());

    group.setTitle(groupUpdateRequestDto.getTitle());
    group.setDescription(groupUpdateRequestDto.getDescription());
    group.setCategory(category);
    group.setLocation(location);
    group.setTimeSlot(timeSlot);
    group.setMaxMembers(groupUpdateRequestDto.getMaxMembers());
    group.setPublic(groupUpdateRequestDto.isPublic());
    group.setAutoAccept(groupUpdateRequestDto.isAutoAccept());

    return new GroupResponseDto(groupRepository.save(group));
  }

  // 모임 삭제
  @Transactional
  public void deleteGroup(Long id, Long userId) {
    Group group = getGroup(id);
    validateGroupOwner(userId, group.getGroupOwner().getId());

    groupRepository.delete(group);
  }

  // 유효성 검사
  private Category getCategory(Long categoryId) {
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

    if (!category.isActive()) {
      throw new CustomException(ErrorCode.INACTIVE_CATEGORY);
    }

    return category;
  }

  private Location getLocation(Long locationId) {
    Location location = locationRepository.findById(locationId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_LOCATION));

    if (!location.isActive()) {
      throw new CustomException(ErrorCode.INACTIVE_LOCATION);
    }

    return location;
  }

  private TimeSlot getTimeSlot(Long timeSlotId) {
    TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TIMESLOT));

    if (!timeSlot.isActive()) {
      throw new CustomException(ErrorCode.INACTIVE_TIMESLOT);
    }

    return timeSlot;
  }

  private Group getGroup(Long groupId) {
    return groupRepository.findById(groupId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GROUP));
  }

  private void validateGroupOwner(Long userId, Long groupOwnerId) {
    if (!Objects.equals(userId, groupOwnerId)) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }
  }
}
