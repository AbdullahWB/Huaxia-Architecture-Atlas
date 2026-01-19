package com.huaxia.atlas.web.admin;

import com.huaxia.atlas.domain.user.UnbanRequestService;
import com.huaxia.atlas.domain.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/unban-requests")
public class AdminUnbanRequestController {

    private final UnbanRequestService unbanRequestService;
    private final UserService userService;

    public AdminUnbanRequestController(UnbanRequestService unbanRequestService, UserService userService) {
        this.unbanRequestService = unbanRequestService;
        this.userService = userService;
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable("id") Long id, RedirectAttributes ra) {
        boolean ok = unbanRequestService.approve(id, userService);
        ra.addFlashAttribute(ok ? "success" : "error", ok ? "Unban request approved." : "Unable to approve request.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable("id") Long id, RedirectAttributes ra) {
        boolean ok = unbanRequestService.reject(id);
        ra.addFlashAttribute(ok ? "success" : "error", ok ? "Unban request rejected." : "Unable to reject request.");
        return "redirect:/admin/dashboard";
    }
}
