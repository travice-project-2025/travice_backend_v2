package com.gmg.travice.domain.plan.service;

import com.gmg.travice.domain.plan.dto.PlanDTO;
import com.gmg.travice.domain.plan.dto.PlanListResponseDTO;
import com.gmg.travice.domain.plan.entity.Plan;
import com.gmg.travice.domain.user.entity.User;

import java.util.List;

public interface PlanService {

    List<PlanListResponseDTO> findAllByUser(User user);

    Plan findByPlanId(Long planId);

    PlanDTO getPlanWithDetails(Long planId);

    PlanDTO createPlan(PlanDTO planDTO, User user);
    PlanDTO updatePlan(Long planId, PlanDTO planDTO, Long userId);
    void deletePlanById(Long planId);

}
