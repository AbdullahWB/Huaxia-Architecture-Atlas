package com.huaxia.atlas.domain.building;

import com.huaxia.atlas.domain.user.UserAccount;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "building_likes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_building_like", columnNames = {"building_id", "user_id"})
        }
)
public class BuildingLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
