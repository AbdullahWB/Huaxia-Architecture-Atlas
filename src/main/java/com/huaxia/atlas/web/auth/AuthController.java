package com.huaxia.atlas.web.auth;

import com.huaxia.atlas.domain.user.UserService;
import com.huaxia.atlas.domain.user.dto.ForgotPasswordForm;
import com.huaxia.atlas.domain.user.dto.ResetPasswordForm;
import com.huaxia.atlas.domain.user.dto.UserRegisterForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "public/login";
    }

    @GetMapping("/register")
    public String registerForm(@ModelAttribute("form") UserRegisterForm form, Model model) {
        model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "form", new BeanPropertyBindingResult(form, "form"));
        return "public/register";
    }

    @PostMapping("/register")
    public String registerSubmit(
            @Valid @ModelAttribute("form") UserRegisterForm form,
            BindingResult bindingResult,
            RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "public/register";
        }

        try {
            userService.register(form);
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("register", ex.getMessage());
            return "public/register";
        }

        ra.addFlashAttribute("success", "Account created. Please login.");
        return "redirect:/login?registered";
    }

    @GetMapping("/forgot")
    public String forgotForm(@ModelAttribute("form") ForgotPasswordForm form, Model model) {
        model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "form", new BeanPropertyBindingResult(form, "form"));
        return "public/forgot-password";
    }

    @PostMapping("/forgot")
    public String forgotSubmit(
            @Valid @ModelAttribute("form") ForgotPasswordForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "public/forgot-password";
        }

        var tokenOpt = userService.createResetToken(form.getEmail());
        model.addAttribute("resetRequested", true);
        tokenOpt.ifPresent(token -> model.addAttribute("resetToken", token));
        return "public/forgot-password";
    }

    @GetMapping("/reset")
    public String resetForm(
            @RequestParam(name = "token", required = false) String token,
            @ModelAttribute("form") ResetPasswordForm form,
            Model model) {
        model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "form", new BeanPropertyBindingResult(form, "form"));
        model.addAttribute("token", token);
        if (token == null || token.isBlank() || !userService.isResetTokenValid(token)) {
            model.addAttribute("tokenInvalid", true);
        }
        return "public/reset-password";
    }

    @PostMapping("/reset")
    public String resetSubmit(
            @RequestParam("token") String token,
            @Valid @ModelAttribute("form") ResetPasswordForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("token", token);
            return "public/reset-password";
        }

        boolean updated = userService.resetPassword(token, form.getPassword());
        if (!updated) {
            model.addAttribute("token", token);
            model.addAttribute("tokenInvalid", true);
            return "public/reset-password";
        }

        ra.addFlashAttribute("success", "Password updated. Please login.");
        return "redirect:/login";
    }
}
