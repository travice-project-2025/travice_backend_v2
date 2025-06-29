package com.gmg.travice.domain.plan.repository;

import com.gmg.travice.domain.plan.entity.Plan;
import com.gmg.travice.domain.plan.entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanDetailRepository extends JpaRepository<PlanDetail, Long> {
    // plan 객체로 디테일 조회
    List<PlanDetail> findByPlanOrderByDayAscArrivalTimeDesc(Plan plan);

    // planID로 디테일 조회
    @Query("SELECT pd FROM PlanDetail pd WHERE pd.plan.id = :planId ORDER BY pd.day ASC, pd.arrivalTime ASC")
    List<PlanDetail> findByPlanIdOrderByDayAscArrivalTimeAsc(@Param("planId") Long planId);

    void deleteByPlanId(Long planId);
}

