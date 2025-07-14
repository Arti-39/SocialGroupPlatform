package com.zerobase.socialgroupplatform.repository;

import com.zerobase.socialgroupplatform.domain.TimeSlot;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
}
