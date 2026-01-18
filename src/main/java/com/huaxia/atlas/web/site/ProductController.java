package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.order.OrderService;
import com.huaxia.atlas.domain.product.ProductService;
import com.huaxia.atlas.domain.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    public ProductController(ProductService productService,
                             OrderService orderService,
                             UserService userService) {
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "9") int size,
            Model model) {
        var result = productService.list(page, size);
        model.addAttribute("items", result.getContent());
        model.addAttribute("page", result);
        return "public/products";
    }

    @PostMapping("/{id}/buy")
    public String buy(
            @PathVariable("id") Long id,
            @RequestParam(name = "qty", defaultValue = "1") int qty,
            Authentication authentication,
            RedirectAttributes ra) {
        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        try {
            orderService.createOrder(user.getId(), id, qty);
            ra.addFlashAttribute("success", "Order placed. You can review it in your orders.");
            return "redirect:/orders";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/products";
        }
    }
}
