package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.order.OrderService;
import com.huaxia.atlas.domain.product.ProductInteractionService;
import com.huaxia.atlas.domain.product.ProductService;
import com.huaxia.atlas.domain.product.dto.ProductReviewForm;
import com.huaxia.atlas.domain.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    private final ProductInteractionService productInteractionService;

    public ProductController(ProductService productService,
                             OrderService orderService,
                             UserService userService,
                             ProductInteractionService productInteractionService) {
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
        this.productInteractionService = productInteractionService;
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

    @GetMapping("/{id}")
    public String detail(
            @PathVariable("id") Long id,
            Authentication authentication,
            Model model) {
        var product = productService.get(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var reviews = productInteractionService.listReviews(id);
        var avgRating = productInteractionService.averageRating(id);
        var likeCount = productInteractionService.countLikes(id);

        boolean liked = false;
        if (authentication != null && authentication.isAuthenticated()) {
            var userOpt = userService.findByLogin(authentication.getName());
            if (userOpt.isPresent()) {
                liked = productInteractionService.hasLiked(id, userOpt.get().getId());
            }
        }

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", avgRating);
        model.addAttribute("reviewCount", productInteractionService.countReviews(id));
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("liked", liked);
        model.addAttribute("reviewForm", new ProductReviewForm());
        return "public/product-detail";
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

    @PostMapping("/{id}/review")
    public String addReview(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("reviewForm") ProductReviewForm form,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("error", "Please add a rating (1-5) and a review.");
            return "redirect:/products/" + id;
        }

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        try {
            productInteractionService.addReview(id, user.getId(), form.getRating(), form.getContent());
            ra.addFlashAttribute("success", "Review submitted.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/products/" + id;
    }

    @PostMapping("/{id}/like")
    public String toggleLike(
            @PathVariable("id") Long id,
            Authentication authentication,
            RedirectAttributes ra) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        boolean liked = productInteractionService.toggleLike(id, user.getId());
        ra.addFlashAttribute("success", liked ? "Product liked." : "Like removed.");
        return "redirect:/products/" + id;
    }
}
