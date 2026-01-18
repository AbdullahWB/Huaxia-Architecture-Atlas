package com.huaxia.atlas.ai.search;

import java.util.List;

public record SearchResult(
        Long buildingId,
        String name,
        double score,
        List<String> matchedFields,
        String snippet) {
}
