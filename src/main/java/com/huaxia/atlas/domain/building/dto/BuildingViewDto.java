package com.huaxia.atlas.domain.building.dto;

import com.huaxia.atlas.domain.building.Building;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record BuildingViewDto(
        Long id,
        String name,
        String dynasty,
        String location,
        String type,
        String yearBuilt,
        String description,
        List<String> tags,
        String coverImage) {
    public static BuildingViewDto from(Building b) {
        return new BuildingViewDto(
                b.getId(),
                b.getName(),
                b.getDynasty(),
                b.getLocation(),
                b.getType(),
                b.getYearBuilt(),
                b.getDescription(),
                splitTags(b.getTags()),
                b.getCoverImage());
    }

    private static List<String> splitTags(String tags) {
        if (tags == null || tags.isBlank())
            return List.of();
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }
}
