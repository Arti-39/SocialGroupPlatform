package com.zerobase.socialgroupplatform.repository;

import com.zerobase.socialgroupplatform.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

}
