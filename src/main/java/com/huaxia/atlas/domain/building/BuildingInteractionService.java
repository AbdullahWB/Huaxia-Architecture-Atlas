package com.huaxia.atlas.domain.building;

import com.huaxia.atlas.domain.user.UserAccount;
import com.huaxia.atlas.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BuildingInteractionService {

    private final BuildingRepository buildingRepository;
    private final BuildingCommentRepository commentRepository;
    private final BuildingLikeRepository likeRepository;
    private final UserRepository userRepository;

    public BuildingInteractionService(
            BuildingRepository buildingRepository,
            BuildingCommentRepository commentRepository,
            BuildingLikeRepository likeRepository,
            UserRepository userRepository) {
        this.buildingRepository = buildingRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public List<BuildingComment> listComments(Long buildingId) {
        return commentRepository.findByBuildingIdOrderByCreatedAtAsc(buildingId);
    }

    public long countLikes(Long buildingId) {
        return likeRepository.countByBuildingId(buildingId);
    }

    public boolean hasLiked(Long buildingId, Long userId) {
        return likeRepository.findByBuildingIdAndUserId(buildingId, userId).isPresent();
    }

    @Transactional
    public void addComment(Long buildingId, Long userId, String content, Long parentId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BuildingComment comment = new BuildingComment();
        comment.setBuilding(building);
        comment.setUser(user);
        comment.setContent(content.trim());

        if (parentId != null) {
            BuildingComment parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
            if (!parent.getBuilding().getId().equals(buildingId)) {
                throw new IllegalArgumentException("Parent comment does not belong to this building");
            }
            comment.setParent(parent);
        }

        commentRepository.save(comment);
    }

    @Transactional
    public boolean toggleLike(Long buildingId, Long userId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var existing = likeRepository.findByBuildingIdAndUserId(buildingId, userId);
        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            return false;
        }

        BuildingLike like = new BuildingLike();
        like.setBuilding(building);
        like.setUser(user);
        likeRepository.save(like);
        return true;
    }
}
