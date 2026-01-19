package com.huaxia.atlas.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UnbanRequestForm {

    @NotBlank
    @Size(max = 200)
    private String login;

    @Size(max = 800)
    private String reason;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
