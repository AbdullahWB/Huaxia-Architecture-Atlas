package com.huaxia.atlas.web.admin;

import com.huaxia.atlas.domain.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{id}/ban")
    public String ban(@PathVariable("id") Long id, RedirectAttributes ra) {
        boolean ok = userService.setEnabled(id, false);
        ra.addFlashAttribute(ok ? "success" : "error", ok ? "User banned." : "Unable to ban this account.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/{id}/unban")
    public String unban(@PathVariable("id") Long id, RedirectAttributes ra) {
        boolean ok = userService.setEnabled(id, true);
        ra.addFlashAttribute(ok ? "success" : "error", ok ? "User re-enabled." : "Unable to unban this account.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        boolean ok = userService.deleteUser(id);
        ra.addFlashAttribute(ok ? "success" : "error", ok ? "User deleted." : "Unable to delete this account.");
        return "redirect:/admin/dashboard";
    }
}
