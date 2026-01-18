package com.huaxia.atlas.domain.building;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long> {

    // Explore filters (type + dynasty, partial match)
    Page<Building> findByTypeContainingIgnoreCaseAndDynastyContainingIgnoreCase(
            String type,
            String dynasty,
            Pageable pageable);

    // Optional: quick name search (useful for autocomplete or admin search)
    Page<Building> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
