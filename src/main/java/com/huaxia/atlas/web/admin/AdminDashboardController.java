package com.huaxia.atlas.web.admin;

import com.huaxia.atlas.domain.building.BuildingRepository;
import com.huaxia.atlas.domain.message.MessageService;
import com.huaxia.atlas.domain.order.OrderService;
import com.huaxia.atlas.domain.post.PostService;
import com.huaxia.atlas.domain.product.ProductRepository;
import com.huaxia.atlas.domain.user.UnbanRequestService;
import com.huaxia.atlas.domain.user.UserService;
import java.math.BigDecimal;
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
        long buildingCount = buildingRepository.count();
        long pendingPostCount = postService.countPending();
        long approvedPostCount = postService.countApproved();
        long unreadMessageCount = messageService.countUnread();
        long userCount = userService.countAll();
        long productCount = productRepository.count();
        long pendingOrderCount = orderService.countPending();
        BigDecimal totalRevenue = orderService.totalRevenue();

        model.addAttribute("buildingCount", buildingCount);
        model.addAttribute("pendingPostCount", pendingPostCount);
        model.addAttribute("unreadMessageCount", unreadMessageCount);
        model.addAttribute("userCount", userCount);
        model.addAttribute("productCount", productCount);
        model.addAttribute("pendingOrderCount", pendingOrderCount);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("systemHealthPercent", systemHealthPercent(buildingCount, approvedPostCount, productCount, totalRevenue));
        model.addAttribute("recentUsers", userService.recentUsers());
        model.addAttribute("unbanRequests", unbanRequestService.latestPending(6));
        return "admin/dashboard";
    }

    private int systemHealthPercent(long buildingCount, long approvedPostCount, long productCount, BigDecimal totalRevenue) {
        int signals = 0;
        if (buildingCount > 0) {
            signals++;
        }
        if (approvedPostCount > 0) {
            signals++;
        }
        if (productCount > 0) {
            signals++;
        }
        if (totalRevenue != null && totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            signals++;
        }
        return progress(signals, 4);
    }

    private int progress(long count, int max) {
        if (max <= 0) {
            return 0;
        }
        long value = Math.round((double) count * 100 / max);
        if (value < 0) {
            return 0;
        }
        if (value > 100) {
            return 100;
        }
        return (int) value;
    }
}
