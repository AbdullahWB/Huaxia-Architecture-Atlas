package com.huaxia.atlas.web.admin;

import com.huaxia.atlas.ai.explain.AiExplainService;
import com.huaxia.atlas.domain.message.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/messages")
public class AdminMessageController {

    private final MessageService messageService;
    private final AiExplainService aiExplainService;

    public AdminMessageController(MessageService messageService, AiExplainService aiExplainService) {
        this.messageService = messageService;
        this.aiExplainService = aiExplainService;
    }

    @GetMapping
    public String list(
            @RequestParam(name = "isRead", required = false) Boolean isRead,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            Model model) {
        var result = messageService.listByReadStatus(isRead, page, size);
        model.addAttribute("items", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("isRead", isRead);
        return "admin/messages";
    }

    @PostMapping("/{id}/read")
    public String markRead(@PathVariable("id") Long id, RedirectAttributes ra) {
        messageService.setRead(id, true);
        ra.addFlashAttribute("success", "Marked as read.");
        return "redirect:/admin/messages";
    }

    @PostMapping("/{id}/unread")
    public String markUnread(@PathVariable("id") Long id, RedirectAttributes ra) {
        messageService.setRead(id, false);
        ra.addFlashAttribute("success", "Marked as unread.");
        return "redirect:/admin/messages";
    }

    @PostMapping("/{id}/reply")
    public String saveReply(
            @PathVariable("id") Long id,
            @RequestParam(name = "adminReply", required = false) String adminReply,
            RedirectAttributes ra) {
        if (adminReply == null || adminReply.trim().isEmpty()) {
            ra.addFlashAttribute("error", "Reply cannot be empty.");
            return "redirect:/admin/messages";
        }

        var saved = messageService.saveAdminReply(id, adminReply);
        if (saved.isEmpty()) {
            ra.addFlashAttribute("error", "Message not found.");
            return "redirect:/admin/messages";
        }

        messageService.setRead(id, true);
        ra.addFlashAttribute("success", "Reply saved.");
        return "redirect:/admin/messages";
    }

    @PostMapping("/{id}/reply/ai")
    public String generateAiReply(@PathVariable("id") Long id, RedirectAttributes ra) {
        var draft = aiExplainService.draftMessageReply(id);
        if (draft.isEmpty()) {
            ra.addFlashAttribute("error", "Message not found.");
            return "redirect:/admin/messages";
        }

        messageService.saveAiReply(id, draft.get());
        messageService.setRead(id, true);
        ra.addFlashAttribute("success", "AI reply generated.");
        return "redirect:/admin/messages";
    }
}
