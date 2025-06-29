package com.gmg.travice.domain.user.repository;

import com.gmg.travice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByToken(String token);
}
