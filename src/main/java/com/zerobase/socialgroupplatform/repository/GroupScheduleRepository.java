package com.zerobase.socialgroupplatform.repository;

import com.zerobase.socialgroupplatform.domain.GroupSchedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, Long> {
  List<GroupSchedule> findByGroupId(Long groupId);
}
