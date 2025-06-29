package com.gmg.travice.domain.recommend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmg.travice.domain.recommend.dto.RecommendRequestDTO;
import com.gmg.travice.domain.recommend.dto.RecommendResponseDTO;
import com.gmg.travice.domain.recommend.util.OpenAiPromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiRecommendService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public RecommendResponseDTO recommend(RecommendRequestDTO.Summary summary) {
        // 1) 프롬프트 조립
        String prompt = OpenAiPromptBuilder.buildPrompt(summary);
        log.debug("프롬프트:\n{}", prompt);
        log.debug("--------------------------------------------");

        // 2) OpenAI 요청 바디 만들기
        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful travel planner."),
                        Map.of("role", "user", "content", prompt)
                )
        );
        log.debug("요청 바디:\n{}", body);
        log.debug("-------------------------------------------");

        // 3) WebClient 호출
        String rawJson = webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)   // ★ String으로 받아와야 JSON 전체를 다룰 수 있음
                .block();
        log.debug("▶ Raw JSON Response:\n{}", rawJson);


        // 4) 응답 파싱
        try {
            // 4) rawJson → JsonNode 로 파싱
            JsonNode root = objectMapper.readTree(rawJson);

            // 5) choices[0].message.content 에 담긴 텍스트(코드 펜스 포함) 추출
            String contentWithFences = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // 6) ```json ... ``` 펜스 제거
            String stripped = contentWithFences
                    .replaceAll("(?s)```json\\s*", "")   // ```json 와 공백 제거
                    .replaceAll("(?s)```$", "");         // 마지막 ``` 제거

            log.debug("▶ Inner JSON:\n{}", stripped);

            // 7) 최종적으로 RecommendResponseDto 로 역직렬화
            return objectMapper.readValue(stripped, RecommendResponseDTO.class);

        } catch (IOException e) {
            log.error("JSON 파싱 실패", e);
            throw new RuntimeException("추천 결과 처리에 실패했습니다.", e);
        }

    }
}