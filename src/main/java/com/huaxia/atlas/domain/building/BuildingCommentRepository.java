package com.huaxia.atlas.domain.building;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingCommentRepository extends JpaRepository<BuildingComment, Long> {

    List<BuildingComment> findByBuildingIdOrderByCreatedAtDesc(Long buildingId);

    long countByBuildingId(Long buildingId);
}
