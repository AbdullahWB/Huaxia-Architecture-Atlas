package com.huaxia.atlas.web.admin;

import com.huaxia.atlas.domain.building.BuildingRepository;
import com.huaxia.atlas.domain.message.MessageService;
import com.huaxia.atlas.domain.order.OrderService;
import com.huaxia.atlas.domain.post.PostService;
import com.huaxia.atlas.domain.product.ProductRepository;
import com.huaxia.atlas.domain.user.UnbanRequestService;
import com.huaxia.atlas.domain.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    private final BuildingRepository buildingRepository;
    private final PostService postService;
    private final MessageService messageService;
    private final UserService userService;
    private final UnbanRequestService unbanRequestService;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public AdminDashboardController(
            BuildingRepository buildingRepository,
            PostService postService,
            MessageService messageService,
            UserService userService,
            UnbanRequestService unbanRequestService,
            ProductRepository productRepository,
            OrderService orderService) {
        this.buildingRepository = buildingRepository;
        this.postService = postService;
        this.messageService = messageService;
        this.userService = userService;
        this.unbanRequestService = unbanRequestService;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    @GetMapping("/admin/login")
    public String loginPage() {
        return "redirect:/login";
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
        model.addAttribute("userCount", userService.countAll());
        model.addAttribute("productCount", productRepository.count());
        model.addAttribute("pendingOrderCount", orderService.countPending());
        model.addAttribute("totalRevenue", orderService.totalRevenue());
        model.addAttribute("recentUsers", userService.recentUsers());
        model.addAttribute("unbanRequests", unbanRequestService.latestPending(6));
        return "admin/dashboard";
    }
}
