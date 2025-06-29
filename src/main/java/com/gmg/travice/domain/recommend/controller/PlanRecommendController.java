package com.gmg.travice.domain.recommend.controller;

import com.gmg.travice.domain.recommend.dto.RecommendRequestDTO;
import com.gmg.travice.domain.recommend.dto.RecommendResponseDTO;
import com.gmg.travice.domain.recommend.service.OpenAiRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanRecommendController {

    private final OpenAiRecommendService recommendService;

    @PostMapping("/recommend")
    public ResponseEntity<RecommendResponseDTO> recommend(
            @RequestBody RecommendRequestDTO requestDTO
            ) {
        // summary 정보를 꺼냄
        RecommendRequestDTO.Summary summary = requestDTO.getSummary();
        // 내부에서 프롬프트 빌드하고 openai 호출 -> 리턴된 String을 recommendation에 담아
        RecommendResponseDTO recommendation = recommendService.recommend(summary);

        return ResponseEntity.ok(recommendation);
    }
}
