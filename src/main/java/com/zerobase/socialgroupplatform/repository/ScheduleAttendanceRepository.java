package com.zerobase.socialgroupplatform.repository;

import com.zerobase.socialgroupplatform.domain.ScheduleAttendance;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleAttendanceRepository extends JpaRepository<ScheduleAttendance, Long> {
  List<ScheduleAttendance> findByGroupSchedule_Id(Long scheduleId);
}
