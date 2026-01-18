package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.post.Post;
import com.huaxia.atlas.domain.post.PostService;
import com.huaxia.atlas.domain.post.dto.PostCreateForm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model
    ) {
        var result = postService.listApproved(page, size);
        model.addAttribute("items", result.getContent());
        model.addAttribute("page", result);
        return "public/posts";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Post post = postService.getApproved(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("post", post);
        return "public/post-detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new PostCreateForm());
        return "public/post-new";
    }

    @PostMapping("/new")
    public String submit(
            @Valid @ModelAttribute("form") PostCreateForm form,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "public/post-new";
        }

        postService.create(form);
        ra.addFlashAttribute("success", "Your post has been submitted for approval.");
        return "redirect:/posts";
    }
}
