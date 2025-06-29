package com.gmg.travice.domain.recommend.util;

import com.gmg.travice.domain.recommend.dto.RecommendRequestDTO;

public class OpenAiPromptBuilder {

    // Summary 객체를 받아서 사람이 읽기 좋은 프롬프트 문자열로 조립함
    public static String buildPrompt(RecommendRequestDTO.Summary s) {
        // 1) 기본 헤더: 역할과 요청 형식 안내
        String header = """
                당신은 친절한 여행 가이드입니다.
                아래 여행 정보를 바탕으로, %d일간의 일정에 맞춰
                일자별 추천 코스를 한국어로 알려주세요.
                
                각 코스 사이의 이동은 제공된 이동수단 중에서만 선택해 주시고,
                현실적으로 타당한 소요시간(예: 자가용 30분, 도보 15분 등)을 반드시 포함해 주세요.
                
                """.formatted(s.getDuration());

        // 2) 여행 정보 리스트
        String transports = String.join(", ", s.getTransports());
        String info = """
                • 기간       : %d일
                • 인원       : %d명
                • 목적       : %s
                • 지역       : %s
                • 이동 수단  : %s
                • 컨셉       : %s
                • 여행 성향  : %s
                
                """.formatted(
                s.getDuration(),
                s.getPeople(),
                s.getPurpose(),
                s.getRegion(),
                transports,
                s.getConcept(),
                s.getMbti()
        );

        // 3) "n일차" 템플릿 동적으로 생성
        StringBuilder daysTemplate = new StringBuilder();
        for (int day = 1; day <= s.getDuration(); day++) {
            daysTemplate.append(String.format(
                    "[%d일차]\n" +
                            "- 장소1 (도착: HH:MM, 출발: HH:MM)\n" +
                            "- 이동수단: %s, 소요시간: ○○분\n" +
                            "- 장소2 (도착: HH:MM, 출발: HH:MM)\n\n",
                    day, transports
            ));
        }

        String jsonDirective = """
                
                **반드시** 아래 예시와 **같은 JSON 포맷**으로만 응답해 주세요.  
                - 자유 텍스트나 다른 설명은 넣지 마시고  
                - 키 이름, 중첩 구조, 배열 구조까지 정확히 지켜주세요.
                - 같은 키가 반복되지 않아야 합니다.
                
                {
                  "days": [
                    {
                      "day": 1,
                      "places": [
                        {
                          "name": "용두암",
                          "arrival": "10:00",
                          "departure": "11:00",
                          "activity": "제주 바다의 경치를 감상하며 산책"
                        },
                        {
                          "name": "이호테우 해변",
                          "arrival": "11:30",
                          "departure": "13:00",
                          "activity": "해변에서 조용히 산책하고 사진촬영"
                        },
                        {
                          "name": "산굼부리",
                          "arrival": "13:30",
                          "departure": "15:00",
                          "activity": "화산 분화구 주변을 걷고 자연의 아름다움 감상"
                        }
                      ],
                      "transports": [
                        {
                          "type": "도보",
                          "duration": "15분"
                        },
                        {
                          "type": "자전거",
                          "duration": "30분"
                        }
                      ]
                    },
                    {
                      "day": 2,
                      "places": [
                        {
                          "name": "성산일출봉",
                          "arrival": "09:00",
                          "departure": "11:00",
                          "activity": "일출봉 등반 및 경치 감상"
                        },
                        {
                          "name": "우도",
                          "arrival": "12:00",
                          "departure": "16:00",
                          "activity": "우도 탐방 및 우도 땅콩 아이스크림 맛보기"
                        }
                      ],
                      "transports": [
                        {
                          "type": "자가용",
                          "duration": "45분"
                        }
                      ]
                    }
                  ]
                }
                """;

        // 4) 최종 프롬프트
        return header
                + info
                + "아래 템플릿을 참고해서, 실제 가능한 시간과 이동수단을 반영해 일자별 일정을 작성해 주세요.\n\n"
                + daysTemplate
                + jsonDirective;
    }

}