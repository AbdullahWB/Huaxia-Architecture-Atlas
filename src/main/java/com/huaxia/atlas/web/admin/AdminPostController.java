package com.huaxia.atlas.web.admin;

import com.huaxia.atlas.domain.post.PostStatus;
import com.huaxia.atlas.domain.post.PostService;
import com.huaxia.atlas.domain.post.dto.PostModerationForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/posts")
public class AdminPostController {

    private final PostService postService;

    public AdminPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String list(
            @RequestParam(name = "status", defaultValue = "PENDING") PostStatus status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            Model model) {
        var result = postService.listByStatus(status, page, size);
        model.addAttribute("items", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("status", status);
        model.addAttribute("moderationForm", new PostModerationForm());
        return "admin/posts";
    }

    @PostMapping("/{id}/moderate")
    public String moderate(
            @PathVariable("id") Long id,
            @RequestParam(name = "status") PostStatus status,
            RedirectAttributes ra) {
        PostModerationForm form = new PostModerationForm();
        form.setStatus(status);

        postService.moderate(id, form);
        ra.addFlashAttribute("success", "Post updated to: " + status);
        return "redirect:/admin/posts?status=PENDING";
    }
}
