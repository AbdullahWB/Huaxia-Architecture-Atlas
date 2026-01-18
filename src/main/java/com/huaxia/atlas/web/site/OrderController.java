package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.notification.UserNotificationService;
import com.huaxia.atlas.domain.order.OrderService;
import com.huaxia.atlas.domain.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final UserNotificationService notificationService;

    public OrderController(OrderService orderService, UserService userService, UserNotificationService notificationService) {
        this.orderService = orderService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public String list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Authentication authentication,
            Model model) {
        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var result = orderService.listByUser(user.getId(), page, size);
        model.addAttribute("items", result.getContent());
        model.addAttribute("page", result);
        return "public/orders";
    }

    @PostMapping("/{id}/pay")
    public String payOrder(
            @PathVariable("id") Long id,
            Authentication authentication,
            RedirectAttributes ra) {
        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        try {
            var order = orderService.markPaid(id, user.getId());
            notificationService.createNotification(
                    user.getId(),
                    "Payment received",
                    "Your payment for order #" + order.getId() + " was successful.");
            ra.addFlashAttribute("success", "Payment completed.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/orders";
    }
}
