package com.gmg.travice.domain.recommend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecommendRequestDTO {

    // 프론트에서 보낸 summary 객체를 통채로 담음
    private Summary summary;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Summary {
        private int duration;
        private int people;
        private String purpose;
        private String region;
        private List<String> transports;
        private String concept;
        private String mbti;
    }
}
