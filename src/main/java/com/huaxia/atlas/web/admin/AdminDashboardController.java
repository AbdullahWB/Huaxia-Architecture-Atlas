package com.huaxia.atlas.web.admin;

import com.huaxia.atlas.domain.building.BuildingRepository;
import com.huaxia.atlas.domain.message.MessageService;
import com.huaxia.atlas.domain.post.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    private final BuildingRepository buildingRepository;
    private final PostService postService;
    private final MessageService messageService;

    public AdminDashboardController(
            BuildingRepository buildingRepository,
            PostService postService,
            MessageService messageService) {
        this.buildingRepository = buildingRepository;
        this.postService = postService;
        this.messageService = messageService;
    }

    // Login page (must be rendered by your app when using custom loginPage in
    // Spring Security)
    @GetMapping("/admin/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/admin")
    public String adminRoot() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("buildingCount", buildingRepository.count());
        model.addAttribute("pendingPostCount", postService.countPending());
        model.addAttribute("unreadMessageCount", messageService.countUnread());
        return "admin/dashboard";
    }
}
