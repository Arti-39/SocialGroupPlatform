package com.zerobase.socialgroupplatform.repository;

import com.zerobase.socialgroupplatform.domain.GroupApplication;
import com.zerobase.socialgroupplatform.domain.common.ApplicationStatus;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupApplicationRepository extends JpaRepository<GroupApplication, Long> {

  boolean existsByGroupIdAndUserIdAndStatusIn(Long groupId, Long userId, Collection<ApplicationStatus> statuses);
  boolean existsByGroupIdAndUserIdAndStatus(Long groupId, Long userId, ApplicationStatus applicationStatus);

  List<GroupApplication> findByUserId(Long userId);
  List<GroupApplication> findByGroupId(Long groupId);

  Optional<GroupApplication> findByGroupIdAndUserIdAndStatus(Long groupId, Long userId, ApplicationStatus applicationStatus);
}
