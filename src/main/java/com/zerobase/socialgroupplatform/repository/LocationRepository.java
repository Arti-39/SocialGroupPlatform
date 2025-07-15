package com.zerobase.socialgroupplatform.repository;

import com.zerobase.socialgroupplatform.domain.Location;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
