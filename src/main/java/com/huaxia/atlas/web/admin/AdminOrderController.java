package com.huaxia.atlas.web.admin;

import com.huaxia.atlas.domain.order.OrderStatus;
import com.huaxia.atlas.domain.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            Model model) {
        var result = orderService.listAll(page, size);
        model.addAttribute("items", result.getContent());
        model.addAttribute("page", result);
        return "admin/orders";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") OrderStatus status,
            RedirectAttributes ra) {
        orderService.updateStatus(id, status);
        ra.addFlashAttribute("success", "Order status updated.");
        return "redirect:/admin/orders";
    }
}
