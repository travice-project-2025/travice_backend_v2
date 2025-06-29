package com.gmg.travice.domain.plan.service;

import com.gmg.travice.domain.plan.entity.City;
import com.gmg.travice.domain.plan.dto.PlanDTO;
import com.gmg.travice.domain.plan.dto.PlanDetailDTO;
import com.gmg.travice.domain.plan.dto.PlanListResponseDTO;
import com.gmg.travice.domain.plan.entity.Plan;
import com.gmg.travice.domain.plan.entity.PlanDetail;
import com.gmg.travice.domain.plan.entity.Transport;
import com.gmg.travice.domain.plan.repository.CityRepository;
import com.gmg.travice.domain.plan.repository.PlanDetailRepository;
import com.gmg.travice.domain.plan.repository.PlanRepository;
import com.gmg.travice.domain.plan.repository.TransportRepository;
import com.gmg.travice.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanDetailRepository planDetailRepository;
    private final CityRepository cityRepository;
    private final TransportRepository transportRepository;

    public PlanServiceImpl(PlanRepository planRepository, PlanDetailRepository planDetailRepository, CityRepository cityRepository, TransportRepository transportRepository) {
        this.planRepository = planRepository;
        this.planDetailRepository = planDetailRepository;
        this.cityRepository = cityRepository;
        this.transportRepository = transportRepository;
    }

    @Override
    public List<PlanListResponseDTO> findAllByUser(User user) {
        List<Plan> plans = planRepository.findAllByUser(user);
        List<PlanListResponseDTO> planListDTOList = new ArrayList<>();

        for (Plan plan : plans) {
            PlanListResponseDTO dto = new PlanListResponseDTO();
            dto.setId(plan.getId());
            dto.setTitle(plan.getTitle());
            dto.setStartDate(plan.getStartDate());
            dto.setEndDate(plan.getEndDate());
            dto.setMemberCount(plan.getMemberCount());
            dto.setCity(plan.getCity());
            dto.setPublic(plan.isPublic());
            dto.setThumbnail(plan.getThumbnail());
            planListDTOList.add(dto);
        }
        return planListDTOList;
    }

    @Override
    public Plan findByPlanId(Long planId) {
        return planRepository.findById(planId).orElse(null);
    }

    @Override
    public PlanDTO getPlanWithDetails(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("플랜이 없습니다: " + planId));
        PlanDTO dto = new PlanDTO();
        dto.setId(plan.getId());
        dto.setTitle(plan.getTitle());
        dto.setStartDate(plan.getStartDate());
        dto.setEndDate(plan.getEndDate());
        dto.setUserId(plan.getUser().getId());

        List<PlanDetail> details = planDetailRepository.findByPlanIdOrderByDayAscArrivalTimeAsc(planId);
        List<PlanDetailDTO> planDetailDTOList = new ArrayList<>();
        for (PlanDetail detail : details) {
            PlanDetailDTO planDetailDTO = new PlanDetailDTO();
            planDetailDTO.setId(detail.getId());
            planDetailDTO.setPlanDetailName(detail.getPlanDetailName());
            planDetailDTO.setDay(detail.getDay());
            planDetailDTO.setArrivalTime(detail.getArrivalTime());
            planDetailDTO.setDepartureTime(detail.getDepartureTime());
            planDetailDTO.setTransportFromPrevious(detail.getTransportFromPrevious());
            planDetailDTO.setMemo(detail.getMemo());
            planDetailDTO.setAddress(detail.getAddress());
            planDetailDTO.setLatitude(detail.getLatitude());
            planDetailDTO.setLongitude(detail.getLongitude());
            planDetailDTOList.add(planDetailDTO);
        }
        dto.setDetails(planDetailDTOList);
        return dto;
    }

    @Override
    @Transactional
    public PlanDTO createPlan(PlanDTO planDTO, User user) {
        Plan plan = new Plan();
        plan.setTitle(planDTO.getTitle());
        plan.setStartDate(planDTO.getStartDate());
        plan.setEndDate(planDTO.getEndDate());
        plan.setUser(user);

        // memberCount 설정 추가
        plan.setMemberCount(planDTO.getMemberCount());
        plan.setPublic(planDTO.getIsPublic() != null ? planDTO.getIsPublic() : false);
        plan.setThumbnail(planDTO.getThumbnail());

        // City 설정 - region 문자열로 찾기
        if (planDTO.getRegion() != null && !planDTO.getRegion().trim().isEmpty()) {
            City city = cityRepository.findByCityNameOrContaining(planDTO.getRegion())
                    .orElseGet(() -> {
                        // 기본값으로 ID가 1인 City 반환 (제주도 등)
                        return cityRepository.findById(1L).orElse(null);
                    });
            plan.setCity(city);
        } else {
            // region이 없으면 기본 City 설정
            City defaultCity = cityRepository.findById(1L).orElse(null);
            plan.setCity(defaultCity);
        }

        // Plan 먼저 저장
        Plan savedPlan = planRepository.save(plan);

        // PlanDetail 리스트 생성 및 저장
        List<PlanDetail> planDetails = new ArrayList<>();
        if (planDTO.getDetails() != null) {
            for (PlanDetailDTO detailDTO : planDTO.getDetails()) {
                PlanDetail planDetail = new PlanDetail();

                // DTO에서 Entity로 데이터 복사 (기존 오류 수정)
                planDetail.setPlanDetailName(detailDTO.getPlanDetailName());
                planDetail.setDay(detailDTO.getDay());
                planDetail.setArrivalTime(detailDTO.getArrivalTime());
                planDetail.setDepartureTime(detailDTO.getDepartureTime());
                planDetail.setMemo(detailDTO.getMemo());
                planDetail.setLatitude(detailDTO.getLatitude());
                planDetail.setLongitude(detailDTO.getLongitude());
                planDetail.setAddress(detailDTO.getAddress());

                // Plan 연결
                planDetail.setPlan(savedPlan);

                // Transport 설정
                if (detailDTO.getTransportFromPrevious() != null &&
                        detailDTO.getTransportFromPrevious().getId() != null) {
                    Transport transport = transportRepository.findById(detailDTO.getTransportFromPrevious().getId())
                            .orElse(null);
                    planDetail.setTransportFromPrevious(transport);
                }

                planDetails.add(planDetail);
            }

            // PlanDetail 일괄 저장
            planDetailRepository.saveAll(planDetails);
        }

        // 응답 DTO 생성
        PlanDTO result = new PlanDTO();
        result.setId(savedPlan.getId());
        result.setTitle(savedPlan.getTitle());
        result.setStartDate(savedPlan.getStartDate());
        result.setEndDate(savedPlan.getEndDate());
        result.setUserId(user.getId());
        result.setMemberCount(savedPlan.getMemberCount());
        result.setIsPublic(savedPlan.isPublic());
        result.setThumbnail(savedPlan.getThumbnail());

        // PlanDetail DTO 리스트 생성
        List<PlanDetailDTO> resultDetails = new ArrayList<>();
        for (PlanDetail detail : planDetails) {
            PlanDetailDTO detailDTO = new PlanDetailDTO();
            detailDTO.setId(detail.getId());
            detailDTO.setPlanDetailName(detail.getPlanDetailName());
            detailDTO.setDay(detail.getDay());
            detailDTO.setArrivalTime(detail.getArrivalTime());
            detailDTO.setDepartureTime(detail.getDepartureTime());
            detailDTO.setMemo(detail.getMemo());
            detailDTO.setLatitude(detail.getLatitude());
            detailDTO.setLongitude(detail.getLongitude());
            detailDTO.setAddress(detail.getAddress());
            detailDTO.setTransportFromPrevious(detail.getTransportFromPrevious());
            resultDetails.add(detailDTO);
        }
        result.setDetails(resultDetails);

        return result;
    }

    @Override
    @Transactional
    public PlanDTO updatePlan(Long planId, PlanDTO planDTO, Long userId) {
        // DB에서 Plan 엔티티 조회
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new EntityNotFoundException("플랜이 없습니다: " + planId));
        // 요청한 유저와 플랜을 쓴 유저랑 다르면 권한 오류
        if (!plan.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다: userId=" + userId);
        }

        plan.setTitle(planDTO.getTitle());
        plan.setStartDate(planDTO.getStartDate());
        plan.setEndDate(planDTO.getEndDate());
        plan.setMemberCount(planDTO.getMemberCount());
        plan.setPublic(planDTO.getIsPublic() != null ? planDTO.getIsPublic() : false);
        plan.setThumbnail(planDTO.getThumbnail());

        if (planDTO.getRegion() != null && !planDTO.getRegion().trim().isEmpty()) {
            City city = cityRepository.findByCityNameOrContaining(planDTO.getRegion())
                    .orElseGet(() -> cityRepository.findById(1L).orElse(null));
            plan.setCity(city);
        }

        Plan updatedPlan = planRepository.save(plan);

        // 기존 plandetail 삭제 -> 이것 좀 고민해봐야함
        planDetailRepository.deleteByPlanId(plan.getId());

        // 새로운 plandetail 생성하고 저장
        List<PlanDetail> resultDetails = new ArrayList<>();
        if (planDTO.getDetails() != null) {
            for (PlanDetailDTO detailDTO : planDTO.getDetails()) {
                PlanDetail planDetail = new PlanDetail();

                planDetail.setPlanDetailName(detailDTO.getPlanDetailName());
                planDetail.setDay(detailDTO.getDay());
                planDetail.setArrivalTime(detailDTO.getArrivalTime());
                planDetail.setDepartureTime(detailDTO.getDepartureTime());
                planDetail.setMemo(detailDTO.getMemo());
                planDetail.setLatitude(detailDTO.getLatitude());
                planDetail.setLongitude(detailDTO.getLongitude());
                planDetail.setAddress(detailDTO.getAddress());
                planDetail.setPlan(updatedPlan);

                if (detailDTO.getTransportFromPrevious() != null && detailDTO.getTransportFromPrevious().getId() != null) {
                    transportRepository.findById(detailDTO.getTransportFromPrevious().getId())
                            .ifPresent(planDetail::setTransportFromPrevious);
                }
                resultDetails.add(planDetail);
            }
            resultDetails = planDetailRepository.saveAll(resultDetails);
        }

        // response 하기 위한 DTO
        PlanDTO result = new PlanDTO();
        result.setId(plan.getId());
        result.setUserId(userId);
        result.setTitle(planDTO.getTitle());
        result.setStartDate(planDTO.getStartDate());
        result.setEndDate(planDTO.getEndDate());
        result.setMemberCount(planDTO.getMemberCount());
        result.setIsPublic(planDTO.getIsPublic());
        result.setThumbnail(planDTO.getThumbnail());
        if (updatedPlan.getCity() != null) {
            result.setRegion(updatedPlan.getCity().getCityName());
        }

        List<PlanDetailDTO> detailDTOs = new ArrayList<>();
        for(PlanDetail detail : resultDetails) {
            PlanDetailDTO detailDTO = new PlanDetailDTO();
            detailDTO.setId(detail.getId());
            detailDTO.setPlanDetailName(detail.getPlanDetailName());
            detailDTO.setDay(detail.getDay());
            detailDTO.setArrivalTime(detail.getArrivalTime());
            detailDTO.setDepartureTime(detail.getDepartureTime());
            detailDTO.setMemo(detail.getMemo());
            detailDTO.setLatitude(detail.getLatitude());
            detailDTO.setLongitude(detail.getLongitude());
            detailDTO.setAddress(detail.getAddress());
            detailDTO.setTransportFromPrevious(detail.getTransportFromPrevious());
            detailDTOs.add(detailDTO);
        }
        result.setDetails(detailDTOs);

        return result;
    }

    @Override
    public void deletePlanById(Long planId) {

    }
}
