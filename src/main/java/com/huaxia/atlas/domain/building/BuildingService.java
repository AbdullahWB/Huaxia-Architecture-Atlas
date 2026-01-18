package com.huaxia.atlas.domain.building;

import com.huaxia.atlas.domain.building.dto.BuildingForm;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BuildingService {

    private final BuildingRepository repo;

    public BuildingService(BuildingRepository repo) {
        this.repo = repo;
    }

    public Page<Building> list(String type, String dynasty, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id"));

        String t = normalizeForContains(type);
        String d = normalizeForContains(dynasty);

        return repo.findByTypeContainingIgnoreCaseAndDynastyContainingIgnoreCase(t, d, pageable);
    }

    public Optional<Building> get(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Building create(BuildingForm form) {
        Building b = new Building();
        applyForm(form, b);
        return repo.save(b);
    }

    @Transactional
    public Optional<Building> update(Long id, BuildingForm form) {
        return repo.findById(id).map(existing -> {
            applyForm(form, existing);
            return repo.save(existing);
        });
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ----------------- helpers -----------------

    private void applyForm(BuildingForm form, Building b) {
        b.setName(form.getName().trim());
        b.setDynasty(blankToNull(form.getDynasty()));
        b.setLocation(blankToNull(form.getLocation()));
        b.setType(blankToNull(form.getType()));
        b.setYearBuilt(blankToNull(form.getYearBuilt()));
        b.setDescription(blankToNull(form.getDescription()));
        b.setTags(blankToNull(form.getTags()));
        b.setCoverImage(blankToNull(form.getCoverImage()));
    }

    private String normalizeForContains(String s) {
        if (s == null)
            return "";
        String t = s.trim();
        return t.isEmpty() ? "" : t;
    }

    private String blankToNull(String s) {
        if (s == null)
            return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
