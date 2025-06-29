package com.gmg.travice.domain.plan.controller;

import com.gmg.travice.domain.plan.dto.PlanDTO;
import com.gmg.travice.domain.plan.dto.PlanDetailDTO;
import com.gmg.travice.domain.plan.dto.PlanListResponseDTO;
import com.gmg.travice.domain.plan.service.PlanService;
import com.gmg.travice.domain.user.entity.User;
import com.gmg.travice.domain.user.service.UserService;
import com.gmg.travice.feature.login.jwt.JWTUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final UserService userService;
    private final JWTUtil jwtUtil; // 생성자 주입으로 변경

    @GetMapping()
    public ResponseEntity<?> getPlansByUser(HttpServletRequest request) {
        User me = userService.findUserByJWTToken(request);

        if(me!=null) {
            List<PlanListResponseDTO> result = planService.findAllByUser(me);

            List<Map<String, Object>> simplifiedResult = new ArrayList<>();

            for (PlanListResponseDTO dto : result) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", dto.getId());
                item.put("title", dto.getTitle());
                item.put("startDate", dto.getStartDate().toString());
                item.put("endDate", dto.getEndDate().toString());
                item.put("memberCount", dto.getMemberCount());
                item.put("cityName", dto.getCity().getId());
                item.put("isPublic", dto.isPublic());
                item.put("thumbnail", dto.getThumbnail());
                simplifiedResult.add(item);
            }

            return ResponseEntity.ok(simplifiedResult);

        }

       return ResponseEntity.badRequest().body("JWT token is expired");

    }

    @PostMapping
    public ResponseEntity<?> createPlan(@RequestBody PlanDTO planDTO, HttpServletRequest request) {
        try {

            User user = userService.findUserByJWTToken(request);

            if (user == null) {
                log.warn("Invalid or expired JWT token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("JWT token is expired or invalid");
            }

            log.info("Creating plan for user: {} with title: {}", user.getId(), planDTO.getTitle());

            PlanDTO createdPlan = planService.createPlan(planDTO, user);

            Map<String, Object> response = new HashMap<>();
            response.put("id", createdPlan.getId());
            response.put("title", createdPlan.getTitle());
            response.put("startDate", createdPlan.getStartDate().toString());
            response.put("endDate", createdPlan.getEndDate().toString());
            response.put("userId", createdPlan.getUserId());

            if (createdPlan.getDetails() != null && !createdPlan.getDetails().isEmpty()) {
                List<Map<String, Object>> detailsList = new ArrayList<>();
                for (PlanDetailDTO detail : createdPlan.getDetails()) {
                    Map<String, Object> detailMap = new HashMap<>();
                    detailMap.put("id", detail.getId());
                    detailMap.put("day", detail.getDay());
                    detailMap.put("arrivalTime", detail.getArrivalTime() != null ? detail.getArrivalTime().toString() : null);
                    detailMap.put("departureTime", detail.getDepartureTime() != null ? detail.getDepartureTime().toString() : null);
                    detailMap.put("planDetailName", detail.getPlanDetailName());
                    detailMap.put("memo", detail.getMemo());
                    detailMap.put("latitude", detail.getLatitude());
                    detailMap.put("longitude", detail.getLongitude());
                    detailMap.put("address", detail.getAddress());
                    detailsList.add(detailMap);
                }
                response.put("details", detailsList);
            }

            log.info("Plan created successfully with ID: {}", createdPlan.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (EntityNotFoundException e) {
            log.error("Entity not found while creating plan: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Required entity not found: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            log.error("유효하지 않은 값으 들어왔습니다. : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());

        } catch (Exception e) {
            log.error("plan을 생성하면서 예상하지 못한 에러가 발생했습니다. : ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("plan을 생성하면서 예상하지 못한 에러가 발생했습니다.");
        }
    }

    @GetMapping("/{planId}")
    public ResponseEntity<?> getPlanWithDetails(@PathVariable Long planId, HttpServletRequest request) {
        try {
            User me = userService.findUserByJWTToken(request);

            if (me == null) {
                log.warn("JWT token 이 만료되었거나 유효하지 않습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("JWT token 이 만료되었거나 유효하지 않습니다.");
            }

            PlanDTO details = planService.getPlanWithDetails(planId);

            Map<String, Object> response = new HashMap<>();
            response.put("id", details.getId());
            response.put("title", details.getTitle());
            response.put("startDate", details.getStartDate().toString());
            response.put("endDate", details.getEndDate().toString());
            response.put("userId", details.getUserId());

            if (details.getDetails() != null && !details.getDetails().isEmpty()) {
                List<Map<String, Object>> detailsList = new ArrayList<>();
                for (PlanDetailDTO detail : details.getDetails()) {
                    Map<String, Object> detailMap = new HashMap<>();
                    detailMap.put("id", detail.getId());
                    detailMap.put("day", detail.getDay());
                    detailMap.put("arrivalTime", detail.getArrivalTime() != null ? detail.getArrivalTime().toString() : null);
                    detailMap.put("departureTime", detail.getDepartureTime() != null ? detail.getDepartureTime().toString() : null); // 수정됨
                    detailMap.put("planDetailName", detail.getPlanDetailName());
                    detailMap.put("memo", detail.getMemo());
                    detailMap.put("latitude", detail.getLatitude());
                    detailMap.put("longitude", detail.getLongitude());
                    detailMap.put("address", detail.getAddress());
                    if (detail.getTransportFromPrevious() != null) {
                        Map<String, Object> transportMap = new HashMap<>();
                        transportMap.put("id", detail.getTransportFromPrevious().getId());
                        transportMap.put("name", detail.getTransportFromPrevious().getTransportName());
                        detailMap.put("transportFromPrevious", transportMap);
                    } else {
                        detailMap.put("transportFromPrevious", null);
                    }
                    detailsList.add(detailMap);
                }
                response.put("details", detailsList);
            }

            log.info("Plan details를 가져오는데 성공했습니다 : {}", planId);
            return ResponseEntity.ok(response); // 수정됨

        } catch (EntityNotFoundException e) {
            log.error("Plan이 없습니다 {}", planId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Plan을 찾을 수 없습니다: " + planId);

        } catch (Exception e) {
            log.error("예상하지 못한 에러가 발생했습니다.: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("예상하지 못한 에러가 발생했습니다.");
        }
    }

    @PutMapping("/{planId}")
    public ResponseEntity<?> updatePlan(@PathVariable Long planId,@RequestBody PlanDTO planDTO, HttpServletRequest request) {
        try {
            User me = userService.findUserByJWTToken(request);
            if (me == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "JWT토큰이 유효하지 않습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            // 서비스 호출
            PlanDTO updated = planService.updatePlan(planId, planDTO, me.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("id", updated.getId());
            response.put("title", updated.getTitle());
            response.put("startDate", updated.getStartDate().toString());
            response.put("endDate", updated.getEndDate().toString());
            response.put("memberCount", updated.getMemberCount());
            response.put("isPublic", updated.getIsPublic());
            response.put("thumbnail", updated.getThumbnail());
            response.put("region", updated.getRegion());
            response.put("userId", updated.getUserId());

            if (updated.getDetails() != null && !updated.getDetails().isEmpty()) {
                List<Map<String, Object>> detailsList = new ArrayList<>();
                for (PlanDetailDTO detail : updated.getDetails()) {
                    Map<String, Object> detailMap = new HashMap<>();
                    detailMap.put("id", detail.getId());
                    detailMap.put("day", detail.getDay());
                    detailMap.put("arrivalTime", detail.getArrivalTime() != null ? detail.getArrivalTime().toString() : null);
                    detailMap.put("departureTime", detail.getDepartureTime() != null ? detail.getDepartureTime().toString() : null);
                    detailMap.put("planDetailName", detail.getPlanDetailName());
                    detailMap.put("memo", detail.getMemo());
                    detailMap.put("latitude", detail.getLatitude());
                    detailMap.put("longitude", detail.getLongitude());
                    detailMap.put("address", detail.getAddress());

                    if (detail.getTransportFromPrevious() != null) {
                        Map<String, Object> transportMap = new HashMap<>();
                        transportMap.put("id", detail.getTransportFromPrevious().getId());
                        transportMap.put("name", detail.getTransportFromPrevious().getTransportName());
                        detailMap.put("transportFromPrevious", transportMap);
                    } else {
                        detailMap.put("transportFromPrevious", null);
                    }
                    detailsList.add(detailMap);
                }
                response.put("details", detailsList);
            }
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Plan을 찾을 수 없습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "예상치 못한 에러: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}