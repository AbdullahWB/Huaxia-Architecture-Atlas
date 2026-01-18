package com.huaxia.atlas.web.admin;

import com.huaxia.atlas.domain.message.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/messages")
public class AdminMessageController {

    private final MessageService messageService;

    public AdminMessageController(MessageService messageService) {
        this.messageService = messageService;
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
}
