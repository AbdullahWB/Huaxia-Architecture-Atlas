package com.huaxia.atlas.ai.chat;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    private static final String HISTORY_KEY = "chatHistory";
    private static final int MAX_HISTORY = 25;

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public String chatPage(HttpSession session, Model model) {
        model.addAttribute("question", "");
        model.addAttribute("answer", "");
        model.addAttribute("lastQuestion", "");
        model.addAttribute("lastAnswer", "");
        model.addAttribute("lastPromptUsed", "");
        model.addAttribute("history", getHistory(session));
        return "public/chat";
    }

    @PostMapping("/chat")
    public String ask(
            @RequestParam(name = "question", required = false) String question,
            HttpSession session,
            Model model) {
        String q = question == null ? "" : question.trim();
        List<ChatMessage> history = getHistory(session);

        if (q.isEmpty()) {
            model.addAttribute("error", "Please enter a question.");
            model.addAttribute("question", "");
            model.addAttribute("answer", "");
            model.addAttribute("history", history);
            return "public/chat";
        }

        var result = chatService.ask(q);
        history.add(new ChatMessage(q, result.answer(), result.promptUsed(), Instant.now()));

        if (history.size() > MAX_HISTORY) {
            int start = history.size() - MAX_HISTORY;
            history.subList(0, start).clear();
        }

        model.addAttribute("question", "");
        model.addAttribute("answer", result.answer());
        model.addAttribute("lastQuestion", q);
        model.addAttribute("lastAnswer", result.answer());
        model.addAttribute("lastPromptUsed", result.promptUsed());
        model.addAttribute("promptUsed", result.promptUsed());
        model.addAttribute("history", history);

        return "public/chat";
    }

    @PostMapping("/chat/clear")
    public String clearHistory(HttpSession session) {
        session.removeAttribute(HISTORY_KEY);
        return "redirect:/chat";
    }

    @SuppressWarnings("unchecked")
    private List<ChatMessage> getHistory(HttpSession session) {
        Object value = session.getAttribute(HISTORY_KEY);
        if (value instanceof List<?> list) {
            return (List<ChatMessage>) list;
        }
        List<ChatMessage> history = new ArrayList<>();
        session.setAttribute(HISTORY_KEY, history);
        return history;
    }
}
