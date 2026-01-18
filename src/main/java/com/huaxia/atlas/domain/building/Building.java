package com.huaxia.atlas.domain.building;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "buildings")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 100)
    private String dynasty;

    @Column(length = 200)
    private String location;

    @Column(length = 80)
    private String type;

    @Column(name = "year_built", length = 80)
    private String yearBuilt;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String tags; // comma-separated

    @Column(name = "cover_image", length = 300)
    private String coverImage;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    // --- getters/setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(String yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
