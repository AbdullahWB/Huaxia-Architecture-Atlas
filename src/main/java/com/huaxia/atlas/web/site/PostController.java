package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.comment.dto.CommentForm;
import com.huaxia.atlas.domain.post.Post;
import com.huaxia.atlas.domain.post.PostInteractionService;
import com.huaxia.atlas.domain.post.PostService;
import com.huaxia.atlas.domain.post.dto.PostCreateForm;
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
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostInteractionService postInteractionService;
    private final UserService userService;

    public PostController(PostService postService,
                          PostInteractionService postInteractionService,
                          UserService userService) {
        this.postService = postService;
        this.postInteractionService = postInteractionService;
        this.userService = userService;
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
    public String detail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Post post = postService.getApproved(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("post", post);
        model.addAttribute("comments", postInteractionService.listComments(id));
        model.addAttribute("likeCount", postInteractionService.countLikes(id));
        model.addAttribute("commentForm", new CommentForm());

        boolean liked = false;
        if (authentication != null && authentication.isAuthenticated()) {
            var userOpt = userService.findByLogin(authentication.getName());
            if (userOpt.isPresent()) {
                liked = postInteractionService.hasLiked(id, userOpt.get().getId());
            }
        }
        model.addAttribute("liked", liked);
        return "public/post-detail";
    }

    @GetMapping("/new")
    public String newForm(Model model, Authentication authentication) {
        model.addAttribute("form", new PostCreateForm());
        if (authentication != null) {
            userService.findByLogin(authentication.getName())
                    .ifPresent(user -> model.addAttribute("currentUser", user));
        }
        return "public/post-new";
    }

    @PostMapping("/new")
    public String submit(
            @Valid @ModelAttribute("form") PostCreateForm form,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model,
            Authentication authentication
    ) {
        if (bindingResult.hasErrors()) {
            if (authentication != null) {
                userService.findByLogin(authentication.getName())
                        .ifPresent(user -> model.addAttribute("currentUser", user));
            }
            return "public/post-new";
        }

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        form.setAuthorName(user.getUsername());
        form.setAuthorEmail(user.getEmail());

        postService.create(form);
        ra.addFlashAttribute("success", "Your post has been submitted for approval.");
        return "redirect:/posts";
    }

    @PostMapping("/{id}/comment")
    public String addComment(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("commentForm") CommentForm form,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("error", "Comment cannot be empty.");
            return "redirect:/posts/" + id;
        }

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        postInteractionService.addComment(id, user.getId(), form.getContent());
        ra.addFlashAttribute("success", "Comment added.");
        return "redirect:/posts/" + id;
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

        boolean liked = postInteractionService.toggleLike(id, user.getId());
        ra.addFlashAttribute("success", liked ? "Post liked." : "Like removed.");
        return "redirect:/posts/" + id;
    }
}
