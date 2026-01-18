package com.huaxia.atlas.ai.chat;

import java.time.Instant;

public class ChatMessage {

    private final String question;
    private final String answer;
    private final String promptUsed;
    private final Instant createdAt;

    public ChatMessage(String question, String answer, String promptUsed, Instant createdAt) {
        this.question = question;
        this.answer = answer;
        this.promptUsed = promptUsed;
        this.createdAt = createdAt;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getPromptUsed() {
        return promptUsed;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
