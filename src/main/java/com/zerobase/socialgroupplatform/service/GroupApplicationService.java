package com.zerobase.socialgroupplatform.service;

import com.zerobase.socialgroupplatform.domain.Group;
import com.zerobase.socialgroupplatform.domain.GroupApplication;
import com.zerobase.socialgroupplatform.domain.User;
import com.zerobase.socialgroupplatform.domain.common.ApplicationStatus;
import com.zerobase.socialgroupplatform.dto.GroupApplicationResponseDto;
import com.zerobase.socialgroupplatform.exception.CustomException;
import com.zerobase.socialgroupplatform.exception.ErrorCode;
import com.zerobase.socialgroupplatform.repository.GroupApplicationRepository;
import com.zerobase.socialgroupplatform.repository.GroupRepository;
import com.zerobase.socialgroupplatform.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupApplicationService {
  private final GroupRepository groupRepository;
  private final GroupApplicationRepository groupApplicationRepository;
  private final UserRepository userRepository;

  // 모임 참가 신청
  public GroupApplicationResponseDto applyToGroup(Long groupId, Long userId) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GROUP));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    // 중복 신청 방지
    boolean applied = groupApplicationRepository.existsByGroupIdAndUserIdAndStatusIn(
        groupId, userId, List.of(ApplicationStatus.PENDING, ApplicationStatus.ACCEPTED));

    if (applied) {
      throw new CustomException(ErrorCode.ALREADY_APPLIED);
    }

    // 모임장 본인일 경우
    if (group.getGroupOwner().equals(user)) {
      throw new CustomException(ErrorCode.ALREADY_APPLIED);
    }

    // 모임 공개 여부 확인
    if (!group.isPublic()) {
      throw new CustomException(ErrorCode.GROUP_IS_PRIVATE);
    }

    // 인원 초과 확인
    if (group.getCurrentMembers() >= group.getMaxMembers()) {
      throw new CustomException(ErrorCode.GROUP_IS_FULL);
    }

    // 자동 승인 여부 확인
    ApplicationStatus status =
        group.isAutoAccept() ? ApplicationStatus.ACCEPTED : ApplicationStatus.PENDING;

    if (status.equals(ApplicationStatus.ACCEPTED)) {
      // 현재 인원 증가
      group.setCurrentMembers(group.getCurrentMembers() + 1);
      groupRepository.save(group);
    }

    GroupApplication groupApplication = GroupApplication.builder()
                                                        .user(user)
                                                        .group(group)
                                                        .status(status)
                                                        .appliedDate(LocalDateTime.now())
                                                        .build();

    return new GroupApplicationResponseDto(groupApplicationRepository.save(groupApplication));
  }

  // 신청 승인/거절(모임장)
  public GroupApplicationResponseDto updateApplicationStatus(Long applicationId, Long groupOwnerId,
      ApplicationStatus applicationStatus) {

    GroupApplication groupApplication = groupApplicationRepository.findById(applicationId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPLICATION));

    if (!groupApplication.getGroup().getGroupOwner().getId().equals(groupOwnerId)) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    if (!groupApplication.getStatus().equals(ApplicationStatus.PENDING)) {
      throw new CustomException(ErrorCode.STATUS_IS_NOT_PENDING);
    }

    // 승인 시
    if (applicationStatus.equals(ApplicationStatus.ACCEPTED)) {
      Group group = groupApplication.getGroup();

      // 현재 인원 확인
      if (group.getCurrentMembers() >= group.getMaxMembers()) {
        throw new CustomException(ErrorCode.GROUP_IS_FULL);
      }

      // 현재 인원 증가
      group.setCurrentMembers(group.getCurrentMembers() + 1);
    }

    groupApplication.setStatus(applicationStatus);

    return new GroupApplicationResponseDto(groupApplication);
  }

  // 신청 취소(사용자)
  public GroupApplicationResponseDto cancelApplication(Long applicationId, Long userId) {
    GroupApplication groupApplication = groupApplicationRepository.findById(applicationId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPLICATION));

    if (!groupApplication.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    if (!groupApplication.getStatus().equals(ApplicationStatus.PENDING)) {
      throw new CustomException(ErrorCode.CANNOT_CANCEL);
    }

    groupApplication.setStatus(ApplicationStatus.CANCELED);

    return new GroupApplicationResponseDto(groupApplication);
  }

  // 신청 내역 조회(사용자)
  public List<GroupApplicationResponseDto> getApplicationByUser(Long userId) {
    List<GroupApplication> groupApplicationList = groupApplicationRepository.findByUserId(userId);

    return groupApplicationList.stream()
        .map(GroupApplicationResponseDto::new)
        .collect(Collectors.toList());
  }

  // 신청 내역 조회(모임장)
  public List<GroupApplicationResponseDto> getApplicationByGroup(Long groupId, Long groupOwnerId) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GROUP));

    if (!group.getGroupOwner().getId().equals(groupOwnerId)) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    List<GroupApplication> groupApplicationList = groupApplicationRepository.findByGroupId(groupId);

    return groupApplicationList.stream()
        .map(GroupApplicationResponseDto::new)
        .collect(Collectors.toList());
  }

  // 모임 탈퇴
  public GroupApplicationResponseDto withdrawFromGroup(Long groupId, Long userId) {
    GroupApplication groupApplication = groupApplicationRepository
        .findByGroupIdAndUserIdAndStatus(groupId, userId, ApplicationStatus.ACCEPTED)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_IN_GROUP));

    Group group = groupApplication.getGroup();

    group.setCurrentMembers(group.getCurrentMembers() - 1);
    groupApplication.setStatus(ApplicationStatus.WITHDRAWN);

    return new GroupApplicationResponseDto(groupApplication);
  }
}
