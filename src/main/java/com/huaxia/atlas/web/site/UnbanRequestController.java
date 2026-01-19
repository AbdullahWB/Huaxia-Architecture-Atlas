package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.user.UnbanRequestService;
import com.huaxia.atlas.domain.user.UserService;
import com.huaxia.atlas.domain.user.dto.UnbanRequestForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/unban")
public class UnbanRequestController {

    private final UserService userService;
    private final UnbanRequestService unbanRequestService;

    public UnbanRequestController(UserService userService, UnbanRequestService unbanRequestService) {
        this.userService = userService;
        this.unbanRequestService = unbanRequestService;
    }

    @GetMapping
    public String form(Model model) {
        model.addAttribute("form", new UnbanRequestForm());
        return "public/unban";
    }

    @PostMapping
    public String submit(
            @Valid @ModelAttribute("form") UnbanRequestForm form,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "public/unban";
        }

        var userOpt = userService.findByLogin(form.getLogin());
        if (userOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Account not found. Use your username or email.");
            return "redirect:/unban";
        }

        var user = userOpt.get();
        if (user.isEnabled()) {
            ra.addFlashAttribute("error", "This account is already active.");
            return "redirect:/unban";
        }

        if (unbanRequestService.hasPending(user.getId())) {
            ra.addFlashAttribute("success", "Your unban request is already pending.");
            return "redirect:/unban";
        }

        unbanRequestService.create(user, form.getReason());
        ra.addFlashAttribute("success", "Unban request submitted. We will review it soon.");
        return "redirect:/unban";
    }
}
