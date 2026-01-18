package com.huaxia.atlas.domain.user.dto;

import jakarta.validation.constraints.Size;

public class UserProfileForm {

    @Size(max = 800)
    private String bio;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
