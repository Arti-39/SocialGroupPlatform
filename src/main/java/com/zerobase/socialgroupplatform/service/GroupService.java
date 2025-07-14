package com.zerobase.socialgroupplatform.service;

import com.zerobase.socialgroupplatform.domain.Category;
import com.zerobase.socialgroupplatform.domain.Group;
import com.zerobase.socialgroupplatform.domain.Location;
import com.zerobase.socialgroupplatform.domain.TimeSlot;
import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.dto.GroupCreateRequestDto;
import com.zerobase.socialgroupplatform.dto.GroupResponseDto;
import com.zerobase.socialgroupplatform.exception.CustomException;
import com.zerobase.socialgroupplatform.exception.ErrorCode;
import com.zerobase.socialgroupplatform.repository.CategoryRepository;
import com.zerobase.socialgroupplatform.repository.GroupRepository;
import com.zerobase.socialgroupplatform.repository.LocationRepository;
import com.zerobase.socialgroupplatform.repository.TimeSlotRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {
  private final GroupRepository groupRepository;
  private final CategoryRepository categoryRepository;
  private final LocationRepository locationRepository;
  private final TimeSlotRepository timeSlotRepository;

  // 모임 생성
  public GroupResponseDto createGroup(GroupCreateRequestDto groupCreateRequestDto,
                                        User groupOwner) {

    // 유효성 검사
    Category category = categoryRepository.findById(groupCreateRequestDto.getCategoryId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));
    if (!category.isActive()) {
      throw new CustomException(ErrorCode.INACTIVE_CATEGORY);
    }

    Location location = locationRepository.findById(groupCreateRequestDto.getLocationId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_LOCATION));
    if (!location.isActive()) {
      throw new CustomException(ErrorCode.INACTIVE_LOCATION);
    }

    TimeSlot timeSlot = timeSlotRepository.findById(groupCreateRequestDto.getTimeSlotId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TIMESLOT));
    if (!timeSlot.isActive()) {
      throw new CustomException(ErrorCode.INACTIVE_TIMESLOT);
    }

    // Location 테이블에서 Point 값 복사
    // 참조하지 않고 복사하는 이유는 이후 검색 기능에서 더 빠른 연산을 위해
    String locationPoint = location.getPoint();

    Group group = Group.builder()
        .groupOwner(groupOwner)
        .title(groupCreateRequestDto.getTitle())
        .description(groupCreateRequestDto.getDescription())
        .category(category)
        .location(location)
        .timeSlot(timeSlot)
        .maxMembers(groupCreateRequestDto.getMaxMembers())
        .isPublic(groupCreateRequestDto.isPublic())
        .autoAccept(groupCreateRequestDto.isAutoAccept())
        .locationPoint(locationPoint)
        .createdDate(LocalDateTime.now())
        .build();

    return new GroupResponseDto(groupRepository.save(group));
  }
}
