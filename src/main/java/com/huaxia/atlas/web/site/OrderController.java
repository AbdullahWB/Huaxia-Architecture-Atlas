package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.order.OrderService;
import com.huaxia.atlas.domain.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
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
}
