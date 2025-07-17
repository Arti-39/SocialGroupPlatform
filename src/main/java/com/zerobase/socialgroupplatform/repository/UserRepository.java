package com.zerobase.socialgroupplatform.repository;

import com.zerobase.socialgroupplatform.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);
  boolean existsByNickname(String nickname);
  boolean existsByUserId(String userId);

  Optional<User> findByUserId(String userId);
}