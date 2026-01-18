package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.message.MessageService;
import com.huaxia.atlas.domain.message.dto.ContactForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    private final MessageService messageService;

    public ContactController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/contact")
    public String contactForm(Model model) {
        model.addAttribute("form", new ContactForm());
        return "public/contact";
    }

    @PostMapping("/contact")
    public String submit(
            @Valid @ModelAttribute("form") ContactForm form,
            BindingResult bindingResult,
            RedirectAttributes ra
    ) {
        if (bindingResult.hasErrors()) {
            return "public/contact";
        }

        messageService.create(form);
        ra.addFlashAttribute("success", "Message sent. Admin will review it soon.");
        return "redirect:/contact";
    }
}
