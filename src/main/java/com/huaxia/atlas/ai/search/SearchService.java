package com.huaxia.atlas.ai.search;

import com.huaxia.atlas.domain.building.Building;
import com.huaxia.atlas.domain.building.BuildingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final BuildingRepository buildingRepository;

    public SearchService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    /**
     * Ranked search (deterministic "AI"):
     * - Score by token overlap with field weights:
     * name (3.0), tags (2.0), dynasty/type/location (1.2), description (1.0)
     * - Return top N with "matchedFields" and a short snippet.
     */
    @Transactional(readOnly = true)
    public List<SearchResult> search(String query, int limit) {
        String q = query == null ? "" : query.trim();
        if (q.isEmpty())
            return List.of();

        int topN = Math.max(1, Math.min(limit, 50));
        Set<String> qTokens = tokenize(q);

        // For a class project dataset, loading all buildings is acceptable.
        // If your dataset gets large, we’ll narrow candidates using repository queries.
        List<Building> candidates = buildingRepository.findAll();

        return candidates.stream()
                .map(b -> scoreOne(b, qTokens))
                .filter(r -> r.score() > 0.0)
                .sorted(Comparator.comparingDouble(SearchResult::score).reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }

    private SearchResult scoreOne(Building b, Set<String> qTokens) {
        double score = 0.0;
        List<String> matchedFields = new ArrayList<>();

        score += scoreField(b.getName(), qTokens, 3.0, matchedFields, "name");
        score += scoreField(b.getTags(), qTokens, 2.0, matchedFields, "tags");
        score += scoreField(b.getDynasty(), qTokens, 1.2, matchedFields, "dynasty");
        score += scoreField(b.getType(), qTokens, 1.2, matchedFields, "type");
        score += scoreField(b.getLocation(), qTokens, 1.2, matchedFields, "location");
        score += scoreField(b.getDescription(), qTokens, 1.0, matchedFields, "description");

        String snippet = buildSnippet(b.getDescription(), qTokens);

        return new SearchResult(
                b.getId(),
                b.getName(),
                round(score, 3),
                matchedFields,
                snippet);
    }

    private double scoreField(String text, Set<String> qTokens, double weight,
            List<String> matchedFields, String fieldName) {
        if (text == null || text.isBlank())
            return 0.0;

        Set<String> fieldTokens = tokenize(text);
        int overlap = 0;
        for (String t : qTokens) {
            if (fieldTokens.contains(t))
                overlap++;
        }
        if (overlap > 0 && !matchedFields.contains(fieldName)) {
            matchedFields.add(fieldName);
        }
        // overlap * weight, with a small bonus for exact substring match (query
        // appears)
        double base = overlap * weight;
        return base;
    }

    private Set<String> tokenize(String s) {
        if (s == null)
            return Set.of();
        String cleaned = s.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\u4e00-\\u9fff\\s,.-]", " ");
        String[] parts = cleaned.split("[\\s,.-]+");

        Set<String> tokens = new HashSet<>();
        for (String p : parts) {
            String t = p.trim();
            if (t.length() >= 2)
                tokens.add(t);
        }
        return tokens;
    }

    private String buildSnippet(String description, Set<String> qTokens) {
        if (description == null || description.isBlank())
            return "";
        String d = description.strip();
        // Keep it short for UI
        int maxLen = 160;
        if (d.length() <= maxLen)
            return d;

        // Try to center around the first matching token (basic heuristic)
        String lower = d.toLowerCase(Locale.ROOT);
        int pos = -1;
        for (String t : qTokens) {
            int idx = lower.indexOf(t.toLowerCase(Locale.ROOT));
            if (idx >= 0) {
                pos = idx;
                break;
            }
        }
        if (pos < 0)
            return d.substring(0, maxLen).strip() + "...";

        int start = Math.max(0, pos - 40);
        int end = Math.min(d.length(), start + maxLen);
        String slice = d.substring(start, end).strip();
        return (start > 0 ? "..." : "") + slice + (end < d.length() ? "..." : "");
    }

    private double round(double v, int decimals) {
        double p = Math.pow(10, decimals);
        return Math.round(v * p) / p;
    }
}
