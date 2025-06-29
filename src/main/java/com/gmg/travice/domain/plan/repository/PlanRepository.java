package com.gmg.travice.domain.plan.repository;

import com.gmg.travice.domain.plan.entity.Plan;
import com.gmg.travice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findAllByUserId(Long userId);

    List<Plan> findAllByUser(User user);

    // 삭제되지 않은 플랜만 조회
    List<Plan> findAllByUserAndIsDeletedFalse(User user);
}
