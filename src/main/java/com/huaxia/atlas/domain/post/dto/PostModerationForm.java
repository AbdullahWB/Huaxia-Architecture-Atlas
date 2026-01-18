package com.huaxia.atlas.domain.post.dto;

import com.huaxia.atlas.domain.post.PostStatus;
import jakarta.validation.constraints.NotNull;

public class PostModerationForm {

    @NotNull
    private PostStatus status;

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }
}
