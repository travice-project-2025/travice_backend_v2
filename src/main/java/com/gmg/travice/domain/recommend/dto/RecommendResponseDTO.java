package com.gmg.travice.domain.recommend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecommendResponseDTO {
    private List<DayDTO> days;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DayDTO {
        private int day;
        private List<PlaceDTO> places;
        private List<TransportDTO> transports;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PlaceDTO {
        private String name;
        private String arrival;
        private String departure;
        private String activity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TransportDTO {
        private String type;
        private String duration;
    }
}
