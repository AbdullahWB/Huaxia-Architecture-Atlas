package com.huaxia.atlas.domain.building;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingLikeRepository extends JpaRepository<BuildingLike, Long> {

    Optional<BuildingLike> findByBuildingIdAndUserId(Long buildingId, Long userId);

    long countByBuildingId(Long buildingId);

    long countByUserId(Long userId);
}
