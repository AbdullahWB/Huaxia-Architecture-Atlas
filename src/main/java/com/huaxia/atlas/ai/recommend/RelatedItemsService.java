package com.huaxia.atlas.ai.recommend;

import com.huaxia.atlas.domain.building.Building;
import com.huaxia.atlas.domain.building.BuildingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelatedItemsService {

    private final BuildingRepository buildingRepository;

    public RelatedItemsService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    /**
     * Recommend buildings related to the given building:
     * - Tag overlap (strong weight)
     * - Same dynasty/type boosts
     */
    @Transactional(readOnly = true)
    public List<Building> relatedTo(Building base, int limit) {
        if (base == null || base.getId() == null)
            return List.of();

        int topN = Math.max(1, Math.min(limit, 12));

        Set<String> baseTags = splitTags(base.getTags());
        String baseDynasty = norm(base.getDynasty());
        String baseType = norm(base.getType());

        List<Building> all = buildingRepository.findAll();

        return all.stream()
                .filter(b -> b.getId() != null && !b.getId().equals(base.getId()))
                .map(b -> new Scored(b, score(baseTags, baseDynasty, baseType, b)))
                .filter(s -> s.score > 0)
                .sorted(Comparator.comparingDouble((Scored s) -> s.score).reversed())
                .limit(topN)
                .map(s -> s.building)
                .collect(Collectors.toList());
    }

    private double score(Set<String> baseTags, String baseDynasty, String baseType, Building other) {
        double score = 0.0;

        Set<String> otherTags = splitTags(other.getTags());
        int overlap = 0;
        for (String t : baseTags) {
            if (otherTags.contains(t))
                overlap++;
        }
        score += overlap * 3.0;

        if (!baseDynasty.isEmpty() && baseDynasty.equals(norm(other.getDynasty())))
            score += 1.2;
        if (!baseType.isEmpty() && baseType.equals(norm(other.getType())))
            score += 1.2;

        // small boost if location shares a token
        if (!norm(other.getLocation()).isEmpty() && !norm(other.getLocation()).isEmpty()) {
            if (norm(other.getLocation()).contains(firstToken(norm(other.getLocation()))))
                score += 0.2;
        }

        return score;
    }

    private Set<String> splitTags(String tags) {
        if (tags == null || tags.isBlank())
            return Set.of();
        String[] parts = tags.split(",");
        Set<String> out = new HashSet<>();
        for (String p : parts) {
            String t = norm(p);
            if (!t.isEmpty())
                out.add(t);
        }
        return out;
    }

    private String norm(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    private String firstToken(String s) {
        if (s == null)
            return "";
        String[] parts = s.split("\\s+");
        return parts.length > 0 ? parts[0] : "";
    }

    private static class Scored {
        final Building building;
        final double score;

        Scored(Building building, double score) {
            this.building = building;
            this.score = score;
        }
    }
}
