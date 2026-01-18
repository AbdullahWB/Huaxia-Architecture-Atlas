package com.huaxia.atlas.ai.chat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public String chatPage(Model model) {
        model.addAttribute("question", "");
        model.addAttribute("answer", "");
        return "public/chat";
    }

    @PostMapping("/chat")
    public String ask(
            @RequestParam(name = "question", required = false) String question,
            Model model) {
        var result = chatService.ask(question);

        model.addAttribute("question", question == null ? "" : question.trim());
        model.addAttribute("answer", result.answer());
        // Optional: show what context/prompt was sent (useful for demo)
        model.addAttribute("promptUsed", result.promptUsed());

        return "public/chat";
    }
}
